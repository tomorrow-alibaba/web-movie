package com.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import com.api.User;

@Component
public class LoginInterceptor implements HandlerInterceptor {

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// TODO Auto-generated method stub
		// ÿһ����Ŀ���ڵ�½��ʵ���߼�����������������ʹ����򵥵�Session��ȡUser����֤��½��
		HttpSession session = request.getSession();
		System.out.println(session);
		// �����User�ǵ�½ʱ����session��
		User user = (User) session.getAttribute("user");
		// ���session��û��user����ʾû��½
		if (user == null) {
			// �����������false��ʾ���Ե�ǰ�������һ���û���������Ҫ��½����ʹ�õĽӿڣ������û�е�½�����ֱ�Ӻ��Ե�
			// ��Ȼ���������response���û�����һЩ��ʾ��Ϣ��������û��½
			return false;
		} else {
			System.out.println(user.getName() + user.getPassword());
			return true; // ���session����user����ʾ���û��Ѿ���½�����У��û����ɼ��������Լ���Ҫ�Ľӿ�
		}

	}

}
