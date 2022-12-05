(ns aoc.d5
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(def example
  "    [D]
[N] [C]
[Z] [M] [P]
 1   2   3

move 1 from 2 to 1
move 3 from 1 to 3
move 2 from 2 to 1
move 1 from 1 to 2")

(def input (slurp (io/resource "d5.txt")))

(def ->int #(Integer/parseInt %))

(defn ->crate [c]
  (if (= nil c)
    "   "
    (str "[" c "]")))

(defn parse-move
  [s]
  (->> (next (re-find #"move (\d+) from (\d+) to (\d+)" s))
       (map ->int)
       (zipmap [:n :from :to])))

(defn parse-layer
  [s]
  (loop [result [] s (seq s)]
    (cond
      (empty? s) result
      (= \space (first s)) (recur (conj result nil) (drop 4 s))
      (= \[ (first s)) (recur (conj result (second s)) (drop 4 s)))))

(defn parse-world
  [ws]
  (let [layers (map parse-layer (butlast ws))
        depth (count layers)
        width (apply max (map count layers))
        layers (map #(take width (concat % (repeat width nil))) layers)]
    (->> (apply interleave layers)
         (partition depth)
         (map (partial filter some?))
         (into []))))

(defn render
  [stacks]
  (let [m (apply max (map count stacks))
        stacks (map #(concat (repeat (- m (count %)) nil) %) stacks)]
    (->> (apply interleave stacks)
         (partition (count stacks))
         (map (fn [coll]
                (str/join " " (map ->crate coll))))
         (str/join "\n"))))

(defn parse [s]
  (let [[w _ m] (->> (str/split-lines s)
                     (partition-by #{""}))]
    [(parse-world w) (map parse-move m)]))

(defn play [f stacks {:keys [n from to]}]
  (let [pickup (take n (get stacks (dec from)))]
    (-> stacks
        (update (dec from) (partial drop n))
        (update (dec to) (partial concat (f pickup))))))

(defn simulate
  [f [stacks moves]]
  (reduce (partial play f) stacks moves))

(defn show [stacks]
  (println "---")
  (println (render stacks))
  (println (str " " (str/join "   " (map inc (range (count stacks))))))
  stacks)

(defn top-view
  [stacks]
  (show stacks)
  (println (str/join (map first stacks))))

(top-view (simulate reverse (parse example)))
(top-view (simulate reverse (parse input)))

(top-view (simulate identity (parse example)))
(top-view (simulate identity (parse input)))
