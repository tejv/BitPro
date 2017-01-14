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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class LoadSimpleBPro {
	private static GUtils.Radix radix = GUtils.Radix.RADIX_HEX;
	private static Element curElement;
	private static JFXTextField txtMain;
	private static StatusBar status;
	private static boolean isLoaded = false;

	public static void setRadix(GUtils.Radix value) {
		radix = value;
	}
	
	public static boolean isLoaded(){
		return isLoaded;
	}
	public static boolean loadSimpleXML(Element simpleElement, JFXTextField txtData, GridPane gPane,
			StatusBar statusBar) {
		isLoaded = true;
		gPane.getChildren().clear();
		
		curElement = simpleElement;
		txtMain = txtData;
		status = statusBar;
		
		Integer maxFields = BProUtils.getMaxFieldsSimpleXML(curElement);
		ObservableList<JFXComboBox> comboList = FXCollections.observableArrayList();
		ObservableList<Label> labelList = FXCollections.observableArrayList();		
		
		for (Integer i = 0; i < maxFields; i++) {
			Integer offset = BProUtils.getFieldOffsetSimpleXML(curElement, i);
			String name = BProUtils.getFieldNameSimpleXML(curElement, i) + "( ";
			Integer fsize = BProUtils.getFieldSizeSimpleXML(curElement, i);
			if (fsize != 1) {
				name += ((Integer) (fsize + offset - 1)).toString() + ".";
			}

			name += offset.toString();

			name += " )";

			Label label = new Label(name);
			JFXComboBox cbox = new JFXComboBox();
			Integer ecount = BProUtils.getFieldEnumCountSimpleXML(curElement, i);
			if(ecount != 0)
			{
				Element enumElement = BProUtils.getFieldEnumSimpleXML(curElement, i);
				for(int j = 0; j < ecount; j++)
				{
					cbox.getItems().add(BProUtils.getFieldEnumNameSimpleXML(enumElement, j));
				}
			}
			cbox.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {			
					Platform.runLater(() -> updateMainTextLoadTab(comboList, event));
				}
			});

			label.setOnMouseEntered(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent e) {
					updateDescLoadTab(e, labelList);
				}
			});
			cbox.setEditable(true);
			comboList.add(cbox);
			labelList.add(label);
			gPane.add(label, 0, i);
			gPane.add(cbox, 1, i);
		}

		updateFieldTextLoadTab(comboList);
		txtData.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				updateFieldTextLoadTab(comboList);
			}
		});
		return true;
	}

	public static void updateDescLoadTab(MouseEvent e, ObservableList<Label> labelList) {
		for (int i = 0; i < labelList.size(); i++) {
			if (e.getSource().equals(labelList.get(i))) {
				Integer offset = BProUtils.getFieldOffsetSimpleXML(curElement, i);
				Integer fsize = BProUtils.getFieldSizeSimpleXML(curElement, i);
				Long mask = GUtils.get32bitMask(fsize);
				String x = "Mask: 0x" + Long.toHexString(mask << offset);
				x += ", Max: (0x" + Long.toHexString(mask) + ", " + mask.toString() + ", 0b"
						+ Long.toBinaryString(mask) + ")";
				x += ", Description: " + BProUtils.getFieldDescSimpleXML(curElement, i);
				status.setText(x);
			}
		}
	}

	public static void updateMainTextLoadTab(ObservableList<JFXComboBox> comboList, ActionEvent event) {
		Long value = 0L;
		for (Integer i = 0; i < comboList.size(); i++) {
			Integer offset = BProUtils.getFieldOffsetSimpleXML(curElement, i);
			Integer fsize = BProUtils.getFieldSizeSimpleXML(curElement, i);
			Long mask = GUtils.get32bitMask(fsize);
			Long result = (GUtils.parseStringtoNumber(getComboInput(comboList, i)) & mask);
			value |= result << offset;
			if(event.getSource().equals(comboList.get(i)))
			{
				comboList.get(i).getSelectionModel().clearSelection();
				//continue;
			}
			comboList.get(i).setValue(GUtils.longToString(result, radix));
		}
		txtMain.setText(GUtils.longToString(value, radix));
	}

	private static void updateFieldTextLoadTab(ObservableList<JFXComboBox> comboList) {
		Long value = GUtils.parseStringtoNumber(txtMain.getText());
		for (Integer i = 0; i < comboList.size(); i++) {
			Integer offset = BProUtils.getFieldOffsetSimpleXML(curElement, i);
			Integer size = BProUtils.getFieldSizeSimpleXML(curElement, i);
			comboList.get(i).setValue(GUtils.longToString(GUtils.getMaskValue(value, offset, size), radix));
		}
		txtMain.setText(GUtils.longToString(value, radix));
	}
	
	private static String getComboInput(ObservableList<JFXComboBox> comboList, Integer index){
		int selectedIdx = comboList.get(index).getSelectionModel().getSelectedIndex();
		if(selectedIdx < 0)
		{
			return comboList.get(index).getValue().toString();
		}
		else {
			Integer ecount = BProUtils.getFieldEnumCountSimpleXML(curElement, index);
			if(ecount != 0)
			{
				Element enumElement = BProUtils.getFieldEnumSimpleXML(curElement, index);
				return Long.toString(BProUtils.getFieldEnumValueSimpleXML(enumElement, selectedIdx));
			}			
		}
		return "0";
	}
}
