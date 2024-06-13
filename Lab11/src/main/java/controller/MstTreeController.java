package controller;

import domain.*;
import domain.list.ListException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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

    @FXML
    private BorderPane bp;
    @FXML
    private Pane paneMST;
    @FXML
    private Pane paneGraphType;
    private SinglyLinkedListGraph slListGraph; // Cambiado a SinglyLinkedListGraph
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
    private Map<Vertex, Circle> vertexMap = new HashMap<>();

    @FXML
    public void initialize() {
        // Inicialización adicional si es necesario
    }

    @FXML
    public void randomizeOnAction(ActionEvent actionEvent) {
        slListGraph = new SinglyLinkedListGraph(); // Instancia de SinglyLinkedListGraph
        fillGraph();
        drawGraph(slListGraph);
    }

    private void fillGraph() {
        try {
            Set<Integer> vertices = new HashSet<>();
            while (vertices.size() < 10) {
                vertices.add(util.Utility.getRandom(100, 0));
            }

            System.out.println("Vertices to be added: " + vertices);

            for (Integer vertex : vertices) {
                slListGraph.addVertex(new Vertex(vertex));
            }

            System.out.println("Vertices added to the graph.");

            for (Integer sourceVertex : vertices) {
                for (Integer targetVertex : vertices) {
                    if (!sourceVertex.equals(targetVertex)) {
                        int randomWeight = util.Utility.getRandom(801, 200);
                        try {
                            slListGraph.addEdgeWeight(sourceVertex, targetVertex, randomWeight); // Requiere implementación
                            System.out.println("Edge added between " + sourceVertex + " and " + targetVertex + " with weight " + randomWeight);
                        } catch (GraphException e) {
                            System.out.println("Error adding edge between " + sourceVertex + " and " + targetVertex + ": " + e.getMessage());
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

            // Dibujar los vértices
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

            // Dibujar las aristas
            for (int i = 0; i < n; i++) {
                Vertex vertex = graph.getVertex(i + 1);
                for (int j = 0; j < vertex.edgesList.size(); j++) {
                    EdgeWeight edge = (EdgeWeight) vertex.edgesList.getNode(j + 1).data;
                    Vertex targetVertex = graph.getVertex((Integer) edge.getEdge());

                    Circle sourceCircle = vertexMap.get(vertex);
                    Circle targetCircle = vertexMap.get(targetVertex);

                    if (sourceCircle != null && targetCircle != null) {
                        Line line = new Line(sourceCircle.getCenterX(), sourceCircle.getCenterY(),
                                targetCircle.getCenterX(), targetCircle.getCenterY());
                        line.setStrokeWidth(2);
                        paneGraphType.getChildren().add(0, line);
                    }
                }
            }
        } catch (ListException | GraphException e) {
            e.printStackTrace();
        }
    }
}
