package controller;

import domain.*;
import domain.list.ListException;
import javafx.event.ActionEvent;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MstTreeController {
    @javafx.fxml.FXML
    private BorderPane bp;
    @javafx.fxml.FXML
    private Pane paneMST;
    @javafx.fxml.FXML
    private Pane paneGraphType;
    private AdjacencyListGraph adjListGraph;
    private AdjacencyMatrixGraph adjMatrixGraph;
    private SinglyLinkedListGraph slListGraph;
    @javafx.fxml.FXML
    private RadioButton radioButtonAdjList;
    @javafx.fxml.FXML
    private RadioButton radioButtonAdjMatrix;
    @javafx.fxml.FXML
    private RadioButton radioButtonKruskal;
    @javafx.fxml.FXML
    private RadioButton radioButtonSlList;
    @javafx.fxml.FXML
    private RadioButton radioButtonPrim;
    private Map<Vertex, Circle> vertexMap = new HashMap<>();

    @javafx.fxml.FXML
    public void initialize() {
        // Inicialización adicional si es necesario
    }

    @javafx.fxml.FXML
    public void randomizeOnAction(ActionEvent actionEvent) {
        if (radioButtonAdjList.isSelected()) {
            adjListGraph = new AdjacencyListGraph(10);
            fillGraph(adjListGraph);
            drawGraph(adjListGraph);
        } else if (radioButtonAdjMatrix.isSelected()) {
            adjMatrixGraph = new AdjacencyMatrixGraph(10);
            fillGraph(adjMatrixGraph);
            drawGraph(adjMatrixGraph);
        } else if (radioButtonSlList.isSelected()) {
            slListGraph = new SinglyLinkedListGraph();
            fillGraph(slListGraph);
            drawGraph(slListGraph);
        }
    }

    private void fillGraph(Graph graph) {
//        try {
//            Set<Integer> vertices = new HashSet<>();
//            while (vertices.size() < 10) {
//                vertices.add(util.Utility.getRandom(100, 0));
//            }
//
//            for (Integer vertex : vertices) {
//                graph.addVertex(new Vertex(vertex));
//            }
//
//            for (Integer sourceVertex : vertices) {
//                for (Integer targetVertex : vertices) {
//                    if (!sourceVertex.equals(targetVertex)) {
//                        int randomWeight = util.Utility.getRandom(801, 200);
//                        graph.addEdgeWeight(sourceVertex, targetVertex, randomWeight);
//                    }
//                }
//            }
//        } catch (GraphException | ListException e) {
//            e.printStackTrace();
//        }
    }

    @javafx.fxml.FXML
    public void adjListOnAction(ActionEvent actionEvent) {
        radioButtonAdjMatrix.setSelected(false);
        radioButtonSlList.setSelected(false);
    }

    @javafx.fxml.FXML
    public void kruskalOnAction(ActionEvent actionEvent) {
        radioButtonPrim.setSelected(false);
    }

    @javafx.fxml.FXML
    public void adjMatrixOnAction(ActionEvent actionEvent) {
        radioButtonAdjList.setSelected(false);
        radioButtonSlList.setSelected(false);
    }

    @javafx.fxml.FXML
    public void primOnAction(ActionEvent actionEvent) {
        radioButtonKruskal.setSelected(false);
    }

    @javafx.fxml.FXML
    public void LinkedListOnAction(ActionEvent actionEvent) {
        radioButtonAdjList.setSelected(false);
        radioButtonAdjMatrix.setSelected(false);
    }

    private void drawGraph(Graph graph) {
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

            for (int i = 0; i < n; i++) {
                Vertex vertex = graph.getVertex(i + 1);
                String vertexName = vertex.getData().toString();
                double angle = i * angleStep;
                double x = centerX + radius * Math.cos(angle);
                double y = centerY + radius * Math.sin(angle);

                Circle circle = new Circle(x, y, 20);
                circle.setStyle("-fx-fill: lightblue; -fx-stroke: black;");
                Text text = new Text(x - 10, y + 5, vertexName);

                vertexMap.put(vertex, circle);
                paneGraphType.getChildren().addAll(circle, text);
            }

            for (int i = 0; i < n; i++) {
                Vertex vertex = graph.getVertex(i + 1);
                for (int j = 0; j < vertex.edgesList.size(); j++) {
                    EdgeWeight edge = (EdgeWeight) vertex.edgesList.getNode(j + 1).data;
                    String targetVertex = edge.getEdge().toString();
                    Circle sourceCircle = vertexMap.get(vertex);
                    Circle targetCircle = vertexMap.get(targetVertex);

                    Line line = new Line(sourceCircle.getCenterX(), sourceCircle.getCenterY(),
                            targetCircle.getCenterX(), targetCircle.getCenterY());
                    line.setStrokeWidth(2);
                    paneGraphType.getChildren().add(0, line);
                }
            }
        } catch (ListException | GraphException e) {
            e.printStackTrace();
        }
    }
}