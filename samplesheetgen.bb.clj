#!/usr/bin/env bb

(require '[babashka.cli :as cli]
         '[clojure.string :as str]
         '[clojure.java.io :as io])

;;================================================
;; Core logic
;;================================================


(defn- read-csv-data-edn [input-file]
  (with-open [reader (io/reader (io/file input-file))]
    (doall (->> reader
                line-seq))))

(defn- update-string [sample-line-string path-prefix]
  (let [[sample fq1 fq2] (str/split sample-line-string #",")]
    (str sample
         ","
         (str path-prefix "/" fq1)

         (if (nil? fq2)
           (str ",")
           (str "," (str path-prefix "/" fq2)))

         "\n")))

(defn- transform-csv [base-csv path-prefix]
  (let [header (first (read-csv-data-edn base-csv))
        body (rest (read-csv-data-edn base-csv))
        update-path-fn (fn [sent] (update-string sent path-prefix))]
    (->> (map update-path-fn body)
         (reduce str)
         (str header "\n"))))

;;================================================
;; DRIVER Code
;;================================================


(defn write-transformed-csv [{:keys [input path-prefix output]}]
  (with-open [w (io/writer output)]
    (spit w (transform-csv input path-prefix))))

;;================================================
;; CLI
;;================================================

(def write-transformed-csv-cli-opts
  {:input     {:desc    "Input CSV file"
               :alias :i}
   :output    {:desc    "Output CSV file"
               :alias :o}
   :path-prefix   {:desc "Path prefix"
                   :alias :p}})

(defn help
  [_]
  (println
   (str "A utility script to generate other samplesheets with different base-path\n" "csv\n"
        (cli/format-opts {:spec write-transformed-csv-cli-opts}))))

(def cli-args
  [{:cmds ["csv"]
    :fn  #(write-transformed-csv (:opts %))
    :spec write-transformed-csv-cli-opts
    :args->opts [:cluster-file]}

   {:cmds [] :fn help}])

(cli/dispatch cli-args *command-line-args* {:coerce {:depth :long}})
