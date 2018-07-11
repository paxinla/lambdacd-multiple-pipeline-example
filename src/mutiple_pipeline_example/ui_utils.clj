(ns multiple_pipeline_example.ui-utils
  (:require
    [lambdaui.core :as lambdaui]
    [compojure.core :refer [routes GET context]]))


(defn ui-routes [pipeline parent-path]
  (let [lambdaui-app    (lambdaui/ui-for pipeline :contextPath (str parent-path "/lambdaui"))]
    (routes
      (context "/lambdaui"  [] lambdaui-app))))
