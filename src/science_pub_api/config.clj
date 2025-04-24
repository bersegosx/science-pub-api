(ns science-pub-api.config)

(def scopus-api-key "1e6f2f9ec4057bf3c1658c73d5d6f6f2")

(def db-spec
  {:classname   "org.sqlite.JDBC"
   :subprotocol "sqlite"
   :subname     "db.sqlite"})

(def http-port 8000)