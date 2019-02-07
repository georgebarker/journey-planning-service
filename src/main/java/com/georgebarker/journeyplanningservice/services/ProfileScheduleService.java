package com.georgebarker.journeyplanningservice.services;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.georgebarker.journeyplanningservice.dao.ProfileScheduleDao;

@Service
public class ProfileScheduleService {

    @Autowired
    ProfileScheduleDao profileScheduleDao;
    
    public Long getDayTypeIdForDateTime(DateTime dateTime) {
        return profileScheduleDao.findById(dateTime.toDate()).get().getDayTypeId();
    }
}
