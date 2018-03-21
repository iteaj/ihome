package com.ihome.web.layout.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletRequest;

/**
 * create time: 2018/3/11
 *
 * @author iteaj
 * @version 1.0
 * @since 1.8
 */
@Controller("LayoutTestController")
@RequestMapping("/test")
public class TestController {

    @RequestMapping(value = "post")
    public String loginPost(HttpServletRequest request) {

        return "test";
    }
}
