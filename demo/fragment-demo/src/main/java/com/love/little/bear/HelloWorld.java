package com.love.little.bear;

import com.love.little.bear.fragment.core.annotation.ExportMethod;
import com.love.little.bear.fragment.core.annotation.ExportService;
import org.springframework.stereotype.Service;

/**
 * Created by hanyang1 on 2017/10/23.
 */
@ExportService
@Service
public class HelloWorld {


    @ExportMethod
    public String sayHello(String hello){
        String s = "{\"code\":10000,\"msg\":null,\"data\":{\"id\":\"7aa0eb56-1026-4497-a42e-4c39f5e3dcf1\",\"topicId\":\"0876ab84-a478-417b-91bc-849843c191a5\",\"title\":null,\"commentId\":null,\"content\":\"" +
                "开发者平台自动化测试：针对帖子发表评论" +
                "\",\"images\":\"\",\"time\":\"2017-10-15 18:09:56\",\"userId\":\"61028f94-de92-4c65-aad3-2fc8614e1d34\",\"userName\":\"devautotest\",\"commentNum\":0,\"status\":0}}";
        return s;
    }
}
