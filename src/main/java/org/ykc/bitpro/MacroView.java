package org.ykc.bitpro;

import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

public class MacroView {
    public static void display(String title, StringBuilder message) {
        Stage window = new Stage();

        //Block events to other windows
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);

        TextArea area = new TextArea();
        area.setEditable(false);
        area.setText(message.toString());
        

        BorderPane layout = new BorderPane();
        layout.setCenter(area);

        //Display window and wait for it to be closed before returning
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }
}
