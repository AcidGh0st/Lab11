package controller;

import domain.GraphException;
import domain.SinglyLinkedListGraph;
import domain.Vertex;
import domain.list.ListException;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class LinkedGraphOperationsController {
    @javafx.fxml.FXML
    private Pane paneGraph;
    @javafx.fxml.FXML
    private BorderPane bp;
    @javafx.fxml.FXML
    private TextArea textArea;

    private SinglyLinkedListGraph graph;

    @javafx.fxml.FXML
    public void initialize() {
        graph = new SinglyLinkedListGraph();
    }

    @javafx.fxml.FXML
    public void randomizeOnAction(ActionEvent actionEvent) {
        Random rand = new Random();
        Set<Integer> vertices = new HashSet<>();
        //Arreglarlo con los nombres historicos
        while (vertices.size() < 10) {
            vertices.add(util.Utility.getRandom(100));
        }

        try {
            graph.clear();
            for (Integer v : vertices) {
                graph.addVertex(v);
            }

            for (int i = 0; i < 10; i++) {
                for (int j = i + 1; j < 10; j++) {
                    if (rand.nextBoolean()) { // Decide aleatoriamente si se debe agregar una arista
                        graph.addEdge(vertices.toArray()[i], vertices.toArray()[j]);
                    }
                }
            }

            drawGraph();
            updateTextArea();
        } catch (GraphException | ListException e) {
            showAlert(e.getMessage());
        }
    }

    @javafx.fxml.FXML
    public void addVertexOnAction(ActionEvent actionEvent) {
        try {
            //Arreglarlo con los nombres historicos
            Object vertex = util.Utility.getRandom(100);

            graph.addVertex(vertex);
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

            while (!edgeRemoved) {
                int indexA = rand.nextInt(graph.size());
                int indexB = rand.nextInt(graph.size());

                //Asegura que no se seleccionen el mismo vértice
                while (indexA == indexB) {
                    indexB = rand.nextInt(graph.size());
                }

                Vertex vertexA = graph.getVertex(indexA);
                Vertex vertexB = graph.getVertex(indexB);

                //Verificación adicional para asegurar que los vértices no sean nulos
                if (vertexA == null || vertexB == null) {
                    continue;
                }

                if (graph.containsEdge(vertexA.data, vertexB.data)) {
                    graph.removeEdge(vertexA.data, vertexB.data);
                    edgeRemoved = true;
                }
            }
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

            Object weight = 100 + util.Utility.getRandom(101); //Peso entre 100 y 200

            graph.addEdgeWeight(vertexA.data, vertexB.data, weight);
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
            alert.setContentText("The Singly Linked List Graph is already empty.");
            alert.showAndWait();
        } else {
            graph.clear();
            textArea.clear();
            paneGraph.getChildren().clear();

            //Mensaje de tabla limpia
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Clear");
            alert.setHeaderText(null);
            alert.setContentText("Singly Linked List Graph has been cleared.");
            alert.showAndWait();
            updateTextArea();
        }
    }

    private void drawGraph() throws ListException {
        paneGraph.getChildren().clear();
        double radius = 200;
        double centerX = paneGraph.getWidth() / 2;
        double centerY = paneGraph.getHeight() / 2;

        double[] vertexX = new double[graph.size()];
        double[] vertexY = new double[graph.size()];

        for (int i = 0; i < graph.size(); i++) {
            double angle = 2 * Math.PI * i / graph.size();
            vertexX[i] = centerX + radius * Math.cos(angle);
            vertexY[i] = centerY + radius * Math.sin(angle);
        }

        for (int i = 0; i < graph.size(); i++) {
            for (int j = i + 1; j < graph.size(); j++) {
                if (!graph.getSinglyLinkedList()[i][j].equals(0)) {
                    javafx.scene.shape.Line line = new javafx.scene.shape.Line();
                    line.setStartX(vertexX[i]);
                    line.setStartY(vertexY[i]);
                    line.setEndX(vertexX[j]);
                    line.setEndY(vertexY[j]);
                    paneGraph.getChildren().add(line);
                }
            }
        }

        for (int i = 0; i < graph.size(); i++) {
            javafx.scene.shape.Circle circle = new javafx.scene.shape.Circle();
            circle.setCenterX(vertexX[i]);
            circle.setCenterY(vertexY[i]);
            circle.setRadius(15);
            circle.setStyle("-fx-fill: lightblue; -fx-stroke: black;");

            Text text = new Text(String.valueOf(graph.getVertex(i).data));
            text.setX(vertexX[i] - 5);
            text.setY(vertexY[i] + 5);

            paneGraph.getChildren().addAll(circle, text);
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
