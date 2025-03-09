(ns extension
  (:require
   [clojure.edn :as edn]
   [resauce.core :as rs]
   [modular.writer :refer [write write-status write-target ensure-directory]]))

(defn add-extension [ext-res-name] 
  (try 
    (->> ext-res-name
         slurp
         edn/read-string)
    (catch Exception ex
      (throw (ex-info (str "extension" ext-res-name " has bad edn format") {:ext-name ext-res-name
                                                                            :ex ex})))))

(defn discover-resauce [resource-dir]
  (let [ext-res-names  (rs/resource-dir resource-dir)]
     (map add-extension ext-res-names)))

(defn extension-dict [ext-list]
  (into {}
        (map (fn [ext]
               [(:name ext) ext]) ext-list)))

(defn disabled? [disabled-extensions {:keys [name]}]
  (contains? disabled-extensions name))


(defn discover
  "Returns the discovered extensions for the purpose of using it in extension start. 
   Consider it a start-fn for an extension db service."
  ([]
   (discover {}))
  ([{:keys [resource-dir output-path disabled]
     :or {resource-dir "ext"
          output-path "target/"
          disabled #{}}}]
   (let [ext-list (discover-resauce resource-dir)
         active-extensions (remove #(disabled? disabled %) ext-list)
         disabled-extensions (filter #(disabled? disabled %) ext-list)
         ]
     {:extensions active-extensions
      :extension-dict (extension-dict active-extensions)
      :output-path output-path
      :extensions-disabled disabled-extensions})))

(defn write-service [state service-kw service-config]
  (write-target (name service-kw) service-config))

(defn write-target-webly [name data]
  (ensure-directory "./target")
  (ensure-directory "./target/webly")
  (let [filename (str "./target/webly/" name ".edn")]
    (write filename data)))

(defn get-extensions-for
  "(get-extensions exts :cljs)
   {“Ui-vega” [ui.vega/raw ui.vega.arrow]
    “Ui-repl” [promesa.core re-frame.core]
     (get-key [exts k v-default]))"
  [{:keys [output-path extensions] :as state} service-kw reducer-fn start-value nil-value]
  (let [get-extension-key (fn [ext]
                            (or (service-kw ext) nil-value))
        service-config (reduce reducer-fn start-value (map get-extension-key extensions))]
    (write-service state service-kw service-config)
    service-config))

(defn- get-key-or-default [[key default] extension]
  [key (or (get extension key) default)])

(defn- get-extension-key-or-default [key-map extension]
  (->> (map #(get-key-or-default % extension) key-map)
       (into {})))


(defn get-extensions [{:keys [output-path extensions] :as state} key-map]
  (map #(get-extension-key-or-default key-map %) extensions))




(defn get-deps-from-classpath []
  (let [deps
        (-> (Thread/currentThread)
            (.getContextClassLoader)
            (.getResources "goldly-extension.edn")
            (enumeration-seq)
            (->> (map (fn [url]
                        #_(-> (slurp url)
                              (edn/read-string)
                              (select-keys [:npm-deps])
                              (assoc :url url))
                        url))
                 (into [])))]
    deps))

(comment

  (get-deps-from-classpath)

  (require '[modular.resource.explore :as explore])
  (->> (explore/describe-files "")
       (clojure.pprint/print-table [:scheme :name]))

  (rs/resources "demo.notebook.goldly")

  (-> (rs/resources "")

      println)

  (-> (rs/resources "demo/notebook/apple.clj")
      first
      (rs/directory?))

  (-> (rs/resource-dir "ext")
      ;first
      last
      ;slurp
      )

  (-> (rs/resource-dir "demo/notebook")
      ;first
      last
      ;rs/name
                                        ;slurp
      )

  ;(recursive-resource-paths "ext")
  ;(recursive-resource-paths "")

  (->> (discover {:lazy true
                  ;:lazy-exclude #{"ui-gorilla"}
                  })
       vals
       (map (juxt :name :lazy)))

  (-> (discover {:lazy true
                  ;:lazy-exclude #{"ui-gorilla"}
                 })
      (write-extensions))

;  
  )
