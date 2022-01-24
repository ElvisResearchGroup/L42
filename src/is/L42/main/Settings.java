package is.L42.main;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import is.L42.common.EndError;
import is.L42.flyweight.C;

public record Settings(Options options,Map<List<C>,List<String>> permissions) implements Serializable{
  public Settings{ options.invariant(); }
  public String toString() {
    String permissions=this.permissions.entrySet().stream().map(e->{
      var cs=e.getKey().stream().map(k->k+"").collect(Collectors.joining("."));
      return cs+"="+e.getValue();
      }).sorted().collect(Collectors.joining(";"));
    return "Settings[options="+this.options
      +",permissions={"+permissions+"}]";
  }
  public static final Options defaultOptions=new Options("1G","256M","2G");
  public static record Options(String maxStackSize,String initialMemorySize,String maxMemorySize) implements Serializable{
    public Options withMaxStackSize(String mss){return new Options(mss,initialMemorySize,maxMemorySize);}
    public Options withInitialMemorySize(String ims){return new Options(maxStackSize,ims,maxMemorySize);}
    public Options withMaxMemorySize(String mms){return new Options(maxStackSize,initialMemorySize,mms);}
    public void addOptions(List<String>opts){
      opts.add(0,"-Xss"+maxStackSize);
      opts.add(1,"-Xms"+initialMemorySize);
      opts.add(2,"-Xmx"+maxMemorySize);
      }
    @Override public String toString(){
      return "-Xss"+maxStackSize+" -Xms"+initialMemorySize+" -Xmx"+maxMemorySize;
      }
    //private boolean isK(String s) {return s.endsWith("K");}
    private boolean isM(String s) {return s.endsWith("M");}
    private boolean isG(String s) {return s.endsWith("G");}
    private long value(String s){
      long val=Long.parseLong(s.substring(0,s.length()-1));
      if (isM(s)){val*=1024L;}
      if (isG(s)){val*=1024L*1024L;}
      return val;
      }
    public void invariant(){
      long mss=value(maxStackSize);
      long ims=value(initialMemorySize);
      long mms=value(maxMemorySize);
      if(ims>mms){throw new EndError.NotWellFormed(List.of(),
        "The specified max memory size is smaller than the initial memory size."
        );}
      if(mss<200){throw new EndError.NotWellFormed(List.of(),
        "The stack size specified is too small. Specify at least 200K"
        );}
      if(mss>1024L*1024L){throw new EndError.NotWellFormed(List.of(),
        "The stack size specified is too large. Specify no more than 1G"
        );}
      if(mms>65536L*1024L*1024L){throw new EndError.NotWellFormed(List.of(),
        "The max memory size specified is too large. Specify no more than 64T (65536G)"
        );}
      if(ims<100L*1024L){throw new EndError.NotWellFormed(List.of(),
          "The initial memory size specified is too small. Specify at least 100M"
          );}
      }
    }
  }
