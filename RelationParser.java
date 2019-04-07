public class RelationParser{

	private String name;
	private int attrNum;

	RelationParser(String name, int attrNum){
		this.name = name;
		this.attrNum = attrNum;
		Attribute[] attr = new Attribute[attrNum];
	}

	public String parseRelationName(){
		return this.name;
	}

	public int parseAttributeCount(){
		return this.attrNum;
	}

}
