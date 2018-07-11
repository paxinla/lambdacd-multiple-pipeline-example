(ns multiple_pipeline_example.index
  (:require
    [multiple_pipeline_example.pipelines.example_one :as flow-example-one]
    [multiple_pipeline_example.pipelines.example_two :as flow-example-two]))
  

(def pipeline-configs [flow-example-one/config
                       flow-example-two/config])
