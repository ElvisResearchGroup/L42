package newTypeSystem;

public class FormattedError extends RuntimeException {
    public final TErr err;
    public final ErrorKind kind;
    public FormattedError(TErr err) {
      super("\n"+err.in+"\n"+err.msg+"\n"+err.kind+"\n"+err._computed);
      this.err=err;
      this.kind=err.kind;
    }

}
