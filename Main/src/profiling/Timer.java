package profiling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.function.Supplier;

public class Timer {
  private static final String TOP 	= "TOP";
  private static final int RAWTOTAL	= 0;
  private static final int RAWMAX	= 1;
  private static final int NETTOTAL	= 2;
  private static final int NETMAX	= 3;
  private static final int COUNT	= 4;
  public static <T> T record(String name,Supplier<T> fun){
  Timer.activate(name);try{  return fun.get();
  }finally{Timer.deactivate(name);}
  }
  public static List<Long> fakeTimes=new ArrayList<>();//for testing
  public static long giveTime(){
    if(fakeTimes.isEmpty()){return System.currentTimeMillis();}
    long res=fakeTimes.get(0);
    fakeTimes=fakeTimes.subList(1, fakeTimes.size());
    return res;
  }
  private static class TimerEntry{
      String name;long time; boolean isOpen;
      TimerEntry(String name,long time, boolean isOpen){this.name=name;this.time=time; this.isOpen = isOpen;}
  }
  public static final List<TimerEntry>timers=new ArrayList<>();
  public static void activate(String name){
    assert timers.isEmpty() || !timers.get(timers.size()-1).name.equals("o_"+name);
    timers.add(new TimerEntry(name,giveTime(), true));
  }
  public static void deactivate(String name){
    timers.add(new TimerEntry(name,giveTime(), false));
  }
 
  public static void restart(){timers.clear(); }
  public static String report(){
    try{return _report();}
    catch(Throwable t){ t.printStackTrace(); return "Unable to profile, caused by "+t;}
  }
  private static String _report(){
    if(timers.size()%2!=0){
      Timer.deactivate("TOP");
    }

    Map<String,long[]> processStatistic = new HashMap<String, long[]>();
    timerTreeNode timerTree = new timerTreeNode(0, TOP);
    getTimersTree(timers.listIterator(), timerTree);
    if(timerTree.childs.isEmpty())
    	return "nothing to see\n";
    if(timerTree.childs.get(0).name.equals(TOP))
    	timerTree = timerTree.childs.get(0);
    else{
    	timerTree.name		= TOP;
    	timerTree.startTime = timerTree.childs.get(0).startTime;
    	timerTree.endTime	= timerTree.childs.get(timerTree.childs.size()-1).endTime;
    }
    try {
		timerTree.calculateTimes(processStatistic, new ArrayList<String>());
	} catch (Exception e) {
		e.printStackTrace();
		return "something went wrong\n";
	}
    
    String result="\n*******************************\n";
    List<String>names=new ArrayList<>(processStatistic.keySet());
    names.sort((n1,n2)->(int)(processStatistic.get(n2)[RAWTOTAL]-processStatistic.get(n1)[RAWTOTAL]));
    long totTop = timerTree.total();
    for(String name:names){
      long[] stats	=processStatistic.get(name);
      result+="# "+name+"\n";
      result+=String.format("percentage:%.2f/%.2f tot:%.3f/%.3f max:%.3f/%.3f number:%d",
    		  (stats[RAWTOTAL]/(float)totTop), (stats[NETTOTAL]/(float)totTop),
    		  stats[RAWTOTAL]/60000f, stats[NETTOTAL]/60000f,
    		  stats[RAWMAX]/60000f, stats[NETMAX]/60000f,
    		  (int)stats[COUNT])
    		  +"\n";
      }
    return result+"*************************************\n";
  }  
  private static long getTimersTree(ListIterator<TimerEntry> timerIterator, timerTreeNode parent){
	  while(true){
		  if(!timerIterator.hasNext())
			return 0;
		  TimerEntry current = timerIterator.next();
		  long time = current.time;
		  String name = current.name;
		  if(!current.isOpen)
			  return time;
		  timerTreeNode self = new timerTreeNode(time, name);
		  parent.addChild(self);
		  long endTime = getTimersTree(timerIterator, self);
		  self.endTime = endTime;
	  }
  }  

  protected static class timerTreeNode{
	  long startTime;
	  long endTime;
	  String name;
	  List<timerTreeNode> childs;
	timerTreeNode(long startTime, String name) {
		super();
		this.startTime = startTime;
		this.endTime = 0;
		this.name = name;
		this.childs = new ArrayList<>();
	}	
	void addChild(timerTreeNode child){
		this.childs.add(child);
	}
	long total(){
		return endTime-startTime;
	}	
	void calculateTimes(Map<String, long[]> outputList, List<String> elders) throws Exception{
		long netNodeTime = this.total();
		boolean isAncestor = true;
		if(elders.contains(this.name))
			isAncestor = false;
		else
			elders.add(this.name);
		
		for(timerTreeNode child:this.childs){
			child.calculateTimes(outputList, elders);
			netNodeTime -= child.total();
		}
		if(isAncestor)
			elders.remove(this.name);
		if(netNodeTime<0)
			throw new Exception("unreliable tree");
		long newNode[]	= new long[5];
		if(outputList.containsKey(this.name)){
			newNode[RAWTOTAL]	= outputList.get(this.name)[RAWTOTAL];
			newNode[RAWMAX]		= outputList.get(this.name)[RAWMAX];
			newNode[NETTOTAL]	= outputList.get(this.name)[NETTOTAL];
			newNode[COUNT]		= outputList.get(this.name)[COUNT];
		}
		if(isAncestor)
			newNode[RAWTOTAL]	+= this.total();
		if(newNode[RAWMAX]<this.total())
			newNode[RAWMAX] = this.total();
		if(newNode[NETMAX]<netNodeTime)
			newNode[NETMAX]	= netNodeTime;
		newNode[NETTOTAL]	+= netNodeTime;
		newNode[COUNT]	+= 1;
		outputList.put(this.name, newNode);
	}
  }
}
