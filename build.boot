                                        ; CONFS

(def project
  {:project     'jsonpipe
   :version     "0.0.1"
   :url         "http://github.com/fmind/jsonpipe"
   :scm         {:url "http://github.com/fmind/jsonpipe"}
   :developers  {"fmind" "fmind@users.noreply.github.com"}
   :license     {"AGPL" "https://opensource.org/licenses/agpl-3.0"}
   :description "Demonstrate how to process jsonlines files in parallel with Clojure."})

(def depends
  '[[org.clojure/clojure "1.9.0"]
    [org.clojure/test.check "0.9.0"]
    [org.clojure/core.async "0.3.465"]
    [cheshire "5.8.0"]])

(merge-env!
  :project project
  :dependencies depends
  ;; define paths for tasks
  :source-paths #{"src/clj"}
  :resource-paths #{"src/res"}
  ;; define namespaces for tasks
  :core 'jsonpipe.core)

(require [(get-env :core) :as 'core])

                                        ; TASKS

(task-options!
  pom project
  aot {:namespace #{(get-env :core)}}
  jar {:file (str (project :project) ".jar")})

(deftask build "Build the application jar."
  []
  (comp
   (aot)
   (pom)
   (uber)
   (jar :main (get-env :core))
   (target)))

(deftask run "Run the program with *args*."
  []
  (apply core/-main *args*))
