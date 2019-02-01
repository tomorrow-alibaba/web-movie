package com.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/client1")
public class Hello {

    @RequestMapping(value = "/hello", produces = "application/json", method = {RequestMethod.GET})
    @ResponseBody
    public String hello() {
        String a = "hello client1";
        return a;
    }

    @RequestMapping(value = "/logout", produces = "application/json", method = {RequestMethod.GET})
    @ResponseBody
    public String logout(HttpServletRequest request) {
        String token = request.getParameter("token");
        HttpSession clientSession = request.getSession();
        clientSession.invalidate();
        return "logout success client1";
    }

}
