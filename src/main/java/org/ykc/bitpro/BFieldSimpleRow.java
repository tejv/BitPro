package org.ykc.bitpro;

public class BFieldSimpleRow {
	private String name;
	private String size;
	private String desc;
	private String enums;
	private Integer length;

	public BFieldSimpleRow(String name, String size, String desc, String enums) throws Exception
	{
		Integer len = findLength(size);
		if((name.trim().isEmpty()) || (len > 64) || (len <= 0))
		{
			throw new Exception("Invalid Parameters");
		}

		this.name = name;
		this.size = size;
		this.desc = desc;
		this.enums = enums;
		this.length = len;
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
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getEnums() {
		return enums;
	}
	public void setEnums(String enums) {
		this.enums = enums;
	}

	public Integer getLen(){
		return findLength(this.size);
	}

	static Integer findLength(String sizeStr){
		Integer result = 0;
		try {
			if(sizeStr.contains(":"))
			{
				String[]  a = sizeStr.split(":");
				result = Integer.parseInt(a[0]) - Integer.parseInt(a[1]) ;
				result++;
			}
			else{
					result = Integer.parseInt(sizeStr);
			}
		} catch (NumberFormatException e) {
		}
		return result;
	}
}
