package domain;

import domain.list.ListException;
import domain.queue.LinkedQueue;
import domain.queue.QueueException;
import domain.stack.LinkedStack;
import domain.stack.StackException;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdjacencyMatrixGraph implements Graph {
    private int n;
    private Vertex vertexList[];
    private Object adjacencyMatrix[][];
    private int counter; //contador de vertices

    //para los recorridos dfs, bfs
    private LinkedStack stack;
    private LinkedQueue queue;

    //Constructor
    public AdjacencyMatrixGraph(int n){
        if(n<=0) System.exit(1);
        this.n = n;
        initObjects();
    }

    private void initObjects() {
        this.counter = 0;
        this.vertexList = new Vertex[n];
        this.adjacencyMatrix = new Object[n][n];
        this.stack = new LinkedStack();
        this.queue = new LinkedQueue();
        initMatrix(); //init matriz
    }

    private void initMatrix() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                adjacencyMatrix[i][j] = 0;
            }
        }
    }

    @Override
    public int size() throws ListException {
        return counter;
    }

    @Override
    public void clear() {
        initObjects();
    }

    @Override
    public boolean isEmpty() {
        return counter==0;
    }

    @Override
    public boolean containsVertex(Object element) throws GraphException, ListException {
        if(isEmpty()){
            throw new GraphException("Adjacency Matrix Graph is empty");
        }
        for (int i = 0; i < counter; i++) {
            if(util.Utility.compare(vertexList[i].data, element)==0)
                return true;
        }
        return false;
    }

    @Override
    public boolean containsEdge(Object a, Object b) throws GraphException, ListException {
        if(isEmpty()){
            throw new GraphException("Adjacency Matrix Graph is empty");
        }
        return !(util.Utility.compare(adjacencyMatrix[indexOf(a)][indexOf(b)], 0)==0);
    }

    @Override
    public void addVertex(Object element) throws GraphException, ListException {
        if(counter>=vertexList.length){
            throw new GraphException("Adjacency matrix graph is full");
        }
        vertexList[counter++] = new Vertex(element);
    }

    @Override
    public void addEdge(Object a, Object b) throws GraphException, ListException {
        if(!containsVertex(a) || !containsVertex(b)){
            throw new GraphException("Cannot add edge between vertexes ["
                    +a+"] and ["+b+"]");
        }
        adjacencyMatrix[indexOf(a)][indexOf(b)] = 1;
        adjacencyMatrix[indexOf(b)][indexOf(a)] = 1; //grafo no dirigido
    }

    private int indexOf(Object value) {
        for (int i = 0; i < counter; i++) {
            if(util.Utility.compare(vertexList[i].data, value)==0)
                return i; //retorno el indice
        }
        return -1; //significa que no lo encontro
    }

    @Override
    public void addWeight(Object a, Object b, Object weight) throws GraphException, ListException {
        if(!containsEdge(a, b)){
            throw new GraphException("Cannot add weight between vertexes ["
                    +a+"] and ["+b+"]");
        }
        adjacencyMatrix[indexOf(a)][indexOf(b)] = weight;
        adjacencyMatrix[indexOf(b)][indexOf(a)] = weight; //grafo no dirigido
    }

    @Override
    public void addEdgeWeight(Object a, Object b, Object weight) throws GraphException, ListException {
        if(!containsVertex(a) || !containsVertex(b)){
            throw new GraphException("Cannot add edge between vertexes ["
                    +a+"] and ["+b+"]");
        }
        adjacencyMatrix[indexOf(a)][indexOf(b)] = weight;
        adjacencyMatrix[indexOf(b)][indexOf(a)] = weight; //grafo no dirigido
    }

    @Override
    public void removeVertex(Object element) throws GraphException, ListException {
        if(isEmpty()){
            throw new GraphException("Adjacency Matrix Graph is empty");
        }
        int index = indexOf(element);
        if(index!=-1){ //si el vertice existe en el grafo
            for (int i = index; i < counter-1; i++) {
                vertexList[i] = vertexList[i+1];

                //en la matriz movemos todas filas, una pos hacia arriba
                for (int j = 0; j < counter; j++) {
                    adjacencyMatrix[i][j] = adjacencyMatrix[i+1][j];
                }
            }
            //en la matriz, movemos todas las cols, una pos a la izq
            for (int i = 0; i < counter; i++) {
                for (int j = index; j < counter-1; j++) {
                    adjacencyMatrix[i][j] = adjacencyMatrix[i][j+1];
                }
            }
            counter--; //se decrementa por el vertice suprimido
        }
        //al final, si ya no quedan vertices, verificamos q se remuevan
        //toas las aristas
        if(counter==0){
            initMatrix();
        }
    }

    @Override
    public void removeEdge(Object a, Object b) throws GraphException, ListException {
        if(!containsVertex(a)||!containsVertex(b)){
            throw new GraphException("There's no some of the vertexes");
        }
        int i = indexOf(a);
        int j = indexOf(b);
        if(i!=-1&&j!=-1){
            adjacencyMatrix[i][j] = 0;
            adjacencyMatrix[j][i] = 0;
        }
    }

    @Override
    public String dfs() throws GraphException, StackException, ListException {
        setVisited(false);//marca todos los vertices como no vistados
        // inicia en el vertice 0
        String info =vertexList[0].data+", ";
        vertexList[0].setVisited(true); // lo marca
        stack.clear();
        stack.push(0); //lo apila
        while( !stack.isEmpty() ){
            // obtiene un vertice adyacente no visitado,
            //el que esta en el tope de la pila
            int index = adjacentVertexNotVisited((int) stack.top());
            if(index==-1) // no lo encontro
                stack.pop();
            else{
                vertexList[index].setVisited(true); // lo marca
                info+=vertexList[index].data+", "; //lo muestra
                stack.push(index); //inserta la posicion
            }
        }
        return info;
    }

    @Override
    public String bfs() throws GraphException, QueueException, ListException {
        setVisited(false);//marca todos los vertices como no visitados
        // inicia en el vertice 0
        String info =vertexList[0].data+", ";
        vertexList[0].setVisited(true); // lo marca
        queue.clear();
        queue.enQueue(0); // encola el elemento
        int v2;
        while(!queue.isEmpty()){
            int v1 = (int) queue.deQueue(); // remueve el vertice de la cola
            // hasta que no tenga vecinos sin visitar
            while((v2=adjacentVertexNotVisited(v1)) != -1 ){
                // obtiene uno
                vertexList[v2].setVisited(true); // lo marca
                info+=vertexList[v2].data+", "; //lo muestra
                queue.enQueue(v2); // lo encola
            }
        }
        return info;
    }

    //setteamos el atributo visitado del vertice respectivo
    private void setVisited(boolean value) {
        for (int i = 0; i < counter; i++) {
            vertexList[i].setVisited(value); //value==true o false
        }//for
    }

    private int adjacentVertexNotVisited(int index) {
        for (int i = 0; i < counter; i++) {
            if(!adjacencyMatrix[index][i].equals(0)
                    && !vertexList[i].isVisited())
                return i;//retorna la posicion del vertice adyacente no visitado
        }//for i
        return -1;
    }

    public String shortestPath(Object start, Object end) throws GraphException, ListException {
        if (!containsVertex(start) || !containsVertex(end)) {
            throw new GraphException("One or both vertices not found.");
        }

        int startIndex = indexOf(start);
        int endIndex = indexOf(end);
        int[] dist = new int[n];
        boolean[] visited = new boolean[n];
        int[] prev = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[startIndex] = 0;

        for (int i = 0; i < n; i++) {
            int u = -1;
            for (int j = 0; j < n; j++) {
                if (!visited[j] && (u == -1 || dist[j] < dist[u])) {
                    u = j;
                }
            }
            if (dist[u] == Integer.MAX_VALUE) break;
            visited[u] = true;

            for (int v = 0; v < n; v++) {
                if (adjacencyMatrix[u][v] != null && !visited[v]) {
                    int newDist = dist[u] + (int) adjacencyMatrix[u][v];
                    if (newDist < dist[v]) {
                        dist[v] = newDist;
                        prev[v] = u;
                    }
                }
            }
        }

        StringBuilder path = new StringBuilder();
        for (int at = endIndex; at != startIndex; at = prev[at]) {
            path.insert(0, vertexList[at] + " ");
        }
        path.insert(0, vertexList[startIndex] + " ");
        return path.toString().trim();
    }

    @Override
    public List<Pair<Integer, Integer>> getNeighbors(int vertex) throws GraphException, ListException {
        if (isEmpty()) {
            throw new GraphException("Adjacency Matrix Graph is empty");
        }
        if (vertex < 0 || vertex >= counter) {
            throw new GraphException("Invalid vertex index: " + vertex);
        }

        List<Pair<Integer, Integer>> neighbors = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            if (adjacencyMatrix[vertex][i] != null && !adjacencyMatrix[vertex][i].equals(0)) {
                if (adjacencyMatrix[vertex][i] instanceof Integer) {
                    neighbors.add(new Pair<>(i, (Integer) adjacencyMatrix[vertex][i]));
                } else {
                    throw new GraphException("Invalid weight type at adjacencyMatrix[" + vertex + "][" + i + "]");
                }
            }
        }

        return neighbors;
    }







    @Override
    public List<Object> getVertices() throws ListException {
        List<Object> vertices = new ArrayList<>();
        for (int i = 0; i < counter; i++) {
            vertices.add(vertexList[i].data);
        }
        return vertices;
    }

    @Override
    public void updateVertexValue(Object vertexData, Object value) throws GraphException, ListException {
        int index = indexOf(vertexData);
        if (index == -1) {
            throw new GraphException("Vertex not found in the graph.");
        }

        // Actualizar el valor del vértice
        vertexList[index].setData(value);
    }



    @Override
    public String toString() {
        String result = "Adjacency Matrix Graph Content";
        for (int i = 0; i < counter; i++) {
            result+="\nThe vertex in the position: "+i+" is: "
                    +vertexList[i].data;
        }
        result+="\n";
        for (int i = 0; i < counter; i++) {
            for (int j = 0; j < counter; j++) {
                if(!adjacencyMatrix[i][j].equals(0)){
                    result+="\nThere is edge between the vertexes: "
                            +vertexList[i].data+"...."+vertexList[j].data;
                    if(!adjacencyMatrix[i][j].equals(1)){
                        result+="\n____WEIGHT: "+adjacencyMatrix[i][j];
                    }
                }
            }
        }
        return result;
    }
}