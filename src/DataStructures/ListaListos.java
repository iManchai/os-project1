package DataStructures;

import Classes.Process;

public class ListaListos {

    private ListaSimple listaListos; // Cambio de nombre a listaListos

    // Constructor que inicializa la lista de procesos listos
    public ListaListos() {
        this.listaListos = new ListaSimple(); // Cambio de nombre a listaListos
    }

    // Obtener el siguiente proceso en estado READY
    public Process obtenerSiguienteProceso() {
        Nodo nodo = listaListos.getpFirst(); // Obtener el primer nodo
        while (nodo != null) {
            Process proceso = (Process) nodo.getInfo();
            if (proceso.getStatus() == Process.ProcessStatus.READY) {
                listaListos.RemoveElement(proceso); // Removerlo de la lista de listos
                return proceso;
            }
            nodo = nodo.getpNext(); // Continuar con el siguiente nodo
        }
        return null; // Si no hay procesos READY, devolver null
    }

    // Obtener el primer proceso (sin eliminarlo)
    public Process getFirstProcess() {
        Nodo nodo = listaListos.getpFirst();
        if (nodo != null) {
            return (Process) nodo.getInfo(); // Retorna el primer proceso en la lista
        }
        return null; // Si la lista está vacía, retorna null
    }

    // Verificar si la lista de procesos listos está vacía
    public boolean isEmpty() {
        return listaListos.EsVacia();
    }

    // Agregar un proceso al final de la lista de listos
    public void agregarProceso(Process proceso) {
        listaListos.InsertAtTheEnd(proceso);
    }

    // Remover un proceso de la lista de listos
    public void removerProceso(Process proceso) {
        listaListos.RemoveElement(proceso);
    }

    // Obtener la lista completa de procesos listos (en forma de ListaSimple)
    public ListaSimple getListaListos() { // Cambio de nombre a getListaListos
        return listaListos;
    }

    // Método para imprimir la lista de procesos listos
    public void imprimirLista() {
        if (listaListos.EsVacia()) {
            System.out.println("La lista de procesos listos está vacía.");
            return;
        }

        Nodo actual = listaListos.getpFirst();
        while (actual != null) {
            Process proceso = (Process) actual.getInfo();
            System.out.print(proceso.getNameProcess() + " -> "); // Imprime el nombre del proceso
            actual = actual.getpNext();
        }
        System.out.println("null");
    }
}