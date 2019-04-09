public class RelationParser{

	private String name;
	private int attrNum;

	RelationParser(String name){
		this.name = name;
		attrNum = 0;
	}

	public String parseRelationName(){
		return this.name;
	}

	public int parseAttributeCount(){
		return this.attrNum;
	}

}
