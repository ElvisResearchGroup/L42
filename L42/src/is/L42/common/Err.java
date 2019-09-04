package is.L42.common;

import static is.L42.tools.General.bug;
import static is.L42.tools.General.todo;

import java.util.ArrayList;
import java.util.List;

public class Err {
  public static String trimExpression(String e){
    if(e.length()<50){return e;}
    String start=e.substring(0,24);
    String end=e.substring(e.length()-24,e.length());
    return start+"[...]"+end;
    }
  public static String hole="[###]";//not contains \.[]{}()<>*+-=!?^$|
  public static boolean strCmp(String complete,String partial){
    String tmp=partial;
    int holes=0;
    List<String> ss=new ArrayList<>();
    int index=-1;
    while((index=tmp.indexOf(hole))!=-1){
      holes+=1;
      ss.add(tmp.substring(0,index));
      tmp=tmp.substring(index+hole.length());
      }
    ss.add(tmp);
    if(holes==0){return complete.equals(partial);}
    
    boolean res=complete.startsWith(ss.get(0)) && complete.endsWith(ss.get(ss.size()-1));
    if(!res){return false;}
    if(holes==1){return true;}
    if(holes==2){
      complete=complete.substring(ss.get(0).length(),complete.length()-ss.get(2).length());
      return complete.contains(ss.get(1));
      }
    throw todo();
    }  
    
  //errors messages
    public static String duplicatedNameAny(){return
  "duplicated names: [Any];  'Any' is implicitly present as an implemented interface"
  ;}public static String duplicatedNameThis(){return
  "duplicated names: [this];  'this' is implicitly present as an parameter name"
  ;}public static String duplicatedNameThat(){return
  "duplicated names: [that];  'that' is implicitly passed as first argument"
  ;}public static String duplicatedName(Object names){return
  "duplicated names: "+names
  ;}public static String varBindingCanNotBe(Object _1){return
  "var bindings can not be "+_1
  ;}public static String deadCodeAfter(Object _1){return
  "dead code after the statement "+_1+" of the block"
  ;}public static String needBlock(Object _1){return
  "expression need to be enclose in block to avoid ambiguities:\n"
  +trimExpression(_1.toString())+"\n"
  
  ;}
  
}
