package is.L42.meta;

import static is.L42.tools.General.L;

import java.util.List;
import java.util.stream.Collectors;

import is.L42.cache.L42CacheMap;
import is.L42.common.Constants;
import is.L42.common.Parse;
import is.L42.generated.C;
import is.L42.generated.S;
import is.L42.generated.X;
import is.L42.nativeCode.TrustedKind;
import is.L42.platformSpecific.javaTranslation.L42NoFields;
import is.L42.visitors.AuxVisitor;

public class L42£Name extends L42NoFields.Eq<L42£Name>{
  private L42£Name(String toString,List<C> cs,S _s,X _x){
    this.toString=toString;this.cs=cs;this._s=_s;this._x=_x;
    if(_x==null){return;}
    if(_s==null){throw new NumberFormatException();}
    if(!_x.equals(X.thisX) && !_s.xs().contains(_x)){throw new NumberFormatException();} 
    }
  private final String toString;
  @Override public String toString(){return toString;}
  final public List<C> cs;
  final public S _s;
  final public X _x;
  private static String cs(List<C> cs){
    if(cs.isEmpty()){return "This";}
    return cs.stream().map(c->c.toString()).collect(Collectors.joining("."));
    }
  private static String norm(List<C> cs,S _s, X _x){
    assert _s!=null|| _x==null
        :"";
    if(_s==null){return cs(cs);}
    if(_x==null){return cs(cs)+"."+_s;}
    return cs(cs)+"."+_s+"."+_x;
    }
  private static L42£Name res(List<C> cs,S _s, X _x){
    String norm=norm(cs,_s,_x);
    return new L42£Name(norm,cs,_s,_x).myNorm();
    }
  public L42£Name onlyCs(){
    if(_s==null){return this;}
    return fromCs(cs);
    }
  public static L42£Name fromCs(List<C> cs){
    if(cs.isEmpty()){return empty;}
    assert cs.stream().noneMatch(c->c.hasUniqueNum());
    return new L42£Name(cs(cs),cs,null,null).myNorm();
    }
  public static L42£Name parse(String s){
    if(s.isEmpty()){return instance;}
    if(s.equals("This")){return empty;}
    var res0 = Parse.ctxPathSelX(Constants.dummy, s);
    if(res0.hasErr()){
      throw new NumberFormatException(s);}
    var res=new AuxVisitor(null).visitPathSelX(res0.res.pathSelX());
    if(res._p()!=null){
      if(res._p().toNCs().n()!=0){throw new NumberFormatException();}
      res=res.with_p(null).withCs(res._p().toNCs().cs());
      }
    if(res.cs().stream().anyMatch(c->c.hasUniqueNum())){throw new NumberFormatException();}
    if(res._s()!=null && res._s().hasUniqueNum()){throw new NumberFormatException();}
    //for(var c:res.cs()) {checkForbidden(c.inner());}//All Cs are uppercase anyway
    if(res._s()!=null){
      for(var x:res._s().xs()){checkForbidden(x.inner());}
      if(!res._s().m().equals("this")){checkForbidden(res._s().m());}
      if(res._x()!=null && !res._x().inner().equals("this")){checkForbidden(res._x().inner());}
      }
    return res(res.cs(),res._s(),res._x());
    }
  private static List<String>forbidden=List.of(
    "fwd",//"reuse","native","mut","imm","lent","read","capsule","class",
    "void","var","catch","interface","if","else","while",
    "for","in","loop","return","error","exception","whoops",
    "method","_","this"
    );
  private static void checkForbidden(String s){
    if(forbidden.contains(s)){
      throw new NumberFormatException();}
    }
  public String x(){return _x==null?"":_x.toString();}
  public String selector(){return _s==null?"":_s.toString();}
  public String path(){return cs(cs);}
  public L42£Name withPath(String cs){return res(parse(cs+".a()").cs,_s,_x);}
  public L42£Name withSelector(String s){
    if(s==null || s.isEmpty()){return onlyCs();}
    try{return res(cs,parse("A."+s+".this")._s,_x);}
    catch(NumberFormatException nfe){return res(cs,parse("A."+s+".this")._s,null);}
    }
  public L42£Name withX(String x){
    if(x==null || x.isEmpty()){return res(cs,_s,null);}
    return res(cs,_s,parse("A.a("+x+")."+x)._x);
    }
  @Override public boolean eq(L42£Name name){return toString.equals(name.toString);}
  public static final Class<L42£Name> _class=L42£Name.class;
  public static final EqCache<L42£Name> myCache=new EqCache<>(TrustedKind.Name);
  @Override public EqCache<L42£Name> myCache(){return myCache;}
  static public L42£Name instance=new L42£Name("<hidden name>",L(),null,null).myNorm();//parse("");//private name
  static public L42£Name empty=new L42£Name("This",L(),null,null).myNorm();//parse("This");
  @Override public L42£Name newInstance(){return instance;}
  }