(load-file "functional.clj")
(load-file "proto.clj")


(def evaluate (memoize (fn [expr mp] (proto-call expr :evaluate mp))))
(def diff (memoize (fn [expr v] (proto-call expr :diff v))))

(def expressionToSymbol #(proto-call % :toString))
(def toString (memoize #(str (expressionToSymbol %))))
(def expressionToInfixSymbol #(proto-call % :toStringInfix))
(def toStringInfix (memoize #(str (expressionToInfixSymbol %))))


(def ConstantPrototype
    {:evaluate      (fn [this mp] ((this :eval-impl) this mp))
     :diff          (fn [this v] ((force (this :diff-impl)) this v))
     :toString      (fn [this] ((this :to-string-impl) (this :actual-value)))
     :toStringInfix toString})

(defn ConstantConstructor [this eval-function diff-function to-string-rule actual-transform]
    (fn [v] (assoc this
                :eval-impl eval-function
                :diff-impl diff-function
                :to-string-impl to-string-rule
                :value (actual-transform v)
                :actual-value v)))

(declare ZERO)
(def Constant ((constructor ConstantConstructor ConstantPrototype)
               (fn [this _] (this :value))
               (delay (constantly ZERO))
               (fn [v] v)
               (fn [v] v)))
(def ZERO  (Constant 0))
(def ONE   (Constant 1))
(def TWO   (Constant 2))
(def Variable ((constructor ConstantConstructor ConstantPrototype)
               #(%2 (% :value))
               (delay #(if (= (% :value) %2) ONE ZERO))
               #(symbol %)
               #(clojure.string/lower-case (str (first %)))))

(reset! object-operations {"cnst" Constant "vrb" Variable})

(def _args (field :args))
(def _symb (field :symb))

(def OperationPrototype
    {:evaluate      (fn [this mp] (apply (proto-get this :fun) (mapv #(evaluate % mp) (_args this))))
     :diff          (fn [this v] ((proto-get this :diff-impl) v (_args this) (mapv #(diff % v) (_args this))))
     :toString      (fn [this] (symbol (str "(" (_symb this) " " (clojure.string/join " " (mapv expressionToSymbol (_args this)))")")))
     :toStringInfix (fn [this] (symbol (apply (fn
                                    ([x] (str (_symb this) "(" (expressionToInfixSymbol x) ")"))
                                    ([a b] (str "(" (expressionToInfixSymbol a) " " (_symb this) " " (expressionToInfixSymbol b) ")"))) (_args this))))})

(defn AbstractOperation [this symb fun diff-rule]
    (assoc this
        :symb symb
        :fun fun
        :diff-impl diff-rule))

(defn abstract-operation-factory [symb fun diff-rule] ((constructor AbstractOperation OperationPrototype) symb fun diff-rule))

(defn operation-factory [symb fun diff-rule]
    (letfn [(Operation# [& args] {:prototype (abstract-operation-factory symb fun diff-rule) :args args})]
        (swap! object-operations assoc symb Operation#)
        Operation#))


;region Operations
(def Add        (operation-factory "+" + #(apply Add %3)))
(def Subtract   (operation-factory "-" - #(apply Subtract %3)))

(def Multiply ())
(defn multi-diff [Operation args argsDiff]
    (apply Operation (mapv #(apply Multiply (let [i# (first %)] (assoc (vec args) i# (nth argsDiff i#)))) (map-indexed vector argsDiff))))

(def Multiply   (operation-factory "*" * #(multi-diff Add %2 %3)))

(def Square     (operation-factory "square" #(* % %) (fn [_ [a] [ad]] (Multiply TWO (Multiply a ad)))))

(def Divide     (operation-factory "/" zero-division #(if (== (count %2) 1)
                                                          (diff (apply Divide ONE %2) %)
                                                          (Divide (multi-diff Subtract %2 %3) (apply Multiply (mapv Square (rest %2)))))))

(def Negate     (operation-factory "negate" #(- %) #(apply Negate %3)))


(def Sum        (operation-factory "sum" + #(apply Sum %3)))
(def Avg        (operation-factory "avg" #(/ (apply + %&) (count %&)) #(apply Avg %3)))


(declare Log-e)
(def ILog       (operation-factory "//" #(/ (Math/log (Math/abs %2)) (Math/log (Math/abs %)))
                                   (fn [[a b] [aDiff bDiff]] (Divide (Subtract (Divide (Multiply bDiff (Log-e a)) b) (Divide (Multiply aDiff (Log-e b)) a)) (Square (Log-e a))))))
(defn Log-e [x] (ILog (Constant Math/E) x))

(def IPow       (operation-factory "**" #(Math/pow % %2) (fn [[a b] [aDiff bDiff]] (Multiply (IPow a b) (Add (Multiply bDiff (Log-e a)) (Multiply b (Divide aDiff a)))))))
;endregion


(def parse-object-list (parser @object-operations))

(def parseObject #(-> % read-string parse-object-list))
