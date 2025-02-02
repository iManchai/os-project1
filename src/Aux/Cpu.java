package Aux;

import java.util.concurrent.Semaphore;

public class Cpu extends Thread {

    private int id;
    private ListaProcesos listaProcesos;
    private ListaBloqueados listaBloqueados;
    private Semaphore cpuSemaphore;

    public Cpu(int id, ListaProcesos listaProcesos, ListaBloqueados listaBloqueados) {
        this.id = id;
        this.listaProcesos = listaProcesos;
        this.listaBloqueados = listaBloqueados;
        this.cpuSemaphore = cpuSemaphore;
    }

@Override
public void run() {
    while (true) {
        if (!listaProcesos.isEmpty()) {
            Process proceso = listaProcesos.obtenerSiguienteProceso();
            
            if (proceso != null && proceso.getStatus() == Process.ProcessStatus.READY) {
                proceso.setStatus(Process.ProcessStatus.RUNNING);
                System.out.println("CPU " + id + " ejecutando: " + proceso.getNameProcess());
                
                // Ejecutar una instrucción del proceso
                proceso.run();
                
            }
        }else{
            break;
           
        }
    }
}

    public int getIdCpu() {
        return id;
    }

    public void setIdCpu(int id) {
        this.id = id;
    }
}
