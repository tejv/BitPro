package org.ykc.bitpro;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.controlsfx.control.StatusBar;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

public class CreateBPro {
	private static StatusBar status;
	public static boolean createSimpleXML(File fileName, String name, Integer struct_size, TableView<BFieldSimpleRow> table, StatusBar statusBar)
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
		for(BFieldSimpleRow row: table.getItems())
		{
			if((offset + row.getLen()) > struct_size)
			{
				status.setText("Error in parsing: Max size limit exceeded");
				return false;
			}
			if(writeFieldXML(body, row.getName(), row.getLen(), row.getDesc(), row.getEnums(), offset) == false)
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
		ObservableList<EnumField> enumList;
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
		for(String part:parts)
		{
			if(part.contains("="))
			{
				String[] x = part.split("=");
				name = x[0].trim();
				value = GUtils.parseStringtoNumber(x[1].trim());
			}
			else
			{
				name = part.trim();
			}
			if(name.isEmpty() == false)
			{
				enumList.add(new EnumField(name, value));
				value++;
			}
		}
		return enumList;
	}
}
