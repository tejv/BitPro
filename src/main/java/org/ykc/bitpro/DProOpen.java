package org.ykc.bitpro;

import java.io.File;

import org.controlsfx.control.StatusBar;
import org.jdom2.Document;
import org.jdom2.Element;

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
    private JFXTextField txtDProBPath;
    
	public DProOpen(Stage primaryStage, TableView<DProRow> tViewDPro, JFXTextField txtDProName,
			TextField txtDProTypeName, TextField txtDProFieldName, TextField txtDProFieldSize,
			TextField txtDProFieldRPath, TextField txtDProFieldDesc, BorderPane borderPaneMainWindow,
			StatusBar statusBar,JFXTextField txtDProBPath) {
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
		this.txtDProBPath = txtDProBPath;
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
        		Preferences.setLastDesignFile(file);
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
	
	public boolean open(File fileName){
		tViewDPro.getItems().clear();
		Document xmlDoc = UtilsBPro.getJDOM2Doc(fileName);
		if((xmlDoc == null) || (!xmlDoc.getRootElement().getChild("head").getChildText("dtype").matches("design")))
		{
			return false;
		}
		txtDProName.setText(xmlDoc.getRootElement().getChild("head").getChildText("dname"));
		String valString = xmlDoc.getRootElement().getChild("head").getChildText("dBasePath");
		File file = new File(valString);
		txtDProBPath.setText(file.getAbsolutePath());
		Element designElement = xmlDoc.getRootElement();
		int listOfFields = UtilsBPro.getDProFieldsCount(designElement);
		for(int i = 0; i < listOfFields; i++){
			DProRow x;
			try {
				x = new DProRow("Error","Error","1","","Error");
			} catch (Exception e) {
				return false;
			}
			x.setType(UtilsBPro.getDProFieldType(designElement, i));		
			x.setName(UtilsBPro.getDProFieldName(designElement, i));
			x.setSize(UtilsBPro.getDProFieldSize(designElement, i).toString());
			x.setDesc(UtilsBPro.getDProFieldDesc(designElement, i));
			x.setRpath(UtilsBPro.getDProFieldPath(designElement, i));
			tViewDPro.getItems().add(x);
		}
		return true;
	}


}
