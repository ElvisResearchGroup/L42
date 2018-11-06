package tools;

import ast.NonNull;
import lombok.Setter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.lang.Iterable;

@RequiredArgsConstructor
public class ListFormatter {
  private final StringBuilder result = new StringBuilder();
  @NonNull @Setter private String header = "";
  @NonNull @Setter private String prefix = "";
  @NonNull @Setter private String suffix = "";
  @NonNull @Setter private String seperator = "";
  @NonNull @Setter private String footer = "";
  @Getter private int count = 0;
  private final boolean skipEmptyList, skipEmptyElements;
  public ListFormatter() { this(true, true); }

  public ListFormatter append(String s) {
    if (this.skipEmptyElements && s.isEmpty()) { return this; }
    if (this.count == 0) { this.result.append(this.header); }
    else { this.result.append(this.seperator); }

    this.result.append(this.prefix).append(s).append(this.suffix);
    this.count += 1;
    return this; }

  public ListFormatter append(Iterable<? extends Object> ss) { for (Object s : ss) { this.append(s.toString()); } return this; }
  // returns an empty string if 'record' was never called
  // Otherwise, returns:
  //    header + (prefix + s1 + suffix) + seperator + ... + seperator + (prefix + sm + suffix) + footer
  // where msg1 ... msgn where the non-empty arguments passed to record (in order)
  @Override public String toString() {
    String r = this.result.toString();
    if (!this.skipEmptyList || !r.isEmpty()) { r = r + footer; }
    return r; }}