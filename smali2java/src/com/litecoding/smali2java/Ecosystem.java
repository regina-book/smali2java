package com.litecoding.smali2java;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import com.litecoding.smali2java.entity.smali.SmaliClass;
import com.litecoding.smali2java.entity.smali.SmaliMethod;
import com.litecoding.smali2java.parser.Parser;
import com.litecoding.smali2java.parser.Rule;
import com.litecoding.smali2java.renderer.ClassRenderer;
import com.litecoding.smali2java.renderer.SmaliRenderer;
import com.litecoding.smali2java.renderer.SmaliRenderer.Block;

public class Ecosystem
{
	private static Ecosystem instance = null;
	
	private Map<String, SmaliClass> classes = new HashMap<String, SmaliClass>();
	
	protected Ecosystem()
	{
		
	}
	
	public static Ecosystem getInstance()
	{
		if(instance == null)
			instance = new Ecosystem();
		return instance;
	}
	
	public Map<String, SmaliClass> getClasses()
	{
		return classes;
	}
	
	public void processFile(String src, String dst) throws Exception
	{
		File srcFile = new File(src);
		
		BufferedReader in = new BufferedReader(new FileReader(srcFile));
	    int ch = 0;
	    StringBuffer out = new StringBuffer();
	    while ((ch = in.read()) != -1)
	      out.append((char)ch);

	    out.append("\n"); //fix for the bug than .end method ends by EOF but not CRLF
	    in.close();
		
		Rule classrule = Parser.parse("smali", out.toString());
		SmaliClass smaliClass = (SmaliClass)classrule.accept(new SmaliClassBuilder());
		classes.put(smaliClass.getClassName(), smaliClass);
		
		Block block = null;
		
		//block = SmaliRenderer.generateBlocks(smaliClass.getMethods().get(0));
		//SmaliRenderer.printBlockChain(block);
		
		for(SmaliMethod method : smaliClass.getMethods()) {
			block = SmaliRenderer.generateBlocks(method);
			
			System.out.println("===[BEGIN OF BLOCK CHAIN]===");
			SmaliRenderer.printBlockChain(block);
			System.out.println("===[END OF BLOCK CHAIN]===");
			System.out.println();
		}
		
		System.out.println(ClassRenderer.renderObject(smaliClass));
	}
	
}
