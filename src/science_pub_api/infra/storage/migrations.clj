(ns science-pub-api.infra.storage.migrations 
  (:require
   [clojure.java.jdbc :as jdbc]
   [science-pub-api.config :refer [db-spec]]))

(defn migrate-articles
  []
  (jdbc/execute! db-spec
                 "drop table if exists articles;")

  (jdbc/execute! db-spec
                 "CREATE TABLE articles(
            id VARCHAR(50) primary key UNIQUE, 
            creator VARCHAR(50) not null,
            cover_date date not null,
            publication_name  VARCHAR(50) not null
        )"))

(defn migrate-words
  []
  (jdbc/execute! db-spec
                 "drop table if exists words;")

  (jdbc/execute! db-spec "CREATE TABLE words(
              word VARCHAR(255) not null,
              article_id INT,
              PRIMARY KEY (word, article_id),
              FOREIGN KEY (article_id) REFERENCES articles (id)
          )"))

(defn run-migrations 
  []
  (migrate-articles)
  (migrate-words))