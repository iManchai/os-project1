package Classes;

import Planificacion.Planificador;
import DataStructures.ListaSimple;
import Planificacion.PlanificadorFCFS;
import java.util.concurrent.Semaphore;
import Interfaz.InterfazInicial;

public class Cpu extends Thread {  // Extiende Thread para manejar concurrencia

    private int id;
    private ListaSimple listaProcesos;
    private Semaphore listaSemaphore;  // Semáforo para sincronizar el acceso a la lista
    public Planificador planificador = new PlanificadorFCFS(); // Configuracion default al iniciar la simulacion
    private Semaphore semaphoreCpu;
    private int velocidadReloj;
    private InterfazInicial interfaz;


    public Cpu(int id, ListaSimple listaProcesos, Semaphore listaSemaphore, Semaphore semaphoreCpu, int velocidadReloj, InterfazInicial interfaz) {
        this.id = id;
        this.listaProcesos = listaProcesos;
        this.listaSemaphore = listaSemaphore;
        this.semaphoreCpu = semaphoreCpu;
        this.interfaz = interfaz;
    
    }

     @Override
    public void run() {
        try {
            while (true) {
                
                 listaSemaphore.acquire();
                 System.out.println("CPU:" + id + " adquirió el semáforo");
                
                System.out.println("Ejecutando el bucle de nuevo");
                
                
                interfaz.actualizarInterfazCPU(id, "0", "system32", "Running", "0", "none");
                if (!listaProcesos.isEmpty()) {
                    
                    
                   
                   

                    Process proceso = planificador.seleccionarProceso(listaProcesos);
                    
                     listaSemaphore.release();

                    proceso.setCpuSemaphore(semaphoreCpu);
                    proceso.setCpuName("CPU" + id);
                    proceso.setCpu(id);
                    proceso.setStatus(Process.ProcessStatus.RUNNING);

                    System.out.println("CPU " + id + " ejecutando: " + proceso.getNameProcess());
                    
                   

                    proceso.run();
                    proceso.join(); // Esperar a que el proceso termine

                    
                    

                    
                    

                }if (listaProcesos.isEmpty()) {
                    

                     System.out.println("CPU:" + id + " solto el semáforo");
                     listaSemaphore.release();
                     
                     Thread.sleep(1000);
                     
                    System.out.println("lista vacia");
                    
                    continue;
                    
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
