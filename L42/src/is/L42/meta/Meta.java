package is.L42.meta;

import static is.L42.tools.General.todo;
import static is.L42.tools.General.unreachable;
import static is.L42.tools.General.L;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

import is.L42.common.Constants;
import is.L42.common.Parse;
import is.L42.common.Program;
import is.L42.generated.C;
import is.L42.generated.Core;
import is.L42.generated.Core.L;
import is.L42.generated.P;
import is.L42.platformSpecific.javaTranslation.L42Any;
import is.L42.platformSpecific.javaTranslation.L42ClassAny;
import is.L42.platformSpecific.javaTranslation.L42Fwd;
import is.L42.platformSpecific.javaTranslation.L42Library;
import is.L42.platformSpecific.javaTranslation.Resources;
import is.L42.tools.General;
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
  public L42Library simpleRedirect(String innerPath, L42Library l42Lib, L42Any classAny){
    L l=l42Lib.unwrap;
    List<C> cs=unwrapCs(innerPath);
    P path=unwrapPath(classAny);
    //var source=l.cs(cs);
    Program p=Resources.currentP;
    //var dest=p._ofCore(path);
    //TOD: check if source and dest are compatible
    var res=replaceP(cs,path,p.push(Resources.currentC,l)).visitL(l);
    res=res.withCs(cs, nc->{throw unreachable();},nc->null);
    System.out.println(res);
    return new L42Library(p.push(Resources.currentC,res));
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
  }
