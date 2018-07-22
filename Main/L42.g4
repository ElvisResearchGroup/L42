
grammar L42;

@header {package antlrGenerated;}
//@lexer::header {package antlrGenerated;}
fragment Uppercase:'A'..'Z'|'%'|'$';
fragment Lowercase:'a'..'z'|'_';
fragment Digit:'0'..'9';
fragment C:Uppercase (Uppercase|Lowercase|Digit)*;


//nice idea, but there must be a bug in antlr... trying to unwrap cathegories
//fragment CharsId :'A'..'Z'|'a'..'z'|'0'..'9';
//fragment CharsOp :'&'|'|'|'*'|'+'|'-'|'='|'/';
//fragment CharsPar :'('|')'|'{'|'}'|'['|']'|'<'|'>';
//fragment CharsPunct :'!'|'?'|';'|':'|','|'.'|' ';
//fragment CharsSpecial : '~'|'@'|'#'|'$'|'%'|'`'|'^'|'_'|'\\';
//fragment CharsAll: CharsId|CharsOp|CharsPar|CharsPunct|CharsSpecial;
//fragment CharsUrl: CharsId|CharsOp|CharsPunct|CharsSpecial
//    |'('|')'|'['|']'|'<'|'>'|' '|'\"'|'\'';
fragment CharsAll:
 '\t'| 'A'..'Z'|'a'..'z'|'0'..'9' | '(' | ')' | '[' | ']' | '<' | '>' |'&'|'|'|'*'|'+'|'-'|'=' | '/' | '!' | '?' | ';' | ':' | ',' | '.' | ' ' | '~' | '@' | '#' | '$' | '%' | '`' | '^' | '_' | '\\' | '{' | '}' ;
fragment CharsUrl:
 '\t'| 'A'..'Z'|'a'..'z'|'0'..'9' | '(' | ')' | '[' | ']' | '<' | '>' |'&'|'|'|'*'|'+'|'-'|'=' | '/' | '!' | '?' | ';' | ':' | ',' | '.' | ' ' | '~' | '@' | '#' | '$' | '%' | '`' | '^' | '_' | '\\'  ;
fragment CharsAllStrLine:
 '\t'| 'A'..'Z'|'a'..'z'|'0'..'9' | '(' | ')' | '[' | ']' | '<' | '>' |'&'|'|'|'*'|'+'|'-'|'=' | '/' | '!' | '?' | ';' | ':' | ',' | '.' | ' ' | '~' | '@' | '#' | '$' | '%' | '`' | '^' | '_' | '\\' | '{' | '}' | '\"' | '\'' ;
fragment CharsAllString:
 '\t'| 'A'..'Z'|'a'..'z'|'0'..'9' | '(' | ')' | '[' | ']' | '<' | '>' |'&'|'|'|'*'|'+'|'-'|'=' | '/' | '!' | '?' | ';' | ':' | ',' | '.' | ' ' | '~' | '@' | '#' | '$' | '%' | '`' | '^' | '_' | '\\' | '{' | '}' | '\'' ;
fragment DocLine: '//' CharsAllStrLine* '\n'; // tab not valid in 42, used to encode ( with space in initial rewriting
fragment DocMultiLine:   '/*' (CharsAllStrLine|'\n')*?  '*/' ;// can be that *? is the non greedy *
//fragment DocMultiLine:   '/*' .*?  '*/' ;// can be that *? is the non greedy *
fragment StrLine: '\'' CharsAllStrLine* '\n'; // tab not valid in 42, used to encode ( with space in initial rewriting
fragment String: CharsAllString*;
//fragment Num:Digit|'-'|'.';
//fragment Num1:'0'|('1'..'9')Digit*;
//fragment NumLast:Digit;
fragment NumNext:Digit|'-'Digit|'.'Digit;
//fragment NumFirst:Digit|'-'Digit;


SRE:'return';
SEX:'exception';
SER:'error';
Mdf:'class'|'mut'|'read'|'lent'|'capsule';
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
Ph:'fwd';
Implements:'implements';
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
Using:'use';
Check:'check';
Refine:'refine';
FieldSpecial:'##field';
WalkBy:'##walkBy';
Stage:'##less'|'##meta'|'##plus'|'##star'|'##needable'|'##needed';
Path://    Outer ('::'C)* |
  C(ClassSep C)*;//| 'Any' | 'Void' | 'Library';
ClassSep: '.';
MX:Lowercase (Uppercase|Lowercase|Digit|'#')*'(';
X:Lowercase (Uppercase|Lowercase|Digit|'#')*;
HashX:'#'  (Uppercase|Lowercase|Digit|'#')*'(';
HashQX:'#?'  (Uppercase|Lowercase|Digit|'#')*'(';
ContextId: '\\'  (Uppercase|Lowercase|Digit|'#')*;
//void is a particular X, syntactically

StringQuote: '"' String '"' | '"' (' ' | ',')* '\n' ((' ' | ',')*StrLine)+ (' ' | ',')* '"';
UrlNL: 'reuse' ' '+ CharsUrl+'\n';
Url: 'reuse' ' '+ CharsUrl+;
//UrlNL: '##urlNL';
//Url: '##url';
Doc: (DocLine WS?)+ | DocMultiLine WS?;
WS:
    (( ' ' | ',' | '\n' )+)-> channel(HIDDEN);

UnOp:  '~'  |  '!'  ;
EqOp:   '+='  |  '-='  |  '*='  |  '/='  |  '&='  |  '|='  |  ':='  |  '++='  |  '**='  |  '--=' |'<><='|'><>='     ;
BoolOp:  '&'  |  '|'  ;
RelOp:  '<'  |  '>'  |  '=='  |  '<='  |  '>='  |  '<<'  |  '>>'  |  '<<='  |  '>>=' |  
        '!<'  |  '!>'  |  '!='  |  '!<='  |  '!>='  |  '!<<'  |  '!>>'  |  '!<<='  |  '!>>=';
DataOp:  '+'  |  '-'  | '--'  | '*'  |  '/'  |  '++'  |  '**'  |'<><'|'><>';

NumParse:Digit NumNext*;
m: MX |HashX;
mDec:m
  | UnOp ORoundNoSpace|EqOp ORoundNoSpace|BoolOp ORoundNoSpace|RelOp ORoundNoSpace|DataOp ORoundNoSpace
  | UnOp ORoundSpace|EqOp ORoundSpace|BoolOp ORoundSpace|RelOp ORoundSpace|DataOp ORoundSpace  ;

path:Path;
docs: Doc;
docsOpt: Doc?;
t: Ph? Mdf? Path docsOpt;
methSelector: (mDec| ORoundNoSpace|ORoundSpace) x* CRound;

x: X;
sS: SRE|SEX|SER;
sEx:   ')' SEX t+;
xOp:X EqOp docsOpt eTop;
//unOp:UnOp;
//binOp:BinOp;
eTopForMethod:eTop | roundBlockForMethod (squareW|square|Dot mCall|ORoundNoSpace round|docs | stringParse)*;
eTop:
    eL3 ( BoolOp docsOpt eL3)*;
eL3:
    eL2 ( RelOp docsOpt eL2)*;
eL2:
    eL1 ( DataOp docsOpt eL1)*;
eL1:
    eUnOp;
numParse: NumParse;
eUnOp: UnOp? /*numParse?*/ ePost;
ePost: eAtom  (squareW|square|Dot mCall|ORoundNoSpace round|docs | stringParse)*;
eAtom:classBReuse|classB|numParse? x | xOp|numParse? Path|numParse? block 
  |ifExpr|whileExpr| signalExpr| loopExpr |WalkBy | w |using | DotDotDot
  | mxRound | hqRound| useSquare|numParse? contextId;
mxRound: MX round;
hqRound: HashQX round;
contextId: ContextId;
useSquare: Using square | Using squareW;
ifExpr: If eTop block (Else eTop)?;
using:Using Path Check mCall eTop;
whileExpr: While eTop block;
signalExpr: sS eTop;
loopExpr: Loop eTop;
block: roundBlock|curlyBlock;
roundBlockForMethod:ORoundNoSpace docsOpt bb* eTop CRound;
roundBlock:ORoundSpace docsOpt bb* eTop CRound;//last one must be eTop
bb:d+ ks;//clearly ambigus, why do not complain?
curlyBlock:OCurly docsOpt bb+ CCurly;
varDec: Var? t? x Equal eTop;
d: varDec| eTop| nestedClass;
stringParse: StringQuote;
//square:OSquare  docsOpt (ps Semicolon docsOpt)* CSquare;
square:OSquare  docsOpt (ps Semicolon docsOpt)* ps CSquare;
squareW:OSquare docsOpt w CSquare;
mCall: m round;
round: docsOpt ps CRound;
ps:(eTop )?(X Colon  eTop )*;
k1: Catch sS t X  eTop;
kMany:Catch sS t+  eTop;
kProp:sS On t+  eTop;
k:k1|kMany|kProp;
ks:k*;//other variations to come (k|kMany|kProp)*

on: On t (Case eTop)? eTop;
onPlus: On t+ (Case eTop)? eTop | Case eTop eTop;
nudeE: eTop EOF;
classBExtra:Stage? Path* EndType;
classBReuse: OCurly docsOpt  Url CCurly |OCurly docsOpt UrlNL docsOpt member+ CCurly;
classB:OCurly docsOpt header impls? fieldDec* docsOpt member* CCurly classBExtra?;
impls:Implements t+;
mhs: Method docsOpt methSelector;
mht:
  (Refine? Mdf? Method docsOpt t (mDec |ORoundNoSpace|ORoundSpace) (t x docsOpt)* sEx)
 |(Refine? Mdf? Method docsOpt t (mDec |ORoundNoSpace|ORoundSpace) (t x docsOpt)* CRound docsOpt);
member: methodWithType|methodImplemented|nestedClass;
methodWithType: mht eTopForMethod?;//was block?
methodImplemented: mhs eTopForMethod;
nestedClass: Path Colon docsOpt eTop;
header:| Interface; //|Mdf? (mDec |ORoundSpace|ORoundNoSpace) fieldDec* CRound;
fieldDec:Var? t x docsOpt;

w: wSwitch|wSimple;
wSwitch:With x* i* varDec* ORoundSpace onPlus+ (Default eTop)? CRound;
i: Var? t? x In eTop;
wSimple: With i+ block;
