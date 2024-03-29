(ns demo.cljsbuild
  (:require 
     [extension :refer [discover get-extensions write-service]])
  )

(defn demo [& _]
  (let [exts (discover)
        build-exts (get-extensions exts {:name "unknown"
                                         :lazy? false
                                         :cljs-namespace []
                                         :cljs-ns-bindings {}})
        ]
    (write-service exts :cljsbuild build-exts)
    (println "build-exts: ")
    (println build-exts)


    ))


