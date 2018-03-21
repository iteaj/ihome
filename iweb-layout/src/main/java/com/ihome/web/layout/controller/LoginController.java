package com.ihome.web.layout.controller;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

/**
 * create time: 2018/3/6
 *
 * @author iteaj
 * @version 1.0
 * @since 1.8
 */
@Controller("LayoutLoginController")
@RequestMapping("/login")
public class LoginController {

    @ResponseBody
    @RequestMapping(value = "post")
    public Object loginPost(HttpServletRequest request) {
        ServletContext context = request.getServletContext();
        BundleContext bundleContext = (BundleContext)context.getAttribute("BundleContext");
        Bundle[] bundles = bundleContext.getBundles();
        return "login success";
    }

}
