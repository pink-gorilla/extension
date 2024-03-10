

;; config

(defn lazy-excluded? [goldly-config module-name]
  (let [excludes (lazy-excludes goldly-config)]
    (contains? excludes module-name)))

(defn ext-lazy-override [goldly-config {:keys [name lazy]
                                        :or {lazy false}
                                        :as ext}]
  ;(info "goldly lazy enabled:" (lazy-enabled goldly-config))
  ;(info "ext lazy excluded:" name (lazy-excluded? goldly-config name))
  (if (and (lazy-enabled goldly-config)
           (not (lazy-excluded? goldly-config name)))
    (assoc ext :lazy lazy)
    (assoc ext :lazy false)))