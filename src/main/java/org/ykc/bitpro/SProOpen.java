package org.ykc.bitpro;

import java.io.File;

import org.controlsfx.control.StatusBar;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.jfoenix.controls.JFXTextField;

import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;

public class SProOpen {
	
	public static boolean open(File fileName, JFXTextField name, ToggleGroup toggleGroup, TableView<SProRow> table){
		table.getItems().clear();
		Document xmlDoc = UtilsBPro.getW3cDomDoc(fileName);
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
		return openRows(xmlDoc,table);
	}

	private static boolean openRows(Document xmlDoc, TableView<SProRow> table)
	{
		NodeList listOfFields = xmlDoc.getElementsByTagName("field");
		for(int i=0; i < listOfFields.getLength(); i++){
			Node fieldNode = listOfFields.item(i);
			openRow(fieldNode, table);
		}
		return true;
	}

	private static boolean openRow(Node node, TableView<SProRow> table)
	{
		SProRow x;
		try {
			x = new SProRow("Error","1","","");
		} catch (Exception e) {
			return false;
		}
		x.setName(((Element)node).getElementsByTagName("fname").item(0).getTextContent());
		x.setSize(((Element)node).getElementsByTagName("fsize").item(0).getTextContent());
		x.setDesc(((Element)node).getElementsByTagName("fdesc").item(0).getTextContent());
		x.setEnums(openEnums((Element)(((Element)node).getElementsByTagName("fenum").item(0))));
		table.getItems().add(x);
		return true;
	}

	private static String openEnums(Element element)
	{
		String eString = "";
		NodeList listOfEnums = element.getElementsByTagName("enum");
		if(listOfEnums.getLength() != 0)
		{
			if(element.hasAttribute("at_ename"))
			{
				eString += element.getAttribute("at_ename") + ":";
			}
		}
		for(int i=0; i < listOfEnums.getLength(); i++){
			Element eNode = (Element)listOfEnums.item(i);
			eString += eNode.getElementsByTagName("ename").item(0).getTextContent() + "=" + eNode.getElementsByTagName("evalue").item(0).getTextContent() + ",";
		}
		return eString;
	}
	
    public static void run(BorderPane borderPaneMainWindow, TextField txtSProFieldName,
    		TextField txtSProFieldSize, TextField txtSProFieldDesc,
    		TextField txtSProFieldEnum, JFXTextField txtSProName,
    		ToggleGroup tgSProBitSel, TableView<SProRow> tViewSPro, StatusBar statusBar)
    {
    	File file = UtilsBPro.openSProFile(borderPaneMainWindow.getScene().getWindow());

        if (file != null) {
    		txtSProFieldName.clear();
    		txtSProFieldSize.clear();
    		txtSProFieldDesc.clear();
    		txtSProFieldEnum.clear();
        	if(open(file, txtSProName, tgSProBitSel, tViewSPro) == true)
        	{
        		statusBar.setText("Open Success");
        	}
        	else
        	{
        		statusBar.setText("Open Failed");
        	}
        }
        else
        {
        	statusBar.setText("Operation Cancelled");
        }

    }	
}
