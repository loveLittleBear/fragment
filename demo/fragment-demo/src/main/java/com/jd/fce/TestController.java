package com.jd.fce;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Author hanyang1
 * @Date 2018/7/27
 * @Description
 */
@Controller
public class TestController {

    @RequestMapping("/view.do")
    public ModelAndView view(){
        ModelAndView view = new ModelAndView("task.vm");
        return view;
    }
}
