package com.love.little.bear.fragment.core;

import org.apache.velocity.Template;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.tools.view.VelocityViewServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.Properties;

/**
 * @Author hanyang1
 * @Date 2018/7/30
 * @Description
 */
public class FragmentViewServlet extends VelocityViewServlet {

    @Override
    protected Template handleRequest(HttpServletRequest request,
                                     HttpServletResponse response, Context ctx) {
        // 初始化Velocity引擎
        VelocityEngine ve = new VelocityEngine();
        InputStream is = this.getClass().getResourceAsStream("/velocity.properties");
        Properties properties = new Properties();
        Template template = null;
        try {
            properties.load(is);
            ve.init(properties);
            template = ve.getTemplate("vm/task.vm");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 为模版中的元素赋值
        ctx.put("orders", "test1");
        return template;
    }

}
