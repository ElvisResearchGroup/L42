package is.L42.common;

import static is.L42.tools.General.bug;
import static is.L42.tools.General.todo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import is.L42.generated.Core;
import is.L42.generated.P;
import is.L42.generated.Pos;

public class ReadURL {
  private static final Map<String,Core.L>cache=new HashMap<>();
  public static void resetCache() {cache.clear();}
  public static Core.L of(String url,List<Pos>poss){
    P.coreAny.hashCode();//preloading class P
    boolean hd=url.startsWith("#$");
    if(hd){url=url.substring(2);}
    String fullName="localhost"+File.separator+url+".L42";
    Path fullPath=Constants.localhost.resolve(url+".L42");
    Core.L res=hd?null:cache.get(fullName);
    if(res!=null){return res;}
    try(
      var file=new FileInputStream(fullPath.toFile()); 
      var in=new ObjectInputStream(file);
      ){res=(Core.L)in.readObject();}
    catch(FileNotFoundException e){throw new EndError.UrlNotExistent(poss,url);}
    catch(IOException e){throw new Error(e);}
    catch(ClassNotFoundException e){throw bug();}
    //TODO: check it is really well typed?
    //should well formedness be sufficient? +checking all info=typed?
    cache.put(fullName,res);
    return res;
  }
  }
