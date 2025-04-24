(ns science-pub-api.infra.storage.articles
  (:require
   [clojure.java.jdbc :as jdbc]
   [science-pub-api.config :refer [db-spec]]
   [honey.sql :as hsql]))

(defn select-articles
  [words & {:keys [limit offset]}]
  
  (->> {:select
        [:a.id :a.creator :a.cover_date :a.publication_name]

        :from [[:articles :a]]

        :inner-join [[{:select [[[:distinct :w.article_id]]]
                       :from [[:words :w]]
                       :where [:in :w.word words]} :suba]

                     [:= :a.id :suba.article_id]]

        :order-by [:a.id]
        :limit limit
        :offset offset}
       hsql/format
       (jdbc/query db-spec)))

(defn insert-articles
  [articles fields]
  (->> (hsql/format {:insert-into :articles
                     :columns fields
                     :values articles
                     :on-conflict []
                     :do-nothing :ignore})
       (jdbc/execute! db-spec)))

(comment 
  (select-articles ["diabetes"])

  [:id :creator :cover_date :publication_name]

  (->> (hsql/format {:insert-into :articles
               :columns 
                [:id :creator :cover_date :publication_name]
               :values [["12.5816"
                         "Moradnejad P."
                         "2025-12-31"
                         "International Cardiovascular Research Journal"
                         ]]
                :on-conflict []
                :do-nothing :ignore})
       (jdbc/execute! db-spec))
  
  (insert-articles [["13.5816"
                     "Moradnejad P."
                     "2025-12-31"
                     "International Cardiovascular Research Journal"]]
                   [:id :creator :cover_date :publication_name]) 
  )