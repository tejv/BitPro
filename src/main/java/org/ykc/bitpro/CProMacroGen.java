package org.ykc.bitpro;

import java.io.File;
import java.util.concurrent.CountDownLatch;

import org.jdom2.Document;
import org.jdom2.Element;
import org.ykc.bitpro.Utils.Radix;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableView;

public class CProMacroGen {
	public static StringBuilder run(File cproFile, String prefix){
		StringBuilder xBuilder = new StringBuilder();
		SProMacroGen.addPreface(xBuilder);
		
		Document xmlDoc = UtilsBPro.getJDOM2Doc(cproFile);
		if(xmlDoc == null)
		{
			xBuilder.append("\n\nFailed to load .cpro file");
			return xBuilder;
		}
		Element cElement = xmlDoc.getRootElement();
		genStruct(xBuilder, cElement);

		return xBuilder;
	}
	
	private static void genStruct(StringBuilder xBuilder, Element cElement){
		xBuilder.append("\n");
		String nameString = cElement.getChild("head").getChildText("cname");
		xBuilder.append("/**\n");
		xBuilder.append(" * @typedef " + nameString + "_t"+ "\n");
		xBuilder.append(" * @brief Structure to \n");
		xBuilder.append(" */\n");
		xBuilder.append("typedef struct\n{\n");		
		
		java.util.List<Element> listOfFields = cElement.getChild("body").getChildren("cfield");
		
		int maxTypeStringSize = 0;
		int maxNameStringSize = 0;
		for(int i = 0; i < listOfFields.size(); i++){
			int typeSize = listOfFields.get(i).getChildText("ctype").length();
			String cName = listOfFields.get(i).getChildText("cname");
			String cCount = listOfFields.get(i).getChildText("cCount");
			int count = Integer.parseInt(cCount);
			String countString = "";
			if(count > 1){
				countString = "[" + count + "]";
			}
			cName += countString;
			
			if(typeSize > maxTypeStringSize){
				maxTypeStringSize = typeSize;
			}	
			if(cName.length() > maxNameStringSize){
				maxNameStringSize = cName.length();
			}
		}
		
		for(int i = 0; i < listOfFields.size(); i++){
			Element e = listOfFields.get(i);
			String cType = e.getChildText("ctype");
			String cName = e.getChildText("cname");
			String cCount = e.getChildText("cCount");
			String cDesc = e.getChildText("cdesc");
			String cOffset = e.getChildText("cOffset");
			
			String aString = "    " + cType;
			aString = Utils.fixedLengthStringLeftAlign(aString, (maxTypeStringSize + 8));
			aString += cName;
			int count = Integer.parseInt(cCount);
			if(count > 1){
				aString += "[" + count + "]";
			}
			aString += ";";
			aString = Utils.fixedLengthStringLeftAlign(aString, (maxTypeStringSize + 8 + maxNameStringSize + 4));
			xBuilder.append(aString + " /* Offset 0x" + Integer.toHexString(Integer.parseInt(cOffset)) +
					": " + cDesc +" */");
			xBuilder.append("\n");
		}	
		xBuilder.append("}" + nameString + "_t;\n");
	}

}
