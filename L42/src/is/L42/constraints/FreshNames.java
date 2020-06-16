package is.L42.constraints;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import is.L42.generated.X;

public class FreshNames implements Serializable{
  int current=0;
  Set<Integer> used=new HashSet<>();
  public FreshNames copy(){
    var res=new FreshNames();
    res.current=this.current;
    res.used.addAll(this.used);
    return res;
    }
  public void addToUsed(X x){
    String i=x.inner();
    if(!i.startsWith("fresh")){return;}
    int u=i.indexOf("_");
    if(u==-1){return;}
    i=i.substring(5,u);//"fresh".length()
    used.add(Integer.parseInt(i));
    }
  public String fresh(String hint){
    while(used.contains(current)){current+=1;}
    String res="fresh"+current+"_"+hint;
    current+=1;
    return res;
    }
  @Override public int hashCode() {return 31*(31+current) + used.hashCode();}
  @Override public boolean equals(Object obj) {
    if(this == obj){return true;}
    if(obj == null){return false;}
    if(getClass() != obj.getClass()){return false;}
    FreshNames other = (FreshNames) obj;
    return current==other.current && used.equals(other.used);
    }
  }