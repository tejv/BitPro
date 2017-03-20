package org.ykc.bitpro;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.controlsfx.control.StatusBar;
import org.w3c.dom.Document;
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
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
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
		ObservableList<JFXTextField> txtList = FXCollections.observableArrayList();
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
			JFXTextField tbox = new JFXTextField();
			Integer ecount = UtilsBPro.getSProFieldEnumCount(curElement, i);
			if(ecount != 0)
			{
				String help = "";
				Element enumElement = UtilsBPro.getSProFieldEnumElement(curElement, i);
				for(int j = 0; j < ecount; j++)
				{
					help += UtilsBPro.getSProEnumName(enumElement, j) + " : ";
					help += UtilsBPro.getSProEnumValueString(enumElement, j) + "\n";
				}
				label.setTooltip(new Tooltip(help));
			}
			tbox.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {			
					Platform.runLater(() -> updateMainText(txtList, event));
				}
			});

			label.setOnMouseEntered(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent e) {
					updateDescStatus(e, labelList);
				}
			});
			tbox.setEditable(true);
			txtList.add(tbox);
			labelList.add(label);
			gPane.add(label, 0, i);
			gPane.add(tbox, 1, i);
		}

		updateFieldText(txtList);
		txtData.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				updateFieldText(txtList);
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

	public static void updateMainText(ObservableList<JFXTextField> txtList, ActionEvent event) {
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
		}
		txtMain.setText(Utils.longToString(value, radix));
	}

	private static void updateFieldText(ObservableList<JFXTextField> txtList) {
		Long value = Utils.parseStringtoNumber(txtMain.getText());
		for (Integer i = 0; i < txtList.size(); i++) {
			Integer offset = UtilsBPro.getSProFieldOffset(curElement, i);
			Integer size = UtilsBPro.getSProFieldSize(curElement, i);
			TextField x = txtList.get(i);
			String xString = Utils.longToString(Utils.getMaskValue(value, offset, size), radix);
			x.setText(xString);
			x.positionCaret(xString.length());
		}
		txtMain.setText(Utils.longToString(value, radix));
	}	
}
