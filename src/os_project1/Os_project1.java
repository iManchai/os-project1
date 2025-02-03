
import DTT.Cpu;
import DTT.ListaBloqueados;
import DTT.ListaProcesos;
import DTT.Process;
import java.util.concurrent.Semaphore;

public class Os_project1 {

    public static void main(String[] args) {
        System.out.println("Iniciamos la simulación...");

        // Crear las listas de procesos listos y bloqueados
        ListaProcesos listaProcesos = new ListaProcesos();
        ListaBloqueados listaBloqueados = new ListaBloqueados();

        // Crear semáforo para controlar el acceso al CPU
        Semaphore cpuSemaphore = new Semaphore(1);
        
                // Crear las CPUs y asignarles las listas correspondientes
        Cpu cpu1 = new Cpu(1, listaProcesos, listaBloqueados);
        Cpu cpu2 = new Cpu(2, listaProcesos, listaBloqueados);

        
        
        /// la simulacion asignara el cpu a cada proceso 

        // Crear algunos procesos de ejemplo
        Process p1 = new Process(1, "Proceso 1", 10, true, false, 2, listaProcesos, listaBloqueados, cpuSemaphore,cpu1);
        Process p2 = new Process(2, "Proceso 2", 15, false, true, 4, listaProcesos, listaBloqueados, cpuSemaphore,cpu1);
        Process p3 = new Process(3, "Proceso 3", 20, true, false, 2, listaProcesos, listaBloqueados, cpuSemaphore,cpu1);
        Process p4 = new Process(4, "Proceso 4", 8, false, true, 7, listaProcesos, listaBloqueados, cpuSemaphore,cpu1);

        // Agregar los procesos a la lista de listos
        listaProcesos.agregarProceso(p1);
        listaProcesos.agregarProceso(p2);
        listaProcesos.agregarProceso(p3);
        listaProcesos.agregarProceso(p4);


        // Iniciar las CPUs (manejan la ejecución de los procesos)
        cpu1.StartCPU();

        // Esperar que todos los procesos terminen
        try {
            p1.join();
            p2.join();
            p3.join();
            p4.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
        }

        // Mostrar el estado final de todos los procesos
        System.out.println("\nEstado final de los procesos:");
        System.out.println("Proceso 1: " + p1.getStatus());
        System.out.println("Proceso 2: " + p2.getStatus());
        System.out.println("Proceso 3: " + p3.getStatus());
        System.out.println("Proceso 4: " + p4.getStatus());

    }
}
