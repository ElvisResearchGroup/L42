package is.L42.typeSystem;

import static is.L42.tools.General.L;
import static is.L42.tools.General.pushL;
import static is.L42.tools.General.typeFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import is.L42.common.EndError;
import is.L42.common.Program;
import is.L42.generated.C;
import is.L42.generated.Core;
import is.L42.generated.Core.L;
import is.L42.generated.P;
import is.L42.platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.CompilationError;
import is.L42.tools.InductiveSet;
import is.L42.translationToJava.Loader;
import is.L42.visitors.CloneVisitorWithProgram;

public class FlagTyped {
  public static Program flagTyped(Loader l,Program p) throws EndError{
    var typable=typable(p);
    if(typable.isEmpty()){return p;}
    p=p.update(flagL(typable,p));
    //TODO: check the typables are well typed.
    try {l.loadNow(p);}
    catch (CompilationError e) {throw new Error(e);}
    return p;
    }
  private static Core.L flagL(List<List<C>> typable,Program p){
    return new CloneVisitorWithProgram(p){
      @Override public Core.L coreLHandler(Core.L l){
        var where=this.whereFromTop();
        List<C> cs=typeFilter(where.stream(),C.class);
        if(cs.size()!=where.size()){return l;}
        l=super.coreLHandler(l);
        if(!l.info().isTyped() && typable.contains(cs)){
          l=l.withInfo(l.info().withTyped(true));
          }
        return l;
        }
      }.visitL(p.topCore());
    }
  private static List<List<C>> typable(Program p){
    var dom=new ArrayList<List<C>>();
    dom.add(L());
    Core.L l=p.topCore();
    for(var nc:l.ncs()){
      var entry=L(nc.key());
      recDom(dom,entry,nc.l());
      }
    Set<java.util.List<C>> untypable=untypable(p,dom,l);
    return L(dom.stream()
      .filter(e->!untypable.contains(e))
      .sorted((l1,l2)->l1.toString().compareTo(l2.toString())));
    }
  private static Set<List<C>> untypable(Program p,ArrayList<List<C>> dom, L l) {
    return new InductiveSet<Integer,List<C>>(){
      @Override public void rule(Integer k, Consumer<List<C>> set) {
        if(!l.isInterface()){set.accept(L());}
        out:for(var cs:dom){
          var lcs=l.cs(cs);
          if(lcs.info().isTyped()){set.accept(cs);continue out;}
          var frommed=L(lcs.info().typeDep(),pi->p.from(pi,P.of(0, cs)));
          for(var p0:frommed){
            var l0=p._ofCore(p0);
            if(l0==null){set.accept(cs);continue out;}
            var hasUntypedOut=p0.n()>=p0.cs().size() && !l0.info().isTyped();
            if(hasUntypedOut){set.accept(cs);continue out;}
            }
          install(0,cs1->{
            var pcs1=P.of(0,cs1);
            var res=p._ofCore(pcs1);
            if(res!=null && res.info().isTyped()){return;}
            if(frommed.contains(pcs1)){set.accept(cs);}
            });
          }
        }
      }.compute(0);
    }
  private static void recDom(ArrayList<List<C>> dom, List<C> entry,Core.L l){
    dom.add(entry);
    for(var nc:l.ncs()){
      var entry2=pushL(entry,nc.key());
      recDom(dom,entry2,nc.l());      
      }
    }
  }