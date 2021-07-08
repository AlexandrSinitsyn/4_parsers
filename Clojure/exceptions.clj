(defn exception-log
    ([msg ind] (str msg " at position: " ind "."))
    ([msg ind c] (str msg ". Found: \"" c "\" at position: " ind "."))
    ([msg ind c rst] (cond
                         (not rst) (exception-log msg ind)
                         (butlast rst) (str msg ". Found: \"" c "\" at position: " ind ". Rest: \"" (clojure.string/join (butlast rst)) "\"")
                         :else (exception-log msg ind c))))

(defn parser-exception [msg [& args]] #(throw (ex-info (apply exception-log msg (+ % 1) args)
                                                       {:type 'parser-error
                                                        :input %2
                                                        :position %
                                                        :err-found-on args})))

(def wrong-number-format-exception          #(parser-exception "Wrong number format" %&))
(def wrong-variable-name-exception          #(parser-exception "Wrong variable name" %&))
(def unknown-operation-exception            #(parser-exception "Unknown operation" %&))
(def no-bracket-exception                   #(parser-exception "No bracket exception" %&))
(def end-of-expression-expected-exception   #(parser-exception "End of expression expected" %&))
(def argument-expected-exception            #(parser-exception "Argument expected" %&))


(def no-space-error                         #(parser-exception "Space expected" %&))


(def wnf wrong-number-format-exception)
(def wvn wrong-variable-name-exception)
(def nbe no-bracket-exception)
(def uoe unknown-operation-exception)
(def eof end-of-expression-expected-exception)
(def aee argument-expected-exception)


(def nse no-space-error)
