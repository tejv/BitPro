package org.ykc.bitpro;

import java.awt.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream.GetField;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.controlsfx.control.StatusBar;
import org.jdom2.Document;
import org.jdom2.Element;
import org.ykc.bitpro.Utils.Radix;

import com.jfoenix.controls.JFXTextField;

import impl.org.controlsfx.tools.rectangle.change.NewChangeStrategy;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.GridPane;

public class CProLoad {
	private File cproFile;
	ObservableList<Long> loadedList = FXCollections.observableArrayList();
	private TextField txtData;
	private GridPane gPane;
	private StatusBar statusBar;
	private TreeTableView<CProRow> ttViewCProLoad;
	int selectedIdx = -1;
	int loadIdx = 0;

	public CProLoad(TextField txtData, GridPane gPane, StatusBar statusBar,
			TreeTableView<CProRow> ttViewCProLoad) {
		this.txtData = txtData;
		this.gPane = gPane;
		this.statusBar = statusBar;
		this.ttViewCProLoad = ttViewCProLoad;

		ttViewCProLoad.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if(ttViewCProLoad.getSelectionModel().getSelectedItem() != null){
			    if(ttViewCProLoad.getSelectionModel().getSelectedItem().isLeaf() == true){
			    	if(selectedIdx != -1){
			    		ttViewCProLoad.getTreeItem(selectedIdx).getValue().setValue(Utils.longToString(Utils.parseStringtoNumber(txtData.getText()),Radix.RADIX_HEX));
			    	}
			    	selectedIdx = ttViewCProLoad.getSelectionModel().getSelectedIndex();
			    	txtData.setText(ttViewCProLoad.getTreeItem(selectedIdx).getValue().getValue());
			    	SProLoad.run(ttViewCProLoad.getSelectionModel().getSelectedItem().getValue().getSimpleElement(), txtData, gPane, statusBar);
					/* Workaround for tableview update issue */
					ttViewCProLoad.getColumns().get(0).setVisible(false);
					ttViewCProLoad.getColumns().get(0).setVisible(true);
			    }
			}
		});

		txtData.addEventHandler(ActionEvent.ACTION, event -> {
			ttViewCProLoad.getTreeItem(selectedIdx).getValue().setValue(Utils.longToString(Utils.parseStringtoNumber(txtData.getText()),Radix.RADIX_HEX));
			/* Workaround for tableview update issue */
			ttViewCProLoad.getColumns().get(0).setVisible(false);
			ttViewCProLoad.getColumns().get(0).setVisible(true);
	    });

	}

	public void updateValueList(File tmpFile){
		loadedList.clear();
		try {
			// Open the file
			FileInputStream fstream = new FileInputStream(tmpFile);
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

			String strLine;

			//Read File Line By Line
			while ((strLine = br.readLine()) != null)   {

			  String[] portion = strLine.split(":");
			  loadedList.add(Utils.parseStringtoNumber(portion[0]));
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


    private void storeChildren(TreeItem<CProRow> root, FileWriter fileWriter){
        for(TreeItem<CProRow> child: root.getChildren()){
            if(child.getChildren().isEmpty()){
            	CProRow row =  child.getValue();
            	try {
					fileWriter.write(row.getValue() + ":" + row.getTotalbytes().toString() + " ;"+ row.getName() + "; Off-> " + row.getOffset() +"\n");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            } else {
            	storeChildren(child, fileWriter);
            }
        }
    }

	public void storeData(FileWriter fileWriter) {
		storeChildren(ttViewCProLoad.getRoot(), fileWriter);
	}

	public boolean run(File cpro){
		ttViewCProLoad.getRoot().getChildren().clear();
		loadIdx = 0;
		selectedIdx = -1;
		Document xmlDoc = UtilsBPro.getJDOM2Doc(cpro);
		if(xmlDoc == null)
		{
			return false;
		}
		Element cElement = xmlDoc.getRootElement();
		loadCProElement(cElement, ttViewCProLoad.getRoot(), 0);
		return true;
	}

	private void loadCProElement(Element cElement, TreeItem<CProRow> root, int gOffset){
		java.util.List<Element> listOfFields = cElement.getChild("body").getChildren("cfield");

		for(int i = 0; i < listOfFields.size(); i++){
			Element e = listOfFields.get(i);
			String cType = e.getChildText("ctype");
			String cName = e.getChildText("cname");
			String cCount = e.getChildText("cCount");
			String cDesc = e.getChildText("cdesc");
			String cOffset = e.getChildText("cOffset");
			String cTotalBytes = e.getChildText("cTotalBytes");
			Integer offset = Integer.parseInt(cOffset);
			Integer count = Integer.parseInt(cCount);
			Integer totalBytes = Integer.parseInt(cTotalBytes);
			String nameString = cName;
			Integer lastOffset = gOffset;

			for(Integer j = 0 ; j < count; j++){
				if(count > 1){
					nameString = cName + "[" + j.toString() +"]";
				}
				if(j > 0)
				{
					lastOffset += (totalBytes);
					lastOffset -= offset;

				}

				lastOffset += offset;
				cOffset = Integer.toString(lastOffset);

				if(e.getChild("complex") != null){
					TreeItem<CProRow> item = new TreeItem<CProRow>(new CProRow(cType, nameString, cCount, cOffset, cTotalBytes, cDesc, "", null));
					root.getChildren().add(item);
					Element x = e.getChild("complex");
					loadCProElement(x, item, lastOffset);
				}
				else{
					Element simpleElement = e.getChild("simple").clone();
					simpleElement.detach();
					String valueString = "0x0";
					if(loadedList.size() > loadIdx){
						valueString = Utils.longToString(loadedList.get(loadIdx), Radix.RADIX_HEX);
					}
					loadIdx++;
					TreeItem<CProRow> item = new TreeItem<CProRow>(new CProRow(cType, nameString, cCount, cOffset, cTotalBytes, cDesc, valueString, simpleElement));
					root.getChildren().add(item);
				}
			}
		}
	}


}
