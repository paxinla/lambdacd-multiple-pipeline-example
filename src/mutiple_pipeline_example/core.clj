(ns multiple_pipeline_example.core
  (:require
    [lambdacd.core :as lambdacd]
    [lambdacd.runners :as runners]
    [lambdacd-git.core :as git]
    [compojure.core :as compojure]
    [hiccup.core :as h]
    [clojure.tools.logging :as log]
    [org.httpkit.server :as http-kit]
    [multiple_pipeline_example.ui-utils :as ui]
    [multiple_pipeline_example.index :as indexes]
    [ring.middleware.basic-authentication :refer [wrap-basic-authentication]])
  (:import 
    (java.nio.file.attribute FileAttribute)
    (java.nio.file Files LinkOption)
    (org.eclipse.jgit.transport UsernamePasswordCredentialsProvider))
  (:gen-class))


; how many build history will be kept in the build page.
(def max-build-history-number 20)

(defn- create-temp-dir []
  (str (Files/createTempDirectory "lambdacd" (into-array FileAttribute []))))

(defn authenticated? [name pass]
  (and (= name "Username for enter the build page.")
       (= pass "Secrect")))


(defn pipeline-for [pipeline-config]
  (let [home-dir           (create-temp-dir)
        config             {:home-dir home-dir
                            :name (:name pipeline-config)
                            :max-builds max-build-history-number
                            :use-new-event-bus true
                            :ui-config {:expand-active-default true
                                        :expand-failures-default false}
                            :git {:credentials-provider (UsernamePasswordCredentialsProvider. (System/getenv "LAMBDACD_GITHUB_USERNAME")
                                                                                              (System/getenv "LAMBDACD_GITHUB_PASSWORD"))}}
        pipeline-structure (:pipeline-structure pipeline-config)
        pipeline           (lambdacd/assemble-pipeline pipeline-structure config)
        app                (wrap-basic-authentication (compojure/routes
                                                        (ui/ui-routes pipeline (:pipeline-url pipeline-config))
                                                        (git/notifications-for pipeline))
                                                      authenticated?)]
    (log/info "CD Home Directory of " (:name pipeline-config) " is " home-dir)
    (runners/start-one-run-after-another pipeline)
    app))


(defn mk-context [project]
  (let [app (pipeline-for project)]
    (compojure/context (:pipeline-url project) [] app)))


(defn mk-link [{url :pipeline-url name :name}]
  [:li [:a {:href (str url "/lambdaui/lambdaui/index.html")} name]])

(defn mk-index [projects]
  (h/html
    [:html
     [:head
      [:title "CI project lists"]]
     [:body
      [:h1 "Pipelines:"]
      [:hr]
      [:ol (map mk-link projects)]]]))


(defn -main [& args]
  (let [contexts (map mk-context indexes/pipeline-configs)
        routes   (apply compojure/routes
                        (conj contexts (compojure/GET "/" [] (mk-index indexes/pipeline-configs))))]
    (log/info "Start http server ... ...")
    (http-kit/run-server routes {:open-browser? false
                                 :ip            "127.0.0.1"
                                 :port          9081})))
