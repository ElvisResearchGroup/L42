package is.L42.nativeCode;

import is.L42.generated.Core.MWT;
import is.L42.translationToJava.J;

public interface Generator{
  void check(boolean allowAbs,MWT mwt,J j);
  void generate(MWT mwt,J j);
  }