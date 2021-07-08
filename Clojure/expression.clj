(load-file "object.clj")
(load-file "parser.clj")


(def +all-chars (mapv char (range 0 128)))
(def +digit (+char wnf "0123456789"))
(def +letter (+char wvn (apply str (filter #(Character/isLetter %) +all-chars))))
(def +string #(+str (+map clojure.string/join (apply +seq (map (partial +char wvn) (clojure.string/split % #""))))))
(def +space (+char nse (apply str (filter #(Character/isWhitespace %) +all-chars))))

(def *ws (+ignore (+star +space)))


(declare *negate *argument term expr)

;region arguments
(def *number (+map (comp Constant read-string)
                   (+str (+seq (+opt (+char wnf "+-")) (+str (+plus +digit))
                               (+str (+map flatten (+opt (+seq (+char wnf ".") (+plus +digit)))))
                               (+str (+opt (+seq (+char wnf "eE") (+opt (+char wnf "+-")) (+str (+plus +digit)))))))))
(def *var (+map Variable (+str (+plus (+char wvn "xyzXYZ")))))

(def *argument (+or *number (delay *negate) *var))
;endregion

;region operations
(defn is-op-correct [x] (if (not (@object-operations x)) ((uoe x))))

(defn *operation
    ([[op a]] (is-op-correct op) ((@object-operations op) a))
    ([trnsf a op b] (is-op-correct op) (apply (@object-operations op) (trnsf [a b]))))
(defn *reduce-by-two [trnsf acc rst]
    (if (empty? rst)
        acc
        (recur trnsf (*operation trnsf acc (first rst) (second rst)) (subvec rst 2))))

(defn *op [rev [arg & args]] (*reduce-by-two rev arg (vec args)))
(def op #(apply *op identity %&))
(def op' #(*op reverse (reverse (flatten %&))))


(defn op-parser [op-converter & seq] (+map op-converter (apply +seqf (comp flatten cons) seq)))
(defn un-op-parser [& cmds]                 (op-parser *operation (apply +or (mapv +string cmds)) (+seq *ws (delay term) *ws)))
(defn bin-op-parser [op# prev-prior & cmds]  (op-parser op# prev-prior (+star (+seq *ws (apply +or (mapv +string cmds)) *ws prev-prior))))

(def *negate    (un-op-parser "negate"))

(def term       (+or *argument (+seqn 1 (+char aee "(") *ws (delay expr) *ws (+char nbe ")"))))
(def pow-log    (bin-op-parser op' term "**" "//"))
(def mul-div    (bin-op-parser op  pow-log "*" "/"))
(def plus-minus (bin-op-parser op  mul-div "+" "-"))
(def expr       plus-minus)
;endregion

(def parseObjectInfix (+parser (+seqn 0 *ws expr *ws)))
