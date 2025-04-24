(defproject science-pub-api "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.12.0"]

                 [io.pedestal/pedestal.jetty "0.7.2"]
                 [org.slf4j/slf4j-simple    "2.0.10"]
                 [clj-http "3.13.0"]
                 [cheshire "5.13.0"]
                 [org.clojure/java.jdbc "0.7.12"]
                 [org.xerial/sqlite-jdbc "3.48.0.0"]
                 [metosin/malli "0.17.0"]
                 [com.github.seancorfield/honeysql "2.7.1295"]]
  :main ^:skip-aot science-pub-api.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
