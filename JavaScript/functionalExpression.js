"use strict";

const availableVars = {
    "x": 0,
    "y": 1,
    "z": 2
}

const cnst      = v => () => v;
const variable  = name => {
    const index = availableVars[name];

    return (...vars) => vars[index];
}

const one       = cnst(1);
const two       = cnst(2);

const fun       = f => (...args) => (...vars) => f(...args.map(arg => arg(...vars)));

const add       = fun((a, b) => a + b);
const subtract  = fun((a, b) => a - b);
const multiply  = fun((a, b) => a * b);
const divide    = fun((a, b) => a / b);
const min5      = fun(Math.min)
const max3      = fun(Math.max)
const negate    = fun(e => -e);


const operation = {
    "+": [add, 2],
    "-": [subtract, 2],
    "*": [multiply, 2],
    "/": [divide, 2],
    "negate": [negate, 1],

    "min5": [min5, 5],
    "max3": [max3, 3]
}

const consts = {
    "one": one,
    "two": two
}

const parse = input => {
    const stack = [];

    for (const expr of input.trim().split(/\s+/)) {
        if (expr in operation) {
            const [op, arity] = operation[expr];

            stack.push(op(...stack.splice(-arity)));
        } else if (expr in consts) {
            stack.push(consts[expr])
        } else if (expr in availableVars) {
            stack.push(variable(expr));
        } else {
            stack.push(cnst(+expr))
        }
    }

    return stack[0];
}


const myExpr = parse("x x * 2 x * - 1 +")

for (let i = 0; i <= 10; i++) {
    println(myExpr(i))
}

const testExpr = divide(variable('x'), divide(divide(cnst(705580), variable('x')), cnst(0)))
println(testExpr(0.8))
