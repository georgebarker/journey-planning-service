package com.georgebarker.journeyplanningservice.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicReference;

import org.jgrapht.EdgeFactory;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.georgebarker.journeyplanningservice.model.NetworkLink;
import com.georgebarker.journeyplanningservice.model.NetworkLinkEdge;
import com.georgebarker.journeyplanningservice.model.NetworkNode;
import com.georgebarker.journeyplanningservice.model.ProfileData;
import com.georgebarker.journeyplanningservice.model.Route;

/**
 *
 * I am a class which provides spatial functions for the network links
 *
 */
@Service
public class JourneyPlanningService {

    private static final Logger LOG = LoggerFactory.getLogger(JourneyPlanningService.class);
    private static final int FIFTEEN_MINUTES = 15;
    private static final int THIRTY_MINUTES = 30;
    private static final int FORTY_FIVE_MINUTES = 45;
    private static final int ON_THE_HOUR = 00;
    private static final int MINUTES_IN_HOUR = 60;

    @Autowired
    private NetworkLinkService networkLinkService;

    @Autowired
    private ProfileDataService profileDataService;

    private final AtomicReference<DefaultDirectedWeightedGraph<NetworkNode, NetworkLinkEdge>> journeyTimeGraph = new AtomicReference<>();

    public List<Route> getRoutes(final long startLinkId, final long endLinkId, long earliestArrivalTimeMillis,
            long latestArrivalTimeMillis) {
        final List<NetworkLink> networkLinkRoute = new LinkedList<>();
        final NetworkLink startLink = networkLinkService.findById(startLinkId);
        final NetworkLink endLink = networkLinkService.findById(endLinkId);
        NetworkNode startNode = startLink.getEndNode();
        if (startLink == endLink) {
            startNode = startLink.getStartNode();
        }

        if (journeyTimeGraph.get() == null) {
            createJourneyTimeGraph();
        }

        final DijkstraShortestPath<NetworkNode, NetworkLinkEdge> pathFinder = new DijkstraShortestPath<>(
                journeyTimeGraph.get(), startNode, endLink.getEndNode());

        final List<NetworkLinkEdge> pathEdgeList = pathFinder.getPathEdgeList();
        addEdgesToList(startLink, endLink, networkLinkRoute, pathEdgeList);
        
        DateTime earliestArrivalTime = new DateTime(earliestArrivalTimeMillis);
        DateTime latestArrivalTime = new DateTime(latestArrivalTimeMillis);

        return createRoutesForSpecifiedInterval(earliestArrivalTime, latestArrivalTime, networkLinkRoute);
    }


    private List<Route> createRoutesForSpecifiedInterval(DateTime earliestArrivalTime, DateTime latestArrivalTime,
            final List<NetworkLink> networkLinkRoute) {
        List<DateTime> intervalList = createIntervalList(earliestArrivalTime, latestArrivalTime);

        Map<NetworkLink, List<ProfileData>> profileDataMap = profileDataService.getProfileDataForLinks(networkLinkRoute,
                earliestArrivalTime, latestArrivalTime);

        List<Route> routes = new ArrayList<>();
        for (DateTime interval : intervalList) {
            Route route = createRoute(networkLinkRoute, profileDataMap, interval);
            routes.add(route);
        }

        setOptimalRoute(routes);

        return routes;
    }
   

    private void setOptimalRoute(List<Route> routes) {
        Route route = Collections.min(routes, Comparator.comparing(r -> r.getMinutesToTravel()));
        route.setOptimalRoute(true);
    }

    private Route createRoute(List<NetworkLink> networkLinkRoute, Map<NetworkLink, List<ProfileData>> profileDataMap,
            DateTime arrivalTime) {

        List<NetworkLink> recalculatedRoute = new ArrayList<>(networkLinkRoute);
        Collections.reverse(recalculatedRoute);

        long beginMinute = arrivalTime.getMinuteOfDay();
        double minutesToTravelRoute = 0L;
        for (NetworkLink networkLink : recalculatedRoute) {
            List<ProfileData> profileDataForLink = profileDataMap.get(networkLink);
            ProfileData data = profileDataService.getProfileDataForBeginMinute(profileDataForLink, beginMinute);
            double linkCost = data.getSpeedMph();
            networkLink.setLinkCost(linkCost);
            float linkLengthInMiles = networkLink.getLinkLengthInMiles();
            double minutesToTravelLink = (linkLengthInMiles / linkCost) * MINUTES_IN_HOUR;
            minutesToTravelRoute += minutesToTravelLink;
            beginMinute = calculateNewBeginMinute(beginMinute, minutesToTravelLink);
        }

        Collections.reverse(recalculatedRoute);

        Route route = new Route();
        route.setArrivalTime(arrivalTime);
        route.setMinutesToTravel(minutesToTravelRoute);
        route.setRoute(recalculatedRoute);
        return route;
    }

    private void addEdgesToList(final NetworkLink startLink, final NetworkLink endLink, final List<NetworkLink> route,
            final List<NetworkLinkEdge> pathEdgeList) {
        if (pathEdgeList != null) {
            if (startLink != endLink) {
                route.add(startLink);
            }
            for (final NetworkLinkEdge edge : pathEdgeList) {
                route.add(edge.getLink());
            }
        } else {
            LOG.info(String.format("Unable to find path between nodes %d to %d", startLink.getLinkId(),
                    endLink.getLinkId()));
        }
    }

    public List<NetworkLink> getAffectedLinks(final NetworkNode upstreamNode, final NetworkNode framedNode) {
        final List<NetworkLink> route = new LinkedList<>();
        final DijkstraShortestPath<NetworkNode, NetworkLinkEdge> pathFinder = new DijkstraShortestPath<>(
                journeyTimeGraph.get(), upstreamNode, framedNode);
        final List<NetworkLinkEdge> pathEdgeList = pathFinder.getPathEdgeList();
        if (pathEdgeList != null) {
            for (final NetworkLinkEdge edge : pathEdgeList) {
                route.add(edge.getLink());
            }
        } else {
            LOG.info(String.format("Unable to find path between nodes %s to %s", upstreamNode, framedNode));
        }
        return route;
    }

    private void createJourneyTimeGraph() {
        final Set<Long> addedNodes = new TreeSet<>();

        List<NetworkLink> networkLinks = networkLinkService.retrieveAllNetworkLinks();

        final DefaultDirectedWeightedGraph<NetworkNode, NetworkLinkEdge> journeyTimeGraphBuilt = createEmptyGraph(
                networkLinks);
        for (final NetworkLink link : networkLinks) {
            addNodeIfNotPresent(journeyTimeGraphBuilt, addedNodes, link.getStartNode());
            addNodeIfNotPresent(journeyTimeGraphBuilt, addedNodes, link.getEndNode());
            final NetworkLinkEdge addEdge = journeyTimeGraphBuilt.addEdge(link.getStartNode(), link.getEndNode());
            if (addEdge != null) {
                journeyTimeGraphBuilt.setEdgeWeight(addEdge, addEdge.getWeight());
            } else {
                LOG.warn(
                        "No edge added for link id {}. Most likely cause is another link already added with the same start and end node id.",
                        link.getLinkId());
            }
        }
        journeyTimeGraph.set(journeyTimeGraphBuilt);
    }

    private void addNodeIfNotPresent(final DefaultDirectedWeightedGraph<NetworkNode, NetworkLinkEdge> journeyTimeGraph,
            final Set<Long> addedNodes, final NetworkNode node) {
        final long nodeId = node.getNodeId();
        if (!addedNodes.contains(nodeId)) {
            journeyTimeGraph.addVertex(node);
            addedNodes.add(nodeId);
        }
    }

    private DefaultDirectedWeightedGraph<NetworkNode, NetworkLinkEdge> createEmptyGraph(
            final List<NetworkLink> networkLinks) {

        final DefaultDirectedWeightedGraph<NetworkNode, NetworkLinkEdge> journeyTimeGraphBuilt = new DefaultDirectedWeightedGraph<>(
                new EdgeFactory<NetworkNode, NetworkLinkEdge>() {

                    @Override
                    public NetworkLinkEdge createEdge(final NetworkNode startNode, final NetworkNode endNode) {
                        final NetworkLink link = findLinkWithNodes(startNode, endNode);
                        return new NetworkLinkEdge(startNode, endNode, link.getLinkCost(), link);
                    }

                    private NetworkLink findLinkWithNodes(final NetworkNode startNode, final NetworkNode endNode) {
                        for (final NetworkLink link : networkLinks) {
                            if (link.getStartNodeId() == startNode.getNodeId()
                                    && link.getEndNodeId() == endNode.getNodeId()) {
                                return link;
                            }
                        }
                        return null;
                    }
                });
        return journeyTimeGraphBuilt;
    }

    private List<DateTime> createIntervalList(DateTime startDateTime, DateTime endDateTime) {
        List<DateTime> intervalList = new ArrayList<>();

        if (isDateAFifteenMinuteInterval(startDateTime) && isDateAFifteenMinuteInterval(endDateTime)
                && !startDateTime.equals(endDateTime)) {
            intervalList.add(startDateTime);
            DateTime interval = startDateTime;
            while (!endDateTime.equals(interval)) {
                interval = interval.plusMinutes(15);
                intervalList.add(interval);
            }

            intervalList.add(endDateTime);
        } else {
            LOG.warn(
                    "Could not calculate intervals; times must be of 15 minute intervals and not equal to each other.");
        }
        return intervalList;
    }

    private boolean isDateAFifteenMinuteInterval(DateTime dateTime) {
        return (dateTime.getMinuteOfHour() == FIFTEEN_MINUTES || dateTime.getMinuteOfHour() == THIRTY_MINUTES
                || dateTime.getMinuteOfHour() == FORTY_FIVE_MINUTES || dateTime.getMinuteOfHour() == ON_THE_HOUR);
    }

    private long calculateNewBeginMinute(long beginMinute, double minutesToTravelLink) {
        double beginMinuteDouble = beginMinute - minutesToTravelLink;
        // this is probably wrong, I need to find the correct 15 minute interval.
        return 15 * (Math.round(beginMinuteDouble / 15));

    }
}
