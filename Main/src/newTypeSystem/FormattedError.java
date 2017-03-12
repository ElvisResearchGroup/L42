package newTypeSystem;

public class FormattedError extends RuntimeException {

    public FormattedError(TErr err) {
      super("\n"+err.in+"\n"+err.msg+"\n"+err.kind+"\n"+err._computed);
    }

}
