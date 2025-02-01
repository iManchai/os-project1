/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Aux;

public class Process extends Thread {

    private int id;
    private String name;
    private int progranCounter;
    private String status;
    private int numberInstrutions;
    private boolean cpuBound;
    private boolean ioBound;


    public void Process(int id, String name, int numberInstrutions, boolean cpuBound , boolean ioBound, String status,int progranCounter) {

        this.id = id;
        this.name = name;
        this.status = status;
        this.progranCounter = progranCounter;
        this.numberInstrutions = numberInstrutions;
        this.cpuBound = cpuBound;
        this.ioBound = ioBound;
        
    }
    
    
   

    @Override
    public void run() {

    }
    
    
    public boolean isCpuBound() {
        return cpuBound;
    }

    public void setCpuBound(boolean cpuBound) {
        this.cpuBound = cpuBound;
    }

    public boolean isIoBound() {
        return ioBound;
    }

    public void setIoBound(boolean ioBound) {
        this.ioBound = ioBound;
    }
    
    public int getNumberInstrutions() {
        return numberInstrutions;
    }

    public void setNumberInstrutions(int numberInstrutions) {
        this.numberInstrutions = numberInstrutions;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    
    /// cambiar el nombre de getName
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProgranCounter() {
        return progranCounter;
    }

    public void setProgranCounter(int progranCounter) {
        this.progranCounter = progranCounter;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getIdProcess() {
        return id;
    }

}
