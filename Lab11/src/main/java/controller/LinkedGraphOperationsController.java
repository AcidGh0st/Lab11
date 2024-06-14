package controller;

import domain.EdgeWeight;
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
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

import java.util.*;

public class LinkedGraphOperationsController {
    @javafx.fxml.FXML
    private Pane paneGraph;
    @javafx.fxml.FXML
    private BorderPane bp;
    @javafx.fxml.FXML
    private TextArea textArea;

    private SinglyLinkedListGraph graph;
    private Map<String, Circle> vertexMap; // Mapa para almacenar los vértices y sus posiciones
    private String[] vertexNames;
    private Random rand = new Random();

    @javafx.fxml.FXML
    public void initialize() {
        graph = new SinglyLinkedListGraph();
        vertexMap = new HashMap<>();
        vertexNames = new String[]{"Buda","Platón","Magno","Cleopatra","Jesús","Mahoma","Marco Polo","Galilei","Napoleón","Darwin",
                "Einstein","Gandhi","Mandela","Ana Bolena","Cervantes","Dalí","Da Vinci","Shelley","Nefertiti","Ramsés",
                "Tutankamón","Shakespeare","Mozart","Picasso","Van Gogh","Kalho","Verne","Allan Poe","Carrol","Cortázar"};
        try {
            initializeGraph();
            if (!graph.isEmpty()) {
                drawGraph(); // Dibujar el grafo si no está vacío
            }
        } catch (GraphException | ListException e) {
            e.printStackTrace();
        }
    }

    private void initializeGraph() throws GraphException, ListException {
        // Limpiar el grafo existente
        graph.clear();

        // Mezclar los nombres y seleccionar los primeros 10
        List<String> namesList = Arrays.asList(vertexNames);
        Collections.shuffle(namesList);
        List<String> selectedNames = namesList.subList(0, 10);

        // Inicializar el grafo con los vértices seleccionados
        for (String vertex : selectedNames) {
            graph.addVertex(vertex);
        }

        // Agregar aristas con conexiones aleatorias
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

    @javafx.fxml.FXML
    public void randomizeOnAction(ActionEvent actionEvent) {
        // Reinicializar el grafo con aristas y pesos aleatorios
        try {
            initializeGraph();
            drawGraph(); // Redibujar el grafo
        } catch (GraphException | ListException e) {
            e.printStackTrace();
        }
    }

    @javafx.fxml.FXML
    public void addVertexOnAction(ActionEvent actionEvent) {
        try {
            // Seleccionar un nombre aleatorio del arreglo vertexNames
            int index = rand.nextInt(vertexNames.length);
            String vertexName = vertexNames[index];

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

        try {
            if (graph == null || graph.isEmpty()) {
                System.out.println("No hay vértices para dibujar.");
                return;
            }
            int n = graph.size();

            // Verificar si no hay vértices en el grafo
            if (n == 0) {
                System.out.println("No hay vértices para dibujar.");
                return;
            }

            // Dibujar vértices
            double paneWidth = paneGraph.getWidth();
            double paneHeight = paneGraph.getHeight();
            double centerX = paneWidth / 2;
            double centerY = paneHeight / 2;
            double radius = Math.min(paneWidth, paneHeight) / 3;
            double angleStep = 2 * Math.PI / n;

            for (int i = 1; i <= n; i++) {
                Vertex vertex = graph.getVertex(i);
                String vertexName = vertex.getData().toString();
                double angle = i * angleStep;
                double x = centerX + radius * Math.cos(angle);
                double y = centerY + radius * Math.sin(angle);

                Circle circle = new Circle(x, y, 20); // Aumentar el tamaño del círculo si es necesario
                circle.setStyle("-fx-fill: lightblue; -fx-stroke: black;");
                Text text = new Text(x - 10, y + 5, vertexName);

                vertexMap.put(vertexName, circle);
                paneGraph.getChildren().addAll(circle, text);
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
                    paneGraph.getChildren().add(0, line); // Agregar la línea al principio para que se dibuje debajo de los vértices
                }
            }
        } catch (ListException e) {
            System.out.println("Error al acceder a la lista en el grafo: " + e.getMessage());
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
