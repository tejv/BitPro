package org.ykc.bitpro;

public class DProRow {
	private String type;
	private String name;
	private String size;
	private String desc;
	private String rPath;

	public DProRow(String type, String name, String size, String desc, String rPath) throws Exception
	{
		if((name.trim().isEmpty()) || (rPath.isEmpty()) || (Utils.parseStringtoNumber(size) == 0))
		{
			throw new Exception("Invalid Parameters");
		}		
		this.type = type;
		this.name = name;
		this.size = size;
		this.desc = desc;
		this.rPath = rPath;
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
	public String getrPath() {
		return rPath;
	}
	public void setrPath(String rPath) {
		this.rPath = rPath;
	}
}
