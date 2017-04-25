package org.ykc.bitpro;

import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.StatusBar;

import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;

public class ParsePDSpec {

	static boolean parse(JFXTextArea txtArea, StatusBar statusBar, TableView<SProRow> tableViewCreate, JFXTextField txtBitProSimpleName, RadioButton rbCreateView32bit)
	{
		Integer reservedCount = 0;
		ObservableList<SProRow> bList = FXCollections.observableArrayList();
		try {
			for (String line : txtArea.getText().split("\\n"))
			{
				line = line.trim();
				if(line.startsWith("Name")){
					txtBitProSimpleName.setText(line.substring(4).trim().toLowerCase() + "_t");
				}
				else if( Character.isDigit(line.charAt(1)) && (line.charAt(0) == 'B') ){
					/* Replace ellipsys with :*/
					line = line.replace("\u2026",":");
					line = line.substring(1);
					Integer fSize = 1;
					String fName = "Err";
					String[] x = line.trim().split("\\s+");
					if(x[0].contains(":")){
						String[]  a = x[0].split(":");
						fSize = Integer.parseInt(a[0]) - Integer.parseInt(a[1]) ;
						fSize++;
					}
					fName = x[1].toLowerCase();
					if(x.length > 2){
						if("reserved".equals(fName))
						{
							fName = "rsvd"+ reservedCount.toString();
							reservedCount++;
						}
						else{
						fName += "_" + x[2].toLowerCase();
						}
					}
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
		/* Adding Items in reverse order as data is in reverse order */
		int list_count = bList.size();
		for(int i = list_count - 1; i >= 0; i--)
		{
			tableViewCreate.getItems().add(bList.get(i));
		}
		rbCreateView32bit.setSelected(true);
		return true;
	}

}
