package com.georgebarker.journeyplanningservice.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ProfileDataPK implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -6790278329081072556L;
    private Long dayTypeId;
    private Long linkId;
    private Long beginMinute;

    public ProfileDataPK() {
    }

    public ProfileDataPK(Long dayTypeId, Long linkId) {
        this.dayTypeId = dayTypeId;
        this.linkId = linkId;
    }
    
    // Constructor that's easier for testing with
    public ProfileDataPK(Long dayTypeId, Long linkId, Long beginMinute) {
        this(dayTypeId, linkId);
        this.beginMinute = beginMinute;
    }

    @Column(name = "DAY_TYPE_ID")
    public long getDayTypeId() {
        return dayTypeId;
    }

    public void setDayTypeId(Long dayTypeId) {
        this.dayTypeId = dayTypeId;
    }

    @Column(name = "LINK_ID")
    public long getLinkId() {
        return linkId;
    }

    public void setLinkId(Long linkId) {
        this.linkId = linkId;
    }

    @Column(name = "BEGIN_MINUTE")
    public long getBeginMinute() {
        return beginMinute;
    }

    public void setBeginMinute(Long beginMinute) {
        this.beginMinute = beginMinute;
    }

    @Override
    public int hashCode() {
        final long prime = 31;
        long result = 1;
        result = prime * result + beginMinute;
        result = prime * result + dayTypeId;
        result = prime * result + linkId;
        return (int) result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ProfileDataPK other = (ProfileDataPK) obj;
        if (beginMinute != other.beginMinute)
            return false;
        if (dayTypeId != other.dayTypeId)
            return false;
        if (linkId != other.linkId)
            return false;
        return true;
    }
}