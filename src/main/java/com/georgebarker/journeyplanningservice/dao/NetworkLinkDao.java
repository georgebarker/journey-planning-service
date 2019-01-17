package com.georgebarker.journeyplanningservice.dao;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.georgebarker.journeyplanningservice.model.NetworkLink;
import com.georgebarker.journeyplanningservice.util.Endpoints;

@Component
public class NetworkLinkDao {

	public NetworkLink findById(long linkId) {
		return new RestTemplate().getForObject(Endpoints.NETWORK_LINK_ENDPOINT + linkId, NetworkLink.class);

	}

	public List<NetworkLink> retrieveAllNetworkLinks() {
		return new RestTemplate().exchange(Endpoints.NETWORK_LINK_ENDPOINT, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<NetworkLink>>() {
				}).getBody();
	}

}
