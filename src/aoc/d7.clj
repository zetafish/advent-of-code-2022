(ns aoc.d7
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(def capacity 70000000)

(def needs 30000000)

(def example (str/split-lines (slurp (io/resource "d7x.txt"))))

(def input (str/split-lines (slurp (io/resource "d7.txt"))))

(defn cmd? [s] (str/starts-with? s "$"))

(defn get-block [coll]
  (concat [(subs (first coll) 2)]
          (take-while (complement cmd?) (rest coll))))

(defn split-block [coll]
  (when (seq coll)
    (let [block (get-block coll)]
      [block (drop (count block) coll)])))

(defn splitter
  [[blocks coll]]

  (let [[block more] (split-block coll)]
    [(conj blocks block) more]))

(defn blockify [coll]
  (->> (iterate splitter [[] coll])
       (take-while (comp some? last))
       last first))

(defn parse-cmd
  [s]
  (str/split s #" "))

(defn parse-inode [path s]
  (let [[a b] (str/split s #" ")]
    (cond
      (= "dir" a) {:dir (conj path (keyword b))}
      :else {:file b :size (parse-long a)})))

(defn step [world block]
  (let [[cmd arg] (parse-cmd (first block))]
    (case cmd
      "ls" (let [inodes (map (partial parse-inode (:path world))
                             (rest block))
                 size (->> inodes (keep :size) (reduce +))]
             (assoc-in world [:ls (:path world)] {:inodes inodes :size size}))
      "cd" (condp = arg
             "/" (assoc world :path [])
             ".." (update world :path pop)
             (update world :path conj (keyword arg))))))

(defn total-size
  [sys path]
  (+ (get-in sys [:ls path :size])
     (->> (get-in sys [:ls path :inodes])
          (keep :dir)
          (map (partial total-size sys))
          (reduce +))))

(defn dir->total-size
  [sys]
  (let [dirs (keys (:ls sys))]
    (zipmap dirs (map (partial total-size sys) dirs))))

(defn unused-space [sys]
  (- capacity (total-size sys [])))

(defn find-small [sys]
  (->> (dir->total-size sys)
       (filter #(<= (second %) 100000))
       (map second)
       (reduce +)))

(defn find-victim
  [sys]
  (let [reclaim (- needs (unused-space sys))]
    reclaim
    (->> (dir->total-size sys)
         (filter #(>= (second %) reclaim))
         (sort-by second)
         first)))

(defn simulate [data]
  (reduce step {} (blockify data)))

(find-small (simulate example))
(find-small (simulate input))

(find-victim (simulate example))
(find-victim (simulate input))

