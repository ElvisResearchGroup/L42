package ast;



import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import lombok.experimental.Wither;
import lombok.AllArgsConstructor;
import lombok.Data;
@SuppressWarnings("serial")
public abstract class InternalError extends RuntimeException{//stuff that should not get out

  @Value @EqualsAndHashCode(callSuper=false)@ToString(callSuper=true, includeFieldNames=true)
public static class InterfaceNotFullyProcessed extends InternalError{}

@Value @EqualsAndHashCode(callSuper=false)@ToString(callSuper=true, includeFieldNames=true)
public static class FurtherAnnotationImpossible extends InternalError{}

@Value @EqualsAndHashCode(callSuper=false)@ToString(callSuper=true, includeFieldNames=true)
public static class ETDeepNotApplicable extends InternalError{}

}