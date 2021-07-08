(load-file "exceptions.clj")


(defn -return [value tail err] {:value value :tail tail :err err})
(def -return-ok #(-return % %2 nil))
(def -return-fail (partial -return nil))
(def -valid? #(and (boolean %) (not (:err %))))
(def -value :value)
(def -tail :tail)
(def -err :err)


(defn _empty [value] (partial -return-ok value))

(defn _char [err p]
    (fn [[c & cs]]
        (if (and c (p c)) (-return c cs nil) (-return-fail cs (err c cs)))))

(defn _map [f]
    (fn [result]
        (if (-valid? result)
            (-return-ok (f (-value result)) (-tail result))
            (-return-fail (-tail result) (-err result)))))

(defn _combine [f a b]
    (fn [str]
        (let [ar ((force a) str)]
            (if (-valid? ar)
                ((_map (partial f (-value ar))) ((force b) (-tail ar)))
                (-return-fail (-tail ar) (-err ar))))))

(defn _either [a b]
    (fn [str]
        (let [ar ((force a) str)]
            (if (-valid? ar) ar ((force b) str)))))

(defn _parser [p]
    (fn [input]
        (let [res ((_combine (fn [v _] v) p (_char eof #{\u0001})) (str input \u0001))]
            (if (-valid? res) (-value res) ((-err res) (- (count input) (count (-tail res))) input)))))


(defn +char [err chars] (_char err (set chars)))
(defn +char-not [err chars] (_char err (comp not (set chars))))
(defn +map [f parser] (comp (_map f) parser))
(def +ignore (partial +map (constantly 'ignore)))

(defn iconj [coll value]
    (if (= value 'ignore) coll (conj coll value)))

(defn +seq [& ps]
    (reduce (partial _combine iconj) (_empty []) ps))

(defn +seqf [f & ps] (+map (partial apply f) (apply +seq ps)))

(defn +seqn [n & ps] (apply +seqf (fn [& vs] (nth vs n)) ps))

(defn +or [p & ps]
    (reduce (partial _either) p ps))

(defn +opt [p]
    (+or p (_empty nil)))

(defn +star [p]
    (letfn [(rec [] (+or (+seqf cons p (delay (rec))) (_empty ())))] (rec)))

(defn +plus [p] (+seqf cons p (+star p)))

(defn +str [p] (+map (partial apply str) p))

(def +parser _parser)



(defn _show [result]
    (if (-valid? result) (str "-> " (pr-str (-value result)) " | " (pr-str (apply str (-tail result))))
                         "!"))

(defn tabulate [parser inputs]
    (run! (fn [input] (printf "    %-10s %s\n" (pr-str input) (_show (parser input)))) inputs))



(defn show-error-message [show-error-point msg index len]
    (if show-error-point
        (str msg "\n\t "
             (apply str (if (== index 0)
                            (cons "^" (repeat len " "))
                            (concat (repeat index " ") "^"))))
        msg))

(defn tabulate-exceptions
    ([parser inputs] (tabulate-exceptions true parser inputs))
    ([show-error-point parser inputs]
     (run! (fn [input]
               (printf "\t%-20s ->\t%s\n" (pr-str input)
                       (try
                           (toString (parser input))
                           (catch RuntimeException e
                               (let [data (ex-data e)]
                                   (if (and data (= (:type data) 'parser-error))
                                       (show-error-message show-error-point (ex-message e) (:position data) (count input))
                                       (throw e))))))) inputs)))
