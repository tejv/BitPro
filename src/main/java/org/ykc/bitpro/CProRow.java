package org.ykc.bitpro;

import org.jdom2.Element;

public class CProRow {
	private String type;
	private String name;
	private String count;
	private String offset;
	private String totalbytes;
	private String desc;
	private String value;
	private Element simpleElement;


	public CProRow(String type, String name, String count, String offset, String totalbytes, String desc, String value,
			Element simpleElement) {
		this.type = type;
		this.name = name;
		this.count = count;
		this.offset = offset;
		this.totalbytes = totalbytes;
		this.desc = desc;
		this.value = value;
		this.simpleElement = simpleElement;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getOffset() {
		return offset;
	}
	public void setOffset(String offset) {
		this.offset = offset;
	}
	public String getTotalbytes() {
		return totalbytes;
	}
	public void setTotalbytes(String totalbytes) {
		this.totalbytes = totalbytes;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Element getSimpleElement() {
		return simpleElement;
	}
	public void setSimpleElement(Element simpleElement) {
		this.simpleElement = simpleElement;
	}	
}
