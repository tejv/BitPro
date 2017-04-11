package org.ykc.bitpro;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.controlsfx.control.StatusBar;
import org.jdom2.Element;
import org.jdom2.output.JDOMLocator;
import org.ykc.bitpro.Utils.Radix;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;

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
	public static boolean run(org.jdom2.Element element, TextField txtData, GridPane gPane,
			StatusBar statusBar) {
		isLoaded = true;
		gPane.getChildren().clear();

		curElement = element;
		txtMain = txtData;
		status = statusBar;

		Integer maxFields = UtilsBPro.getSProFieldsCount(curElement);
		ObservableList<JFXTextField> txtList = FXCollections.observableArrayList();
		ObservableList<Label> labelList = FXCollections.observableArrayList();
		ObservableList<JFXComboBox> comboList = FXCollections.observableArrayList();

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
			JFXTextField tbox = new JFXTextField();
			JFXComboBox cBox = new JFXComboBox();
			Integer ecount = UtilsBPro.getSProFieldEnumCount(curElement, i);

			tbox.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					Platform.runLater(() -> updateMainText(txtList, comboList, event));
				}
			});

			label.setOnMouseEntered(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent e) {
					updateDescStatus(e, labelList, txtList, comboList);
				}
			});
			tbox.setEditable(true);
			txtList.add(tbox);
			comboList.add(cBox);
			labelList.add(label);
			gPane.add(label, 0, i);
			gPane.add(tbox, 2, i);
			if(ecount != 0)
			{
				String help = "";
				Element enumElement = UtilsBPro.getSProFieldEnumElement(curElement, i);
				for(int j = 0; j < ecount; j++)
				{
					String nameString = UtilsBPro.getSProEnumName(enumElement, j);
					String valString = UtilsBPro.getSProEnumValueString(enumElement, j);
					cBox.getItems().add(nameString);
					help += nameString + " : ";
					help += valString + "\n";
				}

				cBox.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					for(int i = 0; i < txtList.size(); i++)
					{
						if(event.getSource().equals(comboList.get(i)))
						{
							int idx = comboList.get(i).getSelectionModel().getSelectedIndex();
							Element enumElement = UtilsBPro.getSProFieldEnumElement(curElement, i);
							txtList.get(i).setText(UtilsBPro.getSProEnumValueString(enumElement, idx));
						}
					}
					Platform.runLater(() -> updateMainText(txtList, comboList, event));
				}
			});
				label.setTooltip(new Tooltip(help));
				gPane.add(cBox, 1, i);
			}
		}

		updateFieldText(txtList, comboList);
		txtData.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				updateFieldText(txtList, comboList);
			}
		});
		return true;
	}

	public static void updateDescStatus(MouseEvent e, ObservableList<Label> labelList,
			ObservableList<JFXTextField> txtList, ObservableList<JFXComboBox> comboList) {
		for (int i = 0; i < labelList.size(); i++) {
			if (e.getSource().equals(labelList.get(i))) {
				Integer offset = UtilsBPro.getSProFieldOffset(curElement, i);
				String x = UtilsBPro.getSProFieldName(curElement, i) + "( ";
				Integer fsize = UtilsBPro.getSProFieldSize(curElement, i);
				Long mask = Utils.get32bitMask(fsize);
				if (fsize != 1) {
					x += ((Integer) (fsize + offset - 1)).toString() + ".";
				}

				x += offset.toString();
				x += " )";
				x += ", Value: ";
				Long value = Utils.parseStringtoNumber(txtList.get(i).getText());
				x += Utils.longToString(value, Radix.RADIX_DECIMAL);
				x += ", " + Utils.longToString(value, Radix.RADIX_HEX);
				x += ", " + Utils.longToString(value, Radix.RADIX_BINARY);

				Integer ecount = UtilsBPro.getSProFieldEnumCount(curElement, i);
				if(ecount != 0)
				{
					x += ", " +comboList.get(i).getSelectionModel().getSelectedItem().toString();
				}

				x += ", Mask: 0x" + Long.toHexString(mask << offset);
				x += ", Max: ( " + mask.toString() + ", 0x" + Long.toHexString(mask) + ", 0b"
						+ Long.toBinaryString(mask) + " )";

				x += ", Description: " + UtilsBPro.getSProFieldDesc(curElement, i);
				status.setText(x);
			}
		}
	}

	public static void updateMainText(ObservableList<JFXTextField> txtList, ObservableList<JFXComboBox> comboList, ActionEvent event) {
		Long value = 0L;
		for (Integer i = 0; i < txtList.size(); i++) {
			Integer offset = UtilsBPro.getSProFieldOffset(curElement, i);
			Integer fsize = UtilsBPro.getSProFieldSize(curElement, i);
			Long mask = Utils.get32bitMask(fsize);
			TextField x = txtList.get(i);
			Long result = (Utils.parseStringtoNumber(x.getText()) & mask);
			value |= result << offset;
			String xString = Utils.longToString(result, radix);
			x.setText(xString);
			x.positionCaret(xString.length());
			Integer ecount = UtilsBPro.getSProFieldEnumCount(curElement, i);
			if(ecount != 0)
			{
				Element enumElement = UtilsBPro.getSProFieldEnumElement(curElement, i);
				int j;
				for(j = 0; j < ecount; j++)
				{
					String nameString = UtilsBPro.getSProEnumName(enumElement, j);
					Long val = Utils.parseStringtoNumber(UtilsBPro.getSProEnumValueString(enumElement, j));
					if(val == result)
					{
						comboList.get(i).getSelectionModel().select(j);
						break;
					}
				}
				if(j == ecount)
				{
					comboList.get(i).getSelectionModel().clearSelection();;
				}
			}
		}
		txtMain.setText(Utils.longToString(value, radix));
	}

	private static void updateFieldText(ObservableList<JFXTextField> txtList, ObservableList<JFXComboBox> comboList) {
		Long value = Utils.parseStringtoNumber(txtMain.getText());
		for (Integer i = 0; i < txtList.size(); i++) {
			Integer offset = UtilsBPro.getSProFieldOffset(curElement, i);
			Integer size = UtilsBPro.getSProFieldSize(curElement, i);
			TextField x = txtList.get(i);
			Long fvalue = Utils.getMaskValue(value, offset, size);
			String xString = Utils.longToString(fvalue, radix);
			x.setText(xString);
			x.positionCaret(xString.length());
			Integer ecount = UtilsBPro.getSProFieldEnumCount(curElement, i);
			if(ecount != 0)
			{
				Element enumElement = UtilsBPro.getSProFieldEnumElement(curElement, i);
				int j;
				for(j = 0; j < ecount; j++)
				{
					String nameString = UtilsBPro.getSProEnumName(enumElement, j);
					Long val = Utils.parseStringtoNumber(UtilsBPro.getSProEnumValueString(enumElement, j));
					if(val == fvalue)
					{
						comboList.get(i).getSelectionModel().select(j);
						break;
					}
				}
				if(j == ecount)
				{
					comboList.get(i).getSelectionModel().clearSelection();;
				}
			}
		}
		txtMain.setText(Utils.longToString(value, radix));
	}
}
