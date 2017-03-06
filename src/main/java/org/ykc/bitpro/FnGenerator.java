package org.ykc.bitpro;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.jfoenix.controls.JFXTextArea;

import javafx.scene.control.TextArea;

public class FnGenerator {
	public static StringBuilder run(String namePrefix, String prefix, String postfix,
			JFXTextArea txtAreaStateNames, JFXTextArea txtAreaEventNames){
		StringBuilder xBuilder = new StringBuilder();
		addPreface(xBuilder);
		addEvtMacros(namePrefix, txtAreaEventNames, xBuilder);
		addEvtEnum(namePrefix, txtAreaEventNames, xBuilder);
		addStateEnum(namePrefix, txtAreaStateNames, xBuilder);
		addFnPrototypes(namePrefix, prefix, postfix, txtAreaStateNames, xBuilder);
		addStateTable(namePrefix, prefix, postfix, txtAreaStateNames, xBuilder);
		addFnDefinitions(namePrefix, prefix, postfix, txtAreaStateNames, txtAreaEventNames, xBuilder);
		return xBuilder;
	}
	
	private static void addPreface(StringBuilder xBuilder){
		String prefaceString = "BitPro Auto Generated File ";
		DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
		Date dateobj = new Date();
		prefaceString += df.format(dateobj) + "\n\n";
		xBuilder.append(prefaceString);
	}	
	
	private static void addEvtMacros(String namePrefix, JFXTextArea txtAreaEventNames, StringBuilder xBuilder) {
		String[] x = txtAreaEventNames.getText().split("\\n");
		int maxStringSize = 0;
		for(int i = 0; i < x.length; i++){
			int size = x[i].length();
			if(size > maxStringSize){
				maxStringSize = size;
			}
		}
		
		if("ALL_MASK".length() > maxStringSize){
			maxStringSize = "ALL_MASK".length();
		}
		
		String fnNamePrefix = "";
		String fnNamePrefixRaw = namePrefix.trim();
		if(!fnNamePrefixRaw.isEmpty()){
			fnNamePrefix = fnNamePrefixRaw + "_EVT_";
		}

		xBuilder.append("/* Event Mask Macros */\n");
		try {
			int fixed_pad = maxStringSize + 12 + fnNamePrefix.length();
			xBuilder.append(GUtils.fixedLengthStringLeftAlign("#define " + fnNamePrefix.toUpperCase() + "NONE", fixed_pad) + "(0x00000000u)\n");
			int i = 0;
			for (String line : txtAreaEventNames.getText().split("\\n"))
			{
				if(i > 31){
					xBuilder.append("\n\nParsing Error: Max number of events exceeeded");
					return;
				}
				line = line.trim();
				line = line.toUpperCase();
				line = "#define " + fnNamePrefix.toUpperCase() + line;
				line = GUtils.fixedLengthStringLeftAlign(line, fixed_pad);
				line += "(0x" + GUtils.intToHexWithPadding((1 << i),32) + "u)\n";
				i++;
				xBuilder.append(line);
			}
			xBuilder.append(GUtils.fixedLengthStringLeftAlign("#define " + fnNamePrefix.toUpperCase() + "ALL_MASK", fixed_pad));
			xBuilder.append("(0xffffffffu)\n\n");
		} catch (Exception e) {
			xBuilder.append("\n\nParsing Failed");
		}	
	}		

	private static void addEvtEnum(String namePrefix, JFXTextArea txtAreaEventNames, StringBuilder xBuilder) {
		String fnNamePrefix = "";
		String fnNamePrefixRaw = namePrefix.trim();
		if(!fnNamePrefixRaw.isEmpty()){
			fnNamePrefix = fnNamePrefixRaw + "_FSM_EVT_";
		}
		String enumName = fnNamePrefixRaw.toLowerCase()+ "_fsm_evt";
		xBuilder.append("/**\n");
		xBuilder.append(" * @enum " + enumName + "_t"+ "\n");
		xBuilder.append(" * @brief Enum to \n");
		xBuilder.append(" */\n");
		xBuilder.append("typedef enum\n{\n");
		try {
			int i = 0;
			for (String line : txtAreaEventNames.getText().split("\\n"))
			{
				line = line.trim();
				line = line.toUpperCase();

				line = "    " + fnNamePrefix.toUpperCase() + line;
				if(i == 0){
					line += " = 0";
				}
				line += ",\n";
				i++;
				xBuilder.append(line);
			}
			xBuilder.append("    " + fnNamePrefix.toUpperCase() + "MAX_EVTS\n");
			xBuilder.append("}" + enumName + "_t;\n\n");
		} catch (Exception e) {
			xBuilder.append("\n\nParsing Failed");
		}	
	}
	
	private static void addStateEnum(String namePrefix,
			 JFXTextArea txtAreaStateNames, StringBuilder xBuilder) {
		String fnNamePrefix = "";
		String fnNamePrefixRaw = namePrefix.trim();
		if(!fnNamePrefixRaw.isEmpty()){
			fnNamePrefix = fnNamePrefixRaw + "_FSM_";
		}
		String enumName = fnNamePrefixRaw.toLowerCase()+ "_fsm_state";
		xBuilder.append("/**\n");
		xBuilder.append(" * @enum " + enumName + "_t"+ "\n");
		xBuilder.append(" * @brief Enum to \n");
		xBuilder.append(" */\n");
		xBuilder.append("typedef enum\n{\n");
		try {
			int i = 0;
			for (String line : txtAreaStateNames.getText().split("\\n"))
			{
				line = line.trim();
				line = line.toUpperCase();

				line = "    " + fnNamePrefix.toUpperCase() + line;
				if(i == 0){
					line += " = 0";
				}
				line += ",\n";
				i++;
				xBuilder.append(line);
			}
			xBuilder.append("    " + fnNamePrefix.toUpperCase() + "MAX_STATES\n");
			xBuilder.append("}" + enumName + "_t;\n\n");
		} catch (Exception e) {
			xBuilder.append("\n\nParsing Failed");
		}
	}

	private static void addFnPrototypes(String namePrefix, String prefix, String postfix, JFXTextArea txtAreaStateNames,
			StringBuilder xBuilder) {
		String fnNamePrefix = "";
		String fnNamePrefixRaw = namePrefix.trim();
		if(!fnNamePrefixRaw.isEmpty()){
			fnNamePrefix = fnNamePrefixRaw + "_FSM_";
		}
		String fnProtPrefix = prefix.trim();
		String fnProtPostfix = postfix.trim();

		xBuilder.append("/* Function Prototypes */\n");

		try {
			for (String line : txtAreaStateNames.getText().split("\\n"))
			{
				line = line.trim();
				line = line.toLowerCase();

				line = fnNamePrefix.toLowerCase() + line;
				xBuilder.append(fnProtPrefix + " " + line + fnProtPostfix + ";\n");
			}
			xBuilder.append("\n\n");
		} catch (Exception e) {
			xBuilder.append("\n\nParsing Failed");
		}
	}

	private static void addStateTable(String namePrefix, String prefix, String postfix, JFXTextArea txtAreaStateNames,
			StringBuilder xBuilder) {
		String fnNamePrefix = "";
		String fnNamePrefixRaw = namePrefix.trim();
		if(!fnNamePrefixRaw.isEmpty()){
			fnNamePrefix = fnNamePrefixRaw + "_FSM_";
		}
		String fnProtPrefix = prefix.trim();
		String fnProtPostfix = postfix.trim();

		xBuilder.append("/* State Table */\n");
		xBuilder.append(fnProtPrefix + " (*const " +  fnNamePrefix.toLowerCase() + "table [" + fnNamePrefix.toUpperCase() + "MAX_STATES]) " + fnProtPostfix + " =\n");
		xBuilder.append("{\n");
		try {
			for (String line : txtAreaStateNames.getText().split("\\n"))
			{
				line = line.trim();
				line = line.toLowerCase();

				line = fnNamePrefix.toLowerCase() + line;
				xBuilder.append("    /* " + line + " */\n");
				xBuilder.append("    " + line + ",\n");
			}
			xBuilder.append("};\n\n");
		} catch (Exception e) {
			xBuilder.append("\n\nParsing Failed");
		}
	}

	private static void addFnDefinitions(String namePrefix, String prefix, String postfix,
			JFXTextArea txtAreaStateNames, JFXTextArea txtAreaEventNames, StringBuilder xBuilder) {
		String fnNamePrefix = "";
		String fnNamePrefixRaw = namePrefix.trim();
		if(!fnNamePrefixRaw.isEmpty()){
			fnNamePrefix = fnNamePrefixRaw + "_FSM_";
		}
		String fnProtPrefix = prefix.trim();
		String fnProtPostfix = postfix.trim();
		String switchString = getEvtSwitchcase(namePrefix, txtAreaEventNames);
		xBuilder.append("/* Function Definitions */\n");

		try {
			for (String line : txtAreaStateNames.getText().split("\\n"))
			{
				line = line.trim();
				line = line.toLowerCase();

				line = fnNamePrefix.toLowerCase() + line;
				xBuilder.append(fnProtPrefix + " " + line + fnProtPostfix + "\n");
				xBuilder.append("{\n");
				xBuilder.append("    /* TODO */\n\n");
				xBuilder.append(switchString);
				xBuilder.append("}\n\n");
			}
			xBuilder.append("\n\n");
		} catch (Exception e) {
			xBuilder.append("\n\nParsing Failed");
		}
	}
	
	private static String getEvtSwitchcase(String namePrefix, JFXTextArea txtAreaEventNames) {
		String result="";
		String fnNamePrefix = "";
		String fnNamePrefixRaw = namePrefix.trim();
		if(!fnNamePrefixRaw.isEmpty()){
			fnNamePrefix = fnNamePrefixRaw + "_FSM_EVT_";
		}
		result = "    switch(evt)\n";
		result += "    {\n";
		
		try {
			for (String line : txtAreaEventNames.getText().split("\\n"))
			{
				line = line.trim();
				line = line.toUpperCase();

				line = fnNamePrefix.toUpperCase() + line;
				result += "        case " + line + ":\n";
				result += "            \n";
				result += "            break;\n";
			}
			result += "        default:\n";
			result += "            \n";
			result += "            break;\n";			
			result += "    }\n\n";
			return result;
		} catch (Exception e) {
			return "Parsing Failed";
		}	
	}	
}
