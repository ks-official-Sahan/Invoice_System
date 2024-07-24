package ewision.sahan.application.main;

import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author ks.official.sahan
 */
public class PanelLogin extends JPanel {

    public PanelLogin() {
        setLayout(new MigLayout("fillx, wrap, insets 60 60 85 60, width 350", "[fill]", "[]20[][]15[][]30[]"));
        putClientProperty(FlatClientProperties.STYLE, ""
                + "background:$Login.background;"
                + "arc:20;");
    }

}
