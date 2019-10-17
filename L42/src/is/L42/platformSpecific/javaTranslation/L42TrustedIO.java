package is.L42.platformSpecific.javaTranslation;

import static is.L42.tools.General.todo;
import static is.L42.tools.General.unreachable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import is.L42.generated.Core;

public class L42TrustedIO {
  public L42Void strDebug(String s){
    Resources.out(s);
    return L42Void.instance;
    }
  public L42Void deployLibrary(String s, L42Library l42Lib){
    Core.L l=l42Lib.unwrap;
    //TODO: check it is self contained
    //TODO: type it
    try(
      var file=new FileOutputStream("localhost"+File.separator+s+".L42"); 
      var out=new ObjectOutputStream(file);
      ){
      out.writeObject(l);
      }
    catch (FileNotFoundException e) {throw unreachable();}
    catch (IOException e) {throw todo();}
    //TODO: should throw a non deterministic exception as for 
    //memory overflow/stack overflow. It should be error S,
    //the same type of the String
    return L42Void.instance;
    }
  }
