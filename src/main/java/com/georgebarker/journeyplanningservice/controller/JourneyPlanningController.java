package com.georgebarker.journeyplanningservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.georgebarker.journeyplanningservice.model.Route;
import com.georgebarker.journeyplanningservice.services.JourneyPlanningService;

@RestController
public class JourneyPlanningController {

    @Autowired
    JourneyPlanningService journeyPlanningService;

    @GetMapping(value = "/route/{startLinkId}/{endLinkId}/{earliestArrivalTimeMillis}/{latestArrivalTimeMillis}")
    public List<Route> getRouteForLinks(@PathVariable long startLinkId, @PathVariable long endLinkId,
            @PathVariable long earliestArrivalTimeMillis, @PathVariable long latestArrivalTimeMillis) {
        return journeyPlanningService.getRoutes(startLinkId, endLinkId, earliestArrivalTimeMillis,
                latestArrivalTimeMillis);
    }

}
