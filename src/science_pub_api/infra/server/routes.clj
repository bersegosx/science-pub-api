(ns science-pub-api.infra.server.routes
  (:require [io.pedestal.http.route :as route]
            [science-pub-api.infra.server.handlers :as h]))

(def routes
  (route/expand-routes
   #{["/find" :get h/find-articles-handler :route-name :find]
     ["/articles" :get h/list-articles-handler :route-name :list]}))