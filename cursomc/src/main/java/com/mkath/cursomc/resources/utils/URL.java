package com.mkath.cursomc.resources.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class URL {

	public static List<Integer> decodeIntList(String value){
		if	(value == null || "".equalsIgnoreCase(value)) {
			return null;
		}
		String[] vet = value.split(",");
		List<Integer> list = new ArrayList<Integer>();
		for	(int i = 0; i < vet.length; i ++) {
			list.add(Integer.parseInt(vet[i]));
		}
		return list;
	}
	
	public static String decodeParam(String value) {
		try {
			return URLDecoder.decode(value, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}
}
