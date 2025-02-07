package DataStructures;

import Classes.Process;

public class ListaSimple {

    private Nodo pFirst;
    private Nodo pLast;
    private int size;

    public ListaSimple() {
        this.pFirst = null;
        this.pLast = null;
        this.size = 0;
    }

    public boolean isEmpty() {
        return (getpFirst() == null);
    }

    public Process nextProcess() {
        Nodo nodo = getpFirst();
        while (nodo != null) {
            Process proceso = (Process) nodo.getInfo();
            if (proceso.getStatus() == Process.ProcessStatus.READY) {
                RemoveProcess(proceso);
                return proceso;
            }
            nodo = nodo.getpNext();
        }
        return null;

    }

    // Obtener el primer proceso (sin eliminarlo)
    public Process getFirstProcess() {
        Nodo nodo = getpFirst();
        if (nodo != null) {
            return (Process) nodo.getInfo(); // Retorna el primer proceso en la lista
        }
        return null; // Si la lista está vacía, retorna null
    }

    public boolean contains(Object elemento) {
        Nodo actual = pFirst;
        while (actual != null) {
            if (actual.getInfo().equals(elemento)) {
                return true;
            }
            actual = actual.getpNext();
        }
        return false;
    }

    public void addProcess(Object x) {
        Nodo nuevo = new Nodo(x);
        if (this.isEmpty()) {
            setpFirst(pLast = nuevo);
        } else {
            Nodo aux = pLast;
            aux.setpNext(nuevo);
            pLast = nuevo;
        }
        size++;
    }

    public void RemoveProcess(Object elemento) {
        if (isEmpty()) {
            return;
        }

        if (pFirst.getInfo().equals(elemento)) {
            pFirst = pFirst.getpNext();
            size--;
            if (pFirst == null) {
                pLast = null;
            }
            return;
        }

        Nodo prev = pFirst;
        Nodo current = pFirst.getpNext();
        while (current != null) {
            if (current.getInfo().equals(elemento)) {
                prev.setpNext(current.getpNext());
                size--;
                if (current == pLast) {
                    pLast = prev;
                }
                return;
            }
            prev = current;
            current = current.getpNext();
        }
    }

    public void printlist() {
        if (isEmpty()) {
            System.out.println("La lista de procesos listos está vacía.");
            return;
        }

        Nodo actual = getpFirst();
        while (actual != null) {
            Process proceso = (Process) actual.getInfo();
            System.out.print(proceso.getNameProcess() + " -> "); // Imprime el nombre del proceso
            actual = actual.getpNext();

        }
        System.out.println("null");
    }

    public void vaciar() {
        this.pFirst = null;
        this.pLast = null;
        this.size = 0;
    }

    public Nodo getpFirst() {
        return pFirst;
    }

    public void setpFirst(Nodo pFirst) {
        this.pFirst = pFirst;
    }

    public Nodo getpLast() {
        return pLast;
    }

    public void setpLast(Nodo pLast) {
        this.pLast = pLast;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

}
