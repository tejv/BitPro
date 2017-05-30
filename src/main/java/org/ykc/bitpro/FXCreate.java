package org.ykc.bitpro;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import javax.management.openmbean.OpenDataException;

import org.apache.commons.io.FilenameUtils;
import org.controlsfx.control.StatusBar;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class FXCreate {
	private TextField txtFxCreateName;
	private BorderPane borderPaneMainWindow;
    private StatusBar statusBar;
    private JFXTextArea txtAreaFxFormula;
	private ObservableList<File> varList = FXCollections.observableArrayList();

	public FXCreate(TextField txtFxCreateName, JFXTextArea txtAreaFxFormula, BorderPane borderPaneMainWindow, StatusBar statusBar) {
		this.txtFxCreateName = txtFxCreateName;
		this.statusBar = statusBar;
		this.txtAreaFxFormula = txtAreaFxFormula;
		this.borderPaneMainWindow = borderPaneMainWindow;
	}

	public void open(){
		File file = UtilsBPro.openEQFile(borderPaneMainWindow.getScene().getWindow());
		if(file != null)
		{
			txtFxCreateName.setText(FilenameUtils.getBaseName(file.getName()));
			txtAreaFxFormula.setText("");
			BufferedReader in = null;
			try {
			    in = new BufferedReader(new FileReader(file));
			    String str;
			    while ((str = in.readLine()) != null) {
			    	txtAreaFxFormula.appendText(str + "\n");
			    }
			} 
			catch (IOException e) {
			} 
			finally {
			    try { 
			    	if(in != null)
			    	in.close();
			    } 
			    catch (Exception ex){ }
			}
			
		}
	}

	public void save(){
    	if(txtFxCreateName.getText().trim().isEmpty() == true)
    	{
    		statusBar.setText("Please provide a name");
    		return;
    	}

    	File file = UtilsBPro.saveEQFile(borderPaneMainWindow.getScene().getWindow(), txtFxCreateName.getText().trim());

        if (file != null) {
        	if(create(file) == true)
        	{
        		statusBar.setText("Save Success");
        	}
        	else {
        		statusBar.setText("Save Failed");
			}
        	
        }
        else
        {
        	statusBar.setText("Operation Cancelled");
        }
	}
	
	private boolean create(File file){
	    ObservableList<CharSequence> paragraph = txtAreaFxFormula.getParagraphs();
	    Iterator<CharSequence>  iter = paragraph.iterator();
	    try
	    {
	        BufferedWriter bf = new BufferedWriter(new FileWriter(file));
	        while(iter.hasNext())
	        {
	            CharSequence seq = iter.next();
	            bf.append(seq);
	            bf.newLine();
	        }
	        bf.flush();
	        bf.close();
	        return true;
	    }
	    catch (IOException e)
	    {
	        e.printStackTrace();
	        return false;
	    }		
	}
}
