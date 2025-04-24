(ns science-pub-api.infra.server.services
  (:require 
   [science-pub-api.infra.storage.articles :refer [insert-articles]]
   [science-pub-api.infra.storage.words :refer [insert-words]]))

(def scopus-sql-fileds [:prism:doi
                        :dc:creator
                        :prism:coverDate
                        :prism:publicationName])

(def sql-article-fields [:id
                         :creator
                         :cover_date
                         :publication_name])

(def sql-word-fields [:word :article_id])


(def select-values (comp vals select-keys))

(defn collect-articles
  [articles]
  (->> articles
       (remove #(not= 4 (count %))) ;; naive ACL
       (mapv #(select-values % scopus-sql-fileds))))

(defn collect-words
  [word articles]
  (map (fn [a]
         [word (first a)])
       articles))

(defn save-articles
  "scopus-results 
   => 
   [(word-1 [{:prism:doi x :dc:creator y ...} ...]) 
    (word-2 [...])
    ...]"
  [scopus-results]
  (doseq [[word articles] scopus-results]

    (let [sql-articles
          (collect-articles articles)

          sql-words
          (collect-words word
                         sql-articles)]

      (insert-articles sql-articles
                       sql-article-fields)

      (insert-words sql-words
                    sql-word-fields))))