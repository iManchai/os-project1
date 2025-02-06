/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataStructures;

import Classes.Process;

public class ListaBloqueados {

    private ListaSimple listaBloqueados;

    public Process obtenerSiguienteProceso() {
        Nodo nodo = listaBloqueados.getpFirst(); // Obtener el primer nodo
        while (nodo != null) {
            Process proceso = (Process) nodo.getInfo();
            if (proceso.getStatus() == Process.ProcessStatus.BLOCKED) {
                listaBloqueados.RemoveElement(proceso); // Removerlo de la lista de bloqueados
                return proceso;
            }
            nodo = nodo.getpNext(); // Continuar con el siguiente nodo
        }
        return null; // Si no hay procesos READY, devolver null
    }

    public boolean isEmpty() {
        return listaBloqueados.EsVacia();
    }

    public ListaBloqueados() {
        this.listaBloqueados = new ListaSimple();
    }

    public void agregarProceso(Process proceso) {
        listaBloqueados.InsertAtTheEnd(proceso);
    }

    public void removerProceso(Process proceso) {
        listaBloqueados.RemoveElement(proceso);
    }

    public ListaSimple getListaBloqueados() {
        return listaBloqueados;
    }

    // Método para imprimir la lista
    public void imprimirLista() {
        if (listaBloqueados.EsVacia()) {
            System.out.println("La lista de procesos bloqueados está vacía.");
            return;
        }
        
        Nodo actual = listaBloqueados.getpFirst();
        while (actual != null) {
            Process proceso = (Process) actual.getInfo();
            System.out.print(proceso.getNameProcess()+ " -> "); // Imprime el nombre del proceso
            actual = actual.getpNext();
        }
        System.out.println("null");
    }

}
