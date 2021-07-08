(load-file "expression.clj")


(def separator #(println (apply str (repeat 100 "-"))))

(separator)

(tabulate-exceptions parseObjectInfix ["10" "10.0" "1." "1," "." ".0" ""])

(separator)

(tabulate-exceptions parseObjectInfix ["x" "y" "z" "xyz" "a" "abc" "xbc" "hello" "ш"])

(separator)

(tabulate-exceptions parseObjectInfix ["+1" "+ 1" "1+" "1+x" "1++1" "1++x" "1.1" "1.x" "1 & x" "x&3" "&3" "1 + a" "a + 1"])

(separator)

(tabulate-exceptions parseObjectInfix ["(" ")" "(x)" "((x)" "()" "(()" "())" "(((((x)))))" "(((x + x + x)))" "((((x) + x) + x))" "(( + ((x))))" "((((x)) + ))" "((((((x + 1" "x + 1))))))"])

(separator)
