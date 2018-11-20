package tools;

import lombok.Delegate;
import java.util.Collection;

public abstract class CollectionWrapper<T> implements Collection<T> {
  @Delegate(types=Collection.class)
  protected abstract Collection collection();
}