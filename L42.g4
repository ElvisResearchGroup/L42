grammar L42;
@header {package is.L42.generated;}
CastOp: '<:';
Uop: '!' | '~';
OP0: '^' | ':' | '<<' | '++' | '--' | '**'; // right associative
OP1: '+' | '-' | '*' | '/'  | '>>'; // left associative 
OP2: '==' | '<' | '>' | '>=' | '<=' | '=>' | '!='; // unassiociative //InKw need to be added in the usage sites
OP3: '->' | '&&' | '||'; // right associative, will be short circuting
OpUpdate: ':=' | '^=' | '<<=' | '+=' | '-=' | '*=' | '/=' | '++=' | '--=' | '**=' | '>>=';
Mdf: 'fwd mut' | 'fwd imm' |'imm' | 'mut' | 'lent' | 'read' | 'capsule' | 'class';
VoidKW:'void';
VarKw:'var';
Info:('#norm{' | '#typed{') BalCurly '}';
CatchKw: 'catch';
InterfaceKw:'interface';
IfKw:'if';
ElseKw:'else';
WhileKw:'while';
ForKw:'for';
InKw:'in';
LoopKw: 'loop';
Throw: 'return'|'error'|'exception';
WhoopsKw: 'whoops';
MethodKw: 'method';
DotDotDot:'...';
Slash:'\\';
slash: Slash;
PathSel: '\''FPathSel;
pathSel: PathSel;

fragment IdUp: '_'* ('A'..'Z'|'$');
fragment IdLow: '_'* 'a'..'z';
fragment IdChar: 'a'..'z' | 'A'..'Z' | '$' | '_' | '0'..'9';
fragment CHAR:
'A'..'Z'|'a'..'z'|'0'..'9' | '(' | ')' | '[' | ']' | '<' | '>' | '&' | '|' | '*' | '+' | '-' | '=' | '/' | '!' | '?' | ';' | ':' | ',' | '.' | ' ' | '~' | '@' | '#' | '$' | '%' | '`' | '^' | '_' | '\\' | '{' | '}' | '"' | '\'' | '\n';
fragment CHARInStringSingle:
'A'..'Z'|'a'..'z'|'0'..'9' | '(' | ')' | '[' | ']' | '<' | '>' |'&'|'|'|'*'|'+'|'-'|'=' | '/' | '!' | '?' | ';' | ':' | ',' | '.' | ' ' | '~' | '@' | '#' | '$' | '%' | '`' | '^' | '_' | '\\' | '{' | '}' |       '\'';//no \n and "
fragment CHARInStringMulti:
'A'..'Z'|'a'..'z'|'0'..'9' | '(' | ')' | '[' | ']' | '<' | '>' |'&'|'|'|'*'|'+'|'-'|'=' | '/' | '!' | '?' | ';' | ':' | ',' | '.' | ' ' | '~' | '@' | '#' | '$' | '%' | '`' | '^' | '_' | '\\' | '{' | '}' | '"' | '\'';//no \n
fragment CharsUrl:
'A'..'Z'|'a'..'z'|'0'..'9' | '(' | ')' | '<' | '>' |'&'|'|'|'*'|'+'|'-'|'=' | '/' | '!' | '?' | ';' | ':' | ',' | '.' | ' ' | '~' | '@' | '#' | '$' | '%' | '`' | '^' | '_' | '\\'  ;
fragment CHARDocText:
'A'..'Z'|'a'..'z'|'0'..'9' | '(' | ')' | '[' | ']' | '<' | '>' |'&'|'|'|'*'|'+'|'-'|'=' | '/' | '!' | '?' | ';' | ':' | ',' | '.' | ' ' | '~' | '#' | '$' | '%' | '`' | '^' | '_' | '\\' | '"' | '\'' | '\n'; //no {}@
fragment AuxBalCurly:
'A'..'Z'|'a'..'z'|'0'..'9' | '(' | ')' | '[' | ']' | '<' | '>' |'&'|'|'|'*'|'+'|'-'|'=' | '/' | '!' | '?' | ';' | ':' | ',' | '.' | ' ' | '~' | '#' | '$' | '%' | '`' | '^' | '_' | '\\' | '"' | '\'' | '\n' |'@'; //no {}
fragment BalCurly: AuxBalCurly* ('{' BalCurly '}' BalCurly)?;
fragment URL:CharsUrl+;
ReuseURL:'reuse' Whitespace* '['URL']';
NativeURL:'native' Whitespace* '{'BalCurly'}';
fragment Fn: '0' | '1'..'9' ('0'..'9')*;
fragment Fx: IdLow IdChar*;
fragment StringMultiOpen:'"""' '%'* '\n';
fragment StringMultiClose:(' ' | ',')* '"""';
fragment StringMultiLine:(' ' | ',')* '|' CHARInStringMulti* '\n';
StringMulti: StringMultiOpen StringMultiLine+ StringMultiClose;
StringSingle: '"' CHARInStringSingle* '"';
string: StringMulti|StringSingle;
Number: '0'..'9' ('.'|'_'|'-'|'0'..'9')*;
MUniqueNum: Fx('#' Fx)*'::'Fn;
MHash: ('#$' | '#'+) Fx('#' Fx)* ('::'Fn)?;
X: Fx;
x: X;
SlashX:'\\' (Fx | (( '#$' | '#'+) Fx('#' Fx)*));
slashX:SlashX;
m: MUniqueNum|MHash|X | VoidKW | VarKw | CatchKw | InterfaceKw | IfKw | ElseKw | WhileKw | ForKw | InKw | LoopKw | Throw | WhoopsKw | MethodKw | Mdf;//filtering fwd later on
CsP: C(ClassSep C)*;
ClassSep: '.';
fragment C: IdUp IdChar* ('::'Fn)?;
UnderScore:'_';//need to be not earlier then here, after X and CsP
OR:' ('| ',(' | '\n(';
ORNS:'(';
Doc: '@'FPathSel | '@'FPathSel?'{'DocText'}';
fragment FS:(MUniqueNum|MHash|X) FParXs;
fragment FParXs: '(' ')' | '(' X ( (' '|',')X )* ')';
fragment FPathSel: FS('.'X)? |FParXs('.'X)? | CsP( ('.'FS|FParXs)('.'X)? )?;
fragment DocText: | CHARDocText DocText | Doc DocText | '{' DocText '}' DocText;
doc:Doc;

BlockComment: '/*' (BlockComment|.)*? '*/'	-> channel(HIDDEN) ; // nesting comments allowed
LineComment: '//' .*? ('\n'|EOF)				-> channel(HIDDEN) ;
Whitespace: ( ' ' | ',' | '\n' )-> channel(HIDDEN);

csP: CsP;
t:Mdf? doc* csP;
tLocal: t | Mdf | ; 

//eAtomic: x | csP | voidE | fullL | block | slash | pathSel | slashX;
eAtomic: x | csP | voidE | fullL | slash | pathSel | slashX;
fullL:'{' (header | DotDotDot | ReuseURL) fullM* info? doc*'}';
fullM: fullF | fullMi | fullMWT | fullNC;
fullF: doc* VarKw? t x('=' e)?;
fullMi: doc* MethodKw mOp oR x* ')' '=' e;
fullMWT: doc* fullMH ('=' NativeURL? e)?;
fullNC:  doc* csP '=' e;
header: InterfaceKw? ('['t+']')?;
info: Info;

fullMH: (Mdf doc*)? MethodKw t mOp oR (t x)* ')' ( ('[' UnderScore t* ']')|('[' t+ ']')  )?;
mOp: | m | Uop | (OP0 | OP1 | OP2 | OP3) Number?;//to filter 'number' to be an int
voidE: VoidKW;
//ePostfix: (Uop|Number)* eAtomic (fCall | squareCall | string | cast)*;
ePostfix: (Uop|Number)* ( 
           ( eAtomic (fCall | squareCall | string | cast)* )
           | block 
           |( block ( mCall | squareCall | string | cast) (fCall | squareCall | string | cast)* )
         );
mCall: '.'m ORNS par ')';
fCall: ('.'m)? ORNS par ')';
squareCall: ('.'m)? '[' (par';')* par ']';
cast: CastOp t;
oR: OR |ORNS;
par: e? (x'='e)*;
//Note, just writing [oR d+ k* whoops? d* e ')'] was causing parsing of ( a b+c(d)) as ( a b+c  (d))  
block: oR d*? e k* whoops? ')' | oR d+ k* whoops? e ')' | oR d+ k* whoops? d*? e ')'
  | '{' d+ (k+ whoops? d* | whoops d*)? '}';
d: (dX '=')? e;
dX:VarKw? tLocal x | tLocal UnderScore | tLocal oR (VarKw? tLocal x)+ ')';
k: CatchKw Throw? t x e | CatchKw Throw? t UnderScore e;
whoops: WhoopsKw t+;
eBinary0: ePostfix (OP0 ePostfix)*;
eBinary1: eBinary0 (OP1 eBinary0)*; //left associative, all op the same
eBinary2: eBinary1 ((OP2|InKw) eBinary1)*; //unassociative, all op the same, thus a<b<c could be resolved as a.#left#1(center:b,right:c)
eBinary3: eBinary2 (OP3 eBinary2)*; //left associative, all op the same
sIf: IfKw match+ e | IfKw e e (ElseKw e)?;
match:  x CastOp t | t x '=' e | t? oR (t? x)+')' '=' e;
sWhile: WhileKw e e;
sFor: ForKw (dX InKw e)+ e;
sLoop: LoopKw e;
sThrow:Throw e;
sUpdate: x OpUpdate e;
e: sIf | sWhile | sFor | sLoop | sThrow | sUpdate | eBinary3;
nudeE: e EOF;
nudeP: fullL ((csP '=' '=' )? fullL)* EOF;
nudeCsP:csP EOF;