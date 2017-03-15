package org.ykc.bitpro;

import java.io.File;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class CProMacroGen {
	public static StringBuilder run(File file, String prefix){
		StringBuilder xBuilder = new StringBuilder();
		SProMacroGen.addPreface(xBuilder);

		/* TODO: For each simple element run pos/mask/formation generate */
		/* TODO: For each simple element run enum/struct generate */
		/* TODO: Final typedef structure for cpro */
		return xBuilder;
	}
}
