(defproject timewarrior/clj-facebook-graph "0.5.2"
  :description "A Clojure client for the Facebook Graph API."
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [org.clojure/data.json "0.1.1"]
                 [ring/ring-core "1.1.5"]
                 [clj-http "0.5.6"]
                 [timewarrior/clj-oauth2 "0.5.1"]]
  :dev-dependencies [[ring/ring-devel "1.1.5"]
                     [ring/ring-jetty-adapter "1.1.5"]
                     [compojure "1.1.3"]]
  :aot [clj-facebook-graph.FacebookGraphException])
