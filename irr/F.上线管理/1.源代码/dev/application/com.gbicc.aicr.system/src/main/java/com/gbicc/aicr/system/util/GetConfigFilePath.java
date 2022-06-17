package com.gbicc.aicr.system.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 得到配置文件路径
 * 
 * @author ZCH
 * 
 */

public class GetConfigFilePath {
	public static final Log log = LogFactory.getLog(GetConfigFilePath.class);
	public final static String GBICCIRS_CONFIG_PATH = "GBICCIRS_HOME";
	private static Properties envVars = null;

	public static void main(String[] args) throws Exception {
		System.out.println("getConfigPath()1=========" + getConfigPath());
		System.out.println("getConfigPath()2=========" + getConfigPath());
	}

	private static Properties getOSEnv() throws IOException {
		if (envVars == null) {
			Process p = null;
			GetConfigFilePath.envVars = new Properties();
			Runtime r = Runtime.getRuntime();
			String OS = System.getProperty("os.name").toLowerCase();
			log.debug("OS=" + OS);

			if (OS.indexOf("windows 9") > -1) {
				p = r.exec("command.com /c set");
			} else if ((OS.indexOf("nt") > -1)
					|| (OS.indexOf("windows 20") > -1)
					|| (OS.indexOf("windows xp") > -1)
					|| (OS.indexOf("windows 8") > -1)
					|| (OS.indexOf("windows 7") > -1)) {
				p = r.exec("cmd.exe /c set");
			} else {
				p = r.exec("env");
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(p
					.getInputStream()));
			String line;
			while ((line = br.readLine()) != null) {
				int idx = line.indexOf('=');
				String key = line.substring(0, idx);
				String value = line.substring(idx + 1);
				envVars.setProperty(key, value);
				log.debug(key + " = " + value);
			}
		} else {
		}

		return envVars;
	}

	public static String getConfigPath() throws IOException {
		String configpath = "";
		getOSEnv();
		if (envVars != null) {
			configpath = envVars.getProperty(GBICCIRS_CONFIG_PATH) + "/cfg";
			File configpathFile = new File(configpath);
			if (!configpathFile.isDirectory()) {
				configpath = "c:/gbiccirshome/cfg";
			}
		} else {
			configpath = "c:/gbiccirshome/cfg";
		}
		return configpath;
	}

	public static String getExcelFilePath() throws IOException {
		String configpath = "";
		getOSEnv();
		if (envVars != null) {
			configpath = envVars.getProperty(GBICCIRS_CONFIG_PATH) + "/excel";
			File configpathFile = new File(configpath);
			if (!configpathFile.isDirectory()) {
				configpath = "c:/gbiccirshome/excel";
			}
		} else {
			configpath = "c:/gbiccirshome/excel";
		}
		return configpath;
	}

	public static String getImagePath() throws IOException {
		String imagepath = "";
		getOSEnv();
		if (envVars != null) {
			imagepath = envVars.getProperty(GBICCIRS_CONFIG_PATH) + "/image";
			File imagepathFile = new File(imagepath);
			if (!imagepathFile.isDirectory()) {
				imagepath = "c:/gbiccirshome/image";
			}
		} else {
			imagepath = "c:/gbiccirshome/image";
		}
		return imagepath;
	}

}
