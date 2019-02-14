package com.georgebarker.journeyplanningservice.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.georgebarker.journeyplanningservice.dao.NetworkLinkDao;
import com.georgebarker.journeyplanningservice.model.NetworkLink;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class NetworkLinkServiceTest {

    @MockBean
    NetworkLinkDao networkLinkDao;

    @Autowired
    NetworkLinkService networkLinkService;

    NetworkLink mockLink1;
    NetworkLink mockLink2;
    List<NetworkLink> links = new ArrayList<>();

    @Before
    public void before() {
        createMockLinks();
    }

    @Test
    public void testFindById() {
        when(networkLinkDao.findById(123L)).thenReturn(mockLink1);
        NetworkLink returnedNetworkLink = networkLinkService.findById(123L);
        assertEquals(mockLink1, returnedNetworkLink);
    }

    @Test
    public void testRetrieveAllNetworkLinks() {
        when(networkLinkDao.retrieveAllNetworkLinks()).thenReturn(links);
        List<NetworkLink> returnedNetworkLinks = networkLinkService.retrieveAllNetworkLinks();
        assertEquals(links, returnedNetworkLinks);
    }

    @Test
    public void testGetNetworkLinkFromList() {
        NetworkLink returnedNetworkLink = networkLinkService.getNetworkLinkFromList(links, 123L);
        assertEquals(mockLink1, returnedNetworkLink);
    }

    @Test
    public void testGetNetworkLinkFromListNotFound() {
        NetworkLink returnedNetworkLink = networkLinkService.getNetworkLinkFromList(links, 1L);
        assertNull(returnedNetworkLink);
    }

    @Test
    public void testGetLinkIdsFromNetworkLinkList() {
        List<Long> returnedLinkIds = networkLinkService.getLinkIdsFromNetworkLinkList(links);
        assertEquals(returnedLinkIds.size(), 2);
        assertEquals(returnedLinkIds.get(0), new Long(123));
        assertEquals(returnedLinkIds.get(1), new Long(456));
    }

    private void createMockLinks() {
        mockLink1 = new NetworkLink();
        mockLink1.setLinkId(123L);
        mockLink2 = new NetworkLink();
        mockLink2.setLinkId(456L);

        links.add(mockLink1);
        links.add(mockLink2);
    }

}
