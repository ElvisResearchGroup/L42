package is.L42.generated;
import lombok.Value;
import lombok.experimental.Wither;
import java.util.List;

public interface ST {
  @Value @Wither public static class 
  STMeth implements ST{ST st; S s; int x;/*0 for no i*/}  
  @Value @Wither public static class 
  STOp implements ST{Op op; List<List<ST>> stzs;}
}
