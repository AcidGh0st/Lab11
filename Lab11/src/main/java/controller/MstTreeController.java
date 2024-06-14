package controller;

import domain.*;
import domain.list.ListException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.util.Pair;

import java.util.*;

public class MstTreeController {

    @FXML
    private BorderPane bp;
    @FXML
    private Pane paneMST;
    @FXML
    private Pane paneGraphType;
    private SinglyLinkedListGraph slListGraph = new SinglyLinkedListGraph(); // Ensure initialization
    @FXML
    private RadioButton radioButtonAdjList;
    @FXML
    private RadioButton radioButtonAdjMatrix;
    @FXML
    private RadioButton radioButtonKruskal;
    @FXML
    private RadioButton radioButtonSlList;
    @FXML
    private RadioButton radioButtonPrim;
    private Map<Integer, Circle> vertexMap = new HashMap<>();
    private Graph graph; // Ensure initialization

    @FXML
    public void initialize() {
//        ToggleGroup toggleGroup = new ToggleGroup();
//        radioButtonAdjList.setToggleGroup(toggleGroup);
//        radioButtonAdjMatrix.setToggleGroup(toggleGroup);
//        radioButtonSlList.setToggleGroup(toggleGroup);


        if(radioButtonSlList.isSelected()) {
            paneGraphType.getChildren().clear();
            graph = new SinglyLinkedListGraph();
        }else if (radioButtonAdjMatrix.isSelected()) {
            paneGraphType.getChildren().clear();
            graph = new AdjacencyMatrixGraph(11);
        }else if (radioButtonAdjList.isSelected()){
            paneGraphType.getChildren().clear();
            graph = new AdjacencyListGraph(11);}

        vertexMap = new HashMap<>();
    }

    @FXML
    public void randomizeOnAction(ActionEvent actionEvent) {

        Set<Integer> vertices = new HashSet<>();
        graph.clear();

        while (vertices.size() < 10) {
            vertices.add(util.Utility.getRandom(100, 0));
        }


        for (int vertex : vertices) {
            try {
                graph.addVertex(vertex); // Add random unique vertices
            } catch (GraphException | ListException e) {
                e.printStackTrace();
            }
        }

        Random random = new Random();

        for (int i = 0; i < 20; i++) {
            List<Integer> vertexList = new ArrayList<>(vertices);
            int src = vertexList.get(random.nextInt(vertexList.size())); // Get a random vertex
            int dest = vertexList.get(random.nextInt(vertexList.size())); // Get another random vertex
            int weight = 200 + random.nextInt(801); // Random weight

            try {
                if (!graph.containsEdge(src, dest)) {
                    graph.addEdgeWeight(src, dest, weight);
                }
            } catch (GraphException | ListException e) {
                e.printStackTrace();
            }
        }

        drawGraph();
    }

    private void fillGraph() {
        try {
            Set<Integer> vertices = new HashSet<>();
            while (vertices.size() < 10) {
                vertices.add(util.Utility.getRandom(100, 0));
            }


            for (Integer vertex : vertices) {
                slListGraph.addVertex(new Vertex(vertex));
            }

            for (Integer sourceVertex : vertices) {
                for (Integer targetVertex : vertices) {
                    if (!sourceVertex.equals(targetVertex)) {
                        int randomWeight = util.Utility.getRandom(801, 200);
                        try {
                            slListGraph.addEdgeWeight(sourceVertex, targetVertex, randomWeight); // Requires implementation
                        } catch (GraphException e) {
                            util.UtilityFX.alert("Error", "Error adding edge between "
                                    + sourceVertex + " and " + targetVertex + ": " + e.getMessage());
                        }
                    }
                }
            }
        } catch (ListException e) {
            e.printStackTrace();
        } catch (GraphException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void adjListOnAction(ActionEvent actionEvent) {
        radioButtonAdjMatrix.setSelected(false);
        radioButtonSlList.setSelected(false);
    }

    @FXML
    public void kruskalOnAction(ActionEvent actionEvent) {
        radioButtonPrim.setSelected(false);
    }

    @FXML
    public void adjMatrixOnAction(ActionEvent actionEvent) {
        radioButtonAdjList.setSelected(false);
        radioButtonSlList.setSelected(false);
    }

    @FXML
    public void primOnAction(ActionEvent actionEvent) {
        radioButtonKruskal.setSelected(false);
    }

    @FXML
    public void LinkedListOnAction(ActionEvent actionEvent) {
        radioButtonAdjList.setSelected(false);
        radioButtonAdjMatrix.setSelected(false);
    }

    private void drawGraph() {
        paneGraphType.getChildren().clear();
        vertexMap.clear();

        double paneWidth = paneGraphType.getWidth();
        double paneHeight = paneGraphType.getHeight();
        double centerX = paneWidth / 2;
        double centerY = paneHeight / 2;
        double radius = Math.min(paneWidth, paneHeight) / 3;

        try {
            int n = graph.size();
            double angleStep = 2 * Math.PI / n;


            // Draw vertices in a circle
            for (int i = 0; i < n; i++) {
                double angle = i * angleStep;
                double x = centerX + radius * Math.cos(angle);
                double y = centerY + radius * Math.sin(angle);

                Circle circle = new Circle(x, y, 20); // Circle size
                circle.setStyle("-fx-fill: lightblue; -fx-stroke: black;");

                Text text = new Text(String.valueOf(i)); // Vertex value
                text.setX(x - 5); // Adjust text position
                text.setY(y + 5); // Adjust text position

                vertexMap.put(i, circle);
                paneGraphType.getChildren().addAll(circle, text);
            }

            // Draw edges
            for (int i = 0; i < n; i++) {
                List<Pair<Integer, Integer>> neighbors = graph.getNeighbors(i);
                Circle sourceCircle = vertexMap.get(i);
                for (Pair<Integer, Integer> neighbor : neighbors) {
                    int targetVertex = neighbor.getKey();
                    Circle targetCircle = vertexMap.get(targetVertex);

                    Line line = new Line(sourceCircle.getCenterX(), sourceCircle.getCenterY(),
                            targetCircle.getCenterX(), targetCircle.getCenterY());
                    line.setStrokeWidth(2); // Adjust line width if needed
                    paneGraphType.getChildren().add(0, line); // Draw beneath vertices

                }
            }
        } catch (ListException | GraphException e) {
            e.printStackTrace();
        }
    }
}
