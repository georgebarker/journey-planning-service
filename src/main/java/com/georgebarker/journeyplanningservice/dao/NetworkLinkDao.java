package com.georgebarker.journeyplanningservice.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.georgebarker.journeyplanningservice.model.NetworkLink;

@Component
public class NetworkLinkDao {

    @Value("${endpoint.networkLink}")
    private String networkLinkEndpoint;

    @Autowired
    RestTemplate restTemplate;

    public NetworkLink findById(long linkId) {
        return restTemplate.getForObject(networkLinkEndpoint + linkId, NetworkLink.class);
    }

    public List<NetworkLink> retrieveAllNetworkLinks() {
        ParameterizedTypeReference<List<NetworkLink>> reference = new ParameterizedTypeReference<List<NetworkLink>>() {
        };
        ResponseEntity<List<NetworkLink>> responseEntity = restTemplate.exchange(networkLinkEndpoint, HttpMethod.GET,
                null, reference);
        return responseEntity.getBody();
    }
}
