package org.ykc.bitpro;

import java.io.File;

import org.controlsfx.control.StatusBar;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;

import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class DProOpen {
	private Stage primaryStage;
	private BorderPane borderPaneMainWindow;
	private TableView<DProRow> tViewDPro;
    private JFXTextField txtDProName;
    private TextField txtDProTypeName;
    private TextField txtDProFieldName;
    private TextField txtDProFieldSize;
    private TextField txtDProFieldRPath;
    private TextField txtDProFieldDesc;
    private StatusBar statusBar;
    
	public DProOpen(Stage primaryStage, TableView<DProRow> tViewDPro, JFXTextField txtDProName,
			TextField txtDProTypeName, TextField txtDProFieldName, TextField txtDProFieldSize,
			TextField txtDProFieldRPath, TextField txtDProFieldDesc, BorderPane borderPaneMainWindow,
			StatusBar statusBar) {
		this.primaryStage = primaryStage;
		this.tViewDPro = tViewDPro;
		this.txtDProName = txtDProName;
		this.txtDProTypeName = txtDProTypeName;
		this.txtDProFieldName = txtDProFieldName;
		this.txtDProFieldSize = txtDProFieldSize;
		this.txtDProFieldRPath = txtDProFieldRPath;
		this.txtDProFieldDesc = txtDProFieldDesc;
		this.borderPaneMainWindow = borderPaneMainWindow;
		this.statusBar = statusBar;
	}

	public void run() {
    	File file = UtilsBPro.openDProFile(borderPaneMainWindow.getScene().getWindow());

        if (file != null) {
        	txtDProTypeName.clear();
    		txtDProFieldName.clear();
    		txtDProFieldSize.clear();
    		txtDProFieldDesc.clear();
    		txtDProFieldRPath.clear();
        	if(open(file) == true)
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
	
	private boolean open(File fileName){
		tViewDPro.getItems().clear();
		Document xmlDoc = UtilsBPro.getW3cDomDoc(fileName);
		if((xmlDoc == null) || (!xmlDoc.getElementsByTagName("dtype").item(0).getTextContent().matches("design")))
		{
			return false;
		}
		txtDProName.setText(xmlDoc.getElementsByTagName("dname").item(0).getTextContent());
		NodeList listOfFields = xmlDoc.getElementsByTagName("field");
		for(int i=0; i < listOfFields.getLength(); i++){
			Node fieldNode = listOfFields.item(i);
			openRow(fieldNode);
		}
		return true;
	}

	private boolean openRow(Node node)
	{
		DProRow x;
		try {
			x = new DProRow("Error","Error","1","","Error");
		} catch (Exception e) {
			return false;
		}
		x.setType(((Element)node).getElementsByTagName("ftype").item(0).getTextContent());		
		x.setName(((Element)node).getElementsByTagName("fname").item(0).getTextContent());
		x.setSize(((Element)node).getElementsByTagName("fsize").item(0).getTextContent());
		x.setDesc(((Element)node).getElementsByTagName("fdesc").item(0).getTextContent());
		x.setRpath(((Element)node).getElementsByTagName("frpath").item(0).getTextContent());
		tViewDPro.getItems().add(x);
		return true;
	}
}
