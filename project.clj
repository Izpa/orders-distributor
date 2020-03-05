(defproject orders-distributor "0.1.0-SNAPSHOT"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[compojure "1.4.0"]
                 [clj-http "3.9.1"]
                 [environ "1.1.0"]
                 [heroku-database-url-to-jdbc "0.2.2"]
                 [migratus "1.2.7"]
                 [morse "0.2.4"]
                 [org.clojure/clojure "1.10.1"]
                 [org.postgresql/postgresql "42.2.3.jre7"]
                 [ring/ring-jetty-adapter "1.4.0"]
                 [ring/ring-json "0.4.0"]
                 [toucan "1.15.0"]]

  :plugins [[lein-environ "1.1.0"]
            [migratus-lein "0.7.3"]]
  :min-lein-version "2.0.0"
  :target-path "target/%s"
  :uberjar-name "orders-distributor.jar"
  :profiles {:uberjar {:aot :all}}
  :migratus {:store :database
             :migration-dir "migrations"
             :db ~(System/getenv "DATABASE_URL")})
