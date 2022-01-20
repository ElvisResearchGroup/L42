grammar SettingsFile;
@header {package is.L42.generated;}

MSS:'maxStackSize';
MMS:'maxMemorySize';
IMS:'initialMemorySize';
Num: '1'..'9' ('0'..'9')* ('K'|'M'|'G');
Eq:'=';

fragment IdUp: '_'* ('A'..'Z'|'$');
fragment IdChar: 'a'..'z' | 'A'..'Z' | '$' | '_' | '0'..'9';
fragment Fn: '0' | '1'..'9' ('0'..'9')*;
ClassSep: '.';
fragment C: IdUp IdChar* ('::'Fn)?;
CsP: C(ClassSep C)*;

URL:'[' CharsUrl+']';
fragment CharsUrl:
'A'..'Z'|'a'..'z'|'0'..'9' | '(' | ')' | '<' | '>' |'&'|'|'|'*'|'+'|'-'|'=' | '/' | '!' | '?' | ';' | ':' | ',' | '.' | ' ' | '~' | '@' | '#' | '$' | '%' | '`' | '^' | '_' | '\\'  ;

BlockComment: '/*' (BlockComment|.)*? '*/'	-> channel(HIDDEN) ; // nesting comments allowed
LineComment: '//' .*? ('\n'|EOF)				-> channel(HIDDEN) ;
Whitespace: ( ' ' | ',' | '\n' )-> channel(HIDDEN);

memOpt: (MSS|MMS|IMS) Eq Num;
secOpt: CsP Eq (URL)+; 
setting: memOpt | secOpt ;
nudeSettings: setting* EOF;
