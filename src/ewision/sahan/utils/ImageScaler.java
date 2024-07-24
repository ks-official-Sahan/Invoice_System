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

    ImageIcon icon;

    public ImageIcon getScaledIcon(String path, int width, int height, boolean isSmooth, boolean isAbsolutePath) {
        //Thread t = new Thread(() -> {
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
            icon = new ImageIcon(image);
        //});
        //t.start();
        return icon;
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

    FlatSVGIcon icon2;
    String image2;

    public FlatSVGIcon getSvgIcon(String image) {
        this.image2 = image;
        //Thread t = new Thread(() -> {
        Color lightColor = FlatUIUtils.getUIColor("Menu.icon.lightColor", Color.red);
        Color darkColor = FlatUIUtils.getUIColor("Menu.icon.darkColor", Color.red);

        if (!image2.toLowerCase().contains(".svg")) {
            image2 += ".svg";
        }

        if (!image2.toLowerCase().contains("/svg/")) {
            image2 = "/ewision/sahan/icon/svg/" + image2;
        } else if (!image2.toLowerCase().contains("/icon/")) {
            image2 = "/ewision/sahan/icon/" + image2;
        } else if (!image2.toLowerCase().contains("/ewision/sahan/")) {
            image2 = "/ewision/sahan/" + image2;
        }

        FlatSVGIcon svgIcon;
        try {
            svgIcon = new FlatSVGIcon(getClass().getResource(image2));
        } catch (NullPointerException e) {
            svgIcon = new FlatSVGIcon(getClass().getResource(defaultSvgPath));
            CommonLogger.logger.log(Level.SEVERE, "SVG Path: {0}is Invalid. {1} \n Location: {2}", new Object[]{image2, e.getMessage(), getClass().getName()});
        }

        FlatSVGIcon.ColorFilter filter = new FlatSVGIcon.ColorFilter();
        filter.add(Color.decode("#ff8f8f"), lightColor, Color.GREEN);
        svgIcon.setColorFilter(filter);

        icon2 = svgIcon;
        //});
        //t.start();
        return icon2;
    }

}
