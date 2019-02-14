package com.georgebarker.journeyplanningservice.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.georgebarker.journeyplanningservice.dao.ProfileDataDao;
import com.georgebarker.journeyplanningservice.model.NetworkLink;
import com.georgebarker.journeyplanningservice.model.ProfileData;
import com.georgebarker.journeyplanningservice.model.ProfileDataPK;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ProfileDataServiceTest {

    @MockBean
    ProfileDataDao profileDataDao;

    @MockBean
    NetworkLinkService networkLinkService;

    @MockBean
    ProfileScheduleService profileScheduleService;

    @Autowired
    ProfileDataService profileDataService;

    NetworkLink mockLink1;
    NetworkLink mockLink2;
    ProfileData mockProfileData1;
    ProfileData mockProfileData2;
    ProfileData mockProfileData3;
    ProfileData mockProfileData4;
    List<NetworkLink> links = new ArrayList<>();
    List<Long> linkIds = new ArrayList<>();
    List<ProfileData> profileData = new ArrayList<>();
    private DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
    private DateTime mockEarliestArrivalTime = formatter.parseDateTime("2019-07-02 00:00");
    private DateTime mockLatestArrivalTime = formatter.parseDateTime("2019-07-02 00:30");
    private Long dayType = 1L;

    @Before
    public void before() {
        createMockData();
    }

    @Test
    public void testGetProfileDataForLinks() {
        when(profileScheduleService.getDayTypeIdForDateTime(mockEarliestArrivalTime)).thenReturn(dayType);
        when(networkLinkService.getLinkIdsFromNetworkLinkList(links)).thenReturn(linkIds);
        when(networkLinkService.getNetworkLinkFromList(links, 123L)).thenReturn(mockLink1);
        when(networkLinkService.getNetworkLinkFromList(links, 456L)).thenReturn(mockLink2);
        when(profileDataDao.findByLinkIdsAndDayTypeId(linkIds, dayType)).thenReturn(profileData);
        
        Map<NetworkLink, List<ProfileData>> profileDataMap = profileDataService.getProfileDataForLinks(links,
                mockEarliestArrivalTime, mockLatestArrivalTime);
        assertEquals(profileDataMap.get(mockLink1).get(0), mockProfileData1);
        assertEquals(profileDataMap.get(mockLink1).get(1), mockProfileData2);
        assertEquals(profileDataMap.get(mockLink2).get(0), mockProfileData3);
        assertEquals(profileDataMap.get(mockLink2).get(1), mockProfileData4);
    }
    
    @Test
    public void testGetProfileDataForBeginMinute() {
        ProfileData returnedProfileData = profileDataService.getProfileDataForBeginMinute(profileData, 5L, mockLink1.getLinkId());
        assertEquals(mockProfileData2, returnedProfileData);
    }
    
    @Test
    public void testGetProfileDataForBeginMinuteNotFound() {
        ProfileData returnedProfileData = profileDataService.getProfileDataForBeginMinute(profileData, 25L, mockLink1.getLinkId());
        assertNull(returnedProfileData);
    }

    private void createMockData() {
        mockLink1 = new NetworkLink();
        mockLink1.setLinkId(123L);
        mockLink2 = new NetworkLink();
        mockLink2.setLinkId(456L);

        links.add(mockLink1);
        links.add(mockLink2);
        
        linkIds.add(123L);
        linkIds.add(456L);
        
        mockProfileData1 = new ProfileData(new ProfileDataPK(dayType, mockLink1.getLinkId(), 0L), 5L, 50L);
        profileData.add(mockProfileData1);
        mockProfileData2 = new ProfileData(new ProfileDataPK(dayType, mockLink1.getLinkId(), 5L), 10L, 52L);
        profileData.add(mockProfileData2);
        mockProfileData3 = new ProfileData(new ProfileDataPK(dayType, mockLink2.getLinkId(), 10L), 15L, 54L);
        profileData.add(mockProfileData3);
        mockProfileData4 = new ProfileData(new ProfileDataPK(dayType, mockLink2.getLinkId(), 15L), 20L, 56L);
        profileData.add(mockProfileData4);
    }

}
