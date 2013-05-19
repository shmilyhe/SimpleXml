package com.iekp.simplexml.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class TestUtils {

	
	public static String readFileAsString(String path){
		try {
			FileInputStream fis = new FileInputStream(path);
			StringBuilder sb = new StringBuilder();
			byte [] b = new byte[1024];
			int len =0;
			while((len=fis.read(b))>0){
				sb.append(new String(b,0,len,"utf-8"));
			}
			fis.close();
			return sb.toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
