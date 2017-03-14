package org.ykc.bitpro;

import org.controlsfx.control.StatusBar;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;

public class ParseRegMap {
	static boolean parse(JFXTextArea txtArea, StatusBar statusBar, TableView<SProRow> tableViewCreate, JFXTextField txtBitProSimpleName, RadioButton rbCreateView32bit)
	{
		ObservableList<SProRow> bList = FXCollections.observableArrayList();
		try {
			for (String line : txtArea.getText().split("\\n"))
			{
				line = line.trim();
				if(line.startsWith("Name")){
					txtBitProSimpleName.setText(line.substring(4).trim().toLowerCase() + "_t");
				}
				else if( Character.isDigit(line.charAt(0))){
					Integer fSize = 1;
					String fName = "Err";
					String[] x = line.trim().split("\\s+");
					if(x[0].contains(":")){
						String[]  a = x[0].split(":");
						fSize = Integer.parseInt(a[0]) - Integer.parseInt(a[1]) ;
						fSize++;
					}
					fName = x[1].toLowerCase();
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
