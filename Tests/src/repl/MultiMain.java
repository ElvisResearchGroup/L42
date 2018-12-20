package repl;

import java.nio.file.Paths;

public class MultiMain {
  public static void main(String[] args) throws Throwable {
    System.setProperty("user.dir",
        Paths.get(MultiMain.class.getResource("MultiMain.class").getFile().split("!", 2)[0].split(":", 2)[1]).getParent().toString());
    if (args.length > 0) {
      facade.L42.main(args);
    } else {
      repl.ReplMain.main(args);
    }
  }
}