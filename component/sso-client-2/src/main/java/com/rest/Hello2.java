package com.rest;

import com.api.ClientSessionStorage;
import com.api.User;
import com.api.UserToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;

@Controller
@RequestMapping("/bookcase")
public class Hello2 {

    public HashMap<String, UserToken> tokenMap = ClientSessionStorage.instance.getMap();

    @RequestMapping(value = "/books", produces = "application/json", method = {RequestMethod.GET})
    @ResponseBody
    public String getBooks() {
        String a = "hello client2";
        return a;
    }

    @RequestMapping(value = "/user", produces = "application/json", method = {RequestMethod.GET})
    @ResponseBody
    public String getUserName(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length != 0) {
            for (Cookie cookie : cookies) {
                if ("CLIENTSESSIONID".equals(cookie.getName())) {
                    return tokenMap.get(cookie.getValue()).getUser().getName();
                }
            }
        }
        return null;
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
