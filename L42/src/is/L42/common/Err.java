package is.L42.common;

import static is.L42.tools.General.bug;
import static is.L42.tools.General.todo;
import static is.L42.tools.General.unreachable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.opentest4j.AssertionFailedError;

import is.L42.generated.Full.E;
import is.L42.generated.Pos;

public class Err {
  public static String trimExpression(String e){
    if(e.length()<50){return e;}
    String start=e.substring(0,24);
    String end=e.substring(e.length()-24,e.length());
    return start+"[...]"+end;
    }
  public static String hole="[###]";//not contains \.[]{}()<>*+-=!?^$|
  
  public static String posString(List<Pos>poss){
    String res="";
    for(Pos pos:poss){res+=pos;}
    return res;
    }

  public static boolean strCmp(String complete,String partial){
    complete=complete.trim();
    partial=partial.trim();
    try{assertTrue(Err.strCmpAux(complete,partial));}
    catch(AssertionFailedError afe){
      assertEquals(partial,complete);
      throw unreachable();
      }
    return true;
    }
  private static boolean strCmpAux(String complete,String partial){
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
  ;}public static String redefinedName(Object names){return
  "redefined names: "+names+" are internally redefined"
  ;}public static String varBindingCanNotBe(Object _1){return
  "var bindings can not be "+_1
  ;}public static String deadCodeAfter(Object _1){return
  "dead code after the statement "+_1+" of the block"
  ;}public static String needBlock(Object _1){return
  "expression need to be enclose in block to avoid ambiguities:\n"
  +trimExpression(_1.toString())+"\n"
  ;}public static String needBlockOps(Object _1){return
  "expressions with more then one kind of binary operator need to be disambiguated with parenthesis: "+_1

  ;}public static String nameUsedInCatch(Object _1){return  
  "name "+_1+ " used in catch; it may not be initialized"
  ;}public static String noFullL(Object _1){return    
  "Method body can not contain a full library literal:\n"
  +trimExpression(_1.toString())+"\n"
  ;}public static String capsuleBindingUsedOnce(Object _1){return      
  "capsule name "+_1+" used more then once"
  ;}public static String nameUsedNotInScope(Object _1){return
  "Used name is not in scope: "+_1
  ;}public static String unapdatable(Object _1){return
  "name "+_1+" is not declared as var, thus it can not be updated"
  ;}public static String slashOut(Object _1){return
  "term "+_1+" can only be used inside parameters"
  ;}public static String methodTypeMdfNoFwd(){return  
  "method modifier can not be fwd imm or fwd mut"
  ;}public static String methodTypeNoFwdPar(Object _1){return    
  "unusable fwd parameter given return type "+_1
  ;}public static String methodTypeNoFwdReturn(){return    
  "invalid fwd return type since there is no fwd parameter"
  ;}public static String tsMustBeImm(){return
  "implemented and exception types can not declare a modifier (and are implicitly imm)"
  ;}public static String ifMatchNoT(Object _1){return
  "invalid 'if match': no type selected in "+trimExpression(_1.toString())
  ;}public static String forMatchNoVar(Object _1){return
  "nested name "+_1+" is var; in a 'for' match only top level names can be var"
  ;}public static String singlePrivateState(Object _1){return
  "Only one private state number is allowed; but the following are used "+_1
  ;}public static String notValidC(Object _1){return
  "Error: "+_1+" is not a valid class name"
  ;}public static String malformedAtInDocs(){return
  "Error: malformed @ in docs"
  ;}public static String invalidNumber(Object _1){return
  "Error: "+_1+" is not a valid number"
  ;}public static String invalidMethodName(Object _1){return
  "Error: "+_1+" is not a valid method name"
  ;}public static String repeatedInfo(Object _1){return
  "Error: invalid syntax for Info; repeated information for "+_1
  ;}public static String emptyInfo(Object _1){return
  "Error: invalid syntax for Info; empty information for "+_1
  ;}public static String malformedInfo(Object _1){return
  "Error: invalid syntax for Info:\n"+_1
  ;}public static String malformedCoreHeader(Object info){return
  " Error: Extraneus token "+info==null?"":Err.trimExpression(info.toString())
  +"\nno dots or reuse in core libraries"
  ;}public static String malformedCoreMember(Object info){return
  " Error: Extraneus token "+info==null?"":Err.trimExpression(info.toString())
  +"\nonly methods with type and nested classes in core libraries"
  ;}public static String malformedCoreTs(Object info){return
  " Error: Extraneus token "+info==null?"":Err.trimExpression(info.toString())
  +"\ninvalid implemented type for core library"
  ;}public static String malformedCoreDocs(Object info){return
  " Error: Extraneus token "+info==null?"":Err.trimExpression(info.toString())
  +"\ninvalid docs for core library"
  ;}public static String malformedCoreNC(Object info){return
  " Error: Extraneus token "+info==null?"":Err.trimExpression(info.toString())  
  +"\ninvalid nested class for core library"
 ;}public static String malformedCoreFullL(){return
  "A full library litera is contained in a core library literal"
  ;}public static String malformedCoreMWT(Object info){return
  " Error: Extraneus token "+info==null?"":Err.trimExpression(info.toString())  
  +"\ninvalid method with type for core library"
  ;}public static String stringInterpolation(Object _1, Object _2){return
  "Error: ill formed string interpolation:"+Err.trimExpression(_1.toString())
  +_2
  ;}public static String methodImplementedInInterface(Object _1){return
  "some methods are implemented in an interface:"+_1
  ;}public static String privateNestedNotCore(Object _1){return
  "private nested class "+_1+" is not defined with a core library"
  ;}public static String privateNestedPrivateMember(Object _1){return
  "member "+_1+" inside a private nested class is not private"
  ;}public static String degenerateStatement(Object _1){return
  "The following expression is not a correct statement: "+_1
  ;}public static String nonUniqueNumber(Object _1,Object _2){return
  "The unique number "+_1+" is in the domain of more then one library literal; others are in positions \n"+_2
  ;}public static String moreThenOneMethodOrigin(Object _1,Object _2){return
  "The method "+_1+" is inherited from multiple interfaces, and do not have a single origin. Origins are "+_2
 
  ;}
}
