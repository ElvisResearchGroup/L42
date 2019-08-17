package is.L42.visitors;
import java.util.List;
import is.L42.generated.*;

public class HasMultilinePart extends CollectorVisitor implements java.util.function.Function<Visitable<?>,Boolean>{
  boolean acc=false;
  @Override public void visitDoc(Core.Doc doc){
    for(var s:doc.texts()){
      if(s.contains("\n")){acc|=true;}
      }
    super.visitDoc(doc);
    }    
  @Override public void visitDoc(Full.Doc doc){
    for(var s:doc.texts()){
      if(s.contains("\n")){acc|=true;}
      }
    super.visitDoc(doc);
    }    
  @Override public void visitL(Core.L l){acc|=!inline(l);}

  @Override public void visitL(Full.L l){acc|=!inline(l);}

  @Override public void visitBlock(Core.Block b){acc|=!inline(b);}

  @Override public void visitBlock(Full.Block b){acc|=!inline(b);}
  
  static boolean hasMultilinePart(List<? extends Visitable<?>>vs){
    for(var v:vs){if(new HasMultilinePart().apply(v)){return true;}}
    return false;
    }
    
  static boolean hasMultilinePartHas(List<? extends HasVisitable>vs){
    for(var v:vs){if(new HasMultilinePart().apply(v.visitable())){return true;}}
    return false;
    }  
    
  static boolean inline(Core.L l){
    if(hasMultilinePart(l.ts())){return false;}
    if(hasMultilinePart(l.mwts())){return false;}
    if(hasMultilinePart(l.ncs())){return false;}
    if(hasMultilinePart(l.docs())){return false;}
    int count=l.ts().size()+l.mwts().size()*10+l.ncs().size()*2;
    return count <20;
    }
  static boolean inline(Full.L l){
    if(hasMultilinePart(l.ts())){return false;}
    if(hasMultilinePartHas(l.ms())){return false;}
    if(hasMultilinePart(l.docs())){return false;}
    int count=l.ts().size();
    count+=l.ms().stream().map(m->{
      if(m instanceof Full.L.F){return 1;}
      if(m instanceof Full.L.NC){return 2;}
      return 10;})
    .count();
    return count <20;    
    }
  static boolean inline(Core.Block b){
    if(!b.ds().isEmpty() || !b.ks().isEmpty()){return false;}
    if(new HasMultilinePart().apply(b.e().visitable())){return false;}
    return true;
    }
  static boolean inline(Full.Block b){
    if(!b.ds().isEmpty() || !b.ks().isEmpty()){return false;}
    var e=b._e();
    if(e!=null && new HasMultilinePart().apply(e.visitable())){return false;}
    return true;
    }
  
  @Override public Boolean apply(Visitable<?> t) {
    return acc;
    }
  }