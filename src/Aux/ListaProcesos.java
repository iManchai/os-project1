package Aux;

public class ListaProcesos {

    private ListaSimple listaProcesos;

    // Constructor que inicializa la lista de procesos
    public ListaProcesos() {
        this.listaProcesos = new ListaSimple();
    }

    // Obtener el siguiente proceso en estado READY
    public Process obtenerSiguienteProceso() {
        Nodo nodo = listaProcesos.getpFirst(); // Obtener el primer nodo
        while (nodo != null) {
            Process proceso = (Process) nodo.getInfo();
            if (proceso.getStatus() == Process.ProcessStatus.READY) {
                listaProcesos.RemoveElement(proceso); // Removerlo de la lista de READY
                return proceso;
            }
            nodo = nodo.getpNext(); // Continuar con el siguiente nodo
        }
        return null; // Si no hay procesos READY, devolver null
    }

    // Obtener el primer proceso (sin eliminarlo)
    public Process getFirstProcess() {
        Nodo nodo = listaProcesos.getpFirst();
        if (nodo != null) {
            return (Process) nodo.getInfo(); // Retorna el primer proceso en la lista
        }
        return null; // Si la lista está vacía, retorna null
    }

    // Verificar si la lista de procesos está vacía
    public boolean isEmpty() {
        return listaProcesos.EsVacia();
    }

    // Agregar un proceso al final de la lista
    public void agregarProceso(Process proceso) {
        listaProcesos.InsertAtTheEnd(proceso);
    }

    // Remover un proceso de la lista
    public void removerProceso(Process proceso) {
        listaProcesos.RemoveElement(proceso);
    }

    // Obtener la lista completa de procesos (en forma de ListaSimple)
    public ListaSimple getListaProcesos() {
        return listaProcesos;
    }
}