package org.ykc.bitpro;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.jfoenix.controls.JFXTextArea;

public class SwitchCaseGenerator {

	public static StringBuilder run(JFXTextArea txtAreaUtilGen) {
		StringBuilder xBuilder = new StringBuilder();
		addPreface(xBuilder);
		xBuilder.append(addSwitchCase(txtAreaUtilGen));
		return xBuilder;
	}

	private static void addPreface(StringBuilder xBuilder){
		String prefaceString = "BitPro Auto Generated File ";
		DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
		Date dateobj = new Date();
		prefaceString += df.format(dateobj) + "\n\n";
		xBuilder.append(prefaceString);
	}

	private static String addSwitchCase(JFXTextArea txtAreaUtilGen) {
		String result = "";
		result = "    switch(#)\n";
		result += "    {\n";
		
		try {
			for (String line : txtAreaUtilGen.getText().split("\\n"))
			{
				line = line.trim();
				String[] xStrings = line.split(" ");
				line = xStrings[0];
				int len = line.length();
				if(line.charAt(len -1) == ','){
					line = line.substring(0, len - 1);
				}
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
