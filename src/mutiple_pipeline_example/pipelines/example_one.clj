(ns multiple_pipeline_example.pipelines.example_one
  (:require 
    [lambdacd.steps.manualtrigger :refer [wait-for-manual-trigger]]
    [lambdacd.stepsupport.output :as output]
    [lambdacd.steps.shell :as shell]))


; Steps
(defn deploy [args ctx]
  (shell/bash ctx (:cwd args)
              "pwd"
              "ls -lh"))
              


; Pipeline
(def pipeline-structure
  `(wait-for-manual-trigger
    deploy))


; Info
(def config {:name                 "Example 1 - An easy example"
             :pipeline-url         "/example_one"
             :pipeline-structure   pipeline-structure})
