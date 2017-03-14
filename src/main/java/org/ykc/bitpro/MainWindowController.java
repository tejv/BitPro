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

    @FXML // fx:id="tabSPro"
    private Tab tabSPro; // Value injected by FXMLLoader

    @FXML // fx:id="tabDPro"
    private Tab tabDPro; // Value injected by FXMLLoader

    @FXML // fx:id="tabLoad"
    private Tab tabLoad; // Value injected by FXMLLoader

    @FXML // fx:id="txtSProName"
    private JFXTextField txtSProName; // Value injected by FXMLLoader

    @FXML // fx:id="rbSPro8bit"
    private RadioButton rbSPro8bit; // Value injected by FXMLLoader

    @FXML // fx:id="tgSProBitSel"
    private ToggleGroup tgSProBitSel; // Value injected by FXMLLoader

    @FXML // fx:id="rbSPro16bit"
    private RadioButton rbSPro16bit; // Value injected by FXMLLoader

    @FXML // fx:id="rbSPro32bit"
    private RadioButton rbSPro32bit; // Value injected by FXMLLoader

    @FXML // fx:id="rbSPro64bit"
    private RadioButton rbSPro64bit; // Value injected by FXMLLoader

    @FXML // fx:id="txtSProFieldName"
    private TextField txtSProFieldName; // Value injected by FXMLLoader

    @FXML // fx:id="txtSProFieldSize"
    private TextField txtSProFieldSize; // Value injected by FXMLLoader

    @FXML // fx:id="txtSProFieldEnum"
    private TextField txtSProFieldEnum; // Value injected by FXMLLoader

    @FXML // fx:id="txtSProFieldDesc"
    private TextField txtSProFieldDesc; // Value injected by FXMLLoader

    @FXML // fx:id="bSProAdd"
    private Button bSProAdd; // Value injected by FXMLLoader

    @FXML
    private Button bSProModify;

    @FXML // fx:id="bSProDelete"
    private Button bSProDelete; // Value injected by FXMLLoader

    @FXML // fx:id="bSProUp"
    private Button bSProUp; // Value injected by FXMLLoader

    @FXML // fx:id="bSProDown"
    private Button bSProDown; // Value injected by FXMLLoader

    @FXML // fx:id="tViewSPro"
    private TableView<SProRow> tViewSPro; // Value injected by FXMLLoader

    @FXML // fx:id="tColSProFname"
    private TableColumn<SProRow, String> tColSProFname; // Value injected by FXMLLoader

    @FXML // fx:id="tColSProFsize"
    private TableColumn<SProRow, String> tColSProFsize; // Value injected by FXMLLoader

    @FXML // fx:id="tColSProFdesc"
    private TableColumn<SProRow, String> tColSProFdesc; // Value injected by FXMLLoader

    @FXML // fx:id="tColSProFenum"
    private TableColumn<SProRow, String> tColSProFenum; // Value injected by FXMLLoader

    @FXML // fx:id="rbLoadViewHex"
    private RadioButton rbLoadViewHex; // Value injected by FXMLLoader

    @FXML // fx:id="toggleGroupRadixSelect"
    private ToggleGroup toggleGroupRadixSelect; // Value injected by FXMLLoader

    @FXML // fx:id="rbLoadViewDecimal"
    private RadioButton rbLoadViewDecimal; // Value injected by FXMLLoader

    @FXML // fx:id="rbLoadViewBinary"
    private RadioButton rbLoadViewBinary; // Value injected by FXMLLoader

    @FXML // fx:id="txtLoadTabData"
    private TextField txtLoadTabData; // Value injected by FXMLLoader

    @FXML // fx:id="gpaneLoadView"
    private GridPane gpaneLoadTab; // Value injected by FXMLLoader

    @FXML // fx:id="tabSolver"
    private Tab tabSolver; // Value injected by FXMLLoader

    @FXML // fx:id="txtSolveEnter"
    private TextField txtSolveEnter; // Value injected by FXMLLoader

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

    @FXML // fx:id="txtAreaUtilGen"
    private JFXTextArea txtAreaUtilGen; // Value injected by FXMLLoader

    @FXML // fx:id="bUtilsGenSwitch"
    private Button bUtilsGenSwitch; // Value injected by FXMLLoader

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
    	SProOpen.run(borderPaneMainWindow, txtSProFieldName,
        		txtSProFieldSize, txtSProFieldDesc,
        		txtSProFieldEnum, txtSProName,
        		tgSProBitSel, tViewSPro, statusBar);
    }

    @FXML
    void saveBitFile(ActionEvent event) {
    	SProCreate.run(txtSProName, rbSPro8bit,
        		rbSPro16bit,rbSPro64bit, borderPaneMainWindow,
        		tViewSPro, statusBar);
    }

    @FXML
    void loadBitField(ActionEvent event) {
    	loadBitFile();
    }

    @FXML
    void sproAddField(ActionEvent event) {
    	SProCreate.addField(tViewSPro, txtSProFieldName.getText(),
    			txtSProFieldSize.getText(), txtSProFieldDesc.getText(),
    			txtSProFieldEnum.getText(), statusBar);

    }

    @FXML
    void sproDeleteField(ActionEvent event) {
    	UtilsTableView.removeSelItem(tViewSPro);
    }

    @FXML
    void sproModifyField(ActionEvent event) {
    	SProCreate.modifyField(tViewSPro, txtSProFieldName.getText(),
    			txtSProFieldSize.getText(), txtSProFieldDesc.getText(),
    			txtSProFieldEnum.getText(), statusBar);
    }

    @FXML
    void sproMoveFieldDown(ActionEvent event) {
    	UtilsTableView.moveDownSelItem(tViewSPro);
    }

    @FXML
    void sproMoveFieldUp(ActionEvent event) {
    	UtilsTableView.moveUpSelItem(tViewSPro);
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
    		SProLoad.setRadix(Utils.Radix.RADIX_BINARY);
    		txtLoadTabData.fireEvent(event);
    	}
    }

    @FXML
    void displayDecimalLoadView(ActionEvent event) {
    	if(rbLoadViewDecimal.isSelected() == true){
    		SProLoad.setRadix(Utils.Radix.RADIX_DECIMAL);
    		txtLoadTabData.fireEvent(event);
    	}
    }

    @FXML
    void displayHexLoadView(ActionEvent event) {
    	if(rbLoadViewHex.isSelected() == true){
    		SProLoad.setRadix(Utils.Radix.RADIX_HEX);
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

    @FXML
    void utilsGenSwitchCase(ActionEvent event) {
    	utilsGenerateSwitchCase(event);
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
			tabPaneMain.getSelectionModel().select(tabSPro);
			break;
		case "tabCombine":
			tabPaneMain.getSelectionModel().select(tabDPro);
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
		tColSProFname.setCellValueFactory(new PropertyValueFactory<>("name"));
		tColSProFsize.setCellValueFactory(new PropertyValueFactory<>("size"));
		tColSProFdesc.setCellValueFactory(new PropertyValueFactory<>("desc"));
		tColSProFenum.setCellValueFactory(new PropertyValueFactory<>("enums"));
		tViewSPro.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		tViewSPro.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
		    if (newSelection != null) {
		    	SProRow x = tViewSPro.getSelectionModel().getSelectedItem();
		    	txtSProFieldName.setText(x.getName());
	    		txtSProFieldSize.setText(x.getSize().toString());
	    		txtSProFieldDesc.setText(x.getDesc());
	    		txtSProFieldEnum.setText(x.getEnums());
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

	public void storeSimpleData(){
		File lastFile = Preferences.getLastLoadedFile();
		if(lastFile != null)
		{
			/* Store data in temp file */
			File tempFile = Utils.getFileNewExtension(lastFile, "tmp");
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
		File file = UtilsBPro.openLoadFile(borderPaneMainWindow.getScene().getWindow());
		if(loadFile(file) == false){
			curLoadedFile = null;
        	statusBar.setText("Operation Cancelled");
		}
	}

	private boolean loadFile(File file){
        if (file != null) {
        	Preferences.setLastLoadedFile(file);
        	File tmpFile = Utils.getFileNewExtension(file, "tmp");
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
    		Document xmlDoc = UtilsBPro.getW3cDomDoc(file);
    		if(xmlDoc == null)
    		{
    			statusBar.setText("Load Failed");
    			return true;
    		}
        	if(SProLoad.run((Element)(xmlDoc.getElementsByTagName("simple").item(0)), txtLoadTabData, gpaneLoadTab, statusBar) == true)
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
		if(ParseRegMap.parse(txtAreaParse, statusBar, tViewSPro, txtSProName, rbSPro32bit) == true)
		{
			tabPaneMain.getSelectionModel().select(tabSPro);
			bSave.fireEvent(event);
		}
	}

	private void genMacros(ActionEvent event){
		if(curLoadedFile != null){
			TextViewer.display("Generated Macros", SProMacroGen.run(curLoadedFile, txtLoadViewPrefix.getText()));
		}
		else {
			statusBar.setText("Operation Fail: No file loaded");
		}
	}

    private void utilsGenerateFn(ActionEvent event) {
		TextViewer.display("Generated Code", UtilsFSMGen.run(txtUtilsFnNamePrefix.getText(),
				txtUtilsFnPrefix.getText(), txtUtilsFnPostFix.getText(), txtAreaUtil1StateNames, txtAreaUtil1EventNames));
	}

	private void utilsGenerateSwitchCase(ActionEvent event) {
		TextViewer.display("Generated Code", UtilsSwitchCaseGen.run(txtAreaUtilGen));
	}

}

