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
