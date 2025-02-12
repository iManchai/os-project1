/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Planificacion;
import Classes.Cpu;
import Classes.Process; // Importa la clase Process
import DataStructures.ListaSimple; // Importa la clase ListaProcesos

/**
 *
 * @author jesusventura
 */
public class PlanificadorRR implements Planificador{
     
        
    @Override
    public Process seleccionarProceso(ListaSimple lista) {
        Process process =  lista.nextProcess();
        process.setTiempoEjecucionRR(5);
        return process;
    };
        
        

  
    }
    
    
    
  
