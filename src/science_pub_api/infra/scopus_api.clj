(ns science-pub-api.infra.scopus-api
  (:require
   [clj-http.client :as client]
   [science-pub-api.config :refer [scopus-api-key]])
  (:import
   [clojure.lang ExceptionInfo]))

(def URL "https://api.elsevier.com/content/search/scopus")
(def ARTICLE-KEYS [:prism:doi
                   :dc:creator
                   :prism:coverDate
                   :prism:publicationName])

(defn- api-call
  [api-key word]
  (let [q-params {"query" word
                  "apiKey" api-key
                  "count" 10}]

    (try
      (client/get URL
                  {:accept :json
                   :as :json
                   :query-params q-params
                   :connection-timeout 5000})

      (catch ExceptionInfo e
        (println "Error:" e)
        nil))))

(defn- parse-articles
  [response]
  (get-in response
          [:body :search-results :entry]))

(defn- parse-artcle
  [article]
  (select-keys article ARTICLE-KEYS))

(defn- fetch-articles-by-word
  [api-key word]
  (->> (api-call api-key word)
       parse-articles
       (take 10)
       (mapv parse-artcle)))

(defn map-futures
  [fn coll]
  (->> coll
       (mapv #(future (fn %)))
       (mapv deref)))

(defn fetch-articles-by-words
  [api-key words]
  (->> words
       (map-futures #(fetch-articles-by-word api-key %))
       (mapv list words)))

(comment
  (api-call scopus-api-key "diabetes")

  (fetch-articles-by-word scopus-api-key "diabetes")

  (fetch-articles-by-words scopus-api-key ["space"])

  (fetch-articles-by-words scopus-api-key ["diabetes" "cancer"])
)