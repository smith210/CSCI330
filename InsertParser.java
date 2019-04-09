public class InsertParser{
	private String name;
	private int attrNum;

	InsertParser(String name){
		this.name = name;
	}

	public String parseRelationName(){
		return this.name;
	}

	public int parseAttributeCount(){
		return this.attrNum;
	}
}
