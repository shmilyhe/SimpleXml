package com.iekp.simplexml.test;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.iekp.simplexml.Ieach;
import com.iekp.simplexml.SimpleXml;

public class TestSimpleXml {

	@Test
	public void testsimpleXml() throws IOException{
		
		SimpleXml xml= SimpleXml.read(new File("D:\\projects\\SimpleXml\\resource\\test.xml"));
		
		System.out.println( xml.g("Ret").g("eric").getAttribute("name"));
		System.out.println( xml.g("Ret").g("eric").g("books").getParent().getQname());
		xml
		.g("Ret")
		.g("eric")
		.g("books")
		.g("book")
		.each(new Ieach(){
			public void each(int index,SimpleXml el){
				System.out.println(index);
				System.out.println(el.g("name").getText());
			}
		});
		
	}
}
