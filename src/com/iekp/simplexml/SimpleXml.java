/**
 * 
 * <h1>简单的XML元素类</h1>
 * 由XMLReader。read方法创建
 * 
 * 提供简单的XML操作方法
 * <root>
 * <books gropName="newbook">
 * 		<book name="book1"/>
 * 		<book name="book2"/>
 * 		<book name="book3"/>
 * 		<book>
 * 			<name>book4</name>
 * 		</book>
 * </books>
 * </root>
 * 1\获取 books的gropName属性
 * SimpleXml.read(xml,"utf-8").g("root").g("books").g("gropName").getText();
 * 或
 * SimpleXml.read(xml,"utf-8").g("root").g("books").getAttribute("gropName");
 * 
 * 遍历所有的BOOK
 * 
 * SimpleXml.read(xml,"utf-8").g("root").g("books").g("book")。each(new Ieach(){
			public void each(int index,XMLElement el){
				打印name属性
				System.out.println(el.g("name").getText());
			}
		});
 * 
 * 
 * 
 */

package com.iekp.simplexml;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class SimpleXml extends DefaultHandler{
	
	/**********************************************************************
	*                             XML解析相关                                                                                                  *
	**********************************************************************/
	private Stack<SimpleXml> tagNameStack = new Stack<SimpleXml>();
	private SimpleXml retEl;
	public static  SimpleXml read(File file) throws IOException{
		FileInputStream finp=null;
		try{finp = new FileInputStream(file);
		SimpleXml el=  new SimpleXml().readFrom(finp);
		return el;
		}catch(IOException e){
			return new SimpleXml();
		}finally{
			if(finp!=null) finp.close();
		}
	}
	public SimpleXml readFrom(InputStream inp){
		
		SAXParserFactory factory = SAXParserFactory.newInstance();  
		try {
			SAXParser parser = factory.newSAXParser();
		
			parser.parse(inp, this); 
		} catch (Exception e) {
			e.printStackTrace();
		} 
		this.addSub(retEl);
		return this;
	}
	public static SimpleXml read (String xml,String encoding){
		ByteArrayInputStream bdinp;
		try {
			bdinp = new  ByteArrayInputStream(xml.getBytes(encoding));
			return new SimpleXml().readFrom(bdinp);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new SimpleXml();
		}
		
		
		
		
	}
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		/**
		 * 上一层的元素
		 */
		SimpleXml pre =tagNameStack.empty()?null:tagNameStack.peek();
		
		SimpleXml cur =  new SimpleXml();
		cur.setQname(qName);
		if(retEl==null)retEl=cur;
		tagNameStack.push(cur);
		if(pre!=null){
			pre.addSub(cur);
		}
		int len =attributes.getLength();
		for(int i=0;i<len;i++){
			cur.setAttribute(attributes.getQName(i), attributes.getValue(i));
		}
		
		//System.out.println(""+qName+"开始!上一个元素:"+preName);
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if(!tagNameStack.empty()){
			tagNameStack.pop();
		}
		//System.out.println();
		
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		//tagNameStack.peek();
		tagNameStack.peek().setText(new String(ch,start,length));//(key, value)
	}

	@Override
	public void error(SAXParseException e) throws SAXException {
		super.error(e);
	}
	
	
	
	
	
	
	/**********************************************************************
	*                             XML节点属性和方法                                                                                                  *
	**********************************************************************/
	/**
	 * 文本节点
	 */
	public final int TYPE_TEXT=1;
	
	/**
	 * 属性节点
	 */
	public final int TYPE_ATT=2;
	
	/**
	 * 标签
	 */
	public final int TYPE_TAG=3;
	
	/**
	 * 文档
	 */
	public final int TYPE_DOC=4;
	
	/**
	 * 标签名
	 */
	private String qName;
	
	/**
	 * 标签的文本
	 */
	private String text;
	
	/**
	 * 标签的URI
	 */
	private String uri;
	
	/**
	 * 标签的类型
	 */
	private int type;
	
	/**
	 * 属性
	 */
	private HashMap<String,String> attributes;
	
	/**
	 * 子节点
	 */
	private List<SimpleXml> subElements;
	
	/**
	 * 选择器选中的同一层次的下一个元素
	 */
	private SimpleXml next;
	
	/**
	 * parent node
	 */
	private SimpleXml parent;
	
	public SimpleXml getParent() {
		return parent;
	}
	public void setParent(SimpleXml parent) {
		this.parent = parent;
	}
	/**
	 * TAG NAME
	 * @param qName
	 */
	public void setQname(String qName){
		this.qName=qName;
	}
	
	/**
	 * 设置TEXT 内容
	 * @param text
	 */
	public void setText(String text){
		this.text=text;
	}
	
	/**
	 * 获得节点的类型
	 * @return
	 */
	public int getType(){
		return type;
	}
	public String getQname(){
		return qName;
	}
	public SimpleXml(){}
	public SimpleXml(String uri,String qname,String text){
		this.uri=uri;
		this.qName=qname;
		this.text=text;
		type=TYPE_TAG;
		System.out.println("init:"+qname);
	}
	public SimpleXml(String qname,String text){
		this.qName=qname;
		this.text=text;
		type=TYPE_TAG;
		System.out.println("init:"+qname);
	}
	
	
	/**
	 * 增加属性
	 * @param key
	 * @param value
	 */
	public void setAttribute(String key,String value){
		if(attributes==null)attributes = new HashMap<String,String>();
		attributes.put(key, value);
	}
	
	
	/**
	 * 增加子元素
	 * @param el
	 */
	public void addSub(SimpleXml el){
		if(el==null)return;
		if(subElements==null)subElements = new ArrayList<SimpleXml>();
		subElements.add(el);
		el.setParent(this);
	}
	
	/**
	 * 获取属性
	 * @param aname
	 * @return
	 */
	public String getAttribute(String aname){
		if(attributes!=null)return attributes.get(aname);
		return null;
	}
	
	/**
	 * 获取文本
	 * @return
	 */
	public  String getText(){
		return text;
	}
	
	/**
	 * 获取URI
	 * @return
	 */
	public  String getUri(){
		return uri;
	}
	
	/**********************************************************************
	*                             XML操作相关方法                                                                                                  *
	**********************************************************************/
	
	/**
	 * 选择器
	 * @param name
	 * @return
	 */
	public SimpleXml g(String name){
		SimpleXml curr =this;
		SimpleXml head =null;
		SimpleXml flag =null;
		//System.out.println(name);
		while(curr!=null){
			//System.out.println(index++);
			SimpleXml rst =null;
			
			
		/**
		 * 从属性中查找 
		 */
		if(curr.attributes!=null&&curr.attributes.containsKey(name)){
			 rst = new SimpleXml();
			 rst.text=curr.attributes.get(name);
			 rst.type=TYPE_ATT;
			}
		
		
		if(rst!=null){
			if(head==null){
				head=rst;
				flag=rst;
			}else{
				flag.next=rst;
				flag =rst;
			}
		}
		rst=null;
		
		
		/**
		 * 查找子节点
		 * 
		 */
		if(curr.subElements!=null){
			for(SimpleXml el :curr.subElements){
				if(name.equals(el.qName)){
					//System.out.println(el);
					if(head==null){
						head=el;
						flag=el;
					}else{
						flag.next=el;
						flag =el;
					}
				}
			}
		}
		
		/**
		 * 从同级的元素中找
		 */
		curr=curr.next;
		}
		return head==null ? new  SimpleXml():head;
	}
	
	/**
	 * 迭代器
	 * @param each
	 */
	public void each(Ieach each){
		SimpleXml flag =this;
		int index=0;
		do{
			each.each(index,flag);
			index++;
			flag=flag.next;
		}while(flag!=null);
	}

}
