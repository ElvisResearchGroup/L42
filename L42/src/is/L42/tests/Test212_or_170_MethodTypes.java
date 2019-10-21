package is.L42.tests;

import java.util.ArrayList;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;

import is.L42.generated.Core;
import is.L42.generated.Core.MH;
import is.L42.generated.Core.T;
import is.L42.common.EndError;
import is.L42.common.Program;
import is.L42.common.TypeManipulation;
import is.L42.generated.Mdf;
import is.L42.generated.MethT;
import is.L42.generated.P;
import is.L42.generated.S;
import is.L42.generated.X;
import static is.L42.typeSystem.AlternativeMethodTypes.*;

import static is.L42.tools.General.L;
import static org.junit.Assert.*;

public class Test212_or_170_MethodTypes {
 //WellFormednessCore.methodType, is ok to have a fwd res with no fwd pars?
public static List<MethT> dataSet=new ArrayList<>();
  static{
    T _t=P.coreAny.withMdf(Mdf.Class);
    MethT proto=new MethT(L(),P.coreAny,L());
    for(Mdf p1:Mdf.values()){
      if(p1.isIn(Mdf.ImmutablePFwd,Mdf.MutablePFwd)){continue;}
      for(Mdf p2:Mdf.values()){
        if(p2.isIn(Mdf.ImmutablePFwd,Mdf.MutablePFwd)){continue;}
        for(Mdf r:Mdf.values()){
          if(r.isIn(Mdf.ImmutablePFwd,Mdf.MutablePFwd)){continue;}
          List<T> tsi=List.of(_t,_t.withMdf(p1),_t.withMdf(p2));
          List<T> tsiSwap=List.of(_t,_t.withMdf(p2),_t.withMdf(p1));
          MethT add1 = proto.withTs(tsi).withT(_t.withMdf(r));
          MethT add2 = proto.withTs(tsiSwap).withT(_t.withMdf(r));
          if(!add1.wf()){continue;}
          assert add2.wf();
          if(dataSet.contains(add2)){continue;}
          dataSet.add(add1);
          }
        }
      }
    }
  static Program p=Program.flat(Core.L.parse("{#norm{}}"));

  @Test
  public void test0(){
    System.out.println(dataSet.size());
    assert dataSet.size()==170;
    for(var e:dataSet){
      System.out.println(mtToS(e));
      }
    }

  @Test
  public void test1(){
    //Lent,ImmutableFwd->ImmutablePFwd
    T t0=P.coreAny.withMdf(Mdf.Class);
    T t=P.coreAny;
    var ts=List.of(t0,t.withMdf(Mdf.Mutable),t.withMdf(Mdf.ImmutableFwd));
    MethT proto=new MethT(ts,t.withMdf(Mdf.ImmutablePFwd),L());
    proto=_mVp(proto,1);
    System.out.println(mtToS(proto));
    }

  @Test
  public void testMI(){
    int count=0;
    for(MethT mt:dataSet){
      MethT base=mBase(mt);
      //mC(mNoFwd(mBase))
      MethT res=_mI(mNoFwd(base));
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
    for(MethT mt:dataSet){
      MethT base=mBase(mt);
      //test mVp(mNoFwd(mBase)) == mNoFwd(mVp(mBase))
      {MethT v1=_mVp(mNoFwd(base),1);
      MethT v2=_mVp(base,1); if(v2!=null){v2=mNoFwd(v2);}
      //assert v2==null ||v1!=null; //v2 ok implies v1 ok
      //not assert v1==null ||v2!=null;//v1 ok imples v2 ok
      assert v2==null || v1.equals(v2):
        mtToS(v1)+" "+mtToS(v2);
      if(v1!=null && v2!=null){countA+=1;}
      }
      {MethT v1=_mVp(mNoFwd(base),2);
      MethT v2=_mVp(base,2); if(v2!=null){v2=mNoFwd(v2);}
      //assert v2==null ||v1!=null; //v2 ok implies v1 ok
      //not assert v1==null ||v2!=null;//v1 ok imples v2 ok
      assert v2==null || v1.equals(v2):
        mtToS(v1)+" "+mtToS(v2);
      if(v1!=null && v2!=null){countB+=1;}
      }
    }
  System.out.println(countA);
  System.out.println(countB);
  }

  boolean tryOk(MethT res,String attempt,String past,Map<MethT,String> map){
    assert res==null || res.wf():
      "";
    if(res!=null && !map.containsKey(res)){map.put(res,attempt+"("+past+")");return true;}
    return false;
  }
  boolean tryEverything(MethT mt,String past,Map<MethT,String> map){
    if(tryOk(mNoFwd(mt),"mNoFwd",past,map)){return true;}
    if(tryOk(_mC(mt),"mC",past,map)){return true;}
    if(tryOk(_mI(mt),"mI",past,map)){return true;}
    if(tryOk(_mVp(mt,1),"mVp0",past,map)){return true;}
    if(tryOk(_mVp(mt,2),"mVp1",past,map)){return true;}
    if(tryOk(_mImmFwd(mt),"mImmFwd",past,map)){return true;}
    if(tryOk(_mRead(mt),"mRead",past,map)){return true;}
    return false;
    }
  Map<MethT,String> fixMap(MethT mt__){
    MethT base=mBase(mt__);
    Map<MethT,String>map=new HashMap<>();
    map.put(base,"mBase");
    while(true){
      int count=0;
      for(MethT t:map.keySet()){
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
    for(MethT mt:dataSet){
      techniques.addAll(fixMap(mt).values());
      }
    for(String s:techniques){
      System.out.println(s);
      }

    }
  @Test
  public void testMapAll(){
    List<Map<MethT,String>> maps=new ArrayList<>();
    for(MethT mt:dataSet){
      maps.add(fixMap(mt));
      }
    maps.stream().sorted((m1,m2)->m1.size()-m2.size()).
    forEach(map->{
      System.out.println(mapToS(map));
      });
  }
  String mapToS(Map<MethT,String> map){
    String[] res={""+map.size()+"|"};
    map.entrySet().stream().sorted(
      (m1,m2)->m1.getValue().length()-m2.getValue().length()
      ).forEach(m->{
      res[0]+="\n    "+mtToS(m.getKey())+" "+m.getValue();
      });
    return res[0];
    }
  String mtsToS(Collection<MethT> mts){
  String[] res={""+mts.size()+"|"};
  mts.forEach(m->{
    res[0]+="\n    "+mtToS(m);
    });
  return res[0];
  }
  String mtToS(MethT mt){
    assert mt!=null;
    assert mt.ts().get(0).mdf()==Mdf.Class;
    return mt.ts().get(1).mdf()+","+mt.ts().get(2).mdf()+"->"+mt.t().mdf();
    }
  @Test
  public void couldSum(){
    List<Map<MethT,String>> maps=new ArrayList<>();
    for(MethT mt:dataSet){maps.add(fixMap(mt));}
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
  public int couldSum(int m1,int m2,List<Map<MethT,String>> maps){
    if(m1==m2){return m1;}
    Set<MethT>m1s=maps.get(m1).keySet();
    Set<MethT>m2s=maps.get(m2).keySet();
    int m3=-1;
    if(m1s.containsAll(m2s)){return m1;}
    if(m2s.containsAll(m1s)){return m2;}
    for(var mi:maps){m3+=1;
      if(mi.keySet().containsAll(m1s) && mi.keySet().containsAll(m2s)){return m3;}
      }
    return -1;
    }
  MethT recLeft(MethT mt){
    assert mt.ts().size()==3;
    List<T>newTs=List.of(mt.ts().get(1),mt.ts().get(0),mt.ts().get(2));
    Mdf mdf=newTs.get(0).mdf();
    if(TypeManipulation.fwd_or_fwdP_in(mdf)){return null;}
    return mt.withTs(newTs);
    }
  @Test
  public void testReciverWorksAsParameter(){
    for(MethT mt:dataSet){
      MethT left=recLeft(mt);
      if(left==null){continue;}
      List<MethT> list = types(mt);
      List<MethT> listLeftBefore = types(left);
      List<MethT> listLeftAfter = L(list,this::recLeft);
      assert listLeftBefore.equals(listLeftAfter):
        "\n"+mtsToS(list)+
        "\n"+mtsToS(listLeftBefore)+
        "\n"+mtsToS(listLeftAfter);
      }
    }

  @Test
  public void testListGenerateAllOfMap(){
    for(MethT mt:dataSet){
      Map<MethT, String> all = fixMap(mt);
      List<MethT> list = types(mt);
      //mi(mVp would be promotable in imm, but is unuseful:
      //the direct promotion to mc is more expressive
      Set<MethT> removeUnuseful=all.entrySet().stream()
        .filter(e->!e.getValue().contains("mI(mVp"))
        .map(e->e.getKey()).collect(Collectors.toSet());
      boolean either=new HashSet<>(list).equals(removeUnuseful)
        ||new HashSet<>(list).equals(all.keySet());
      assert either:
        mapToS(all)+"\n"+mtsToS(list);
      }
    }

//False, correctly does not hold for fwd methods @Test
public void listGenerateAtLeastMt(){
  for(MethT mt:dataSet){
    List<MethT> list = types(mt);
    assert list.contains(mt):
      mtToS(mt)+"\n"+mtsToS(list);
    }
  }
@Test
public void testListGenerateInGoodOrder(){
  for(MethT mt:dataSet){
    List<MethT> list = types(mt);
    for(int i=0;i<list.size();i++){
      Mdf ri=list.get(i).t().mdf();
      for(int j=i;j<list.size();j++){
        Mdf rj=list.get(j).t().mdf();
        if(ri==rj){continue;}
        if(ri==Mdf.Mutable && rj==Mdf.Readable){
        //if(Functions.isSubtype(ri, rj)){
          System.out.println(i+"\n "+mtsToS(list));
          assertTrue(false);
          }
        //assertTrue(!Functions.isSubtype(ri, rj));
        }
      }
    }
  }

//subtype mdf dataset is equal of subtype mdf of all mt in map
//(what method subtype to use for sum/redirect?)
//if mt<mt' then forall mt'i in list(mt'), exists mti in list(mt) s.t. mti<mt'i
  @Test
  public void subtypeDirectImpliesSubtypeList(){
    for(MethT mt0:dataSet){
      for(MethT mt1:dataSet){
        if(mt0==mt1){continue;}
        if(!methMdfTSubtype(mt0,mt1)){continue;}
        assert methTSubtype(p, mt0,mt1):
          mtToS(mt0)+"\n"+mtToS(mt1);
        checkOne(mt0,mt1);
        }
      }
    }
  void checkOne(MethT mt0,MethT mt1){
    List<MethT> list0 = types(mt0);
    List<MethT> list1 = types(mt1);
    for(MethT mt1i:list1){
      assert list0.stream().anyMatch(mt0i->methMdfTSubtype(mt0i,mt1i)):
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