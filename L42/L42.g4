grammar L42;
@header {package is.L42.generated;}
Mdf: 'fwd mut' | 'fwd imm' |'imm' | 'mut' | 'lent' | 'read' | 'capsule' | 'class';
VoidKW:'void';
VarKw:'var';
CatchKw: 'catch';
InterfaceKw:'interface';
Throw: 'return'|'error'|'exception';
WhoopsKw: 'whoops';
MethodKw: 'method';
DotDotDot:'...';
fragment IdUp: '_'* ('A'..'Z'|'$');
fragment IdLow: '_'* 'a'..'z';
fragment IdChar: 'a'..'z' | 'A'..'Z' | '$' | '_' | '0'..'9';
fragment CHAR:
'A'..'Z'|'a'..'z'|'0'..'9' | '(' | ')' | '[' | ']' | '<' | '>' |'&'|'|'|'*'|'+'|'-'|'=' | '/' | '!' | '?' | ';' | ':' | ',' | '.' | ' ' | '~' | '@' | '#' | '$' | '%' | '`' | '^' | '_' | '\\' | '{' | '}' | '"' | '\'' | '\n';
fragment CHARInStringSingle:
'A'..'Z'|'a'..'z'|'0'..'9' | '(' | ')' | '[' | ']' | '<' | '>' |'&'|'|'|'*'|'+'|'-'|'=' | '/' | '!' | '?' | ';' | ':' | ',' | '.' | ' ' | '~' | '@' | '#' | '$' | '%' | '`' | '^' | '_' | '\\' | '{' | '}' |         '\'';//no \n and "
fragment CharsUrl:
'A'..'Z'|'a'..'z'|'0'..'9' | '(' | ')' | '<' | '>' |'&'|'|'|'*'|'+'|'-'|'=' | '/' | '!' | '?' | ';' | ':' | ',' | '.' | ' ' | '~' | '@' | '#' | '$' | '%' | '`' | '^' | '_' | '\\'  ;
fragment CHARDocText:
'A'..'Z'|'a'..'z'|'0'..'9' | '(' | ')' | '[' | ']' | '<' | '>' |'&'|'|'|'*'|'+'|'-'|'=' | '/' | '!' | '?' | ';' | ':' | ',' | '.' | ' ' | '~' | '#' | '$' | '%' | '`' | '^' | '_' | '\\' | '"' | '\'' | '\n'; //no {}@
fragment URL:CharsUrl+;
ReuseURL:'reuse' Whitespace* '['URL']';
NativeURL:'native' Whitespace* '['URL']';
fragment Fn: '0' | '1'..'9' ('0'..'9')*;
fragment Fx: IdLow IdChar*;
StringSingle: '"' CHARInStringSingle '"';
string: StringSingle;//TODO: will also match multilineStr and interpolation
Number : '0'..'9' ('.'|'_'|'-'|'0'..'9')*;
MUniqueNum: Fx('#' Fx)*'::'Fn;
MHash: '#' '$'? Fx('#' Fx)* ('::'Fn)?;
X:  Fx;
m: MUniqueNum|MHash|X;
x: X;
fragment C: IdUp ('A'..'Z'|'$'|'a'..'z'|'0'..'9')*;
CsP: C(ClassSep C)*;
ClassSep: '.';
UnderScore:'_';//need to be not earlier then here, after X and CsP
OR:' ('| ',(' | '\n(';
ORNS:'(';
Doc: '@'FPathLit | '@'FPathLit?'{'DocText'}';
fragment FS:(MUniqueNum|MHash|X) FParXs;
fragment FParXs: '(' ')' | '(' X ( (' '|',')X )* ')';
fragment FPathLit: FS('.'X)? |FParXs('.'X)? | CsP( ('.'FS|FParXs)('.'X)? )?;
fragment DocText: | CHARDocText DocText | Doc DocText | '{' DocText '}' DocText;
doc:Doc;

BlockComment: '/*' (BlockComment|.)*? '*/'	-> channel(HIDDEN) ; // nesting comments allowed
LineComment: '//' .*? ('\n'|EOF)				-> channel(HIDDEN) ;
Whitespace: (( ' ' | ',' | '\n' )+)-> channel(HIDDEN);

csP: CsP;
t:Mdf? doc* csP | '\\';
tLocal: t | Mdf | ; 

eAtomic: x | csP | voidE | fullL | block;//| LL | B | '('T e')' | '\'  | '\'' PathLit;
fullL:'{' (header | DotDotDot | ReuseURL) fullM* doc*'}';
fullM: fullF | fullMi;//| MI | MWT | NC
fullF: VarKw? t x;
fullMi: doc* MethodKw m oR x* ')' '=' e;
fullMWT: doc* fullMH ('=' NativeURL? e)?;
//  FULL.NC ::= Docs C = e //FULL.L can be inside e
header: InterfaceKw? ('['t+']')?;
fullMH: (Mdf doc*)? MethodKw t mOp oR (t x)* ')' ('['t+']')?;
mOp: | m | '~' | '!';// |OP;
voidE: VoidKW;
e: eAtomic |e fCall;
fCall: ORNS par ')';
oR: OR |ORNS;
par: e? (x'='e)*;
block: oR d*? e ')' | oR d+ k* whoops? (d* e)? ')' | '{' d+ (k+ whoops? d* | whoops d*)? '}';
d: (dX '=')? e;
dX:VarKw? tLocal x | tLocal UnderScore | tLocal oR (VarKw? tLocal x)+ ')';
k: CatchKw Throw? t x e | CatchKw Throw? t UnderScore e;
whoops: WhoopsKw t+;

nudeE: e EOF;