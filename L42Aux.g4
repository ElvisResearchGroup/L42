grammar L42Aux;
@header {package is.L42.generated;}

Dot: '.';
TDStart:'@@';
InfoNorm:'#norm{';
InfoTyped:'#typed{';
TypeDep:'typeDep=';
CoherentDep:'coherentDep=';
MetaCoherentDep:'metaCoherentDep=';
Watched:'watched=';
UsedMethods:'usedMethods=';
HiddenSupertypes:'hiddenSupertypes=';
Refined:'refined=';
Close: 'close';
NativeKind: 'nativeKind=';
NativePar: 'nativePar=';
UniqueId: 'uniqueId=';
ThisKw: 'This' Fn?;
AnyKw:'Any';
VoidKw:'Void';
LibraryKw:'Library';
C:IdUp IdChar* ('::'Fn)?;

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
  fragment Fn: '0' | '1'..'9' ('0'..'9')*;
  fragment Fx: IdLow IdChar*;
  fragment Number: '0'..'9' ('.'|'_'|'-'|'0'..'9')*;

MUniqueNum: Fx('#' Fx)*'::'Fn;
MHash: ('#$' | '#'+) Fx('#' Fx)* ('::'Fn)?;
X: Fx;
ORound:'(';
CRound:')';

OCurly:'{';
CCurly:'}';
W: ( ' ' | ',' | '\n' );

Doc: '@'FPathSel | '@'FPathSel? OCurly DocText CCurly;

  fragment FS:(MUniqueNum|MHash|X) FParXs;
  fragment FParXs: '(' ')' | '(' X ( (' '|',')X )* ')';
  fragment FPathSel: FS('.'X)? |FParXs('.'X)? | C(Dot C)*( ('.'FS|FParXs)('.'X)? )?;
  fragment DocText: | CHARDocText DocText | Doc DocText | '{' DocText '}' DocText;

CHARInDoc:
  'A'..'Z'|'a'..'z'|'0'..'9' | '[' | ']' | '<' | '>' |'&'|'|'|'*'|'+'|'-'|'=' | '/' | '!' | '?' | ';' | ':' | '~' | '#' | '$' | '%' | '`' | '^' | '_' | '\\' | '"' | '\''; //no { } . @ W ( ) and any other token

anyKw:AnyKw;
voidKw:VoidKw;
libraryKw:LibraryKw;
thisKw:ThisKw;
c:C;
csP:(path|cs|anyKw|voidKw|libraryKw);
nudeCsP: csP EOF;
path: thisKw (Dot c)*;
cs: c (Dot c)*;
selector: m? ORound (x W*)* CRound;
selectorCall: (Dot m)? ORound (x W*)* CRound;
pathSel:csP (selectorCall)? | selector;
pathSelX:csP (selectorCall (Dot x)?)? | selector (Dot x)?;
nudePathSelX:pathSelX EOF;

infoNorm:InfoNorm;
infoTyped:InfoTyped;

info: W*(infoNorm|infoTyped)W* (infoBody W*)* CCurly EOF;
infoBody: typeDep|coherentDep|metaCoherentDep|watched|usedMethods|hiddenSupertypes|refined|close|nativeKind|nativePar|uniqueId;
typeDep: TypeDep(W* path)+;
coherentDep: CoherentDep(W* path)+;
metaCoherentDep: MetaCoherentDep (W* path)+;
watched: Watched (W* path)+;
usedMethods: UsedMethods (W* pathSel)+;
hiddenSupertypes: HiddenSupertypes (W* path)+;
refined: Refined (W* selector)+;
close: Close;
nativeKind: NativeKind W* (x|c);
nativePar: NativePar (W* path)+;
uniqueId: UniqueId W* x;


x: X|close;
m: MUniqueNum|MHash|X|close;

charInDoc: 
  CHARInDoc | MUniqueNum | MHash |
  X | Dot | ThisKw | C | AnyKw |
  VoidKw | LibraryKw | W |
  ORound | CRound | OCurly | CCurly | 
  InfoNorm | InfoTyped |
  TypeDep | CoherentDep | MetaCoherentDep |
  Watched | UsedMethods | HiddenSupertypes | Refined | Close |
  NativeKind | NativePar | UniqueId; 

topDoc:TDStart pathSelX EOF | TDStart pathSelX? OCurly topDocText CCurly EOF;
topDocText:charInDoc* | charInDoc* doc topDocText | charInDoc* OCurly topDocText CCurly topDocText;
doc:Doc;