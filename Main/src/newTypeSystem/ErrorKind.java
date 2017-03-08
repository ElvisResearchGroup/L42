package newTypeSystem;

public enum ErrorKind {
  SelectorNotFound(false),
  LibraryNotCoherent(false),
  BindingUnavailable(true),
  NotSubtypeClass(false),
  NotSubtypeMdf(true),
  MethodLeaksReturns(false),
  MethodLeaksExceptions(false),
  InvalidImplements(false),
  SkeletalTypeCircularDef(false),
  NoMostGeneralMdf(false),//??
  AttemptReturnFwd(false),
  PluginNotFound(false),
  PluginTypeNotPresent(false);
  final boolean needContext;
  ErrorKind(boolean needContext){this.needContext=needContext;}
  }
