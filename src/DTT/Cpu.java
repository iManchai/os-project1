package DTT;


/// el cpu debe ser un thread para manaejar semaforos para el manejo de la lista de listos ????

public class Cpu {

    private int id;
    private ListaProcesos listaProcesos;
    private ListaBloqueados listaBloqueados;

    public Cpu(int id, ListaProcesos listaProcesos, ListaBloqueados listaBloqueados) {
        this.id = id;
        this.listaProcesos = listaProcesos;
        this.listaBloqueados = listaBloqueados;
    }


public void StartCPU() {s
    while (true) {
        if (!listaProcesos.isEmpty()) {
            Process proceso = listaProcesos.obtenerSiguienteProceso();
            listaProcesos.removerProceso(proceso);
            
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
