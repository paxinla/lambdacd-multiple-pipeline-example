(ns multiple_pipeline_example.pipelines.example_two
  (:require 
    [lambdacd.steps.control-flow :refer [in-parallel]]
    [lambdacd.steps.shell :as shell]
    [lambdacd-git.core :as lambdacd-git]))


; I used HTTPS here, lambdacd-git also support the ssh way. For more detail, visit https://github.com/flosell/lambdacd-git  .
(def repo "https://github.com/paxinla/lambdacd-multiple-pipeline-example.git")
(def action_shell_param "param1 param2")


; Steps
(defn wait-for-gitlab-release [args ctx]
  (lambdacd-git/wait-for-git ctx repo
                             :ref "refs/heads/release"
                             :ms-between-polls (* 600 1000)))

(defn pull_release_code [args ctx]
  (shell/bash ctx (:cwd args) (str "/bin/bash /home/test/pull_release_code.sh " action_shell_param)))

(defn update_config [args ctx]
  (shell/bash ctx (:cwd args) (str "/bin/bash /home/test/update_config.sh " action_shell_param)))

(defn restart_service [args ctx]
  (shell/bash ctx (:cwd args) "/bin/bash /home/test/restart_service.sh restart"))


; Pipeline
(def pipeline-structure
  `(
    wait-for-gitlab-release
    pull_release_code
    update_config
    restart_service))


; Info
(def config {:name                 "Example 2 - An example of github."
             :pipeline-url         "/example_two"
             :pipeline-structure   pipeline-structure})
