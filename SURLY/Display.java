import java.util.*;

public class Display{
	private final String star = "*";
	private final String dash = "-";
	private final String pipe = "|";

	Display(Relation r){
		for(int j = 0; j < r.parseRelationSchema().size(); j++){
			System.out.println(r.parseRelationSchema().get(j).parseAttributeName());
			int width = r.parseRelationSchema().get(j).parseAttributeName().length();
			int tupWidth = r.parseRelationSchema().get(j).parseAttributeLength()
			if(tupWidth > width){
				width = tupWidth;
			}
		}
	}

	public String printLine(String

	public int retrieveSize(LinkedList<Attribute> sch){
		int size = 0;		
		for(int i = 0; i < sch.size(); i++){
			size = size + sch.get(i).parseAttributeLength();
		}
		return size;
	}
}
