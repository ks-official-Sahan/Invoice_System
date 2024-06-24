package ewision.sahan.utils;

/**
 *
 * @author ksoff
 */
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.ui.FlatUIUtils;
import ewision.sahan.loggers.CommonLogger;
import ewision.sahan.model.Constants;
import java.awt.Color;
import java.awt.Image;
import java.util.logging.Level;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ImageScaler {

    /* Image Icons */
    private String defaultImagePath = Constants.GRADIENT_ICON;
    private String defaultSvgPath = "/ewision/sahan/icon/svg/service1.svg";

    public ImageIcon getScaledIcon(String path, int width) {
        return getScaledIcon(path, width, width, true, false);
    }

    public ImageIcon getScaledIcon(String path, int width, int height) {
        return getScaledIcon(path, width, height, true, false);
    }

    public ImageIcon getScaledIcon(String path, int width, int height, boolean isSmooth, boolean isAbsolutePath) {
        ImageIcon imageIcon = new ImageIcon(getClass().getResource(defaultImagePath));
        try {
            if (isAbsolutePath) {
                imageIcon = new ImageIcon(path);
            } else {
                imageIcon = new ImageIcon(getClass().getResource(path));
            }
        } catch (NullPointerException e) {
            JOptionPane.showMessageDialog(new JFrame(), "Image path: " + path + " is incorrect : \n " + e.getMessage(), "Invalid Path", JOptionPane.WARNING_MESSAGE);
            CommonLogger.logger.log(Level.SEVERE, "Path: " + path + " is Invalid. \n" + getClass().getName() + "\n" + e.getMessage(), path);
        }
        Image image = imageIcon.getImage().getScaledInstance(width, height, isSmooth ? Image.SCALE_SMOOTH : Image.SCALE_AREA_AVERAGING);
        return new ImageIcon(image);
    }

    /* SVG Icons */
    public FlatSVGIcon getSvgIcon(String image, int width) {
        return getSvgIcon(image, width, width);
    }

    public FlatSVGIcon getSvgIcon(String image, int width, int height) {
        FlatSVGIcon svgIcon = getSvgIcon(image);
        svgIcon = svgIcon.derive(width, height);
        return svgIcon;
    }

    public FlatSVGIcon getSvgIcon(String image) {
        Color lightColor = FlatUIUtils.getUIColor("Menu.icon.lightColor", Color.red);
        Color darkColor = FlatUIUtils.getUIColor("Menu.icon.darkColor", Color.red);

        if (!image.toLowerCase().contains(".svg")) {
            image += ".svg";
        }

        if (!image.toLowerCase().contains("/svg/")) {
            image = "/ewision/sahan/icon/svg/" + image;
        } else if (!image.toLowerCase().contains("/icon/")) {
            image = "/ewision/sahan/icon/" + image;
        } else if (!image.toLowerCase().contains("/ewision/sahan/")) {
            image = "/ewision/sahan/" + image;
        }

        FlatSVGIcon svgIcon;
        try {
            svgIcon = new FlatSVGIcon(getClass().getResource(image));
        } catch (NullPointerException e) {
            svgIcon = new FlatSVGIcon(getClass().getResource(defaultSvgPath));
            CommonLogger.logger.log(Level.SEVERE, "SVG Path: {0}is Invalid. {1} \n Location: {2}", new Object[]{image, e.getMessage(), getClass().getName()});
        }

        FlatSVGIcon.ColorFilter filter = new FlatSVGIcon.ColorFilter();
        filter.add(Color.decode("#ff8f8f"), lightColor, Color.GREEN);
        svgIcon.setColorFilter(filter);

        return svgIcon;
    }

}
