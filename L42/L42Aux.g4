grammar L42Aux;
@header {package is.L42.generated;}

ThisKw: "This"Fn?;
C:IdUp IdChar*;

csP:(path|cs) EOF;
path: ThisKw ('.'C)*;
cs: C ('.'C)*;
selector: m '(' (x W)* ')' 
pathSel:path '.' selector;

infoNorm:'#norm{';
infoTyped:'#typed{';

info: infoNorm|infoTyped infoBody* '}';
infoBody: typeDep|coherentDep|friends|usedMethods|privateSubtypes|refined|canBeClassAny;
typeDep: 'typeDep='(W path)+ W;
coherentDep: 'coherentDep='(W path)+ W;
friends: 'friends='(W path)+ W;
usedMethods: 'usedMethods='(W pathSel)+ W;
privateSubtypes: 'privateSubtypes='(W path)+ W;
refined: 'refined='(W selector)+ W;
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
fragment C: IdUp ('A'..'Z'|'$'|'a'..'z'|'0'..'9')*;
//UnderScore:'_';//need to be not earlier then here, after X and CsP
//Doc: '@'FPathSel | '@'FPathSel?'{'DocText'}';
fragment FS:(MUniqueNum|MHash|X) FParXs;
fragment FParXs: '(' ')' | '(' X ( (' '|',')X )* ')';
fragment FPathSel: FS('.'X)? |FParXs('.'X)?;
fragment DocText: | CHARDocText DocText | Doc DocText | '{' DocText '}' DocText;
//doc:Doc;

W: ( ' ' | ',' | '\n' )*;

nudeE: e EOF;