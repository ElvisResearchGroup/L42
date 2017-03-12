package facade;

public class Configuration {
  public static Reduction reduction=null;
  public static void loadAll() throws InstantiationException, IllegalAccessException, ClassNotFoundException{
    Configuration.reduction=(Reduction) 
        Class.forName("reduction.Facade").newInstance();
    }
}
