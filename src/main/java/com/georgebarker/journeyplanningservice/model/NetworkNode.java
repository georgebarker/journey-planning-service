package com.georgebarker.journeyplanningservice.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * I am class responsible for defining the NetworkNode entity. I define what
 * fields are to be in the NetworkNode table.
 * 
 */
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

	/**
     * I am a method which returns the nodeId field. I am set to the id of the
     * table, meaning that the nodeId field is the primary key.
     * 
     * @return The nodeId field.
     */
    public long getNodeId() {
        return nodeId;
    }

    /**
     * I am a method which sets the nodeId field.
     * 
     * @param nodeId
     * The value which is to be set to the nodeId field.
     */
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

    @Override
    public int hashCode() {
        return (int) (nodeId % Integer.MAX_VALUE);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof NetworkNode)) {
            return false;
        }
        return nodeId == ((NetworkNode) obj).nodeId;
    }

}