package is.L42.platformSpecific.javaTranslation;

import is.L42.flyweight.P;

public interface L42Any {
  public static final L42Any pathInstance=new L42ClassAny(P.pAny);
  }