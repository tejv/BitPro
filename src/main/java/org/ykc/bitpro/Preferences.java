package org.ykc.bitpro;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import javafx.scene.control.Tab;

public class Preferences {
	private static File lastDirectory = null;
	private static File lastLoadedFile = null;
	private static String lastOpenTabName = "tabSPro";
	private static String xSolveLastData = "0";
	private static String loadViewPrefixValue = "";
	private static String utilsFnNamePrefixString = "";
	private static String utilsFnPrefixString = "";
	private static String utilsFnPostfixString = "";
	private static File dproLastBrowseDir = null;
	private static File dproBasePath = new File(System.getProperty("user.home") + "/BitPro");
	private static File lastDPROFile = null;

	public static File getDproLastBrowseDir() {
		return dproLastBrowseDir;
	}

	public static File getLastDesignFile() {
		return lastDPROFile;
	}

	public static void setLastDesignFile(File lastDesignFile) {
		Preferences.lastDPROFile = lastDesignFile;
	}

	public static void setDproLastBrowseDir(File dproLastBrowseDir) {
		Preferences.dproLastBrowseDir = dproLastBrowseDir;
	}

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
			Document prefDoc = UtilsBPro.getJDOM2Doc(prefFile);
			if(prefDoc == null)
			{
				return false;
			}
			Element prefElement = prefDoc.getRootElement();
			String lastDir = prefElement.getChildText("lastDirectory");
			if(!lastDir.equals("null"))
			{
				lastDirectory = new File(lastDir);
			}
			lastOpenTabName = prefElement.getChildText("lastOpenTab"); 
			String fileName = prefElement.getChildText("lastLoadFile");
			if(!fileName.equals("null"))
			{
				lastLoadedFile = new File(fileName);
			}

			String xSolveDat = prefElement.getChildText("xSolveLastData");
			if(xSolveDat != null)
			{
				xSolveLastData = xSolveDat;
			}
			String temp = prefElement.getChildText("lastLoadViewPrefix");
			if(temp != null){
				loadViewPrefixValue = temp;
			}

			temp = prefElement.getChildText("lastUtilsGen1FnNamePrefix");
			if(temp != null){
				utilsFnNamePrefixString = temp;
			}

			temp = prefElement.getChildText("lastUtilsGen1FnPrefix");
			if(temp != null){
				utilsFnPrefixString = temp;
			}

			temp = prefElement.getChildText("lastUtilsGen1FnPostfix");
			if(temp != null){
				utilsFnPostfixString = temp;
			}

			temp = prefElement.getChildText("dproLastBrowseDir");
			if(!temp.equals("null"))
			{
				dproLastBrowseDir = new File(temp);
			}

			temp = prefElement.getChildText("dproBasePath");
			File file = new File(temp);
			if(file.exists())
			{
				dproBasePath = file;
			}
			
			String lastdpro = prefElement.getChildText("lastDPROFile");
			if(!lastdpro.equals("null"))
			{
				lastDPROFile = new File(lastdpro);
			}
			return true;
		} catch (Exception e) {

		}
		return false;
	}

	public static File getDproBasePath() {
		return dproBasePath;
	}

	public static void setDproBasePath(File dproBasePath) {
		Preferences.dproBasePath = dproBasePath;
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


		Element dproBrowseDir = new Element("dproLastBrowseDir");
		if(dproLastBrowseDir != null){
			dproBrowseDir.setText(dproLastBrowseDir.getAbsolutePath());
		}
		else{
			dproBrowseDir.setText("null");
		}
		theRoot.addContent(dproBrowseDir);

		Element dproBasePathElement = new Element("dproBasePath");
		dproBasePathElement.setText(dproBasePath.getAbsolutePath());
		theRoot.addContent(dproBasePathElement);

		Element lastDProElement = new Element("lastDPROFile");
		if(lastDPROFile != null){
			lastDProElement.setText(lastDPROFile.getAbsolutePath());
		}
		else{
			lastDProElement.setText("null");
		}
		theRoot.addContent(lastDProElement);		
		
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
