(ns aoc.d4
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(def example
  [[[2 4] [6 8]]
   [[2 3] [4 5]]
   [[5 7] [7 9]]
   [[2 8] [3 7]]
   [[6 6] [4 6]]
   [[2 6] [4 8]]])

(defn parse-line [s]
  (->> (str/split s #",|-")
       (map #(Integer/parseInt %))
       (partition 2)))

(def input
  (map parse-line (str/split-lines (slurp (io/resource "d4.txt")))))

(defn fully-contained?
  [[[a1 a2] [b1 b2]]]
  (or (<= a1 b1 b2 a2)
      (<= b1 a1 a2 b2)))

(defn overlap?
  [[[a1 a2] [b1 b2]]]
  (or (<= a1 b1 a2)
      (<= a1 b2 a2)
      (<= b1 a1 b2)
      (<= b1 a2 b2)))

(count (filter fully-contained? example))
(count (filter fully-contained? input))

(count (filter overlap? example))
(count (filter overlap? input))
