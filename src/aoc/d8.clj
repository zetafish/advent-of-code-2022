(ns aoc.d8
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(str/split-lines (slurp (io/resource "d8x.txt")))

(defn f [a b c]
  (+ a b c))
