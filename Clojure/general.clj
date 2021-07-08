(def var-names {'x "x" 'y "y" 'z "z"})

(def function-operations (atom {}))
(def object-operations (atom {}))

(def zero-division (fn
                       ([x] (/ 1.0 x))
                       ([arg & args] (if (some zero? args) ##Inf (apply / arg args)))))

(defn parser [operations]
    #(cond
         (number? %) ((operations "cnst") %)
         (contains? var-names %) ((operations "vrb") (var-names %))
         :else (apply (operations (str (first %))) (mapv (parser operations) (rest %)))))
