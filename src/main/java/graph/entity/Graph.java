package graph.entity;

import org.jgraph.JGraph;
import org.jgraph.graph.*;
import org.springframework.stereotype.*;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.*;
import java.util.List;

@Component
public class Graph {

    private Map<String, List<String>> undirectedGraph = new HashMap<String, List<String>>();

    public Graph(){
    }

    public Graph(int vertexCount) {
        createUndirectedGraph(vertexCount);
    }

    public void createUndirectedGraph(int vertexCount) {
        for (int i = 0; i < vertexCount; i++) {
            undirectedGraph.put(i + " - vertex - " + i, new ArrayList<String>());
        }

        ArrayList<String> tempVertexList = new ArrayList<String>(undirectedGraph.keySet());
        Random random = new Random();
        String firstVertex, secondVertex;
        for (int i = 0; i < vertexCount * 2; i++) {
            firstVertex = tempVertexList.get(random.nextInt(vertexCount));
            secondVertex = tempVertexList.get(random.nextInt(vertexCount));
            if (firstVertex.equals(secondVertex)) {
                continue;
            }
            if (undirectedGraph.get(firstVertex).contains(secondVertex)) {
                continue;
            }
            undirectedGraph.get(firstVertex).add(secondVertex);
            undirectedGraph.get(secondVertex).add(firstVertex);
        }
    }

    public void showGraph() {
        for (String loopVertex : undirectedGraph.keySet()) {
            System.out.println(loopVertex + "  *  " + undirectedGraph.get(loopVertex));
        }
    }

    public void calculateGraph() {
        List<String> foundedPath = new ArrayList<String>();
        List<String> currentPath = new ArrayList<String>();
        for (String loopVertex : undirectedGraph.keySet()) {

            try {
                String currentVertex = loopVertex;
                currentPath.clear();
                foundedPath = Graph.searchElerPath(currentVertex, currentPath, undirectedGraph);
                if (!foundedPath.isEmpty()) {
                    break;
                }
            } catch (Exception ex) {

            }
        }
        System.out.println(foundedPath);
    }

    private static List<String> searchElerPath(String currentVertex, List<String> currentPath,
                                               Map<String, List<String>> currentGraph) {
        List<String> newPath = new ArrayList<String>(currentPath);
        newPath.add(currentVertex);
        if (newPath.size() == currentGraph.keySet().size()) {
            return newPath;
        }
        for (String loopVertex : currentGraph.get(currentVertex)) {
            if (!currentPath.contains(loopVertex)) {
                List<String> tempPath = Graph.searchElerPath(loopVertex, newPath, currentGraph);
                if (tempPath.size() == currentGraph.keySet().size()) {
                    newPath = tempPath;
                    return newPath;
                } else {

                }
            }
        }
        newPath.remove(currentVertex);
        return newPath;
    }

    public void showGraphInSwingWindow() {

        // Construct Model and graph
        GraphModel model = new DefaultGraphModel();
        JGraph graph = new JGraph(model);
        // Control-drag should clone selection
        graph.setCloneable(true);

        // Enable edit without final RETURN keystroke
        graph.setInvokesStopCellEditing(true);

        // When over a cell, jump to its default port (we only have one, anyway)
        graph.setJumpToDefaultPort(true);

        // Insert all three cells in one call, so we need an array to store them
        List<DefaultGraphCell> cellsList = new ArrayList<DefaultGraphCell>();
        Map<String, Integer> conformityMap = new HashMap<String, Integer>();


        // Create vertex
        int vertexNumber = 0;
        for (String loopVertex : undirectedGraph.keySet()) {
            cellsList.add(createVertex(loopVertex, 40 * (vertexNumber + 1) + 20 * ((vertexNumber + 1) % 2),
                    20 * (vertexNumber + 2) + 50 * ((vertexNumber + 1) % 2), 80, 20, null, false));
            conformityMap.put(loopVertex, vertexNumber);
            vertexNumber++;
        }

        int firstVertexNumber;
        int secondVertexNumber;
        for (String loopVertex : undirectedGraph.keySet()) {

            firstVertexNumber = conformityMap.get(loopVertex);
            for (String loopCorrespondingVertex : undirectedGraph.get(loopVertex)) {
                secondVertexNumber = conformityMap.get(loopCorrespondingVertex);
                if (secondVertexNumber <= firstVertexNumber) {
                    continue;
                }
                // Create Edge
                DefaultEdge edge = new DefaultEdge();
                // Fetch the ports from the new vertices, and connect them with the edge
                edge.setSource(cellsList.get(firstVertexNumber).getChildAt(0));
                edge.setTarget(cellsList.get(secondVertexNumber).getChildAt(0));
                cellsList.add(edge);
            }
        }


        // Set Arrow Style for edge
//        int arrow = GraphConstants.ARROW_NONE;
//        GraphConstants.setLineEnd(edge.getAttributes(), arrow);
//        GraphConstants.setEndFill(edge.getAttributes(), true);

        // Insert the cells via the cache, so they get selected

        DefaultGraphCell[] cells = new DefaultGraphCell[cellsList.size()];
        for (int i = 0; i < cellsList.size(); i++) {
            cells[i] = cellsList.get(i);
        }
        graph.getGraphLayoutCache().insert(cells);

        // Show in Frame
        JFrame frame = new JFrame();
        frame.getContentPane().add(new JScrollPane(graph));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public static DefaultGraphCell createVertex(String name, double x,
                                                double y, double w, double h, Color bg, boolean raised) {

        // Create vertex with the given name
        DefaultGraphCell cell = new DefaultGraphCell(name);

        // Set bounds
        GraphConstants.setBounds(cell.getAttributes(),
                new Rectangle2D.Double(x, y, w, h));

        // Set fill color
        if (bg != null) {
            GraphConstants.setGradientColor(cell.getAttributes(), bg);
            GraphConstants.setOpaque(cell.getAttributes(), true);
        }

        // Set raised border
        if (raised) {
            GraphConstants.setBorder(cell.getAttributes(),
                    BorderFactory.createRaisedBevelBorder());
        } else // Set black border
        {
            GraphConstants.setBorderColor(cell.getAttributes(),
                    Color.black);
        }
        // Add a Floating Port
        cell.addPort();

        return cell;
    }
}