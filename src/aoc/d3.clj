(ns aoc.d3
  (:require [clojure.java.io :as io]
            [clojure.set :as set]
            [clojure.string :as str]))

(def example ["vJrwpWtwJgWrhcsFMMfFFhFp"
              "jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL"
              "PmmdzqPrVvPwwTWBwg"
              "wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn"
              "ttgJtRGJQctTZtZT"
              "CrZsJsPPZsGzwwsLwLmpwMDw"])

(def input (str/split-lines (slurp (io/resource "d3.txt"))))

(defn prio [c]
  (cond
    (<= (int \a) (int c) (int \z)) (+ 1 (- (int c) (int \a)))
    (<= (int \A) (int c) (int \Z)) (+ 27 (- (int c) (int \A)))))

(defn misplaced-items
  [coll]
  (->> coll
       seq
       ((fn [x] (partition (/ (count x) 2) x)))
       (map set)
       (apply set/intersection)))

(defn badge
  [coll]
  (->> coll
       (map (comp set seq))
       (apply set/intersection)))

;; part 1
(->> example (mapcat misplaced-items) (map prio) (reduce +))
(->> input (mapcat misplaced-items) (map prio) (reduce +))

;; part 2
(->> example (partition 3) (mapcat badge) (map prio) (reduce +))
(->> input (partition 3) (mapcat badge) (map prio) (reduce +))
