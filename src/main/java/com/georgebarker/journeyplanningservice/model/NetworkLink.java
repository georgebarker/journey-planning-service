package com.georgebarker.journeyplanningservice.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * I am a class responsible for defining the NetworkLink entity.
 */
public class NetworkLink {

    private static final float METERS_TO_KM = 1000f;
    private static final float METRES_TO_MILES = 1609.344f;

    private long linkId;

    private Float linkLength;
    private double linkCost;

    private long lanes;

    private String carriageway;
    private String direction;
    private String linkName;

    private long startNodeId;
    private long endNodeId;

    private double startNodeLatitude;
    private double startNodeLongitude;

    private double endNodeLatitude;
    private double endNodeLongitude;
    
    public long getLinkId() {
        return linkId;
    }
    
    public void setLinkId(long linkId) {
        this.linkId = linkId;
    }
    
    public long getStartNodeId() {
        return startNodeId;
    }

    public void setStartNodeId(long startNodeId) {
        this.startNodeId = startNodeId;
    }

    public long getEndNodeId() {
        return endNodeId;
    }

    public void setEndNodeId(long endNodeId) {
        this.endNodeId = endNodeId;
    }

    public String getCarriageway() {
        return carriageway;
    }

    public void setCarriageway(String carriageway) {
        this.carriageway = carriageway;
    }

    public long getLanes() {
        return lanes;
    }

    public void setLanes(long lanes) {
        this.lanes = lanes;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public Float getLinkLength() {
        return linkLength;
    }

    public void setLinkLength(Float linkLength) {
        this.linkLength = linkLength;
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }
    
    public void setEndNodeLatitude(double endNodeLatitude) {
        this.endNodeLatitude = endNodeLatitude;
    }

    public void setEndNodeLongitude(double endNodeLongitude) {
        this.endNodeLongitude = endNodeLongitude;

    }

    public void setStartNodeLatitude(double startNodeLatitude) {
        this.startNodeLatitude = startNodeLatitude;
    }

    public void setStartNodeLongitude(double startNodeLongitude) {
        this.startNodeLongitude = startNodeLongitude;
    }

    public double getStartNodeLongitude() {
        return startNodeLongitude;
    }

    public double getEndNodeLatitude() {
        return endNodeLatitude;
    }

    public double getEndNodeLongitude() {
        return endNodeLongitude;
    }

    public double getStartNodeLatitude() {
        return startNodeLatitude;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    @Override
    public int hashCode() {
        return (int) linkId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof NetworkLink)) {
            return false;
        }
        NetworkLink rhs = (NetworkLink) obj;
        return linkId == rhs.getLinkId();
    }

    public double getLinkCost() {
        return linkCost;
    }

    public void setLinkCost(double linkCost) {
        this.linkCost = linkCost;
    }

    public Float getLengthInKM() {
        return this.getLinkLength() / METERS_TO_KM;
    }

    public NetworkNode getStartNode() {
        return new NetworkNode(startNodeId, startNodeLatitude, startNodeLongitude);
    }

    public NetworkNode getEndNode() {
        return new NetworkNode(endNodeId, endNodeLatitude, endNodeLongitude);
    }

    /**
     * Converts the link length in metres to miles for calculations.
     * @return The length of the link in miles.
     */
    public Float getLinkLengthInMiles() {
        return this.getLinkLength() / METRES_TO_MILES;
    }
}
