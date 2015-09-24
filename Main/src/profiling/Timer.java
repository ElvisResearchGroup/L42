package profiling;

import java.util.ArrayList;
import java.util.List;

public class Timer {
  public String name;public Timer(String s){this.name=s;}
  public List<Long> times=new ArrayList<>(500);
  public void start(){
    assert times.isEmpty();
    times.add(System.currentTimeMillis());
  }
  public void pause(){
    assert times.size()%2!=0;
    times.add(System.currentTimeMillis());
  }
  public void restart(){
    assert !times.isEmpty() && times.size()%2==0;
    times.add(System.currentTimeMillis());
  }
  public String toString(){
    String result=name+"[";
    long tot=0;
    long max=0;
    long howMany=0;
    for(int i=0;i+1<times.size();i+=2){
      howMany+=1;
      long current=times.get(i+1)-times.get(i);
      tot+=current;
      if(max<current){max=current;}
    }
    result+=String.format("tot:%.3f max:%.3f number:%f",tot/60000f,max/60000f,howMany);
    return result+"]";
  }
}
