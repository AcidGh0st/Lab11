package domain;

import domain.list.ListException;
import domain.list.SinglyLinkedList;
import domain.queue.LinkedQueue;
import domain.queue.QueueException;
import domain.stack.LinkedStack;
import domain.stack.StackException;

public class DirectedSinglyLinkedListGraph implements Graph {
    private SinglyLinkedList vertexList;

    //para los recorridos dfs, bfs
    private LinkedStack stack;
    private LinkedQueue queue;

    //Constructor
    public DirectedSinglyLinkedListGraph(){
        initObjects();
    }

    private void initObjects() {
        this.vertexList = new SinglyLinkedList();
        this.stack = new LinkedStack();
        this.queue = new LinkedQueue();
    }

    @Override
    public int size() throws ListException {
        return this.vertexList.size();
    }

    @Override
    public void clear() {
        initObjects();
    }

    @Override
    public boolean isEmpty() {
        return this.vertexList.isEmpty();
    }

    @Override
    public boolean containsVertex(Object element) throws GraphException, ListException {
       if(isEmpty()){
           throw new GraphException("Directed Singly Linked List Graph is empty");
       }
       int n = vertexList.size();
        for (int i = 1; i <= n; i++) {
           Vertex vertex = (Vertex)vertexList.getNode(i).data;
           if(util.Utility.compare(vertex.data, element)==0)
               return true;
        }
        return false;
    }

    @Override
    public boolean containsEdge(Object a, Object b) throws GraphException, ListException {
        if(isEmpty()){
            throw new GraphException("Directed Singly Linked List Graph is empty");
        }
        if(!containsVertex(a)||!containsVertex(b)) return false;

        int n = vertexList.size();
        for (int i = 1; i <= n; i++) {
            Vertex vertex = (Vertex)vertexList.getNode(i).data;
            if((util.Utility.compare(vertex.data, a)==0)
                    &&!vertex.edgesList.isEmpty()
                    &&vertex.edgesList.contains(new EdgeWeight(b, null))
            )
                return true;
        }
        return false; //no existe la arista
    }

    @Override
    public void addVertex(Object element) throws GraphException, ListException {
        if(vertexList.isEmpty())
            vertexList.add(new Vertex(element));
        //esta validacion es para que no agregue repetidos
        else if(!vertexList.contains(new Vertex(element)))
            vertexList.add(new Vertex(element));
    }

    @Override
    public void addEdge(Object a, Object b) throws GraphException, ListException {
        if(!containsVertex(a) || !containsVertex(b)){
            throw new GraphException("Cannot add edge between vertexes ["
                    +a+"] and ["+b+"]");
        }
        addVertexEdgeWeight(a, b, null, "addEdge");
       }

    private void addVertexEdgeWeight(Object a, Object b, Object weight, String action) throws ListException {
        for (int i = 1; i <= vertexList.size(); i++) {
            Vertex vertex = (Vertex)vertexList.getNode(i).data;
            if((util.Utility.compare(vertex.data, a)==0)){
                switch (action){
                    case "addEdge":
                        vertex.edgesList.add(new EdgeWeight(b, weight));
                        break;
                    case "addWeight":
                       vertex.edgesList.getNode(new EdgeWeight(b,null))
                               .setData(new EdgeWeight(b, weight));
                       break;
                    case "remove":
                        if(vertex.edgesList!=null&&!vertex.edgesList.isEmpty())
                            vertex.edgesList.remove(new EdgeWeight(b, weight));
                }
            }
        }
    }

    @Override
    public void addWeight(Object a, Object b, Object weight) throws GraphException, ListException {
        if(!containsEdge(a, b)){
            throw new GraphException("Cannot add weight between vertexes ["
                    +a+"] and ["+b+"]");
        }
        addVertexEdgeWeight(a, b, weight, "addWeight");
    }

    @Override
    public void addEdgeWeight(Object a, Object b, Object weight) throws GraphException, ListException {
        if(!containsVertex(a) || !containsVertex(b)){
            throw new GraphException("Cannot add edge between vertexes ["
                    +a+"] and ["+b+"]");
        }
        if(!containsEdge(a, b)){
            addVertexEdgeWeight(a, b, weight, "addEdge");
        }
    }

    @Override
    public void removeVertex(Object element) throws GraphException, ListException {
        if(isEmpty()){
            throw new GraphException("Directed Singly Linked List Graph is empty");
        }
        if(containsVertex(element)) { //si el vertice existe en el grafo
            boolean removed = false;
            for (int i = 1; !removed && i <= vertexList.size(); i++) {
                Vertex vertex = (Vertex) vertexList.getNode(i).data;
                if (util.Utility.compare(vertex.data, element) == 0) {
                    vertexList.remove(vertex); //opcion 1 correcta
                    //vertexList.remove(new Vertex(element)); //opcion 2 correcta
                    removed = true;
                    //ahora se debe eliminar la entrada de ese vertice de todas
                    //las listas de aristas
                    for (int j = 1; vertexList!=null&&!vertexList.isEmpty()
                            && j <=vertexList.size() ; j++) {
                        vertex = (Vertex) vertexList.getNode(j).data;
                        if(!vertex.edgesList.isEmpty())
                            addVertexEdgeWeight(vertex.data, element, null, "remove");
                    }
                }
            }
        }

    }

    @Override
    public void removeEdge(Object a, Object b) throws GraphException, ListException {
        if(!containsVertex(a)||!containsVertex(b)){
            throw new GraphException("There's no some of the vertexes");
        }
        addVertexEdgeWeight(a, b, null, "remove"); //suprimo la arista
    }

    @Override
    public String dfs() throws GraphException, StackException, ListException {
        setVisited(false);//marca todos los vertices como no vistados
        // inicia en el vertice 1
        Vertex vertex = (Vertex)vertexList.getNode(1).data;
        String info =vertex+", ";
        vertex.setVisited(true); //lo marca
        stack.clear();
        stack.push(1); //lo apila
        while( !stack.isEmpty() ){
            // obtiene un vertice adyacente no visitado,
            //el que esta en el tope de la pila
            int index = adjacentVertexNotVisited((int) stack.top());
            if(index==-1) // no lo encontro
                stack.pop();
            else{
                vertex = (Vertex)vertexList.getNode(index).data;
                vertex.setVisited(true); // lo marca
                info+=vertex+", ";
                stack.push(index); //inserta la posicion
            }
        }
        return info;
    }//dfs

    @Override
    public String bfs() throws GraphException, QueueException, ListException {
        setVisited(false);//marca todos los vertices como no visitados
        // inicia en el vertice 1
        Vertex vertex = (Vertex)vertexList.getNode(1).data;
        String info =vertex+", ";
        vertex.setVisited(true); //lo marca
        queue.clear();
        queue.enQueue(1); // encola el elemento
        int index2;
        while(!queue.isEmpty()){
            int index1 = (int) queue.deQueue(); // remueve el vertice de la cola
            // hasta que no tenga vecinos sin visitar
            while((index2=adjacentVertexNotVisited(index1)) != -1 ){
                // obtiene uno
                vertex = (Vertex)vertexList.getNode(index2).data;
                vertex.setVisited(true); //lo marco
                info+=vertex+", ";
                queue.enQueue(index2); // lo encola
            }
        }
        return info;
    }

    //setteamos el atributo visitado del vertice respectivo
    private void setVisited(boolean value) throws ListException {
        for (int i=1; i<=vertexList.size(); i++) {
            Vertex vertex = (Vertex)vertexList.getNode(i).data;
            vertex.setVisited(value); //value==true or false
        }//for
    }

    private int adjacentVertexNotVisited(int index) throws ListException {
        Vertex vertex1 = (Vertex)vertexList.getNode(index).data;
        for(int i=1; i<=vertexList.size(); i++){
            Vertex vertex2 = (Vertex)vertexList.getNode(i).data;
            if(!vertex2.edgesList.isEmpty()&&vertex2.edgesList
                    .contains(new EdgeWeight(vertex1.data, null))
                    && !vertex2.isVisited())
                return i;
        }
        return -1;
    }

    @Override
    public String toString() {
        String result = "DIRECTED SINGLY LINKED LIST GRAPH CONTENT...\n";
        try {
            for(int i=1; i<=vertexList.size(); i++){
                Vertex vertex = (Vertex)vertexList.getNode(i).data;
                result+="\nThe vertex in the position "+i+" is: "+vertex+"\n";
                if(!vertex.edgesList.isEmpty()){
                    result+="........EDGES AND WEIGHTS: "+vertex.edgesList+"\n";
                }//if

            }//for
        } catch (ListException ex) {
            System.out.println(ex.getMessage());
        }

        return result;
    }

}
