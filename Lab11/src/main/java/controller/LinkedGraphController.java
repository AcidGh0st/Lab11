package controller;

import domain.EdgeWeight;
import domain.GraphException;
import domain.SinglyLinkedListGraph;
import domain.Vertex;
import domain.list.ListException;
import domain.queue.QueueException;
import domain.stack.StackException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.*;

public class LinkedGraphController {
    @FXML
    private Text textMessage;
    @FXML
    private BorderPane bp;
    @FXML
    private TextArea textArea;
    @FXML
    private Pane paneGrafo;

    private SinglyLinkedListGraph graph;
    private Map<String, Circle> vertexMap; // Mapa para almacenar los vértices y sus posiciones
    private String[] vertexNames;

    @FXML
    public void initialize() {
        graph = new SinglyLinkedListGraph();
        vertexMap = new HashMap<>();
        vertexNames = new String[]{"Einstein", "Newton", "Curie", "Galileo", "Tesla",
                "Da Vinci", "Aristóteles", "Darwin", "Hawking", "Edison", "Jackson", "Pasteur", "Copernico", "Fleming",
                "Mendel", "Schrödinger", "Ochoa", "Nobel", "Pitágoras", "Dalton", "Descartes", "Franklin", "Linneo"};

        try {
            initializeGraph();
            drawGraph();
        } catch (GraphException | ListException e) {
            e.printStackTrace();
        }
    }

    private void initializeGraph() throws GraphException, ListException {

        graph.clear();


        List<String> namesList = Arrays.asList(vertexNames);
        Collections.shuffle(namesList);
        List<String> selectedNames = namesList.subList(0, 10);


        for (String vertex : selectedNames) {
            graph.addVertex(vertex);
        }


        Random random = new Random();
        for (String vertex1 : selectedNames) {
            for (String vertex2 : selectedNames) {
                // Agregar una arista con una probabilidad del 30%
                if (!vertex1.equals(vertex2) && random.nextDouble() < 0.3) {
                    graph.addEdgeWeight(vertex1, vertex2, random.nextInt(1001) + 1000);
                }
            }
        }
    }

    @FXML
    public void RandomizeOnAction(ActionEvent actionEvent) {

        try {
            initializeGraph();
            drawGraph();
        } catch (GraphException | ListException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void ContainsEdgeOnAction(ActionEvent actionEvent) throws IOException {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Verificar Arista");
        dialog.setHeaderText("Ingrese los vértices para verificar la arista:");
        dialog.setContentText("Vértice A:");

        // Mostrar el diálogo y esperar a que el usuario ingrese un vértice A
        dialog.showAndWait().ifPresent(vertexA -> {
            // Pedir al usuario que ingrese el vértice B
            dialog.setHeaderText("Ingrese el segundo vértice:");
            dialog.setContentText("Vértice B:");

            // Mostrar el diálogo y esperar a que el usuario ingrese un vértice B
            dialog.showAndWait().ifPresent(vertexB -> {
                // Verificar si existe una arista entre los vértices A y B
                try {
                    boolean containsEdge = graph.containsEdge(vertexA, vertexB);


                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Resultado");
                    alert.setHeaderText(null);
                    if (containsEdge) {
                        alert.setContentText("Existe una arista entre los vértices " + vertexA + " y " + vertexB);
                    } else {
                        alert.setContentText("No existe una arista entre los vértices " + vertexA + " y " + vertexB);
                    }
                    alert.showAndWait();
                } catch (GraphException | ListException e) {
                    e.printStackTrace();

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Ocurrió un error al verificar la arista: " + e.getMessage());
                    alert.showAndWait();
                }
            });
        });
    }

    @FXML
    public void DFSOnAction(ActionEvent actionEvent) throws GraphException, ListException, StackException {
        String result = graph.dfs();
        textArea.setText(result);
    }

    @FXML
    public void ContainsVertexOnAction(ActionEvent actionEvent) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Verificar Vértice");
        dialog.setHeaderText("Ingrese el vértice que desea verificar:");
        dialog.setContentText("Vértice:");


        dialog.showAndWait().ifPresent(vertex -> {

            boolean exists;
            try {
                exists = graph.containsVertex(vertex);
            } catch (GraphException | ListException e) {
                throw new RuntimeException(e);
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Resultado de la búsqueda");
            alert.setHeaderText(null);
            if (exists) {
                alert.setContentText("El vértice " + vertex + " está en el grafo.");
            } else {
                alert.setContentText("El vértice " + vertex + " no está en el grafo.");
            }
            alert.showAndWait();
        });
    }

    @FXML
    public void ToStringOnAction(ActionEvent actionEvent) {
        textArea.setText(graph.toString());
    }

    @FXML
    public void BFSOnAction(ActionEvent actionEvent) throws GraphException, QueueException, ListException {
        String result = graph.bfs();
        textArea.setText(result);
    }

    private void drawGraph() {
        paneGrafo.getChildren().clear();
        vertexMap.clear();

        // Dibujar vértices
        double paneWidth = paneGrafo.getWidth();
        double paneHeight = paneGrafo.getHeight();
        double centerX = paneWidth / 2;
        double centerY = paneHeight / 2;
        double radius = Math.min(paneWidth, paneHeight) / 3;

        try {
            int n = graph.size();
            double angleStep = 2 * Math.PI / n;

            for (int i = 1; i <= n; i++) {
                Vertex vertex = graph.getVertex(i);
                String vertexName = vertex.getData().toString();
                double angle = i * angleStep;
                double x = centerX + radius * Math.cos(angle);
                double y = centerY + radius * Math.sin(angle);

                Circle circle = new Circle(x, y, 20); // Aumentar el tamaño del círculo
                circle.setStyle("-fx-fill: lightblue; -fx-stroke: black;");
                Text text = new Text(x - 10, y + 5, vertexName);

                vertexMap.put(vertexName, circle);
                paneGrafo.getChildren().addAll(circle, text);
            }

            // Dibujar aristas después de los vértices
            for (int i = 1; i <= n; i++) {
                Vertex vertex = graph.getVertex(i);
                String vertexName = vertex.getData().toString();
                for (int j = 1; j <= vertex.edgesList.size(); j++) {
                    EdgeWeight edge = (EdgeWeight) vertex.edgesList.getNode(j).data;
                    String targetVertex = edge.getEdge().toString(); // Acceder al vértice destino de la arista
                    Circle sourceCircle = vertexMap.get(vertexName);
                    Circle targetCircle = vertexMap.get(targetVertex);

                    Line line = new Line(sourceCircle.getCenterX(), sourceCircle.getCenterY(),
                            targetCircle.getCenterX(), targetCircle.getCenterY());
                    line.setStrokeWidth(2); // Ajustar el ancho de la línea si es necesario


                    line.setOnMouseEntered(event -> {
                        String message = "Edge between vertexes: [" + vertexName + "]......[" + targetVertex + "]. Weight: " + edge.getWeight();
                        textMessage.setText(message);
                    });

                    paneGrafo.getChildren().add(0, line); // Agregar la línea al principio para que se dibuje debajo de los vértices
                }
            }
        } catch (ListException e) {
            e.printStackTrace();
        }
    }


    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setPrefWidth(600);
        dialogPane.setPrefHeight(500);
        alert.showAndWait();
    }
}
