package is.L42.introspection;

import static is.L42.tools.General.L;

import java.util.List;
import java.util.stream.Collectors;

import is.L42.flyweight.CoreL;
import is.L42.flyweight.P;
import is.L42.generated.Core;
import is.L42.generated.Mdf;
import is.L42.generated.Pos;
import is.L42.generated.Core.MWT;
import is.L42.tools.General;

public class OverviewVisitor extends is.L42.introspection.FullS{
  public static String makeOverview(CoreL top,boolean topShowFiles){
    var v=new OverviewVisitor(topShowFiles);
    top.accept(v);
    String res=v.result().substring(2,v.result().length()-4);
    return res.lines().map(l->l.substring(2)).collect(Collectors.joining("\n"));    
    }
  public OverviewVisitor(boolean topShowFiles){this.showFiles=topShowFiles?-1:0;}
  int showFiles;
  Mdf lastMethodMdf=null;
  long methKinds=0;
  private void updateMethKinds(CoreL l){
    methKinds=l.mwts().stream()
      .filter(m->!m.key().hasUniqueNum())
      .map(m->m.mh().mdf())
      .distinct()
      .count();
    }
  @Override public void visitInfo(Core.Info info){}
  @Override public boolean headerNewLine(){return true;}
  @Override public void visitL(CoreL l){
    showFiles+=1;
    lastMethodMdf=null;
    updateMethKinds(l);
    try{
      var mwts=L(l.mwts().stream().sorted(this::compareMWT));
      var ncs=L(l.ncs().stream().sorted(this::compareNC));
      super.visitL(l.withMwts(mwts).withNcs(ncs));
      }
    finally { showFiles-=1; methKinds=0; }
    }
  int compareMWT(Core.MWT mwt1,Core.MWT mwt2){
    var mdf1=valOfMdf(mwt1.mh().mdf());
    var mdf2=valOfMdf(mwt2.mh().mdf());
    if(mdf1==mdf2){ return mwt1.key().toString().compareTo(mwt2.key().toString()); }
    return mdf2-mdf1;
    }
  int valOfMdf(Mdf mdf){ return switch(mdf) {
    case Class-> 10;
    case Immutable-> 9;
    case ImmutableFwd-> 8;
    case Readable-> 7;
    case Lent-> 6;
    case Mutable-> 5;
    case MutableFwd-> 4;
    case Capsule-> 3;
    default ->{ throw General.bug(); }
    };}
  int compareNC(Core.NC nc1,Core.NC nc2){
    int poss=this.showFiles!=0?0:comparePoss(nc1.poss(),nc2.poss());
    if(poss!=0){ return poss; }
    return nc1.key().inner().compareTo(nc2.key().inner());
    }
  int comparePoss(List<Pos>pos1,List<Pos> pos2){
    //any local> all remote
    var l1Local=pos1.stream().anyMatch(this::isLocal);
    var l2Remote=pos2.stream().noneMatch(this::isLocal);
    if(l1Local && l2Remote){ return -1; }
    var l2Local=pos2.stream().anyMatch(this::isLocal);
    var l1Remote=pos1.stream().noneMatch(this::isLocal);
    if(l2Local && l1Remote){ return 1; }
    return 0;
    }
  boolean isLocal(Pos p){
    return p.fileName().toString().startsWith("file:");
    }
  @Override public void visitP(P p){
    if(p.hasUniqueNum()){c("<private>");return;}    
    separeFromChar();
    if(p==P.pAny){c("Any");return;}
    if(p==P.pLibrary){c("Library");return;}
    if(p==P.pVoid){c("Void");return;}
    assert p.isNCs();
    var p0=p.toNCs();
    if(p0.cs().isEmpty()){
      if(p0.n()==0){c("This");}
      else{c("This"+p0.n());}
      }
    seq(i->{},p0.cs(),".");
    }
  @Override public void visitMWT(MWT mwt){
    if(mwt.key().hasUniqueNum()){return;}
    printMethName(mwt.mh());
    super.visitMWT(mwt);
    }
  private void printMethName(Core.MH mh){
    if(this.methKinds>1 && this.lastMethodMdf!=mh.mdf()){
      c("//"+mh.mdf().inner +" methods:");
      nl();
      }
    this.lastMethodMdf=mh.mdf();
    c(mh.key().m());
    c("(");
    seq(i->{},mh.key().xs(),", ");
    c(")");
    int sizeArgs=mh.key().xs().stream().mapToInt(x->x.inner().length()+2).sum();
    int size=mh.key().m().length()+Math.max(2,sizeArgs);
    int spaces=Math.max(35-size,5);
    c(" ".repeat(spaces));    
    }
  @Override public void visitDoc(Core.Doc doc){
    c("@");
    var hasPs=doc._pathSel()!=null;
    var hasText=doc.texts().isEmpty();
    if(hasPs){ visitPathSel(doc._pathSel()); }
    if(hasText && hasPs){ return; }
    if(hasText && !hasPs){ c("{}"); return; }
    assert doc.texts().size()==doc.docs().size()+1;
    c("{");
    indent();
    if(!doc.texts().get(0).replace(" ","").startsWith("\n")){nl();}
    int min=minSpacesAfterNL(doc.texts());
    int adj=state().currentIndent().length()-min;
    seq(i->c(formatDocText(adj,doc.texts().get(i))),doc.docs(),"");
    c(formatDocText(adj,doc.texts().get(doc.texts().size()-1)));
    if(!endsWith("\n"+state().currentIndent())){ nl(); }
    deIndent();
    c("}");
    }  
  int minSpacesAfterNL(List<String>texts){
    return minSpacesAfterNL(texts.toString());
    }
  int minSpacesAfterNL(String text){
    return text.lines().skip(1).mapToInt(this::countStartSpaces).min().orElse(0);
    }
  int countStartSpaces(String text){
    return text.length()-text.stripLeading().length();
    }
  String formatDocText(int adj,String s){
    String ending="";
    if(s.endsWith("\n")){ ending="\n"+" ".repeat(adj); }
    var first=s.lines().limit(1).findFirst().orElse("");
    var rest=s.lines().skip(1).map(si->formatDocTextLine(adj, si))
      .collect(Collectors.joining("\n"));
    if(rest.isEmpty()){ return first+ending; }
    return first+"\n"+rest+ending;
    }
  String formatDocTextLine(int adj,String s){
    var text=s.stripLeading();
    var spaces=(s.length()-text.length())+adj;
    return " ".repeat(Math.max(0,spaces))+text;
    }
  }