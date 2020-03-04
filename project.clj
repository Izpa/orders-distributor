(defproject orders-distributor "0.1.0-SNAPSHOT"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.8.0"]
                 [environ "1.1.0"]
                 [morse "0.2.4"]
                 [compojure "1.4.0"]
                 [ring/ring-jetty-adapter "1.4.0"]
                 [ring/ring-json "0.4.0"]
                 [clj-http "3.9.1"]]

  :plugins [[lein-environ "1.1.0"]]
  :min-lein-version "2.0.0"
  :target-path "target/%s"
  :uberjar-name "orders-distributor.jar"
  :profiles {:uberjar {:aot :all}})
