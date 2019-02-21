package com.georgebarker.journeyplanningservice.model;

import java.util.List;

import org.joda.time.DateTime;

public class Route {
    private List<NetworkLink> route;
    private double minutesToTravel;
    private DateTime arrivalTime;
    private boolean isOptimalRoute;

    public List<NetworkLink> getRoute() {
        return route;
    }

    public void setRoute(List<NetworkLink> route) {
        this.route = route;
    }

    public double getMinutesToTravel() {
        return minutesToTravel;
    }

    public void setMinutesToTravel(double minutesToTravel) {
        this.minutesToTravel = minutesToTravel;
    }

    public long getArrivalTimeMillis() {
        return arrivalTime.getMillis();
    }

    public void setArrivalTime(DateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
    
    public long getDepartureTimeMillis() {
        return arrivalTime.minusMinutes((int) getMinutesToTravel()).getMillis();
    }

    public boolean isOptimalRoute() {
        return isOptimalRoute;
    }

    public void setOptimalRoute(boolean isOptimalRoute) {
        this.isOptimalRoute = isOptimalRoute;
    }

}