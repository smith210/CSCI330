public class AttributeValue{
	private String name;
	private String value;

	public void setName(String name){
		this.name = name;
	}

	public void setValue(String value){
		this.value = value;
	}

	public String parseAttName(){
		return this.name;
	}
	
	public String parseAttValue(){
		return this.value;
	}


}

