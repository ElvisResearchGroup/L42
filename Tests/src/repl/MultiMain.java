package repl;
public class MultiMain {
  public static void main(String[] args) throws Throwable {
    ClassLoader.getSystemClassLoader().setDefaultAssertionStatus(true);
    if (args.length > 0) {
      facade.L42.main(args);
    } else {
      repl.ReplMain.main(args);
    }
  }
}