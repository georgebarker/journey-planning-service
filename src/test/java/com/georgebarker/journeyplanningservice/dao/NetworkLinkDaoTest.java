package com.georgebarker.journeyplanningservice.dao;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import com.georgebarker.journeyplanningservice.model.NetworkLink;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@TestPropertySource(properties = { "endpoint.networkLink=TEST_ENDPOINT", })
public class NetworkLinkDaoTest {

    public static String MOCK_NETWORK_LINK_ENDPOINT = "TEST_ENDPOINT";

    private static final long LINK_ID_1 = 123L;
    private static final long LINK_ID_2 = 456L;
    private static final long LINK_ID_3 = 789L;

    @MockBean
    RestTemplate restTemplate;

    @MockBean
    ResponseEntity<List<NetworkLink>> responseEntity;

    @Autowired
    NetworkLinkDao networkLinkDao;

    NetworkLink mockNetworkLink1;
    NetworkLink mockNetworkLink2;
    NetworkLink mockNetworkLink3;
    List<NetworkLink> mockNetworkLinks;

    @Before
    public void before() {
        createMockNetworkLinks();
    }

    @Test
    public void testFindById() {
        when(restTemplate.getForObject(MOCK_NETWORK_LINK_ENDPOINT + LINK_ID_1, NetworkLink.class))
                .thenReturn(mockNetworkLink1);
        NetworkLink returnedNetworkLink = networkLinkDao.findById(LINK_ID_1);
        assertEquals(mockNetworkLink1, returnedNetworkLink);
    }

    @Test
    public void testRetrieveAllNetworkLinks() {
        ParameterizedTypeReference<List<NetworkLink>> reference = new ParameterizedTypeReference<List<NetworkLink>>() {
        };
        when(restTemplate.exchange(MOCK_NETWORK_LINK_ENDPOINT, HttpMethod.GET, null, reference))
                .thenReturn(responseEntity);
        when(responseEntity.getBody()).thenReturn(mockNetworkLinks);

        List<NetworkLink> returnedNetworkLinks = networkLinkDao.retrieveAllNetworkLinks();
        assertEquals(mockNetworkLinks, returnedNetworkLinks);
    }

    private void createMockNetworkLinks() {
        mockNetworkLink1 = new NetworkLink();
        mockNetworkLink1.setLinkId(LINK_ID_1);
        mockNetworkLink2 = new NetworkLink();
        mockNetworkLink2.setLinkId(LINK_ID_2);
        mockNetworkLink3 = new NetworkLink();
        mockNetworkLink3.setLinkId(LINK_ID_3);

        mockNetworkLinks = new ArrayList<>();
        mockNetworkLinks.add(mockNetworkLink1);
        mockNetworkLinks.add(mockNetworkLink2);
        mockNetworkLinks.add(mockNetworkLink3);
    }

}
