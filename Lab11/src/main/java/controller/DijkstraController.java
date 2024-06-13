package controller;

import domain.*;
import domain.list.ListException;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.util.Pair;
import util.UtilityFX;

import java.util.*;


public class DijkstraController {
    @FXML
    private RadioButton radbLinkedList;
    @FXML
    private RadioButton radbAdjMatrix;
    @FXML
    private RadioButton radbAdjList;
    @FXML
    private TableColumn<Pair<Integer, Pair<Integer, Integer>>, Integer> columnVertex;
    @FXML
    private TableColumn<Pair<Integer, Pair<Integer, Integer>>, Integer> columnPosition;
    @FXML
    private TableColumn<Pair<Integer, Pair<Integer, Integer>>, Integer> columnDistance;
    @FXML
    private Button btnRandomize;
    @FXML
    private BorderPane bp;
    @FXML
    private TableView<Pair<Integer, Pair<Integer, Integer>>> tableView;
    @FXML
    private Pane midPane; // Pane para dibujar el grafo

    private Graph graph;
    private ObservableList<Pair<Integer, Pair<Integer, Integer>>> data;
    private Map<Integer, Circle> vertexMap; // Mapa para almacenar los vértices y sus posiciones

    @FXML
    public void initialize() {
        ToggleGroup toggleGroup = new ToggleGroup();
        radbLinkedList.setToggleGroup(toggleGroup);
        radbAdjMatrix.setToggleGroup(toggleGroup);
        radbAdjList.setToggleGroup(toggleGroup);

        columnPosition.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getKey()));
        columnVertex.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getValue().getKey()));
        columnDistance.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getValue().getValue()));

        data = FXCollections.observableArrayList();
        tableView.setItems(data);

        vertexMap = new HashMap<>();
    }

    @FXML
    public void randomizeOnAction() {
        if (graph == null) {
            UtilityFX.alert("Error", "Graph is not initialized! Please select a graph type.").show();
            return;
        }

        int numVertices = 10; // Ajustado para incluir el vértice en la posición 0
        int maxEdges = 20; // Número máximo de aristas

        Set<Integer> vertices = new HashSet<>();
        Random random = new Random();

        graph.clear();

        while (vertices.size() < numVertices) {
            int vertexValue = random.nextInt(99) + 1; // Genera valores aleatorios entre 1 y 99
            vertices.add(vertexValue);
        }

        for (int vertex : vertices) {
            try {
                graph.addVertex(vertex); // Agrega vértices aleatorios únicos
            } catch (GraphException | ListException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < maxEdges; i++) {
            List<Integer> vertexList = new ArrayList<>(vertices);
            int src = vertexList.get(random.nextInt(vertexList.size())); // Obtiene un vértice de manera aleatoria
            int dest = vertexList.get(random.nextInt(vertexList.size())); // Obtiene otro vértice de manera aleatoria
            int weight = 200 + random.nextInt(801); // Peso aleatorio

            try {
                if (!graph.containsEdge(src, dest)) {
                    graph.addEdgeWeight(src, dest, weight);
                }
            } catch (GraphException | ListException e) {
                e.printStackTrace();
            }
        }

        applyDijkstra();
        drawGraph(); // Dibujar el grafo después de la aleatorización
    }

    @FXML
    public void adjListOnAction() {
        try {
            graph = new AdjacencyListGraph(15); // Inicializar grafo con capacidad para 100 vértices
            UtilityFX.alert("List of Adjacency", "Adjacency List graph initialized!").show();
        } catch (Exception e) {
            e.printStackTrace();
            UtilityFX.alert("Error", "Failed to initialize Adjacency List graph: " + e.getMessage()).show();
        }
    }

    @FXML
    public void adjMatrixOnAction() {
        graph = new AdjacencyMatrixGraph(15); // Inicializar grafo con capacidad para 100 vértices
        UtilityFX.alert("Adjacency Matrix", "Adjacency Matrix graph initialized!").show();
    }

    @FXML
    public void linkedListOnAction() {
        try {
            graph = new SinglyLinkedListGraph(); // Inicializar grafo con la implementación de lista enlazada
            UtilityFX.alert("Linked List", "Linked List graph initialized!").show();
        } catch (Exception e) {
            e.printStackTrace();
            UtilityFX.alert("Error", "Failed to initialize Linked List graph: " + e.getMessage()).show();
        }
    }

    private void applyDijkstra() {
        if (graph == null) {
            UtilityFX.alert("Error", "Graph is not initialized! Please select a graph type.").show();
            return;
        }

        data.clear();
        try {
            int source = 0; // Vértice de inicio por defecto
            Map<Integer, Integer> distances = dijkstra(source);

            int position = 0;
            for (int i = 0; i < graph.size(); i++) {
                int vertex = i;
                int distance = distances.containsKey(i) ? distances.get(i) : Integer.MAX_VALUE; // Usar MAX_VALUE para mostrar no conectado
                data.add(new Pair<>(position++, new Pair<>(vertex, distance))); // Ajustar el orden de los valores en el Pair
            }
        } catch (GraphException | ListException e) {
            e.printStackTrace();
        }
    }

    private Map<Integer, Integer> dijkstra(int start) throws GraphException, ListException {
        int n = graph.size();
        Map<Integer, Integer> distances = new HashMap<>();
        PriorityQueue<Pair<Integer, Integer>> queue = new PriorityQueue<>(Comparator.comparingInt(Pair::getValue));
        Set<Integer> visited = new HashSet<>();

        // Inicializar todas las distancias con un valor alto excepto el vértice de inicio
        for (int i = 0; i < n; i++) {
            distances.put(i, Integer.MAX_VALUE);
        }

        // Colocar el vértice de inicio en la cola con distancia inicial 0
        queue.add(new Pair<>(start, 0));
        distances.put(start, 0);

        while (!queue.isEmpty()) {
            int current = queue.poll().getKey();

            if (!visited.add(current)) {
                continue;
            }

            List<Pair<Integer, Integer>> neighbors = graph.getNeighbors(current);
            for (Pair<Integer, Integer> neighbor : neighbors) {
                int neighborVertex = neighbor.getKey();
                int edgeWeight = neighbor.getValue();

                if (distances.get(current) + edgeWeight < distances.get(neighborVertex)) {
                    distances.put(neighborVertex, distances.get(current) + edgeWeight);
                    queue.add(new Pair<>(neighborVertex, distances.get(neighborVertex)));
                }
            }
        }

        return distances;
    }

    private void drawGraph() {
        midPane.getChildren().clear();
        vertexMap.clear();

        double paneWidth = midPane.getWidth();
        double paneHeight = midPane.getHeight();
        double centerX = paneWidth / 2;
        double centerY = paneHeight / 2;
        double radius = Math.min(paneWidth, paneHeight) / 3;

        try {
            int n = graph.size();
            double angleStep = 2 * Math.PI / n;

            // Dibujar vértices en un círculo
            for (int i = 0; i < n; i++) {
                double angle = i * angleStep;
                double x = centerX + radius * Math.cos(angle);
                double y = centerY + radius * Math.sin(angle);

                Circle circle = new Circle(x, y, 20); // Tamaño del círculo
                circle.setStyle("-fx-fill: lightblue; -fx-stroke: black;");

                Text text = new Text(String.valueOf(i)); // Valor del vértice
                text.setX(x - 5); // Ajustar posición del texto
                text.setY(y + 5); // Ajustar posición del texto

                vertexMap.put(i, circle);
                midPane.getChildren().addAll(circle, text);
            }

            // Dibujar aristas
            for (int i = 0; i < n; i++) {
                List<Pair<Integer, Integer>> neighbors = graph.getNeighbors(i);
                Circle sourceCircle = vertexMap.get(i);
                for (Pair<Integer, Integer> neighbor : neighbors) {
                    int targetVertex = neighbor.getKey();
                    Circle targetCircle = vertexMap.get(targetVertex);

                    Line line = new Line(sourceCircle.getCenterX(), sourceCircle.getCenterY(),
                            targetCircle.getCenterX(), targetCircle.getCenterY());
                    line.setStrokeWidth(2); // Ajustar el ancho de la línea si es necesario
                    midPane.getChildren().add(0, line); // Dibujar debajo de los vértices

                    // Agregar etiquetas de peso de arista
                    Text weightText = new Text(String.valueOf(neighbor.getValue()));
                    weightText.setX((sourceCircle.getCenterX() + targetCircle.getCenterX()) / 2);
                    weightText.setY((sourceCircle.getCenterY() + targetCircle.getCenterY()) / 2);
                    weightText.setFill(Color.RED); // Cambiar color del peso a rojo
                    midPane.getChildren().add(weightText);
                }
            }
        } catch (ListException | GraphException e) {
            e.printStackTrace();
        }
    }
}


