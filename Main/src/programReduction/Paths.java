package programReduction;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import ast.Ast;
import ast.Ast.C;
import ast.ErrorMessage;
import ast.ExpCore.ClassB;
import coreVisitors.IsCompiled;
public class Paths {
  //@SuppressWarnings("serial")
  //public static class EmptyPath extends RuntimeException{}
  final Paths next;
  final List<List<Ast.C>> current;
  private Paths(Paths next, List<List<Ast.C>> current){
    this.next=next; this.current=current;
    assert new HashSet<>(current).size()==current.size();
    }
  private static final Paths empty=new Paths(null,Collections.emptyList());
  public static Paths empty(){return empty;}
  public boolean isEmpty(){return this==empty;}

  public List<List<Ast.C>> top(){
    assert current!=null;
    return current;
    }
  public Paths pop(){
    if (this==empty){return empty;}
    return this.next;
    }
  public Paths push(List<List<Ast.C>> css){
    return new Paths(this,minimize(css));
    }
  public Paths union(Paths other){
    assert other!=null:
    "";
    if (this==empty){return other;}
    if (other==empty){return this;}
    Paths rec=this.pop().union(other.pop());
    List<List<Ast.C>> css = new ArrayList<>(this.top());
    css.addAll(other.top());
    return rec.push(css);
  }
  public boolean checkAllDefined(Program p){
    if(this.isEmpty()){return true;}
    for(List<Ast.C>cs:this.top()){
      try{
        ClassB li=p.top().getClassB(cs);
        if(!IsCompiled.of(li)){
          throw new ErrorMessage.PathMetaOrNonExistant(true, cs, p.top(), p.top().getP(), null);
          }
        }
      catch(ErrorMessage.PathMetaOrNonExistant pne){
        throw pne.withListOfNodeNames(cs);
        }
      catch(RuntimeException rte){
        throw rte;//to breakpoint
        }
      }
    if(this.pop().isEmpty()){return true;}
    return this.pop().checkAllDefined(p.pop());

    }

  public Paths prefix(List<Ast.C>cs){
    if(this==empty){return empty;}
    if(cs.isEmpty()){return this;}
    if(this.pop()==empty){
      List<List<Ast.C>> res=new ArrayList<>();
      //checked in constructor: assert new HashSet<>(this.top()).size()==this.top().size();//no dups in TOP
      for(List<Ast.C>csi:this.top()){
        List<Ast.C>cscsi=new ArrayList<>(cs);
        cscsi.addAll(csi);
        res.add(cscsi);
        }
      return new Paths(empty,res);
      }
    assert this.pop()!=empty;
    Paths path=this.pop();
    List<Ast.C> csPopped=cs.subList(0, cs.size()-1);
    Paths paths0=path.prefix(csPopped);
    assert paths0!=empty;
    Paths paths1=new Paths(empty,this.top()).prefix(cs);
    assert paths1!=empty;
    return paths0.union(paths1);//this remove duplicates from paths0 and paths1 by "minimize"
    }
  public static Paths reorganize(List<Ast.Path> ps){
    if(ps.isEmpty()){return empty;}
    return reorganize(0,ps);
  }
  private static Paths reorganize(int level,List<Ast.Path> ps){
  if(ps.isEmpty()){return empty;}
  List<List<Ast.C>> csi=new ArrayList<>();
  List<Ast.Path> nextPs=new ArrayList<>();
  for(Ast.Path pi:ps){
    if(pi.isPrimitive()){continue;}
    if(pi.outerNumber()==level){csi.add(pi.getCBar());continue;}
    nextPs.add(pi);
    }
  return reorganize(level+1,nextPs).push(csi);
  }
  public static List<List<Ast.C>> minimize(List<List<Ast.C>> css){
    Set<List<Ast.C>> res=new LinkedHashSet<>();
    for(List<Ast.C> csCandidate: css) {
      if(!hasPrefixIn(css,csCandidate)){res.add(csCandidate);}
      }
    return new ArrayList<>(res);
    }
  private static boolean hasPrefixIn(List<List<Ast.C>>cssShort,List<Ast.C>csLong){
    for(List<Ast.C> csTest: cssShort){
      if(isPrefix(csTest,csLong)){return true;}
      }
    return false;
    }
  private static boolean isPrefix(List<Ast.C>csShort,List<Ast.C>csLong){
    if(csShort.size()>=csLong.size()){return false;}
    for(int i=0;i<csShort.size();i++){
      if (!csShort.get(i).equals(csLong.get(i))){return false;}
      }
    //with cs1 in csShort.vals(), cs2 in csLong.vals(maxTo:csShort.size()) ( if cs1!=cs2 (return Bool.false()))
    return true;
    }
  public String toString(){
    return toString(0);
    }
  public String toString(int n){
    String result="";
    if(this.isEmpty()){return "<Empty Paths>";}
    for(List<Ast.C> cs:this.top()){
      result+=Ast.Path.outer(n, cs)+" ";
      }
    if(this.pop()!=empty){result+=this.pop().toString(n+1);}
    return result;
    }

  public boolean containsPrefixFor(ast.Ast.Path s) {
    if(s.isPrimitive()){return false;}
    Paths popped=this;
    for(int i=0;i<s.outerNumber();i++){
      popped=popped.pop();
      }
    for(List<C> p:popped.current){
      if(s.getCBar().size()<p.size()){continue;}
      if(s.getCBar().subList(0, p.size()).equals(p)){
        return true;
        }
      }
    return false;
    }
  }
