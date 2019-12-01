(ns aoc-solutions-2019.core
    (:require
     [reagent.core :as r]
     [clojure.string]
     [cljs.reader]))

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
  \"To find the fuel required for a module, take its mass, 
   divide by three, round down, and subtract 2.
   Currently not accounting for inputs which would result 
   in 0 or negative amounts of fuel.\"
  [mass]
  (- (js/Math.floor (/ mass 3)) 2)

(defn problem-1-part-1
  \"Solves part 1 of problem 1: 
   https://adventofcode.com/2019/day/1#part1. Expects a string 
   of white space separated numbers.\"
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
  \"Solves part 1 of problem 2: 
   https://adventofcode.com/2019/day/1#part2. Expects a string
   of white space separated numbers.\"
  [module-masses-input]
  (let [masses (get-numbers module-masses-input)]
    (reduce + (map find-fuel-part-2 masses))))
"}))

;; -------------------------
;; Views

(defn solution [name code]
  [:div
   [:h3 name]
   [:pre {:style {:background-color "rgb(248, 248, 248)"}}
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
   [:ul (map (fn [sol] 
               [:li [solution (sol :name) (sol :code)]])
             solutions)]])

;; -------------------------
;; Initialize app

(defn mount-root []
  (r/render [home-page] (.getElementById js/document "app")))

(defn init! []
  (mount-root))

;; Utils

(defn get-numbers [lines]
  (let [str-numbers (clojure.string/split lines #"\s+")
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
  (let [masses (get-numbers module-masses-input)]
    (reduce + (map find-fuel-req masses))))

;; Problem 1 Part 2

(defn problem-1-part-2
  "Solves part 1 of problem 2: https://adventofcode.com/2019/day/1#part2. Expects a string of white space separated numbers."
  [module-masses-input]
  (let [masses (get-numbers module-masses-input)]
    (reduce + (map find-fuel-part-2 masses))))