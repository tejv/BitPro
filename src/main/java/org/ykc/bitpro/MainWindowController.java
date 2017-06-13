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

import com.jfoenix.controls.JFXListView;
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
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.input.MouseEvent;
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

    @FXML // fx:id="bGenBinary"
    private Button bGenBinary; // Value injected by FXMLLoader

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

    @FXML // fx:id="bSProModify"
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

    @FXML // fx:id="tgLoadRadixSel"
    private ToggleGroup tgLoadRadixSel; // Value injected by FXMLLoader

    @FXML // fx:id="rbLoadDecimal"
    private RadioButton rbLoadDecimal; // Value injected by FXMLLoader

    @FXML // fx:id="rbLoadBinary"
    private RadioButton rbLoadBinary; // Value injected by FXMLLoader

    @FXML // fx:id="txtLoadEnterData"
    private TextField txtLoadEnterData; // Value injected by FXMLLoader

    @FXML // fx:id="gpaneLoad"
    private GridPane gpaneLoad; // Value injected by FXMLLoader

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

    @FXML // fx:id="gPaneXsolve"
    private GridPane gPaneXsolve; // Value injected by FXMLLoader

    @FXML // fx:id="txtAreaParse"
    private JFXTextArea txtAreaParse; // Value injected by FXMLLoader

    @FXML // fx:id="bParseRegMap"
    private Button bParseRegMap; // Value injected by FXMLLoader

    @FXML // fx:id="bParsePDSpec"
    private Button bParsePDSpec; // Value injected by FXMLLoader

    @FXML
    private Button bParseUnion;
    
    @FXML
    private Button bParseEZSpro;  
    
    @FXML // fx:id="tabParse"
    private Tab tabParse; // Value injected by FXMLLoader

    @FXML // fx:id="lbLoadName"
    private Label lbLoadName; // Value injected by FXMLLoader

    @FXML // fx:id="aPaneMain"
    private AnchorPane aPaneMain; // Value injected by FXMLLoader

    @FXML // fx:id="txtLoadPrefix"
    private TextField txtLoadPrefix; // Value injected by FXMLLoader

    @FXML // fx:id="txtAreaUtilFSMStateName"
    private JFXTextArea txtAreaUtilFSMStateName; // Value injected by FXMLLoader

    @FXML // fx:id="txtAreaUtilFSMEventName"
    private JFXTextArea txtAreaUtilFSMEventName; // Value injected by FXMLLoader

    @FXML // fx:id="txtUtilFSMFnPrefix"
    private TextField txtUtilFSMFnPrefix; // Value injected by FXMLLoader

    @FXML // fx:id="txtUtilFSMFnPostFix"
    private TextField txtUtilFSMFnPostFix; // Value injected by FXMLLoader

    @FXML
    private RadioButton rbUtilsFSMIf;

    @FXML // fx:id="bUtilFSMGenFn"
    private Button bUtilFSMGenFn; // Value injected by FXMLLoader

    @FXML // fx:id="txtUtilFSMFnNamePrefix"
    private TextField txtUtilFSMFnNamePrefix; // Value injected by FXMLLoader

    @FXML // fx:id="tabUtils"
    private Tab tabUtils; // Value injected by FXMLLoader

    @FXML // fx:id="txtAreaUtilGen"
    private JFXTextArea txtAreaUtilGen; // Value injected by FXMLLoader

    @FXML // fx:id="bUtilsGenSwitch"
    private Button bUtilsGenSwitch; // Value injected by FXMLLoader

    @FXML // fx:id="bUtilsGenFns"
    private Button bUtilsGenFns; // Value injected by FXMLLoader

    @FXML // fx:id="bDProBrowse"
    private Button bDProBrowse; // Value injected by FXMLLoader

    @FXML // fx:id="txtDProName"
    private JFXTextField txtDProName; // Value injected by FXMLLoader

    @FXML // fx:id="txtDProBPath"
    private JFXTextField txtDProBPath; // Value injected by FXMLLoader

    @FXML // fx:id="txtDProTypeName"
    private TextField txtDProTypeName; // Value injected by FXMLLoader

    @FXML // fx:id="txtDProFieldName"
    private TextField txtDProFieldName; // Value injected by FXMLLoader

    @FXML // fx:id="txtDProFieldSize"
    private TextField txtDProFieldSize; // Value injected by FXMLLoader

    @FXML // fx:id="txtDProFieldRPath"
    private TextField txtDProFieldRPath; // Value injected by FXMLLoader

    @FXML // fx:id="txtDProFieldDesc"
    private TextField txtDProFieldDesc; // Value injected by FXMLLoader

    @FXML // fx:id="bDProAdd"
    private Button bDProAdd; // Value injected by FXMLLoader

    @FXML // fx:id="bDProDelete"
    private Button bDProDelete; // Value injected by FXMLLoader

    @FXML // fx:id="bDProModify"
    private Button bDProModify; // Value injected by FXMLLoader

    @FXML // fx:id="bDProUp"
    private Button bDProUp; // Value injected by FXMLLoader

    @FXML // fx:id="bDProDown"
    private Button bDProDown; // Value injected by FXMLLoader

    @FXML // fx:id="bDProGenCPro"
    private Button bDProGenCPro; // Value injected by FXMLLoader

    @FXML // fx:id="tViewDPro"
    private TableView<DProRow> tViewDPro; // Value injected by FXMLLoader

    @FXML // fx:id="lViewDPro"
    private JFXListView<String> lViewDPro; // Value injected by FXMLLoader`

    @FXML // fx:id="tColDProTypeName"
    private TableColumn<DProRow, String> tColDProTypeName; // Value injected by FXMLLoader

    @FXML // fx:id="tColDProFname"
    private TableColumn<DProRow, String> tColDProFname; // Value injected by FXMLLoader

    @FXML // fx:id="tColDProFcount"
    private TableColumn<DProRow, String> tColDProFsize; // Value injected by FXMLLoader

    @FXML // fx:id="tColDProFdesc"
    private TableColumn<DProRow, String> tColDProFdesc; // Value injected by FXMLLoader

    @FXML // fx:id="tColDProFrelPath"
    private TableColumn<DProRow, String> tColDProFrelPath; // Value injected by FXMLLoader

    @FXML // fx:id="ttViewCProLoad"
    private TreeTableView<CProRow> ttViewCProLoad; // Value injected by FXMLLoader

    @FXML // fx:id="ttColCProType"
    private TreeTableColumn<CProRow, String> ttColCProType; // Value injected by FXMLLoader

    @FXML // fx:id="ttColCProName"
    private TreeTableColumn<CProRow, String> ttColCProName; // Value injected by FXMLLoader

    @FXML // fx:id="ttColCProCount"
    private TreeTableColumn<CProRow, String> ttColCProCount; // Value injected by FXMLLoader

    @FXML // fx:id="ttColCProOffset"
    private TreeTableColumn<CProRow, String> ttColCProOffset; // Value injected by FXMLLoader

    @FXML // fx:id="ttColCProTotalBytes"
    private TreeTableColumn<CProRow, String> ttColCProTotalBytes; // Value injected by FXMLLoader

    @FXML // fx:id="ttColCProDesc"
    private TreeTableColumn<CProRow, String> ttColCProDesc; // Value injected by FXMLLoader

    @FXML // fx:id="ttColCProValue"
    private TreeTableColumn<CProRow, String> ttColCProValue; // Value injected by FXMLLoader

    @FXML // fx:id="lbLoadTotalBytes"
    private Label lbLoadTotalBytes; // Value injected by FXMLLoader

    @FXML // fx:id="lViewFX"
    private JFXListView<String> lViewFX; // Value injected by FXMLLoader

    @FXML // fx:id="txtFxName"
    private JFXTextField txtFxName; // Value injected by FXMLLoader

    @FXML // fx:id="gPaneFxLoad"
    private GridPane gPaneFxLoad; // Value injected by FXMLLoader

    @FXML // fx:id="tabFormula"
    private Tab tabFormula; // Value injected by FXMLLoader

    @FXML // fx:id="tabPaneFX"
    private TabPane tabPaneFX; // Value injected by FXMLLoader

    @FXML // fx:id="tabFXLoad"
    private Tab tabFXLoad; // Value injected by FXMLLoader

    @FXML // fx:id="txtFxResult"
    private JFXTextField txtFxResult; // Value injected by FXMLLoader
    
    @FXML // fx:id="txtAreaFxDesc"
    private JFXTextArea txtAreaFxDesc; // Value injected by FXMLLoader
    
    @FXML
    private JFXTextArea txtAreaFxFormula;

    @FXML
    private TextField txtFxCreateName;

    private Stage myStage;

    private BProLoad bProLoad;
    private DProCreate dproCreate;
    private DProOpen dproOpen;
    private FXLoad fxLoad;
    private FXCreate fxCreate;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	    bProLoad = new BProLoad(txtLoadEnterData, txtLoadPrefix,
	    		borderPaneMainWindow, statusBar, gpaneLoad, lbLoadName, ttViewCProLoad, lbLoadTotalBytes);
	    dproCreate = new DProCreate(myStage, lViewDPro, tViewDPro, txtDProName,
	    		txtDProTypeName, txtDProFieldName, txtDProFieldSize,
				txtDProFieldRPath, txtDProFieldDesc, statusBar, borderPaneMainWindow,
				txtDProBPath);
	    dproOpen = new DProOpen(myStage, tViewDPro, txtDProName, txtDProTypeName,
	    		txtDProFieldName, txtDProFieldSize, txtDProFieldRPath, txtDProFieldDesc,
	    		borderPaneMainWindow, statusBar, txtDProBPath);
	    fxLoad = new FXLoad(myStage, lViewFX, txtFxName, gPaneFxLoad, txtFxResult, txtAreaFxDesc, statusBar);
	    fxCreate = new FXCreate(txtFxCreateName, txtAreaFxFormula, borderPaneMainWindow, statusBar);

		Preferences.loadPreferences();
		txtDProBPath.setText(Preferences.getDproBasePath().getAbsolutePath());
		dproCreate.browseDir(Preferences.getDproLastBrowseDir());
		fxLoad.browseDir(Preferences.getFxLastDirectory());
		fxLoad.load(Preferences.getFxLastLoadedFile());
		txtLoadPrefix.setText(Preferences.getLoadViewPrefixValue());
		txtUtilFSMFnNamePrefix.setText(Preferences.getUtilsFnNamePrefixString());
		txtUtilFSMFnPrefix.setText(Preferences.getUtilsFnPrefixString());
		txtUtilFSMFnPostFix.setText(Preferences.getUtilsFnPostfixString());
		txtSolveEnter.setText(Preferences.getxSolveLastData());
		dproOpen.open(Preferences.getLastDesignFile());
		solveExpression();
		enDisTabButtons(Preferences.getLastOpenTabName());

		/* Link CProLoad tableview to modal class */
		ttColCProType.setCellValueFactory(new TreeItemPropertyValueFactory<CProRow, String>("type"));
		ttColCProName.setCellValueFactory(new TreeItemPropertyValueFactory<CProRow, String>("name"));
		ttColCProCount.setCellValueFactory(new TreeItemPropertyValueFactory<CProRow, String>("count"));
		ttColCProOffset.setCellValueFactory(new TreeItemPropertyValueFactory<CProRow, String>("offset"));
		ttColCProTotalBytes.setCellValueFactory(new TreeItemPropertyValueFactory<CProRow, String>("totalbytes"));
		ttColCProDesc.setCellValueFactory(new TreeItemPropertyValueFactory<CProRow, String>("desc"));
		ttColCProValue.setCellValueFactory(new TreeItemPropertyValueFactory<CProRow, String>("value"));
	    TreeItem<CProRow> rootItem = new TreeItem<CProRow> ();
	    ttViewCProLoad.setRoot(rootItem);


		bProLoad.loadFile(Preferences.getLastLoadedFile());
		switch(Preferences.getLastOpenTabName())
		{
		case "tabLoad":
			tabPaneMain.getSelectionModel().select(tabLoad);
			break;
		case "tabSPro":
			tabPaneMain.getSelectionModel().select(tabSPro);
			break;
		case "tabDPro":
			tabPaneMain.getSelectionModel().select(tabDPro);
			break;
		case "tabSolver":
			tabPaneMain.getSelectionModel().select(tabSolver);
			break;
		case "tabParse":
			tabPaneMain.getSelectionModel().select(tabParse);
			break;
		case "tabUtils":
			tabPaneMain.getSelectionModel().select(tabUtils);
			break;
		case "tabFormula":
			tabPaneMain.getSelectionModel().select(tabFormula);
			break;
		}
		tabPaneFX.getSelectionModel().select(tabFXLoad);

		/* Link tViewSPro to Modal class SProRow */
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

		/* Link tViewDPro to Modal class DProRow */
		tColDProTypeName.setCellValueFactory(new PropertyValueFactory<>("type"));
		tColDProFname.setCellValueFactory(new PropertyValueFactory<>("name"));
		tColDProFsize.setCellValueFactory(new PropertyValueFactory<>("size"));
		tColDProFdesc.setCellValueFactory(new PropertyValueFactory<>("desc"));
		tColDProFrelPath.setCellValueFactory(new PropertyValueFactory<>("rpath"));
		tViewDPro.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		tViewDPro.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
		    if (newSelection != null) {
		    	DProRow x = tViewDPro.getSelectionModel().getSelectedItem();
		    	txtDProTypeName.setText(x.getType());
		    	txtDProFieldName.setText(x.getName());
	    		txtDProFieldSize.setText(x.getSize());
	    		txtDProFieldDesc.setText(x.getDesc());
	    		txtDProFieldRPath.setText(x.getRpath());
		    }
		});

		bUtilsGenSwitch.setTooltip(new Tooltip("Generate Switch Case from enum entries"));
		bUtilsGenFns.setTooltip(new Tooltip("Generate Function prototype's comments and\n Function body"));
		bParsePDSpec.setTooltip(new Tooltip("Make sure \"Name\" is provided and clean up numbers/special char at start(in name of field ) in lines starting with B"));
		bParseEZSpro.setTooltip(new Tooltip("First line ->Name <name>. On each line <fieldname> <space> <size> <space> <Optional Desc>"));
		bParseUnion.setTooltip(new Tooltip("First line ->Name <name>. Copy only the field lines along with semicolon after that"));

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
		tabPaneFX.getSelectionModel().selectedItemProperty().addListener(
			    new ChangeListener<Tab>() {
			        @Override
			        public void changed(ObservableValue<? extends Tab> ov, Tab oldTab, Tab newTab) {
			    		if("tabFXLoad".equals(newTab.getId()))
			    		{
			    			bOpen.setDisable(true);
			    			bSave.setDisable(true);
			    			bLoad.setDisable(true);
			    			bGenMacros.setDisable(true);
			    			bGenBinary.setDisable(true);
			    		}
			    		else {
			    			bOpen.setDisable(false);
			    			bSave.setDisable(false);
						}
			        }
			    }
			);

	}

    @FXML
    void exitApplication(ActionEvent event) {
    	closeProgram();
    }

	@FXML
    void setPreferences(ActionEvent event) {
		/* TODO: */
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
    void openBitFile(ActionEvent event) {
    	String tabName = tabPaneMain.getSelectionModel().getSelectedItem().getId();
    	if(tabName.equals("tabSPro"))
    	{
    		SProOpen.run(borderPaneMainWindow, txtSProFieldName,
        		txtSProFieldSize, txtSProFieldDesc,
        		txtSProFieldEnum, txtSProName,
        		tgSProBitSel, tViewSPro, statusBar);
    	}
    	else if(tabName.equals("tabDPro")){
			dproOpen.run();
		}
    	else {
			fxCreate.open();
		}
    }

    @FXML
    void saveBitFile(ActionEvent event) {
    	String tabName = tabPaneMain.getSelectionModel().getSelectedItem().getId();
    	if(tabName.equals("tabSPro"))
    	{
    		SProCreate.run(txtSProName, rbSPro8bit,
        		rbSPro16bit,rbSPro64bit, borderPaneMainWindow,
        		tViewSPro, statusBar);
    	}
    	else if(tabName.equals("tabDPro")){
    		File file = dproCreate.save();
    		if(file != null){
    			/* Generate CPro on save */
    			DProGenCPro.run(file);
    		}
		}
    	else{
    		fxCreate.save();
    	}
    }

    @FXML
    void loadBitFile(ActionEvent event) {
    	bProLoad.load();
    }


    @FXML
    void generateBinary(ActionEvent event) {
    	bProLoad.generateBinary();
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
    	if(rbLoadBinary.isSelected() == true){
    		SProLoad.setRadix(Utils.Radix.RADIX_BINARY);
    		txtLoadEnterData.fireEvent(event);
    	}
    }

    @FXML
    void displayDecimalLoadView(ActionEvent event) {
    	if(rbLoadDecimal.isSelected() == true){
    		SProLoad.setRadix(Utils.Radix.RADIX_DECIMAL);
    		txtLoadEnterData.fireEvent(event);
    	}
    }

    @FXML
    void displayHexLoadView(ActionEvent event) {
    	if(rbLoadViewHex.isSelected() == true){
    		SProLoad.setRadix(Utils.Radix.RADIX_HEX);
    		txtLoadEnterData.fireEvent(event);
    	}
    }

    @FXML
    void parseSimpleRegmap(ActionEvent event) {
    	parseSimpleRegister(event);
    }

    @FXML
    void parsePDSpec(ActionEvent event) {
    	parseUSBPDSpec(event);
    }
    
    @FXML
    void parseEZSpro(ActionEvent event) {
    	parseEZSproCreate(event);
    }
    
	@FXML
    void parseUnion(ActionEvent event) {
    	parseUnionStruct(event);
    }    

	@FXML
    void dproAddField(ActionEvent event) {
    	dproCreate.addField();
    }

    @FXML
    void dproBrowse(ActionEvent event) {
    	dproCreate.browse();
    }

    @FXML
    void dproDeleteField(ActionEvent event) {
    	UtilsTableView.removeSelItem(tViewDPro);
    }

    @FXML
    void dproModifyField(ActionEvent event) {
    	dproCreate.modifyField();
    }

    @FXML
    void dproMoveFieldDown(ActionEvent event) {
    	UtilsTableView.moveDownSelItem(tViewDPro);
    }

    @FXML
    void dproMoveFieldUp(ActionEvent event) {
    	UtilsTableView.moveUpSelItem(tViewDPro);
    }

    @FXML
    void dproGenCPro(ActionEvent event) {
    	File dproFile = Preferences.getLastDesignFile();
    	if((dproFile == null) || (!dproFile.exists())){
    		statusBar.setText("Operation Failed: No design file(.dpro) opened");
    	}

    	if(DProGenCPro.run(dproFile) == true){
    		statusBar.setText("CPro file successfully created!!");
    	}
    	else{
    		statusBar.setText("CPro file generation failed: Please check all dependencies are present");
    	}

    }

    @FXML
    void generateMacros(ActionEvent event) {
    	bProLoad.genMacros();
    }

    @FXML
    void fxBrowse(ActionEvent event) {
    	fxLoad.browse();
    }

    @FXML
    void utilsGenFunction(ActionEvent event) {
    	utilsFSMGenerateFn(event);
    }

    @FXML
    void utilsGenSwitchCase(ActionEvent event) {
    	utilsGenerateSwitchCase(event);
    }

    @FXML
    void utilsGenFunctions(ActionEvent event) {
    	utilsGenerateFunctions(event);
    }

	public void setStage(Stage stage) {
        myStage = stage;
		myStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
		      public void handle(WindowEvent we) {
		    	  storeDataBeforeClose();
		      }
		  });
    }

	private void enDisTabButtons(String tabId){
		if(tabId.equals("tabLoad"))
		{
			bOpen.setDisable(true);
			bSave.setDisable(true);
			bLoad.setDisable(false);
			bGenMacros.setDisable(false);
			bGenBinary.setDisable(false);
		}
		else if( (tabId.equals("tabSolver") )|| (tabId.equals("tabUtils")) ){
			bOpen.setDisable(true);
			bSave.setDisable(true);
			bLoad.setDisable(true);
			bGenMacros.setDisable(true);
			bGenBinary.setDisable(true);
		}
		else if(tabId.equals("tabParse"))
		{
			bOpen.setDisable(true);
			bSave.setDisable(true);
			bLoad.setDisable(true);
			bGenMacros.setDisable(true);
			bGenBinary.setDisable(true);
		}
		else if(tabId.equals("tabFormula"))
		{
			bOpen.setDisable(true);
			bSave.setDisable(true);
			bLoad.setDisable(true);
			bGenMacros.setDisable(true);
			bGenBinary.setDisable(true);
		}
		else
		{
			bOpen.setDisable(false);
			bSave.setDisable(false);
			bLoad.setDisable(true);
			bGenMacros.setDisable(true);
			bGenBinary.setDisable(true);
		}
	}

	public void storeDataBeforeClose(){
		Preferences.setLoadViewPrefixValue(txtLoadPrefix.getText());
		Preferences.setUtilsFnNamePrefixString(txtUtilFSMFnNamePrefix.getText());
		Preferences.setUtilsFnPrefixString(txtUtilFSMFnPrefix.getText());
		Preferences.setUtilsFnPostfixString(txtUtilFSMFnPostFix.getText());
		Preferences.setDproBasePath(new File(txtDProBPath.getText()));
		fxLoad.saveOldData();
    	bProLoad.saveLoadedData();
    	Preferences.storePreferences();
	}

    private void closeProgram() {
    	storeDataBeforeClose();
    	Platform.exit();
	}

    private void solveExpression() {
		XpressionSolver x = new XpressionSolver();
		x.solve(txtSolveEnter, txtSolveShowHex, txtSolveShowBinary, txtSolveShowDecimal, statusBar, gPaneXsolve);
		Preferences.setxSolveLastData(txtSolveEnter.getText());
	}

    private void solveReverseXpression(ActionEvent event) {
		XpressionSolver x = new XpressionSolver();
		x.reverseSolve(txtSolveEnter, txtSolveShowHex, txtSolveShowBinary, txtSolveShowDecimal, statusBar, gPaneXsolve, event);
		Preferences.setxSolveLastData(txtSolveEnter.getText());

	}

	private void displayAboutMe() {
		Properties prop = new Properties();
		InputStream input;
		try {
			input = getClass().getResource("/version.properties").openStream();
			prop.load(input);
		} catch (IOException e) {
		}
		String ver = prop.getProperty("MAJOR_VERSION") + "."+ prop.getProperty("MINOR_VERSION") + "." + prop.getProperty("BUILD_NO");
		MsgBox.display("About Me", "BitPro\nVersion: "+ ver +"\nAuthor: Tejender Sheoran\nEmail: tejendersheoran@gmail.com\nCopyright(C) (2016-2017) Tejender Sheoran\nThis program is free software. You can redistribute it and/or modify it\nunder the terms of the GNU General Public License Ver 3.\n<http://www.gnu.org/licenses/>");
	}

	private void parseSimpleRegister(ActionEvent event) {
		if(ParseRegMap.parse(txtAreaParse, statusBar, tViewSPro, txtSProName, rbSPro32bit) == true)
		{
			tabPaneMain.getSelectionModel().select(tabSPro);
			bSave.fireEvent(event);
		}
	}

    private void parseUSBPDSpec(ActionEvent event) {
		if(ParsePDSpec.parse(txtAreaParse, statusBar, tViewSPro, txtSProName, rbSPro32bit) == true)
		{
			tabPaneMain.getSelectionModel().select(tabSPro);
			bSave.fireEvent(event);
		}
	}
    
	private void parseUnionStruct(ActionEvent event) {
		if(ParseUnion.parse(txtAreaParse, statusBar, tViewSPro, txtSProName, rbSPro32bit) == true)
		{
			tabPaneMain.getSelectionModel().select(tabSPro);
			bSave.fireEvent(event);
		}
	} 
	
    private void parseEZSproCreate(ActionEvent event) {
		if(ParseEZSPro.parse(txtAreaParse, statusBar, tViewSPro, txtSProName, rbSPro32bit) == true)
		{
			tabPaneMain.getSelectionModel().select(tabSPro);
			bSave.fireEvent(event);
		}
		
	}	

    private void utilsFSMGenerateFn(ActionEvent event) {
		TextViewer.display("Generated Code", UtilsFSMGen.run(txtUtilFSMFnNamePrefix.getText(),
				txtUtilFSMFnPrefix.getText(), txtUtilFSMFnPostFix.getText(),
				txtAreaUtilFSMStateName, txtAreaUtilFSMEventName, rbUtilsFSMIf));
	}

	private void utilsGenerateSwitchCase(ActionEvent event) {
		TextViewer.display("Generated Code", UtilsSwitchCaseGen.run(txtAreaUtilGen));
	}

	private void utilsGenerateFunctions(ActionEvent event) {
		TextViewer.display("Generated Code", UtilsFuncGen.run(txtAreaUtilGen));
	}
}

