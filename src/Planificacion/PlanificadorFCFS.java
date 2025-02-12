package Planificacion;

import Classes.Process; // Importa la clase Process
import DataStructures.ListaSimple; // Importa la clase ListaProcesos

public class PlanificadorFCFS implements Planificador {

    @Override
    public Process seleccionarProceso(ListaSimple lista) {
        Process proceso = lista.nextProcess();
        proceso.setTiempoEjecucionRR(1);
        return proceso;
    }
}