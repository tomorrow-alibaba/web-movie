package com.rest;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.storage.ClientSessionStorage;
import com.storage.GlobalSessionStorage;
import com.storage.TokenStorage;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.api.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/user")
public class UserRest {

    HashMap<String, List<String>> globalTokenMap = GlobalSessionStorage.instance.getMap();
    HashMap<String, String> localTokenMap = ClientSessionStorage.instance.getMap();
    HashMap<String, User> tokenMap = TokenStorage.instance.getMap();
    HashMap<String, String> map = new HashMap<>();

    @RequestMapping(value = "/login", produces = "application/json", method = {RequestMethod.POST})
    @ResponseBody
    public String login(@RequestBody User user, HttpServletRequest request, HttpServletResponse response) {
        String token = UUID.randomUUID().toString();
        Cookie cookie = new Cookie("token", token);
        response.addCookie(cookie);
//        String cookieComment = response.getHeader("Set-Cookie");
//        if (cookieComment != null) {
//            cookieComment = "token=" + token + ";" + cookieComment;
//            response.setHeader("Set-Cookie", cookieComment);
//        }
        //JSESSIONID=C8386A6679F9A81E540C7CD444F10F9F; Path=/; HttpOnly
//        if (cookieComment != null && cookieComment != "") {
//            String[] keySetPair = cookieComment.split(";");
//            for (String pair : keySetPair) {
//                if (pair.contains("JSESSIONID")) {
//                    String regex = "JSESSIONID=(([0-9]|[A-Z])+)";
//                    Pattern pattern = Pattern.compile(regex);
//                    Matcher matcher = pattern.matcher(pair);
//                    if (matcher.find()) {
//                        response.setHeader("Cust-Cookie", pair.substring(pair.indexOf("JSESSIONID=") + 11));
//                    }
//                }
//            }
//        }

        String clientUrl = request.getParameter("redirect_url");
        // 子系统跳转过来的登录请求，授权、存储后，跳转回去
        if (clientUrl != null && !"".equals(clientUrl)) {
//            // 存储，用于注销
//            if (globalTokenMap.containsKey(token)) {
//                List<String> clientList = globalTokenMap.get(token);
//                clientList.add(clientUrl);
//            } else {
//                List<String> clientList = new ArrayList<>();
//                clientList.add(clientUrl);
//                globalTokenMap.put(token, clientList);
//            }
            tokenMap.put(token, user);
            if (clientUrl.contains("#")) {
                return "redirect:" + clientUrl.split("#")[0] + "?token=" + token + "#" + clientUrl.split("#")[1];
            }
            return "redirect:" + clientUrl + "?token=" + token;
        }
        //非子系统跳转请求
        return "redirect:/";
    }

    @RequestMapping(value = "/register", produces = "application/json", method = {RequestMethod.POST})
    @ResponseBody
    public String register(@RequestBody User user) {
        String a = "register ok";
        return user.getName() + user.getPassword() + a;
    }

    @RequestMapping(value = "/hello", produces = "application/json", method = {RequestMethod.GET})
    @ResponseBody
    public String hello() {
        String a = "hello ok";
        return a;
    }

    @RequestMapping(value = "/logout", produces = "application/json", method = {RequestMethod.POST})
    @ResponseBody
    public String logout(@RequestBody User user, HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }


    @RequestMapping(value = "/status", produces = "application/json", method = {RequestMethod.GET})
    @ResponseBody
    public String getStatus(HttpServletRequest request, HttpServletResponse response) {
        String a = "hello ok";
        return a;
    }

    @RequestMapping(value = "/sso", produces = "application/json", method = {RequestMethod.GET})
    @ResponseBody
    public String getSso(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        boolean hasClientSessionId = false;
        String token = "";
        if (cookies != null && cookies.length != 0) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    hasClientSessionId = true;
                    break;
                }
            }
        }
        if (hasClientSessionId) {
            String redirect = request.getParameter("redirect_url");
            try {
                response.sendRedirect(redirect + "?token=" + token);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                String redirect = request.getParameter("redirect_url");
                response.sendRedirect("/index.html?redirect_url=" + redirect);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "ok";
    }

    @RequestMapping(value = "/token", produces = "application/json", method = {RequestMethod.GET})
    @ResponseBody
    public User getToken(String token) {
        User user = tokenMap.get(token);
        return user;
    }
}
