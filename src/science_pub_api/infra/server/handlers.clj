(ns science-pub-api.infra.server.handlers
  (:require
   [cheshire.core :as json]
   [malli.core :as m]
   [malli.transform :as mt]
   [science-pub-api.config :refer [scopus-api-key]]
   [science-pub-api.infra.scopus-api :refer [fetch-articles-by-words]]
   [science-pub-api.infra.server.serializers :refer [format-results]]
   [science-pub-api.infra.server.services :refer [save-articles]]
   [science-pub-api.infra.storage.articles :refer [select-articles]]
   [science-pub-api.infra.storage.words :refer [get-total-records]]))

(defn to-list
  [x]
  (if (sequential? x)
    x
    [x]))

(defn json-resp
  [status data]
  {:status status
   :body (json/encode data)
   :headers {"Content-Type" "application/json"}})

(def json-ok (partial json-resp 200))
(def json-error (partial json-resp 400))

(defn- find-articles!
  [words]
  (let [scopus-results
        (fetch-articles-by-words scopus-api-key
                                 words)]
    (save-articles scopus-results)))

(defn find-articles-handler [request]
  (let [words
        (get-in request
                [:query-params :word])]
    (if words
      (do
        (find-articles! (to-list words))
        (json-ok {:ok true}))
      (json-error {:error "missing word"}))))

(defn- list-articles
  [words page]
  (let [total (get-total-records words)
        
        limit 5
        offset (* limit (dec page))

        articles (select-articles words
                                  :limit limit
                                  :offset offset)]
    (format-results articles total)))

(defn list-articles-handler [request]
  (let [words
        (get-in request
                [:query-params :word])

        page
        (get-in request
                [:query-params :page]
                "1")]
    (if (and words page)
      (let [result
            (list-articles (to-list words)
                           (Integer/parseInt page))]
        (json-ok result))

      (json-error {:error "missing word or page"}))))

(comment
  (find-articles! ["diabetes"])

  (find-articles! ["space" "time"])

  (list-articles ["diabetes"] 1)

  (select-articles ["diabetes"]
                   :limit 5
                   :offset 1)

  (json-ok (list-articles ["cat"] 1))

  (let [rq {:query-params
            {:word ["cat" "mouse"]
             :page "20"}
            }]
    (list-articles-handler rq))

  (let [rq {:query-params
            {:word ["cancer" "mouse"]}}]
    (find-articles-handler rq))

  (to-list [1 2])
  (to-list "aaa")

  (def non-empty-string
    (m/schema [:string {:min 3}]))
  
  (m/validate non-empty-string "123")
  (m/coerce [:int] "42" mt/string-transformer)

  )