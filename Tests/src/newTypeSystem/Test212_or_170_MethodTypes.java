package newTypeSystem;

import java.util.ArrayList;

import java.util.Arrays;
import java.util.Collection;
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
import auxiliaryGrammar.Functions;
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
  MethodType proto=new MethodType(true,Mdf.Capsule,Collections.emptyList(),_t,Collections.emptyList());
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


  @Test
  public void test1(){
    assert dataSet.size()==170:
      dataSet.size();
    //Lent,ImmutableFwd->ImmutablePFwd
    NormType _t=Path.Any().toImmNT();
    MethodType proto=new MethodType(true,Mdf.Capsule,
      Arrays.asList(_t.withMdf(Mdf.Mutable),_t.withMdf(Mdf.ImmutableFwd)),
      _t.withMdf(Mdf.ImmutablePFwd),Collections.emptyList());
    proto=_mVp(proto,1);
    System.out.println(mtToS(proto));

  }
  
  @Test
  public void testMI(){
    int count=0;
    for(MethodType mt:dataSet){
      MethodType base=mBase(mt);
      //mC(mNoFwd(mBase))
      MethodType res=_mI(mNoFwd(base));
      if (mNoFwd(base).equals(base)){continue;}
      if(res==null){continue;}
      count+=1;
    }
    assert count==0;//mI need read to start. read return prevents fwd parameters
    System.out.println(count);
  }
  @Test
  public void testMvpNoFwdSwap(){
    int countA=0;
    int countB=0;    
    for(MethodType mt:dataSet){
      MethodType base=mBase(mt);
      //test mVp(mNoFwd(mBase)) == mNoFwd(mVp(mBase))
      {MethodType v1=_mVp(mNoFwd(base),1);
      MethodType v2=_mVp(base,1); if(v2!=null){v2=mNoFwd(v2);}
      assert v2==null ||v1!=null; //v2 ok implies v1 ok
      //not assert v1==null ||v2!=null;//v1 ok imples v2 ok
      assert v2==null || v1.equals(v2):
        mtToS(v1)+" "+mtToS(v2);
      if(v1!=null && v2!=null){countA+=1;}
      }
      {MethodType v1=_mVp(mNoFwd(base),2);
      MethodType v2=_mVp(base,2); if(v2!=null){v2=mNoFwd(v2);}
      assert v2==null ||v1!=null; //v2 ok implies v1 ok
      //not assert v1==null ||v2!=null;//v1 ok imples v2 ok
      assert v2==null || v1.equals(v2):
        mtToS(v1)+" "+mtToS(v2);
      if(v1!=null && v2!=null){countB+=1;}
      }    
    }
  System.out.println(countA);
  System.out.println(countB);
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
    if(tryOk(_mVp(mt,1),"mVp0",past,map)){return true;}
    if(tryOk(_mVp(mt,2),"mVp1",past,map)){return true;}
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
  public void testMapStr(){
    Set<String>techniques=new HashSet<String>();
    for(MethodType mt:dataSet){
      techniques.addAll(fixMap(mt).values());
      }
    for(String s:techniques){
      System.out.println(s);
      }
    
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
  String mtsToS(Collection<MethodType> mts){
  String[] res={""+mts.size()+"|"};
  mts.forEach(m->{
    res[0]+="\n    "+mtToS(m);
    });
  return res[0];
  }
  String mtToS(MethodType mt){
    return mt.getTs().get(0).getNT().getMdf()+","+
           mt.getTs().get(1).getNT().getMdf()+"->"+
           mt.getReturnType().getNT().getMdf();
    }
  @Test
  public void couldSum(){
    List<Map<MethodType,String>> maps=new ArrayList<>();
    for(MethodType mt:dataSet){
      maps.add(fixMap(mt));
      }
    int ok=0;
    int fail=0;
    int okExt=0;
    int okFwd=0;
    for(int i=0;i<dataSet.size();i++){
      for(int j=0;j<dataSet.size();j++){
        if(i==j){continue;}
        int r=couldSum(i,j,maps);
        if(r==-1){fail+=1;}
        else if(r==i || r==j){ok+=1;}
        else {
          okExt+=1;
          String rs=mapToS(maps.get(r));
          if(rs.contains("Fwd,") ||rs.contains("Fwd->") ){okFwd+=1;}
          else{
            System.out.println("RES");
            System.out.println(mapToS(maps.get(r)));
            System.out.println("m1");
            System.out.println(mapToS(maps.get(i)));
            System.out.println("m2");
            System.out.println(mapToS(maps.get(j)));
          }
          }
      }
    }
    System.out.println("ok "+ok);
    System.out.println("okExt "+okExt);
    System.out.println("okFwd "+okFwd);
    System.out.println("fail "+fail);
  }
  public int couldSum(int m1,int m2,List<Map<MethodType,String>> maps){
    if(m1==m2){return m1;}
    Set<MethodType>m1s=maps.get(m1).keySet();
    Set<MethodType>m2s=maps.get(m2).keySet();
    int m3=-1;
    if(m1s.containsAll(m2s)){return m1;}
    if(m2s.containsAll(m1s)){return m2;}
    for(Map<MethodType,String> mi:maps){m3+=1;
      if(mi.keySet().containsAll(m1s) && mi.keySet().containsAll(m2s)){return m3;}
      }
    return -1;
    }
  

@Test
public void listGenerateAllOfMap(){
  for(MethodType mt:dataSet){
    Map<MethodType, String> all = fixMap(mt);
    List<MethodType> list = AlternativeMethodTypes.types(mt);
    assert new HashSet<>(list).equals(all.keySet()):
      mapToS(all)+"\n"+mtsToS(list);
    }
  }

//False, correctly does not hold for fwd methods @Test
public void listGenerateAtLeastMt(){
  for(MethodType mt:dataSet){
    List<MethodType> list = AlternativeMethodTypes.types(mt);
    assert list.contains(mt):
      mtToS(mt)+"\n"+mtsToS(list);
    }
  }


//and subtype mdf dataset is equal of subtype mdf of all mt in map
//(what method subtype to use for sum/redirect?)
//if mt<mt' then forall mt'i in list(mt'), exists mti in list(mt) s.t. mti<mt'i
  @Test
  public void subtypeDirectImpliesSubtypeList(){
    for(MethodType mt0:dataSet){
      for(MethodType mt1:dataSet){
        if(mt0==mt1){continue;}
        if(!TypeSystem._methMdfTSubtype(mt0,mt1)){continue;}
        assert TypeSystem.methTSubtype(__p, mt0,mt1):
        mtToS(mt0)+"\n"+mtToS(mt1);
        checkOne(mt0,mt1);
        }
      }
    }
  void checkOne(MethodType mt0,MethodType mt1){
    List<MethodType> list0 = AlternativeMethodTypes.types(mt0);
    List<MethodType> list1 = AlternativeMethodTypes.types(mt1);
    for(MethodType mt1i:list1){
      assert list0.stream().anyMatch(mt0i->TypeSystem._methMdfTSubtype(mt0i,mt1i)):
        "\n"+mtToS(mt0)+"\n"+mtToS(mt1)+"\n"+mtToS(mt1i)+"\n"+mtsToS(list0);
      }
    }
  }

/*

mBase
mNoFwd(mBase)
mC(mBase)
mI(mBase)
mC(mNoFwd(mBase))
mI(mRead(mBase))
mImmFwd(mBase)
mNoFwd(mImmFwd(mBase))
mRead(mBase)
mVp(mBase)
mVp(mNoFwd(base))


mVp(mNoFwd(base)) is more "effective" than mNoFwd(mVp(mBase))
but if both ok, you get the same result




*/