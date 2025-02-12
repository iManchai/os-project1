package Classes;

import Planificacion.Planificador;
import Planificacion.PlanificadorRR;
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

                try {

                    if (!listaProcesos.isEmpty()) {
                        System.out.println("CPU:" + id + " adquirio el semaforo");
                        listaSemaphore.acquire();
                        Process proceso = planificador.seleccionarProceso(listaProcesos);

                        proceso.setCpuSemaphore(semaphoreCpu);
                        proceso.setCpuName("CPU" + id);
                        proceso.setCpu(id);

                        proceso.setStatus(Process.ProcessStatus.RUNNING);
                        System.out.println("CPU " + id + " ejecutando: " + proceso.getNameProcess());

                        listaSemaphore.release();

                        proceso.run();

                        System.out.println("CPU:" + id + " solto es semaforo");

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }}

    

    

    

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
