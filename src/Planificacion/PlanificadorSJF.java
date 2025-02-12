package Planificacion;

import Classes.Process;
import DataStructures.ListaSimple;
import DataStructures.Nodo;

public class PlanificadorSJF implements Planificador {

    @Override
    public Process seleccionarProceso(ListaSimple lista) {
        if (lista.isEmpty()) {
            return null;
        }

        Process procesoMasCorto = null;
        Nodo nodoActual = lista.getpFirst();

        while (nodoActual != null) {
            
            Process procesoActual = (Process) nodoActual.getInfo();
            
            

            if (procesoActual.getStatus() == Process.ProcessStatus.READY) {
                if (procesoMasCorto == null || procesoActual.getDuracion() < procesoMasCorto.getDuracion()) {
                    procesoMasCorto = procesoActual;
                }
            }
            nodoActual = nodoActual.getpNext();
        }

        if (procesoMasCorto != null) {
            lista.RemoveProcess(procesoMasCorto); 
        }
        procesoMasCorto.setTiempoEjecucionRR(1);
        return procesoMasCorto;
    }
}
