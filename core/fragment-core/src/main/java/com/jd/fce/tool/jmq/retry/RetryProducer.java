package com.jd.fce.tool.jmq.retry;

import com.alibaba.fastjson.JSONObject;
import com.jd.fce.tool.jmq.retry.dto.RetryMsgDto;
import com.jd.jmq.client.connection.ClusterTransportManager;
import com.jd.jmq.client.connection.TransportConfig;
import com.jd.jmq.client.connection.TransportManager;
import com.jd.jmq.client.producer.MessageProducer;
import com.jd.jmq.client.producer.ProducerConfig;
import com.jd.jmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RetryProducer {

    private final static Logger logger = LoggerFactory.getLogger(RetryProducer.class);

    private MessageProducer retryProducer;

    private String topic = "";
    private String app;
    private String user;
    private String password;
    private String address;
    private int sendTimeout = 150;
    private int retryNum = 2;

    private int poolSize = 10;
    private ExecutorService fixedThreadPool;

    private boolean needInit = true;

    {
        fixedThreadPool = Executors.newFixedThreadPool(poolSize);
        try {
            initProducer();
            needInit = false;
        } catch (Exception e) {
            logger.error("init RetryProducer error:", e);
        }
    }

    public void initProducer() throws Exception {
        TransportConfig transportConfig = new TransportConfig();
        transportConfig.setApp(app);
        transportConfig.setUser(user);
        transportConfig.setPassword(password);
        transportConfig.setAddress(address);
        transportConfig.setSendTimeout(sendTimeout);
        TransportManager transportManager = new ClusterTransportManager(transportConfig);

        ProducerConfig producerConfig = new ProducerConfig();
        producerConfig.setRetryTimes(retryNum);

        retryProducer = new MessageProducer();
        retryProducer.setConfig(producerConfig);
        retryProducer.setTransportManager(transportManager);

        transportManager.start();
        retryProducer.start();
    }

    public void sendRetryMsg(RetryMsgDto retryMsgDto) throws Exception {
        if(needInit){
            initProducer();
        }
        Message msg = new Message(topic, JSONObject.toJSONString(retryMsgDto), retryMsgDto.getBusiId());
        retryProducer.send(msg);
    }

    public void sendRetryMsgAsyn(final RetryMsgDto retryMsgDto){
        fixedThreadPool.submit(new Runnable() {
            public void run() {
                try {
                    sendRetryMsg(retryMsgDto);
                } catch (Exception e) {
                    logger.error("busiId:{} sendRetryMsgAsyn exception data:{}", retryMsgDto.getBusiId(), retryMsgDto, e);
                }
            }
        });
    }
}
