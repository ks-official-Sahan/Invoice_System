package ewision.sahan.table.spinner;

import javax.swing.JSpinner;

/**
 *
 * @author ksoff
 */
public interface SpinnerChangeEvent {

    public void run(int row, JSpinner spinner);

    //public void run(int row);

    //public void run();
}
