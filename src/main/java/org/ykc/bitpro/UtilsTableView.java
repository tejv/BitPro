package org.ykc.bitpro;

import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

public class UtilsTableView {
	public static void moveUpSelItem(TableView table)
	{
        int selectedIndex = table.getSelectionModel().getSelectedIndex();
        if(selectedIndex <= 0)
        {
        	return;
        }
        Object removedItem = table.getItems().remove(selectedIndex);
        int newIndex = selectedIndex - 1;
        table.getItems().add(newIndex, removedItem);
        table.getSelectionModel().clearAndSelect(newIndex);	
	}
	
	public static void moveDownSelItem(TableView table)
	{
        int selectedIndex = table.getSelectionModel().getSelectedIndex();
        int maxIndex = table.getItems().size() - 1;
        if(selectedIndex == maxIndex)
        {
        	return;
        }
        Object removedItem = table.getItems().remove(selectedIndex);
        int newIndex = selectedIndex + 1;
        table.getItems().add(newIndex, removedItem);
        table.getSelectionModel().clearAndSelect(newIndex);	
	}

	public static void removeSelItem(TableView table)
	{
        ObservableList selectedItems;
        selectedItems = table.getSelectionModel().getSelectedItems();
        table.getItems().removeAll(selectedItems);
	}
	
}
