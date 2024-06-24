package ewision.sahan.utils;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public class ImageFileFilter extends FileFilter {

    @Override
    public boolean accept(File f) {
        String name = f.getName().toLowerCase();
        return f.isDirectory() || name.endsWith(".jpg") || name.endsWith(".png");
    }

    @Override
    public String getDescription() {
        return "Image";
    }

}
