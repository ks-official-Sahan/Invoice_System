package ewision.sahan.utils;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public class ExcelFileFilter extends FileFilter {

    @Override
    public boolean accept(File f) {
        String name = f.getName().toLowerCase();
        return f.isDirectory() || name.endsWith(".csv") || name.endsWith(".elsx") || name.endsWith(".els");
    }

    @Override
    public String getDescription() {
        return "Excel";
    }

}
