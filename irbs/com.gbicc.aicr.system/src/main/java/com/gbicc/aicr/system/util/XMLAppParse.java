package com.gbicc.aicr.system.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;


import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gbicc.aicr.system.geval.calculation.PriorityCalcQueue;
import com.gbicc.aicr.system.geval.function.Function;

public class XMLAppParse {
	private static final Logger log =LoggerFactory.getLogger(XMLAppParse.class);
	public static void main(String[] args) throws Exception {
		Object[] o = { "" };
		Function s = (Function) createClass(
				"com.gbicc.irs.geval.function.math.Abs", o);
		System.out.println(s.getName());
		XMLAppParse.getFuntionList();

	}

	public static List getFuntionList() throws FileNotFoundException {
		String cfgpath = "";
		try {
			cfgpath = GetConfigFilePath.getConfigPath();
		} catch (IOException e1) {
			log.error("Exception",e1);
			e1.printStackTrace();
		}
		File cfgpathfile = new File(cfgpath);
		if (!cfgpathfile.isDirectory()) {
			cfgpath = "C:/gbiccirshome";
		}

		File configFile = new File(cfgpath + "/CFG/gbiccfunction.xml");
		List functionlist = new ArrayList();
		if (configFile.isFile()) {
			FileInputStream configFileInputSteam = new FileInputStream(
					configFile);
			try {
				SAXReader reader = new SAXReader();
				Document doc = reader.read(configFileInputSteam);

				List applicationNodes = doc
						.selectNodes("/function-config/object");
				for (int i = 0; i < applicationNodes.size(); i++) {
					Element appElement = (Element) applicationNodes.get(i);
					String name = appElement.selectSingleNode("@name")
							.getText();
					String classname = appElement
							.selectSingleNode("@classname").getText();
					String type = appElement.selectSingleNode("@type")
							.getText();
					System.out.println("name===" + name + "/classname=="
							+ classname);
					FunctionCfg aconfig = new FunctionCfg();
					aconfig.setName(name);
					aconfig.setClassname(classname);
					aconfig.setType(type);
					functionlist.add(aconfig);

				}
			} catch (Exception e) {
				log.error("Exception",e);
			}
		}
		return functionlist;
	}

	public static Function getFunctionObj(String name) throws Exception {
		Object[] o = { "" };
		Function s = (Function) createClass(name, o);
		return s;
	}

	// 自动找到合适的构造方法并构造
	public static Object createClass(String name, Object[] o) throws Exception {
		Class myClass = Class.forName(name);
		Class[] argsClass = new Class[o.length];
		for (int i = 0; i < o.length; i++) {
			argsClass[i] = o[i].getClass();
		}
		try {
			Constructor cons = myClass.getConstructor(argsClass);
			return cons.newInstance(o);
		} catch (Exception e) {
			log.error("Exception",e);
			return myClass.newInstance();
		}

	}

}
