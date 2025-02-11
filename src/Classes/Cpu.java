package Classes;

import Planificacion.Planificador;
import DataStructures.ListaSimple;
import java.util.concurrent.Semaphore;

public class Cpu extends Thread {  // Extiende Thread para manejar concurrencia

    private int id;
    private ListaSimple listaProcesos;
    private Semaphore listaSemaphore;  // Semáforo para sincronizar el acceso a la lista
    public Planificador planificador;
    private Semaphore semaphoreCpu;

    public Cpu(int id, ListaSimple listaProcesos, Semaphore listaSemaphore, Planificador planificador, Semaphore semaphoreCpu) {
        this.id = id;
        this.listaProcesos = listaProcesos;
        this.listaSemaphore = listaSemaphore;
        this.planificador = planificador;
        this.semaphoreCpu = semaphoreCpu;

    }

@Override
public void run() {
    try {
        while (true) {
            listaSemaphore.acquire();

            try {
                if (!listaProcesos.isEmpty()) {
                    Process proceso = planificador.seleccionarProceso(listaProcesos);

                    // Verificar si proceso es null antes de acceder a sus métodos
                    if (proceso != null) {
                        proceso.setCpuSemaphore(semaphoreCpu);
                        proceso.setCpuName("CPU" + id);
                        proceso.setCpu(id);

                        if (proceso.getStatus() == Process.ProcessStatus.READY) {
                            proceso.setStatus(Process.ProcessStatus.RUNNING);
                            System.out.println("CPU " + id + " ejecutando: " + proceso.getNameProcess());

                            listaSemaphore.release();

                            proceso.run();

                            listaSemaphore.acquire();
                        }
                    }
                }
            } finally {
                listaSemaphore.release();
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}

    public int getIdCpu() {
        return id;
    }

    public void setIdCpu(int id) {
        this.id = id;
    }

    public Planificador getPlanificador() {
        return planificador;
    }

    public void setPlanificador(Planificador planificador) {
        this.planificador = planificador;
    }

}
