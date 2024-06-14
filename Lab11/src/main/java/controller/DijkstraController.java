package controller;

import domain.*;
import domain.list.ListException;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    private ObservableList<Pair<Integer, Pair<Integer, Integer>>> data = FXCollections.observableArrayList();
    private Map<Integer, Circle> vertexMap = new HashMap<>(); // Mapa para almacenar los vértices y sus posiciones

    @FXML
    public void initialize() {
        ToggleGroup toggleGroup = new ToggleGroup();
        radbLinkedList.setToggleGroup(toggleGroup);
        radbAdjMatrix.setToggleGroup(toggleGroup);
        radbAdjList.setToggleGroup(toggleGroup);

        columnPosition.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getKey()));
        columnVertex.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getValue().getKey()));
        columnDistance.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getValue().getValue()));

        tableView.setItems(data);
    }

    @FXML
    public void randomizeOnAction() {
        if (graph == null) {
            UtilityFX.alert("Error", "Graph is not initialized! Please select a graph type.").show();
            return;
        }

        int numVertices = 10;
        Set<Integer> vertices = new HashSet<>();
        Random random = new Random();

        graph.clear();

        while (vertices.size() < numVertices) {
            int vertexValue = random.nextInt(99) + 1;
            vertices.add(vertexValue);
        }

        Map<Integer, Integer> vertexIndexMap = new HashMap<>();
        int index = 0;

        for (int vertex : vertices) {
            vertexIndexMap.put(vertex, index);
            try {
                graph.addVertex(index);
            } catch (GraphException | ListException e) {
                e.printStackTrace();
            }
            index++;
        }

        List<Integer> vertexList = new ArrayList<>(vertexIndexMap.values());
        for (int i = 0; i < numVertices * 2; i++) {
            int srcIndex = vertexList.get(random.nextInt(vertexList.size()));
            int destIndex = vertexList.get(random.nextInt(vertexList.size()));
            int weight = 200 + random.nextInt(801);

            try {
                if (srcIndex != destIndex && !graph.containsEdge(srcIndex, destIndex)) {
                    graph.addEdgeWeight(srcIndex, destIndex, weight);
                }
            } catch (GraphException | ListException e) {
                e.printStackTrace();
            }
        }

        applyDijkstra(vertices);
        drawGraph(vertices, vertexIndexMap);
    }



    @FXML
    public void adjListOnAction() {
        try {
            graph = new AdjacencyListGraph(100); // Inicializar grafo con capacidad para 15 vértices
            UtilityFX.alert("List of Adjacency", "Adjacency List graph initialized!").show();
        } catch (Exception e) {
            e.printStackTrace();
            UtilityFX.alert("Error", "Failed to initialize Adjacency List graph: " + e.getMessage()).show();
        }
    }

    @FXML
    public void adjMatrixOnAction() {
        graph = new AdjacencyMatrixGraph(100); // Inicializar grafo con capacidad para 15 vértices
        UtilityFX.alert("Adjacency Matrix", "Adjacency Matrix graph initialized!").show();
    }

    @FXML
    public void linkedListOnAction() {
        try {
            System.out.println("Initializing LinkedList Graph...");
            graph = new SinglyLinkedListGraph(); // Inicializar grafo con la implementación de lista enlazada
            UtilityFX.alert("Linked List", "Linked List graph initialized!").show();
            System.out.println("LinkedList Graph initialized successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            UtilityFX.alert("Error", "Failed to initialize Linked List graph: " + e.getMessage()).show();
        }
    }
    private void applyDijkstra(Set<Integer> vertices) {
        if (graph == null) {
            UtilityFX.alert("Error", "Graph is not initialized! Please select a graph type.").show();
            return;
        }

        data.clear();
        try {
            List<Integer> vertexList = new ArrayList<>(vertices);
            // Mapa para mantener la correspondencia entre los índices y los vértices originales
            Map<Integer, Integer> vertexMap = new HashMap<>();
            for (int i = 0; i < vertexList.size(); i++) {
                vertexMap.put(i, vertexList.get(i));
            }

            // Seleccionar un vértice de origen de la lista de índices
            int sourceIndex = new Random().nextInt(vertexList.size());
            int source = vertexMap.get(sourceIndex);
            System.out.println("Applying Dijkstra from source: " + source);

            // Ejecutar Dijkstra desde el índice del vértice de origen
            Map<Integer, Integer> distances = dijkstra(sourceIndex);
            System.out.println("Distances: " + distances);

            int position = 0;
            for (int vertex : vertexList) {
                int vertexIndex = getIndexForVertex(vertex, vertexMap); // Convertir el vértice al índice
                int distance = distances.getOrDefault(vertexIndex, Integer.MAX_VALUE);
                System.out.println("Position: " + position + ", Vertex: " + vertex + ", Distance: " + distance);
                data.add(new Pair<>(position, new Pair<>(vertex, distance)));
                position++;
            }

            for (Pair<Integer, Pair<Integer, Integer>> pair : data) {
                System.out.println("Table Data - Position: " + pair.getKey() + ", Vertex: "
                        + pair.getValue().getKey() + ", Distance: " + pair.getValue().getValue());
            }
        } catch (GraphException | ListException e) {
            e.printStackTrace();
        }
    }

    private int getIndexForVertex(int vertex, Map<Integer, Integer> vertexMap) {
        for (Map.Entry<Integer, Integer> entry : vertexMap.entrySet()) {
            if (entry.getValue() == vertex) {
                return entry.getKey();
            }
        }
        return -1; // Indicar que no se encontró el índice, debería manejarse adecuadamente
    }

    private Map<Integer, Integer> dijkstra(int start) throws GraphException, ListException {
        int n = graph.size();
        Map<Integer, Integer> distances = new HashMap<>();
        PriorityQueue<Pair<Integer, Integer>> queue = new PriorityQueue<>(Comparator.comparingInt(Pair::getValue));
        Set<Integer> visited = new HashSet<>();

        System.out.println("Initializing distances...");
        for (Object vertexObj : graph.getVertices()) {
            int vertex = (Integer) vertexObj;
            distances.put(vertex, Integer.MAX_VALUE);
        }
        System.out.println("Distances initialized: " + distances);

        queue.add(new Pair<>(start, 0));
        distances.put(start, 0);

        while (!queue.isEmpty()) {
            int current = queue.poll().getKey();
            System.out.println("Processing vertex: " + current);

            if (!visited.add(current)) {
                continue;
            }

            List<Pair<Integer, Integer>> neighbors = graph.getNeighbors(current);
            System.out.println("Neighbors of " + current + ": " + neighbors);
            for (Pair<Integer, Integer> neighbor : neighbors) {
                int neighborVertex = neighbor.getKey();
                int edgeWeight = neighbor.getValue();

                if (distances.get(current) + edgeWeight < distances.get(neighborVertex)) {
                    distances.put(neighborVertex, distances.get(current) + edgeWeight);
                    queue.add(new Pair<>(neighborVertex, distances.get(neighborVertex)));
                }
            }
        }

        System.out.println("Final distances: " + distances);
        return distances;
    }


    private void drawGraph(Set<Integer> vertices, Map<Integer, Integer> vertexIndexMap) {
        midPane.getChildren().clear();
        vertexMap.clear();

        double paneWidth = midPane.getWidth();
        double paneHeight = midPane.getHeight();
        double centerX = paneWidth / 2;
        double centerY = paneHeight / 2;
        double radius = Math.min(paneWidth, paneHeight) / 3;

        try {
            int n = vertices.size();
            double angleStep = 2 * Math.PI / n;

            List<Integer> vertexList = new ArrayList<>(vertices);

            // Dibujar vértices en un círculo
            for (int i = 0; i < n; i++) {
                int vertex = vertexList.get(i);
                double angle = i * angleStep;
                double x = centerX + radius * Math.cos(angle);
                double y = centerY + radius * Math.sin(angle);

                Circle circle = new Circle(x, y, 20); // Tamaño del círculo
                circle.setStyle("-fx-fill: lightblue; -fx-stroke: black;");

                Text text = new Text(String.valueOf(vertex)); // Valor del vértice
                text.setX(x - 5); // Ajustar posición del texto
                text.setY(y + 5); // Ajustar posición del texto

                vertexMap.put(vertexIndexMap.get(vertex), circle); // Guardar en el mapa
                midPane.getChildren().addAll(circle, text);
            }

            // Dibujar aristas
            for (int src : vertices) {
                List<Pair<Integer, Integer>> neighbors = graph.getNeighbors(vertexIndexMap.get(src));
                Circle sourceCircle = vertexMap.get(vertexIndexMap.get(src));
                for (Pair<Integer, Integer> neighbor : neighbors) {
                    int dest = neighbor.getKey();
                    Circle targetCircle = vertexMap.get(dest);

                    if (targetCircle != null) {
                        Line line = new Line(sourceCircle.getCenterX(), sourceCircle.getCenterY(),
                                targetCircle.getCenterX(), targetCircle.getCenterY());
                        line.setStrokeWidth(2);
                        midPane.getChildren().add(0, line);

                        // Agregar etiquetas de peso de arista
                        Text weightText = new Text(String.valueOf(neighbor.getValue()));
                        weightText.setX((sourceCircle.getCenterX() + targetCircle.getCenterX()) / 2);
                        weightText.setY((sourceCircle.getCenterY() + targetCircle.getCenterY()) / 2);
                        weightText.setFill(Color.RED);
                        midPane.getChildren().add(weightText);
                    }
                }
            }
        } catch (ListException | GraphException e) {
            e.printStackTrace();
        }
    }


}