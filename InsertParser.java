public class InsertParser{
	private String name;
	private int attrNum;

	InsertParser(String name, int attrNum){
		this.name = name;
		this.attrNum = attrNum;
	}

	public String parseRelationName(){
		return this.name;
	}

	public int parseAttributeCount(){
		return this.attrNum;
	}
}
