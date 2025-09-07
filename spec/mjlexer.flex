package rs.ac.bg.etf.pp1;

import java_cup.runtime.Symbol;

%%

%{
	private Symbol new_symbol(int type) {
		return new Symbol(type, yyline+1, yycolumn);
	}
	
	private Symbol new_symbol(int type, Object value) {
		return new Symbol(type, yyline+1, yycolumn, value);
	}

%}

%cup
%line
%column

%xstate COMMENT

%eofval{
	return new_symbol(sym.EOF);
%eofval}

%%

" " 	{ }
"\b" 	{ }
"\t" 	{ }
"\r\n" 	{ }
"\f" 	{ }

"program"   { return new_symbol(sym.PROGRAM, yytext()); }
"break"  	{ return new_symbol(sym.BREAK, yytext()); }
"class"  	{ return new_symbol(sym.CLASS, yytext()); }
"else"  	{ return new_symbol(sym.ELSE, yytext()); }
"const"		{ return new_symbol(sym.CONST, yytext()); }
"if"		{ return new_symbol(sym.IF, yytext()); }
"new" 		{ return new_symbol(sym.NEW, yytext()); }
"print" 	{ return new_symbol(sym.PRINT, yytext()); }
"read" 		{ return new_symbol(sym.READ, yytext()); }
"return" 	{ return new_symbol(sym.RETURN, yytext()); }
"void" 		{ return new_symbol(sym.VOID, yytext()); }
"extends" 	{ return new_symbol(sym.EXTENDS, yytext()); }
"continue" 	{ return new_symbol(sym.CONTINUE, yytext()); }
"union" 	{ return new_symbol(sym.UNION, yytext()); }
"do"		{ return new_symbol(sym.DO, yytext()); }
"while" 	{ return new_symbol(sym.WHILE, yytext()); }
"map" 		{ return new_symbol(sym.MAP, yytext()); }
"interface" { return new_symbol(sym.INTERFACE, yytext()); }

"'"[ -~]"'" { return new_symbol(sym.CHAR_CONST, yytext().charAt(1)); }
"true" 		{ return new_symbol(sym.BOOL_CONST, 1); }
"false" 	{ return new_symbol(sym.BOOL_CONST, 0); }

// Operators
<YYINITIAL> "+" 		{ return new_symbol(sym.PLUS, yytext()); }
"-" 		{ return new_symbol(sym.MINUS, yytext()); }
"*" 		{ return new_symbol(sym.MULTIPLY, yytext()); }
"/" 		{ return new_symbol(sym.DIVIDE, yytext()); }
"%" 		{ return new_symbol(sym.MODUO, yytext()); }

"==" 		{ return new_symbol(sym.EQUAL, yytext()); }
"!=" 		{ return new_symbol(sym.NOT_EQUAL, yytext()); }
">" 		{ return new_symbol(sym.GREATER, yytext()); }
">=" 		{ return new_symbol(sym.GREATER_EQUAL, yytext()); }
"<" 		{ return new_symbol(sym.LESS, yytext()); }
"<=" 		{ return new_symbol(sym.LESS_EQUAL, yytext()); }

"&&" 		{ return new_symbol(sym.AND, yytext()); }
"||" 		{ return new_symbol(sym.OR, yytext()); }

"=" 		{ return new_symbol(sym.ASSIGNMENT, yytext()); }

"++" 		{ return new_symbol(sym.INC, yytext()); }
"--" 		{ return new_symbol(sym.DEC, yytext()); }

";" 		{ return new_symbol(sym.SEMI, yytext()); }
":" 		{ return new_symbol(sym.COLON, yytext()); }
"," 		{ return new_symbol(sym.COMMA, yytext()); }
"." 		{ return new_symbol(sym.POINT, yytext()); }

"(" 		{ return new_symbol(sym.LEFT_ROUND_BRACKET, yytext()); }
")" 		{ return new_symbol(sym.RIGHT_ROUND_BRACKET, yytext()); }
"[" 		{ return new_symbol(sym.LEFT_SQUARE_BRACKET, yytext()); }
"]" 		{ return new_symbol(sym.RIGHT_SQUARE_BRACKET, yytext()); }
"{" 		{ return new_symbol(sym.LEFT_CURLY_BRACKET, yytext()); }
"}"			{ return new_symbol(sym.RIGHT_CURLY_BRACKET, yytext()); }

// by default every state starts with YYINITIAL so no need to write it down.
<YYINITIAL> "//" { yybegin(COMMENT); } // after this line we are in COMMENT state
<COMMENT> .      { yybegin(COMMENT); } // any character comes, we stay in COMMENT state
<COMMENT> "\r\n" { yybegin(YYINITIAL); } // when new line char comes, we get back to original YYINITIAL state

[0-9]+  { return new_symbol(sym.NUMBER, Integer.parseInt(yytext())); }
([a-z]|[A-Z])[a-z|A-Z|0-9|_]* 	{return new_symbol (sym.IDENT, yytext()); }

. { System.err.println("Leksicka greska ("+yytext()+") u liniji "+(yyline+1)+ " na poziciji " + (yycolumn + 1)); }






