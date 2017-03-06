package org.ykc.bitpro;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javafx.scene.control.Tab;

public class Preferences {
	private static File lastDirectory = null;
	private static File lastLoadedFile = null;
	private static String lastOpenTabName = "tabCreate";
	private static String xSolveLastData = "0";
	private static String loadViewPrefixValue = "";
	private static String utilsFnNamePrefixString = "";
	private static String utilsFnPrefixString = "";
	private static String utilsFnPostfixString = "";
	
	public static String getUtilsFnNamePrefixString() {
		return utilsFnNamePrefixString;
	}

	public static void setUtilsFnNamePrefixString(String utilsFnNamePrefixString) {
		Preferences.utilsFnNamePrefixString = utilsFnNamePrefixString;
	}

	public static String getUtilsFnPrefixString() {
		return utilsFnPrefixString;
	}

	public static void setUtilsFnPrefixString(String utilsFnPrefixString) {
		Preferences.utilsFnPrefixString = utilsFnPrefixString;
	}

	public static String getUtilsFnPostfixString() {
		return utilsFnPostfixString;
	}

	public static void setUtilsFnPostfixString(String utilsFnPostfixString) {
		Preferences.utilsFnPostfixString = utilsFnPostfixString;
	}

	public static String getLoadViewPrefixValue() {
		return loadViewPrefixValue;
	}

	public static void setLoadViewPrefixValue(String loadViewPrefixValue) {
		Preferences.loadViewPrefixValue = loadViewPrefixValue;
	}

	public static String getxSolveLastData() {
		return xSolveLastData;
	}

	public static void setxSolveLastData(String xSolveLastData) {
		Preferences.xSolveLastData = xSolveLastData;
	}

	public static String getLastOpenTabName() {
		return lastOpenTabName;
	}

	public static void setLastOpenTabName(String lastOpenTabName) {
		Preferences.lastOpenTabName = lastOpenTabName;
	}

	public static File getLastDirectory()
	{
		return lastDirectory;
	}
	
	public static void setLastDirectory(File dir)
	{
		lastDirectory = dir;
	}
	
	public static File getLastLoadedFile()
	{
		return lastLoadedFile;
	}
	
	public static void setLastLoadedFile(File file)
	{
		lastLoadedFile = file;
	}	
	
	public static boolean storePreferences()
	{
	    try {
			File samplesDir = new File(System.getProperty("user.home"), "BitPro/preferences");
			if (! samplesDir.exists()) {
				samplesDir.mkdirs();
			}
			File prefFile = new File(System.getProperty("user.home"), "BitPro/preferences/app.xml");
			if (! prefFile.exists()) {
				prefFile.createNewFile();
			}
			return createPreferences(prefFile);
		} catch (Exception e) {
			
		}
		return false;
	}
	
	public static boolean loadPreferences(){
		
	    try {
			File prefFile = new File(System.getProperty("user.home"), "BitPro/preferences/app.xml");
			if (! prefFile.exists()) {
				return false;
			}
			org.w3c.dom.Document prefDoc = BProUtils.getDocument(prefFile);
			if(prefDoc == null)
			{
				return false;
			}
			String lastDir = prefDoc.getElementsByTagName("lastDirectory").item(0).getTextContent();
			if(!lastDir.equals("null"))
			{
				lastDirectory = new File(lastDir);
			}	
			lastOpenTabName = prefDoc.getElementsByTagName("lastOpenTab").item(0).getTextContent();		
			String fileName = prefDoc.getElementsByTagName("lastLoadFile").item(0).getTextContent();
			if(!fileName.equals("null"))
			{
				lastLoadedFile = new File(fileName);
			}	
			
			String xSolveDat = prefDoc.getElementsByTagName("xSolveLastData").item(0).getTextContent();
			if(xSolveDat != null)
			{
				xSolveLastData = xSolveDat;
			}
			String temp = prefDoc.getElementsByTagName("lastLoadViewPrefix").item(0).getTextContent();
			if(temp != null){
				loadViewPrefixValue = temp;
			}
			
			temp = prefDoc.getElementsByTagName("lastUtilsGen1FnNamePrefix").item(0).getTextContent();
			if(temp != null){
				utilsFnNamePrefixString = temp;
			}
			
			temp = prefDoc.getElementsByTagName("lastUtilsGen1FnPrefix").item(0).getTextContent();
			if(temp != null){
				utilsFnPrefixString = temp;
			}
			
			temp = prefDoc.getElementsByTagName("lastUtilsGen1FnPostfix").item(0).getTextContent();
			if(temp != null){
				utilsFnPostfixString = temp;
			}
			return true;
		} catch (Exception e) {
			
		}
		return false;
	}
	
	private static boolean createPreferences(File preFile){
		Document doc = new Document();
		Element theRoot = new Element("preferences");
		doc.setRootElement(theRoot);

		Element lastDir = new Element("lastDirectory");
		if(lastDirectory != null){
			lastDir.setText(lastDirectory.getAbsolutePath());
		}
		else{
			lastDir.setText("null");
		}
		theRoot.addContent(lastDir);
		
		Element lastTab = new Element("lastOpenTab");
		lastTab.setText(lastOpenTabName);
		theRoot.addContent(lastTab);

		Element lastLoadFile = new Element("lastLoadFile");
		if(lastLoadedFile != null){
			lastLoadFile.setText(lastLoadedFile.getAbsolutePath());
		}
		else{
			lastLoadFile.setText("null");
		}
		theRoot.addContent(lastLoadFile);
		
		Element xsolveDat = new Element("xSolveLastData");
		xsolveDat.setText(xSolveLastData);
		theRoot.addContent(xsolveDat);
		
		Element loadPrefix = new Element("lastLoadViewPrefix");
		loadPrefix.setText(loadViewPrefixValue);
		theRoot.addContent(loadPrefix);
		
		Element fnNamePrefix = new Element("lastUtilsGen1FnNamePrefix");
		fnNamePrefix.setText(utilsFnNamePrefixString);
		theRoot.addContent(fnNamePrefix);	
		
		Element fnPrefix = new Element("lastUtilsGen1FnPrefix");
		fnPrefix.setText(utilsFnPrefixString);
		theRoot.addContent(fnPrefix);
		
		Element fnPostfix = new Element("lastUtilsGen1FnPostfix");
		fnPostfix.setText(utilsFnPostfixString);
		theRoot.addContent(fnPostfix);
		
		XMLOutputter xmlOutput = new XMLOutputter(Format.getPrettyFormat());
		try {
			FileOutputStream x = new FileOutputStream(preFile);
			xmlOutput.output(doc, x);
			x.close();
			return true;
		} catch (IOException e) {
		}
		return false;
	}
}
