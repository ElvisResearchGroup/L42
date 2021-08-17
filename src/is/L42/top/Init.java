package is.L42.top;

import static is.L42.generated.Mdf.Capsule;
import static is.L42.generated.Mdf.Lent;
import static is.L42.generated.Mdf.Mutable;
import static is.L42.tools.General.L;
import static is.L42.tools.General.LL;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import is.L42.common.CTz;
import is.L42.common.Constants;
import is.L42.common.EndError;
import is.L42.common.Parse;
import is.L42.common.Program;
import is.L42.constraints.FreshNames;
import is.L42.generated.C;
import is.L42.generated.Core;
import is.L42.generated.Full;
import is.L42.generated.Full.CsP;
import is.L42.generated.LL;
import is.L42.generated.Mdf;
import is.L42.generated.P;
import is.L42.generated.S;
import is.L42.generated.X;
import is.L42.platformSpecific.javaTranslation.Resources;
import is.L42.translationToJava.Loader;
import is.L42.visitors.PropagatorCollectorVisitor;

public class Init {
  public Init(String s){this(Parse.sureProgram(Constants.dummy,s));}
  public Init(Path initialPath,String s){this(Parse.sureProgram(initialPath,s));}
  public final Program p;
  //public final Path initialPath;
  public final FreshNames f;
  public Init(Full.L l){this(Program.flat(l));}
  public Init(Program program){
    assert program!=null;
    this.f=new FreshNames();
    Program res=init(program,f);
    assert res.top.wf();
    collectAllUniqueNs(res,Resources.usedUniqueNs);
    p=res;
    }
  private void collectAllUniqueNs(Program p,HashSet<Integer> c){
    p.top.visitable().accept(new PropagatorCollectorVisitor(){
      @Override public void visitS(S s){
        if(s.hasUniqueNum()){c.add(s.uniqueNum());}
        }
      @Override public void visitC(C s){
        if(s.hasUniqueNum()){c.add(s.uniqueNum());}
        }        
      });
    }
  //in the formalism, it is from L to L, here with p to p,
  //we can parsing initialised programs.
  //this also twist pTails with C so that the C={} have the right content
  public static Program init(Program p,FreshNames f){
    var outer=Paths.get(p.top.pos().fileName()).getParent();
    var fsOk=new CheckFileSystemPortable(outer);
    fsOk.makeError(p.top.poss());
    var ll=initTop(p,f);
    if(p.pTails.isEmpty()){return p.update(ll);}
    var tail=p.update(ll).pop();
    tail=init(tail,f);
    if(!p.pTails.hasC()){return tail.push(ll);}
    return tail.push(p.pTails.c(),ll);
    }
  public static LL initTop(Program pStart,FreshNames f){
    pStart.top.wf();
    return pStart.top.visitable().accept(new InitVisitor(f,pStart));
    }
  public static Core.L topCache(CachedTop c,String code){
    //TestCachingCases.timeNow("begin0");
    return new Init(code).topCache(c);
    }    
  public static Core.L topCache(CachedTop c,Path initialPath,String code){
    Init i;try{i=new Init(initialPath,code);}
    catch(EndError e){//well formedness or the like. Anyway, cache is untouched
      c.fakeRunWithNoChange();
      throw e; 
    }
    finally{Resources.killAllSlaves();}
    return i.topCache(c);
    }
  protected State makeState(){//overriddable for tests
    return new State(f,new ArrayList<>(),0,new ArrayList<>(),new ArrayList<>());
    }
  public Core.L topCache(CachedTop c){
    //TestCachingCases.timeNow("begin1");
    Resources.loader=new Loader();
    //TestCachingCases.timeNow("begin2");
    LayerE l=LayerL.empty().push(this.p.top,new CTz().releaseMap());
    //TestCachingCases.timeNow("begin3");
    R res=c.openClose(new GLOpen(l,makeState()));
    if(res.isErr()){throw res._err;}
    return (Core.L)res._obj;    
    }
  public static P resolveCsP(Program p,CsP s) {
  if(s._p()!=null){return InitVisitor.min(s._p(),p,s.poss());}
  return InitVisitor.min(p.resolve(s.cs(),s.poss()),p,s.poss());
  }
  private static void addMWT(ArrayList<Full.L.M> acc, Full.L.F f, Full.MH mh){
    List<Full.Doc> docs=L();
    if(f._e()==null){docs=f.docs();}
    var mwt=new Full.L.MWT(f.pos(),docs,mh,"",f._e());
    acc.add(mwt);
    }
  private static void expandM(ArrayList<Full.L.M> acc, Full.L.F f){
    if (f._e()!=null){
      var s=f.key().withM("#default#"+f.key().m());
      var mh=new Full.MH(Mdf.Class,L(),f.t(),null,-1,s,L(),false,L());
      addMWT(acc, f, mh);
      f=f.with_e(null);
      }
    if(f.isVar()){
      var s=f.key().withXs(X.thatXs);
      var mh=new Full.MH(Mdf.Mutable,L(),P.fullVoid,null,-1,s,L(f.t()),false,L());
      addMWT(acc, f, mh);
      f=f.withVar(false);      
      }
    Mdf mdf=f.t()._mdf();
    if(mdf==null){mdf=Mdf.Immutable;}
    var isMut=mdf.isIn(Lent,Mutable,Capsule);
    if(isMut && !mdf.isCapsule()){
      var s=f.key().withM("#"+f.key().m());
      var mh=new Full.MH(Mdf.Mutable,L(),f.t(),null,-1,s,L(),false,L());
      addMWT(acc, f, mh);
      }
    if(isMut){f=f.withT(f.t().with_mdf(Mdf.Readable));}
    var mh=new Full.MH(Mdf.Readable,L(),f.t(),null,-1,f.key(),L(),false,L());      
    addMWT(acc,f,mh);
    }
  static List<Full.L.M> exapandMs(List<Full.L.M>ms){
    var res=new ArrayList<Full.L.M>();
    for(var mi:ms){
      if(mi instanceof Full.L.F fi){expandM(res,fi);}
      else{res.add(mi);}
      }
    return LL(res);
    }
  }