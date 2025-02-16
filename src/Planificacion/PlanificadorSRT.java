package Planificacion;   //// esta mal debe expulsar a un proceso si es mas corto 

import Classes.Process;
import DataStructures.ListaSimple;
import DataStructures.Nodo;

public class PlanificadorSRT implements Planificador {

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
                if (procesoMasCorto == null || procesoActual.getDuracion() - procesoActual.getProgramCounter() < procesoMasCorto.getDuracion() - procesoMasCorto.getProgramCounter()) {
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
