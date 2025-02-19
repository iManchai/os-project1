/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Interfaz;

import javax.swing.JLabel;

/**
 *
 * @author jesusventura
 */
public class CpuLabels {
    
    public JLabel idLabel;
    public JLabel nameLabel;
    public JLabel estateLabel;
    public JLabel pcLabel;
    public JLabel marLabel;
    public JLabel longitud;
    

    public CpuLabels(JLabel id, JLabel name, JLabel estate, JLabel pc, JLabel mar, JLabel longitud) {
        this.idLabel = id;
        this.nameLabel = name;
        this.estateLabel = estate;
        this.pcLabel = pc;
        this.marLabel = mar;
        this.longitud = longitud;
    }
    
}
