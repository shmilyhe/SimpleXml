package com.iekp.simplexml.test;

public class TestSimpleXml2{

      
      /**
   * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		/**
		 * 创建 XML
		 */
		SimpleXml root =SimpleXml.read("<root><items></items></root>", "utf-8");
		SimpleXml items =root.g("root").g("items");
		for(int i=0;i<10;i++){
			items.addSub(SimpleXml.read("<item id='cc"+i+"' name='fff"+i+"'></item>","utf-8"));
		}
		
		System.out.println(root);
		
		
		/**
		 * 解析XML
		 */
		
		String xml =root.toString();
		
		SimpleXml rooElement= SimpleXml.read(xml, "utf-8");
		
		rooElement.g("root").g("items").g("item").each(new Ieach(){

			public void each(int index, SimpleXml el) {
				System.out.println("[第"+index+"个item元素]id:"+el.getAttribute("id")+";name:"+el.g("name").getText());
			}
			
		});
		
	}


}
