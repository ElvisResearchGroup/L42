package newReduction;

import java.util.List;
import java.util.function.BiConsumer;

import platformSpecific.javaTranslation.Resources;

public interface Fwd{
  static  Resources.Void AddIfFwd(Object x1, Object res, BiConsumer<Object,Object> action){
    if(x1 instanceof Fwd){((Fwd)x1).rememberAssign(res,action);}
    return Resources.Void.instance;
    }
  static Resources.Void Fix(Object a,Object b) { //hoping 'a' is a Fwd
    Fwd _a=(Fwd)a;
    _a.fix(b);
    return Resources.Void.instance;
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
    for(int i=0;i<os.size();i++){
      fs.get(i).accept(b,os.get(i));
      }
    }
  }