(ns game.macros)

(defmacro effect [& expr]
  `(fn ~['state 'side 'card 'targets]
     ~(let [actions (map #(if (#{:runner :corp} (second %))
                            (concat [(first %) 'state (second %)] (drop 2 %))
                            (concat [(first %) 'state 'side] (rest %)))
                         expr)]
        `(let ~['runner '(:runner @state)
                'corp '(:corp @state)
                'register '(:register @state)]
           ~@actions))))

(defmacro req [& expr]
  `(fn ~['state 'card 'targets]
     (let ~['runner '(:runner @state)
            'corp '(:corp @state)
            'corp-reg '(get-in @state [:corp :register])
            'runner-reg '(get-in @state [:runner :register])
            'tagged '(> (get-in @state [:runner :tag]) 0)]
        ~@expr)))

(defmacro msg [& expr]
  `(fn ~['state 'side 'card]
     (let ~['runner '(:runner @state)
            'corp '(:corp @state)]
       (str ~@expr))))
