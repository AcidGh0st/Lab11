package domain;

import domain.list.SinglyLinkedList;

public class Vertex {
    public Object data; // El tipo de data puede ser String, Integer, u otro según tu aplicación
    public boolean visited;
    public SinglyLinkedList edgesList;

    public Vertex(Object data) {
        this.data = data;
        this.visited = false;
        this.edgesList = new SinglyLinkedList();
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    @Override
    public String toString() {
        return data.toString(); // Asegúrate de convertir data a String de manera segura
    }
}
