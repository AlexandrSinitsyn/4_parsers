:- load_library('alice.tuprolog.lib.DCGLibrary').

:- (op(1200, xfy, '>>>')).
:- (op(400, xfx, 'var')).
:- (op(700, fx, '#')).
'#'(X) --> [X].


evaluate(const(N), _, N).

evaluate(variable(Name), [(NamePrototype, R) | T], R) :- atom_chars(Name, [FirstLetter | _]), FirstLetter = NamePrototype, !.
evaluate(variable(Name), [(_, _) | T], R) :- evaluate(variable(Name), T, R).


evaluate(operation(Op, X), Variables, Result) :- evaluate(X, Variables, R), evaluate(Op, R, Result).

evaluate(operation(Op, A, B), Variables, Result) :- evaluate(A, Variables, X), evaluate(B, Variables, Y), evaluate(Op, X, Y, Result).

evaluate(op_add, A, B, R) :- R is A + B.
evaluate(op_subtract, A, B, R) :- R is A - B.
evaluate(op_multiply, A, B, R) :- R is A * B.
evaluate(op_divide, A, B, R) :- R is A / B.

evaluate(op_negate, X, Result) :- Result is -X.
evaluate(op_sinh, X, Result) :- Result is (exp(X) - exp(-X)) / 2.
evaluate(op_cosh, X, Result) :- Result is (exp(X) + exp(-X)) / 2.


get_op(Op, R) :- op_symbol(Op, Name), atom_chars(Name, R).
'>>>'(A, R) :- A var B, R =.. [skip_spaces, B].

parse_bin(Op) --> { Op >>> Skp_sp, get_op(Op, R) }, #' ', Skp_sp, R, #' '.
parse_un(Op) --> { Op >>> Skp_sp, get_op(Op, R) }, Skp_sp, R, Skp_sp.

op_symbol(op_add, '+').
op_symbol(op_subtract, '-').
op_symbol(op_multiply, '*').
op_symbol(op_divide, '/').
op_symbol(op_sinh, 'sinh').
op_symbol(op_cosh, 'cosh').

op_symbol(op_negate, 'negate').


skip_spaces(fail) --> [].
skip_spaces(true) --> [].
skip_spaces(true) --> #' ', skip_spaces(true).


var(E, true) :- var(E).
var(E, fail) :- nonvar(E).

parse_expr(variable(Name)) --> { nonvar(Name), atom_chars(Name, Chars) }, Chars.
parse_expr(variable(Name)) --> { var(Name), Name >>> Skp_sp }, Skp_sp, parser_name(Chars), Skp_sp, { Chars = [_ | _], atom_chars(Name, Chars) }.
parser_name([]) --> [].
parser_name([H | T]) -->
    { atom_chars('xyzXYZ', Available), member(H, Available) },
    #H, parser_name(T).


parse_expr(const(Value)) --> { nonvar(Value), number_chars(Value, Chars) }, Chars.
parse_expr(const(Value)) -->
    { var(Value), Value >>> Skp_sp },
    Skp_sp, parse_number(Chars), Skp_sp,
    { Chars = [_, _ | _], number_chars(Value, Chars) }.

parse_number(['-' | T]) --> #'-', parse_digit(T).
parse_number(['+' | T]) --> #'+', parse_digit(T).
parse_number(List) --> parse_digit(List).

parse_digit([]) --> [].
parse_digit([H | T]) -->
    { member(H, ['.', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'e', 'E']) },
    #H, parse_digit(T).


parse_expr(operation(Op, A, B)) --> { Op >>> Skp_sp }, Skp_sp, #'(', parse_expr(A), parse_bin(Op), parse_expr(B), #')', Skp_sp.

parse_expr(operation(Op, A)) --> { Op >>> Skp_sp }, parse_un(Op), Skp_sp, #'(', parse_expr(A), #')', Skp_sp.


infix_str(E, A) :- ground(E), phrase(parse_expr(E), C), atom_chars(A, C), !.
infix_str(E, A) :-   atom(A), atom_chars(A, C), phrase(parse_expr(E), C), !.
