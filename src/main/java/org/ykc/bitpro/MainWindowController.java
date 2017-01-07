package org.ykc.bitpro;

import java.awt.Window;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.controlsfx.control.StatusBar;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.jfoenix.controls.JFXTextField;
import com.sun.glass.ui.Application;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainWindowController implements Initializable{

    @FXML // fx:id="borderPaneMainWindow"
    private BorderPane borderPaneMainWindow; // Value injected by FXMLLoader

    @FXML // fx:id="mItemExit"
    private MenuItem mItemExit; // Value injected by FXMLLoader
    
    @FXML // fx:id="bOpen"
    private Button bOpen; // Value injected by FXMLLoader

    @FXML // fx:id="bSave"
    private Button bSave; // Value injected by FXMLLoader

    @FXML // fx:id="bLoad"
    private Button bLoad; // Value injected by FXMLLoader

    @FXML // fx:id="statusBar"
    private StatusBar statusBar; // Value injected by FXMLLoader

    @FXML // fx:id="splitPaneFileExplorer"
    private SplitPane splitPaneFileExplorer; // Value injected by FXMLLoader

    @FXML // fx:id="tabPaneMain"
    private TabPane tabPaneMain; // Value injected by FXMLLoader

    @FXML // fx:id="tabCreate"
    private Tab tabCreate; // Value injected by FXMLLoader

    @FXML // fx:id="tabCombine"
    private Tab tabCombine; // Value injected by FXMLLoader

    @FXML // fx:id="tabLoad"
    private Tab tabLoad; // Value injected by FXMLLoader

    @FXML // fx:id="txtBitProSimpleName"
    private JFXTextField txtBitProSimpleName; // Value injected by FXMLLoader

    @FXML // fx:id="rbCreateView8bit"
    private RadioButton rbCreateView8bit; // Value injected by FXMLLoader

    @FXML // fx:id="toggleGroupBitSel"
    private ToggleGroup toggleGroupBitSel; // Value injected by FXMLLoader

    @FXML // fx:id="rbCreateView16bit"
    private RadioButton rbCreateView16bit; // Value injected by FXMLLoader

    @FXML // fx:id="rbCreateView32bit"
    private RadioButton rbCreateView32bit; // Value injected by FXMLLoader

    @FXML // fx:id="rbCreateView64bit"
    private RadioButton rbCreateView64bit; // Value injected by FXMLLoader

    @FXML // fx:id="tFieldName"
    private TextField tFieldName; // Value injected by FXMLLoader

    @FXML // fx:id="tBitSize"
    private TextField tBitSize; // Value injected by FXMLLoader

    @FXML // fx:id="tEnum"
    private TextField tEnum; // Value injected by FXMLLoader

    @FXML // fx:id="tDescription"
    private TextField tDescription; // Value injected by FXMLLoader

    @FXML // fx:id="bCreateViewAdd"
    private Button bCreateViewAdd; // Value injected by FXMLLoader

    @FXML
    private Button bCreateViewModify;

    @FXML // fx:id="bCreateViewDelete"
    private Button bCreateViewDelete; // Value injected by FXMLLoader

    @FXML // fx:id="bCreateViewUp"
    private Button bCreateViewUp; // Value injected by FXMLLoader

    @FXML // fx:id="bCreateViewDown"
    private Button bCreateViewDown; // Value injected by FXMLLoader

    @FXML // fx:id="tableViewCreate"
    private TableView<BitField> tableViewCreate; // Value injected by FXMLLoader

    @FXML // fx:id="tCreateColFieldName"
    private TableColumn<BitField, String> tCreateColFieldName; // Value injected by FXMLLoader

    @FXML // fx:id="tCreateColBitSize"
    private TableColumn<BitField, Integer> tCreateColBitSize; // Value injected by FXMLLoader

    @FXML // fx:id="tCreateColDesc"
    private TableColumn<BitField, String> tCreateColDesc; // Value injected by FXMLLoader

    @FXML // fx:id="tCreateColEnum"
    private TableColumn<BitField, String> tCreateColEnum; // Value injected by FXMLLoader

    @FXML // fx:id="rbLoadViewHex"
    private RadioButton rbLoadViewHex; // Value injected by FXMLLoader

    @FXML // fx:id="toggleGroupRadixSelect"
    private ToggleGroup toggleGroupRadixSelect; // Value injected by FXMLLoader

    @FXML // fx:id="rbLoadViewDecimal"
    private RadioButton rbLoadViewDecimal; // Value injected by FXMLLoader

    @FXML // fx:id="rbLoadViewBinary"
    private RadioButton rbLoadViewBinary; // Value injected by FXMLLoader

    @FXML // fx:id="txtLoadTabData"
    private JFXTextField txtLoadTabData; // Value injected by FXMLLoader

    @FXML // fx:id="gpaneLoadView"
    private GridPane gpaneLoadTab; // Value injected by FXMLLoader
    
    @FXML
    void exitApplication(ActionEvent event) {
    	closeProgram();
    }

	@FXML
    void setPreferences(ActionEvent event) {
    	
    }
    
    @FXML
    void openBitFile(ActionEvent event) {
    	openBitFile();
    }

    @FXML
    void saveBitFile(ActionEvent event) {
    	createBitFile();
    }

    @FXML
    void loadBitField(ActionEvent event) {
    	loadBitFile();
    }

    @FXML
    void addBitField(ActionEvent event) {
    	if(addBitField(tableViewCreate, tFieldName.getText(), tBitSize.getText(), tDescription.getText(), tEnum.getText()) == true)
    	{
    		tFieldName.clear();
    		tBitSize.clear();
    		tDescription.clear();
    		tEnum.clear();
    	}
    }

    @FXML
    void deleteBitField(ActionEvent event) {
    	removeBitField(tableViewCreate);
    }

    @FXML
    void modifyBitField(ActionEvent event) {
    	modifyBitField(tableViewCreate, tFieldName.getText(), tBitSize.getText(), tDescription.getText(), tEnum.getText());
    }

    @FXML
    void moveDownBitField(ActionEvent event) {
    	moveDownBitField(tableViewCreate);
    }

    @FXML
    void moveUpBitField(ActionEvent event) {
    	moveUpBitField(tableViewCreate);
    }

    @FXML
    void displayBinaryLoadView(ActionEvent event) {
    	if(rbLoadViewBinary.isSelected() == true){
    		LoadSimpleBPro.setRadix(GenericUtils.Radix.RADIX_BINARY);
    		txtLoadTabData.fireEvent(event);
    	}
    }

    @FXML
    void displayDecimalLoadView(ActionEvent event) {
    	if(rbLoadViewDecimal.isSelected() == true){
    		LoadSimpleBPro.setRadix(GenericUtils.Radix.RADIX_DECIMAL);
    		txtLoadTabData.fireEvent(event);
    	}
    }

    @FXML
    void displayHexLoadView(ActionEvent event) {
    	if(rbLoadViewHex.isSelected() == true){
    		LoadSimpleBPro.setRadix(GenericUtils.Radix.RADIX_HEX);
    		txtLoadTabData.fireEvent(event);
    	}
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Preferences.loadPreferences();
		bLoad.setDisable(true);
		/* Link tableViewCreate to Modal class BitField */
		tCreateColFieldName.setCellValueFactory(new PropertyValueFactory<>("name"));
		tCreateColBitSize.setCellValueFactory(new PropertyValueFactory<>("size"));
		tCreateColDesc.setCellValueFactory(new PropertyValueFactory<>("desc"));
		tCreateColEnum.setCellValueFactory(new PropertyValueFactory<>("enums"));
		tableViewCreate.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		tableViewCreate.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
		    if (newSelection != null) {
		    	BitField x = tableViewCreate.getSelectionModel().getSelectedItem();
		    	tFieldName.setText(x.getName());
	    		tBitSize.setText(x.getSize().toString());
	    		tDescription.setText(x.getDesc());
	    		tEnum.setText(x.getEnums());
		    }
		});
		/* Not using this pane for now */
		splitPaneFileExplorer.getItems().remove(splitPaneFileExplorer.getItems().get(0));
		tabPaneMain.getSelectionModel().selectedItemProperty().addListener(
			    new ChangeListener<Tab>() {
			        @Override
			        public void changed(ObservableValue<? extends Tab> ov, Tab oldTab, Tab newTab) {
			    		if(newTab == tabLoad)
			    		{
			    			bOpen.setDisable(true);
			    			bSave.setDisable(true);
			    			bLoad.setDisable(false);
			    		}
			    		else
			    		{
			    			bOpen.setDisable(false);
			    			bSave.setDisable(false);
			    			bLoad.setDisable(true);
			    		}
			        }
			    }
			);
	}

    void createBitFile() {
    	if(txtBitProSimpleName.getText().isEmpty() == true)
    	{
    		statusBar.setText("Please provide a name");
    		return;
    	}
    	Integer size = 32;
    	if(rbCreateView8bit.isSelected())
    	{
    		size = 8;
    	}
    	else if(rbCreateView16bit.isSelected())
    	{
    		size = 16;
    	}
    	else if(rbCreateView64bit.isSelected())
    	{
    		size = 64;
    	}
    	File file = BProUtils.saveBitFile(borderPaneMainWindow.getScene().getWindow());
    	
        if (file != null) {
        	if(CreateBPro.createSimpleXML(file, txtBitProSimpleName.getText(), size, tableViewCreate, statusBar) == true)
        	{
        		statusBar.setText("Save Success");
        	}
        }
        else
        {
        	statusBar.setText("Operation Cancelled");
        }

    }

    void openBitFile()
    {
    	File file = BProUtils.openBitFile(borderPaneMainWindow.getScene().getWindow());
 
        if (file != null) {
    		tFieldName.clear();
    		tBitSize.clear();
    		tDescription.clear();
    		tEnum.clear();
        	if(OpenBPro.openSimpleXML(file, txtBitProSimpleName, toggleGroupBitSel, tableViewCreate) == true)
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

	public boolean addBitField(TableView<BitField> tableViewCreate, String name, String size, String desc, String enums)
	{
		BitField row;
		try {
			row = new BitField(name, Integer.parseInt(size), desc, enums);
		} catch (Exception e) {
			statusBar.setText("Invalid Entry");
			return false;
		}
		tableViewCreate.getItems().add(row);
		statusBar.setText("Row Added");
		return true;
	}

	public boolean modifyBitField(TableView<BitField> tableViewCreate, String name, String size, String desc, String enums)
	{
		BitField row;
		try {
			row = new BitField(name, Integer.parseInt(size), desc, enums);
		} catch (Exception e) {
			statusBar.setText("Invalid Entry");
			return false;
		}
		BitField x = tableViewCreate.getSelectionModel().getSelectedItem();
		x.setName(row.getName());
		x.setDesc(row.getDesc());
		x.setEnums(row.getEnums());
		x.setSize(row.getSize());

		/* Workaround for tableview update issue */
		tableViewCreate.getColumns().get(0).setVisible(false);
		tableViewCreate.getColumns().get(0).setVisible(true);
		statusBar.setText("Row Updated");
		return true;
	}


	public void removeBitField(TableView<BitField> tableViewCreate)
	{
        ObservableList<BitField> selectedItems;
        selectedItems = tableViewCreate.getSelectionModel().getSelectedItems();
        tableViewCreate.getItems().removeAll(selectedItems);
	}

	public void moveUpBitField(TableView<BitField> tableViewCreate)
	{
        int selectedIndex = tableViewCreate.getSelectionModel().getSelectedIndex();
        if(selectedIndex <= 0)
        {
        	return;
        }
        BitField removedItem = tableViewCreate.getItems().remove(selectedIndex);
        int newIndex = selectedIndex - 1;
        tableViewCreate.getItems().add(newIndex, removedItem);
        tableViewCreate.getSelectionModel().clearAndSelect(newIndex);
	}

	public void moveDownBitField(TableView<BitField> tableViewCreate)
	{
        int selectedIndex = tableViewCreate.getSelectionModel().getSelectedIndex();
        int maxIndex = tableViewCreate.getItems().size() - 1;
        if(selectedIndex == maxIndex)
        {
        	return;
        }
        BitField removedItem = tableViewCreate.getItems().remove(selectedIndex);
        int newIndex = selectedIndex + 1;
        tableViewCreate.getItems().add(newIndex, removedItem);
        tableViewCreate.getSelectionModel().clearAndSelect(newIndex);
	}

	public void storeSimpleData(){
		File lastFile = Preferences.getLastLoadedFile();
		if(lastFile != null)
		{
			/* Store data in temp file */
			File tempFile = GenericUtils.getFileNewExtension(lastFile, "tmp");
			if(!tempFile.exists()){
				try {
					tempFile.createNewFile();
				} catch (IOException e) {
				}
			}
			if(tempFile.exists()){
				try {
					FileWriter x = new FileWriter(tempFile);
					x.write(txtLoadTabData.getText());
					x.close();
				} catch (IOException e) {
				}
			}
			
		}
	}
	public void loadBitFile(){
		storeSimpleData();
		File file = BProUtils.openBitFile(borderPaneMainWindow.getScene().getWindow());
    	
        if (file != null) {
        	Preferences.setLastLoadedFile(file);
        	File tmpFile = GenericUtils.getFileNewExtension(file, "tmp");
        	if(tmpFile.exists())
        	{
        		try {
					BufferedReader x = new BufferedReader(new FileReader(tmpFile));
					txtLoadTabData.setText(x.readLine());
					x.close();
				} catch (FileNotFoundException e) {
				} catch (IOException e) {
				}
        	}
        	else{
        		txtLoadTabData.setText("0");
        	}
    		Document xmlDoc = BProUtils.getDocument(file);
    		if(xmlDoc == null)
    		{
    			statusBar.setText("Load Failed");
    			return;
    		}
        	if(LoadSimpleBPro.loadSimpleXML((Element)(xmlDoc.getElementsByTagName("simple").item(0)), txtLoadTabData, gpaneLoadTab, statusBar) == true)
        	{
        		statusBar.setText("Load Success");
        	}
        	else
        	{
        		statusBar.setText("Load Failed");
        	}
        }
        else
        {
        	statusBar.setText("Operation Cancelled");
        }
	}
	
    private void closeProgram() {
    	Preferences.storePreferences();
    	storeSimpleData();
    	/* TODO : exit application */
	}

}
