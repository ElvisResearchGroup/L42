package programReduction;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import ast.Ast;
public class Paths {
  @SuppressWarnings("serial")
  public static class EmptyPath extends RuntimeException{}
  final Paths next;
  final List<List<String>> current;
  private Paths(Paths next, List<List<String>> current){
    this.next=next; this.current=current;
    }
  private static final Paths empty=new Paths(null,null);
  public static Paths empty(){return empty;}
  
  
  public List<List<String>> top(){return current;}
  public Paths pop(){
    if (this==empty){throw new EmptyPath();}
    return this.next;
    }
  public Paths push(List<List<String>> css){
    return new Paths(this,minimize(css));
    }
  public Paths union(Paths other){
    if (this==empty){return other;}
    if (other==empty){return this;}
    Paths rec=this.pop().union(other.pop());
    List<List<String>> css = new ArrayList<>(this.top());
    css.addAll(other.top());
    return rec.push(css);
  }
  public Paths prefix(List<String>cs){
    if(this==empty){return empty;}
    if(cs.isEmpty()){return this;}
    Paths path=this.pop();
    List<List<String>> css=this.top();
    List<String> csPopped=cs.subList(0, cs.size()-1);
    String c=cs.get(cs.size()-1);
    Paths paths0=path.prefix(csPopped);
    List<List<String>> top0=new ArrayList<>(paths0.top());
    for(List<String>csi:css){
      List<String>cscsi=new ArrayList<>(cs);
      cscsi.addAll(csi);
      top0.add(cscsi);
      }
    return paths0.pop().push(top0);
    }
  public static Paths reorganize(List<Ast.Path> ps){
    if(ps.isEmpty()){return empty;}
    return reorganize(0,ps);
  }
  private static Paths reorganize(int level,List<Ast.Path> ps){
  if(ps.isEmpty()){return empty;}
  List<List<String>> csi=new ArrayList<>();
  List<Ast.Path> nextPs=new ArrayList<>();
  for(Ast.Path pi:ps){
    if(pi.isPrimitive()){continue;}
    if(pi.outerNumber()==level){csi.add(pi.getCBar());continue;}
    nextPs.add(pi);
    }
  return reorganize(level+1,nextPs).push(csi);
  }
  private static List<List<String>> minimize(List<List<String>> css){
    List<List<String>> res=new ArrayList<>();
    for(List<String> csCandidate: css) outCandidate:{
      for(List<String> csTest: css){
        if(isPrefix(csTest,csCandidate)){break outCandidate;}
      }
      res.add(csCandidate);
    }
    return css;
    }
  private static boolean isPrefix(List<String>csShort,List<String>csLong){
    if(csShort.size()>=csLong.size()){return false;}
    for(int i=0;i<csShort.size();i++){
      if (!csShort.get(i).equals(csLong.get(i))){return false;}
      }
    //with cs1 in csShort.vals(), cs2 in csLong.vals(maxTo:csShort.size()) ( if cs1!=cs2 (return Bool.false()))
    return true;
    }
  }
