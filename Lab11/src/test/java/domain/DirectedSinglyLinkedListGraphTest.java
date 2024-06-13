package domain;

import domain.list.ListException;
import domain.queue.QueueException;
import domain.stack.StackException;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class DirectedSinglyLinkedListGraphTest {

    @Test
    void test() {
        DirectedSinglyLinkedListGraph graph = new DirectedSinglyLinkedListGraph();
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
    void directedSinglyLinkedListGraphTest(){

    try {

        DirectedSinglyLinkedListGraph graph = new DirectedSinglyLinkedListGraph();


        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);
        graph.addVertex(4);
        graph.addVertex(5);

        Random random = new Random();

        graph.addEdgeWeight(1, 1, random.nextInt(501) + 500);
        graph.addEdgeWeight(1, 3, random.nextInt(501) + 500);
        graph.addEdgeWeight(1, 4, random.nextInt(501) + 500);
        graph.addEdgeWeight(1, 5, random.nextInt(501) + 500);
        graph.addEdgeWeight(2, 2, random.nextInt(501) + 500);
        graph.addEdgeWeight(2, 1, random.nextInt(501) + 500);
        graph.addEdgeWeight(2, 3, random.nextInt(501) + 500);
        graph.addEdgeWeight(2, 4, random.nextInt(501) + 500);
        graph.addEdgeWeight(2, 5, random.nextInt(501) + 500);
        graph.addEdgeWeight(3, 3, random.nextInt(501) + 500);
        graph.addEdgeWeight(3, 4, random.nextInt(501) + 500);
        graph.addEdgeWeight(4, 4, random.nextInt(501) + 500);
        graph.addEdgeWeight(5, 5, random.nextInt(501) + 500);
        graph.addEdgeWeight(5, 1, random.nextInt(501) + 500);
        graph.addEdgeWeight(5, 2, random.nextInt(501) + 500);
        graph.addEdgeWeight(5, 3, random.nextInt(501) + 500);
        graph.addEdgeWeight(5, 4, random.nextInt(501) + 500);


        System.out.println(graph);


        System.out.println("DFS: " + graph.dfs());
        System.out.println("BFS: " + graph.bfs());


        graph.removeVertex(2);
        graph.removeVertex(4);
        graph.removeVertex(5);


        System.out.println(graph);

    } catch (GraphException | ListException | QueueException | StackException e) {
        e.printStackTrace();
    }
}

}
