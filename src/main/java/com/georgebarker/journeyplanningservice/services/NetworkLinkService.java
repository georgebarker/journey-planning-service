package com.georgebarker.journeyplanningservice.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.georgebarker.journeyplanningservice.dao.NetworkLinkDao;
import com.georgebarker.journeyplanningservice.model.NetworkLink;

@Service
public class NetworkLinkService {

    @Autowired
    NetworkLinkDao networkLinkDao;

    public NetworkLink findById(long linkId) {
        return networkLinkDao.findById(linkId);
    }

    public List<NetworkLink> retrieveAllNetworkLinks() {
        return networkLinkDao.retrieveAllNetworkLinks();
    }
    
    public NetworkLink getNetworkLinkFromList(List<NetworkLink> networkLinks, Long linkId) {
        for (NetworkLink networkLink : networkLinks) {
            if (networkLink.getLinkId() == linkId) {
                return networkLink;
            }
        }
        System.out.println("Couldn't find a link in this list, id: " + linkId);
        return null;
    }
    
    public List<Long> getLinkIdsFromNetworkLinkList(List<NetworkLink> networkLinks) {
        List<Long> linkIds = new ArrayList<>();
        
        for (NetworkLink networkLink : networkLinks) {
            linkIds.add(networkLink.getLinkId());
        }
        
        return linkIds;
    }
}
