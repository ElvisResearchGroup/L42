grammar L42;
@header {package is.L42.generated;}
CastOp: '<:';
Uop: '!' | '~';

Mdf: 'fwd mut' | 'fwd imm' |'imm' | 'mut' | 'lent' | 'read' | 'capsule' | 'class';
VoidKW:'void';
VarKw:'var';
CatchKw: 'catch';
InterfaceKw:'interface';
Throw: 'return'|'error'|'exception';
WhoopsKw: 'whoops';
MethodKw: 'method';
DotDotDot:'...';
Slash:'\\';
slash: Slash;
PathLit: '\''FPathLit;
pathLit: PathLit;

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
SlashX:'\\'Fx;
slashX:SlashX;
m: MUniqueNum|MHash|X;
x: X;
CsP: C(ClassSep C)*;
ClassSep: '.';
fragment C: IdUp ('A'..'Z'|'$'|'a'..'z'|'0'..'9')*;
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

eAtomic: x | csP | voidE | fullL | block | slash | pathLit | slashX;//CORE.L
fullL:'{' (header | DotDotDot | ReuseURL) fullM* doc*'}';
fullM: fullF | fullMi |fullMWT | fullNC;
fullF: VarKw? t x;
fullMi: doc* MethodKw mOp oR x* ')' '=' e;
fullMWT: doc* fullMH ('=' NativeURL? e)?;
fullNC:  doc* csP '=' e;
header: InterfaceKw? ('['t+']')?;
fullMH: (Mdf doc*)? MethodKw t mOp oR (t x)* ')' ('['t+']')?;
mOp: | m | Uop;// |OP;
voidE: VoidKW;
ePostfix: eAtomic (fCall | squareCall | string | cast)*;
fCall: ('.'m)? ORNS par ')';
squareCall: ('.'m)? '[' (par';')* par ']';
cast: CastOp t;
oR: OR |ORNS;
par: e? (x'='e)*;
block: oR d*? e ')' | oR d+ k* whoops? (d* e)? ')' | '{' d+ (k+ whoops? d* | whoops d*)? '}';
d: (dX '=')? e;
dX:VarKw? tLocal x | tLocal UnderScore | tLocal oR (VarKw? tLocal x)+ ')';
k: CatchKw Throw? t x e | CatchKw Throw? t UnderScore e;
whoops: WhoopsKw t+;
eUnary: Uop ePostfix | Number? ePostfix;

/*
  CMP/FULL.eBinary0 ::= eUnary (OP0 eUnary)* //right associative, all ops must be the same
  CMP/FULL.eBinary1 ::= (eBinary0 OP1)* eBinary0 //left associative, all op the same
  CMP/FULL.eBinary2 ::= (eBinary1 OP2)* eBinary1 //unassociative, all op the same, thus a<b<c could be resolved as a.#left#1(center:b,right:c)
  CMP/FULL.eBinary3 ::= (eBinary2 OP3)* eBinary2 //left associative, all op the same
  CMP/FULL.B ::= ( Ds e ) | ( D+ Ks WHOOPS? (Ds e)? ) | { D+ (K+ WHOOPS? Ds | WHOOPS Ds)? }
  CMP/FULL.DX ::= var? TLocal x | TLocal '_' | TLocal ( var?0 TLocal0 x0 ..var?n TLocaln xn )
  CMP/FULL.statement  ::= sIf | sWhile | sFor | loop e | throw e | x opUpdate e
  CMP/FULL.sIf ::= if e e (else e)? | if match+ e
  CMP/FULL.match ::= T x | T x = e | T?(T?0 x0..T?n xn) = e //where at least one Ti? is not empty //correctly not TLocal
  CMP/FULL.sWhile::= while e e
  CMP/FULL.sFor ::= 'for' (DX in e)* e
  CMP/FULL.e ::= statement | e-binary3
  */
e: eUnary;

nudeE: e EOF;