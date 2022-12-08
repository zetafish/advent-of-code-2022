(ns aoc.d6
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(def example (seq "mjqjpqmgbljsphdztnvjfqwrcgsmlb"))
(def input (seq (slurp (io/resource "d6.txt"))))

(defn prefix-len [s n]
  (+ n (->> (iterate next s)
            (map (partial take n))
            (map (partial str/join ""))
            (take (count s))
            (take-while #(not= n (count (set %))))
            count)))

(prefix-len example 4)
(prefix-len input 4)

(prefix-len example 14)
(prefix-len "mjqjpqmgbljsphdztnvjfqwrcgsmlb" 14)
(prefix-len input 14)
