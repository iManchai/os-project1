package DTT;

 

import java.util.concurrent.Semaphore;


public class Cpu extends Thread {  // Extiende Thread para manejar concurrencia
    private int id;
    private ListaListos listaProcesos;
    private Semaphore listaSemaphore;  // Semáforo para sincronizar el acceso a la lista

    public Cpu(int id, ListaListos listaProcesos, Semaphore listaSemaphore) {
        this.id = id;
        this.listaProcesos = listaProcesos;
        this.listaSemaphore = listaSemaphore;
    }

    @Override
    public void run() {
        try {
            while (true) {
                listaSemaphore.acquire();  // Adquirir el semáforo para acceder a la lista

                if (!listaProcesos.isEmpty()) {
                    // Obtener el siguiente proceso de la lista de listos
                    Process proceso = listaProcesos.obtenerSiguienteProceso();

                    if (proceso != null && proceso.getStatus() == Process.ProcessStatus.READY) {
                        proceso.setStatus(Process.ProcessStatus.RUNNING);
                        System.out.println("CPU " + id + " ejecutando: " + proceso.getNameProcess());

                        listaSemaphore.release();  // Liberar el semáforo antes de ejecutar el proceso

                        // Ejecutar el proceso
                        proceso.run();

                        // Volver a adquirir el semáforo para actualizar la lista
                        listaSemaphore.acquire();

                    } else {
                        listaSemaphore.release();  // Liberar el semáforo si no hay procesos listos
                    }
                } else {
                    listaSemaphore.release();  // Liberar el semáforo si la lista está vacía
                    break;  // Salir del bucle si no hay más procesos
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
}