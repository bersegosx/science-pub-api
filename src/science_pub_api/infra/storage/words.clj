(ns science-pub-api.infra.storage.words
  (:require
   [clojure.java.jdbc :as jdbc]
   [science-pub-api.config :refer [db-spec]]
   [honey.sql :as hsql]))

(defn get-total-records
  [words]
  (->> {:select [[[:count [:distinct :w.article_id]] :total]],
        :from [[:words :w]],
        :where [:in :w.word words]}
       hsql/format
       (jdbc/query db-spec)
       first
       :total))

(defn insert-words
  [words fields]
  (->> (hsql/format {:insert-into :words
                     :columns fields
                     :values words
                     :on-conflict []
                     :do-nothing :ignore})
       (jdbc/execute! db-spec)))

  (comment 
    (get-total-records ["diabetes"])

    (jdbc/query db-spec
                "select * from words;")
    
    (insert-words [["mouse" "10.5816"]]
                  [:word :article_id])

    (jdbc/execute! db-spec "CREATE TABLE words(
              word VARCHAR(255) not null,
              article_id INT,
              PRIMARY KEY (word, article_id),
              FOREIGN KEY (article_id) REFERENCES articles (id)
          )"))