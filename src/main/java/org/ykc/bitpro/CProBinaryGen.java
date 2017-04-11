package org.ykc.bitpro;

import java.io.FileWriter;
import java.io.IOException;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableView;

public class CProBinaryGen {
	private static int itemCount ;
	public static StringBuilder run(TreeTableView<CProRow> ttViewCProLoad) {
		StringBuilder xBuilder = new StringBuilder();
		SProMacroGen.addPreface(xBuilder);
		itemCount = 0;
		xBuilder.append("\n\n");
		addBinary(ttViewCProLoad.getRoot(), xBuilder);
		return xBuilder;
	}

    private static void addBinary(TreeItem<CProRow> root, StringBuilder xBuilder){
        for(TreeItem<CProRow> child: root.getChildren()){
            if(child.getChildren().isEmpty()){
            	CProRow row =  child.getValue();
            	addBinaryString(row, xBuilder);
            } else {
            	addBinary(child, xBuilder);
            }
        }
    }

    private static void addBinaryString(CProRow row, StringBuilder xBuilder){
    	Integer value = Utils.parseStringtoNumber(row.getValue()).intValue();
    	Integer len = Utils.parseStringtoNumber(row.getTotalbytes()).intValue();
    	String hexString = Utils.intToHexWithPadding(value, (len*8));
    	int j = len*2 - 2;
    	for(int i = 0; i < len; i++){
    		/* Little endian */
    		xBuilder.append("0x" + Utils.getSubString(hexString,j,2) + ", ");
    		j = j - 2;
        	itemCount++;
        	if(itemCount == 10){
        		xBuilder.append("\n");
        		itemCount = 0;
        	}
    	}
    }
}
