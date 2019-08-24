grammar L42Aux;
@header {package is.L42.generated;}
Dot: '.';
TDStart:'@@';
ThisKw: 'This' Fn?;
AnyKw:'Any';
VoidKw:'Void';
LibraryKw:'Library';
anyKw:AnyKw;
voidKw:VoidKw;
libraryKw:LibraryKw;
thisKw:ThisKw;
C:IdUp IdChar* ('::'Fn)?;
c:C;
csP:(path|cs|anyKw|voidKw|libraryKw);
nudeCsP: csP EOF;
path: thisKw (Dot c)*;
cs: c (Dot c)*;
selector: m? '(' (x W*)* ')';
pathSel:csP (Dot? selector)? | selector;
pathSelX:pathSel (Dot x)?;

infoNorm:'#norm{';
infoTyped:'#typed{';

info: (infoNorm|infoTyped) infoBody* '}';
infoBody: typeDep|coherentDep|friends|usedMethods|privateSubtypes|refined|canBeClassAny;
typeDep: 'typeDep='(W* path)+ W*;
coherentDep: 'coherentDep='(W* path)+ W*;
friends: 'friends='(W* path)+ W*;
usedMethods: 'usedMethods='(W* pathSel)+ W*;
privateSubtypes: 'privateSubtypes='(W* path)+ W*;
refined: 'refined='(W* selector)+ W*;
canBeClassAny: 'canBeClassAny';


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
x: X;
m: MUniqueNum|MHash|X;
//UnderScore:'_';//need to be not earlier then here, after X and CsP
Doc: '@'FPathSel | '@'FPathSel?'{'DocText'}';
fragment FS:(MUniqueNum|MHash|X) FParXs;
fragment FParXs: '(' ')' | '(' X ( (' '|',')X )* ')';
fragment FPathSel: FS('.'X)? |FParXs('.'X)? | C(Dot C)*( ('.'FS|FParXs)('.'X)? )?;
fragment DocText: | CHARDocText DocText | Doc DocText | '{' DocText '}' DocText;

W: ( ' ' | ',' | '\n' );

CHARInDoc:
'A'..'Z'|'a'..'z'|'0'..'9' | '(' | ')' | '[' | ']' | '<' | '>' |'&'|'|'|'*'|'+'|'-'|'=' | '/' | '!' | '?' | ';' | ':' | '~' | '#' | '$' | '%' | '`' | '^' | '_' | '\\' | '"' | '\''; //no { } . @ W

charInDoc: CHARInDoc | MUniqueNum | MHash | X | Dot | ThisKw | C | AnyKw | VoidKw | LibraryKw | W; 

topDoc:TDStart pathSelX EOF | TDStart pathSelX? '{' topDocText '}' EOF;
topDocText:charInDoc* | charInDoc* doc topDocText | charInDoc* '{' topDocText '}' topDocText;
doc:Doc;

