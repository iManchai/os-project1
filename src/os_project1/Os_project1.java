import Classes.Cpu;
import Classes.Process;
import Interfaz.InterfazInicial;
import java.util.concurrent.Semaphore;
import DataStructures.ListaSimple;


public class Os_project1 {

      public static void main(String[] args) {
        ListaSimple listaListos = new ListaSimple();
        ListaSimple listaBloqueados = new ListaSimple();
        Semaphore semaphore = new Semaphore(1); // Semáforo compartido

        Cpu cpu1 = new Cpu(1, listaListos, semaphore);


        Process p1 = new Process(1, "Proceso 1", 10, true, false, 2, listaListos, listaBloqueados, semaphore, cpu1);
        Process p2 = new Process(2, "Proceso 2", 15, false, true, 4, listaListos, listaBloqueados, semaphore, cpu1);
        Process p3 = new Process(3, "Proceso 3", 20, true, false, 2, listaListos, listaBloqueados, semaphore, cpu1);
        Process p4 = new Process(4, "Proceso 4", 8, false, true, 7, listaListos, listaBloqueados, semaphore, cpu1);

        listaListos.addProcess(p1);
        listaListos.addProcess(p2);
        listaListos.addProcess(p3);
        listaListos.addProcess(p4);

        cpu1.start();
        
        new InterfazInicial().setVisible(true);


        try {
            p1.join();
            p2.join();
            p3.join();
            p4.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
      
      
}