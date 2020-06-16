package is.L42.top;

import static is.L42.tools.General.L;
import static is.L42.tools.General.bug;
import static is.L42.tools.General.merge;
import static is.L42.tools.General.range;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

import is.L42.common.CTz;
import is.L42.common.Constants;
import is.L42.common.EndError;
import is.L42.common.Err;
import is.L42.common.PTails;
import is.L42.common.Parse;
import is.L42.common.Program;
import is.L42.constraints.FreshNames;
import is.L42.generated.C;
import is.L42.generated.Core;
import is.L42.generated.Core.T;
import is.L42.generated.Full;
import is.L42.generated.Full.CsP;
import is.L42.translationToJava.Loader;
import is.L42.typeSystem.ProgramTypeSystem;
import is.L42.generated.LDom;
import is.L42.generated.LL;
import is.L42.generated.P;
import is.L42.generated.Pos;
import is.L42.generated.S;
import is.L42.generated.X;
import is.L42.nativeCode.TrustedKind;
import is.L42.platformSpecific.javaTranslation.Resources;
import is.L42.tests.TestCachingCases;
import is.L42.visitors.CloneVisitor;
import is.L42.visitors.CloneVisitorWithProgram;
import is.L42.visitors.PropagatorCollectorVisitor;

public class Init {
  public Init(String s){this(Constants.dummy,Parse.sureProgram(Constants.dummy,s));}
  public final Program p;
  public final Path initialPath;
  public final FreshNames f;
  public Init(Full.L l){this(initialPath(l),Program.flat(l));}
  static Path initialPath(Full.L l){return Paths.get(l.pos().fileName()).getParent();}
  public Init(Path initialPath,Program program){
    assert program!=null;
    this.f=new FreshNames();
    this.initialPath=initialPath;
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
    TestCachingCases.timeNow("begin0");
    return new Init(code).topCache(c);
    }    
  public static Core.L topCache(CachedTop c,Full.L code){
    return new Init(code).topCache(c);
    }
  protected State makeState(){//overriddable for tests
    return new State(f,new ArrayList<>(),0,new ArrayList<>(),new ArrayList<>());
    }
  public Core.L topCache(CachedTop c){
    TestCachingCases.timeNow("begin1");
    Resources.loader=new Loader();
    TestCachingCases.timeNow("begin2");
    LayerE l=LayerL.empty.push(this.p.top,new CTz().releaseMap());
    TestCachingCases.timeNow("begin3");
    R res=c.openClose(new GLOpen(l,makeState()));
    if(res.isErr()){throw res._err;}
    return (Core.L)res._obj;    
    }
  }