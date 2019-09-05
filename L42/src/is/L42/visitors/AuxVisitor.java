package is.L42.visitors;

import static is.L42.tools.General.L;
import static is.L42.tools.General.pushL;
import static is.L42.tools.General.unreachable;

import java.util.List;
import java.util.stream.Collectors;

import is.L42.common.Parse;
import is.L42.generated.C;
import is.L42.generated.Full;
import is.L42.generated.L42AuxBaseVisitor;
import is.L42.generated.L42AuxParser;
import is.L42.generated.P;
import is.L42.generated.Pos;
import is.L42.generated.S;
import is.L42.generated.X;
import is.L42.generated.Full.Doc;
import is.L42.generated.L42AuxParser.NudeCsPContext;

final class AuxVisitor extends L42AuxBaseVisitor<Object> {
private final Pos pos;

AuxVisitor(Pos pos) { this.pos = pos; }

@Override public Full.CsP visitNudeCsP(NudeCsPContext ctx) {
  return visitCsP(ctx.csP());
  }
@Override public Full.CsP visitCsP(L42AuxParser.CsPContext ctx) {
  Full.CsP r;
  if((r=FullL42Visitor.opt(ctx.cs(),null,this::visitCs))!=null){return r;}
  if((r=FullL42Visitor.opt(ctx.path(),null,this::visitPath))!=null){return r;}
  if(ctx.anyKw()!=null){return new Full.CsP(pos,L(),P.pAny);}
  if(ctx.voidKw()!=null){return new Full.CsP(pos,L(),P.pVoid);}
  if(ctx.libraryKw()!=null){return new Full.CsP(pos,L(),P.pLibrary);}
  throw unreachable();
  }
  
@Override public Full.CsP visitCs(L42AuxParser.CsContext ctx) {
  List<C> cs=L(ctx.c(),(r,c)->r.add(visitC(c)));
  return new Full.CsP(pos,cs, null);
  }

@Override public Full.CsP visitPath(L42AuxParser.PathContext ctx) {
  int n=visitThisKw(ctx.thisKw());
  List<C> cs=L(ctx.c(),(r,c)->r.add(visitC(c)));
  return new Full.CsP(pos,L(),P.of(n,cs)); 
  }

@Override public C visitC(L42AuxParser.CContext ctx) {
  String s=ctx.getText();
  int i=s.indexOf("::");
  if(i==-1){return new C(s,-1);}
  int u=Integer.parseInt(s.substring(i+2));
  return new C(s.substring(0,i),u);
  }

@Override public Integer visitThisKw(L42AuxParser.ThisKwContext ctx) {
  String s=ctx.getText().substring(4);
  if(s.isEmpty()){return 0;}
  return Integer.parseInt(s);
  }
 @Override public Full.Doc visitTopDoc(L42AuxParser.TopDocContext ctx) {
    Full.PathSel p=FullL42Visitor.opt(ctx.pathSelX(),null,this::visitPathSelX);
    if(ctx.topDocText()==null){return new Full.Doc(p, L(),L());}
    return visitTopDocText(ctx.topDocText()).with_pathSel(p);
    }
  @Override public Full.PathSel visitPathSel(L42AuxParser.PathSelContext ctx) {
    Full.CsP p=FullL42Visitor.opt(ctx.csP(),null,this::visitCsP);
    S s=FullL42Visitor.opt(ctx.selector(),null,this::visitSelector);
    return new Full.PathSel(p.cs(),p._p(),s,null); 
    }
  @Override public Full.PathSel visitPathSelX(L42AuxParser.PathSelXContext ctx) {
    Full.CsP p=FullL42Visitor.opt(ctx.pathSel().csP(),null,this::visitCsP);
    S s=FullL42Visitor.opt(ctx.pathSel().selector(),null,this::visitSelector);
    X x=FullL42Visitor.opt(ctx.x(),null,this::visitX);
    List<C> cs=L();
    P _p=null;
    if(p!=null){
      cs=p.cs();
      _p=p._p();
      }
    return new Full.PathSel(cs,_p,s,x); 
    }
  @Override public S visitSelector(L42AuxParser.SelectorContext ctx) {
    List<X>xs=L(ctx.x(),(c,xi)->c.add(visitX(xi)));
    if(ctx.m()==null){return new S("",xs,-1);}
    assert !ctx.m().getText().contains("fwd ");
    return FullL42Visitor.parseM(ctx.m().getText()).withXs(xs);
    }
    
  @Override public X visitX(L42AuxParser.XContext ctx) {
    return new X(ctx.getText());
    }

  @Override public Full.Doc visitTopDocText(L42AuxParser.TopDocTextContext ctx) {
    if(ctx.doc()==null && ctx.topDocText().isEmpty()){ 
      return new Full.Doc(null, L(ctx.getText()), L());
      }
    String firstText=ctx.charInDoc().stream()
      .map(ci->ci.getText()).collect(Collectors.joining(""));
    if(ctx.doc()==null){
      assert ctx.topDocText().size()==2;
      Full.Doc text1=visitTopDocText(ctx.topDocText(0));
      Full.Doc text2=visitTopDocText(ctx.topDocText(1));
      assert !text1.texts().isEmpty();
      assert !text2.texts().isEmpty();
      List<Full.Doc> docs=L(c->{c.addAll(text1.docs());c.addAll(text2.docs());});
      List<String> texts=L(c->{
        int s1=text1.texts().size();
        int s2=text2.texts().size();
        if(s1==1){
          c.add(firstText+"{"+text1.texts().get(0)+"}"+text2.texts().get(0));
          }
        else{
          c.add(firstText+"{"+text1.texts().get(0));
          c.addAll(text1.texts().subList(1,s1-1));
          c.add(text1.texts().get(s1-1)+"}"+text2.texts().get(0));
          }
        c.addAll(text2.texts().subList(1,s2)); 
        });
      return new Full.Doc(null, texts, docs);
      }
    assert ctx.topDocText().size()==1;
    Full.Doc text1=visitTopDocText(ctx.topDocText(0));
    var res=Parse.doc(pos.fileName(),"@"+ctx.doc().getText());
    assert !res.hasErr();
    Full.Doc doc=visitTopDoc(res.res);
    return text1
      .withDocs(pushL(doc,text1.docs()))
      .withTexts(pushL(firstText,text1.texts()));
    }  
}