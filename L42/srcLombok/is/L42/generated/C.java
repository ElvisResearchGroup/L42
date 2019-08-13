package is.L42.generated;
import lombok.Value;
import lombok.experimental.Wither;

@Value @Wither public class C implements LDom{String inner;}

//TODO:
/*
 
 replace ctxL with C:LL
 pTail::= CORE.L | C:LL
 
C calls static{loadclass Init}
class Init static checks:
  assertions are on
  set some global variables for caching and disablyng certain
    assertions/wf checks
all ast stuff, including Half implements a single Visitable interface
  then we have a single big visitor pattern
  including visit C, visit X and visit List<C> ...
  
define Half.E and so on

all toString cutpasted as return Stuff.toS(this) this:Visitable
also, wf cutpasted as return Stuff.wf(this) and not called automatically

check why toStringVisitor was not great

all wf for C/m/P... uses the parser

subparse for string interpolation and docs
for each wf criteria, make some negative tests
*/