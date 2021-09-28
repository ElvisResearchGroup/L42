package is.L42.common;

import static is.L42.tools.General.bug;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import is.L42.flyweight.CoreL;
import is.L42.flyweight.P;
import is.L42.generated.Pos;

public class ReadURL {
  private static final Map<String,CoreL>cache=new HashMap<>();
  public static void resetCache() {cache.clear();}
  static CoreL parseSerialized(ObjectInputStream stream){
    try(var in=stream){return (CoreL)in.readObject();}
    catch(IOException e){throw new Error(e);}
    catch(ClassNotFoundException e){throw bug();}    
    }
  public static CoreL of(String url,List<Pos>poss){
    P.coreAny.hashCode();//preloading class P
    boolean hd=url.startsWith("#$");
    if(hd){url=url.substring(2);}
    var info=ToNameUrl.of(url, poss);
    CoreL res=hd?null:cache.get(info.fullName());
    if(res!=null){return res;}
    try(var file=info.fullPath().openStream()){
      var in=new ObjectInputStream(file);
      res=parseSerialized(in);
      }
    catch(FileNotFoundException e){throw new EndError.UrlNotExistent(poss,url);}
    //catch(StreamCorruptedException sce) {res=parseSource(info.fullPath);}
    catch(IOException e){throw new Error(e);}//TODO: on urls one time I've got an error code 500
    //TODO: check it is really well typed?
    //should well formedness be sufficient? +checking all info=typed?
    cache.put(info.fullName(),res);
    return res;
    }
  }
