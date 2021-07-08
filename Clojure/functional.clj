(load-file "general.clj")


(def constant constantly)
(defn variable [name] #(% name))
(reset! function-operations {"cnst" constant "vrb" variable})


(defn create-function [f & args] (fn [mp] (apply f (mapv #(% mp) args))))
(defn function-factory [s f]
    (let [function (partial create-function f)]
        (swap! function-operations assoc s function)
        function))

(def add        (function-factory "+" +))
(def subtract   (function-factory "-" -))
(def multiply   (function-factory "*" *))
(def divide     (function-factory "/" zero-division))
(def negate     (function-factory "negate" -))

(def sum        (function-factory "sum" +))
(def avg        (function-factory "avg" #(/ (apply + %&) (count %&))))


(def parse-function-list (parser @function-operations))

(def parseFunction #(-> % read-string parse-function-list))
