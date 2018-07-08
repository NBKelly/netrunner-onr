(ns game-test.macros
  (:require [game.core :as core]
            [clojure.test :refer :all]))

(defmacro do-game [s & body]
  `(let [~'state ~s
         ~'get-corp (fn [] (:corp @~'state))
         ~'get-runner (fn [] (:runner @~'state))
         ~'get-run (fn [] (:run @~'state))
         ~'get-hand-size (fn [~'side] (+ (get-in @~'state [~'side :hand-size :base])
                                         (get-in @~'state [~'side :hand-size :mod])))
         ~'assert-prompt (fn [~'side]
                       (is (first (get-in @~'state [~'side :prompt]))
                           (str "Expected an open " (name ~'side) " prompt")))
         ~'refresh (fn [~'card] 
                     ;; (is ~'card "card passed to refresh should not be nil")
                     (let [~'ret (core/get-card ~'state ~'card)]
                       
                       ;; (is ~'ret "(refresh card) should not be nil - use (core/get-card state card) instead")
                       ~'ret))
         
         ~'prompt-choice-partial (fn [~'side ~'choice]
                                   (core/resolve-prompt
                                     ~'state ~'side
                                     {:choice (~'refresh (first (filter #(.contains % ~'choice)
                                                                        (->> @~'state ~'side :prompt first :choices))))}))
         ~'prompt-choice (fn [~'side ~'choice]
                           (is (or (number? ~'choice)
                                   (string? ~'choice))
                               "prompt-choice should only be called with strings or numbers")
                           (~'assert-prompt ~'side)
                           (core/resolve-prompt ~'state ~'side {:choice (~'refresh ~'choice)}))

         ~'prompt-card (fn [~'side ~'card]
                         (~'assert-prompt ~'side)
                         (core/resolve-prompt ~'state ~'side {:card (~'refresh ~'card)}))
         ~'prompt-select (fn [~'side ~'card]
                           (~'assert-prompt ~'side)
                           (core/select ~'state ~'side {:card (~'refresh ~'card)}))
         ~'prompt-is-card? (fn [~'side ~'card]
                             (~'assert-prompt ~'side)
                             (and (:cid ~'card) (-> @~'state ~'side :prompt first :card :cid)
                                  (= (:cid ~'card) (-> @~'state ~'side :prompt first :card :cid))))
         ~'prompt-is-type? (fn [~'side ~'type]
                             (~'assert-prompt ~'side)
                             (and ~'type (-> @~'state ~'side :prompt first :prompt-type)
                                  (= ~'type (-> @~'state ~'side :prompt first :prompt-type))))

         ~'prompt-map (fn [side#] (first (get-in @~'state [side# :prompt])))
         ~'prompt-titles (fn [side#] (map #(:title %) (:choices (~'prompt-map side#))))
         ~'prompt? (fn [~'side] (-> @~'state ~'side :prompt first))]
     ~@body))

(defmacro deftest-pending [name & body]
  (let [message (str "\n" name " is pending")]
    `(clojure.test/deftest ~name (println ~message))))

(defmacro changes-val-macro [change-amt val-form body-form msg]
  `(let [start-val# ~val-form]
    ~body-form
    (let [end-val# ~val-form
          actual-change# (- end-val# start-val#)]
      (clojure.test/do-report
        {:type (if (= actual-change# ~change-amt) :pass :fail)
         :actual actual-change#
         :expected ~change-amt
         :message (str "Changed from " start-val# " to " end-val# ", Expected end result of " (+ start-val# ~change-amt) " " ~msg " " '~body-form)}))))

(defmethod clojure.test/assert-expr 'changes-val [msg form]
  (let [change-amt (nth form 1)
        val-form (nth form 2)
        body-form (nth form 3)]
    `(changes-val-macro ~change-amt ~val-form ~body-form ~msg)))

;; Enables you to do this:
;; (is (changes-credits (get-runner) -5
;;   (play-from-hand state :runner "Magnum Opus")))
(defmethod clojure.test/assert-expr 'changes-credits [msg form]
  (let [side (nth form 1)
        change-amt (nth form 2)
        body-form (nth form 3)]
    `(changes-val-macro ~change-amt (:credit ~side) ~body-form ~msg)))
