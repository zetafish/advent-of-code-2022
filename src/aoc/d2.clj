(ns aoc.d2
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(defn parse
  [s]
  (->>
   (str/split-lines s)
   (map #(str/split % #" "))
   (map (partial (partial map {"A" 1 "B" 2 "C" 3 "X" 1 "Y" 2 "Z" 3})))))

(def example
  (parse "A Y\nB X\nC Z"))

(def input
  (parse (slurp (io/resource "d2.txt"))))

(defn fight [a x] (get {[1 1] 3
                        [2 2] 3
                        [3 3] 3
                        [1 2] 6
                        [1 3] 0
                        [2 1] 0
                        [2 3] 6
                        [3 1] 6
                        [3 2] 0} [a x]))

(def win {1 2 2 3 3 1})
(def lose {1 3 2 1 3 2})
(def draw {1 1 2 2 3 3})

(defn choose-shape
  [[a x]]
  [a (case x
       1 (get lose a)
       2 (get draw a)
       3 (get win a))])

(defn score [[a x]]
  (+ (fight a x) x))

(defn rock-paper-scissors
  [f coll]
  (->> coll (map f) (map score) (reduce +)))

;; part 1
(rock-paper-scissors identity example)
(rock-paper-scissors identity input)

;; part 2
(rock-paper-scissors choose-shape example)
(rock-paper-scissors choose-shape input)
