package com.georgebarker.journeyplanningservice.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.georgebarker.journeyplanningservice.dao.ProfileDataDao;
import com.georgebarker.journeyplanningservice.model.NetworkLink;
import com.georgebarker.journeyplanningservice.model.ProfileData;

@Service
public class ProfileDataService {

    @Autowired
    ProfileDataDao profileDataDao;

    @Autowired
    NetworkLinkService networkLinkService;

    @Autowired
    ProfileScheduleService profileScheduleService;

    public Map<NetworkLink, List<ProfileData>> getProfileDataForLinks(List<NetworkLink> networkLinks,
            DateTime earliestArrivalTime, DateTime latestArrivalTime) {
        /*
         * Still need to figure out what to do when a user selects a route that crosses
         * two different days.
         */
        Long startDateDayTypeId = profileScheduleService.getDayTypeIdForDateTime(earliestArrivalTime);
        List<ProfileData> profileData = getProfileDataForNetworkLinksAndDayTypeId(networkLinks, startDateDayTypeId);

        Map<NetworkLink, List<ProfileData>> profileDataMap = createProfileDataMap(networkLinks, profileData);

        return profileDataMap;

    }

    public ProfileData getProfileDataForBeginMinute(List<ProfileData> profileData, long beginMinute) {
        for (ProfileData data : profileData) {
            if (data.getProfileDataPK().getBeginMinute() == beginMinute) {
                return data;
            }
        }
        System.out.println("Couldn't find profile data for link: " + profileData.get(0).getLinkId()
                + " at begin minute: " + beginMinute);
        return null;
    }

    private List<ProfileData> getProfileDataForNetworkLinksAndDayTypeId(List<NetworkLink> networkLinks,
            Long dayTypeId) {
        List<Long> linkIds = networkLinkService.getLinkIdsFromNetworkLinkList(networkLinks);
        return profileDataDao.findByLinkIdsAndDayTypeId(linkIds, dayTypeId);
    }

    private Map<NetworkLink, List<ProfileData>> createProfileDataMap(List<NetworkLink> networkLinks,
            List<ProfileData> profileData) {
        Map<NetworkLink, List<ProfileData>> profileDataMap = new HashMap<>();

        for (ProfileData data : profileData) {
            NetworkLink networkLink = networkLinkService.getNetworkLinkFromList(networkLinks, data.getLinkId());
            List<ProfileData> profileDataForLink = profileDataMap.get(networkLink);
            if (profileDataForLink == null) {
                profileDataForLink = new ArrayList<>();
                profileDataForLink.add(data);
                profileDataMap.put(networkLink, profileDataForLink);
            } else {
                profileDataForLink.add(data);
            }
        }

        return profileDataMap;
    }
}
