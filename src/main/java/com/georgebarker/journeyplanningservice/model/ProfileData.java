package com.georgebarker.journeyplanningservice.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQuery(name = "ProfileData.findByLinkIdsAndDayTypeId", query = "SELECT p FROM ProfileData p WHERE p.profileDataPK.linkId in (?1) AND p.profileDataPK.dayTypeId = ?2 order by p.profileDataPK.linkId desc")
@Table(name = "FUSED_PROFILE_BUCKET_SUMMARY")
public class ProfileData {

    @EmbeddedId
    ProfileDataPK profileDataPK;

    private Long endMinute;
    private Long speedMph;

    public ProfileData() {
    }
    
    //Constructor that makes it easier for testing
    public ProfileData(ProfileDataPK profileDataPK, Long endMinute, Long speedMph) {
        this.profileDataPK = profileDataPK;
        this.endMinute = endMinute;
        this.speedMph = speedMph;
    }

    public ProfileDataPK getProfileDataPK() {
        return profileDataPK;
    }

    public void setProfileDataPK(ProfileDataPK id) {
        this.profileDataPK = id;
    }

    public Long getDayTypeId() {
        return profileDataPK.getDayTypeId();
    }

    public Long getLinkId() {
        return profileDataPK.getLinkId();
    }

    public Long getBeginMinute() {
        return profileDataPK.getBeginMinute();
    }

    @Column(name = "END_MINUTE")
    public Long getEndMinute() {
        return endMinute;
    }

    public void setEndMinute(Long endMinute) {
        this.endMinute = endMinute;
    }

    @Column(name = "SPEED_MPH")
    public Long getSpeedMph() {
        return speedMph;
    }

    public void setSpeedMph(Long speedMph) {
        this.speedMph = speedMph;
    }
}
