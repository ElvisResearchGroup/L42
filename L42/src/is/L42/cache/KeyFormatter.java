package is.L42.cache;

import static is.L42.tools.General.L;
import static is.L42.tools.General.bug;
import static is.L42.tools.General.range;
import static is.L42.tools.General.toOneOr;
import static is.L42.tools.General.toOneOrBug;
import static is.L42.tools.General.todo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import is.L42.common.Program;
import is.L42.generated.C;
import is.L42.generated.Core;
import is.L42.generated.P;
import is.L42.generated.X;
import is.L42.nativeCode.TrustedKind;
import is.L42.platformSpecific.javaTranslation.Resources;

interface FormatKind{
  P path();
  String format(boolean isInterface,int size);
  String specialS();
  }
class Format42 implements FormatKind{
  L42Cache<?> cache;
  P.NCs path;
  int lineN;
  KeyFormatter f;
  String miniPath(){
    if(path.cs().isEmpty()){return path.toString();}
    C first=path.cs().get(0);
    Program pp=f.p;
    for(@SuppressWarnings("unused") int i:range(path.n())){
      if(pp.topCore().inDom(first)){return path.toString();}
      pp=pp.pop();
      }
    return path.cs().stream().map(c->c.toString()).collect(Collectors.joining("."));
    }
  public String format(boolean isInterface,int size){
    assert f.k.lines().length>lineN;
    String name=isInterface?miniPath():"";
    var l=f.p._ofCore(path);
    if(!l.info().nativeKind().isEmpty()){
      assert isInterface;
      String res=f.formatDispatch(path,path,
        null,null,f.k.lines()[lineN][1],false,size+name.length());
      if(res.startsWith("\"")||res.startsWith("[")){return name+res;}
      if(res.startsWith("-")){return name+"\""+res+"\"";}
      return res+name; 
      }
    assert l!=null:path;
    List<X> xs=l.mwts().stream()
      .filter(m->m._e()==null && m.mh().mdf().isClass())
      .map(m->m.key().xs())
      .distinct()
      .reduce(toOneOrBug()).get();
    Object[] fields=f.k.lines()[lineN];
    int estimateSize=xs.size()*7+name.length();    
    StringBuilder res=new StringBuilder(name+"(");
    if(!xs.isEmpty() && !xs.get(0).equals(X.thatX)){res.append(xs.get(0)+"=");}
    for(int i:range(1,fields.length)){
      res.append(f.formatDispatch(null,path,l,xs.get(i-1),fields[i],cache.rawFieldCache(i-1)==null,estimateSize));
      if(i+1!=fields.length){res.append(", "+xs.get(i)+"=");}    
      }
    res.append(")");
    return res.toString();
    }
  private C fromS(String s){
    int colon=s.indexOf(":");
    if(colon==-1){return new C(s,-1);}
    String a=s.substring(0,colon);
    String b=s.substring(colon+2);
    return new C(a,Integer.parseInt(b));
    }
  public Format42(P.NCs hint,KeyFormatter f, int lineN) {
    this.cache=(L42Cache<?>)f.k.lines()[lineN][0];
    this.lineN=lineN;
    this.f=f;
    if(hint!=null){this.path=hint;assert null!=f.p._ofCore(path);return;}
    var names=(String[])cache.typename();
    List<C> cs=L(range(names.length),(c,i)->c.add(fromS(names[i])));
    this.path=f.p.minimize(P.of(f.p.dept(), cs));
    assert null!=f.p._ofCore(path):cs;
    }
  @Override public String specialS(){return null;}
  @Override public P path(){return path;}
  }
class FormatTrusted implements FormatKind{
  P hint;KeyFormatter f; int lineN;
  public FormatTrusted(P hint,KeyFormatter f, int lineN) {
    this.hint=hint;this.f=f;this.lineN=lineN;
    }
  @Override public P path(){return hint;}
  @Override public String format(boolean isInterface, int size){return specialS();}
  @Override public String specialS(){
    String res= f.k.lines()[lineN][1].toString();
    if(!res.isEmpty() && res.matches("[0-9]+")){return res;}
    res.replace("\"", "\\dq").replace("\n","\\nl");
    return "\""+res+"\"";
    }
  }
class FormatVector extends Format42{
  public FormatVector(P.NCs hint,KeyFormatter f, int lineN){
    super(hint,f,lineN);
    }
  @Override public String format(boolean isInterface, int size){
    assert f.k.lines().length>lineN;
    String name=isInterface?miniPath():"";
    Object[] fields=f.k.lines()[lineN];
    int estimateSize=fields.length*3+name.length();    
    StringBuilder res=new StringBuilder(name+"[");
    for(int i=1;i<fields.length;i+=2){
      res.append(f.formatDispatch(null,path,null,null,fields[i],cache.rawFieldCache(i-1)==null,estimateSize));
      if(i+2!=fields.length){res.append("; ");}    
      }
    res.append("]");
    return res.toString();
    }
  }
class KeyFormatter{
  KeyNorm2D k;
  Program p=Resources.currentP;
  HashMap<Integer,String> expanded=new HashMap<>();
  HashSet<Integer> visited= new HashSet<>();
 
  public FormatKind newFormatKind(P.NCs hint,int lineN){
    var cache=(L42Cache<?>)k.lines()[lineN][0];
    if(cache.typename() == TrustedKind.Vector){return new FormatVector(hint,this,lineN);}
    if(cache.typename() instanceof TrustedKind){return new FormatTrusted(hint,this,lineN);}
    if(cache.typename() instanceof String[]){return new Format42(hint,this,lineN);}
    assert false:cache.typename();
    throw bug();
    }
  public String varName(P path){
    String name=path.toString();
    int i=name.lastIndexOf(".");
    if(i!=-1){name=name.substring(i+1);}
    String first=name.substring(0,1).toLowerCase();
    return first+name.substring(1);
    }

  private String formatDispatchId(int id,P.NCs hint,Object o, boolean isInterface, int size) {
    FormatKind k=newFormatKind(hint,id);
    String special=k.specialS();
    if(special!=null){return special;}
    String name=varName(k.path())+id;
    if(visited.contains(id)){return name;}
    if(size<50){return k.format(isInterface,size);}
    visited.add(id);
    expanded.put(id,name+"="+k.format(true,size));
    return name;    
    }
  public String formatDispatch(P.NCs hint,P.NCs source,Core.L l,X x, Object o, boolean isInterface, int size) {
    int id=((KeyVarID)o).value();
    var cache=(L42Cache<?>)k.lines()[id][0];
    boolean isNative=cache.typename()instanceof TrustedKind;
    if(hint==null && isNative){hint=_computeHint(source, l, x);}
    return formatDispatchId(id,hint,0,isInterface,size);
    }
  private P.NCs _computeHint(P.NCs source, Core.L l, X x){
    if(l==null){
      l=p._ofCore(source);
      assert l.info().nativeKind().equals("Vector");
      P res= p.from(l.info().nativePar().get(0),source);
      if(res.isNCs()){return res.toNCs();}
      return null;
      }
    List<P> types=L(l.mwts(),(c,m)->{
      if(m._e()!=null || !m.mh().mdf().isClass()){return;}
      int index=m.key().xs().indexOf(x);
      assert index!=-1;
      P pi=p.from(m.mh().pars().get(index).p(),source);
      if(p._ofCore(pi).isInterface()){return;}
      c.add(pi);
      });
    assert types.stream().distinct().count()==1;
    P p=types.get(0);
    if(p.isNCs()){return p.toNCs();}
    return null;
    }
  public static String start(KeyNorm2D k){
    KeyFormatter f=new KeyFormatter();
    f.k=k;
    FormatKind first=f.newFormatKind(null,0);
    String res=first.format(true,0);
    if(f.expanded.isEmpty()){return res;}
    res+="\n"+f.expanded.entrySet().stream()
      .sorted((e1,e2)->e1.getKey()-e2.getKey())
      .map(e->e.getValue())
      .collect(Collectors.joining("\n"));
    if(f.expanded.keySet().contains(0)){
      res=f.varName(first.path())+"0="+res;
      }
    return res;    
    }
  }