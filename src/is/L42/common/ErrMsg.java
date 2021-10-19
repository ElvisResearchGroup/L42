package is.L42.common;

import static is.L42.tools.General.L;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import is.L42.flyweight.X;
import is.L42.generated.Pos;
import is.L42.generated.S;
import is.L42.generated.Core.MWT;

public class ErrMsg {
  public static String trimExpression(String e){
    if(e.length()<50){return e;}
    String start=e.substring(0,24);
    String end=e.substring(e.length()-24,e.length());
    return start+"[...]"+end;
    }
  public static String options(S attempted, List<MWT> mwts){
    mwts=L(mwts.stream().sorted((m1,m2)->best(attempted,m1,m2)));
    return mwts.stream().map(m->m.key()+"        "+m.with_e(null)).collect(Collectors.joining("\n"));
    }
  public static int best(S hint, MWT m1, MWT m2){
    //consider using some variation of Levenshtein Distance from attempted
    int tierM1=tier(hint.m().toLowerCase(),m1.key().m().toLowerCase());
    int tierM2=tier(hint.m().toLowerCase(),m2.key().m().toLowerCase());
    if(tierM1!=tierM2){return Integer.compare(tierM1, tierM2);}
    var un1=m1.key().hasUniqueNum();
    var un2=m2.key().hasUniqueNum();  
    var n1=m1.key().m();
    var n2=m2.key().m();
    var xs1=m1.key().xs().size();  
    var xs2=m2.key().xs().size();
    if(un1 && !un2){return 1;}
    if(un2 && !un1){return -1;}
    var cmp=n1.compareTo(n2);
    if(cmp!=0){return cmp;}
    if(xs1<xs2){return -1;}
    if(xs1>xs2){return 1;}
    return m1.key().toString().compareTo(m2.key().toString());    
    }
  /*
  with get
  0 all the ones with name exactly get
  1 first all the ones starting with get
  2 then all the ones starting with #get
  3 then all the ones containing get
  4 finally all the others in alphabetic order, with # stuff later on
  */
  public static int tier(String hint,String m){
    if(hint.equals(m)){return 0;}
    if(m.startsWith(hint)){return 1;}
    if(m.startsWith("#"+hint)){return 2;}
    if(m.contains(hint)){return 3;}
    return 4;
    }
  private static int uriPriority(URI uri1,URI uri2){
    if(uri1.equals(uri2)){ return 0; }
    String s1=uri1.toASCIIString();
    String s2=uri2.toASCIIString();
    var s1File=s1.startsWith("file:");
    var s2File=s2.startsWith("file:");
    var s1Adams=s1.startsWith("AdamsTowel/");
    var s2Adams=s2.startsWith("AdamsTowel/");
    if(s1File && !s2File) { return -1;}
    if(!s1File && s2File) { return 1;}
    if(s1Adams && !s2Adams) { return 1;}
    if(!s1Adams && s2Adams) { return -1;}
    return uri1.compareTo(uri2);
    }
  public static String singlePosString(URI uri,List<Pos>poss){
    return uri.toString()+"\n"+poss.stream()
      .map(p->"line " + p.line() + ":" + p.column()+"\n")
      .collect(Collectors.joining());
    }
  @SuppressWarnings("unchecked")
  public static String oPosString(Object o){
    if(!(o instanceof List<?> l) || l.isEmpty()){return o.toString();}
    if(!(l.get(0) instanceof Pos)){return o.toString();}
    return posString((List<Pos>)l);
    }
  public static String posString(List<Pos>poss){
    if(poss==null){return "[no position]";}
    Map<URI,List<Pos>> ps=poss.stream().collect(Collectors.groupingBy(p->p.fileName()));
    return ps.entrySet().stream()
      .sorted((e1,e2)->uriPriority(e1.getKey(),e2.getKey()))
      .map(e->singlePosString(e.getKey(),e.getValue()))
      .collect(Collectors.joining());
    }
   public static String duplicatedNameAny(){return
  "duplicated names: [Any];  'Any' is implicitly present as an implemented interface"
  ;}public static String duplicatedNameThis(){return
  "duplicated names: [this];  'this' is implicitly present as an parameter name"
  ;}public static String duplicatedNameThat(){return
  "duplicated names: [that];  'that' is implicitly passed as first argument"
  ;}public static String duplicatedName(Object names){return
  "duplicated names: "+names
  ;}public static String redefinedName(Object name){return
  "redefined name: "+name+" is internally redefined"
  ;}public static String varBindingCanNotBe(Object _1){return
  "var bindings can not be "+_1
  ;}public static String deadCodeAfter(Object _1){return
  "dead code after the statement "+_1+" of the block"
  ;}public static String deadThrow(Object _1){return
  "The "+_1+" is dead code, since the expression never terminate normally"
  ;}public static String needBlock(Object _1){return
  "expression need to be enclose in block to avoid ambiguities:\n"
  +trimExpression(_1.toString())+"\n"
  ;}public static String needBlockOps(Object _1){return
  "expressions with more then one kind of binary operator need to be disambiguated with parenthesis: "+_1

//  ;}public static String returnMdfInconsistent(Object _1){return  
//  "catch return is using different modifiers for the catched types: "+_1
  ;}public static String nameUsedInCatchOrMatch(Object _1){return  
  "name "+_1+ " used in catch or in forward match; it may not be initialized"
  ;}public static String noFullL(Object _1){return    
  "Method body can not contain a full library literal:\n"
  +trimExpression(_1.toString())+"\n"
  ;}public static String capsuleBindingUsedOnce(Object _1){return      
  "capsule name "+_1+" used more then once"
  ;}public static String nameUsedNotInScope(Object _1){return
  "Used name is not in scope: "+_1
  ;}public static String slashOut(Object _1){return
  "term "+_1+" can only be used inside parameters or method bodies"
  ;}public static String methodTypeMdfNoFwd(){return  
  "method modifier can not be fwd imm or fwd mut"
  ;}public static String methodTypeNoFwdPar(Object _1){return    
  "unusable fwd parameter given return type "+_1
  ;}public static String methodTypeNoFwdReturn(){return    
  "invalid fwd return type since there is no fwd parameter"
  ;}public static String tsMustBeImm(){return
  "implemented and exception types can not declare a modifier (and are implicitly imm)"
  ;}public static String ifMatchNoT(Object _1){return
  "invalid 'if match': no type selected in "+trimExpression(_1.toString())
  ;}public static String forMatchNoVar(Object _1){return
  "nested name "+_1+" is var; in a 'for' match only top level names can be var"
  ;}public static String zeroInterfaceMethod(Object _1){return
  "Zero is not allowed for interface methods, but the following is used "+_1
  ;}public static String zeroPrivateState(Object _1){return
  "Only zero is allowed for private state, but the following is used "+_1
  ;}public static String notValidC(Object _1){return
  "Error: "+_1+" is not a valid class name"
  ;}public static String malformedAtInDocs(){return
  "Error: malformed @ in docs"
  ;}public static String invalidNumber(Object _1){return
  "Error: "+_1+" is not a valid number"
  ;}public static String invalidMethodName(Object _1){return
  "Error: "+_1+" is not a valid method name"
  ;}public static String repeatedInfo(Object _1){return
  "Error: invalid syntax for Info; repeated information for "+_1
  ;}public static String emptyInfo(Object _1){return
  "Error: invalid syntax for Info; empty information for "+_1
  ;}public static String malformedInfo(Object _1){return
  "Error: invalid syntax for Info:\n"+_1
  ;}public static String malformedCoreHeader(Object info){return
  " Error: Extraneus token "+(info==null?"":ErrMsg.trimExpression(info.toString()))
  +"\nno dots or reuse in core libraries"
  ;}public static String malformedCoreMember(Object info){return
  " Error: Extraneus token "+(info==null?"":ErrMsg.trimExpression(info.toString()))
  +"\nonly methods with type and nested classes in core libraries"
  ;}public static String malformedCoreTs(Object info){return
  " Error: Extraneus token "+(info==null?"":ErrMsg.trimExpression(info.toString()))
  +"\ninvalid implemented type for core library"
  ;}public static String malformedCoreDocs(Object info){return
  " Error: Extraneus token "+(info==null?"":ErrMsg.trimExpression(info.toString()))
  +"\ninvalid docs for core library"
  ;}public static String malformedCoreNC(Object info){return
  " Error: Extraneus token "+(info==null?"":ErrMsg.trimExpression(info.toString()))  
  +"\ninvalid nested class for core library"
 ;}public static String malformedCoreFullL(){return
  "A full library litera is contained in a core library literal"
  ;}public static String malformedCoreMWT(Object info,Object names){return
  " Error: Extraneus token "+(info==null?"":ErrMsg.trimExpression(info.toString()))
  +"\ninvalid method with type for core library; invalid methods are:"+names
  ;}public static String stringInterpolation(Object _1, Object _2){return
  "Error: ill formed string interpolation: ["
    +ErrMsg.trimExpression(_1.toString())+"]\n"+_2
  ;}public static String methodImplementedInInterface(Object _1){return
  "some methods are implemented in an interface:"+_1
  ;}public static String privateNestedNotCore(Object _1){return
  "private nested class "+_1+" is not defined with a core library"
  ;}public static String privateNestedPrivateMember(Object _1){return
  "member "+_1+" inside a private nested class is not private"
  ;}public static String degenerateStatement(Object _1){return
  "The following expression is not a correct independent statement: "+_1
  ;}public static String degenerateStatement(Object _1,Object _2){return
  "The following expression is not a correct independent statement: "+_1+
  "\n the code after is recognized as a block, not as a list of parameters: "+_2+
  "\nSome parameter name may be missing."
  ;}public static String zeroNumberForC(Object _1){return
  "The unique number 0 can not be used on class names, but it is used in "+_1
  ;}public static String zeroNumberForNonThis(Object _1){return
  "The unique number 0 can only be used to call methods on 'this', but it is used in "+_1
  ;}public static String nonUniqueNumber(Object _1,Object _2){return
  "The unique numbers "+_1+" are in the domain of more then one library literal; others are in positions \n"+ErrMsg.oPosString(_2)
  ;}public static String moreThenOneMethodOrigin(Object _1,Object _2){return
  "The method "+_1+" is inherited from multiple interfaces, and do not have a single origin. Origins are "+_2
  ;}public static String noMethodOrigin(Object _1,Object _2){return
  "The method "+_1+" is not inherited from any interfaces; inherited methods are "+_2
  ;}public static String notInterfaceImplemented(){return
  "A nested class is implemented but is not an interface"
  ;}public static String thisNumberOutOfScope(Object _1){return
  "Path "+_1+" this number is out of the scope of the program"  
  ;}public static String pathNotExistant(Object _1, Object others){return
  "Path "+Err.pathToS(_1)+" not existant."+others  
  ;}public static String urlNotExistant(Object _1){return
  "Url "+_1+" not existant."
  ;}public static String interfaceImplementsItself(Object ts){return
  "Interfaces can not implement This (self implement); but the current interface implements "+ts
  ;}public static String sealedInterface(Object _1,Object _2){return
  "Implemented interfaces includes a selad interface: "+_1+"; all the implemented interfaces are: "+_2
  ;}public static String importingInterfacePrivateMethod(Object _1){return
  "Implemented interface declare method "+_1+" that is not declared in the heir. Methods with unique numbers need to be manually declared"
  ;}public static String invalidPathInInfo(Object _1){return
  "Invalid path in info: "+_1
  ;}public static String infoPathNotInTyped(Object _1,Object _2){return
  "The following paths listed in "+_1+" are not found in typeDep: "+_2   
  ;}public static String infoWatchedUsedDisjoint(Object _1){return
  "Path "+_1+" is listed as both watched and usedMethods"   
  ;}public static String nestedClassesImplemented(Object _1){return
  "some nested classes are implemented before they are defined: "+_1  
  ;}public static String noDocWithReuseOrDots(Object _1, Object _2){return
  "Docs can not be provided on library literals using "+_1+"; the provided Docs are: "+_2  
  ;}public static String invalidMemberWithReuseOrDots(Object _1,Object _2){return
  "Invalid member kind for a library literals using "+_1+"; the invalid members are: "+_2  
  ;}public static String invalidFieldType(Object _1){return
  "Invalid modifier for field: "+_1
  ;}public static String operatorNotFound(Object _1,List<?> _2){
    if(_2.isEmpty()){return "Operator "+_1+" could not be translated in any method call";}
    return "Operator "+_1+" could be translated in any of the following method calls: "+_2
  ;}public static String inferenceFailNoInfoAbout(Object _1,String hints){return
  "The type of "+_1+" can not be inferred; no informations about it are available"+hints
  ;}public static String contraddictoryInfoAbout(Object _1,Object _2){return
  "The type of "+_1+" can not be inferred; it is required to be a subtype of all the following incompatible types:"+_2
  ;}public static String noCommonSupertypeAmong(Object _1,Object _2){return
  "The type of "+_1+" can not be inferred; none of the following types is a subtype of all the others:"+_2
  ;}public static String reuseShadowsMember(Object _1,Object _2){return
  "Reusing url "+_1+" shadows the following members: "+_2
  ;}public static String noOperatorOnPrimitive(Object _1,Object _2){return
  "Path "+_1+" can not be used as a operator receiver for "+_2
  ;}public static String invalidExpectedTypeForVoidLiteral(Object _1){return
  "\"void\" is only of type \"Void\", but the program requires it to be of type "+_1
  ;}public static Supplier<String> castOnPathMustBeClass(Object _1){return
  ()->"cast on paths must have the class modifier, but "+_1+" is provided"
  ;}public static String subTypeExpected(Object _1,Object _2){return
  "the path "+_1+" is not a subtype of "+_2
  ;}public static String castOnPathOnlyValidIfNotInterface(Object _1){return
  "except for Any, path casts can not be applied on interfaces, but "+_1+" is declared as interface"
  ;}public static String leakedThrow(Object _1){return
  "An "+_1+" is leaked out of this expression"
  ;}public static Supplier<String> leakedExceptionFromMethCall(Object _1){return
  ()->"An exception is leaked out of this expression ("+_1+")"
  ;}public static Supplier<String> methodDoesNotExists(S attempted,List<MWT> others){return
  ()->methodDoesNotExists(attempted,options(attempted,others))
  ;}public static String methodDoesNotExists(Object attempted,Object others){return
  "Method "+attempted+" does not exists. Existing methods are:\n"+others+"\n"
  ;}public static String invalidSetOfMdfs(Object _1){return
  "Invalid set of modifiers for the expected type of a return: "+_1
  ;}public static Supplier<String> methCallResultIncompatibleWithExpected(Object _1,Object _2){return
  ()->"for method "+_1+", method call result type is incompatible with the expected modifier: "+_2
  ;}public static String methCallNoCompatibleMdfParametersSignature(Object _1,Object _2){return
  "Incompatible modifiers for method call.\nFor method "+_1+", method call parameter signature is incompatible with the parameters expected signature: "+_2
  ;}public static String mayLeakUnresolvedFwd(Object _1){return
  "fwd variable "+_1+" may be leaked out using a return"  
  ;}public static String nativeInlinedInvalid(Object _1){return
  "Invalid Java has been inlined in a native body; here is the javac error:\n "+_1
  ;}public static String nativeExceptionNotLazyMessage(Object _1,Object _2){return
  "class of native kind "+_1+" uses as error path "+_2+", that is not of nativeKind LazyMsg"  
  ;}public static String nativeExceptionNotCoherentDep(Object _1,Object _2){return
  "class of native kind "+_1+" uses as error path "+_2+", that is not in coherentDep" 
  ;}public static String nativeReceiverInvalid(Object _1,Object _2){return
  "native body "+_1+" uses unrecognized receiver of kind "+_2
  ;}public static String nativeParameterViolatedConstraint(String kind,Object p,String msg){return
  "native kind "+kind+" requires the parameter "+p+" "+msg
  ;}public static String nativeParameterCountInvalid(Object _1,Object _2,Object _3){return
  "native body "+_1+" has selector "+_2+" but the parameters need to be "+_3
  ;}public static String nativeBodyShapeInvalid(Object mh,Object shape){return
  "native body "+mh+" body is not of shape "+shape
  ;}public static String nativeKindParCountInvalid(Object _1,Object _2,Object _3){return
  "native kind "+_1+" requires "+_2+" nativePar parameters, but "+_3+" are declared"
  ;}public static String nativeKindInvalid(Object _1){return
  "native kind "+_1+" is not recognized"
  ;}public static String nativeTrustedOpInvalid(Object _1){return
  "native trusted operation "+_1+" is not recognized"
  ;}public static String nativeSlaveInvalid(Object _1){return
  "native slave invalid:"+_1
  ;}public static String nativeKindInvalidSelector(Object _1,Object _2){return
  "native kind "+_1+" can not be applied on #$ method "+_2
  ;}public static String nativeKindInvalidNativeKind(Object mh,Object expected,Object actual){return
  "method "+mh+" must be annotated native as "+expected+" but it is "+(actual.toString().isEmpty()?"<empty>":actual)
  ;}public static String nativeBodyInvalidThis(boolean isNative,Object _1,Object mh){return
  (isNative?"native":"")+" body "+_1+" has selector "+mh+"; uses this for more then just accesses a imm/capsule field"
  ;}public static String nativeBodyInvalidThisCount(boolean isNative,Object _1,Object mh){return
  (isNative?"native":"")+" body "+_1+" has selector "+mh+"; uses this more then 1 time"
  ;}public static String nativeBodyInvalidDsFV(MWT mwt,List<X> xs){return
  "native method "+mwt.mh()+" "+mwt.nativeUrl()+", parallel declarations can not refer to each other, but "+xs+" are internally referred"
  ;}public static String nativeBodyDisjointCapsuleFields(MWT mwt,List<S> ss){return
  "native method "+mwt.mh()+" "+mwt.nativeUrl()+", parallel declarations are mutating capsule fields "+ss+" on multiple branches"
  ;}public static String nativeBodyInvalidExceptions(MWT mwt){return
  "native "+mwt.nativeUrl()+" with selector "+mwt.mh().key()+"; this method must have an empty throw exception clause, but it is "+mwt.mh()
  ;}public static String nativeParameterInvalidKind(boolean isNative,Object _1,Object mh,Object sig,Object _3,Object _4){return
  (isNative?"native":"")+" body "+_1+" has selector "+mh+"; required "+sig+" but the type "+_3+" is not of kind "+_4
  ;}public static String nonCoherentNoSetOfFields(Object _1){return
  "The class is not coherent. Candidate factory parameters are: "+_1
  ;}public static String nonCoherentPrivateStateAndPublicAbstractMethods(Object meths,Object pos){return
  "The class is not coherent. It has private state but also abstract methods:"+meths+", the first one is in positions:"+ErrMsg.oPosString(pos)
  ;}public static String nativeFactoryAbsent(Object _1){return
  "The class is not coherent. It is declared with native kind "+_1+" but has no factory method"  
  ;}public static String nonCoherentMethod(Object _1){return
  "The class is not coherent. Method "+_1+" is not part of the abstract state"
  ;}public static String lastStatementDoesNotGuaranteeBlockTermination(){return
  "last statement does not guarantee block termination"
  ;}public static String catchStatementDoesNotGuaranteeBlockTermination(int i){return
  "catch statement "+i+" does not guarantee block termination"
  ;}public static String curlyWithCatchReturn(){return
      "curly block has explicit 'catch return' inside"
  ;}public static String curlyWithNoReturn(){return
  "curly block do not have any return statement"
  ;}public static String nonVarBindingOpUpdate(Object x,Object op){return
  "the operator "+op+" is used on the variable "+x+", that is not declared var"
  ;}public static String fwdVarBindingOpUpdate(Object x,Object op){return
  "the operator "+op+" is used on the variable "+x+", that is still uninitialized"
  ;}public static String errorVarBindingOpUpdate(Object x,Object op){return
  "the operator "+op+" is used on the variable "+x+", but updating it would violate strong error safety"
  ;}public static String bridgeMethodsInFullL(Object _1){return
  "if methods bridging effectful #$ non determinism are present, then all methods need to be annotated with their type. The following bridge methods was present : "+_1
  ;}public static String bridgeNotMutable(Object s,Object mdf){return
  "methods "+s+", bridging effectful #$ non determinism have modifier "+mdf+", but only {mut,lent,capsule} are allowed"
  ;}public static String bridgeViolatedByFactory(Object bi,Object fi){return
  "methods "+bi+", bridging effectful #$ non determinism could be invoked through factory "+fi+", that has no #$"
  ;}public static Supplier<String> nonDetermisticErrorOnlyHD(Object m,Object e){return
  ()->"method "+m+" catches the non deterministic error "+e+". Only #$ methods are allowed to catch non deterministic errors."
  ;}public static String mustHaveCloseState(){return
  "the core library literal must be a close class. Caused by Cache.Now methods"
  ;}public static String mustHaveCloseStateBridge(Object fs,Object bs){return
  "the core library literal must be a close class; it is either a close interface or factory methods "+fs+
  " are present, and methods bridging effectful #$ non determinism "+bs+" are present"
  ;}public static String methSubTypeExpectedRet(Object s, Object _1,Object _2){return
  "Invalid method inheritance for "+s+":\nthe return type "+_1+" is not a subtype of the inherited type "+_2
  ;}public static String methSubTypeExpectedMdf(Object s, Object _1,Object _2){return
  "Invalid method inheritance for "+s+": the modifier "+_1+" is different from the inherited mdf "+_2
  ;}public static String methSubTypeExpectedPars(Object s, Object _1,Object _2){return
  "Invalid method inheritance for "+s+": the parameters "+_1+" are different from the inherited parameters "+_2
  ;}public static String methSubTypeExpectedExc(Object s, Object _1,Object _2){return
  "Invalid method inheritance for "+s+": exception "+_1+" is not a subtype of any exception in "+_2
  ;}public static String invalidDotDotDotLocation(){return
  "Invalid '...' location: no other file is identified"
  ;}public static String dotDotDotSouceNotExistant(Object _1){return
  "There is no source file in the location "+_1+", identified by the '...'"
  ;}public static String dotDotDotSouceNotPortable(Object top,Object bad,Object repeated){return
  "The files under the top level root "+top+" are not portable on different operative systems:\n"+
  "Bad file names: "+bad+"\n"+
  "Repeated (except for case) file names: "+repeated+"\n"
  ;}public static String dotDotDotSouceRepeated(Object _1,Object _2){return
  "The file names "+_1+" and "+_2+" would clash on case insensitive file systems; thus they are not portable on different operative systems"
  ;}public static String dotDotDotSouceHidden(Object _1){return
  "The file name "+_1+" is hidden; this behaviour would not be portable on different operative systems"
  ;}public static String dotDotDotCoreSouceWithMs(){return
  "'...' identifying a core library literal must be of form {...}, but here other members are provided"
  ;}public static String missedTypeDep(Object ps){return
  "Core library literal do not declare all the type dependencies; the following are missing: "+ps
  ;}public static String missedCoheDep(Object ps){return
  "Core library literal do not declare all the coherence dependencies; the following are missing: "+ps
  ;}public static String missedMetaCoheDep(Object ps){return
  "Core library literal do not declare all the meta coherence dependencies; the following are missing: "+ps
  ;}public static String missedWatched(Object ps){return
  "Core library literal do not declare all the watched; the following are missing: "+ps
  ;}public static String missedWatchedNative(Object ps){return
  "Core library literal do not declare all the watched: a native method requires to watch also the following: "+ps
  ;}public static String missedHiddenSupertypes(Object ps){return
  "Core library literal do not declare all the hidden supertypes; the following are missing: "+ps
  ;}public static String missedUsedMethods(Object ps){return
  "Core library literal do not declare all the used methods; the following are missing: "+ps
  ;}public static String missedRefined(Object ss){return
  "Core library literal do not declare all the refined methods; the following are missing: "+ss
  ;}public static String noSelfWatch(){return
  "Core library literal can not watch This" 
  ;}public static String noUniqueWatch(Object _1){return
  "Core library literal can not watch unique numbers "+_1 
  ;}public static String typeDependencyNotCore(Object p){return
  "Typed core library literal can not depend on full core libary "+p
  ;}public static String untypedDependency(Object p,Object ps){return
  "untyped dependencies for "+p+": "+ps
  ;}public static String uncompiledDependency(Object p,Object pi,Object ps){return
  p+" depends from "+pi+", but "+ps+" is still not compiled"  
  ;}public static String mismatchRefine(Object estimate,Object declared){return
  "Core library literal declares the following refined methods :"+declared+", but the actual set of refined methods is "+estimate
  ;}public static String missingImplementedInterface(Object missing){return
  "Core libraries need to report all the transitive implemented interfaces, but "+missing+" is missing"

  ;}public static String parserAmbiguityBlockTerminator(){return
  "Round parenthesis block seams to terminate without a resulting expression."  
 ;}
  
  public static boolean strCmpAux(String cmp1, String cmp2,String stringHole) {
    if(cmp2.isEmpty()){return cmp1.isEmpty();}
    List<String> split = new ArrayList<String>(Arrays.asList(cmp2.split(Pattern.quote(stringHole))));
    for(int i = 0; i < split.size(); i++) { if(split.get(i).length() == 0) split.remove(i--); }
    boolean beginswith = cmp2.startsWith(stringHole);
    boolean endswith = cmp2.endsWith(stringHole);    
    int holes = (beginswith ? 1 : 0) + (endswith ? 1 : 0) + split.size() - 1;    
    //Trivial
    if(holes == 0) { return cmp1.equals(cmp2); }    
    //If we didn't start with a hole, compare everything up to the hole
    if(!beginswith) {
      String startCmp2 = split.get(0);      
      //The thing before the hole is bigger than the entire string!
      if(startCmp2.length() > cmp1.length()) { return false; }      
      String startCmp1 = cmp1.substring(0, startCmp2.length());      
      if(!startCmp1.equals(startCmp2)) { return false; }      
      cmp1 = cmp1.substring(startCmp2.length());
      split.remove(0);
      }    
    //If we didn't start with a hole, compare everything up to the hole
    if(!endswith) {
      String endCmp2 = split.get(split.size() - 1);      
      //The thing after the hole is bigger than the entire (remaining) string!
      if(endCmp2.length() > cmp1.length()) { return false; }      
      String endCmp1 = cmp1.substring(cmp1.length() - endCmp2.length(), cmp1.length());      
      if(!endCmp1.equals(endCmp2)) { return false; }      
      cmp1 = cmp1.substring(0, cmp1.length() - endCmp2.length());
      split.remove(split.size() - 1);
      }    
    //Everything on both sides of the hole matches, therefore the hole matches everything in the center
    //Or, alternatively, the single hole was at the end, and therefore everything but the whole matched the beginning or the end of cmp1
    if(holes == 1) { return true; }    
    //We have a string in between two holes, and all text outside the holes has been removed from both cmp1 and cmp2
    //Check that the middle text exists within cmp1. If it does, all outer text can be considered part of the holes and we have a match
    if(holes == 2) { return cmp1.contains(split.get(0)); }
    if(holes > 2) {
      //Guess how much space the first hole takes up by iteratively searching for the middle text
      int index = 0;
      while((index = cmp1.indexOf(split.get(0))) > -1) {
        cmp1 = cmp1.substring(index);
        if(strCmpAux(cmp1, reconstituteRight(split,stringHole),stringHole)) { return true; }
        cmp1 = cmp1.substring(split.get(0).length());
        }
      }
    return false;
    }
  private static String reconstituteRight(List<String> list,String stringHole) {
    StringBuilder builder = new StringBuilder();
    for(int i = 0; i < list.size(); i++) {
      builder.append(list.get(i));
      builder.append(stringHole);
      }
    return builder.toString();
    }
}
