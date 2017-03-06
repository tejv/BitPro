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
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.print.attribute.standard.PresentationDirection;

import org.controlsfx.control.StatusBar;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.sun.glass.ui.Application;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
import javafx.stage.WindowEvent;

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

    @FXML
    private Button bGenMacros;

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
    private TableView<BFieldSimpleRow> tableViewCreate; // Value injected by FXMLLoader

    @FXML // fx:id="tCreateColFieldName"
    private TableColumn<BFieldSimpleRow, String> tCreateColFieldName; // Value injected by FXMLLoader

    @FXML // fx:id="tCreateColBitSize"
    private TableColumn<BFieldSimpleRow, Integer> tCreateColBitSize; // Value injected by FXMLLoader

    @FXML // fx:id="tCreateColDesc"
    private TableColumn<BFieldSimpleRow, String> tCreateColDesc; // Value injected by FXMLLoader

    @FXML // fx:id="tCreateColEnum"
    private TableColumn<BFieldSimpleRow, String> tCreateColEnum; // Value injected by FXMLLoader

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

    @FXML // fx:id="tabSolver"
    private Tab tabSolver; // Value injected by FXMLLoader

    @FXML // fx:id="txtSolveEnter"
    private JFXTextField txtSolveEnter; // Value injected by FXMLLoader

    @FXML // fx:id="txtSolveShowHex"
    private TextField txtSolveShowHex; // Value injected by FXMLLoader

    @FXML // fx:id="txtSolveShowDecimal"
    private TextField txtSolveShowDecimal; // Value injected by FXMLLoader

    @FXML // fx:id="txtSolveShowBinary"
    private TextField txtSolveShowBinary; // Value injected by FXMLLoader

    @FXML // fx:id="gPaneXsolveTab"
    private GridPane gPaneXsolveTab; // Value injected by FXMLLoader

    @FXML // fx:id="txtAreaParse"
    private JFXTextArea txtAreaParse; // Value injected by FXMLLoader

    @FXML // fx:id="bParseRegMap"
    private Button bParseRegMap; // Value injected by FXMLLoader

    @FXML // fx:id="tabParse"
    private Tab tabParse; // Value injected by FXMLLoader

    @FXML // fx:id="lbNameLoadView"
    private Label lbNameLoadView; // Value injected by FXMLLoader

    @FXML // fx:id="aPaneMain"
    private AnchorPane aPaneMain; // Value injected by FXMLLoader

    @FXML // fx:id="txtLoadViewPrefix"
    private TextField txtLoadViewPrefix; // Value injected by FXMLLoader

    @FXML // fx:id="txtAreaUtil1StateNames"
    private JFXTextArea txtAreaUtil1StateNames; // Value injected by FXMLLoader

    @FXML // fx:id="txtAreaUtil1EventNames"
    private JFXTextArea txtAreaUtil1EventNames; // Value injected by FXMLLoader

    @FXML // fx:id="txtUtilsFnPrefix"
    private TextField txtUtilsFnPrefix; // Value injected by FXMLLoader

    @FXML // fx:id="txtUtilsFnPostFix"
    private TextField txtUtilsFnPostFix; // Value injected by FXMLLoader

    @FXML // fx:id="bUtilsGenerateFn"
    private Button bUtilsGenerateFn; // Value injected by FXMLLoader

    @FXML // fx:id="txtUtilsFnNamePrefix"
    private TextField txtUtilsFnNamePrefix; // Value injected by FXMLLoader

    @FXML // fx:id="tabUtils"
    private Tab tabUtils; // Value injected by FXMLLoader
    
    private Stage myStage;
    private File curLoadedFile = null;

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
//    		tFieldName.clear();
//    		tBitSize.clear();
//    		tDescription.clear();
//    		tEnum.clear();
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
    void solveExpression(ActionEvent event) {
    	solveExpression();
    }

    @FXML
    void solveReverseExpression(ActionEvent event) {
    	solveReverseXpression(event);
    }

	@FXML
    void showAboutMe(ActionEvent event) {
    	displayAboutMe();
    }

	@FXML
    void displayBinaryLoadView(ActionEvent event) {
    	if(rbLoadViewBinary.isSelected() == true){
    		LoadSimpleBPro.setRadix(GUtils.Radix.RADIX_BINARY);
    		txtLoadTabData.fireEvent(event);
    	}
    }

    @FXML
    void displayDecimalLoadView(ActionEvent event) {
    	if(rbLoadViewDecimal.isSelected() == true){
    		LoadSimpleBPro.setRadix(GUtils.Radix.RADIX_DECIMAL);
    		txtLoadTabData.fireEvent(event);
    	}
    }

    @FXML
    void displayHexLoadView(ActionEvent event) {
    	if(rbLoadViewHex.isSelected() == true){
    		LoadSimpleBPro.setRadix(GUtils.Radix.RADIX_HEX);
    		txtLoadTabData.fireEvent(event);
    	}
    }

    @FXML
    void parseSimpleRegmap(ActionEvent event) {
    	parseSimpleRegister(event);
    }

    @FXML
    void generateMacros(ActionEvent event) {
    	genMacros(event);
    }

    @FXML
    void utilsGenFunction(ActionEvent event) {
    	utilsGenerateFn(event);
    }

	public void setStage(Stage stage) {
        myStage = stage;
		myStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
		      public void handle(WindowEvent we) {
		    	  storeDataBeforeClose();
		      }
		  });
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Preferences.loadPreferences();
		txtLoadViewPrefix.setText(Preferences.getLoadViewPrefixValue());
		txtUtilsFnNamePrefix.setText(Preferences.getUtilsFnNamePrefixString());
		txtUtilsFnPrefix.setText(Preferences.getUtilsFnPrefixString());
		txtUtilsFnPostFix.setText(Preferences.getUtilsFnPostfixString());
		txtSolveEnter.setText(Preferences.getxSolveLastData());
		solveExpression();
		enDisTabButtons(Preferences.getLastOpenTabName());
		loadFile(Preferences.getLastLoadedFile());
		switch(Preferences.getLastOpenTabName())
		{
		case "tabLoad":
			tabPaneMain.getSelectionModel().select(tabLoad);
			break;
		case "tabCreate":
			tabPaneMain.getSelectionModel().select(tabCreate);
			break;
		case "tabCombine":
			tabPaneMain.getSelectionModel().select(tabCombine);
			break;
		case "tabSolver":
			tabPaneMain.getSelectionModel().select(tabSolver);
			break;
		case "tabParse":
			tabPaneMain.getSelectionModel().select(tabParse);
		case "tabUtils":
			tabPaneMain.getSelectionModel().select(tabUtils);
			break;
		}
		/* Link tableViewCreate to Modal class BitField */
		tCreateColFieldName.setCellValueFactory(new PropertyValueFactory<>("name"));
		tCreateColBitSize.setCellValueFactory(new PropertyValueFactory<>("size"));
		tCreateColDesc.setCellValueFactory(new PropertyValueFactory<>("desc"));
		tCreateColEnum.setCellValueFactory(new PropertyValueFactory<>("enums"));
		tableViewCreate.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		tableViewCreate.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
		    if (newSelection != null) {
		    	BFieldSimpleRow x = tableViewCreate.getSelectionModel().getSelectedItem();
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
			        	enDisTabButtons(newTab.getId());
			        	Preferences.setLastOpenTabName(newTab.getId());
			        }
			    }
			);
	}

	private void enDisTabButtons(String tabId){
		if(tabId.equals("tabLoad"))
		{
			bOpen.setDisable(true);
			bSave.setDisable(true);
			bLoad.setDisable(false);
			bGenMacros.setDisable(false);
		}
		else if( (tabId.equals("tabSolver") )|| (tabId.equals("tabUtils")) ){
			bOpen.setDisable(true);
			bSave.setDisable(true);
			bLoad.setDisable(true);
			bGenMacros.setDisable(true);
		}
		else if(tabId.equals("tabParse"))
		{
			bOpen.setDisable(true);
			bSave.setDisable(true);
			bLoad.setDisable(true);
			bGenMacros.setDisable(true);
		}
		else
		{
			bOpen.setDisable(false);
			bSave.setDisable(false);
			bLoad.setDisable(true);
			bGenMacros.setDisable(true);
		}
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
    	File file = BProUtils.saveSimpleBitFile(borderPaneMainWindow.getScene().getWindow(), txtBitProSimpleName.getText());

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
    	File file = BProUtils.openSimpleBitFile(borderPaneMainWindow.getScene().getWindow());

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

	public boolean addBitField(TableView<BFieldSimpleRow> tableViewCreate, String name, String size, String desc, String enums)
	{
		BFieldSimpleRow row;
		try {
			row = new BFieldSimpleRow(name, size, desc, enums);
		} catch (Exception e) {
			statusBar.setText("Invalid Entry");
			return false;
		}
		tableViewCreate.getItems().add(row);
		statusBar.setText("Row Added");
		return true;
	}

	public boolean modifyBitField(TableView<BFieldSimpleRow> tableViewCreate, String name, String size, String desc, String enums)
	{
		BFieldSimpleRow row;
		try {
			row = new BFieldSimpleRow(name, size, desc, enums);
		} catch (Exception e) {
			statusBar.setText("Invalid Entry");
			return false;
		}
		BFieldSimpleRow x = tableViewCreate.getSelectionModel().getSelectedItem();
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


	public void removeBitField(TableView<BFieldSimpleRow> tableViewCreate)
	{
        ObservableList<BFieldSimpleRow> selectedItems;
        selectedItems = tableViewCreate.getSelectionModel().getSelectedItems();
        tableViewCreate.getItems().removeAll(selectedItems);
	}

	public void moveUpBitField(TableView<BFieldSimpleRow> tableViewCreate)
	{
        int selectedIndex = tableViewCreate.getSelectionModel().getSelectedIndex();
        if(selectedIndex <= 0)
        {
        	return;
        }
        BFieldSimpleRow removedItem = tableViewCreate.getItems().remove(selectedIndex);
        int newIndex = selectedIndex - 1;
        tableViewCreate.getItems().add(newIndex, removedItem);
        tableViewCreate.getSelectionModel().clearAndSelect(newIndex);
	}

	public void moveDownBitField(TableView<BFieldSimpleRow> tableViewCreate)
	{
        int selectedIndex = tableViewCreate.getSelectionModel().getSelectedIndex();
        int maxIndex = tableViewCreate.getItems().size() - 1;
        if(selectedIndex == maxIndex)
        {
        	return;
        }
        BFieldSimpleRow removedItem = tableViewCreate.getItems().remove(selectedIndex);
        int newIndex = selectedIndex + 1;
        tableViewCreate.getItems().add(newIndex, removedItem);
        tableViewCreate.getSelectionModel().clearAndSelect(newIndex);
	}

	public void storeSimpleData(){
		File lastFile = Preferences.getLastLoadedFile();
		if(lastFile != null)
		{
			/* Store data in temp file */
			File tempFile = GUtils.getFileNewExtension(lastFile, "tmp");
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
		File file = BProUtils.openBitFileForLoad(borderPaneMainWindow.getScene().getWindow());
		if(loadFile(file) == false){
			curLoadedFile = null;
        	statusBar.setText("Operation Cancelled");
		}
	}

	private boolean loadFile(File file){
        if (file != null) {
        	Preferences.setLastLoadedFile(file);
        	File tmpFile = GUtils.getFileNewExtension(file, "tmp");
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
    			return true;
    		}
        	if(LoadSimpleBPro.loadSimpleXML((Element)(xmlDoc.getElementsByTagName("simple").item(0)), txtLoadTabData, gpaneLoadTab, statusBar) == true)
        	{
        		statusBar.setText("Load Success");
        	}
        	else
        	{
        		statusBar.setText("Load Failed");
        	}
        	lbNameLoadView.setText(file.getName());
    		curLoadedFile = file;
        	return true;
        }
        else
        {
        	return false;
        }
	}

	public void storeDataBeforeClose(){
		Preferences.setLoadViewPrefixValue(txtLoadViewPrefix.getText());
		Preferences.setUtilsFnNamePrefixString(txtUtilsFnNamePrefix.getText());
		Preferences.setUtilsFnPrefixString(txtUtilsFnPrefix.getText());
		Preferences.setUtilsFnPostfixString(txtUtilsFnPostFix.getText());
    	Preferences.storePreferences();
    	storeSimpleData();
	}

    private void closeProgram() {
    	storeDataBeforeClose();
    	Platform.exit();
	}

    private void solveExpression() {
		XpressionSolver x = new XpressionSolver();
		x.solve(txtSolveEnter, txtSolveShowHex, txtSolveShowBinary, txtSolveShowDecimal, statusBar, gPaneXsolveTab);
		Preferences.setxSolveLastData(txtSolveEnter.getText());
	}

	private void displayAboutMe() {
		Properties prop = new Properties();
		InputStream input;
		try {
			input = getClass().getResource("/version.properties").openStream();
			prop.load(input);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String ver = prop.getProperty("MAJOR_VERSION") + "."+ prop.getProperty("MINOR_VERSION") + "." + prop.getProperty("BUILD_NO");
		MsgBox.display("About Me", "BitPro\nVersion: "+ ver +"\nAuthor: Tejender Sheoran\nEmail: tejendersheoran@gmail.com\nCopyright(C) (2016-2017) Tejender Sheoran\nThis program is free software. You can redistribute it and/or modify it\nunder the terms of the GNU General Public License Ver 3.\n<http://www.gnu.org/licenses/>");
	}

    private void solveReverseXpression(ActionEvent event) {
		XpressionSolver x = new XpressionSolver();
		x.reverseSolve(txtSolveEnter, txtSolveShowHex, txtSolveShowBinary, txtSolveShowDecimal, statusBar, gPaneXsolveTab, event);
		Preferences.setxSolveLastData(txtSolveEnter.getText());

	}

	private void parseSimpleRegister(ActionEvent event) {
		if(ParseSimpleRegister.parse(txtAreaParse, statusBar, tableViewCreate, txtBitProSimpleName, rbCreateView32bit) == true)
		{
			tabPaneMain.getSelectionModel().select(tabCreate);
			bSave.fireEvent(event);
		}
	}

	private void genMacros(ActionEvent event){
		if(curLoadedFile != null){
			MacroView.display("Generated Macros", MacroGenerator.run(curLoadedFile, txtLoadViewPrefix.getText()));
		}
		else {
			statusBar.setText("Operation Fail: No file loaded");
		}
	}

    private void utilsGenerateFn(ActionEvent event) {
		MacroView.display("Generated objects", FnGenerator.run(txtUtilsFnNamePrefix.getText(),
				txtUtilsFnPrefix.getText(), txtUtilsFnPostFix.getText(), txtAreaUtil1StateNames, txtAreaUtil1EventNames));
	}

}

