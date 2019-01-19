package com.georgebarker.journeyplanningservice.dao;

import java.util.Date;

import org.springframework.data.repository.CrudRepository;

import com.georgebarker.journeyplanningservice.model.ProfileSchedule;

public interface ProfileScheduleDao extends CrudRepository<ProfileSchedule, Date> {

}
