package ewision.sahan.model;

import javax.swing.ImageIcon;

public class ImageNText {

    private ImageIcon icon;
    private String text;

    public ImageNText(ImageIcon icon, String text) {
        this.icon = icon;
        this.text = text;
    }    

    public ImageIcon getIcon() {
        return icon;
    }

    public String getText() {
        return text;
    }
    
}
