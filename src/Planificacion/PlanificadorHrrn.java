package Planificacion;

import Classes.Process;
import DataStructures.ListaSimple;
import DataStructures.Nodo;

public class PlanificadorHrrn implements Planificador {

    @Override
    public Process seleccionarProceso(ListaSimple lista) {
        if (lista.isEmpty()) {
            return null;
        }

        Process procesoSeleccionado = null;
        double maxResponseRatio = -1;

        Nodo nodoActual = lista.getpFirst();

        while (nodoActual != null) {
            Process procesoActual = (Process) nodoActual.getInfo();

            // Solo considera procesos en estado READY
            if (procesoActual.getStatus() == Process.ProcessStatus.READY) {
                // Calcula el response ratio
                double responseRatio = calcularResponseRatio(procesoActual);

                // Selecciona el proceso con el mayor response ratio
                if (responseRatio > maxResponseRatio) {
                    maxResponseRatio = responseRatio;
                    procesoSeleccionado = procesoActual;
                }
            }

            nodoActual = nodoActual.getpNext();
        }

        // Si se seleccionó un proceso, actualiza su estado y tiempo de espera
        if (procesoSeleccionado != null) {
            procesoSeleccionado.setStatus(Process.ProcessStatus.RUNNING);
            lista.RemoveProcess(procesoSeleccionado); 
            procesoSeleccionado.setTiempoEspera(0);
        }
        
        procesoSeleccionado.setTiempoEjecucionRR(1);
        return procesoSeleccionado;
    }

    
    private double calcularResponseRatio(Process proceso) {
        int tiempoEspera = proceso.getTiempoEspera();
        int duracion = proceso.getDuracion();

        if (duracion == 0) {
            return 0;
        }

        return (double) (tiempoEspera + duracion) / duracion;
    }
    
}