
package os_project1;
import Interfaz.InterfazInicial;


public class Os_project1 {

    public static void main(String[] args) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new InterfazInicial().setVisible(true);
            }
        });
    }

}
