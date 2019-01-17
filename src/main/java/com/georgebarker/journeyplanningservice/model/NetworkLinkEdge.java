package com.georgebarker.journeyplanningservice.model;

import org.jgrapht.graph.DefaultWeightedEdge;


/**
 * This class holds a single node and cost of traversing that node in a format
 * DefaultDirectedWeightedGraph can use
 *
 */
public class NetworkLinkEdge extends DefaultWeightedEdge {
    private static final long serialVersionUID = 1L;
    private final NetworkNode startNode;
    private final NetworkNode endNode;
    private final double linkWeight;
    private final NetworkLink link;

    public NetworkLinkEdge(final NetworkNode startNode, final NetworkNode endNode,
            final double weight, final NetworkLink link) {
        this.startNode = startNode;
        this.endNode = endNode;
        linkWeight = weight;
        this.link = link;
    }

    @Override
    public double getWeight() {
        return linkWeight;
    }

    public NetworkNode getStartNode() {
        return startNode;
    }

    public NetworkNode getEndNode() {
        return endNode;
    }

    public NetworkLink getLink() {
        return link;
    }
}