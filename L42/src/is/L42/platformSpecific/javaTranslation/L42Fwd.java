package is.L42.platformSpecific.javaTranslation;

import java.util.List;
import java.util.function.BiConsumer;

public interface L42Fwd{
static L42Void £cAddIfFwd(Object x1, Object res, BiConsumer<Object,Object> action){
  ((L42Fwd)x1).rememberAssign(res,action);
  return L42Void.instance;
  }
static L42Void £cFix(Object a,Object b) { //hoping 'a' is a Fwd
  L42Fwd _a=(L42Fwd)a;
  _a.fix(b);
  return L42Void.instance;
  }
List<Object> os();
List<BiConsumer<Object,Object>> fs();
default void rememberAssign(Object f, BiConsumer<Object,Object> fo){
  os().add(f);
  fs().add(fo);
  }
default void fix(Object b){
  List<Object> os=os();
  List<BiConsumer<Object,Object>> fs=fs();
  assert os.size()==fs.size();
  for(int i=0;i<os.size();i++){fs.get(i).accept(b,os.get(i));}
  }
}