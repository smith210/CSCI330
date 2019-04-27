import java.util.*;

public class DestroyParser {
  private String relation;

  DestroyParser(String input) {
    Parser p = new Parser(input);
		LinkedList<String> commands = p.parseCommandSet();
    for(int i = 0; i < commands.size(); i++){
      String currCommand = commands.get(i);
      System.out.println(currCommand);
    }
  }

  public String parseRelationName() {
    return this.relation;
  }
}
