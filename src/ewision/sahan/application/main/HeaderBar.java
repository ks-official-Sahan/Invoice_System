package ewision.sahan.application.main;

import com.formdev.flatlaf.FlatClientProperties;
import ewision.sahan.application.Application;
import ewision.sahan.model.Constants;
import ewision.sahan.utils.ImageScaler;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.border.EmptyBorder;
import raven.toast.Notifications;

/**
 *
 * @author ks.official.sahan
 */
public class HeaderBar extends JPanel {

    private JLabel sessionLabel;
    private Timer timer;
    private int sessionTime = 2 * 60 * 60 + 10; // session time in seconds (e.g., 1 hour = 60*60)
    private JLabel logoLabel;

    public void setLogoLabel(String text, Icon icon) {
        logoLabel.setText(text);
        logoLabel.setIcon(icon);
    }

    public HeaderBar() {
        setBorder(new EmptyBorder(0, 150, 0, 150));

        setLayout(new MigLayout("insets 0, fillx, aligny center",
                "[fill]10[center]10[fill]",
                "[]"));

        // Set the header bar style
        putClientProperty(FlatClientProperties.STYLE, ""
                + "background:$Header.background;"
                + "arc:20;");

        //ImageIcon icon = new ImageIcon(getClass().getResource("/ewision/sahan/icon/png/logo.png"));
        //Image image = (Image) icon.getImage().getScaledInstance(80, 60, Image.SCALE_SMOOTH);
        //icon = new ImageIcon(image);
        //ImageIcon icon = new ImageScaler().getScaledIcon(Constants.GRADIENT_ICON, 30, 35);

        // Logo and text on the left
        logoLabel = new JLabel();
        //logoLabel.setIcon(icon);
        logoLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(logoLabel, "left");

        // Session countdown timer in the center
        sessionLabel = new JLabel();
        sessionLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        add(sessionLabel, "center");

        // User profile icon on the right
        JButton userProfileIcon = new JButton();
        //userProfileIcon.setIcon(icon);
        userProfileIcon.setHorizontalAlignment(SwingConstants.TRAILING);
        userProfileIcon.addActionListener(((e) -> {
            Notifications.getInstance().show(Notifications.Type.INFO, Notifications.Location.TOP_RIGHT, "Hello sample message");
        }));
        userProfileIcon.setBorder(new EmptyBorder(0, 0, 0, 0));
        userProfileIcon.setContentAreaFilled(false);
        userProfileIcon.setMaximumSize(new Dimension(80, 40));
        add(userProfileIcon, "right");

        // Initialize the session timer
        startSessionTimer();
    }

    private void startSessionTimer() {
        updateSessionLabel();
        timer = new Timer(1000, (ActionEvent e) -> {
            if (sessionTime > 0) {
                sessionTime--;
                updateSessionLabel();
            } else {
                timer.stop();
                // Handle session timeout here if needed
                Application.logout();
            }
        });
        timer.start();
    }

    private void updateSessionLabel() {
        int hours = sessionTime / 60 / 60;
        int minutes = (sessionTime - hours * 60 * 60) / 60;
        int seconds = sessionTime % 60;
        sessionLabel.setText(String.format("Session Time: %02d:%02d:%02d", hours, minutes, seconds));
    }

//    public static void main(String[] args) {
//        JFrame frame = new JFrame("Application Header Bar Example");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(800, 600);
//
//        HeaderBar headerBar = new HeaderBar();
//        frame.add(headerBar, BorderLayout.NORTH);
//
//        frame.setVisible(true);
//    }
}
