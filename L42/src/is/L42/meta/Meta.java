package is.L42.meta;

import static is.L42.tools.General.todo;
import static is.L42.tools.General.unreachable;
import static is.L42.generated.LDom._elem;
import static is.L42.tools.General.L;
import static is.L42.tools.General.mergeU;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import is.L42.common.Constants;
import is.L42.common.Parse;
import is.L42.common.Program;
import is.L42.generated.C;
import is.L42.generated.Core;
import is.L42.generated.Core.L;
import is.L42.generated.Core.L.Info;
import is.L42.generated.Core.L.MWT;
import is.L42.generated.Core.L.NC;
import is.L42.generated.Core.T;
import is.L42.generated.Mdf;
import is.L42.generated.Core.Doc;
import is.L42.generated.P;
import is.L42.generated.Pos;
import is.L42.generated.S;
import is.L42.platformSpecific.javaTranslation.L42Any;
import is.L42.platformSpecific.javaTranslation.L42ClassAny;
import is.L42.platformSpecific.javaTranslation.L42Fwd;
import is.L42.platformSpecific.javaTranslation.L42Library;
import is.L42.platformSpecific.javaTranslation.Resources;
import is.L42.tools.General;
import is.L42.top.Top;
import is.L42.typeSystem.ProgramTypeSystem;
import is.L42.visitors.CloneVisitor;
import is.L42.visitors.CloneVisitorWithProgram;

public class Meta {
  private static P unwrapPath(L42Any classAny){
    L42ClassAny cn;
    if(classAny instanceof L42ClassAny){cn=(L42ClassAny)classAny;}
    else{cn=((L42Fwd)classAny).asPath();}
    return cn.unwrap;
    }
  private static List<C> unwrapCs(String s){
    var csP = Parse.csP(Constants.dummy, s);
    assert !csP.hasErr();
    var res=csP.res;
    if(!res.cs().isEmpty()){return res.cs();}
    var path0=res._p();
    assert path0.isNCs() && path0.toNCs().n()==0;
    return path0.toNCs().cs();
    }
  private static L42Library wrapL(L res){
    var res0=new L42Library(Resources.currentP.push(Resources.currentC,res));
    assert res0.unwrap==null;
    res0.currentProgram(Resources.currentP);
    assert res0.unwrap!=null;
    return res0;
    }
  private static S applyS=S.parse("#apply()");
  public L42Library resource(L42Library that){
    L l=that.unwrap;
    L body=addThis1().visitL(l);
    var mh=new Core.MH(Mdf.Class,L(),P.coreLibrary,applyS,L(),L());
    var meth=new Core.L.MWT(body.poss(),L(),mh,"",body);
    ArrayList<P.NCs> typePs=new ArrayList<>();
    ArrayList<P.NCs> cohePs=new ArrayList<>();
    Top.collectDepsE(Resources.currentP,body,typePs,cohePs);
    var i=l.info();
    var info=new Info(
      i.isTyped(),L(typePs.stream()),L(cohePs.stream()),
      /*watched,usedMethods,hiddenSupertypes,*/L(),L(),L(),//TODO: to fix those 3 fields
      L(),true,false,"",L(),-1
      );
    var res=new Core.L(body.poss(), false, L(), L(meth), L(), info,L());
    return wrapL(res);
    }
  public L42Library simpleSum(L42Library a, L42Library b){
    L res=directSum(a.unwrap,b.unwrap);
    return wrapL(res);
    }
  public L directSum(L a, L b){
    List<MWT> mwts=sumMWTs(a.mwts(),b.mwts());
    List<NC> ncs=sumNCs(a.ncs(),b.ncs());
    Info info=Top.sumInfo(a.info(),b.info());
    boolean interf=a.isInterface() || b.isInterface();
    List<T> ts=mergeU(a.ts(),b.ts());
    List<Pos> pos=mergeU(a.poss(),b.poss());
    List<Doc> docs=mergeU(a.docs(),b.docs());
    return new Core.L(pos, interf, ts, mwts, ncs, info, docs);
    }

  public List<NC> sumNCs(List<NC> a,List<NC> b){
    return L(c->{
      for(var mi:a){
        var other=_elem(b,mi.key());
        if(other==null){c.add(mi);}
        else{c.add(sumNC(mi,other));}
        }
      for(var mi:b){
        var other=_elem(a,mi.key());
        if(other==null){c.add(mi);}
        }
      });    
    }
  public NC sumNC(NC a,NC b){
    return a.withL(directSum(a.l(), b.l()));
    //TODO: sum the docs and the pos
    }
  public List<MWT> sumMWTs(List<MWT> a,List<MWT> b){
    return L(c->{
      for(var mi:a){
        var other=_elem(b,mi.key());
        if(other==null){c.add(mi);}
        else{c.add(sumMWT(mi,other));}
        }
      for(var mi:b){
        var other=_elem(a,mi.key());
        if(other==null){c.add(mi);}
        }
      });
    }
  public MWT sumMWT(MWT a,MWT b){
    if (a._e()!=null && b._e()!=null){throw todo();}
    if(!a.mh().equals(b.mh())){
      System.out.println(a.mh());
      System.out.println(b.mh());
      throw todo();
      }//also check proper subtype
    var body=a._e();
    var nativeUrl=a.nativeUrl();
    if(body==null){body=b._e();nativeUrl=b.nativeUrl();}
    return a.with_e(body).withNativeUrl(nativeUrl);
    //TODO: sum the mh docs and the pos
    }
  public L42Library simpleRedirect(String innerPath, L42Library l42Lib, L42Any classAny){
    L l=l42Lib.unwrap;
    List<C> cs=unwrapCs(innerPath);
    P path=unwrapPath(classAny);
    //var source=l.cs(cs);
    Program p=Resources.currentP;
    //var dest=p._ofCore(path);
    //TODO: check if source and dest are compatible
    var res=replaceP(cs,path,p.push(Resources.currentC,l)).visitL(l);
    res=res.withCs(cs, nc->{throw unreachable();},nc->null);
    return wrapL(res);
    }
  private static CloneVisitorWithProgram replaceP(List<C>cs,P dest,Program pStart){
    return new CloneVisitorWithProgram(pStart){
      @Override public P visitP(P path){
        int nesting=whereFromTop().size();
        P frommed=pStart.from(path,P.of(nesting,L()));
        if(!frommed.isNCs()){return path;}
        var ncs=frommed.toNCs();
        if(ncs.n()!=0){return path;}
        var csFrommed=ncs.cs();
        if(!cs.equals(csFrommed)){return path;}
        if(!dest.isNCs()){return dest;}
        var res=dest.toNCs();
        res=res.withN(res.n()+nesting+1);//because destination is relative to outside pStart.top
        return this.p().minimize(res);
        //return this.p().minimize(P.of(levels, cs));
        }
      };
    }
  private static CloneVisitor addThis1(){
    return new CloneVisitor(){
      int dept=0;
      @Override public Core.L visitL(Core.L l){
        dept+=1;
        var res=super.visitL(l);
        dept-=1;
        return res;
        }
      @Override public P visitP(P path){
        if(!path.isNCs()){return path;}
        var p=path.toNCs();
        if(p.n()<dept){return p;}
        return p.withN(p.n()+1);
        }
      };
    }
  }
