package org.ykc.bitpro;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.controlsfx.control.StatusBar;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import com.jfoenix.controls.JFXTextField;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;

public class SProCreate {
	private static StatusBar status;
	private static boolean create(File fileName, String name, 
			Integer struct_size, TableView<SProRow> table, StatusBar statusBar)
	{
		status = statusBar;
		if(name.trim().isEmpty() || (struct_size > 64))
		{
			status.setText("Error in parsing: Name not provided");
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
		for(SProRow row: table.getItems())
		{
			if((offset + row.getLen()) > struct_size)
			{
				status.setText("Error in parsing: Max size limit exceeded");
				return false;
			}
			if(writeField(body, row.getName(), row.getLen(),
					row.getDesc(), row.getEnums(), offset) == false)
			{
				status.setText("Error in parsing: Field Parse Error");
				return false;
			}
			offset += row.getLen();
		}

		XMLOutputter xmlOutput = new XMLOutputter(Format.getPrettyFormat());
		try {
			xmlOutput.output(doc, new FileOutputStream(fileName));
		} catch (IOException e) {
			status.setText("Error in creating bpro file: IO Error");
			return false;
		}

		return true;
	}

	private static boolean writeField(Element root, String fieldName,
			Integer bitSize, String desc, String enums, Integer offset){

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

		return writeEnums(fenum, enums);

	}
	private static boolean writeEnums(Element root, String enumString)
	{
		ObservableList<SProEnum> enumList;
		String[] nameNenums = enumString.split(":");
		if(nameNenums.length == 2)
		{
			root.setAttribute("at_ename", nameNenums[0].trim());
			enumList = parseEnumString(nameNenums[1]);
		}
		else {
			enumList = parseEnumString(enumString);
		}
		
		if(enumList.isEmpty())
		{
			return true;
		}

		for(SProEnum row: enumList)
		{
			writeEnum(root, row.getName(), "0x" + Long.toHexString(row.getVal()));
		}
		return true;
	}

	private static void writeEnum(Element root, String name, String value)
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

	private static ObservableList<SProEnum> parseEnumString(String enumString)
	{
		ObservableList<SProEnum> enumList = FXCollections.observableArrayList();
		String[] parts = enumString.split(",");
		Long value = 0L;
		String name;
		for(String part:parts)
		{
			if(part.contains("="))
			{
				String[] x = part.split("=");
				name = x[0].trim();
				value = Utils.parseStringtoNumber(x[1].trim());
			}
			else
			{
				name = part.trim();
			}
			if(name.isEmpty() == false)
			{
				enumList.add(new SProEnum(name, value));
				value++;
			}
		}
		return enumList;
	}
	
	public static boolean addField(TableView<SProRow> tView,
			String name, String size, String desc, String enums, StatusBar statusBar)
	{
		SProRow row;
		try {
			row = new SProRow(name, size, desc, enums);
		} catch (Exception e) {
			statusBar.setText("Invalid Entry");
			return false;
		}
		tView.getItems().add(row);
		statusBar.setText("Row Added");
		return true;
	}

	public static boolean modifyField(TableView<SProRow> tView,
			String name, String size, String desc, String enums,  StatusBar statusBar)
	{
		SProRow row;
		try {
			row = new SProRow(name, size, desc, enums);
		} catch (Exception e) {
			statusBar.setText("Invalid Entry");
			return false;
		}
		SProRow x = tView.getSelectionModel().getSelectedItem();
		x.setName(row.getName());
		x.setDesc(row.getDesc());
		x.setEnums(row.getEnums());
		x.setSize(row.getSize());

		/* Workaround for tableview update issue */
		tView.getColumns().get(0).setVisible(false);
		tView.getColumns().get(0).setVisible(true);
		statusBar.setText("Row Updated");
		return true;
	}
	
    public static void run(JFXTextField txtSProName, RadioButton rbSPro8bit,
    		RadioButton rbSPro16bit, RadioButton rbSPro64bit,
    		BorderPane borderPaneMainWindow, TableView<SProRow> tViewSPro, StatusBar statusBar) {
    	if(txtSProName.getText().isEmpty() == true)
    	{
    		statusBar.setText("Please provide a name");
    		return;
    	}
    	Integer size = 32;
    	if(rbSPro8bit.isSelected())
    	{
    		size = 8;
    	}
    	else if(rbSPro16bit.isSelected())
    	{
    		size = 16;
    	}
    	else if(rbSPro64bit.isSelected())
    	{
    		size = 64;
    	}
    	File file = UtilsBPro.saveSProFile(borderPaneMainWindow.getScene().getWindow(), txtSProName.getText());

        if (file != null) {
        	if(create(file, txtSProName.getText(), size, tViewSPro, statusBar) == true)
        	{
        		statusBar.setText("Save Success");
        	}
        }
        else
        {
        	statusBar.setText("Operation Cancelled");
        }

    }	
}
