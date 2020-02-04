package is.L42.platformSpecific.javaTranslation;

import static is.L42.tools.General.pushL;
import static is.L42.tools.General.todo;

import java.io.Serializable;

import is.L42.cache.L42Cachable;
import is.L42.cache.L42Cache;
import is.L42.cache.L42CacheMap;
import is.L42.cache.nativecache.ValueCache;
import is.L42.common.Program;
import is.L42.generated.C;
import is.L42.generated.Core;
import is.L42.generated.P;
import is.L42.generated.Core.L;

public class L42Library implements L42Any,Serializable{
  Program originP;
  private Program currentP=null;
  public L unwrap=null;
  private P.NCs localPath=null;
  public P.NCs localPath(){return localPath;}
  L originL;
  C originName=null;
  public static final L42Any pathInstance=new L42ClassAny(P.pLibrary);
  public L42Library(Program p) {
    originP=p.pop();
    if(p.pTails.hasC()){originName=p.pTails.c();}
    originL=p.topCore();
    }
  public boolean eq(L42Library l){
    boolean res=originName==l.originName || (originName!=null && originName.equals(l.originName));
    return res && originP.equals(l.originP) && originL.equals(originL);
    }
  public void currentProgram(Program p){
    assert p!=null;
    if(p==currentP){
      assert localPath!=null;
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
  public static final Class<L42Library> _class = L42Library.class;
  public static final LibraryCache myCache = new LibraryCache();
  static{L42CacheMap.addCacheableType(L42Library.class, myCache);}
  }
  class LibraryCache extends ValueCache<L42Library>{
    @Override public String typename() {return "L42Library";}
    @Override protected boolean valueCompare(L42Library t1, L42Library t2) {
      return t1.eq(t2);
      }
    }