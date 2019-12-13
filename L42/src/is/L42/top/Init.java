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
import is.L42.visitors.CloneVisitor;
import is.L42.visitors.CloneVisitorWithProgram;

public class Init {
  public Init(String s){this(Parse.sureProgram(Constants.dummy,s));}
  public final Top top;
  public final Program p;
  public Init(Full.L l){this(Program.flat(l));}
  protected Top makeTop(Program program, FreshNames f){
    Path path=Paths.get(program.top.pos().fileName());
    return new Top(f,0,new Loader(),path);//but can be overridden as a testing handler
    }
  public Init(Program program){
    assert program!=null;
    FreshNames f=new FreshNames();
    top=makeTop(program,f);
    Program res=init(program,f);
    assert res.top.wf();
    p=res;
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
    return pStart.top.visitable().accept(new InitVisitor(f,pStart));
    }
}
