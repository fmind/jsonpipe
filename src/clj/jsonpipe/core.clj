(ns jsonpipe.core
  (:gen-class)
  (:require [cheshire.core :as json]
            [clojure.core.async :as async]
            [clojure.spec.alpha :as s]
            [clojure.java.io :as io]))

(s/def ::name string?)
(s/def ::number nat-int?)
(s/def ::season nat-int?)
(s/def ::episode (s/keys :req-un [::name ::number ::season]))

(defn writeln "Write + flush."
  [writer x]
  (let [ln (str x "\n")]
    (.write writer ln)
    (.flush writer)))

(defn line->doc "Line to doc."
  [js] ;; convert with keyword
  (json/parse-string js true))

(defn doc->info "Doc to info."
  [doc]
  {:pre [(s/valid? ::episode doc)]}
  ((juxt :season :number :name) doc))

(def ncpu
  (.availableProcessors
   (Runtime/getRuntime)))

(def proc
  (comp
   (map line->doc)
   (map doc->info)))

(defn pipe "Create a parallel pipeline."
  [xf in out err]
  (let [n ncpu
        close? true
        to (async/chan n)
        from (-> in line-seq async/to-chan)
        ln-handler (fn [x] (writeln out x))
        ex-handler (fn [x] (writeln err x))]
    (async/go-loop []
      (when-let [ln (async/<! to)] (ln-handler ln) (recur)))
    (async/pipeline-blocking n to xf from close? ex-handler)))

(defn -main
  [& args]
  (async/<!! ;; wait done
   (pipe proc ;; function
         (io/reader *in*)
         (io/writer *out*)
         (io/writer *err*))))
