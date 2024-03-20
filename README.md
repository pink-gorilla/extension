# extension

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





