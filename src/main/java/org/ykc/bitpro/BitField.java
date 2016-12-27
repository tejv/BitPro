package org.ykc.bitpro;

public class BitField {
	private String name;
	private Integer  size;
	private String desc;
	private String enums;

	public BitField(String name, Integer size, String desc, String enums) throws Exception
	{
		if((name.trim().isEmpty()) || (size > 64))
		{
			throw new Exception("Invalid Parameters");
		}
			
		this.name = name;
		this.size = size;
		this.desc = desc;
		this.enums = enums;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public Integer getSize() {
		return size;
	}
	public void setSize(Integer size) {
		this.size = size;
	}
	public String getEnums() {
		return enums;
	}
	public void setEnums(String enums) {
		this.enums = enums;
	}


}
