package org.ykc.bitpro;

import java.net.URL;
import java.util.ResourceBundle;

import org.controlsfx.control.StatusBar;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

public class MainWindowController implements Initializable{

    @FXML // fx:id="borderPaneMainWindow"
    private BorderPane borderPaneMainWindow; // Value injected by FXMLLoader

    @FXML // fx:id="bCreate"
    private Button bCreate; // Value injected by FXMLLoader
    
    @FXML // fx:id="statusBar"
    private StatusBar statusBar; // Value injected by FXMLLoader    

    @FXML // fx:id="splitPaneFileExplorer"
    private SplitPane splitPaneFileExplorer; // Value injected by FXMLLoader

    @FXML // fx:id="toggleGroupBitSel"
    private ToggleGroup toggleGroupBitSel; // Value injected by FXMLLoader

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
    void createBitFile(ActionEvent event) {

    }

    @FXML
    void deleteBitField(ActionEvent event) {
    	removeBitField(tableViewCreate);
    }

    @FXML
    void moveDownBitField(ActionEvent event) {
    	moveDownBitField(tableViewCreate);
    }

    @FXML
    void moveUpBitField(ActionEvent event) {
    	moveUpBitField(tableViewCreate);
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		/* Link tableViewCreate to Modal class BitField */
		tCreateColFieldName.setCellValueFactory(new PropertyValueFactory<>("name"));
		tCreateColBitSize.setCellValueFactory(new PropertyValueFactory<>("size"));
		tCreateColDesc.setCellValueFactory(new PropertyValueFactory<>("desc"));
		tCreateColEnum.setCellValueFactory(new PropertyValueFactory<>("enums"));
		tableViewCreate.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
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

}

