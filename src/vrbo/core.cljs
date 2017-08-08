(ns vrbo.core
  (:require
   vrbo.dashboard
   vrbo.device))

(enable-console-print!)

(defn node-exists? [selector]
  (.querySelector js/document selector))

;; mount namespaces based on rails' controller/action
(defn mount-roots []
  (cond
    (node-exists? "#livedashboard") (vrbo.dashboard/init!)
    (node-exists? "#device-root") (vrbo.device/init!)))

(defn init! []
  (mount-roots))

(defn fig-reload []
  (init!)
  (print "figwheel reload complete!"))

(init!)
