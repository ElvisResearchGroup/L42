package is.L42.platformSpecific.javaTranslation;

import is.L42.cache.L42Cache;
import is.L42.cache.L42SingletonCache;
import is.L42.generated.P;

public class L42Void extends L42Singleton<L42Void> implements L42Any{
  private L42Void(){}
  public static final L42Any pathInstance=new L42ClassAny(P.pVoid);
  public static final L42Void instance=new L42Void();
  public static final Class<L42Void> _class=L42Void.class;
  public static final L42SingletonCache<L42Void> myCache=new L42SingletonCache<L42Void>("L42Void", L42Void._class);
  @Override public L42Cache<L42Void> myCache(){return myCache;}
  }