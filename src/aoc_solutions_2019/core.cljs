(ns aoc-solutions-2019.core
    (:require
     [reagent.core :as r]
     [clojure.string]
     [cljs.reader]
     [clojure.set]))

;; Project generated by following instructions here: https://github.com/reagent-project/reagent
;; then by running:
;; lein new reagent-frontend myproject

;; this syntax for writing HTML is called Hiccup: http://reagent-project.github.io/docs/master/UsingHiccupToDescribeHTML.html

;; Solutions code
(def solutions `({:name "Problem 1 (part 1)"
                  :code "
(defn get-numbers [lines]
  (let [str-numbers (clojure.string/split lines #\"\\s+\")
        numbers (map js/parseInt str-numbers)]
    numbers))

(defn find-fuel-req
  [mass]
  (- (js/Math.floor (/ mass 3)) 2))

(defn problem-1-part-1
  \"Expects a string of white space separated numbers.\"
  [module-masses-input]
  (let [masses (get-numbers module-masses-input)]
    (reduce + (map find-fuel-req masses))))"}
                 {:name "Problem 1 (part 2)"
                  :code "
(defn find-fuel-part-2 [mass-input]
  (loop [mass mass-input
         total 0]
    (if (<= mass 8)
      total
      (let [fuel (find-fuel-req mass)]
        (recur fuel (+ fuel total))))))

(defn problem-1-part-2
  \"Expects a string of white space separated numbers.\"
  [module-masses-input]
  (let [masses (get-numbers module-masses-input)]
    (reduce + (map find-fuel-part-2 masses))))
"}
                 {:name "Problem 2 (part 1)"
                  :code "
(defn get-numbers [lines regex]
  (let [str-numbers (clojure.string/split lines regex)
        numbers (map js/parseInt str-numbers)]
    numbers))

(defn process-op-codes
  \"Expects a vector of 4 numbers\"
  [op-codes program]
  (let [op (op-codes 0)
        ;; get value at index of program given by element of subvector
        first-operand (program (op-codes 1))
        second-operand (program (op-codes 2))
        ops {1 + 2 *}]
    ((ops op) first-operand second-operand)))

(defn process-program
  \"Expects a vector of numbers. Assumes all programs are terminated by a 99 operation\"
  [program]
  (loop [modified-program program
         start-index 0]
    (let [end-index (+ start-index 4)
          sub-vector (subvec modified-program start-index end-index)]
      (if (= 99 (first sub-vector))
        ;; base case: return first element of modified program
        (first modified-program)
        ;; otherwise calculate value based on sub vector and replace element of program with specified index
        (let [res (process-op-codes sub-vector modified-program)
              replacement-index (sub-vector 3)]
          (recur (assoc modified-program replacement-index res) end-index))))))

(defn problem-2-part-1 [program-str]
  (let [program-arr (vec (get-numbers program-str #\",\"))
        program-replace-digits (assoc program-arr 1 12 2 2)]
    (process-program program-replace-digits)))
"}
                 {:name "Problem 2 (part 2)"
                  :code "
(defn problem-2-part-1 [program-str noun verb]
  (let [program-arr (vec (get-numbers program-str #\",\"))
        program-replace-digits (assoc program-arr 1 noun 2 verb)]
    (process-program program-replace-digits)))

;; warning: really ugly clojure incoming, nesting loops is something I'm struggling to do recursively
;; I'd like to return to this to tidy it up
(defn problem-2-part-2 [program-str]
  (loop [x 0]
    (loop [y 0]
      (if (= (problem-2-part-1 program-str x y) 19690720)
        (println (str \"Solution: \" x \" \" y \" then multiply x by 100 and add y: \" (+ y (* 100 x))))
        nil)
      (if (> y 99)
        nil
        (recur (inc y))))
    (if (> x 99)
      nil
      (recur (inc x)))))
"}
                 {:name "Problem 3 (part 1)"
                  :code "
(defn move-wire
  \"Expects start to be a 2D Vector and instruction to be for example R5. Returns a set of points that the wire now crosses\"
  [start instruction]
  (let [dir (first instruction)
        dist (js/parseInt (subs instruction 1))
        start-x (start 0)
        start-y (start 1)]
    (cond
      ;; this could be better...
      (= dir \"R\") [(set (map (fn [i] [(+ start-x i) start-y]) (range 0 (+ dist 1)))) [(+ start-x dist) start-y]]
      (= dir \"L\") [(set (map (fn [i] [(- start-x i) start-y]) (range 0 (+ dist 1)))) [(- start-x dist) start-y]]
      (= dir \"U\") [(set (map (fn [i] [start-x (+ start-y i)]) (range 0 (+ dist 1)))) [start-x (+ start-y dist)]]
      (= dir \"D\") [(set (map (fn [i] [start-x (- start-y i)]) (range 0 (+ dist 1)))) [start-x (- start-y dist)]])))

(defn generate-points [path]
  (loop [remaining-directions path
         points-crossed #{}
         starting-point [0 0]]
    (if (empty? remaining-directions)
      points-crossed
      (let [[head & rest] remaining-directions
            res (move-wire starting-point head)]
        (recur rest
               (into points-crossed (res 0))
               (res 1))))))

(defn get-manhattan-dist [point]
  (let [abs-x (js/Math.abs (point 0))
        abs-y (js/Math.abs (point 1))]
    (+ abs-x abs-y)))

(defn problem-3-part-1 [path-str-1 path-str-2]
  (let [path-1 (clojure.string/split path-str-1 #\",\")
        path-2 (clojure.string/split path-str-2 #\",\")
        path-1-points (generate-points path-1)
        path-2-points (generate-points path-2)
        intersections (clojure.set/intersection path-1-points path-2-points)
        dists (map get-manhattan-dist intersections)]
    (second (sort dists))))
"
                  }))

;; -------------------------
;; Views

(defn solution [name code]
  [:div
   [:h3 name]
   [:pre {:style {:background-color "rgb(248, 248, 248)"
                  :overflow-x "scroll"}
          :class "language-clojure"}
    [:code code]]])

(defn main-description []
  [:p "In an effort to learn Clojurescript and Reagent I am going to try to solve each of the "
   [:a {:href "https://adventofcode.com/"} "Advent of Code"]
   " challenges (in Clojure) and post my solutions here. Along with posting the solutions I'd also like to "
   "flush out the website as I go, perhaps by adding features like the ability to search for solutions or "
   "experimenting with different CSS frameworks."])

(defn home-page []
  [:div 
   [:h2 "Advent of Code Solutions 2019"]
   [main-description]
   [:div (map (fn [sol] 
               [:div [solution (sol :name) (sol :code)]])
             solutions)]])

;; -------------------------
;; Initialize app

(defn mount-root []
  (r/render [home-page] (.getElementById js/document "app")))

(defn init! []
  (mount-root))

;; Utils

(defn get-numbers [lines regex]
  (let [str-numbers (clojure.string/split lines regex)
        numbers (map js/parseInt str-numbers)]
    numbers))

;; Solutions

;; Problem 1 Part 1

(defn find-fuel-req
  "To find the fuel required for a module, take its mass, divide by three, round down, and subtract 2.
   Currently not accounting for inputs which would result in 0 or negative amounts of fuel."
  [mass]
  (- (js/Math.floor (/ mass 3)) 2)
  )

(defn find-fuel-part-2 [mass-input]
  (loop [mass mass-input
         total 0]
    (if (<= mass 8)
      total
      (let [fuel (find-fuel-req mass)]
        (recur fuel (+ fuel total))))))

(defn problem-1-part-1 
  "Solves part 1 of problem 1: https://adventofcode.com/2019/day/1#part1. Expects a string of white space separated numbers."
  [module-masses-input]
  (let [masses (get-numbers module-masses-input #"\s+")]
    (reduce + (map find-fuel-req masses))))

;; Problem 1 Part 2

(defn problem-1-part-2
  "Solves part 1 of problem 2: https://adventofcode.com/2019/day/1#part2. Expects a string of white space separated numbers."
  [module-masses-input]
  (let [masses (get-numbers module-masses-input #"\s+")]
    (reduce + (map find-fuel-part-2 masses))))

(defn get-program-arr [program-str]
  (clojure.string/split program-str #",")
  )

(defn process-op-codes 
  "Expects a vector of 4 numbers"
  [op-codes program]
  (let [op (op-codes 0)
        ;; get value at index of program given by element of subvector
        first-operand (program (op-codes 1))
        second-operand (program (op-codes 2))
        ops {1 + 2 *}]
    ((ops op) first-operand second-operand)
    )
  )

(defn process-program 
  "Expects a vector of numbers. Assumes all programs are terminated by a 99 operation"
  [program]
  (loop [modified-program program
         start-index 0]
    (let [end-index (+ start-index 4)
          sub-vector (subvec modified-program start-index end-index)]
      (if (= 99 (first sub-vector))
        ;; base case: return first element of modified program
        (first modified-program)
        ;; otherwise calculate value based on sub vector and replace element of program with specified index
        (let [res (process-op-codes sub-vector modified-program)
              replacement-index (sub-vector 3)]
          (recur (assoc modified-program replacement-index res) end-index)))))
  )

(defn problem-2-part-1 [program-str noun verb]
  (let [program-arr (vec (get-numbers program-str #","))
        program-replace-digits (assoc program-arr 1 noun 2 verb)]
    (process-program program-replace-digits)
    )
  )

;; warning: really ugly clojure incoming, nesting loops is something I'm struggling to do recursively
(defn problem-2-part-2 [program-str]
  (loop [x 0]
    (loop [y 0]
      (if (= (problem-2-part-1 program-str x y) 19690720)
        (println (str "Solution: " x " " y " then multiply x by 100 and add y: " (+ y (* 100 x))))
        nil
        )
      (if (> y 99)
        nil
        (recur (inc y))))
    (if (> x 99)
      nil
      (recur (inc x))))
  )

(defn move-wire 
  "Expects start to be a 2D Vector and instruction to be for example R5. Returns a set of points that the wire now crosses"
  [start instruction]
  (let [dir (first instruction)
        dist (js/parseInt (subs instruction 1))
        start-x (start 0)
        start-y (start 1)]
    (cond 
      ;; this could be better...
      (= dir "R") [(set (map (fn [i] [(+ start-x i) start-y]) (range 0 (+ dist 1)))) [(+ start-x dist) start-y]]
      (= dir "L") [(set (map (fn [i] [(- start-x i) start-y]) (range 0 (+ dist 1)))) [(- start-x dist) start-y]]
      (= dir "U") [(set (map (fn [i] [start-x (+ start-y i)]) (range 0 (+ dist 1)))) [start-x (+ start-y dist)] ]
      (= dir "D") [(set (map (fn [i] [start-x (- start-y i)]) (range 0 (+ dist 1)))) [start-x (- start-y dist)]])
    )
  )

(defn generate-points [path]
  (loop [remaining-directions path
         points-crossed #{}
         starting-point [0 0]]
    (if (empty? remaining-directions)
      points-crossed
      (let [[head & rest] remaining-directions
            res (move-wire starting-point head)]
        (recur rest
               (into points-crossed (res 0))
               (res 1))))))

(defn get-manhattan-dist [point]
  (let [abs-x (js/Math.abs (point 0))
        abs-y (js/Math.abs (point 1))]
    (+ abs-x abs-y)
    )
  )

(defn problem-3-part-1 [path-str-1 path-str-2]
  (let [path-1 (clojure.string/split path-str-1 #",")
        path-2 (clojure.string/split path-str-2 #",")
        path-1-points (generate-points path-1)
        path-2-points (generate-points path-2)
        intersections (clojure.set/intersection path-1-points path-2-points)
        dists (map get-manhattan-dist intersections)]
    (second (sort dists))
    ))

; (defn add-steps-to-ints [ints sub-path first-time]
;   ;; need to check if we've already hit this intersection
;   (loop [remaing-points sub-path])
;   )

; (defn get-steps-to-intersections [path steps-to-intersections]
;   (loop [remaining-directions path
;          ints steps-to-intersections
;          starting-point [0 0]
;          encountered-ints #{}]
;     (if (empty? remaining-directions)
;       steps-to-intersections
;       (let [[head & rest] remaining-directions
;             res (move-wire starting-point head)
;             ;; res 1 is the list of points the direction brought us through
;             ;; ints is a map from intersection point to a 2D vector with the number of steps it takes to get to the intersection along both paths
;             sad (add-steps-to-ints ints (res 1))]
;         (recur rest
;                (add-steps-to-ints ints (res 1))
;                (res 1))
;         )
;       )
;     )
;   )

; (defn problem-3-part-2 [path-str-1 path-str-2]
;   (let [path-1 (clojure.string/split path-str-1 #",")
;         path-2 (clojure.string/split path-str-2 #",")
;         path-1-points (generate-points path-1)
;         path-2-points (generate-points path-2)
;         intersections (clojure.set/intersection path-1-points path-2-points)
;         steps-to-intersections (apply assoc {}
;                                       (interleave intersections (cycle [[]])))
;         path-1-steps (get-steps-to-intersections path-1 steps-to-intersections)]

;     ))

; {[5 6] [2 3] [1 2] []}

; (def m {:a 1})

; (def s #{:a :b})

; (assoc m :a 2)

; (apply assoc {}
;        (interleave #{:a :b} (cycle [[]])))

; (map (fn [x] x) s)

; (+ 1 1)
