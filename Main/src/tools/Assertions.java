package tools;

public final class Assertions {
  /**
   * An userErrorAssert means "this functionality is not being used in the right way"
   * 
   * @param s
   *          specify where is the user error.
   */
  public final static Error userErrorAssert(String s, Exception e) {
    AssertionError e1 = new AssertionError("-UserErrorAssert:: " + s);
    e1.initCause(e);
    throw e1;
  }
  
  public final static Error userErrorAssert(String s) {
    AssertionError e1 = new AssertionError("-UserErrorAssert:: " + s);
    throw e1;
  }
  
  /**
   * A reflectionError means that an assumption about the shape of some class was wrong. It is more specific respect to userErrorAssert.
   * 
   * @param s
   *          extra informations
   * @param e
   *          the cause of this event
   */
  public final static Error reflectionError(String s, Exception e) {
    AssertionError e1 = new AssertionError("-ReflectionError:: " + s);
    e1.initCause(e);
    throw e1;
  }
  
  /**
   * A systemError means that something very bad is happened in the system,
   * so it is no more longer able to write files,
   * allocate memory or whatever else.
   * 
   * @param s
   * @param e
   *          the cause of this event
   */
  public final static Error systemError(String s, Throwable e) {
    AssertionError e1 = new AssertionError("-SystemError:: " + s);
    e1.initCause(e);
    throw e1;
  }
  
  /**
   * iDoNotKnowJavaError means that some assumption the coder
   * is doing about the behavior of the JVM or the java language
   * was wrong.
   * 
   * @param s
   *          extra informations
   * @param e
   *          the cause of this event
   */
  public final static Error iDoNotKnowJavaError(String s, Throwable e) {
    AssertionError e1 = new AssertionError("-IDoNotKnowJavaError:: " + s);
    e1.initCause(e);
    throw e1;
  }
  
  /**
   * A codeNotReachable mark in a point means that
   * programmer suppose the execution will never reach that point.
   * 
   * @param s
   */
  public final static Error codeNotReachable(String s) {
    throw new AssertionError("-Code should not be reachable:: " + s);
  }
  public final static Error codeNotReachable(String s,Throwable cause) {
    throw new AssertionError("-Code should not be reachable:: " + s,cause);
  }
  
  
  /**
   * A codeNotReachable mark in a point means that
   * programmer suppose the execution will never reach that point.
   */
  public final static Error codeNotReachable() {
    throw new AssertionError("-Code should not be reachable--");
  }
  
}