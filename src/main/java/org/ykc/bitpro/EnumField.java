package org.ykc.bitpro;

public class EnumField {
	private String name;
	private Long val;
	
	public EnumField(String name, Long val)
	{
		this.name = name;
		this.val = val;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getVal() {
		return val;
	}
	public void setVal(Long val) {
		this.val = val;
	}

}
