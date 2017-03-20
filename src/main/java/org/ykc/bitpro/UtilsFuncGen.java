package org.ykc.bitpro;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class UtilsFuncGen {

	public static StringBuilder run(JFXTextArea txtAreaUtilGen) {
		StringBuilder xBuilder = new StringBuilder();
		addPreface(xBuilder);
		xBuilder.append("/* Function Prototypes */\n\n");
		addFnProtoComment(xBuilder, txtAreaUtilGen);
		xBuilder.append("/* Function Definitions */\n\n");
		addFnBody(xBuilder, txtAreaUtilGen);
		return xBuilder;
	}

	private static void addPreface(StringBuilder xBuilder){
		String prefaceString = "BitPro Auto Generated File ";
		DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
		Date dateobj = new Date();
		prefaceString += df.format(dateobj) + "\n\n";
		xBuilder.append(prefaceString);
	}
	
	private static void addFnProtoComment(StringBuilder xBuilder, JFXTextArea txtAreaUtilGen) {

		try {
			for (String line : txtAreaUtilGen.getText().split("\\n"))
			{
				ObservableList<String> paramList = FXCollections.observableArrayList();
				line = line.trim();
				int start = line.indexOf('(');
				int end = line.indexOf(')');
				String paramString = line.substring(start+1, end);
				if(paramString.contains(",")){
					String[] names = paramString.split(",");
					for(String name : names){
						String[] x = name.split(" ");
						paramList.add(x[x.length -1]);
					}
				}
				else{
					if(paramString.contains("void")){
						
					}
					else {
						String[] names = paramString.split(" ");
						paramList.add(names[names.length -1]);
					}
				}
				xBuilder.append("/**\n");
				xBuilder.append(" * @brief \n");
				for(int i = 0; i < paramList.size(); i++)
				{
					xBuilder.append(" * @param " + paramList.get(i) +" \n");
				}
				xBuilder.append(" * @return \n");
				xBuilder.append(" */\n");
				xBuilder.append(line);
				xBuilder.append("\n\n");

			}

		} catch (Exception e) {
			xBuilder.append("\n\nParsing Failed.");
		}		
	}
	
	private static void addFnBody(StringBuilder xBuilder, JFXTextArea txtAreaUtilGen) {
		try {
			for (String line : txtAreaUtilGen.getText().split("\\n"))
			{
				ObservableList<String> paramList = FXCollections.observableArrayList();
				line = line.trim();
				int semiColonidx = line.indexOf(';');
				line = line.substring(0, semiColonidx);
				line = line.trim();
				xBuilder.append(line + "\n");
				xBuilder.append("{\n");
				xBuilder.append("    /* TODO */\n");
				xBuilder.append("    \n");
				xBuilder.append("}");
				xBuilder.append("\n\n");
			}

		} catch (Exception e) {
			xBuilder.append("\n\nParsing Failed.");
		}	
	}	
}
