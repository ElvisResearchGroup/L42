package ast;

public class PathAux {

}
//@Value
///* @ToString(exclude="n") */ public class Path implements Expression, ExpCore, Atom {
/* int n;
 List<String> rowData;
 public NormType toImmNT(){return new NormType(Mdf.Immutable,this,Ph.None,Doc.empty());}
 private Path(int n, List<String> rowData) {
  assert!rowData.get(0).contains("-") : rowData;// as in Outer-1 :-(
  assert!rowData.contains(null) : // comment to force new line an put
          // break
  rowData;
  rowData = Collections.unmodifiableList(rowData);
  this.n = n;
  this.rowData = rowData;
 }

 public Path(List<String> rowData) {
  assert!rowData.get(0).contains("-") : rowData;// as in Outer-1 :-(
  rowData = Collections.unmodifiableList(rowData);
  this.rowData = rowData;
  String start = this.rowData.get(0);
  boolean isCore = isOutern(start);
  if (isCore) {
   start = start.substring("This".length());
   n = Integer.parseInt(start);
  } else {
   n = -1;
  }
 }

 @Override
 public <T> T accept(sugarVisitors.Visitor<T> v) {
  return v.visit(this);
 }

 @Override
 public <T> T accept(coreVisitors.Visitor<T> v) {
  return v.visit(this);
 }

 public boolean isPrimitive() {
  return this.equals(Path.Void()) || this.equals(Path.Library()) || this.equals(Path.Any());
 }

 public boolean isCore() {
  return n != -1;
 }

 public String toString() {
  return sugarVisitors.ToFormattedText.of(this);
 }

 private static boolean isOutern(String start) {
  if (!start.startsWith("This")) {
   return false;
  }
  start = start.substring("This".length());
  if (start.isEmpty()) {
   return false;
  }
  for (char c : start.toCharArray()) {
   if (!Character.isDigit(c)) {
    return false;
   }
  }
  return true;
 }

 public Path popC() {
  // assert outerNumber()==0;
  List<String> s = new ArrayList<String>(this.rowData);
  // s.remove(1);
  s.remove(s.size() - 1);
  return new Path(n, s);
 }

 public Path pushC(String c) {
  // assert outerNumber()==0;
  List<String> s = new ArrayList<String>(this.rowData);
  s.add(c);
  return new Path(n, s);
 }

 public List<String> getCBar() {
  assert!this.isPrimitive();
  if (this.isCore()) {
   return rowData.subList(1, rowData.size());
  }
  return rowData;
 }

 public Path setNewOuter(int n) {
  assert isCore();
  List<String> s = new ArrayList<String>(this.rowData);
  s.set(0, "This" + n);
  return new Path(n, s);
 }

 public int outerNumber() {
  assert isCore() : this;
  return this.n;
 }

 private static final Path _Outer0 = new Path(0, Arrays.asList("This0"));
 private static final Path _Void = new Path(-1, Arrays.asList("Void"));
 private static final Path _Any = new Path(-1, Arrays.asList("Any"));
 private static final Path _Library = new Path(-1, Arrays.asList("Library"));

 public static Path outer(int n, List<String> cs) {
  List<String> arr = new ArrayList<>();
  arr.add("This" + n);
  arr.addAll(cs);
  return new Path(n, arr);
 }

 public static Path outer(int n) {
  assert n >= 0;
  if (n == 0) {
   return _Outer0;
  }
  return new Path(n, Arrays.asList("This" + n));
 }

 public static Path Void() {
  return _Void;
 }

 public static Path Any() {
  return _Any;
 }

 public static Path Library() {
  return _Library;
 }

 public static List<String> parseValidCs(String cs) {
  if (cs.equals("This0") || cs.equals("This")) {
   return Collections.emptyList();
  }
  List<String> rowData = Collections.unmodifiableList(Arrays.asList(cs.split("\\.")));
  for (String s : rowData) {
   if (!isValidClassName(s)) {
    throw new Resources.Error("InvalidPath: " + cs);
   }
  }
  return rowData;
 }

 public static boolean isValidOuter(String name) {// thus invalid as
              // pathName
  if (name.equals("This")) {
   return true;
  }
  if (name.equals("This0")) {
   return true;
  }
  if (!name.startsWith("This")) {
   return false;
  }
  int firstN = "This".length();
  char c = name.charAt(firstN);
  // first is 1--9 and all rest is 0-9
  if ("123456789".indexOf(c) != -1) {
   return false;
  }
  for (int i = firstN + 1; i < name.length(); i++) {
   if ("0123456789".indexOf(name.charAt(i)) == -1) {
    return false;
   }
  }
  return true;
 }

 public static boolean isValidClassName(String name) {
             if(name.isEmpty()){return false;}
  if (isValidOuter(name)) {
   return false;
  }
  if (!isValidPathStart(name.charAt(0))) {
   return false;
  }
  for (int i = 1; i < name.length(); i++) {
   if (!isValidPathChar(name.charAt(i))) {
    return false;
   }
  }
  return true;
 }

 public static Path parse(String path) {
  List<String> rowData = Arrays.asList(path.split("\\."));
  for (String s : rowData) {// TODO: make it more precise, throw real
         // error?
   assert isValidOuter(s) || isValidClassName(s) : path;
  }
  return new Path(rowData);
 }

 public static boolean isValidPathStart(char c) {
  if (c == '%') {
   return true;
  }
  if (c == '$') {
   return true;
  }
  return Character.isUpperCase(c);
 }

 public static boolean isValidPathChar(char c) {
  if (c == '%') {
   return true;
  }
  if (c == '$') {
   return true;
  }
  if (c == '_') {
   return true;
  }
  assert c!='\t':
   c;
  return Character.isUpperCase(c) || Character.isLowerCase(c) || Character.isDigit(c);
 }
}*/