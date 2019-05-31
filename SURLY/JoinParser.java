import java.util.*;

public class JoinParser{
	private String relationAName;
	private String relationBName;
	private boolean validStatement;
	private Parser p;

	JoinParser(Parser p){
		this.p = p;
		addInfo();
	}
	private int hasOn(LinkedList<String> info){
		int i = 0;
		while(i != info.size() && !info.get(i).equals("ON")){
			i++;
		}
		if(i == info.size()){
			i = -1;
			validStatement = false;
		}
		return i;
	}

	private void addInfo(){
		LinkedList<String> content = p.parseCommandSet();
		int onPtr = hasOn(content);
		if(onPtr != -1){
			relationAName = content.get(3);
			relationBName = content.get(4);
			
		}
	
	}

	public boolean isValid(){ return validStatement; }
	public String getRelationAName(){ return relationAName; }
	public String getRelationBName(){ return relationBName; }

}
