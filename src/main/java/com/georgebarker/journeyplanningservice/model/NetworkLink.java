package com.georgebarker.journeyplanningservice.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * I am a class responsible for defining the NetworkLink entity.
 */

public class NetworkLink {

	private static final float METERS_TO_KM = 1000f;

	private long linkId;

	private Float linkLength;
	private Double linkCost;

	private long lanes;

	private String carriageway;
	private String direction;
	private String linkName;
	private String linkType;

	private long startNodeId;
	private long endNodeId;

	private double startNodeLatitude;
	private double startNodeLongitude;

	private double endNodeLatitude;
	private double endNodeLongitude;

	/**
	 * I am a method which returns the linkId field. I am set to the id of the
	 * table, meaning that the linkId field is the primary key.
	 * 
	 * @return The linkId field.
	 */
	public long getLinkId() {
		return linkId;
	}

	/**
	 * I am a method which sets the linkId field.
	 * 
	 * @param linkId The value which is to be set to the linkId field.
	 */
	public void setLinkId(long linkId) {
		this.linkId = linkId;
	}

	/**
	 * I am a method which returns the startNodeId field.
	 * 
	 * @return The startNodeId field.
	 */
	public long getStartNodeId() {
		return startNodeId;
	}

	/**
	 * I am a method which sets the startNodeId field.
	 * 
	 * @param startNodeId The value which is to be set to the startNodeId field.
	 */
	public void setStartNodeId(long startNodeId) {
		this.startNodeId = startNodeId;
	}

	/**
	 * I am a method which returns the endNodeId field.
	 * 
	 * @return The endNodeId field.
	 */
	public long getEndNodeId() {
		return endNodeId;
	}

	/**
	 * I am a method which sets the endNodeId field.
	 * 
	 * @param endNodeId The value which is to be set to the endNodeId field.
	 */
	public void setEndNodeId(long endNodeId) {
		this.endNodeId = endNodeId;
	}

	/**
	 * I am a method which returns the carriageway field.
	 * 
	 * @return The carriageway field.
	 */
	public String getCarriageway() {
		return carriageway;
	}

	/**
	 * I am a method which sets the carriageway field.
	 * 
	 * @param carriageway The value which is to be set to the carriageway field.
	 */
	public void setCarriageway(String carriageway) {
		this.carriageway = carriageway;
	}

	/**
	 * I am a method which returns the lanes field.
	 * 
	 * @return The lanes field.
	 */
	public long getLanes() {
		return lanes;
	}

	/**
	 * I am a method which sets the lanes field.
	 * 
	 * @param lanes The value which is to be set to the lanes field.
	 */
	public void setLanes(long lanes) {
		this.lanes = lanes;
	}

	/**
	 * I am a method which returns the direction field.
	 * 
	 * @return The direction field.
	 */
	public String getDirection() {
		return direction;
	}

	/**
	 * I am a method which sets the direction field.
	 * 
	 * @param direction The value which is to be set to the direction field.
	 */
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

	public String getLinkType() {
		return linkType;
	}

	public void setLinkType(String linkType) {
		this.linkType = linkType;
	}

//    public String getTranslatedDirection() {
//        return translateNetworkModelDirectionTypeToDatex2DirectionType(this.direction);
//    }

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

	public Double getLinkCost() {
		return linkCost;
	}

	public void setLinkCost(Double linkCost) {
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
}
