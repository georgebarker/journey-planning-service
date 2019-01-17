package com.georgebarker.journeyplanningservice.services;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicReference;

import org.jgrapht.EdgeFactory;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.georgebarker.journeyplanningservice.dao.NetworkLinkDao;
import com.georgebarker.journeyplanningservice.model.NetworkLink;
import com.georgebarker.journeyplanningservice.model.NetworkLinkEdge;
import com.georgebarker.journeyplanningservice.model.NetworkNode;

/**
 *
 * I am a class which provides spatial functions for the network links
 *
 */
@Service
public class JourneyPlanningService {

	private static final Logger LOG = LoggerFactory.getLogger(JourneyPlanningService.class);

	@Autowired
	private NetworkLinkDao networkLinkDao;

	private final AtomicReference<DefaultDirectedWeightedGraph<NetworkNode, NetworkLinkEdge>> journeyTimeGraph = new AtomicReference<>();

	public List<NetworkLink> getRoute(final long startLinkId, final long endLinkId) {

		final List<NetworkLink> route = new LinkedList<>();
		final NetworkLink startLink = networkLinkDao.findById(startLinkId);
		final NetworkLink endLink = networkLinkDao.findById(endLinkId);
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
		addEdgesToList(startLink, endLink, route, pathEdgeList);
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

		List<NetworkLink> networkLinks = networkLinkDao.retrieveAllNetworkLinks();
		
		// Calculate weights

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
}
