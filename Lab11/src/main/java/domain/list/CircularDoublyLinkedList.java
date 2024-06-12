package domain.list;

public class CircularDoublyLinkedList implements List{
    private Node first; //apuntador al inicio de la lista
    private Node last; //apuntador al final de la lista

    public CircularDoublyLinkedList() {
        this.first = this.last = null; //la lista no existe
    }

    @Override
    public int size() throws ListException {
        if(isEmpty()){
            throw new ListException("Circular Doubly Linked List is empty");
        }
        Node aux = first;
        int count=0;
        while(aux!=last){
            count++;
            aux = aux.next; //lo movemos al sgte nodo
        }
        return count+1; //+1 pq cuente el ult nodo
    }

    @Override
    public void clear() {
        this.first = this.last = null; //anulamos la lista
    }

    @Override
    public boolean isEmpty() {
        return this.first == null; //si es nulo está vacía
    }

    @Override
    public boolean contains(Object element) throws ListException {
        if(isEmpty()){
            throw new ListException("Circular Doubly Linked List is empty");
        }
        Node aux = first;
        while(aux!=last){
            if(util.Utility.compare(aux.data, element)==0){
                return true;
            }
            aux = aux.next; //lo movemos al sgte nodo
        }
        //se sale del while cuando aux==last
        //solo nos queda verificar si el elemento a buscar está en el
        //ult nodo
        return util.Utility.compare(aux.data, element)==0;
    }

    @Override
    public void add(Object element) {
        Node newNode = new Node(element);
        if(isEmpty()){
            first = last = newNode;
        }else {
            last.next = newNode;
            //ponemos last a apuntar al ult nodo
            last = newNode;
        }
        //hago el enlace circular
        last.next = first;
        //hago el enlace doble
        first.prev = last;
    }

    @Override
    public void addFirst(Object element) {
        Node newNode = new Node(element);
        if(isEmpty()){
            first = last = newNode;
        }else{
            newNode.next = first;
            //hago el enlace doble
            first.prev = newNode;
            first = newNode;
        }
        //garantizo siempre el enlace Circular y doble
        last.next = first;
        first.prev = last;

    }

    @Override
    public void addLast(Object element) {
        add(element);
    }

    @Override
    public void addInSortedList(Object element) {

    }

    @Override
    public void remove(Object element) throws ListException {
        if(isEmpty()){
            throw new ListException("Circular Doubly Linked List is Empty");
        }
        //Caso 1. El elemento a suprimir esta al inicio
        if(util.Utility.compare(first.data, element)==0){
            first = first.next; //saltamos el primer nodo
        }else{  //Caso 2. El elemento a suprimir puede estar al medio o final
            Node prev = first; //dejo un apuntador al nodo anterior
            Node aux = first.next;
            while(aux!=last && !(util.Utility.compare(aux.data, element)==0)){
                prev = aux;
                aux = aux.next;
            }
            //se sale cuando aux==last o cuando encuentra el elemento
            if(util.Utility.compare(aux.data, element)==0){
                //ya lo encontro, procedo a desenlazar el nodo
                prev.next = aux.next;
                //mantengo el doble enlace
                aux.next.prev = prev;
            }
            //q pasa si el elemento a suprimir esta en el ult nodo
            //es decir donde está last
            if(aux==last&&util.Utility.compare(aux.data, element)==0){
                last = prev; //desenlaza el ult nodo
            }
        }
        //mantengo el enlace circular y doble
        last.next = first;
        first.prev = last;

        //Otro caso:
        //q pasa si solo queda un nodo y es el que quiero eliminar
        if(first==last && util.Utility.compare(first.data, element)==0){
            clear(); //anulo la lista
        }
    }

    @Override
    public Object removeFirst() throws ListException {
        return null;
    }

    @Override
    public Object removeLast() throws ListException {
        return null;
    }

    @Override
    public void sort() throws ListException {
        if(isEmpty()){
            throw new ListException("Circular Doubly Linked List is Empty");
        }
        for (int i = 1; i <= size() ; i++) {
            for (int j = i+1; j <= size() ; j++) {
                if(util.Utility.compare(getNode(j).data, getNode(i).data)<0){
                    Object aux = getNode(i).data;
                    getNode(i).data = getNode(j).data;
                    getNode(j).data = aux;
                }
            }
        }
    }

    @Override
    public int indexOf(Object element) throws ListException {
        if(isEmpty()){
            throw new ListException("Circular Doubly Linked List is Empty");
        }
        Node aux = first;
        int index=1; //la lista inicia en 1
        while(aux!=last){
            if(util.Utility.compare(aux.data, element)==0){
                return index;
            }
            index++; //incremento el indice
            aux=aux.next; //muevo aux al sgte nodo
        }
        //se sale cuando alcanza last
        if(util.Utility.compare(aux.data, element)==0){
            return index;
        }
        return -1; //indica q el elemento no existe
    }

    @Override
    public Object getFirst() throws ListException {
        if(isEmpty()){
            throw new ListException("Circular Doubly Linked List is Empty");
        }
        return first.data;
    }

    @Override
    public Object getLast() throws ListException {
        if(isEmpty()){
            throw new ListException("Circular Doubly Linked List is Empty");
        }
        return last.data;
    }

    @Override
    public Object getPrev(Object element) throws ListException {
        if(isEmpty()){
            throw new ListException("Circular Doubly Linked List is Empty");
        }
        if(util.Utility.compare(first.data, element)==0){
            return "It's the first, it has no previous";
        }
        Node aux = first;
        //mientras no llegue al ult nodo
        while(aux.next!=last){
            if(util.Utility.compare(aux.next.data, element)==0){
                return aux.data; //retornamos la data del nodo actual
            }
            aux=aux.next;
        }
        //se sale cuando aux.next == last
        if(util.Utility.compare(aux.next.data, element)==0){
            return aux.data;
        }
        return "Does not exist in Single Linked List";
    }

    @Override
    public Object getNext(Object element) throws ListException {
        return null;
    }

    @Override
    public Node getNode(int index) throws ListException {
        if(isEmpty()){
            throw new ListException("Circular Doubly Linked List is Empty");
        }
        Node aux = first;
        int i = 1; // pos del primer nodo
        while(aux!=last){
            if(util.Utility.compare(i, index)==0) {  //ya encontro el indice
                return aux;
            }
            i++; //incremento la var local
            aux = aux.next; //muevo aux al sgte nodo
        }
        //se sale cuando aux==last
        if(util.Utility.compare(i, index)==0) {  //ya encontro el indice
            return aux;
        }
        return null; //si llega aqui es xq no encontro el index
    }

    @Override
    public String toString() {
        String result = "Circular Doubly Linked List Content\n\n";
        Node aux = first;
        while(aux!=last){
            result+= aux.data;
            aux = aux.next;
        }
        return result+ "\n"+ aux.data; //agrego la data del ult nodo
    }
}
