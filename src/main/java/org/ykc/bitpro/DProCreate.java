package org.ykc.bitpro;

import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class DProCreate {
	private Stage primaStage;
	
	public void browse(){
		DirectoryChooser chooser = new DirectoryChooser();
		chooser.setTitle("BitPro Files");
//		File defaultDirectory = new File("c:/dev/javafx");
//		chooser.setInitialDirectory(defaultDirectory);
//		File selectedDirectory = chooser.showDialog(primaryStage);
	}
}
