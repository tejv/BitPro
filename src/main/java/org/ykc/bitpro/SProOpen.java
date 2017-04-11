package org.ykc.bitpro;

import java.io.File;

import org.controlsfx.control.StatusBar;
import org.jdom2.Document;
import org.jdom2.Element;

import com.jfoenix.controls.JFXTextField;

import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;

public class SProOpen {
	
	public static boolean open(File fileName, JFXTextField name, ToggleGroup toggleGroup, TableView<SProRow> table){
		table.getItems().clear();
		Document xmlDoc = UtilsBPro.getJDOM2Doc(fileName);
		if((xmlDoc == null) || (!xmlDoc.getRootElement().getChild("head").getChildText("stype").matches("simple")))
		{
			return false;
		}
		name.setText(xmlDoc.getRootElement().getChild("head").getChildText("sname"));
		String slen = xmlDoc.getRootElement().getChild("head").getChildText("slen");
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
		Element simplElement = xmlDoc.getRootElement();
		int listOfFields = UtilsBPro.getSProFieldsCount(simplElement);
		for(int i= 0; i < listOfFields; i++){
			SProRow x;
			try {
				x = new SProRow("Error","1","","");
			} catch (Exception e) {
				return false;
			}
			Element fieldElement = UtilsBPro.getSProFieldElement(simplElement, i);
			x.setName(fieldElement.getChildText("fname"));
			x.setSize(fieldElement.getChildText("fsize"));
			x.setDesc(fieldElement.getChildText("fdesc"));
			x.setEnums(openEnums(fieldElement.getChild("fenum")));
			table.getItems().add(x);
		}
		return true;
	}

	private static String openEnums(Element eElement)
	{
		String eString = "";
		if(eElement.getChild("enum") != null){

			if(eElement.getAttribute("at_ename") != null)
			{
				eString += eElement.getAttribute("at_ename").getValue() + ":";
			}
			
			for(int i = 0; i < UtilsBPro.getSProFieldEnumsCount(eElement); i++){
				eString += UtilsBPro.getSProEnumName(eElement, i) + "=" + UtilsBPro.getSProEnumValueString(eElement, i) + ",";
			}
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
