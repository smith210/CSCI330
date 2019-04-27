import java.util.*;

public class DeleteParser {
  private String relation;

  DeleteParser(String input) {
    Parser p = new Parser(input);
    LinkedList<String> commands = p.parseCommandSet();
    for(int i = 0; i < commands.size(); i++){
      String currCommand = commands.get(i);
	  if(!currCommand.equals(";")){
		relation = currCommand;
	  }
    }
  }
  public String parseRelationName() {
    return this.relation;
  }
}
