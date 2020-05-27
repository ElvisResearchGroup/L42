package is.L42.platformSpecific.javaTranslation;

import static is.L42.tools.General.pushL;
import static is.L42.tools.General.todo;

import java.io.Serializable;
import java.util.List;
import java.util.function.BiConsumer;

import is.L42.cache.L42Cachable;
import is.L42.cache.L42Cache;
import is.L42.cache.L42CacheMap;
import is.L42.cache.nativecache.ValueCache;
import is.L42.common.Program;
import is.L42.generated.C;
import is.L42.generated.Core;
import is.L42.generated.P;
import is.L42.tools.General;
import is.L42.generated.Core.L;

public class L42£Library extends L42NoFields.Eq<L42£Library> implements L42Any,Serializable{
  Program originP;
  private Program currentP=null;
  public L unwrap=null;
  private P.NCs localPath=null;
  public P.NCs localPath(){return localPath;}
  L originL;
  C originName=null;
  public static final L42£Library pathInstance;static{
    class L42£LibraryFwd extends L42£Library implements L42Fwd{
      @Override public List<Object> os(){return General.L();}
      @Override public List<BiConsumer<Object, Object>> fs(){return General.L();}
      @Override public L42ClassAny asPath(){return new L42ClassAny(P.pLibrary);}
      @Override public int hashCode(){return 1;}
      };
    pathInstance=new L42£LibraryFwd();
    }
  private L42£Library(){}
  public L42£Library(Program p) {
    originP=p.pop();
    if(p.pTails.hasC()){originName=p.pTails.c();}
    originL=p.topCore();
    }
  @Override public int hashCode(){
    int res=originName==null?0:originName.hashCode();//purposely ignoring originP
    return res*31+originL.poss().toString().hashCode(); 
    }
  @Override public boolean eq(L42£Library l){
    if(this==l){return true;}
    if(this==pathInstance ||l==pathInstance){return false;}
    boolean res=originName==l.originName || (originName!=null && originName.equals(l.originName));
    return res && originP.equals(l.originP) && originL.equals(originL);
    }
  public void currentProgram(Program p){
    assert p!=null;
    if(p==currentP){
      //assert localPath!=null;
      assert unwrap!=null;
      return;
      }
    currentP=p;
    var pathC=currentP.path();
    var pathO=originP.path();
    while(!pathC.isEmpty()&&!pathO.isEmpty()&&pathC.get(0)==pathO.get(0)){
      pathC.remove(0);
      pathO.remove(0);
      }
    assert pathC.isEmpty()||pathO.isEmpty()||!pathC.get(0).equals(pathO.get(0));
    //origin:C1..Cn   {..}
    //current:C1'..Ck'
    //{..}[from Thisk.C1..Cn;currentP]
    localPath=P.of(pathC.size(),pathO);
    unwrap=(Core.L)currentP.from(originL,localPath);
    if(this.originName==null){localPath=null;}
    else{localPath=localPath.withCs(pushL(localPath.cs(),originName));}
    }
  public static final Class<L42£Library> _class = L42£Library.class;
  public static final EqCache<L42£Library> myCache=new EqCache<>("L42£Library");
  @Override public EqCache<L42£Library> myCache(){return myCache;}
  static{L42CacheMap.addCachableType_synchronized(L42£Library.class, myCache);}
  }