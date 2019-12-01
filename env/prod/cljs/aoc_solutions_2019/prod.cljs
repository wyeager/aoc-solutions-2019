(ns aoc-solutions-2019.prod
  (:require
    [aoc-solutions-2019.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)
