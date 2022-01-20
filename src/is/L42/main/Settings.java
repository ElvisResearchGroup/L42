package is.L42.main;

import java.util.List;
import java.util.Map;

import is.L42.flyweight.C;

public record Settings(Options options,Map<List<C>,List<String>> permissions) {
  public static final Options defaultOptions=new Options("1G","256M","2G");
  public static record Options(String maxStackSize,String initialMemorySize,String maxMemorySize){
    public Options withMaxStackSize(String mss){return new Options(mss,initialMemorySize,maxMemorySize);}
    public Options withInitialMemorySize(String ims){return new Options(maxStackSize,ims,maxMemorySize);}
    public Options withMaxMemorySize(String mms){return new Options(maxStackSize,initialMemorySize,mms);}
    @Override public String toString(){
      return "-Xss"+maxStackSize+" -Xms"+initialMemorySize+" -Xmx"+maxMemorySize;
    }
  }  
}


