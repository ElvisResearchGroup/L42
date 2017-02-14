package is.L42.connected.withSafeOperators.location;

import java.util.ArrayList;
import java.util.List;

import ast.Ast;
import ast.Ast.Position;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.NotAvailable;

public class Doc implements Location{
  Ast.Doc inner;
  Location location;

  public int annotationsSize(){return inner.getAnnotations().size();}
  
  public Annotation annotation(int that) throws NotAvailable{
    Object ann=Location.listAccess(inner.getAnnotations(), that);
    return null;//TODO: waiting for the RefTo kinds to be implemented
    }
  public static class Annotation{
    public TypeRefTo key() {return key;}
    public void key(TypeRefTo key) {this.key = key;}
    public String text() {return text;}
    public void text(String text) {this.text = text;}
    public Annotation(TypeRefTo key, String text) {
    super();
    this.key = key;
    this.text = text;
    }
    TypeRefTo key; 
    String text;
    }
  @Override
  public Location location() {return location;}
  List<Origin> origins=null;
  List<Origin> cachedOrigins(){
    if (origins!=null){return origins;}
    origins=new ArrayList<>();
    Position p=this.inner.getP();
    while(true){
      if (p==null || p==Position.noInfo){return origins;}
      origins.add(new Origin(p.getFile(),p.getLine1(),p.getLine2(),p.getPos1(),p.getPos2()));
      p=p.get_next();
      }
    }
  @Override
  public int originsSize() {
    return cachedOrigins().size();
    }
  @Override
  public Origin origin(int that) throws NotAvailable{
    return Location.listAccess(cachedOrigins(), that);
    }

  @Override
  public String toS() {return inner.getS();}
  }