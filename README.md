# extension [![GitHub Actions status |pink-gorilla/extension](https://github.com/pink-gorilla/extension/workflows/CI/badge.svg)](https://github.com/pink-gorilla/extension/actions?workflow=CI)[![Clojars Project](https://img.shields.io/clojars/v/org.pinkgorilla/extension.svg)](https://clojars.org/org.pinkgorilla/extension)


A extension manager

Helps in discovery of dynamic services and service-extensions.


# Start Extension Manager

To start the extension manager:
```
(def exts (extension/discover))
```

This is equivalent to:
```
(def exts (extension/discover {:resource-dir "ext"
                               :output-path "target"
                               :disabled-extensions []}))
```


# Use Extension manager to configure an extension

```
(extension/get-extensions-for exts :quanta/template concat [] []])
```





