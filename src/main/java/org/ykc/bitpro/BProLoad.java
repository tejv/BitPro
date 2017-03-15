package org.ykc.bitpro;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.controlsfx.control.StatusBar;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class BProLoad {
	private TextField txtLoadEnterData;
	private TextField txtLoadPrefix;	
	private BorderPane borderPaneMainWindow;
	private StatusBar statusBar;
	private GridPane gpaneLoad;
	private Label lbLoadName;
	private boolean isLoaded = false;
	private String fileType = "";
	private Integer rowIdx = 0;
	private Integer colIdx = 0;
	
	
	public BProLoad(TextField txtLoadEnterData, TextField txtLoadPrefix, BorderPane borderPaneMainWindow, StatusBar statusBar,
			GridPane gpaneLoad, Label lbLoadName) {
		this.txtLoadEnterData = txtLoadEnterData;
		this.txtLoadPrefix = txtLoadPrefix;
		this.borderPaneMainWindow = borderPaneMainWindow;
		this.statusBar = statusBar;
		this.gpaneLoad = gpaneLoad;
		this.lbLoadName = lbLoadName;
		
	}

	public void load(){
		saveLoadedData();
		File file = UtilsBPro.openLoadFile(borderPaneMainWindow.getScene().getWindow());
		loadFile(file);

	}
	
	public void loadFile(File file){
        if (file != null) {
        	Preferences.setLastLoadedFile(file);
    		String extension = Utils.getFileExtension(file);
    		if(extension.equals("cpro"))
    		{
    			fileType = "cpro";
    		}
    		else if(extension.equals("spro"))
    		{
    			fileType = "spro";
    		}        	
        	retrieveSavedData(file);

    		Document xmlDoc = UtilsBPro.getW3cDomDoc(file);
    		if(xmlDoc == null)
    		{
    			statusBar.setText("Load Failed: Not an XML file");
    			return;
    		}
    		
    		if(fileType.equals("spro"))
    		{
            	if(SProLoad.run((Element)(xmlDoc.getElementsByTagName("simple").item(0)), txtLoadEnterData, gpaneLoad, statusBar) == true)
            	{
            		statusBar.setText("Load Success");
            	}
            	else
            	{
            		statusBar.setText("Load Failed");
            	}
    		}else if(fileType.equals("cpro"))
    		{
    			/* TODO : Load simple elements and list of simple elements and set events to update simple elements as well */
    		}

        	lbLoadName.setText(file.getName());
        	isLoaded = true;
        }
        else
        {
        	statusBar.setText("Load Failed: File not found");
        }		
	}
	

	private void retrieveSavedData(File file){
    	File tmpFile = Utils.getFileNewExtension(file, "tmp");
    	if(tmpFile.exists())
    	{
    		try {
				BufferedReader x = new BufferedReader(new FileReader(tmpFile));
				if(fileType.equals("spro"))
				{
					txtLoadEnterData.setText(x.readLine());
				}
				else if(fileType.equals("cpro"))
				{
					/* TODO: Either retrieve all data in RAM or just retrive only current row-col */
				}				
				
				x.close();
			} catch (FileNotFoundException e) {
			} catch (IOException e) {
			}
    	}
    	else{
    		txtLoadEnterData.setText("0");
    		if(fileType.equals("cpro"))
			{
				/* TODO: Need to initialize temp storage to 0 */
			}	    		
    	}		
	}
	
	public void saveLoadedData(){
		if(isLoaded == false)
		{
			return;
		}
		File lastFile = Preferences.getLastLoadedFile();
		if(lastFile != null)
		{
			/* Get temp file name */
			File tempFile = Utils.getFileNewExtension(lastFile, "tmp");
			if(!tempFile.exists()){
				try {
					tempFile.createNewFile();
				} catch (IOException e) {
				}
			}
			if(tempFile.exists()){
				writeSavedData(tempFile);	
			}
		}
	}
	
	private void writeSavedData(File file)
	{
		try {
			FileWriter x = new FileWriter(file);
			if(fileType.equals("spro"))
			{
				x.write(txtLoadEnterData.getText());
			}
			else if(fileType.equals("cpro"))
			{
				/* TODO: Based on rowIdx and colIdx modify existing file with correct data */
				/* Also update row type (8 bit 16 bit or 32 bit )*/
			}
			
			x.close();
		} catch (IOException e) {
		}		
	}
	

	
	public void genMacros(){
		File curLoadedFile = Preferences.getLastLoadedFile();
		String prefix ="";
		if(txtLoadPrefix.getText() != null)
		{
			prefix = txtLoadPrefix.getText().trim();
		}
		if(curLoadedFile != null){
			String extension = Utils.getFileExtension(curLoadedFile);
			if(extension.equals("cpro"))
			{
				CProMacroGen.run(curLoadedFile, prefix);
			}
			else if(extension.equals("spro"))
			{
				TextViewer.display("Generated Macros", SProMacroGen.run(curLoadedFile, prefix));				
			}
		}
		else {
			statusBar.setText("Operation Fail: No file loaded");
		}
	}
}
