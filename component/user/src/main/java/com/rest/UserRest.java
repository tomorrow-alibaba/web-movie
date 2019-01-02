package com.rest;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.api.User;

@Controller
@RequestMapping("/user")
public class UserRest {

	@RequestMapping(value = "/login", produces = "application/json;charset=utf-8", method = { RequestMethod.POST })
	@ResponseBody
	public String login(@RequestBody User user,HttpServletRequest req) {
		String a = "login ok";
		req.getSession().setAttribute("isLogin", true);
		return user.getName() + user.getPassword() + a;
	}

	@RequestMapping(value = "/register", produces = "application/json", method = { RequestMethod.POST })
	@ResponseBody
	public String register(@RequestBody User user) {
		String a = "register ok";
		return user.getName() + user.getPassword() + a;
	}

}
