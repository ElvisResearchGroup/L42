package tools;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import ast.Util;
import tools.LambdaExceptionUtil.*;

// Some collection utilities
public abstract class Utils {
  @SuppressWarnings("unchecked")
  public static <T> T cast(Object y) { return (T)y; }

  // forces y to be infered as having type T
  public static <T> T infer(T y) { return y; }
}
