release: DATABASE_URL=$DATABASE_URL lein migratus migrate
web: java $JVM_OPTS -cp target/uberjar/orders-distributor.jar clojure.main -m orders-distributor.core
