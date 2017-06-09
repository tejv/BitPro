package org.ykc.bitpro;

import org.controlsfx.control.StatusBar;

import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;

public class ParseUnion {
	static boolean parse(JFXTextArea txtArea, StatusBar statusBar, TableView<SProRow> tableViewCreate, JFXTextField txtBitProSimpleName, RadioButton rbCreateView32bit)
	{
		ObservableList<SProRow> bList = FXCollections.observableArrayList();
		try {
			for (String line : txtArea.getText().split("\\n"))
			{
				line = line.trim();
				if(!line.isEmpty()){
					if(line.startsWith("Name") || line.startsWith("name")){
						txtBitProSimpleName.setText(line.substring(4).trim().toLowerCase());
					}
					else{
						Integer fSize = 1;
						String fName = "Err";
						String[] beforeStrings = line.split(":");
						String[] nameStrings = beforeStrings[0].trim().split("\\s+");
						fName = nameStrings[1].toLowerCase();
						String[] x = beforeStrings[1].trim().split(";");					
						fSize = Integer.parseInt(x[0]);
						
						SProRow bField;
						try {
							bField = new SProRow(fName, fSize.toString(), "", "");
						} catch (Exception e) {
							statusBar.setText("Parsing Failed");
							return false;
						}
						bList.add(bField);
					}
				}
			}
		} catch (Exception e) {
			statusBar.setText("Parsing Failed");
			return false;
		}
		tableViewCreate.getItems().clear();
		tableViewCreate.getItems().addAll(bList);
		rbCreateView32bit.setSelected(true);
		return true;
	}
}
