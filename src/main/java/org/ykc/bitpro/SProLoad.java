package org.ykc.bitpro;

import org.controlsfx.control.StatusBar;
import org.w3c.dom.Element;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class SProLoad {
	private static Utils.Radix radix = Utils.Radix.RADIX_HEX;
	private static Element curElement;
	private static TextField txtMain;
	private static StatusBar status;
	private static boolean isLoaded = false;

	public static void setRadix(Utils.Radix value) {
		radix = value;
	}
	
	public static boolean isLoaded(){
		return isLoaded;
	}
	public static boolean run(Element simpleElement, TextField txtData, GridPane gPane,
			StatusBar statusBar) {
		isLoaded = true;
		gPane.getChildren().clear();
		
		curElement = simpleElement;
		txtMain = txtData;
		status = statusBar;
		
		Integer maxFields = UtilsBPro.getSProFieldsCount(curElement);
		ObservableList<JFXComboBox> comboList = FXCollections.observableArrayList();
		ObservableList<Label> labelList = FXCollections.observableArrayList();		
		
		for (Integer i = 0; i < maxFields; i++) {
			Integer offset = UtilsBPro.getSProFieldOffset(curElement, i);
			String name = UtilsBPro.getSProFieldName(curElement, i) + "( ";
			Integer fsize = UtilsBPro.getSProFieldSize(curElement, i);
			if (fsize != 1) {
				name += ((Integer) (fsize + offset - 1)).toString() + ".";
			}

			name += offset.toString();

			name += " )";

			Label label = new Label(name);
			JFXComboBox cbox = new JFXComboBox();
			Integer ecount = UtilsBPro.getSProFieldEnumCount(curElement, i);
			if(ecount != 0)
			{
				Element enumElement = UtilsBPro.getSProFieldEnumElement(curElement, i);
				for(int j = 0; j < ecount; j++)
				{
					cbox.getItems().add(UtilsBPro.getSProEnumName(enumElement, j));
				}
			}
			cbox.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {			
					Platform.runLater(() -> updateMainText(comboList, event));
				}
			});

			label.setOnMouseEntered(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent e) {
					updateDescStatus(e, labelList);
				}
			});
			cbox.setEditable(true);
			comboList.add(cbox);
			labelList.add(label);
			gPane.add(label, 0, i);
			gPane.add(cbox, 1, i);
		}

		updateFieldText(comboList);
		txtData.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				updateFieldText(comboList);
			}
		});
		return true;
	}

	public static void updateDescStatus(MouseEvent e, ObservableList<Label> labelList) {
		for (int i = 0; i < labelList.size(); i++) {
			if (e.getSource().equals(labelList.get(i))) {
				Integer offset = UtilsBPro.getSProFieldOffset(curElement, i);
				Integer fsize = UtilsBPro.getSProFieldSize(curElement, i);
				Long mask = Utils.get32bitMask(fsize);
				String x = "Mask: 0x" + Long.toHexString(mask << offset);
				x += ", Max: (0x" + Long.toHexString(mask) + ", " + mask.toString() + ", 0b"
						+ Long.toBinaryString(mask) + ")";
				x += ", Description: " + UtilsBPro.getSProFieldDesc(curElement, i);
				status.setText(x);
			}
		}
	}

	public static void updateMainText(ObservableList<JFXComboBox> comboList, ActionEvent event) {
		Long value = 0L;
		for (Integer i = 0; i < comboList.size(); i++) {
			Integer offset = UtilsBPro.getSProFieldOffset(curElement, i);
			Integer fsize = UtilsBPro.getSProFieldSize(curElement, i);
			Long mask = Utils.get32bitMask(fsize);
			Long result = (Utils.parseStringtoNumber(getComboInput(comboList, i)) & mask);
			value |= result << offset;
			if(event.getSource().equals(comboList.get(i)))
			{
				comboList.get(i).getSelectionModel().clearSelection();
				//continue;
			}
			comboList.get(i).setValue(Utils.longToString(result, radix));
		}
		txtMain.setText(Utils.longToString(value, radix));
	}

	private static void updateFieldText(ObservableList<JFXComboBox> comboList) {
		Long value = Utils.parseStringtoNumber(txtMain.getText());
		for (Integer i = 0; i < comboList.size(); i++) {
			Integer offset = UtilsBPro.getSProFieldOffset(curElement, i);
			Integer size = UtilsBPro.getSProFieldSize(curElement, i);
			comboList.get(i).setValue(Utils.longToString(Utils.getMaskValue(value, offset, size), radix));
		}
		txtMain.setText(Utils.longToString(value, radix));
	}
	
	private static String getComboInput(ObservableList<JFXComboBox> comboList, Integer index){
		int selectedIdx = comboList.get(index).getSelectionModel().getSelectedIndex();
		if(selectedIdx < 0)
		{
			return comboList.get(index).getValue().toString();
		}
		else {
			Integer ecount = UtilsBPro.getSProFieldEnumCount(curElement, index);
			if(ecount != 0)
			{
				Element enumElement = UtilsBPro.getSProFieldEnumElement(curElement, index);
				return Long.toString(UtilsBPro.getSProEnumValue(enumElement, selectedIdx));
			}			
		}
		return "0";
	}
}
