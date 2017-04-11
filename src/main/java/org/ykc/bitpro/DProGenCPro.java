package org.ykc.bitpro;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class DProGenCPro {
	public static Boolean run(File dproFile){
		File cproFile = Utils.getFileNewExtension(dproFile, "cpro");
		return create(cproFile, dproFile);
	}

	private static boolean create(File cproFile, File dproFile)
	{
		Document dproDoc = UtilsBPro.getJDOM2Doc(dproFile);
		if((dproDoc == null) ||
		  (!dproDoc.getRootElement().getChild("head").getChildText("dtype").matches("design")))
		{
			return false;
		}

		Document cproDoc = new Document();
		if(populateXML(cproDoc, dproDoc) == false){
			return false;
		}

		return createXMLFile(cproFile, cproDoc);
	}

	private static boolean createXMLFile(File cproFile, Document cproDoc){
		XMLOutputter xmlOutput = new XMLOutputter(Format.getPrettyFormat());
		try {
			xmlOutput.output(cproDoc, new FileOutputStream(cproFile));
		} catch (IOException e) {
			return false;
		}

		return true;
	}


	private static boolean populateXML(Document cproDoc, Document dproDoc){
		Element theRoot = new Element("complex");
		cproDoc.setRootElement(theRoot);

		Element head = new Element("head");
		theRoot.addContent(head);

		Element body = new Element("body");
		theRoot.addContent(body);

		Element cname = new Element("cname");
		cname.setText(dproDoc.getRootElement().getChild("head").getChildText("dname"));
		head.addContent(cname);

		Element type = new Element("ctype");
		type.setText("complex");
		head.addContent(type);

		Element count = new Element("cCount");
		count.setText("0");
		head.addContent(count);		

		Element designElement = dproDoc.getRootElement();
		Integer fields = UtilsBPro.getDProFieldsCount(designElement);
		Integer offset = 0;
		for(int i = 0 ; i < fields; i++ )
		{
			offset += addField(body, i, designElement, offset);
		}

		Element totalbytes = new Element("cTotalBytes");
		totalbytes.setText(Integer.toString(offset));
		head.addContent(totalbytes);

		return true;
	}

	private static int addField(Element body, int i, Element designElement, int offset) {
		Integer count = UtilsBPro.getDProFieldSize(designElement, i);
		Integer size = 1;
		Element x =
		writeCField(body,
				    UtilsBPro.getDProFieldType(designElement, i),
				    UtilsBPro.getDProFieldName(designElement, i),
				    count.toString(),
				    UtilsBPro.getDProFieldDesc(designElement, i),
				    offset);
		File file = new File(UtilsBPro.getDProBasePath(designElement) + "/"+ UtilsBPro.getDProFieldPath(designElement, i));
		if(file.exists()){
			Document doc = UtilsBPro.getJDOM2Doc(file);
			if(doc != null)
			{
				Element core;
				Element jdomElement = doc.getRootElement();
	    		String extension = Utils.getFileExtension(file);
	    		if(extension.equals("cpro"))
	    		{
	    			size = Integer.parseInt(doc.getRootElement().getChild("head").getChildText("cTotalBytes"));
	    			Element cTotalBytes = new Element("cTotalBytes");
	    			cTotalBytes.setText(Integer.toString(size));
	    			x.addContent(cTotalBytes);	
	    			core = new Element("complex");	 
					x.addContent(core);	
		    		Element thisBody = (Element)jdomElement.getChildren("body").get(0).clone(); 
		    		thisBody.detach();
					core.addContent(thisBody);	    			
	    		}
	    		else
	    		{					
	    			size = Integer.parseInt(doc.getRootElement().getChild("head").getChildText("slen")) /8;
	    			Element cTotalBytes = new Element("cTotalBytes");
	    			cTotalBytes.setText(Integer.toString(size));
	    			x.addContent(cTotalBytes);
	    			
	    			Element eCopyElement = (Element)jdomElement.clone();
	    			eCopyElement.detach();
					x.addContent(eCopyElement);	    			
	    		}
			}
		}
		

		
		return size * count;

	}

	private  static Element writeCField(Element root, String type, String name,
			String size, String desc, int offset){

		Element cfield = new Element("cfield");
		root.addContent(cfield);

		Element ctype = new Element("ctype");
		ctype.setText(type);
		cfield.addContent(ctype);

		Element cname = new Element("cname");
		cname.setText(name);
		cfield.addContent(cname);

		Element cCount = new Element("cCount");
		cCount.setText(size);
		cfield.addContent(cCount);

		Element cdesc = new Element("cdesc");
		cdesc.setText(desc);
		cfield.addContent(cdesc);
		
		Element cOffset = new Element("cOffset");
		cOffset.setText(Integer.toString(offset));
		cfield.addContent(cOffset);	
		
		return cfield;
	}
}
