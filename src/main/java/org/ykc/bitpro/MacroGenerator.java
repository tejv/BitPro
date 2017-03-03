package org.ykc.bitpro;

import java.awt.Checkbox;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class MacroGenerator {
	public static StringBuilder run(File file){
		StringBuilder xBuilder = new StringBuilder();
		addPreface(xBuilder);

		if(GUtils.getFileExtension(file).equals("spro"))
		{
    		Document xmlDoc = BProUtils.getDocument(file);
    		if(xmlDoc == null)
    		{
    			xBuilder.append("Macro Generation Failed: File Load failed");
    			return xBuilder;
    		}
    		Element sElement = (Element)(xmlDoc.getElementsByTagName("simple").item(0));
			genMacrosSPRO(xBuilder, sElement);
		}
		return xBuilder;
	}

	private static void addPreface(StringBuilder xBuilder){
		String prefaceString = "Auto Generated File ";
		DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
		Date dateobj = new Date();
		prefaceString += df.format(dateobj) + "\n";
		xBuilder.append(prefaceString);
	}
	
	private static void genMacrosSPRO(StringBuilder xBuilder, Element sElement) {
		genStructSPRO(xBuilder, sElement);
		genPosMaskSPRO(xBuilder,sElement);
	}

	private static void genStructSPRO(StringBuilder xBuilder, Element sElement) {
		String nameString = BProUtils.getNameSimpleXML(sElement).toLowerCase();
		xBuilder.append("/**\n");
		xBuilder.append(" * @union " + nameString + "_t"+ "\n");
		xBuilder.append(" * @brief \n");
		xBuilder.append(" */\n");
		xBuilder.append("typedef union\n{\n    ");
		String tString = getLenType(BProUtils.getLengthSimpleXML(sElement));
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
		for(int i = 0; i < BProUtils.getMaxFieldsSimpleXML(sElement); i++){
			int size = BProUtils.getFieldNameSimpleXML(sElement, i).length();
			if(size > maxStringSize){
				maxStringSize = size;
			}
		}		
		
		for(int i = 0; i < BProUtils.getMaxFieldsSimpleXML(sElement); i++){
			
			String a = "        " + tString + " " + BProUtils.getFieldNameSimpleXML(sElement, i).toLowerCase();
			String b = ":" + BProUtils.getFieldSizeSimpleXML(sElement, i).toString() + ";";
			String desc = BProUtils.getFieldDescSimpleXML(sElement, i);
			String c ="";
			if(!desc.trim().isEmpty())
			{
				c = "/**<" + BProUtils.getFieldDescSimpleXML(sElement, i)+ " */";
			}
			a = GUtils.fixedLengthStringLeftAlign(a, (maxStringSize + 30));
			b = GUtils.fixedLengthStringLeftAlign(b, 6);
			xBuilder.append(a + b + c);
			xBuilder.append("\n");
		}
	}
	
	private static void genPosMaskSPRO(StringBuilder xBuilder, Element sElement) {
		xBuilder.append("\n/* Macros */\n");
		Integer len = BProUtils.getLengthSimpleXML(sElement);
		int maxStringSize = 0;
		for(int i = 0; i < BProUtils.getMaxFieldsSimpleXML(sElement); i++){
			int size = BProUtils.getFieldNameSimpleXML(sElement, i).length();
			if(size > maxStringSize){
				maxStringSize = size;
			}
		}		
		
		for(int i = 0; i < BProUtils.getMaxFieldsSimpleXML(sElement); i++){
			String prefix = "";
			String name = prefix + BProUtils.getFieldNameSimpleXML(sElement, i).toUpperCase();
			String a = "#define " + name;
			String a1 = a + "_MASK";
			String a2 = a + "_POS";
			String a3 = a + "_POS_MASK";
			Integer size = BProUtils.getFieldSizeSimpleXML(sElement, i);
			Integer offset = BProUtils.getFieldOffsetSimpleXML(sElement, i);
			Integer mask = (int) (GUtils.get32bitMask(size) << offset);
			Integer pos_mask = (int) (1 << offset);
			
			String b1 = "(0x" + GUtils.intToHexWithPadding(mask, len) + ")";
			String b2 = "(" + offset.toString() + ")";
			String b3 = "(0x" + GUtils.intToHexWithPadding(pos_mask, len) + ")";
			a1 = GUtils.fixedLengthStringLeftAlign(a1, (maxStringSize + 30));
			a2 = GUtils.fixedLengthStringLeftAlign(a2, (maxStringSize + 30));
			a3 = GUtils.fixedLengthStringLeftAlign(a3, (maxStringSize + 30));
			xBuilder.append(a1 + b1);
			xBuilder.append("\n");
			xBuilder.append(a2 + b2);
			if(size > 1){
				xBuilder.append("\n");
				xBuilder.append(a3 + b3);
			}
			xBuilder.append("\n\n");
		}
		
	}	

}

