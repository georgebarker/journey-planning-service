package com.georgebarker.journeyplanningservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.georgebarker.journeyplanningservice.model.NetworkLink;
import com.georgebarker.journeyplanningservice.services.JourneyPlanningService;

@RestController
public class JourneyPlanningController {

	@Autowired
	JourneyPlanningService journeyPlanningService;

	@GetMapping(value = "/route/{startLinkId}/{endLinkId}")
	public List<NetworkLink> getRouteForLinks(@PathVariable long startLinkId, @PathVariable long endLinkId) {
		return journeyPlanningService.getRoute(startLinkId, endLinkId);
	}

}
