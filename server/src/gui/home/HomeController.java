package gui.home;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.InetAddress;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class controls the home page view.
 */

public class HomeController implements Initializable {
    @FXML
    private Label ipAddressLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ipAddressLabel.setText("Server Connected To Network: " + InetAddress.getLocalHost().getHostAddress());
        } catch (Exception e) {
            e.printStackTrace();
            ipAddressLabel.setText("Server not connected to network.");
        }
    }

}
