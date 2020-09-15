package is.L42.platformSpecific.javaTranslation;

import static is.L42.tools.General.L;

import java.util.List;
import java.util.function.BiConsumer;

import is.L42.cache.L42Cache;
import is.L42.cache.L42CacheMap;
import is.L42.cache.L42SingletonCache;
import is.L42.generated.P;

public class L42£Void extends L42NoFields<L42£Void> implements L42Any{
  private L42£Void(){}
  public static final L42£Void pathInstance;static{
    class L42£VoidFwd extends L42£Void implements L42Fwd{
      @Override public List<Object> os(){return L();}
      @Override public List<BiConsumer<Object, Object>> fs(){return L();}
      @Override public L42ClassAny asPath(){return new L42ClassAny(P.pVoid);}
      };
    pathInstance=new L42£VoidFwd();
    }
  public static final L42£Void instance=new L42£Void();
  public static final Class<L42£Void> _class=L42£Void.class;
  public static final L42SingletonCache<L42£Void> myCache=L42CacheMap.newSingletonCache("L42£Void", L42£Void._class);
  @Override public L42Cache<L42£Void,?> myCache(){return myCache;}
  }