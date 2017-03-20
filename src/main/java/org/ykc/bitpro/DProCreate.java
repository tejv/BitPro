package org.ykc.bitpro;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.FilenameUtils;
import org.controlsfx.control.StatusBar;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class DProCreate {
	private Stage primaryStage;
	private BorderPane borderPaneMainWindow;
	private JFXListView<String> lViewDPro;
	private TableView<DProRow> tViewDPro;
    private JFXTextField txtDProName;
    private TextField txtDProTypeName;
    private TextField txtDProFieldName;
    private TextField txtDProFieldSize;
    private TextField txtDProFieldRPath;
    private TextField txtDProFieldDesc;
    private JFXTextField txtDProBPath;
    private StatusBar statusBar;
	private ObservableList<String> rPathList = FXCollections.observableArrayList();
	public static final String basePath = System.getProperty("user.home") + "/BitPro/";

	public DProCreate(Stage primaryStage, JFXListView<String> lViewDPro, TableView<DProRow> tViewDPro,
			JFXTextField txtDProName, TextField txtDProTypeName, TextField txtDProFieldName, TextField txtDProFieldSize,
			TextField txtDProFieldRPath, TextField txtDProFieldDesc, StatusBar statusBar, BorderPane borderPaneMainWindow,
			JFXTextField txtDProBPath) {
		this.primaryStage = primaryStage;
		this.lViewDPro = lViewDPro;
		this.tViewDPro = tViewDPro;
		this.txtDProName = txtDProName;
		this.txtDProTypeName = txtDProTypeName;
		this.txtDProFieldName = txtDProFieldName;
		this.txtDProFieldSize = txtDProFieldSize;
		this.txtDProFieldRPath = txtDProFieldRPath;
		this.txtDProFieldDesc = txtDProFieldDesc;
		this.statusBar = statusBar;
		this.borderPaneMainWindow = borderPaneMainWindow;
		this.txtDProBPath = txtDProBPath;

		lViewDPro.setOnMouseClicked(new EventHandler<MouseEvent>() {

		    @Override
		    public void handle(MouseEvent click) {

		        if (click.getClickCount() == 2) {
		        	doubleClicked();
		        }
		    }
		});

	}

	public void browse(){
		DirectoryChooser chooser = new DirectoryChooser();
		chooser.setTitle("BitPro Files");
		File defaultDirectory = new File(txtDProBPath.getText());
		if(!defaultDirectory.exists())
		{
			defaultDirectory.mkdir();
		}
		if(defaultDirectory.exists()){
			chooser.setInitialDirectory(defaultDirectory);
		}
		File selDir = chooser.showDialog(primaryStage);
		Preferences.setDproLastBrowseDir(selDir);
		browseDir(selDir);
	}

	public void browseDir(File selDir)
	{
		if(selDir != null){
			lViewDPro.getItems().clear();
			rPathList.clear();
			for(File f: selDir.listFiles()){
				String extension = Utils.getFileExtension(f);
				if((extension.equals("spro")) || (extension.equals("cpro")) )
				{
					String path = f.getAbsolutePath();
					String relative = new File(txtDProBPath.getText() + "/").toURI().relativize(new File(path).toURI()).getPath();
					rPathList.add(relative);
					lViewDPro.getItems().add(FilenameUtils.getBaseName(f.getName()) + "_t");
				}
			}
		}
	}

	public void doubleClicked() {
        int idx = lViewDPro.getSelectionModel().getSelectedIndex();
        String type = lViewDPro.getSelectionModel().getSelectedItem();
        DProRow row;
        try {
			row = new DProRow(type, "", "1", "", rPathList.get(idx));
			tViewDPro.getItems().add(row);
			tViewDPro.getSelectionModel().select(tViewDPro.getItems().size() - 1);
		} catch (Exception e) {
		}
	}

	public boolean addField()
	{
		DProRow row;
		try {
			row = new DProRow(txtDProTypeName.getText(), txtDProFieldName.getText(),
				txtDProFieldSize.getText(), txtDProFieldDesc.getText(), txtDProFieldRPath.getText() );
		} catch (Exception e) {
			statusBar.setText("Invalid Entry");
			return false;
		}
		tViewDPro.getItems().add(row);
		statusBar.setText("Row Added");
		return true;
	}

	public boolean modifyField()
	{
		DProRow row;
		try {
			row = new DProRow(txtDProTypeName.getText(), txtDProFieldName.getText(),
				txtDProFieldSize.getText(), txtDProFieldDesc.getText(), txtDProFieldRPath.getText() );
		} catch (Exception e) {
			statusBar.setText("Invalid Entry");
			return false;
		}
		DProRow x = tViewDPro.getSelectionModel().getSelectedItem();
		x.setType(row.getType());
		x.setName(row.getName());
		x.setSize(row.getSize());
		x.setDesc(row.getDesc());
		x.setRpath(row.getRpath());

		/* Workaround for tableview update issue */
		tViewDPro.getColumns().get(0).setVisible(false);
		tViewDPro.getColumns().get(0).setVisible(true);
		statusBar.setText("Row Updated");
		return true;
	}

	public void save() {
    	if(txtDProName.getText().trim().isEmpty() == true)
    	{
    		statusBar.setText("Please provide a name");
    		return;
    	}

    	File file = UtilsBPro.saveDProFile(borderPaneMainWindow.getScene().getWindow(), txtDProName.getText().trim());

        if (file != null) {
        	if(create(file) == true)
        	{
        		statusBar.setText("Save Success");
        	}
        }
        else
        {
        	statusBar.setText("Operation Cancelled");
        }

	}

	private boolean create(File fileName)
	{
		String name = txtDProName.getText().trim();
		/* TODO: Put other checks if any */
		if(name.isEmpty())
		{
			statusBar.setText("Error in parsing: Name not provided");
			return false;
		}


		Document doc = new Document();
		Element theRoot = new Element("design");
		doc.setRootElement(theRoot);

		Element head = new Element("head");
		theRoot.addContent(head);

		Element body = new Element("body");
		theRoot.addContent(body);

		Element dname = new Element("dname");
		dname.setText(name);
		head.addContent(dname);

		Element type = new Element("dtype");
		type.setText("design");
		head.addContent(type);

		Element basePath = new Element("dBasePath");
		basePath.setText(txtDProBPath.getText());
		head.addContent(basePath);

		for(DProRow row: tViewDPro.getItems())
		{
			if(writeField(body, row.getType(), row.getName(), row.getSize(),
					row.getDesc(), row.getRpath()) == false)
			{
				statusBar.setText("Error in parsing: Field Parse Error. Mandatory field may be missing");
				return false;
			}
		}

		XMLOutputter xmlOutput = new XMLOutputter(Format.getPrettyFormat());
		try {
			xmlOutput.output(doc, new FileOutputStream(fileName));
		} catch (IOException e) {
			statusBar.setText("Error in creating dpro file: IO Error");
			return false;
		}

		return true;
	}

	private  boolean writeField(Element root, String type, String name,
			String size, String desc, String rpath){

		if(name.trim().isEmpty())
		{
			return false;
		}
		Element field = new Element("field");
		root.addContent(field);

		Element ftype = new Element("ftype");
		ftype.setText(type);
		field.addContent(ftype);

		Element fname = new Element("fname");
		fname.setText(name);
		field.addContent(fname);

		Element fsize = new Element("fsize");
		fsize.setText(size);
		field.addContent(fsize);

		Element fdesc = new Element("fdesc");
		fdesc.setText(desc);
		field.addContent(fdesc);

		Element frpath = new Element("frpath");
		frpath.setText(rpath);
		field.addContent(frpath);

		return true;

	}
}
