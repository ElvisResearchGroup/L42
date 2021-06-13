package is.L42.cache.nativecache;

import is.L42.nativeCode.Flags;

public class FlagsCache extends L42ValueCache<Flags> {
  @Override public String typename() {return "£nativeflags";}
  @Override protected boolean valueCompare(Flags t1, Flags t2){return t1 == t2;}
  }
