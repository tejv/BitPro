package org.ykc.bitpro;

public class DProRow {
	private String type;
	private String name;
	private String size;
	private String desc;
	private String rpath;

	public DProRow(String type, String name, String size, String desc, String rpath) throws Exception
	{
		if((type.trim().isEmpty()) ||(Utils.parseStringtoNumber(size) == 0))
		{
			throw new Exception("Invalid Parameters");
		}		
		this.type = type;
		this.name = name;
		this.size = size;
		this.desc = desc;
		this.rpath = rpath;
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
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getRpath() {
		return rpath;
	}
	public void setRpath(String rpath) {
		this.rpath = rpath;
	}
}
