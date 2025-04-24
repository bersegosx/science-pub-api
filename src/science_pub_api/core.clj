(ns science-pub-api.core
  (:gen-class) 
  (:require
   [science-pub-api.infra.server.core :refer [start]]
   [science-pub-api.infra.storage.migrations :refer [run-migrations]]))

(defn -main
  [& args]
  (run-migrations)
  (start))
