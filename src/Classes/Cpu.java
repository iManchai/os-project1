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

    public Cpu(int id, ListaSimple listaProcesos, Semaphore listaSemaphore , Planificador planificador, Semaphore semaphoreCpu) {
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
                listaSemaphore.acquire();  // Adquirir el semáforo para acceder a la lista

                if (!listaProcesos.isEmpty()) {
                    // Obtener el siguiente proceso de la lista de listos
                    Process proceso = planificador.seleccionarProceso(listaProcesos);
                    proceso.setCpuSemaphore(semaphoreCpu);
                    proceso.setCpuName("CPU" + id);
                    proceso.setCpu(id);

                    if (proceso != null && proceso.getStatus() == Process.ProcessStatus.READY) {
                        proceso.setStatus(Process.ProcessStatus.RUNNING);
                        System.out.println("CPU " + id + " ejecutando: " + proceso.getNameProcess());

                        listaSemaphore.release();  // Liberar el semáforo antes de ejecutar el proceso

                        // Ejecutar el proceso
                        proceso.run();

                        // Volver a adquirir el semáforo para actualizar la lista
                        listaSemaphore.acquire();

                    } else{
                        listaSemaphore.release();  // Liberar el semáforo si no hay procesos listos
                    }
                } else {
                    listaSemaphore.release();  // Liberar el semáforo si la lista está vacía
                }

                listaSemaphore.release();  // Liberar el semáforo al final del ciclo
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
