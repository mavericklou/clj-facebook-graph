; Copyright (c) Maximilian Weber. All rights reserved.
; The use and distribution terms for this software are covered by the
; Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
; which can be found in the file epl-v10.html at the root of this distribution.
; By using this software in any fashion, you are agreeing to be bound by
; the terms of this license.
; You must not remove this notice, or any other, from this software.

(ns clj-facebook-graph.helper
  "Some helper functions."
  (:use [clj-http.client :only [unexceptional-status?]]
        [clj-facebook-graph.uri :only [make-uri]]
        [clojure.string :only [blank?]]
        ring.middleware.params)
  (:require [cheshire.core :as json])
  (:import
   (java.io PushbackReader ByteArrayInputStream InputStreamReader)))

(def facebook-base-url "https://graph.facebook.com")

(def facebook-fql-base-url "https://api.facebook.com/method/fql.query")

(defn read-json-from-body
  "convert body to a reader to be compatible with clojure.data.json 0.2.1
   In case body is a byte array, aka class [B"
  [body]
  (if (instance? String body)
    (json/parse-string body true)
    (with-open [reader (clojure.java.io/reader body)]
      (json/parse-stream reader true))))

(defn build-url [request]
  "Builds a URL string which corresponds to the information of the request."
  (let [{:keys [server-port server-name uri query-params scheme]} request]
    (str (make-uri {:scheme (name scheme)
                    :host server-name
                    :port server-port
                    :path uri
                    :query query-params}))))

(defn wrap-exceptions [client]
  "An alternative Ring-style middleware to the #'clj-http.core/wrap-exceptions. This
   one also extracts the body from the http response, which is very helpful to see
   the error description Facebook has returned as JSON document."
  (fn [req]
    (let [{:keys [status body] :as resp} (client req)]
      (if (or (not (clojure.core/get req :throw-exceptions true))
              (unexceptional-status? status))
        resp
        (throw (Exception. (str "Status: " status " body: " (slurp body))))))))

(defn wrap-print-request-map [client]
  "Simply prints the request map to *out*."
  (fn [req]
    (println req)
    (client req)))
