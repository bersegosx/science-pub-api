(ns science-pub-api.infra.server.serializers
  (:require
   [clojure.set :refer [rename-keys]]))

(defn- format-article
  [article]
  (rename-keys article
               {:id :doi
                :creator :author
                :cover_date :date
                :publication_name :title}))

(defn format-results
  [articles total] 
  (let [fmt-articles
        (map format-article articles)]

    {:articles fmt-articles
     :total total}))

(comment
  (format-article {:id "10.5812/ijpr-156983",
                   :creator "Mohajeri M.",
                   :cover_date "2025-12-31",
                   :publication_name "Iranian"})
  )