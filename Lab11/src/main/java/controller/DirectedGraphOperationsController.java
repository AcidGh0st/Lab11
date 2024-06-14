package controller;

import domain.*;
import domain.list.ListException;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;

import java.util.*;

public class DirectedGraphOperationsController {
    @javafx.fxml.FXML
    private Pane paneGraph;
    @javafx.fxml.FXML
    private BorderPane bp;
    @javafx.fxml.FXML
    private TextArea textArea;

    private DirectedSinglyLinkedListGraph graph;
    private Map<String, Circle> vertexMap; // Mapa para almacenar los vértices y sus posiciones
    private String[] vertexNames;
    private Random rand = new Random();

    String[] vertexes = {"T.Eiffel","T.Pisa","E.Libertad","Guiza","MurallaChina","MacchuPicchu","Taj Majal","Rushmore","I.Pascua","Kremlin",
            "ÓperaSidney","ChichenItzá","Big Ben","CristoRed","Acrópolis","NotreDame","Golden Gate","SagradaFam","Casa Blanca","Esfinge",
            "Stonhenge","Coliseo","Teotihuacán","Petra","Lúxor","Angkor","Saint-Michel","Burj Khalifa","Wat Pho","Grand Place"};

    @javafx.fxml.FXML
    public void initialize() {

    }

    @javafx.fxml.FXML
    public void randomizeOnAction(ActionEvent actionEvent) {
        try {
            graph = new DirectedSinglyLinkedListGraph();
            vertexMap = new HashMap<>();
            Set<Integer> weight = new HashSet<>();

            // Selecciona aleatoriamente 10 nombres sin repetir
            List<String> selectedVertexes = new ArrayList<>();
            Random random = new Random();
            while (selectedVertexes.size() < 10) {
                String randomVertex = vertexes[random.nextInt(vertexes.length)];
                if (!selectedVertexes.contains(randomVertex)) {
                    selectedVertexes.add(randomVertex);
                }
            }

            //Añadir vertices al grafo
            for (String vertex : selectedVertexes) {
                graph.addVertex(vertex);
            }

            // Shuffle the list of vertices
            Collections.shuffle(selectedVertexes);

            for (int i = 0; i < selectedVertexes.size() - 1; i++) {
                String sourceVertex = selectedVertexes.get(i);
                String targetVertex = selectedVertexes.get(i + 1);

                int edgeWeight = util.Utility.getRandom(201, 2001);
                graph.addEdgeWeight(sourceVertex, targetVertex, edgeWeight);
            }

            // Add an edge between the last and first vertices to complete the cycle
            String lastVertex = selectedVertexes.get(selectedVertexes.size() - 1);
            String firstVertex = selectedVertexes.get(0);
            int edgeWeight = util.Utility.getRandom(201, 2001);
            graph.addEdgeWeight(lastVertex, firstVertex, edgeWeight);

            // Draw the graph
            drawGraph();

        } catch (GraphException | ListException e) {
            throw new RuntimeException(e);
        }
    }

    @javafx.fxml.FXML
    public void addVertexOnAction(ActionEvent actionEvent) {
        try {
            // Seleccionar un nombre aleatorio del arreglo vertexNames
            int index = rand.nextInt(vertexes.length);
            String vertexName = vertexes[index];

            graph.addVertex(vertexName);
            drawGraph();
            updateTextArea();
        } catch (GraphException | ListException e) {
            showAlert(e.getMessage());
        }
    }

    @javafx.fxml.FXML
    public void addEdgesOnAction(ActionEvent actionEvent) {
        try {
            if (graph.size() < 2) {
                showAlert("Not enough vertices to add an edge.");
                return;
            }

            int indexA = util.Utility.getRandom(graph.size());
            int indexB = util.Utility.getRandom(graph.size());

            //Asegura que no se conecten el mismo vértice
            while (indexA == indexB) {
                indexB = util.Utility.getRandom(graph.size());
            }

            Vertex vertexA = graph.getVertex(indexA);
            Vertex vertexB = graph.getVertex(indexB);

            //Verificación adicional para asegurar que los vértices no sean nulos
            if (vertexA == null || vertexB == null) {
                //Coment del profe: Failed to retrieve valid vértices. El último vértice no lo remueve
                showAlert("Failed to retrieve valid vertices.");
                return;
            }

            Object weight = 1000 + util.Utility.getRandom(2001); //Peso entre 1000 y 2000

            graph.addEdgeWeight(vertexA.data, vertexB.data, weight);
            drawGraph();
            updateTextArea();
        } catch (GraphException | ListException e) {
            showAlert(e.getMessage());
        }
    }

    @javafx.fxml.FXML
    public void removeVertexOnAction(ActionEvent actionEvent) {
        try {
            if (graph.isEmpty()) {
                showAlert("No vertices to remove.");
                return;
            }

            int randomIndex = util.Utility.getRandom(graph.size());
            Vertex vertex = graph.getVertex(randomIndex);

            //Verificación para asegurar que el vértice no sea nulo
            if (vertex == null) {
                showAlert("Failed to retrieve a valid vertex.");
                return;
            }

            graph.removeVertex(vertex.data);
            drawGraph();
            updateTextArea();
        } catch (GraphException | ListException e) {
            showAlert(e.getMessage());
        }
    }

    @javafx.fxml.FXML
    public void removeEdgesOnAction(ActionEvent actionEvent) {
        try {
            if (graph.isEmpty()) {
                showAlert("No edges to remove.");
                return;
            }

            Random rand = new Random();
            boolean edgeRemoved = false;

            // Iterar hasta que se elimine una arista válida
            while (!edgeRemoved) {
                // Obtener dos índices aleatorios distintos
                int indexA = rand.nextInt(graph.size());
                int indexB = rand.nextInt(graph.size());

                // Asegurar que los índices sean distintos
                while (indexA == indexB) {
                    indexB = rand.nextInt(graph.size());
                }

                Vertex vertexA = graph.getVertex(indexA);
                Vertex vertexB = graph.getVertex(indexB);

                // Verificar si los vértices y la arista existen antes de intentar eliminarla
                if (vertexA != null && vertexB != null && graph.containsEdge(vertexA.data, vertexB.data)) {
                    graph.removeEdge(vertexA.data, vertexB.data);
                    edgeRemoved = true; // Se encontró y eliminó una arista válida
                }
            }

            drawGraph();
            updateTextArea();
        } catch (GraphException | ListException e) {
            showAlert(e.getMessage());
        }
    }

    @javafx.fxml.FXML
    public void clearOnAction(ActionEvent actionEvent) {
        if (graph == null || graph.isEmpty()) {
            //Mostrar mensaje de error si la lista está vacía o nula
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("The Directed Singly Linked List Graph is already empty.");
            alert.showAndWait();
        } else {
            graph.clear();
            textArea.clear();
            paneGraph.getChildren().clear();

            //Mensaje de tabla limpia
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Clear");
            alert.setHeaderText(null);
            alert.setContentText("Directed Singly Linked List Graph has been cleared.");
            alert.showAndWait();
            try {
                if (!graph.isEmpty()) {
                    drawGraph(); // Dibujar el grafo si no está vacío después de limpiar
                }
                updateTextArea();
            } catch (ListException | GraphException e) {
                showAlert("Error drawing graph after clearing: " + e.getMessage());
            }
        }
    }

    private void drawGraph() throws ListException, GraphException {
        paneGraph.getChildren().clear();
        vertexMap.clear();

        double paneWidth = paneGraph.getWidth();
        double paneHeight = paneGraph.getHeight();
        double centerX = paneWidth / 2;
        double centerY = paneHeight / 2;
        double radius = Math.min(paneWidth, paneHeight) / 3;

        try {
            int n = graph.size();
            double angleStep = 2 * Math.PI / n;

            // Dibujar vértices
            for (int i = 1; i <= n; i++) {
                Vertex vertex = graph.getVertex(i);
                String vertexName = vertex.getData().toString();
                double angle = i * angleStep;
                double x = centerX + radius * Math.cos(angle);
                double y = centerY + radius * Math.sin(angle);

                Circle circle = new Circle(x, y, 20);
                circle.setStyle("-fx-fill: lightblue; -fx-stroke: black;");
                Text text = new Text(x - 10, y + 5, vertexName);
                text.setFill(Color.DEEPPINK); // Cambiar el color de la letra
                text.setFont(javafx.scene.text.Font.font("Arial", 20)); // Cambiar el tamaño de la letra

                vertexMap.put(vertexName, circle);
                paneGraph.getChildren().addAll(circle, text);
            }

            // Dibujar aristas con flechas
            for (int i = 1; i <= n; i++) {
                Vertex vertex = graph.getVertex(i);
                String vertexName = vertex.getData().toString();
                for (int j = 1; j <= vertex.edgesList.size(); j++) {
                    EdgeWeight edge = (EdgeWeight) vertex.edgesList.getNode(j).data;
                    String targetVertexName = edge.getEdge().toString();
                    Circle sourceCircle = vertexMap.get(vertexName);
                    Circle targetCircle = vertexMap.get(targetVertexName);

                    if (sourceCircle != null && targetCircle != null) {
                        double sourceX = sourceCircle.getCenterX();
                        double sourceY = sourceCircle.getCenterY();
                        double targetX = targetCircle.getCenterX();
                        double targetY = targetCircle.getCenterY();

                        // Calcular el ángulo de la flecha
                        double angle = Math.atan2(targetY - sourceY, targetX - sourceX);

                        // Dibujar la línea de la arista
                        Line line = new Line(sourceX, sourceY, targetX, targetY);
                        line.setStrokeWidth(2);
                        paneGraph.getChildren().add(line);

                        // Dibujar la flecha
                        double arrowSize = 10;
                        double arrowX = targetX - arrowSize * Math.cos(angle);
                        double arrowY = targetY - arrowSize * Math.sin(angle);
                        Polygon arrow = new Polygon();
                        arrow.getPoints().addAll(new Double[]{
                                arrowX, arrowY,
                                arrowX + arrowSize * Math.cos(angle + Math.toRadians(150)),
                                arrowY + arrowSize * Math.sin(angle + Math.toRadians(150)),
                                arrowX + arrowSize * Math.cos(angle - Math.toRadians(150)),
                                arrowY + arrowSize * Math.sin(angle - Math.toRadians(150))
                        });
                        arrow.setFill(javafx.scene.paint.Color.BLACK);

                        paneGraph.getChildren().add(arrow);
                    }
                }
            }
        } catch (ListException e) {
            e.printStackTrace();
        }
    }

    private void updateTextArea() {
        try {
            textArea.setText(graph.toString());
        } catch (Exception e) {
            showAlert("Error updating text area: " + e.getMessage());
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
