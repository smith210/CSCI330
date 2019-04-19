import java.util.*;

public class Tuple{
	private LinkedList<AttributeValue> values;

	public void setTupleValues(LinkedList<AttributeValue> values){
		this.values = values;
	}
	
	public LinkedList<AttributeValue> parseTupleValues(){
		return this.values;
	}

	public String getValue(String attributeName){
		int pointer = 0;
		String retVal = "";
		while(pointer != values.size() || retVal.isEmpty()){
			AttributeValue currVal = values.get(pointer);
			String currName = currVal.parseAttName();

			if(currName.equals(attributeName)){
				retVal = attributeName;
			}

			pointer++;
		}
		return retVal;
	}
}

