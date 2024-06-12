package domain;

import domain.list.ListException;
import domain.queue.QueueException;
import domain.stack.StackException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class SinglyLinkedListGraphTest {


    SinglyLinkedListGraph graph = new SinglyLinkedListGraph();
    @Test
    void test() {

        try {
            graph.addVertex('A');
            graph.addVertex('B');
            graph.addVertex('C');
            graph.addVertex('D');
            graph.addVertex('E');
            graph.addVertex('F');
            graph.addVertex('G');
            graph.addVertex('H');

            graph.addEdgeWeight('A', 'B', 20);
            graph.addEdgeWeight('A', 'E', 15);
            graph.addEdgeWeight('B', 'C', 10);
            graph.addEdgeWeight('E', 'F', 30);
            graph.addEdgeWeight('C', 'D', 40);
            graph.addEdgeWeight('F', 'G', 5);
            graph.addEdgeWeight('G', 'H', 7);

            //lanza una excepción
            //graph.addEdgeWeight('G', 'K', 7);

            System.out.println(graph.toString());

            //Busqueda en Profundidad
            System.out.println("\nRECORRIDO CON EL ALGORITMO DFS (DEPTH FIRST SEARCH): "
                    +"\n"+graph.dfs());

            //Busqueda en Amplitud
            System.out.println("\nRECORRIDO CON EL ALGORITMO BFS (BREADTH FIRST SEARCH): "
                    +"\n"+graph.bfs());

        } catch (GraphException | ListException | StackException | QueueException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    void testSingly(){
    try {
        // a. Cree e instancie un objeto tipo SinglyLinkedListGraph llamado “graph”.
        SinglyLinkedListGraph graph = new SinglyLinkedListGraph();

        // b. Para el grafo, agregue vértices, aristas y pesos de acuerdo con el siguiente modelo:
        // Vertices: A, B, C, D, E, F, G, H, I, J
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        graph.addVertex("D");
        graph.addVertex("E");
        graph.addVertex("F");
        graph.addVertex("G");
        graph.addVertex("H");
        graph.addVertex("I");
        graph.addVertex("J");

        // Edges: (A, B), (A, C), (A, D), (B, F), (C, G), (D, H), (E, F), (F, G), (G, H), (H, I), (G, J)
        graph.addEdge("A", "B");
        graph.addEdge("A", "C");
        graph.addEdge("A", "D");
        graph.addEdge("B", "F");
        graph.addEdge("C", "G");
        graph.addEdge("D", "H");
        graph.addEdge("E", "F");
        graph.addEdge("F", "G");
        graph.addEdge("G", "H");
        graph.addEdge("H", "I");
        graph.addEdge("G", "J");

        // c. Agregue como pesos nombres de personas
        graph.addWeight("A", "B", "John");
        graph.addWeight("A", "C", "Doe");
        graph.addWeight("A", "D", "Jane");
        graph.addWeight("B", "F", "Alice");
        graph.addWeight("C", "G", "Bob");
        graph.addWeight("D", "H", "Charlie");
        graph.addWeight("E", "F", "David");
        graph.addWeight("F", "G", "Eve");
        graph.addWeight("G", "H", "Frank");
        graph.addWeight("H", "I", "Grace");
        graph.addWeight("G", "J", "Hank");

        // d. Muestre el contenido del grafo por consola (vértices, aristas, pesos)
        System.out.println(graph);

        // e. Pruebe los recorridos dfs(), bfs() y muestre los resultados por consola
        System.out.println("DFS: " + graph.dfs());
        System.out.println("BFS: " + graph.bfs());

        // f. Suprima los vértices F, H, J (también deberá suprimir aristas y pesos)
        graph.removeVertex("F");
        graph.removeVertex("H");
        graph.removeVertex("J");

        // g. Pruebe nuevamente los recorridos dfs(), bfs() y muestre los resultados por consola
        System.out.println("DFS after removals: " + graph.dfs());
        System.out.println("BFS after removals: " + graph.bfs());

        // h. Muestre el contenido del grafo por consola (vértices, aristas, pesos)
        System.out.println(graph);

    } catch (GraphException | ListException | QueueException | StackException e) {
        e.printStackTrace();
    }
}

}