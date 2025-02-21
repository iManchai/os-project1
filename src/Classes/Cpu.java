package Classes;

import Planificacion.Planificador;
import DataStructures.ListaSimple;
import DataStructures.Nodo;
import Planificacion.PlanificadorFCFS;
import java.util.concurrent.Semaphore;
import Interfaz.InterfazInicial;
import com.sun.source.tree.BreakTree;

public class Cpu extends Thread {  // Extiende Thread para manejar concurrencia

    private int id;
    private ListaSimple listaProcesos;
    private Semaphore listaSemaphore;  // Semáforo para sincronizar el acceso a la lista
    public Planificador planificador = new PlanificadorFCFS(); // Configuracion default al iniciar la simulacion
    private Semaphore semaphoreCpu;
    private int velocidadReloj;
    private InterfazInicial interfaz;
    private boolean isRunning;

    public Cpu(int id, ListaSimple listaProcesos, Semaphore listaSemaphore, Semaphore semaphoreCpu, int velocidadReloj, InterfazInicial interfaz) {
        this.id = id;
        this.listaProcesos = listaProcesos;
        this.listaSemaphore = listaSemaphore;
        this.semaphoreCpu = semaphoreCpu;
        this.interfaz = interfaz;
        this.isRunning = isRunning;

    }

    @Override
    public void run() {

        while (true) { // Bucle principal
            if (isRunning) {
                try {
                    listaSemaphore.acquire();

                    int utilizacionSistema = interfaz.getUtilizacionSistema();


                    interfaz.actualizarInterfazCPU(id, "0", "Running", "0", "0", "system32", "none");

                    if (!listaProcesos.isEmpty()) {
                        Process proceso = planificador.seleccionarProceso(listaProcesos);

                        listaSemaphore.release();

                        proceso.setCpuSemaphore(semaphoreCpu);
                        proceso.setCpuName("CPU" + id);
                        proceso.setCpu(id);
                        proceso.setStatus(Process.ProcessStatus.RUNNING);

                        proceso.run();
                        proceso.join(); // Esperar a que el proceso termine
                    }

                    if (listaProcesos.isEmpty()) {
                        listaSemaphore.release();

                        Thread.sleep(velocidadReloj);
                        continue;
                    }
                } catch (InterruptedException e) {
                    // Captura la excepción y sale del bucle
                    System.out.println("CPU " + id + " fue interrumpido. Finalizando ejecución...");
                    e.printStackTrace();

                }
            } else {
                 try {
                Thread.sleep(100); // Espera pasiva (100 ms)
            } catch (InterruptedException e) {
                System.out.println("CPU " + id + " fue interrumpido mientras esperaba. Finalizando.");
                e.printStackTrace();
                break;
            }
            }
        }
    }

    public boolean isIsRunning() {
        return isRunning;
    }

    public void setIsRunning(boolean isRunning) {
        this.isRunning = isRunning;
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
