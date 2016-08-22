(ns vrbo.dashboard
  (:require
   ;;[vrng.util :as u :refer [t state to-millis track-event]]
   ;;[vrng.sporktrum :as spork]
   [reagent.core :as reagent :refer [atom]]
   [reagent.session :as session]
   ;;[secretary.core :as secretary :include-macros true]
   [ajax.core :refer [PUT]]
   [clojure.string :as str]
   ;;[cljsjs.selectize]
   cljsjs.moment
   goog.string.format
   goog.string)
  (:require-macros [cljs.core :refer [exists?]]))

;; ------------------------------
;; state

(defonce state (atom {}))

;; ------------------------------
;; demo data

(defn populate-with-demo-data []
  (swap! state assoc :lines
         [{:key         "sophie-glaser1"
           :instance-id "i-065618ba"
           :device      "butt"
           :venue-state "offline"
           :talk-state  "archived"
           :server-heartbeat-progress 50}
          {:key         "sophie-glaser2"
           :instance-id "i-065618bb"
           :device      "darkice"
           :venue-state "available"
           :talk-state  "prelive"
           :server-heartbeat-progress 50}
          {:key         "sophie-glaser3"
           :instance-id "i-065618bc"
           :device      "box: Aristotele"
           :venue-state "awaiting_stream"
           :talk-state  "live"
           :server-heartbeat-progress 50}
          ]))

(populate-with-demo-data)

;; ------------------------------
;; components

(defn now-comp []
  [:div#current-time-holder
   [:div#current-time-badge
    (.format (:now @state) "hh:mm:ss")]])

(defn line-comp [line]
  ^{:key (line :key)}
  [:div.venue-tab
   [:div.top-row.clearfix
    [:div.play-button-holder
     [:button.play-button
      [:svg [:use {:xlink:href "#icon-sound_on"}]]]]
    [:div.venue-info
     [:p [:span.venue-name (line :key)] " " [:span.venue-state.float-right {:class (line :talk-state)} (line :venue-state)]]
     [:p.state-badges
      [:span.device-type (line :device)]
      [:span.device-type {:class (line :talk-state)} (line :talk-state)]
      [:span.device-type (line :server-heartbeat-progress)]]]]])

(defn lines-comp [lines]
  [:div#venue-column (doall (map line-comp lines))])

(defn main-comp []
  [:main
   [:div#time-grid.ui-draggable.ui-draggable-handle
    {:style {:left "400px" :top "0px"}}
    [:div.marker "10:00"]
    [:div.marker.half]
    [:div.marker "11:00"]
    [:div.marker.half]
    [:div.marker "12:00"]
    [:div.marker.half]
    [:div.marker "13:00"]
    [:div.marker.half]
    [:div.marker "14:00"]
    [:div.marker.half]
    [:div.venue-timeslots
     [:div.venue-timeslot-row
      [:div.point-in-time {:style {:margin-left "450px"}}]
      [:div.time-slot-holder
       {:style {:margin-left "400px"}}
       [:p.time-slot-title "This is a talky talk"]
       [:div.time-slot-fill]
       [:div.time-slot]]]
     [:div.venue-timeslot-row
      [:div.time-slot-holder
       {:style {:margin-left "800px"}}
       [:p.time-slot-title "This is really awesome"]
       [:div.time-slot]]]
     [:div.venue-timeslot-row
      [:div.time-slot-holder
       {:style {:margin-left "200px"}}
       [:p.time-slot-title "Funny Porcelain Handpuppet"]
       [:div.time-slot-fill]
       [:div.time-slot]
       [:div.time-slot-holder
        {:style {:margin-left "400px"}}
        [:p.time-slot-title "Gagnly Steel Kerchiefs"]
        [:div.time-slot-fill {:style {:width "102px"}}]
        [:div.time-slot]]]]]
    [:div#current-time-line]
    [now-comp]]
   [:div#dashboard
    [lines-comp (@state :lines)]]])

;; -------------------------
;; briefings (initial data)

;; -------------------------
;; message handlers

(defn server-heartbeat-handler [heartbeat]
  (swap! state assoc-in [:server-heartbeat (heartbeat :token)] (js/Date.)))

(defn client-heartbeat-handler [heartbeat]
  (swap! state assoc-in [:client-heartbeat (heartbeat :identifier)] (js/Date.)))

(defn client-report-handler [data]
  (let [key (data :identifier)
        path [:client-report key]]
    (swap! state assoc-in path data)))

(defn server-stats-handler [data]
  (let [key (data :slug)
        path [:server-stats key]]
    (swap! state assoc-in path data)))

(defn venues-handler [data]
  (let [key ((data :venue) :slug)
        path [:venue key]]
    (swap! state assoc-in path data)))

(defn talks-handler [data]
  (let [key ((data :talk) :slug)
        path [:talk key]]
    (swap! state assoc-in path data)))

(defn client-event-handler [data]
  (let [key (data :identifier)
        path [:client-event key]]
    (swap! state assoc-in path data)))

(defn connections-handler [data]
  (let [key (data :slug)
        path [:connection key]]
    (swap! state assoc-in path (data :event))))

;; -------------------------
;; init helpers

(defn update-loop []
  (js/requestAnimationFrame update-loop)
  (swap! state assoc :now (js/moment)))

(defn subscribe [channel handler]
  (.subscribe js/fayeClient channel
              #(handler (js->clj %  :keywordize-keys true))))

(defn mount-root []
  (reagent/render [main-comp] (.getElementById js/document "livedashboard")))

;; -------------------------
;; initialize

(defn init! []
  (mount-root))

(update-loop)

(subscribe "/report"            client-report-handler)
(subscribe "/heartbeat"         client-heartbeat-handler)
(subscribe "/admin/stats"       server-stats-handler)
(subscribe "/admin/venues"      venues-handler)
(subscribe "/admin/talks"       talks-handler)
(subscribe "/admin/connections" connections-handler)
(subscribe "/server/heartbeat"  server-heartbeat-handler)
(subscribe "/event/devices"     client-event-handler)
