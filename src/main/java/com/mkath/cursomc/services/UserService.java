package com.mkath.cursomc.services;

import org.springframework.security.core.context.SecurityContextHolder;

import com.mkath.cursomc.security.UserSS;

public class UserService {

	public static UserSS getUserAuthenticated() {
		try {
			return (UserSS) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		} catch (Exception e) {
			return null;
		}
	}
}
