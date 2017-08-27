package newReduction;

import java.util.List;
import java.util.function.BiConsumer;

public interface Fwd{
  static  void addIfFwd(Object x1, Object res, BiConsumer<Object,Object> action){
    if(x1 instanceof Fwd){((Fwd)x1).rememberAssign(res,action);}
  }
  List<Object> os();
  List<BiConsumer<Object,Object>> fs();
  default void rememberAssign(Object f, BiConsumer<Object,Object> fo){
    os().add(f);
    fs().add(fo);
    }
  default void fix(){
    List<Object> os=os();
    List<BiConsumer<Object,Object>> fs=fs();
    assert os.size()==fs.size();
    for(int i=0;i<os.size();i++){
      fs.get(i).accept(this,os.get(i));
      }
    }
  }