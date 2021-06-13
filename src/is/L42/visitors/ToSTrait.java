package is.L42.visitors;
import static is.L42.tools.General.range;

import java.util.List;
import java.util.function.Function;
import java.util.function.IntConsumer;
import is.L42.generated.*;

public interface ToSTrait extends CollectorVisitor{
  class ToSState{
    private StringBuilder result=new StringBuilder();
    private String currentIndent="";
    boolean lastWasNl=false;
    boolean lastWasNum=false;
    }
  ToSState state();
  default StringBuilder result(){return state().result;}
  default IntConsumer empty(){return i->{};} 
  default void nl(){
    if(state().lastWasNl){return;}
    state().result.append("\n");
    state().result.append(state().currentIndent);
    state().lastWasNl=true;
    state().lastWasNum=false;
    }
  default void indent(){state().currentIndent+="  ";}
  default void deIndent(){
    state().currentIndent=state().currentIndent.substring(2);
    }
  default char last(){
    return state().result.charAt(state().result.length()-1);
    }
  default void c(String s){
    if(s.isEmpty()){return;}
    assert !s.startsWith(",") || last()!='(':s;
    state().result.append(s);
    state().lastWasNl=false;
    state().lastWasNum=false;
    }
  default void separeFromChar(){
    if(state().lastWasNl || state().lastWasNum){return;}
    if(state().result.length()==0){return;}
    char last=last();
    if(Character.isLetter(last) || Character.isDigit(last) || last=='$' || last=='_'){
      state().result.append(" ");
      }
    }
  default void kw(String s){
    separeFromChar();
    c(s);
    }
  default void seqHas(IntConsumer prefix, List<? extends HasVisitable> elements,String sep){
    for(int i:range(elements)){
      if(i!=0){c(sep);}
      prefix.accept(i);
      elements.get(i).visitable().accept(this);
      }
    }
  default void seq(IntConsumer prefix, List<? extends Visitable<?>>elements,String sep){
    for(int i:range(elements)){
      if(i!=0){c(sep);}
      prefix.accept(i);
      elements.get(i).accept(this);
      }
    }
  default <E>void seq(List<E> elements,Function<E,String> map,String sep){
    for(int i:range(elements)){
      if(i!=0){c(sep);}
      c(map.apply(elements.get(i)));
      }
    }
  }
