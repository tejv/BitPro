package org.ykc.bitpro;

import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.StatusBar;

import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;


/*
 * Example of input
Table Passive_Cbl_VDO

B31…28	HW Version	0000b…1111b assigned by the VID owner
B27…24	FW Version	0000b…1111b assigned by the VID owner
B23…21	VDO Version	Version Number of the VDO (not this specification Version): Version 1.0 = 000b, 001b…111b  Reserved
B20	Reserved Shall be set to zero.
B19…18	Captive cbl	2= USB Type-C, 3= Captive
B17	Reserved Shall be set to zero.
B16…13	Cable Latency
B12…11	Cable Term_Type	00b = VCONN not required, 01b = VCONN required
B10…9	Maximum VBUS_Voltage 0– 20V, 1– 30V, 2– 40V, 3– 50V,
B8…7	Reserved Shall be set to zero.
B6…5	VBUS Current Handling Capability	1= 3A, 2= 5A
B4…3	Reserved Shall be set to zero.
B2…0	USB SuperSpeed Signaling Support 0= USB 2.0 only, 1= [USB 3.1] Gen1, 2= [USB 3.1] Gen1 and Gen2
 */
public class ParsePDSpec {

	static boolean parse(JFXTextArea txtArea, StatusBar statusBar, TableView<SProRow> tableViewCreate, JFXTextField txtBitProSimpleName, RadioButton rbCreateView32bit)
	{
		Integer reservedCount = 0;
		ObservableList<SProRow> bList = FXCollections.observableArrayList();
		try {
			for (String line : txtArea.getText().split("\\n"))
			{
				line = line.trim();
				if(!line.isEmpty()){
					if(line.startsWith("Name") || line.startsWith("Table")){
						txtBitProSimpleName.setText(line.substring(5).trim().toLowerCase() );
					}
					else if( Character.isDigit(line.charAt(1)) && (line.charAt(0) == 'B') ){
						/* Replace ellipsys with :*/
						String desc = line;
						line = line.replace("\u2026",":");
						line = line.substring(1);
						line = line.replace("Maximum", "max");
						line = line.replace("Minimum", "min");
						line = line.replace("Operating", "op");
						line = line.replace("Voltage", "volt");
						line = line.replace("Current", "cur");
						line = line.replace("Communications", "Comm");
						line = line.replace("Capable", "Cap");
						line = line.replace("Supported", "sup");

						Integer fSize = 1;
						String fName = "Err";
						String[] x = line.trim().split("\\s+");
						if(x[0].contains(":")){
							String[]  a = x[0].split(":");
							fSize = Integer.parseInt(a[0]) - Integer.parseInt(a[1]) ;
							fSize++;
						}
						fName = x[1].toLowerCase();
						if(x.length > 2){
							if("reserved".equals(fName))
							{
								fName = "rsvd";
							}
							else{
							fName += "_" + x[2].toLowerCase();
							}
						}
						SProRow bField;
						try {
							bField = new SProRow(fName, fSize.toString(), desc, "");
						} catch (Exception e) {
							statusBar.setText("Parsing Failed");
							return false;
						}
						bList.add(bField);
					}
				}
			}
		} catch (Exception e) {
			statusBar.setText("Parsing Failed");
			return false;
		}
		tableViewCreate.getItems().clear();
		/* Adding Items in reverse order as data is in reverse order */
		int list_count = bList.size();
		for(int i = list_count - 1; i >= 0; i--)
		{
			if(bList.get(i).getName().equals("rsvd")){
				bList.get(i).setName("rsvd" + reservedCount.toString());
				reservedCount++;
			}
			tableViewCreate.getItems().add(bList.get(i));
		}
		rbCreateView32bit.setSelected(true);
		return true;
	}

}
