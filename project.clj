(defproject multiple_pipeline_example "0.1.0-SNAPSHOT"
            :description "Mutiple pipeline with an index page."
            :url "https://github.com/paxinla/lambdacd-multiple-pipeline-example.git"
            :dependencies [[lambdacd "0.14.0"]
                           [lambdaui "1.1.0"]
                           [lambdacd-git "0.4.1"]
                           [ring/ring-jetty-adapter "1.4.0"]
                           [ring-basic-authentication "1.0.5"]
                           [http-kit "2.2.0"]
                           [org.clojure/clojure "1.9.0"]
                           [org.clojure/tools.logging "0.3.1"]
                           [org.slf4j/slf4j-api "1.7.5"]
                           [ch.qos.logback/logback-core "1.0.13"]
                           [ch.qos.logback/logback-classic "1.0.13"]]
            :profiles {:uberjar {:aot :all}}
            :main multiple_pipeline_example.core)
