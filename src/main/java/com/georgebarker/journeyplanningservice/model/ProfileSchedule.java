package com.georgebarker.journeyplanningservice.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PROFILE_SCHEDULE")
public class ProfileSchedule {

    private Date profileDate;

    private Long DayTypeId;

    public ProfileSchedule() {

    }

    @Id
    @Column(name = "PROFILE_DATE")
    public Date getProfileDate() {
        return profileDate;
    }

    public void setProfileDate(Date profileDate) {
        this.profileDate = profileDate;
    }

    @Column(name = "DAY_TYPE_ID")
    public Long getDayTypeId() {
        return DayTypeId;
    }

    public void setDayTypeId(Long dayTypeId) {
        DayTypeId = dayTypeId;
    }

}
