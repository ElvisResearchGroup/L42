package newTypeSystem;

public enum ErrorKind {
  //SelectorNotFound(false),
  LibraryNotCoherent(false),
  BindingUnavailable(true),
  NotSubtypeClass(false),
  NotSubtypeMdf(true),
  MethodLeaksReturns(false),
  MethodLeaksExceptions(false),
  InvalidImplements(false),
  //no, will be throw as error from Norm code 
  //SkeletalTypeCircularDef(false),
  NoMostGeneralMdf(false),//??
  AttemptReturnFwd(false),
  PluginNotFound(false),
  PluginTypeNotPresent(false),
  UnsafeCatchAny(false),
  ReceiverInvalidMdfForMs(false),
  ClassMethCalledOnNonClass(false),
  NonClassMethCalledOnClass(false);
  final boolean needContext;
  ErrorKind(boolean needContext){this.needContext=needContext;}
  }
