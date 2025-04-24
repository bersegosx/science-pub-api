(ns science-pub-api.infra.server.core
  (:require
   [io.pedestal.http :as http]
   [science-pub-api.config :refer [http-port]]
   [science-pub-api.infra.server.routes :refer [routes]]))

(def service-map
  {::http/routes routes
   ::http/type :jetty
   ::http/port http-port
   ;;  ::http/file-path "public" ;; worked
   ::http/allowed-origins (constantly true)
   ::http/secure-headers {:content-security-policy-settings
                          {:object-src "none"}}
   ::http/resource-path "public"})

(defn start []
  (http/start (http/create-server 
               (assoc service-map
                      ::http/host "0.0.0.0"))))

(defonce server (atom nil))    

(defn start-dev []
  (reset! server
          (http/start (http/create-server
                       (assoc service-map
                              ::http/join? false)))))  

(defn stop-dev []
  (http/stop @server))

(defn restart []
  (stop-dev)
  (start-dev))

(comment
  (start-dev)

  (stop-dev)
  
  (restart))
