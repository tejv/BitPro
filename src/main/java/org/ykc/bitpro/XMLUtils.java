package org.ykc.bitpro;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.controlsfx.control.StatusBar;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Text;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.w3c.dom.*;
import org.xml.sax.*;
import javax.xml.parsers.*;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

public class XMLUtils {
	public static final int RADIX_HEX = 0;
	public static final int RADIX_DECIMAL = 1;
	public static final int RADIX_BINARY = 2;
	private static int radix = RADIX_HEX;
	
	public static void setRadix(int value)
	{
		radix = value;
	}
	public static boolean createSimpleXML(File fileName, String name, Integer struct_size, TableView<BitField> table)
	{
		if(name.trim().isEmpty() || (struct_size > 64))
		{
			return false;
		}

		Document doc = new Document();
		Element theRoot = new Element("simple");
		doc.setRootElement(theRoot);

		Element head = new Element("head");
		theRoot.addContent(head);

		Element body = new Element("body");
		theRoot.addContent(body);

		Element sname = new Element("sname");
		sname.setText(name);
		head.addContent(sname);

		Element type = new Element("stype");
		type.setText("simple");
		head.addContent(type);

		Element size = new Element("slen");
		size.setText(struct_size.toString());
		head.addContent(size);
		Integer offset = 0;
		for(BitField row: table.getItems())
		{
			if((offset + row.getSize()) > struct_size)
			{
				return false;
			}
			if(writeFieldXML(body, row.getName(), row.getSize(), row.getDesc(), row.getEnums(), offset) == false)
			{
				return false;
			}
			offset += row.getSize();
		}

		XMLOutputter xmlOutput = new XMLOutputter(Format.getPrettyFormat());
		try {
			xmlOutput.output(doc, new FileOutputStream(fileName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return false;
		}

		return true;
	}

	private static boolean writeFieldXML(Element root, String fieldName, Integer bitSize, String desc, String enums, Integer offset){

		Element field = new Element("field");
		root.addContent(field);

		Element fname = new Element("fname");
		fname.setText(fieldName);
		field.addContent(fname);

		Element foffset = new Element("foffset");
		foffset.setText(offset.toString());
		field.addContent(foffset);

		Element fsize = new Element("fsize");
		fsize.setText(bitSize.toString());
		field.addContent(fsize);

		Element fdesc = new Element("fdesc");
		fdesc.setText(desc);
		field.addContent(fdesc);

		Element fenum = new Element("fenum");
		field.addContent(fenum);

		return writeEnumXML(fenum, enums);

	}
	private static boolean writeEnumXML(Element root, String enumString)
	{
		ObservableList<EnumField> enumList = parseEnumString(enumString);
		if(enumList.isEmpty())
		{
			return true;
		}

		for(EnumField row: enumList)
		{
			writeEnumUnitXML(root, row.getName(), "0x" + Long.toHexString(row.getVal()));
		}
		return true;
	}

	private static void writeEnumUnitXML(Element root, String name, String value)
	{
		Element enums = new Element("enum");
		root.addContent(enums);

		Element ename = new Element("ename");
		ename.setText(name);
		enums.addContent(ename);

		Element evalue = new Element("evalue");
		evalue.setText(value);
		enums.addContent(evalue);
	}

	private static ObservableList<EnumField> parseEnumString(String enumString)
	{
		ObservableList<EnumField> enumList = FXCollections.observableArrayList();
		String[] parts = enumString.split(",");
		Long value = 0L;
		String name;
		for(String entry:parts)
		{
			if(entry.contains("="))
			{
				String[] x = entry.split("=");
				name = x[0].trim();
				value = parseStringtoNumber(x[1].trim());
			}
			else
			{
				name = entry.trim();
			}
			if(name.isEmpty() == false)
			{
				enumList.add(new EnumField(name, value));
				value++;
			}
		}
		/* TODO: Parse Enums */
		return enumList;
	}

	private static Long parseStringtoNumber(String input)
	{
		Long value = 0L;
		try {
			if(input.startsWith("0x"))
			{
				value = Long.parseLong(input.substring(2), 16);
			}
			else if(input.startsWith("0b"))
			{
				value = Long.parseLong(input.substring(2), 2);
			}
			else
			{
				value = Long.parseLong(input, 10);
			}
		} catch (NumberFormatException e) {
			return 0L;
		}
		return value;

	}

	public static boolean openSimpleXML(File fileName, JFXTextField name, ToggleGroup toggleGroup, TableView<BitField> table){
		table.getItems().clear();
		org.w3c.dom.Document xmlDoc = getDocument(fileName);
		if((xmlDoc == null) || (!xmlDoc.getElementsByTagName("stype").item(0).getTextContent().matches("simple")))
		{
			return false;
		}
		name.setText(xmlDoc.getElementsByTagName("sname").item(0).getTextContent());
		String slen = xmlDoc.getElementsByTagName("slen").item(0).getTextContent();
		switch(slen)
		{
		case "8":
			toggleGroup.selectToggle(toggleGroup.getToggles().get(0));
			break;
		case "16":
			toggleGroup.selectToggle(toggleGroup.getToggles().get(1));
			break;
		case "32":
			toggleGroup.selectToggle(toggleGroup.getToggles().get(2));
			break;
		case "64":
			toggleGroup.selectToggle(toggleGroup.getToggles().get(3));
			break;
		}
		return loadRows(xmlDoc,table);
	}

	private static boolean loadRows(org.w3c.dom.Document xmlDoc, TableView<BitField> table)
	{
		NodeList listOfFields = xmlDoc.getElementsByTagName("field");
		for(int i=0; i < listOfFields.getLength(); i++){
			Node fieldNode = listOfFields.item(i);
			loadRow(fieldNode, table);
		}
		return true;
	}

	private static boolean loadRow(Node node, TableView<BitField> table)
	{
		BitField x;
		try {
			x = new BitField("Error",1,"","");
		} catch (Exception e) {
			return false;
		}
		x.setName(((org.w3c.dom.Element)node).getElementsByTagName("fname").item(0).getTextContent());
		x.setSize(Integer.parseInt(((org.w3c.dom.Element)node).getElementsByTagName("fsize").item(0).getTextContent()));
		x.setDesc(((org.w3c.dom.Element)node).getElementsByTagName("fdesc").item(0).getTextContent());
		x.setEnums(loadEnums(((org.w3c.dom.Element)node)));
		table.getItems().add(x);
		return true;
	}

	private static String loadEnums(org.w3c.dom.Element element)
	{
		String eString = "";
		NodeList listOfEnums = element.getElementsByTagName("enum");
		for(int i=0; i < listOfEnums.getLength(); i++){
			org.w3c.dom.Element eNode = (org.w3c.dom.Element)listOfEnums.item(i);
			eString += eNode.getElementsByTagName("ename").item(0).getTextContent() + "=" + eNode.getElementsByTagName("evalue").item(0).getTextContent() + ",";
		}
		return eString;
	}

	// Reads an XML file into a DOM document

	public static org.w3c.dom.Document getDocument(File input) {

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

	public static boolean loadSimpleXML(org.w3c.dom.Document xmlDoc, JFXTextField txtData, GridPane gPane, StatusBar statusBar) {
		Integer max_fields = getMaxFieldsSimpleXML(xmlDoc);

		ObservableList<JFXComboBox> comboList = FXCollections.observableArrayList();
		ObservableList<Label> labelList = FXCollections.observableArrayList();

		gPane.getChildren().clear();
        for(Integer i = 0; i < max_fields; i++)
        {
        	Integer offset = getFieldOffsetSimpleXML(xmlDoc,i);
        	String name = getFieldNameSimpleXML(xmlDoc,i) + "( " ;
        	Integer fsize = getFieldSizeSimpleXML(xmlDoc,i);
        	if(fsize != 1)
        	{
        		name += ((Integer)(fsize + offset -1)).toString() + ".";
        	}

        	name += offset.toString();

        	name += " )";

        	Label label = new Label(name);
        	JFXComboBox cbox = new JFXComboBox();
        	cbox.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                	updateMainTextLoadTab(xmlDoc, txtData, gPane, comboList);
                }
            });
            label.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                	updateDescLoadTab(e, xmlDoc, statusBar, labelList);
                }
              });       	
        	cbox.setEditable(true);
        	comboList.add(cbox);
        	labelList.add(label);
        	gPane.add(label, 0, i);
        	gPane.add(cbox, 1, i);
        }
        
    	updateFieldTextLoadTab(xmlDoc, txtData, gPane, comboList);
		txtData.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	updateFieldTextLoadTab(xmlDoc, txtData, gPane, comboList);
            }
        });
		return true;
	}

	public static void updateDescLoadTab(MouseEvent e, org.w3c.dom.Document xmlDoc, StatusBar statusBar, ObservableList<Label> labelList){
		for(int i = 0; i < labelList.size(); i++)
		{
			if(e.getSource().equals(labelList.get(i)))
			{
				statusBar.setText(getFieldDescSimpleXML(xmlDoc,i));
			}
		}
	}
	
	public static void updateMainTextLoadTab(org.w3c.dom.Document xmlDoc, JFXTextField txtData, GridPane gPane,ObservableList<JFXComboBox> comboList){
		Integer max_fields = getMaxFieldsSimpleXML(xmlDoc);
		Long value = 0L;
        for(Integer i = 0; i < max_fields; i++)
        {
        	Integer offset = getFieldOffsetSimpleXML(xmlDoc,i);
        	Integer fsize = getFieldSizeSimpleXML(xmlDoc,i);
        	Long mask = BitUtils.getMask(fsize);
        	Long result = (parseStringtoNumber(comboList.get(i).getValue().toString()) & mask);
        	value |=  result << offset;
        	String res = "";
            switch(radix)
            {
            case RADIX_BINARY:
            	res = "0b" + Long.toBinaryString(result);
            	break;
            case RADIX_DECIMAL:
            	res = Long.toString(result);
            	break;
            default:
            	res = "0x" + Long.toHexString(result);
            	break;
            }
        	comboList.get(i).setValue(res);
        }
        switch(radix)
        {
        case RADIX_BINARY:
        	txtData.setText("0b" + Long.toBinaryString(value));
        	break;
        case RADIX_DECIMAL:
        	txtData.setText(Long.toString(value));
        	break;
        default:
        	txtData.setText("0x" + Long.toHexString(value));
        	break;
        }
        
	}

	private static void updateFieldTextLoadTab(org.w3c.dom.Document xmlDoc, JFXTextField txtData, GridPane gPane,ObservableList<JFXComboBox> comboList){
		Long value = parseStringtoNumber(txtData.getText());
		Integer max_fields = getMaxFieldsSimpleXML(xmlDoc);
        for(Integer i = 0; i < max_fields; i++)
        {
        	Integer offset = getFieldOffsetSimpleXML(xmlDoc,i);
        	Integer fsize = getFieldSizeSimpleXML(xmlDoc,i);
        	Long result = BitUtils.getValue(value, offset, fsize);
        	String res = "";
            switch(radix)
            {
            case RADIX_BINARY:
            	res = "0b" + Long.toBinaryString(result);
            	break;
            case RADIX_DECIMAL:
            	res = Long.toString(result);
            	break;
            default:
            	res = "0x" + Long.toHexString(result);
            	break;
            }
        	comboList.get(i).setValue(res);
        }
        switch(radix)
        {
        case RADIX_BINARY:
        	txtData.setText("0b" + Long.toBinaryString(value));
        	break;
        case RADIX_DECIMAL:
        	txtData.setText(Long.toString(value));
        	break;
        default:
        	txtData.setText("0x" + Long.toHexString(value));
        	break;
        }
	}
	
	private static Integer getMaxFieldsSimpleXML(org.w3c.dom.Document doc){
		return doc.getElementsByTagName("field").getLength();
	}

	private static Integer getFieldOffsetSimpleXML(org.w3c.dom.Document doc,Integer index){
		NodeList listOfFields = doc.getElementsByTagName("field");
		Node node = listOfFields.item(index);
		return Integer.parseInt(((org.w3c.dom.Element)node).getElementsByTagName("foffset").item(0).getTextContent());
	}

	private static Integer getFieldSizeSimpleXML(org.w3c.dom.Document doc,Integer index){
		NodeList listOfFields = doc.getElementsByTagName("field");
		Node node = listOfFields.item(index);
		return Integer.parseInt(((org.w3c.dom.Element)node).getElementsByTagName("fsize").item(0).getTextContent());
	}

	private static String getFieldNameSimpleXML(org.w3c.dom.Document doc, Integer index){
		NodeList listOfFields = doc.getElementsByTagName("field");
		Node node = listOfFields.item(index);
		return ((org.w3c.dom.Element)node).getElementsByTagName("fname").item(0).getTextContent();
	}
	
	private static String getFieldDescSimpleXML(org.w3c.dom.Document doc, Integer index){
		NodeList listOfFields = doc.getElementsByTagName("field");
		Node node = listOfFields.item(index);
		return ((org.w3c.dom.Element)node).getElementsByTagName("fdesc").item(0).getTextContent();
	}	
}
