package is.L42.connected.withHtml;

import java.util.ArrayList;
import java.util.List;

import platformSpecific.javaTranslation.Resources;

public class Events{
boolean isDisposed=false;
private final List<String>data=new ArrayList<>();
public synchronized void add(String s){
  data.add(s);}
public synchronized String toString(){return data.toString();}
public synchronized String get(){
  while(!isDisposed && data.isEmpty()){
    try{this.wait(100);}
    catch (InterruptedException e) {Frame.propagateException(e);}
    }
  if(isDisposed){throw new Resources.Error("Requested EventQueue is disposed");}
  return data.remove(0);
  }
}