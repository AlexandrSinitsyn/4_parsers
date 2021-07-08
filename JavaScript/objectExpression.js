"use strict";

const expressions = (function () {
    function Expression(...args) {
        this.args = args;
    }

    function getStringByFunction(args, methodName) { return args.map(arg => arg[methodName]()).join(" ") }

    Expression.prototype = {
        constructor: Expression,
        evaluate: function (x, y, z) {},
        diff: function (v) {}
    }


    function factory(evaluate, diff) {
        function Expr(...args) {
            Expression.call(this, ...args);
        }
        Expr.prototype = Object.create(Expression);
        Expr.prototype.toString =
            Expr.prototype.prefix =
                Expr.prototype.postfix = function () { return getStringByFunction(this.args, "toString") };
        Expr.prototype.evaluate = evaluate;
        Expr.prototype.diff = diff;
        Expr.prototype.simplify = function () { return this };

        return Expr;
    }


    function operationFactory(symbol, fn, diff, simplifyAbsorbing, simplifyNeutral) {
        const Expr = factory(
            function (x, y, z) { return fn(...this.args.map(arg => arg.evaluate(x, y, z))) },
            function (v) { return diff(...this.args, ...this.args.map(arg => arg.diff(v))) }
        );
        Expr.prototype.toString = function () { return getStringByFunction(this.args, "toString") + " " + symbol };
        Expr.prototype.prefix = function () { return `(${symbol} ${getStringByFunction(this.args, "prefix")})` };
        Expr.prototype.postfix = function () { return `(${getStringByFunction(this.args, "postfix")} ${symbol})` };
        Expr.arity = fn.length;

        Expr.prototype.simplify = function () {
            this.args = this.args.map(arg => arg.simplify())

            if (this.args.every(arg => arg instanceof Const)) {
                return new Const(fn(...this.args.map(arg => arg.evaluate())))
            } else if (fn.length === 1) {
                return new Expr(...this.args);
            } else if (fn.length > 1) {
                const absorbing = simplifyAbsorbing
                const neutral = simplifyNeutral

                let absorbingFound = false

                this.args = this.args.filter((arg, index) => {
                    if (arg instanceof Const) {
                        const number = arg.evaluate();

                        if (absorbing[1] && number === absorbing[0]) {
                            absorbingFound = true;
                            return true;
                        }

                        if (number === neutral[0] && ((index === 0 && neutral[1]) || index !== 0)) {
                            return false;
                        }
                    }

                    return true;
                });

                if (absorbingFound) {
                    return Const.ZERO;
                }
            }

            if (this.args.length === 1) {
                return this.args[0];
            }

            return new Expr(...this.args);
        };

        operation.set(symbol, Expr);

        return Expr;
    }


    return {
        factory: factory,
        operationFactory: operationFactory
    }
})();
const operationFactory = expressions.operationFactory;

const availableNames = new Map([
    ["x", 0],
    ["y", 1],
    ["z", 2]
]);
const Variable  = expressions.factory(
    function (...vars) { return vars[availableNames.get(this.args[0])] },
    function (v) { return v === this.args[0] ? Const.ONE : Const.ZERO }
    );

const Const     = expressions.factory(
    function () { return this.args[0] },
    function () { return Const.ZERO }
    );

Const.ZERO      = new Const(0);
Const.ONE       = new Const(1);
Const.TWO       = new Const(2);
Const.THREE     = new Const(3);

const operation = new Map();

const Add       = operationFactory("+", (a, b) => a + b, (a, b, aDiff, bDiff) => new Add(aDiff, bDiff),
    [0, false], [0, true]);
const Subtract  = operationFactory("-", (a, b) => a - b, (a, b, aDiff, bDiff) => new Subtract(aDiff, bDiff),
    [0, false], [0, false]);
const Multiply  = operationFactory("*", (a, b) => a * b, (a, b, aDiff, bDiff) => new Add(new Multiply(aDiff, b), new Multiply(a, bDiff)),
    [0, true], [1, true]);
const Divide    = operationFactory("/", (a, b) => a / b, (a, b, aDiff, bDiff) => new Divide(new Subtract(new Multiply(aDiff, b), new Multiply(a, bDiff)), new Multiply(b, b)),
    [0, true], [1, false]);
const Sumsq     = operationFactory("sumsq", (...args) => args.reduce((total, cur) => total + cur * cur, 0),
    (...allArgs) => {
        const argsDiff = allArgs.splice(-allArgs.length / 2);

        return allArgs.reduce(
            (total, currentValue, i) => new Add(total, new Multiply(new Multiply(Const.TWO, currentValue), argsDiff[i])),
            Const.ZERO)
    });
const Length    = operationFactory("length", (...args) => Math.sqrt(args.reduce((total, cur) => total + cur * cur, 0)),
    (...allArgs) => {
        const argsDiff = allArgs.splice(-allArgs.length / 2);
        const args = allArgs;

        if (args.length === 0) {
            return Const.ZERO;
        }

        return new Divide(
            args.reduce((total, currentValue, i) => new Add(total, new Multiply(new Multiply(Const.TWO, args[i]), argsDiff[i])),
                Const.ZERO),
            new Multiply(Const.TWO, new Length(...args)));
    });
const Hypot     = operationFactory("hypot", (a, b) => a * a + b * b,
    (a, b, aDiff, bDiff) => new Add(new Multiply(new Multiply(Const.TWO, a), aDiff), new Multiply(new Multiply(Const.TWO, b), bDiff)),
    [0, true], [1, true]);
const HMean     = operationFactory("hmean", (a, b) => 2 / (1 / a + 1 / b),
    (a, b, aDiff, bDiff) => new Divide(
        new Multiply(Const.TWO,
            new Add(new Multiply(new Square(a), bDiff), new Multiply(new Square(b), aDiff))
        ), new Square(new Add(a, b))),
    [0, true], [1, true]);

const Negate    = operationFactory("negate", e => -e, (a, aDiff) => new Negate(aDiff));
const Cube      = operationFactory("cube", e => e * e * e, (a, aDiff) => new Multiply(new Multiply(Const.THREE, new Multiply(a, a)), aDiff));
const Cbrt      = operationFactory("cbrt", Math.cbrt, (a, aDiff) => new Divide(aDiff, new Multiply(Const.THREE, new Cbrt(new Multiply(a, a)))));

const Square    = operationFactory("^2", e => e * e, (a, aDiff) => new Multiply(new Multiply(Const.TWO, a), aDiff));


const parse = input => {
    const stack = [];

    for (const expr of input.trim().split(/\s+/)) {
        if (operation.has(expr)) {
            const op = operation.get(expr);

            stack.push(new op(...stack.splice(-op.arity)));
        } else {
            const number = parseFloat(expr);
            stack.push(isNaN(number) ? new Variable(expr) : new Const(number));
        }
    }

    return stack[0];
};

const errors = (function () {
    function ParserError(message, position) {
        this.message = `Fail with: "${message}" at ${position}`;
        Error.call(this, this.message);
    }
    ParserError.prototype = Object.create(Error.prototype);

    function errorFactory(message, position) {
        function ExpressionError() {
            ParserError.call(this, message, position);
        }
        ExpressionError.prototype = Object.create(ParserError.prototype);

        return ExpressionError;
    }

    return {
        EmptyInputError:        position => errorFactory("empty input", position),
        UnknownVariableError:   position => errorFactory("unknown variable", position),
        InvalidNumberFormat:    position => errorFactory("invalid number format", position),
        UnknownOperationError:  position => errorFactory("unknown operation", position),
        NoOpeningBracketError:  position => errorFactory("no opening bracket", position),
        NoClosingBracketError:  position => errorFactory("no closing bracket", position),
        ExcessiveInfoError:     args => position => errorFactory(`excessive info; count of passed arguments: ${args}`, position),
        MissingInfoError:       args => position => errorFactory(`missing info; count of passed arguments: ${args}`, position),
        NoOperationError:       position => errorFactory("no operation", position)
    }
})();

class CharSource {
    constructor(input, isForward) {
        this.input = input;
        this.isForward = isForward;
        this.length = input.length;
        this.pos = isForward ? 0 : (this.length - 1);
    }

    get() {
        return this.input[this.pos];
    }

    hasNext() {
        return (this.isForward ? this.pos < this.length : this.pos >= 0);
    }

    next() {
        if (this.hasNext()) {
            this.pos += this.isForward ? 1 : -1;
        }
    }

    getToken() {
        let res = "";

        while (!this.eof()) {
            const current = this.get();

            if (/[\s()]/.test(current)) {
                break;
            } else {
                res += current;

                this.next();
            }
        }

        return this.isForward ? res : res.split("").reverse().join("");
    }

    eof() {
        return !this.hasNext();
    }

    error(errorType) {
        return new (errorType(this.isForward ? this.pos : this.input.length - this.pos));
    }
}

const parseWithBrackets = forward => input => {
    const charSource = new CharSource(input, forward);

    const res = parseTerm(charSource);

    skipWhitespace(charSource);

    if (charSource.eof()) {
        return res;
    } else {
        throw charSource.error(errors.ExcessiveInfoError("> 1"));
    }
};


const parsePrefix = parseWithBrackets(true);

const parsePostfix = parseWithBrackets(false);

const skipWhitespace = charSource => {
    while (/\s/.test(charSource.get())) {
        charSource.next();
    }
};


const closingBracket = charSource => charSource.get() === (charSource.isForward ? ")" : "(");

const noClosingBracket = charSource => charSource.error(charSource.isForward ? errors.NoClosingBracketError : errors.NoOpeningBracketError);

const parseOperation = charSource => {
    skipWhitespace(charSource);

    let operationName = charSource.getToken();

    if (!operation.has(operationName)) {
        throw charSource.error(errors.UnknownOperationError);
    }

    const op = operation.get(operationName);

    let args = [];

    const length = op.arity;

    skipWhitespace(charSource);
    while (! (charSource.eof() || closingBracket(charSource)) ) {
        args.push(parseTerm(charSource));
    }

    if (length === 0) {
        // everything is ok
    } else if (args.length < length) {
        throw charSource.error(errors.MissingInfoError(args.length));
    } else if (args.length > length) {
        throw charSource.error(errors.ExcessiveInfoError(args.length));
    }

    if (!charSource.isForward) {
        args = args.reverse();
    }

    skipWhitespace(charSource);

    if (charSource.eof()) {
        throw noClosingBracket(charSource);
    }

    charSource.next();

    return new op(...args);
};

const parseTerm = charSource => {
    skipWhitespace(charSource);

    if (!charSource.hasNext()) {
        throw charSource.error(errors.EmptyInputError);
    }

    let current = charSource.get();

    let result = undefined;

    if (current === "(" || current === ")") {
        if ((current === "(" && !charSource.isForward) || (current === ")" && charSource.isForward)) {
            throw noClosingBracket(charSource);
        }

        charSource.next();
        result = parseOperation(charSource);
    } else {
        const token = charSource.getToken();

        if (!isNaN(+token)) {
            result = new Const(+token);
        } else {
            if (availableNames.has(token)) {
                result = new Variable(token);
            } else {
                throw charSource.error(errors.UnknownVariableError);
            }
        }
    }

    skipWhitespace(charSource);

    return result;
};

console.log(parse('x y hmean').diff('x').toString())
console.log(parse('x y hmean').diff('x').simplify().toString())
