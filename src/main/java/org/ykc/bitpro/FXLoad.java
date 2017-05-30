package org.ykc.bitpro;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;
import org.controlsfx.control.StatusBar;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.scene.control.Label;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class FXLoad {
	private Stage primaryStage;
	private JFXListView<String> lViewFX;
	private JFXTextField txtFxName;
    private GridPane gPaneFxLoad;
    private JFXTextField txtFxResult;
    private JFXTextArea txtAreaFxDesc;
    private StatusBar statusBar;
    private String formulaString = "";
    private String descString = "";
	private ObservableList<File> fxList = FXCollections.observableArrayList();
	ObservableList<JFXTextField> txtList = FXCollections.observableArrayList();
	ObservableList<String> varList = FXCollections.observableArrayList();
	ObservableList<String> valList = FXCollections.observableArrayList();
	boolean isLoaded = false;


	public FXLoad(Stage primaryStage, JFXListView<String> lViewFX, JFXTextField txtFxName,
			GridPane gPaneFxLoad, JFXTextField txtFxResult, JFXTextArea txtAreaFxDesc, StatusBar statusBar) {
		this.primaryStage = primaryStage;
		this.lViewFX = lViewFX;
		this.txtFxName = txtFxName;
		this.gPaneFxLoad = gPaneFxLoad;
		this.txtFxResult = txtFxResult;
		this.txtAreaFxDesc = txtAreaFxDesc;
		this.statusBar = statusBar;

		lViewFX.setOnMouseClicked(new EventHandler<MouseEvent>() {

		    @Override
		    public void handle(MouseEvent click) {
		        int idx = lViewFX.getSelectionModel().getSelectedIndex();
		        File loadFile = fxList.get(idx);
		        if(loadFile != null)
		        {
		        	load(loadFile);
		        }
		    }
		});

	}
	public void browse() {
		DirectoryChooser chooser = new DirectoryChooser();
		chooser.setTitle("F(x) (.eq) Folder");
		File defaultDirectory = Preferences.getFxLastDirectory();

		if(defaultDirectory.exists()){
			chooser.setInitialDirectory(defaultDirectory);
		}
		File selDir = chooser.showDialog(primaryStage);
		Preferences.setFxLastDirectory(selDir);
		browseDir(selDir);
	}

	public void browseDir(File selDir)
	{
		if(selDir != null){
			lViewFX.getItems().clear();
			fxList.clear();
			for(File f: selDir.listFiles()){
				String extension = Utils.getFileExtension(f);
				if(extension.equals("eq"))
				{
					fxList.add(f);
					lViewFX.getItems().add(FilenameUtils.getBaseName(f.getName()));
				}
			}
		}
	}

	public void load(File loadFile) {
		if((loadFile == null) || (!loadFile.exists())){
			return;
		}
		saveOldData();
		isLoaded = true;
		Preferences.setFxLastLoadedFile(loadFile);
		txtFxName.setText(FilenameUtils.getBaseName(loadFile.getName()));
		gPaneFxLoad.getChildren().clear();
		formulaString = "";
		descString = "";
		txtList.clear();
		varList.clear();
		valList.clear();

		/* TODO: Load saved data */
		loadOldData();
		BufferedReader in = null;
		try {
		    in = new BufferedReader(new FileReader(loadFile));
		    String str;
		    boolean formula_found = false;
		    while ((str = in.readLine()) != null) {
		    	String xString = str.trim();
		    	if(xString.equals("")){
		    		continue;
		    	}
		    	if(xString.startsWith("#EQ"))
		    	{
		    		formula_found = true;
		    		continue;
		    	}
		    	if(formula_found == true){
		    		if(!xString.startsWith("#")){
		    			formulaString += xString;
		    		}
		    		else {
		    			formula_found = false;
		    		}
		    	}
		    	else{
		    		descString += xString + "\n";
		    	}
		    }
		    txtAreaFxDesc.setText(descString);
		    Pattern pattern = Pattern.compile("[a-z][a-z0-9_]*", Pattern.CASE_INSENSITIVE);
		    Matcher matcher = pattern.matcher(formulaString);
		    int rowCount = 0;
		    while(matcher.find()){
		    	String varString = matcher.group();
		    	varList.add(varString);
		    	Label label = new Label(varString);
		    	JFXTextField tbox = new JFXTextField();
		    	tbox.setEditable(true);
		    	gPaneFxLoad.add(label, 0, rowCount);
		    	gPaneFxLoad.add(tbox, 1, rowCount);
		    	txtList.add(tbox);

				tbox.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						updateResult();
					}
				});

				if(valList.size() > rowCount){
					tbox.setText(valList.get(rowCount));
				}
				else{
					tbox.setText("1");
				}
				rowCount++;
		    }
		    updateResult();

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

	private void loadOldData() {
		File lastFile = Preferences.getFxLastLoadedFile();
		if(lastFile != null)
		{
			/* Get temp file name */
			File tempFile = Utils.getFileNewExtension(lastFile, "tmp");
			if(tempFile.exists()){
				try {
					// Open the file
					FileInputStream fstream = new FileInputStream(tempFile);
					BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

					String strLine;

					//Read File Line By Line
					while ((strLine = br.readLine()) != null)   {
					  valList.add(strLine.trim());
					}

					//Close the input stream
					br.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}
	
	public void saveOldData() {
		if(isLoaded == false)
		{
			return;
		}

		File lastFile = Preferences.getFxLastLoadedFile();
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
			String valString = "";
			for(int i = 0; i < varList.size(); i++)
			{
				valString += txtList.get(i).getText() + "\n";
			}

			x.write(valString);
			x.close();
		} catch (IOException e) {
		}
	}


	private void updateResult() {
		String solveString = formulaString;
		for(int i = 0; i < varList.size(); i++)
		{
			solveString = solveString.replaceAll(varList.get(i), txtList.get(i).getText());
		}
		XpressionSolver xpressionSolver = new XpressionSolver();
		xpressionSolver.solveSimple(solveString, txtFxResult);
	}

}
