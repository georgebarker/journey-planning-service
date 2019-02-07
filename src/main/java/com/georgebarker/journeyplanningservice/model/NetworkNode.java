package com.georgebarker.journeyplanningservice.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class NetworkNode {

    private long nodeId;
    private double longitude;
    private double latitude;

    public NetworkNode(long nodeId, double longitude, double latitude) {
		super();
		this.nodeId = nodeId;
		this.longitude = longitude;
		this.latitude = latitude;
	}

    public long getNodeId() {
        return nodeId;
    }

    public void setNodeId(long nodeId) {
        this.nodeId = nodeId;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    
    public double getLongitude() {
        return longitude;
    }

    
    public double getLatitude() {
        return latitude;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}