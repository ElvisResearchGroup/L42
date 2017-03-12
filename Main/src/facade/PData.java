package facade;

/*
 * Facade class for 
 * exposing the program and other informations to
 * plugins.
 * */
public class PData {
  public PData(programReduction.Program p) {
    this.p = p;
    }

  public programReduction.Program p=null;
  }
