package domain;

import domain.list.ListException;
import domain.queue.LinkedQueue;
import domain.queue.QueueException;
import domain.stack.LinkedStack;
import domain.stack.StackException;
import javafx.util.Pair;

import java.util.*;

public class AdjacencyListGraph implements Graph {
    private int n;
    private Vertex vertexList[];
    private int counter; //contador de vertices

    //para los recorridos dfs, bfs
    private LinkedStack stack;
    private LinkedQueue queue;

    //Constructor
    public AdjacencyListGraph(int n){
        if(n<=0) System.exit(1);
        this.n = n;
        initObjects();
    }

    private void initObjects() {
        this.counter = 0;
        this.vertexList = new Vertex[n];
        this.stack = new LinkedStack();
        this.queue = new LinkedQueue();
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
           throw new GraphException("Adjacency List Graph is empty");
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
            throw new GraphException("Adjacency List Graph is empty");
        }
        return !vertexList[indexOf(a)].edgesList.isEmpty()
                ? vertexList[indexOf(a)].edgesList.contains(new EdgeWeight(b, null))
                : false;
    }

    @Override
    public void addVertex(Object element) throws GraphException, ListException {
        if(counter>=vertexList.length){
            throw new GraphException("Adjacency list graph is full");
        }
        vertexList[counter++] = new Vertex(element);
    }

    @Override
    public void addEdge(Object a, Object b) throws GraphException, ListException {
        if(!containsVertex(a) || !containsVertex(b)){
            throw new GraphException("Cannot add edge between vertexes ["
                    +a+"] and ["+b+"]");
        }
        vertexList[indexOf(a)].edgesList.add(new EdgeWeight(b, null));
        vertexList[indexOf(b)].edgesList.add(new EdgeWeight(a, null));  //grafo no dirigido
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
        EdgeWeight ew = (EdgeWeight) vertexList[indexOf(a)].edgesList.getNode(new EdgeWeight(b, null)).data;
        ew.setWeight(weight); //settea el peso correspondiente a la arista indicada
        vertexList[indexOf(a)].edgesList.getNode(new EdgeWeight(b, null)).setData(ew);

        //grafo no dirigido
        ew = (EdgeWeight) vertexList[indexOf(b)].edgesList.getNode(new EdgeWeight(a, null)).data;
        ew.setWeight(weight); //settea el peso correspondiente a la arista indicada
        vertexList[indexOf(b)].edgesList.getNode(new EdgeWeight(a, null)).setData(ew);
    }

    @Override
    public void addEdgeWeight(Object a, Object b, Object weight) throws GraphException, ListException {
        if(!containsVertex(a) || !containsVertex(b)){
            throw new GraphException("Cannot add edge between vertexes ["
                    +a+"] and ["+b+"]");
        }
        vertexList[indexOf(a)].edgesList.add(new EdgeWeight(b, weight));
        vertexList[indexOf(b)].edgesList.add(new EdgeWeight(a, weight));  //grafo no dirigido
    }

    @Override
    public void removeVertex(Object element) throws GraphException, ListException {
        if(isEmpty()){
            throw new GraphException("Adjacency List Graph is empty");
        }
        if(containsVertex(element)){ //si el vertice existe en el grafo
            //debo recorrer la lista de vertices y suprimir todas las entradas con el elemento
            //esto significa que estoy eliminando todas las aristas
            for (int i = 0; i < counter; i++) {
                if(util.Utility.compare(vertexList[i].data, element)==0) {
                    //procedo a eliminar todas las aristas
                    for (int j = 0; j < counter; j++) {
                        if (containsEdge(vertexList[j].data, element))
                            removeEdge(vertexList[j].data, element);
                    }

                    //una vez eliminadas todas las aristas, procedemos a suprimir el vertice
                    for (int j = i; i < counter - 1; i++) {
                        vertexList[j] = vertexList[j + 1];
                    }
                    counter--; //por el vertice suprimido
                }
            }
        }
    }

    @Override
    public void removeEdge(Object a, Object b) throws GraphException, ListException {
        if(!containsVertex(a)||!containsVertex(b)){
            throw new GraphException("There's no some of the vertexes");
        }
        if(!vertexList[indexOf(a)].edgesList.isEmpty()){
            vertexList[indexOf(a)].edgesList.remove(new EdgeWeight(b, null));
        }
        //grafo no dirigido
        if(!vertexList[indexOf(b)].edgesList.isEmpty()){
            vertexList[indexOf(b)].edgesList.remove(new EdgeWeight(a, null));
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

    private int adjacentVertexNotVisited(int index) throws ListException {
        Object vertexData = vertexList[index].data;
        for (int i = 0; i < counter; i++) {
            if(!vertexList[i].edgesList.isEmpty() && vertexList[i].edgesList
                    .contains(new EdgeWeight(vertexData, null)) && !vertexList[i].isVisited()
                    && !vertexList[i].isVisited())
                return i;//retorna la posicion del vertice adyacente no visitado
        }//for i
        return -1;
    }

    public String shortestPath(Object start, Object end) throws GraphException, ListException {
        if (!containsVertex(start) || !containsVertex(end)) {
            throw new GraphException("One or both vertices not found.");
        }

        Map<Object, Integer> distances = new HashMap<>();
        Set<Object> visited = new HashSet<>();
        Map<Object, Object> predecessors = new HashMap<>();

        for (Vertex vertex : vertexList) {
            if (vertex != null) {
                distances.put(vertex.data, Integer.MAX_VALUE);
            }
        }
        distances.put(start, 0);

        PriorityQueue<Object> queue = new PriorityQueue<>(Comparator.comparingInt(distances::get));
        queue.add(start);

        while (!queue.isEmpty()) {
            Object current = queue.poll();
            if (visited.contains(current)) continue;
            visited.add(current);

            Vertex vertex = vertexList[indexOf(current)];
            for (int i = 1; i <= vertex.edgesList.size(); i++) {
                EdgeWeight edge = (EdgeWeight) vertex.edgesList.getNode(i).data;
                Object neighbor = edge.getEdge();
                if (visited.contains(neighbor)) continue;

                int newDist = distances.get(current) + (int) edge.getWeight();
                if (newDist < distances.get(neighbor)) {
                    distances.put(neighbor, newDist);
                    predecessors.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        }

        StringBuilder path = new StringBuilder();
        for (Object at = end; at != null; at = predecessors.get(at)) {
            path.insert(0, at + " ");
        }

        return path.toString().trim();
    }

    @Override
    public List<Pair<Integer, Integer>> getNeighbors(int vertex) throws GraphException, ListException {
        if (vertex < 0 || vertex >= counter) {
            throw new GraphException("Vertex index out of bounds.");
        }

        List<Pair<Integer, Integer>> neighbors = new ArrayList<>();
        Vertex v = vertexList[vertex];
        if (v != null && !v.edgesList.isEmpty()) {
            for (int i = 1; i <= v.edgesList.size(); i++) {
                EdgeWeight edge = (EdgeWeight) v.edgesList.getNode(i).data;
                int neighborIndex = indexOf(edge.getEdge());
                if (neighborIndex != -1) {
                    neighbors.add(new Pair<>(neighborIndex, (Integer) edge.getWeight()));
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
    public String toString() {
        String result = "Adjacency List Graph Content";
        for (int i = 0; i < counter; i++) {
            result+="\nThe vertex in the position: "+i+" is: "
                    +vertexList[i].data;
            if(!vertexList[i].edgesList.isEmpty()){
                result+="\n...EDGES AND WEIGHTS: "+vertexList[i].edgesList.toString();
            }
        }
        return result;
    }
}
