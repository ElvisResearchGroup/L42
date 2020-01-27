package is.L42.cache;

import java.util.List;

public class IntCache implements Cache<Integer> {

	@Override
	public Integer normalize(Integer t) { return t; }
	
	@Override
	public NormResult<Integer> normalizeInner(Integer t, List<Object> chain) {
		return new NormResult<Integer>(t);
		}
	
	@Override
	public boolean isNorm(Integer t) { return true; }

	@Override
	public boolean structurallyEqual(Integer t1, Integer t2) {
		return t1.intValue() == t2.intValue();
		}

	public Object f(Integer t, int i) {
		throw new ArrayIndexOutOfBoundsException();
		}

	@Override
	public Object[] f(Integer t) {
		return new Object[0];
		}

	@Override
	public void f(Integer t, Object o, int i) {
		throw new ArrayIndexOutOfBoundsException();
		}

	@Override
	public String typename(Integer t) {
		return "�nativeint";
		}

	@Override
	public KeyNorm2D computeKeyNN(Integer t) {
		return this.simpleKey(t);
		}

	@Override
	public NormResult<Integer> computeKeyNNInner(Integer t, List<Object> chain) {
		return new NormResult<Integer>(t);
		}
	}
