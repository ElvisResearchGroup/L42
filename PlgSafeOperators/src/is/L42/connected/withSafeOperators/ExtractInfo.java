package is.L42.connected.withSafeOperators;

import ast.ExpCore;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ast.Ast.Path;
import ast.ExpCore.*;
import coreVisitors.CloneWithPath;
import coreVisitors.From;
import platformSpecific.javaTranslation.Resources;

public class ExtractInfo {
  static class IsUsed extends CloneWithPath{
    Path target;IsUsed(Path target){this.target=target;}
    Set<Path> whereUsed=new HashSet<>();
    public ExpCore visit(Path s) {
      if(From.fromP(s, getPath()).equals(target)){
        whereUsed.add(getPath());
        }
      return super.visit(s);
      }
  public static Set<Path> of(ClassB cb,Path path){
    IsUsed iu=new IsUsed(path);
    cb.accept(iu);
    return iu.whereUsed;
    }
  }
  //path member is not a nestedclass
  //path is used
  public static void checkBox(ClassB cb,Path path) throws Resources.Error/*NotBox*/{
    List<String> meth=new ArrayList<>();
    for(ClassB.Member m:cb.getMs()){
      m.match(nc->false, mi->meth.add(mi.getS().toString()), mt->meth.add(mt.getMs().toString()));
      }
    Set<Path> used = ExtractInfo.IsUsed.of(cb,Path.outer(0));
    if(meth.isEmpty()&& used.isEmpty() && !cb.isInterface()){return;}
    throw Resources.Error.multiPartStringError("NotBox",
        "UsedBy",""+used,
        "ContainsMethods",""+meth,
        "isInterface",""+cb.isInterface());
  }
}
