package Planificacion;

import Classes.Process; // Importa la clase Process
import DataStructures.ListaSimple; // Importa la clase ListaProcesos

public class PlanificadorFCFS implements Planificador {

    @Override
    public Process seleccionarProceso(ListaSimple lista) {
        return lista.nextProcess();
    }
}