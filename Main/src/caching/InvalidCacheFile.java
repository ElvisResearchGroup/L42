package caching;

public class InvalidCacheFile extends RuntimeException{
  private static final long serialVersionUID = 1L;
  public InvalidCacheFile(Throwable cause){super(cause);}
  }
