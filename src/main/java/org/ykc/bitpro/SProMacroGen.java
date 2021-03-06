package org.ykc.bitpro;

import java.awt.Checkbox;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FilenameUtils;
import org.jdom2.Document;
import org.jdom2.Element;

import javafx.collections.ObservableList;


public class SProMacroGen {
	public static StringBuilder run(File file, String prefix){
		StringBuilder xBuilder = new StringBuilder();
		addPreface(xBuilder);

		if(Utils.getFileExtension(file).equals("spro"))
		{
    		Document xmlDoc = UtilsBPro.getJDOM2Doc(file);
    		if(xmlDoc == null)
    		{
    			xBuilder.append("Macro Generation Failed: File Load failed");
    			return xBuilder;
    		}
    		Element sElement = xmlDoc.getRootElement();
			genMacrosSPRO(xBuilder, sElement, prefix);
		}
		return xBuilder;
	}

	public static void addPreface(StringBuilder xBuilder){
		String prefaceString = "BitPro Auto Generated File ";
		DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
		Date dateobj = new Date();
		prefaceString += df.format(dateobj) + "\n";
		xBuilder.append(prefaceString);
	}

	private static void genMacrosSPRO(StringBuilder xBuilder, Element sElement, String prefix) {
		xBuilder.append("\n/* Macros */\n");
		genPosMaskSPRO(xBuilder,sElement, prefix);
		xBuilder.append("\n/* Formation Macros */\n");
		genFormationMacroSPRO(xBuilder,sElement);
		genEnumSPRO(xBuilder, sElement);
		genStructSPRO(xBuilder, sElement);
		xBuilder.append("\n/* Java Snippet */\n");
		genJavaSnippet(xBuilder,sElement, prefix);
	}

	public static void genStructSPRO(StringBuilder xBuilder, Element sElement) {
		String nameString = UtilsBPro.getSProName(sElement).toLowerCase();
		xBuilder.append("/**\n");
		xBuilder.append(" * @typedef " + nameString + "_t"+ "\n");
		xBuilder.append(" * @brief Union to \n");
		xBuilder.append(" */\n");
		xBuilder.append("typedef union\n{\n    ");
		String tString = getLenType(UtilsBPro.getSProLength(sElement));
		xBuilder.append(tString + " val;\n    ");
		xBuilder.append("struct\n    {\n");
		/* Append fields */
		getfieldsSPRO(xBuilder,sElement, tString);
		xBuilder.append("    }field;\n");
		xBuilder.append("}" + nameString + "_t;\n");
	}

	private static String getLenType(Integer len){
		if(len == 8){
			return "uint8_t";
		}
		else if(len == 16){
			return "uint16_t";
		}
		else{
			return "uint32_t";
		}
	}

	private static void getfieldsSPRO(StringBuilder xBuilder, Element sElement, String tString){
		int maxStringSize = 0;
		for(int i = 0; i < UtilsBPro.getSProFieldsCount(sElement); i++){
			int size = UtilsBPro.getSProFieldName(sElement, i).length();
			if(size > maxStringSize){
				maxStringSize = size;
			}
		}

		for(int i = 0; i < UtilsBPro.getSProFieldsCount(sElement); i++){

			String a = "        " + tString + " " + UtilsBPro.getSProFieldName(sElement, i).toLowerCase();
			String b = ":" + UtilsBPro.getSProFieldSize(sElement, i).toString() + ";";
			String desc = UtilsBPro.getSProFieldDesc(sElement, i);
			String c ="";
			if(!desc.trim().isEmpty())
			{
				c = "/**< " + UtilsBPro.getSProFieldDesc(sElement, i)+ " */";
			}
			a = Utils.fixedLengthStringLeftAlign(a, (maxStringSize + 30));
			b = Utils.fixedLengthStringLeftAlign(b, 6);
			xBuilder.append(a + b + c);
			xBuilder.append("\n");
		}
	}

	public static void genPosMaskSPRO(StringBuilder xBuilder, Element sElement, String prefix) {
		Integer len = UtilsBPro.getSProLength(sElement);
		int maxStringSize = 0;
		for(int i = 0; i < UtilsBPro.getSProFieldsCount(sElement); i++){
			int size = UtilsBPro.getSProFieldName(sElement, i).length();
			if(size > maxStringSize){
				maxStringSize = size;
			}
		}

		for(int i = 0; i < UtilsBPro.getSProFieldsCount(sElement); i++){
			String prefixString = prefix.trim();
			if(!prefixString.isEmpty()){
				prefixString += "_";
			}
			String name = prefixString + UtilsBPro.getSProFieldName(sElement, i).toUpperCase();
			String a = "#define " + name;
			String a1 = a + "_MASK";
			String a2 = a + "_POS";
			String a3 = a + "_POS_MASK";
			String a4 = "#define " + "GET_" + name + "(input)";
			Integer size = UtilsBPro.getSProFieldSize(sElement, i);
			Integer offset = UtilsBPro.getSProFieldOffset(sElement, i);
			Integer mask = (int) (Utils.get32bitMask(size) << offset);
			Integer pos_mask = (int) (1 << offset);

			String mString = "0x" + Utils.intToHexWithPadding(mask, len);
			String pString = offset.toString();
			String b1 = "(" + mString + ")";
			String b2 = "(" + pString + ")";
			String b3 = "(0x" + Utils.intToHexWithPadding(pos_mask, len) + ")";
			String b4 = "(((input) & " + mString + ") >> " + pString + ")";
			a1 = Utils.fixedLengthStringLeftAlign(a1, (maxStringSize + 30));
			a2 = Utils.fixedLengthStringLeftAlign(a2, (maxStringSize + 30));
			a3 = Utils.fixedLengthStringLeftAlign(a3, (maxStringSize + 30));
			a4 = Utils.fixedLengthStringLeftAlign(a4, (maxStringSize + 30));

			xBuilder.append(a1 + b1);
			xBuilder.append("\n");
			xBuilder.append(a2 + b2);
			if(size > 1){
				xBuilder.append("\n");
				xBuilder.append(a3 + b3);
			}
			xBuilder.append("\n");
			xBuilder.append(a4 + b4);
			xBuilder.append("\n\n");
		}
	}

	public static void genFormationMacroSPRO(StringBuilder xBuilder, Element sElement) {
		String name = "FORM_" + UtilsBPro.getSProName(sElement).toUpperCase();
		String a = "#define " + name + "(";
		int len1 = a.length();
		int maxStringSize = 0;
		for(int i = 0; i < UtilsBPro.getSProFieldsCount(sElement); i++){
			int size = UtilsBPro.getSProFieldName(sElement, i).length();
			if(size > maxStringSize){
				maxStringSize = size;
			}
		}

		for(int i = 0; i < UtilsBPro.getSProFieldsCount(sElement); i++){
			if(i != 0){
				a += Utils.fixedLengthStringLeftAlign(",", (maxStringSize + 1 - UtilsBPro.getSProFieldName(sElement, i-1).length()));
				a += "  \\\n";
				a += Utils.fixedLengthStringLeftAlign(" ", len1);
			}

			a += UtilsBPro.getSProFieldName(sElement, i);
		}
		a += ")" + Utils.fixedLengthStringLeftAlign(" ", (maxStringSize + 3 - UtilsBPro.getSProFieldName(sElement, UtilsBPro.getSProFieldsCount(sElement)-1).length()));

		int len = len1 + maxStringSize + 4;
		String temp = "";

		for(int i = 0; i < UtilsBPro.getSProFieldsCount(sElement); i++){

			if(i != 0){
				a += " |";
				a += Utils.fixedLengthStringLeftAlign(" ", (maxStringSize + 11 - temp.length()));
				a += "    \\\n";
				a += Utils.fixedLengthStringLeftAlign(" ", len + 4);
			}else{
				a += "    ";
			}
			temp = "(("+ UtilsBPro.getSProFieldName(sElement, i).toLowerCase()+ ") << " +
				      UtilsBPro.getSProFieldOffset(sElement, i).toString() + ")";
			a += temp;
		}
		xBuilder.append(a);
		xBuilder.append("\n\n");
	}

	public static void genEnumSPRO(StringBuilder xBuilder, Element sElement) {

		for(int i = 0; i < UtilsBPro.getSProFieldsCount(sElement); i++){

			Element xElement = UtilsBPro.getSProFieldEnumElement(sElement, i);
			if(xElement.getAttribute("at_ename") != null)
			{
				String nameString = xElement.getAttribute("at_ename").getValue();

				xBuilder.append("/**\n");
				xBuilder.append(" * @typedef " + nameString + "_t"+ "\n");
				xBuilder.append(" * @brief Enum to \n");
				xBuilder.append(" */\n");
				xBuilder.append("typedef enum\n{\n");
				for(int j = 0; j < UtilsBPro.getSProFieldEnumCount(sElement, i); j++){
					xBuilder.append("    " + UtilsBPro.getSProEnumName(xElement, j) + " = " + UtilsBPro.getSProEnumValueString(xElement, j));
					if(j !=  (UtilsBPro.getSProFieldEnumCount(sElement, i) -1)){
						xBuilder.append(",\n");
					}
					else{
						xBuilder.append("\n");
					}
				}
				xBuilder.append("}" + nameString + "_t;\n\n");
			}
		}
	}

	public static void genJavaSnippet(StringBuilder xBuilder, Element sElement, String prefix) {
		String nameSpro = UtilsBPro.getSProName(sElement).toUpperCase();
		String nameFunc = "add" + nameSpro;
		xBuilder.append("    private static void "+ nameFunc + "(ObservableList<DetailsRow> list, Long input) {\n");
		xBuilder.append("        list.add(new DetailsRow.Builder().name(\"" + nameSpro + "\").value(\"0x\" + Long.toHexString(input)).level(0).build());\n");
		xBuilder.append("        Long val;\n");
		xBuilder.append("        Integer valInt;\n");
		xBuilder.append("        String valString;\n");

		for(int i = UtilsBPro.getSProFieldsCount(sElement) - 1; i >=0 ; i--){
			String prefixString = prefix.trim();
			if(!prefixString.isEmpty()){
				prefixString += "_";
			}
			String name = prefixString + UtilsBPro.getSProFieldName(sElement, i).toLowerCase();
			Integer size = UtilsBPro.getSProFieldSize(sElement, i);
			Integer offset = UtilsBPro.getSProFieldOffset(sElement, i);
			Integer mask = (int) (Utils.get32bitMask(size) << offset);
			Integer pos_mask = (int) (1 << offset);

			String mString = "0x" + Utils.intToHexWithPadding(mask, 32);
			String pString = offset.toString();
			String b4 = "((input) & " + mString + ") >> " + pString;
			if(!(name.startsWith("rsvd"))){
				if(size > 4){
					xBuilder.append("        val = " + b4 + ";\n");
					xBuilder.append("        valString = \"0x\" + Long.toHexString(val) + \" (\" + Long.toString(val) + \")\";\n");

				}
				else{
					xBuilder.append("        valInt = (int)(" + b4 + ");\n");
					if(size == 1){
						xBuilder.append("        valString = enumYesNo.values()[valInt].name();\n");
					}
					else{
						xBuilder.append("        valString = enumDummy.values()[valInt].name();\n");
					}

				}
				xBuilder.append("        list.add(new DetailsRow.Builder().name(\"" + name + "\").value(valString).level(1).build());\n");
			}
			else{
				xBuilder.append("        val = " + b4 + ";\n");
				xBuilder.append("        valString = \"0x\" + Long.toHexString(val);\n");
				xBuilder.append("        if(val == 0){\n");
				xBuilder.append("            list.add(new DetailsRow.Builder().name(\"rsvd\").value(valString).level(1).build());\n");
				xBuilder.append("        }\n");
				xBuilder.append("        else{\n");
				xBuilder.append("            list.add(new DetailsRow.Builder().name(\"rsvd\").value(valString).level(1).bcolor(BG.RED).build());\n");
				xBuilder.append("        }\n");

			}

		}
		xBuilder.append("    }\n\n");
	}
}

