
grammar L42;

@header {package antlrGenerated;}
//@lexer::header {package antlrGenerated;}
S:
    'return'|'error'|'exception';
Mdf:
    'type'|'mut'|'read'|'lent'|'capsule';
ORoundNoSpace:'(';
ORoundSpace:'\t';
CRound:')';
OSquare:'[';
CSquare:']';
OCurly:'{';
DotDotDot:'...';
EndType:'^##';
CCurly:'}';
Colon:':';
Semicolon:';';
Dot:'.';
Equal:'=';
Ph:'^';
Implements:'<:';
Case:'case';
If:'if';
Else:'else';
While:'while';
Loop:'loop';
With:'with';
On:'on';
In:'in';
Catch:'catch';
Var:'var';
Default:'default';
Interface:'interface';
Method:'method';
Using:'using';
Check:'check';
FieldSpecial:'##field';
WalkBy:'##walkBy';
Stage:'##less'|'##meta'|'##plus'|'##star'|'##needable'|'##needed';
fragment Uppercase:'A'..'Z'|'%'|'$';
fragment Lowercase:'a'..'z'|'_';
fragment Digit:'0'..'9';
fragment C:Uppercase (Uppercase|Lowercase|Digit)*;
Path://    Outer ('::'C)* |
  C(ClassSep C)*;//| 'Any' | 'Void' | 'Library';
ClassSep: '::';
X:Lowercase (Uppercase|Lowercase|Digit|'#')*;
HashX:'#' Lowercase (Uppercase|Lowercase|Digit|'#')*;
//void is a particular X, syntactically
fragment CharsId :'A'..'Z'|'a'..'z'|'0'..'9';
fragment CharsOp :'&'|'|'|'*'|'+'|'-'|'='|'/';
fragment CharsPar :'('|')'|'{'|'}'|'['|']'|'<'|'>';
fragment CharsPunct :'!'|'?'|';'|':'|','|'.'|' ';
fragment CharsSpecial : '~'|'@'|'#'|'$'|'%'|'`'|'^'|'_'|'\\';
fragment CharsAll: CharsId|CharsOp|CharsPar|CharsPunct|CharsSpecial;
fragment CharsUrl: CharsId|CharsOp|CharsPunct|CharsSpecial
    |'('|')'|'['|']'|'<'|'>'|' '|'\"'|'\'';
fragment StrLine: '\'' (CharsAll|'\"'|'\'')*'\n'; // tab not valid in 42, used to encode ( with space in initial rewriting
fragment String: (CharsAll|'\'')*;
//fragment Num:Digit|'-'|'.';
//fragment Num1:'0'|('1'..'9')Digit*;
//fragment NumLast:Digit;
fragment NumNext:Digit|'-'Digit|'.'Digit;
//fragment NumFirst:Digit|'-'Digit;
StringQuote: '"' String '"' | '"' (' ' | ',')* '\n' ((' ' | ',')*StrLine)+ (' ' | ',')* '"';
UrlNL: 'reuse' ' '+ CharsUrl+'\n';
Url: 'reuse' ' '+ CharsUrl+;
//UrlNL: '##urlNL';
//Url: '##url';
Doc: (StrLine WS?)+;
WS:
    (( ' ' | ',' | '\n' )+)-> channel(HIDDEN);

UnOp:'~'|'!';
EqOp:'+='|'-='|'*='|'/='|'&='|'|='|':='|'<<='|'>>='|'++='|'**=';
BoolOp:'&'|'|';
RelOp:'<'|'>'|'=='|'<='|'>='|'!=';
DataOp:'+'|'-'|'*'|'/'|'<<'|'>>'|'++'|'**';

NumParse:Digit NumNext*;
m: X |HashX;
mDec:m| UnOp|EqOp|BoolOp|RelOp|DataOp;

path:Path;
docs: Doc;
docsOpt: Doc?;
t:concreteT|historicalT;
concreteT:Mdf? Path Ph?;
historicalSeq:ClassSep methSelector (ClassSep x)?;
historicalT:Path historicalSeq+ Ph?;
//t:Mdf? Path Ph? (Dot methSelector)*;
methSelector: mDec? (ORoundNoSpace|ORoundSpace) x* CRound;
//ws:WS?;

x: X;
xOp:X EqOp eTop;
//unOp:UnOp;
//binOp:BinOp;
eTopForMethod:eTop | roundBlockForMethod (squareW|square|Dot mCall|round|docs | stringParse)*;
eTop:
    eL3 ( BoolOp  eL3)*;
eL3:
    eL2 ( RelOp  eL2)*;
eL2:
    eL1 ( DataOp eL1)*;
eL1:
    eUnOp;
numParse: DataOp? NumParse;
eUnOp: UnOp? /*numParse?*/ ePost;
ePost: eAtom  (squareW|square|Dot mCall|round|docs | stringParse)*;
eAtom:classBReuse|classB|numParse? x | xOp|numParse? Path|numParse? block|ifExpr|whileExpr| signalExpr| loopExpr |WalkBy | w |using | DotDotDot;
ifExpr: If eTop block (Else eTop)?;
using:Using Path Check mCall eTop;
whileExpr: While eTop block;
signalExpr: S eTop;
loopExpr: Loop eTop;
block: roundBlock|curlyBlock;
roundBlockForMethod:ORoundNoSpace docsOpt bb* eTop CRound;
roundBlock:ORoundSpace docsOpt bb* eTop CRound;//last one must be eTop
bb:d+ k?;//clearly ambigus, why do not complain?
curlyBlock:OCurly docsOpt bb+ CCurly;
varDec: Var? t? x Equal eTop;
d: varDec| eTop| nestedClass;
stringParse: StringQuote;
//square:OSquare  docsOpt (ps Semicolon docsOpt)* CSquare;
square:OSquare  docsOpt (ps Semicolon docsOpt)* ps CSquare;
squareW:OSquare docsOpt w CSquare;
mCall: m round;
round:ORoundNoSpace docsOpt ps CRound;
ps:(eTop )?(X Colon  eTop )*;
k: Catch S X? (ORoundSpace|ORoundNoSpace) on* (Default eTop)? CRound;   
on: On t (Case eTop)? eTop;
onPlus: On t+ (Case eTop)? eTop | Case eTop eTop;
nudeE: eTop EOF;
classBExtra:Stage? Path* EndType;
classBReuse: OCurly docsOpt  Url CCurly |OCurly docsOpt UrlNL docsOpt member+ CCurly;
classB:OCurly docsOpt header Implements? Path* docsOpt member* CCurly classBExtra?;
mhs: Method docsOpt methSelector (EndType Path mht)?;
mht: Mdf? Method docsOpt t mDec? (ORoundNoSpace|ORoundSpace) (t x docsOpt)* CRound (S Path+)?;
member: methodWithType|methodImplemented|nestedClass;
methodWithType: mht (EndType Path)? docsOpt eTopForMethod?| mht FieldSpecial;//was block?
methodImplemented: mhs eTopForMethod;
nestedClass: Path Colon docsOpt eTop;
header:| Interface |Mdf? mDec? (ORoundSpace|ORoundNoSpace) fieldDec* CRound;
fieldDec:Var? t x docsOpt;

w: wSwitch|wSimple;
wSwitch:With x* i* varDec* ORoundSpace onPlus+ (Default eTop)? CRound;
i: Var? t? x In eTop;
wSimple: With i+ block;
//C:\Users\marco\Desktop\EclipseLuna4.4.2\L42_2015\Main\src\antlrGenerated
//C:\Users\marco\Desktop\latexHelper\disiFiles\marco\L42_2015\Main\src\antlrGenerated
//C:\Users\Marco\workspace\TestJ8\src\antlrGenerated
//C:\Users\marco\Desktop\latexHelper\disiFiles\marco\java8Lombock\src\antlrGenerated
//C:\Users\marco\Desktop\latexHelper\disiFiles\marco\java8Alternate\src\antlrGenerated