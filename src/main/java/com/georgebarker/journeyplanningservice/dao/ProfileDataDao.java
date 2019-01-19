package com.georgebarker.journeyplanningservice.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.georgebarker.journeyplanningservice.model.ProfileData;
import com.georgebarker.journeyplanningservice.model.ProfileDataPK;

@Repository
public interface ProfileDataDao extends CrudRepository<ProfileData, ProfileDataPK> {
    List<ProfileData> findByLinkIdsAndDayTypeId(List<Long> linkIds, Long dayTypeId);
    
}
