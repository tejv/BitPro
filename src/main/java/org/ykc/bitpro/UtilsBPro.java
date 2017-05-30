package org.ykc.bitpro;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.controlsfx.control.StatusBar;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.xml.sax.InputSource;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Window;

import javax.xml.parsers.*;

public class UtilsBPro {

	private static File openBitFile(Window win, ObservableList<ExtensionFilter> extensionFilterslist){
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

	public static File openSProFile(Window win){
		ObservableList<ExtensionFilter> extensionFilters = FXCollections.observableArrayList();
		extensionFilters.add(new ExtensionFilter("BitPro Files(*.spro)", "*.spro"));
		return openBitFile(win, extensionFilters);
	}

	public static File openCProFile(Window win){
		ObservableList<ExtensionFilter> extensionFilters = FXCollections.observableArrayList();
		extensionFilters.add(new ExtensionFilter("BitPro Files(*.cpro)", "*.cpro"));
		return openBitFile(win, extensionFilters);
	}

	public static File openLoadFile(Window win){

		ObservableList<ExtensionFilter> extensionFilters = FXCollections.observableArrayList();
		extensionFilters.add(new ExtensionFilter("BitPro Files(All Files)", "*.spro", "cpro", "*.cpro"));
		extensionFilters.add(new ExtensionFilter("BitPro Files(*.spro)", "*.spro"));
		extensionFilters.add(new ExtensionFilter("BitPro Files(*.cpro)", "*.cpro"));
		return openBitFile(win, extensionFilters);
	}

	public static File openDProFile(Window win){
		ObservableList<ExtensionFilter> extensionFilters = FXCollections.observableArrayList();
		extensionFilters.add(new ExtensionFilter("BitPro Files(*.dpro)", "*.dpro"));
		return openBitFile(win, extensionFilters);
	}
	
	public static File openEQFile(Window win){
		ObservableList<ExtensionFilter> extensionFilters = FXCollections.observableArrayList();
		extensionFilters.add(new ExtensionFilter("F(x) Files(*.eq)", "*.eq"));
		FileChooser fileChooser = new FileChooser();
	    fileChooser.setTitle("Open F(x) File");

	    if(Preferences.getFxLastDirectory() != null)
	    {
	    	fileChooser.setInitialDirectory(Preferences.getFxLastDirectory());
	    }

	    fileChooser.getExtensionFilters().addAll(extensionFilters);
	    File file = fileChooser.showOpenDialog(win);
	    if (file != null) {
	    	Preferences.setFxLastDirectory(file.getParentFile());
	    }
	    return file;
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

	public static File saveSProFile(Window win, String initialName){
		return saveFile(win, initialName, "BitPro Files(*.spro)", "*.spro");
	}

	public static File saveDProFile(Window win, String initialName){
		return saveFile(win, initialName, "BitPro Files(*.dpro)", "*.dpro");
	}
	
	public static File saveEQFile(Window win, String initialName) {
		FileChooser fileChooser = new FileChooser();
	    fileChooser.setTitle("Save EQ File");

	    if(Preferences.getFxLastDirectory() != null)
	    {
	    	fileChooser.setInitialDirectory(Preferences.getFxLastDirectory());
	    }

	    fileChooser.getExtensionFilters().addAll(
	            new FileChooser.ExtensionFilter("F(x) Files(*.eq)", "*.eq")
	        );
	    fileChooser.setInitialFileName(initialName);

	    File file = fileChooser.showSaveDialog(win);
        if (file != null) {
        	Preferences.setFxLastDirectory(file.getParentFile());
        }
	    return file;		
	}	
	
	public static org.jdom2.Document getJDOM2Doc(File input) {
		try {
			SAXBuilder saxBuilder = new SAXBuilder();
			org.jdom2.Document document = saxBuilder.build(input);
			return document;
		}
		catch(Exception ex){

		}
		return null;
	}	

	public static String getSProName(Element simpleElement){
		return simpleElement.getChild("head").getChildText("sname");
	}

	public static Integer getSProLength(Element simpleElement){
		return Integer.parseInt(simpleElement.getChild("head").getChildText("slen"));
	}

	public static Integer getSProFieldsCount(Element simpleElement){
		return simpleElement.getChild("body").getChildren("field").size();
	}
	
	public static Element getSProFieldElement(Element simpleElement, int index){
		return simpleElement.getChild("body").getChildren("field").get(index);
	}	

	public static Integer getDProFieldsCount(Element designElement){
		return designElement.getChild("body").getChildren("field").size();
	}
	
	public static String getDProBasePath(Element designElement){
		return designElement.getChild("head").getChildText("dBasePath");
	}	

	public static String getDProFieldName(Element designElement, Integer index){
		return designElement.getChild("body").getChildren("field").get(index).getChildText("fname");
	}

	public static String getDProFieldType(Element designElement, Integer index){
		return designElement.getChild("body").getChildren("field").get(index).getChildText("ftype");
	}

	public static Integer getDProFieldSize(Element designElement, Integer index){
		return Integer.parseInt(designElement.getChild("body").getChildren("field").get(index).getChildText("fsize"));
	}

	public static String getDProFieldDesc(Element designElement, Integer index){
		return designElement.getChild("body").getChildren("field").get(index).getChildText("fdesc");
	}

	public static String getDProFieldPath(Element designElement, Integer index){
		return designElement.getChild("body").getChildren("field").get(index).getChildText("frpath");		
	}

	public static Integer getSProFieldOffset(Element simpleElement,Integer index){
		return Integer.parseInt(simpleElement.getChild("body").getChildren("field").get(index).getChildText("foffset"));
	}

	public static Integer getSProFieldSize(Element simpleElement,Integer index){
		return Integer.parseInt(simpleElement.getChild("body").getChildren("field").get(index).getChildText("fsize"));
	}

	public static String getSProFieldName(Element simpleElement, Integer index){
		return simpleElement.getChild("body").getChildren("field").get(index).getChildText("fname");
	}

	public static String getSProFieldDesc(Element simpleElement, Integer index){
		return simpleElement.getChild("body").getChildren("field").get(index).getChildText("fdesc");
	}

	public static Integer getSProFieldEnumCount(Element simpleElement,Integer index){
		return simpleElement.getChild("body").getChildren("field").get(index).getChild("fenum").getChildren().size();
	}

	public static Element getSProFieldEnumElement(Element simpleElement,Integer index){
		return simpleElement.getChild("body").getChildren("field").get(index).getChild("fenum");
	}
	
	public static int getSProFieldEnumsCount(Element enumElement){
		return enumElement.getChildren("enum").size();
	}

	public static String getSProEnumName(Element enumElement,Integer index){
		return enumElement.getChildren("enum").get(index).getChildText("ename");
	}

	public static Long getSProEnumValue(Element enumElement,Integer index){
		return Utils.parseStringtoNumber(enumElement.getChildren("enum").get(index).getChildText("evalue"));
	}

	public static String getSProEnumValueString(Element enumElement,Integer index){
		return enumElement.getChildren("enum").get(index).getChildText("evalue");
	}


}
