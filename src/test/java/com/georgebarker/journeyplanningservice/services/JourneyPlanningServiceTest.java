package com.georgebarker.journeyplanningservice.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
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

import com.georgebarker.journeyplanningservice.model.NetworkLink;
import com.georgebarker.journeyplanningservice.model.NetworkNode;
import com.georgebarker.journeyplanningservice.model.ProfileData;
import com.georgebarker.journeyplanningservice.model.ProfileDataPK;
import com.georgebarker.journeyplanningservice.model.Route;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class JourneyPlanningServiceTest {

    @MockBean
    private NetworkLinkService networkLinkService;

    @MockBean
    private ProfileDataService profileDataService;

    @Autowired
    JourneyPlanningService journeyPlanningService;

    long mockStartLinkId = 123L;
    NetworkLink startLink;
    NetworkNode startLinkStartNode;
    NetworkNode startLinkEndNode;

    long mockMiddleLinkId = 456L;
    NetworkLink middleLink;
    NetworkNode middleLinkStartNode;
    NetworkNode middleLinkEndNode;

    long mockEndLinkId = 789L;
    NetworkLink endLink;
    NetworkNode endLinkStartNode;
    NetworkNode endLinkEndNode;

    private Long dayType = 1L;
    private static final int THIRTY_MINUTES = 30;
    private static final int FIVE_MINUTES = 5;

    List<NetworkLink> networkLinks = new ArrayList<>();
    Map<NetworkLink, List<ProfileData>> mockProfileDataMap = new HashMap<>();

    private DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");

    private DateTime mockEarliestArrivalTime = formatter.parseDateTime("2019-07-02 00:00");
    private DateTime mockLatestArrivalTime = formatter.parseDateTime("2019-07-02 00:30");

    @Before
    public void before() {
        createMockData();
    }

    @Test
    public void testGetRoutes() {
        when(networkLinkService.findById(mockStartLinkId)).thenReturn(startLink);
        when(networkLinkService.findById(mockEndLinkId)).thenReturn(endLink);
        when(networkLinkService.retrieveAllNetworkLinks()).thenReturn(networkLinks);
        when(profileDataService.getProfileDataForLinks(networkLinks, mockEarliestArrivalTime, mockLatestArrivalTime))
                .thenReturn(mockProfileDataMap);

        for (NetworkLink networkLink : networkLinks) {
            List<ProfileData> profileDataForLink = mockProfileDataMap.get(networkLink);
            for (ProfileData profileData : profileDataForLink) {
                when(profileDataService.getProfileDataForBeginMinute(profileDataForLink, profileData.getBeginMinute(),
                        networkLink.getLinkId())).thenReturn(profileData);
            }
        }

        List<Route> routes = journeyPlanningService.getRoutes(mockStartLinkId, mockEndLinkId,
                mockEarliestArrivalTime.getMillis(), mockLatestArrivalTime.getMillis());
        assertEquals(4, routes.size());       
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetRoutesEndLinkNotFound() {
        when(networkLinkService.findById(mockStartLinkId)).thenReturn(startLink);
        when(networkLinkService.findById(mockEndLinkId)).thenReturn(null);
        journeyPlanningService.getRoutes(mockStartLinkId, mockEndLinkId, mockEarliestArrivalTime.getMillis(),
                mockLatestArrivalTime.getMillis());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetRoutesStartLinkNotFound() {
        when(networkLinkService.findById(mockStartLinkId)).thenReturn(null);
        when(networkLinkService.findById(mockEndLinkId)).thenReturn(endLink);
        journeyPlanningService.getRoutes(mockStartLinkId, mockEndLinkId, mockEarliestArrivalTime.getMillis(),
                mockLatestArrivalTime.getMillis());
    }

    /*
     * This test exists because the functionality for journey planning across
     * multiple days has not yet been implemented.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetRoutesLatestArrivalTimeIsNotSameDay() {
        journeyPlanningService.getRoutes(mockStartLinkId, mockEndLinkId, mockEarliestArrivalTime.getMillis(),
                mockLatestArrivalTime.plusDays(2).getMillis());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetRoutesStartLinkIdInvalid() {
        journeyPlanningService.getRoutes(0, mockEndLinkId, mockEarliestArrivalTime.getMillis(),
                mockLatestArrivalTime.getMillis());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetRoutesEndLinkIdInvalid() {
        journeyPlanningService.getRoutes(mockStartLinkId, 0, mockEarliestArrivalTime.getMillis(),
                mockLatestArrivalTime.getMillis());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetRoutesEarliestArrivalTimeInvalid() {
        journeyPlanningService.getRoutes(mockStartLinkId, mockEndLinkId, 0, mockLatestArrivalTime.getMillis());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetRoutesLatestArrivalTimeInvalid() {
        journeyPlanningService.getRoutes(mockStartLinkId, mockEndLinkId, mockEarliestArrivalTime.getMillis(), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetRoutesAllParametersInvalid() {
        journeyPlanningService.getRoutes(0, 0, 0, 0);
    }

    private void createMockData() {
        startLink = new NetworkLink();
        startLink.setLinkId(mockStartLinkId);
        startLink.setLinkCost(1L);
        startLink.setStartNodeId(4321L);
        startLink.setStartNodeLatitude(1D);
        startLink.setStartNodeLongitude(1D);
        startLink.setEndNodeId(4322L);
        startLink.setEndNodeLatitude(1.1D);
        startLink.setEndNodeLongitude(1.1D);
        startLink.setLinkLength(100.0F);

        middleLink = new NetworkLink();
        middleLink.setLinkId(mockMiddleLinkId);
        middleLink.setLinkCost(1L);
        middleLink.setStartNodeId(4322L);
        middleLink.setStartNodeLatitude(1.2D);
        middleLink.setStartNodeLongitude(1.2D);
        middleLink.setEndNodeId(4323L);
        middleLink.setEndNodeLatitude(1.3D);
        middleLink.setEndNodeLongitude(1.3D);
        middleLink.setLinkLength(100.0F);

        endLink = new NetworkLink();
        endLink.setLinkId(mockEndLinkId);
        endLink.setLinkCost(1L);
        endLink.setStartNodeId(4323L);
        endLink.setStartNodeLatitude(1.4D);
        endLink.setStartNodeLongitude(1.4D);
        endLink.setEndNodeId(4324L);
        endLink.setEndNodeLatitude(1.5D);
        endLink.setEndNodeLongitude(1.5D);
        endLink.setLinkLength(100.0F);

        networkLinks.add(startLink);
        networkLinks.add(middleLink);
        networkLinks.add(endLink);

        for (NetworkLink networkLink : networkLinks) {
            List<ProfileData> mockProfileData = new ArrayList<>();

            for (long count = 0; count <= THIRTY_MINUTES; count = count + FIVE_MINUTES) {
                ProfileData profileData = new ProfileData(new ProfileDataPK(dayType, networkLink.getLinkId(), count),
                        count + 5, 50L);
                mockProfileData.add(profileData);
            }
            mockProfileDataMap.put(networkLink, mockProfileData);
        }
    }
}