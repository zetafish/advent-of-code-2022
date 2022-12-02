(ns aoc.d1
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(def example
  ["1000"
   "2000"
   "3000"
   ""
   "4000"
   ""
   "5000"
   "6000"
   ""
   "7000"
   "8000"
   "9000"
   ""
   "10000"])

(def input (str/split-lines (slurp (io/resource "d1.txt"))))

(defn sum-elves
  [col]
  (->>
   col
   (partition-by #{""})
   (remove #{[""]})
   (map (partial map #(Integer/parseInt %)))
   (map (partial apply +))))

(defn top
  [n col]
  (->>
   col
   sort
   reverse
   (take n)
   (reduce +)))

;; part 1
(top 1 (sum-elves example))
(top 1 (sum-elves input))

;; part 2
(top 3 (sum-elves example))
(top 3 (sum-elves input))

