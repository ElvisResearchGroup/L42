grammar L42;
@header {package is.L42.generated;}
fragment IdUp: '_'* ('A'..'Z'|'$');
fragment IdLow: '_'* 'a'..'z';
fragment IdChar: 'a'..'z' | 'A'..'Z' | '$' | '_' | '0'..'9';
fragment CHAR:
'A'..'Z'|'a'..'z'|'0'..'9' | '(' | ')' | '[' | ']' | '<' | '>' |'&'|'|'|'*'|'+'|'-'|'=' | '/' | '!' | '?' | ';' | ':' | ',' | '.' | ' ' | '~' | '@' | '#' | '$' | '%' | '`' | '^' | '_' | '\\' | '{' | '}' | '"' | '\'' | '\n';
fragment CHARInStringSingle:
'A'..'Z'|'a'..'z'|'0'..'9' | '(' | ')' | '[' | ']' | '<' | '>' |'&'|'|'|'*'|'+'|'-'|'=' | '/' | '!' | '?' | ';' | ':' | ',' | '.' | ' ' | '~' | '@' | '#' | '$' | '%' | '`' | '^' | '_' | '\\' | '{' | '}' |         '\'';//no \n and "
fragment CharsUrl:
'A'..'Z'|'a'..'z'|'0'..'9' | '(' | ')' | '[' | ']' | '<' | '>' |'&'|'|'|'*'|'+'|'-'|'=' | '/' | '!' | '?' | ';' | ':' | ',' | '.' | ' ' | '~' | '@' | '#' | '$' | '%' | '`' | '^' | '_' | '\\'  ;
fragment URL:CharsUrl+;
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
OR:' ('| ',(' | '\n(';
ORNS:'(';
BlockComment : '/*' (BlockComment|.)*? '*/'	-> channel(HIDDEN) ; // nesting comments allowed
LineComment : '//' .*? ('\n'|EOF)				-> channel(HIDDEN) ;
Whitespace :(( ' ' | ',' | '\n' )+)-> channel(HIDDEN);

eAtomic: x | csP | voidE | block;//| LL | B | '('T e')' | '\'  | '\'' PathLit;
csP: CsP;
voidE: 'void';
e: eAtomic |e fCall;
fCall: ORNS par ')';
oR: OR |ORNS;
par: e? (x'='e)*;
block: oR d* e ')' ;//| '(' D+ K* WOPS? (D* e)? ')' | '{' D* (D K+ WOPS? D*)? '}'
d: e;//(DX '=')? e;
nudeE: e EOF;