%%

%token>String> QNAME
%token DEMAND IMERATIVE VARPATH WHITESPACE

qname			: QNAME ':' QNAME
				| QNAME

attr_assign : QNAME':' subst_expr

subst_expr	: simple_value_expr
				|  cmpnd_value_expr

simple_subst_expr	: value_expr

cmpnd_subst_expr	: text '{' value_expr '}'
			| cmpnd_subst_expr text '{' value_expr '}'

value_expr	: childof_expr
				| func_call

childof_expr : '#' imper_expr
				| imper_expr

imper_expr 	:  '!' force_expr
				| force_expr

force_expr 	: FORCE path_expr
				| path_expr

test_expr			: test_or_expr

test_or_expr	: test_and_expr
				 	| test_or_expr '|' test_and_expr
				
test_and_expr	: base_test
				 	| test_and_expr WHITESPACE base_test
				
base_test 	: path_expr
				| '!' path_expr

func_call	: qname '(' arglist ')'

arg		: STRING
			| INT
			| value_expr

arglist	: arg
			| arglist ',' arg
		|

path_expr	: basepathexp 

basepathexp	: path pathexpsuffix

path	 		: relpath
				| abspath

abspath		: '/' relpath

relpath		: pathelement
				| relpath '/' pathelement

pathexpsuffix	: '!'  qname
				| pathexpsuffix ':' ...
				|

pathelement : qname
				| VARPATH
				| '*'
				| '.'

text		: TEXT
			|

%%
dx:case="<test_expr>"
dx:if="<test_expr>"
dx:text="<subst_expr>"
dx:attr="<attr_assign> ..."
dx:cattr="(<attr_assign> <test_expr>) ..."
%%


[.] { return '.'; }
/ { return '/'; }
!! { return DEMAND; }
! { return '!'; }
[#] { return '#'; }
[**] { return VARPATH; }
[*] { return '*'; }
[@] { return '@'; }
[:] { return ':'; }

[a-zA-Z][a-zA-Z0-9-]*	{ return QNAME; }
[ \t]+	{ return WHITESPACE; }

[0-9]+	{ return INT; }


