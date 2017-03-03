package org.ykc.bitpro;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.controlsfx.control.StatusBar;
import org.w3c.dom.*;
import org.xml.sax.InputSource;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Window;

import javax.xml.parsers.*;

public class BProUtils {

	public static File openBitFile(Window win, ObservableList<ExtensionFilter> extensionFilterslist){
		FileChooser fileChooser = new FileChooser();
	    fileChooser.setTitle("Open BitPro File");
	    
	    if(Preferences.getLastDirectory() != null)
	    {
	    	fileChooser.setInitialDirectory(Preferences.getLastDirectory());
	    }
	    
	    fileChooser.getExtensionFilters().addAll(extensionFilterslist);
	    File file = fileChooser.showOpenDialog(win);
	    if (file != null) {
	    	Preferences.setLastDirectory(file.getParentFile());
	    }
	    return file;
	}
	
	public static File openSimpleBitFile(Window win){
		ObservableList<ExtensionFilter> extensionFilters = FXCollections.observableArrayList();
		extensionFilters.add(new ExtensionFilter("BitPro Files(*.spro)", "*.spro"));
		return openBitFile(win, extensionFilters);
	}

	public static File openComplexBitFile(Window win){
		ObservableList<ExtensionFilter> extensionFilters = FXCollections.observableArrayList();
		extensionFilters.add(new ExtensionFilter("BitPro Files(*.bpro)", "*.bpro"));
		return openBitFile(win, extensionFilters);
	}
	
	public static File openBitFileForLoad(Window win){
		
		ObservableList<ExtensionFilter> extensionFilters = FXCollections.observableArrayList();
		extensionFilters.add(new ExtensionFilter("BitPro Files(All Files)", "*.spro", "bpro", "*.bpro"));		
		extensionFilters.add(new ExtensionFilter("BitPro Files(*.spro)", "*.spro"));
		extensionFilters.add(new ExtensionFilter("BitPro Files(*.bpro)", "*.bpro"));		
		return openBitFile(win, extensionFilters);
	}	
	
	public static File openComplexDesignBitFile(Window win){
		ObservableList<ExtensionFilter> extensionFilters = FXCollections.observableArrayList();
		extensionFilters.add(new ExtensionFilter("BitPro Files(*.dpro)", "*.dpro"));
		return openBitFile(win, extensionFilters);
	}
	
	private static File saveFile(Window win, String initialName, String typeString1, String typeString2){
		FileChooser fileChooser = new FileChooser();
	    fileChooser.setTitle("Save BitPro File");
	    
	    if(Preferences.getLastDirectory() != null)
	    {
	    	fileChooser.setInitialDirectory(Preferences.getLastDirectory());
	    }
	    
	    fileChooser.getExtensionFilters().addAll(
	            new FileChooser.ExtensionFilter(typeString1, typeString2)
	        );
	    fileChooser.setInitialFileName(initialName);
	    
	    File file = fileChooser.showSaveDialog(win);
        if (file != null) {
        	if(!file.getName().contains(".")) {
        		file = new File(file.getAbsolutePath() + ".bpro");
        		}
        	Preferences.setLastDirectory(file.getParentFile());
        }
	    return file;
	}	
	
	public static File saveSimpleBitFile(Window win, String initialName){
		return saveFile(win, initialName, "BitPro Files(*.spro)", "*.spro");
	}
	
	public static Document getDocument(File input) {
		try {

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setIgnoringComments(true);
			factory.setIgnoringElementContentWhitespace(true);
			//factory.setValidating(true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			return builder.parse(new InputSource(input.getAbsolutePath()));

		}
		catch(Exception ex){

		}
		return null;
	}

	public static String getNameSimpleXML(Element simpleElement){
		return simpleElement.getElementsByTagName("sname").item(0).getTextContent();
	}
	
	public static Integer getLengthSimpleXML(Element simpleElement){
		return Integer.parseInt(simpleElement.getElementsByTagName("slen").item(0).getTextContent());
	}	
	
	public static Integer getMaxFieldsSimpleXML(Element simpleElement){
		return simpleElement.getElementsByTagName("field").getLength();
	}

	public static Integer getFieldOffsetSimpleXML(Element simpleElement,Integer index){
		NodeList listOfFields = simpleElement.getElementsByTagName("field");
		Node node = listOfFields.item(index);
		return Integer.parseInt(((Element)node).getElementsByTagName("foffset").item(0).getTextContent());
	}

	public static Integer getFieldSizeSimpleXML(Element simpleElement,Integer index){
		NodeList listOfFields = simpleElement.getElementsByTagName("field");
		Node node = listOfFields.item(index);
		return Integer.parseInt(((Element)node).getElementsByTagName("fsize").item(0).getTextContent());
	}

	public static String getFieldNameSimpleXML(Element simpleElement, Integer index){
		NodeList listOfFields = simpleElement.getElementsByTagName("field");
		Node node = listOfFields.item(index);
		return ((Element)node).getElementsByTagName("fname").item(0).getTextContent();
	}

	public static String getFieldDescSimpleXML(Element simpleElement, Integer index){
		NodeList listOfFields = simpleElement.getElementsByTagName("field");
		Node node = listOfFields.item(index);
		return ((Element)node).getElementsByTagName("fdesc").item(0).getTextContent();
	}
	
	public static Integer getFieldEnumCountSimpleXML(Element simpleElement,Integer index){
		NodeList listOfFields = simpleElement.getElementsByTagName("field");
		Node node = listOfFields.item(index);
		Element enumElement = (Element)((Element)node).getElementsByTagName("fenum").item(0);
		
		return enumElement.getElementsByTagName("enum").getLength();
	}
	
	public static Element getFieldEnumSimpleXML(Element simpleElement,Integer index){
		NodeList listOfFields = simpleElement.getElementsByTagName("field");
		Node node = listOfFields.item(index);
		Element enumElement = (Element)((Element)node).getElementsByTagName("fenum").item(0);
		return enumElement;
	}
	
	public static String getFieldEnumNameSimpleXML(Element enumElement,Integer index){
		NodeList listOfEnames = enumElement.getElementsByTagName("enum");
		Node node = listOfEnames.item(index);
		return ((Element)node).getElementsByTagName("ename").item(0).getTextContent();
	}
	
	public static Long getFieldEnumValueSimpleXML(Element enumElement,Integer index){
		NodeList listOfEnames = enumElement.getElementsByTagName("enum");
		Node node = listOfEnames.item(index);
		return GUtils.parseStringtoNumber(((Element)node).getElementsByTagName("evalue").item(0).getTextContent());
	}	
}
