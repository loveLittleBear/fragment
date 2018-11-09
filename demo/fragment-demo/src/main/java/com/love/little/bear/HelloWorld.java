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

        return hello;
    }
}
