package newTypeSystem;

import java.util.ArrayList;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import ast.Ast.Mdf;
import ast.Ast.MethodType;
import ast.Ast.Path;
import ast.Ast.Type;
import auxiliaryGrammar.WellFormednessCore;
import programReduction.Program;
import programReduction.TestProgram;
import ast.Ast.NormType;
import static newTypeSystem.AlternativeMethodTypes.*;

public class Test212_or_170_MethodTypes {
 //WellFormednessCore.methodType, is ok to have a fwd res with no fwd pars?
public static List<MethodType> dataSet=new ArrayList<>();
static{
  NormType _t=Path.Any().toImmNT();
  MethodType proto=new MethodType(true,Mdf.Capsule,Collections.emptyList(),Path.Any().toImmNT(),Collections.emptyList());
  for(Mdf p1:Mdf.values()){
    if(p1==Mdf.ImmutablePFwd ||p1==Mdf.MutablePFwd){continue;}
    for(Mdf p2:Mdf.values()){
      if(p2==Mdf.ImmutablePFwd ||p2==Mdf.MutablePFwd){continue;}
      for(Mdf r:Mdf.values()){
        if(r==Mdf.ImmutablePFwd ||r==Mdf.MutablePFwd){continue;}
        List<Type> tsi=Arrays.asList(_t.withMdf(p1),_t.withMdf(p2));
        List<Type> tsiSwap=Arrays.asList(_t.withMdf(p2),_t.withMdf(p1));
        MethodType add1 = proto.withTs(tsi).withReturnType(_t.withMdf(r));
        MethodType add2 = proto.withTs(tsiSwap).withReturnType(_t.withMdf(r));
        if(!WellFormednessCore.methodTypeWellFormed(add1)){continue;}
        if(dataSet.contains(add2)){continue;}
        dataSet.add(add1);
        }
      }
    }
  }
static Program __p=TestProgram.p("{}");
public static boolean methTSubtype(MethodType mSub,MethodType mSuper){
  if (!newTypeSystem.TypeSystem.subtype(__p,mSub.getReturnType().getNT(),mSuper.getReturnType().getNT())){
    return false;
  }
  if(mSub.getTs().size()!=mSuper.getTs().size()){return false;}
  {int i=-1;for(Type tSub:mSub.getTs()){i+=1;Type tSuper=mSuper.getTs().get(i);
    if (!newTypeSystem.TypeSystem.subtype(__p,tSuper.getNT(),tSub.getNT())){
      return false;
      }    
  }}
  //TODO: add mdf and exceptions
  return true;
  }

  @Test
  public void test1(){
    assert dataSet.size()==170:
      dataSet.size();
  }
  //@Test
  public void testMi(){
    int count=0;    
    for(MethodType mt:dataSet){
      MethodType base=mBase(mt);
      //test noFwd(mImmFwd(base))=mI(mRead(base))
      MethodType mImmFwdBase = _mImmFwd(base);
      if(mImmFwdBase==null){continue;}
      MethodType noFwdMImmFwdBase = mNoFwd(mImmFwdBase);
      MethodType mReadBase=_mRead(base);
      if(mReadBase==null){continue;}
      MethodType mIMReadBase = _mI(mReadBase);
      if(mIMReadBase==null){continue;}
      count+=1;
      assert methTSubtype(noFwdMImmFwdBase, mIMReadBase):
      "";
    }
  System.out.println(count);
  }

  boolean tryOk(MethodType res,String attempt,String past,Map<MethodType,String> map){
    assert res==null || WellFormednessCore.methodTypeWellFormed(res):
      "";
    if(res!=null && !map.containsKey(res)){map.put(res,attempt+"("+past+")");return true;}
    return false;
  }
  boolean tryEverything(MethodType mt,String past,Map<MethodType,String> map){
    if(tryOk(mNoFwd(mt),"mNoFwd",past,map)){return true;}
    if(tryOk(_mC(mt),"mC",past,map)){return true;}
    if(tryOk(_mI(mt),"mI",past,map)){return true;}
    if(tryOk(_mVp(mt,0),"mVp0",past,map)){return true;}
    if(tryOk(_mVp(mt,1),"mVp1",past,map)){return true;}
    if(tryOk(_mImmFwd(mt),"mImmFwd",past,map)){return true;}
    if(tryOk(_mRead(mt),"mRead",past,map)){return true;}
    return false;
    }
  Map<MethodType,String> fixMap(MethodType mt__){
    MethodType base=mBase(mt__);
    Map<MethodType,String>map=new HashMap<>();
    map.put(base,"mBase");
    while(true){
      int count=0;
      for(MethodType t:map.keySet()){
        if(tryEverything(t, map.get(t), map)){
          count+=1;
          break;
          }
        }
      if(count==0){break;}
      }
    return map;
    }
  @Test
  public void testMapAll(){
    List<Map<MethodType,String>> maps=new ArrayList<>();
    for(MethodType mt:dataSet){
      maps.add(fixMap(mt));
      }
    maps.stream().sorted((m1,m2)->m1.size()-m2.size()).
    forEach(map->{
      System.out.println(mapToS(map));
      });
  }
  String mapToS(Map<MethodType,String> map){
    String[] res={""+map.size()+"|"};
    map.entrySet().stream().sorted(
      (m1,m2)->m1.getValue().length()-m2.getValue().length()
      ).forEach(m->{
      res[0]+="\n    "+mtToS(m.getKey())+" "+m.getValue();
      });
    return res[0];
    }
  String mtToS(MethodType mt){
    return mt.getTs().get(0).getNT().getMdf()+","+
           mt.getTs().get(1).getNT().getMdf()+"->"+
           mt.getReturnType().getNT().getMdf();
    }
}

/*





*/