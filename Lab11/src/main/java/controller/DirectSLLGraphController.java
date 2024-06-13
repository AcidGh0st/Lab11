package controller;

import domain.DirectedSinglyLinkedListGraph;
import domain.EdgeWeight;
import domain.GraphException;
import domain.Vertex;
import domain.list.ListException;
import domain.queue.QueueException;
import domain.stack.StackException;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;

import java.util.*;

public class DirectSLLGraphController
{
    @javafx.fxml.FXML
    private Text textMessage;
    @javafx.fxml.FXML
    private BorderPane bp;
    @javafx.fxml.FXML
    private TextArea textArea;
    @javafx.fxml.FXML
    private Pane paneGraph;
    private DirectedSinglyLinkedListGraph graph;
    private Map<String, Circle> vertexMap;

    @javafx.fxml.FXML
    public void initialize() {

    }

    private void drawGraph() {
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

                        line.setOnMouseMoved(event -> {
                            line.setStroke(Color.	rgb(255, 105, 180));
                            String message = "Edge between vertexes: [" + vertexName + "]......[" + targetVertexName + "]. Weight: " + edge.getWeight();
                            textMessage.setText(message);

                        });

                        paneGraph.getChildren().add(arrow);
                    }
                }
            }
        } catch (ListException e) {
            e.printStackTrace();
        }
    }


    @javafx.fxml.FXML
    public void toStringOnAction(ActionEvent actionEvent) {
        textArea.setText(graph.toString());
    }

    @javafx.fxml.FXML
    public void DFSOnAction(ActionEvent actionEvent) {
        try {
            textArea.clear();
            String result = graph.dfs();
            textArea.setText(result);
        } catch (GraphException | StackException | ListException e) {
            throw new RuntimeException(e);
        }
    }

    @javafx.fxml.FXML
    public void randomizeOnAction(ActionEvent actionEvent) {
        try {
            graph = new DirectedSinglyLinkedListGraph();
            vertexMap = new HashMap<>();
            Set<Integer> weight = new HashSet<>();
            String[] vertexes = {
                    "Muralla China", "Torre Eiffel", "Taj Mahal", "Coliseo Romano", "Estatua de la Libertad",
                    "Cristo Redentor", "Pirámides de Giza", "Machu Picchu", "Acrópolis de Atenas", "Ciudad Prohibida",
                    "Gran Muralla de China", "Mausoleo de Halicarnaso", "Petra", "Torre de Londres", "Angkor Wat",
                    "Partenón", "Stonehenge", "Esfinge de Guiza", "Monte Rushmore", "Muro de Berlín",
                    "Castillo de Neuschwanstein", "Templo de Karnak", "Alhambra", "Sagrada Familia", "Templo de Borobudur",
                    "Torre Inclinada de Pisa", "Moais de la Isla de Pascua", "Catedral de Notre Dame", "Templo de Luxor",
                    "Acueducto de Segovia"
            };

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
    public void BFSOnAction(ActionEvent actionEvent) {
        try {
            textArea.clear();
            String result = graph.bfs();
            textArea.setText(result);
        } catch (GraphException | ListException | QueueException e) {
            throw new RuntimeException(e);
        }
    }

    @javafx.fxml.FXML
    public void containsVertexOnAction(ActionEvent actionEvent) {
        util.UtilityFX.dialog("Contains Vertex", "Enter the vertex you want to check:","Vertex:").showAndWait().ifPresent(vertex -> {
            boolean exists;
            try {
                exists = graph.containsVertex(vertex);
            } catch (GraphException | ListException e) {
                throw new RuntimeException(e);
            }
            // Mostrar un mensaje dependiendo del resultado
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

    @javafx.fxml.FXML
    public void containsEdgeOnAction(ActionEvent actionEvent) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Verificar Arista");
        dialog.setHeaderText("Ingrese los vértices para verificar la arista:");
        dialog.setContentText("Vértice A:");


        dialog.showAndWait().ifPresent(vertexA -> {

            dialog.setHeaderText("Ingrese el segundo vértice:");
            dialog.setContentText("Vértice B:");

            dialog.showAndWait().ifPresent(vertexB -> {
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
                    util.UtilityFX.alert("Error",
                            "Ocurrió un error al verificar la arista: ", Alert.AlertType.ERROR);
                }
            });
        });
    }
}
