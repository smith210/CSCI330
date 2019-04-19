public class Attribute{
	private String name;
	private String dataType;
	private int length;

	public void setAttributeName(String name){
		this.name = name;
	}

	public void setDataType(String dataType){
		this.dataType = dataType;
	}	

	public void setLength(int length){
		this.length = length;
	}
	
	public String parseAttributeName(){
		return this.name;
	}

	public String parseAttributeType(){
		return this.dataType;
	}

	public int parseAttributeLength(){
		return this.length;
	}
}

