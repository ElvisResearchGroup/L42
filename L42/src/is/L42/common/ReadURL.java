package is.L42.common;

import static is.L42.tools.General.bug;
import static is.L42.tools.General.todo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import is.L42.generated.Core;

public class ReadURL {
  public static Core.L of(String url){
    if(url.startsWith("#$")){url=url.substring(2);}
    Core.L res;try(
      var file=new FileInputStream("localhost"+File.separator+url+".L42"); 
      var in=new ObjectInputStream(file);
      ){res=(Core.L)in.readObject();}
    catch (FileNotFoundException e) {throw todo();}
    catch (IOException e) {throw todo();}
    catch (ClassNotFoundException e) {throw bug();}
    //TODO: check it is really well typed?
    //should well formedness be sufficient? +checking all info=typed?
    return res;
  }
  }
