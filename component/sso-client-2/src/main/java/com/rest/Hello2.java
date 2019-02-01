package com.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/client2")
public class Hello2 {

    @RequestMapping(value = "/hello", produces = "application/json", method = {RequestMethod.GET})
    @ResponseBody
    public String hello() {
        String a = "hello client2";
        return a;
    }

    @RequestMapping(value = "/logout", produces = "application/json", method = {RequestMethod.GET})
    @ResponseBody
    public String logout(HttpServletRequest request) {
        String token = request.getParameter("token");
        HttpSession clientSession = request.getSession();
        clientSession.invalidate();
        return "logout success client2";
    }

}
