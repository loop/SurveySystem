import database.DatabaseInitialiser;
import gui.main.MainController;
import helpers.AppFolderHelper;
import gui.login.LoginController;
import helpers.PasswordEncryptor;
import connectivity.DiscoveryThread;
import connectivity.ServerConnections;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is the main class that executes the server. It starts the login scene and upon being successful opens the
 * sockets.
 */

public class Main extends Application {

    private static final Logger log = Logger.getLogger(Main.class.getName());
    private ServerConnections serverConnections;
    private Thread serverThread;

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Starts the application by loading the login scene and then the main scene and starts server upon
     * successful login.
     *
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println(PasswordEncryptor.generateSHA256("fyp"));
        AppFolderHelper.createAppFolder();
        DatabaseInitialiser.initialise();
        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("gui/login/login.fxml"));
        Pane loginPane = loginLoader.load();
        Stage loginStage = new Stage();
        loginStage.setScene(new Scene(loginPane));
        LoginController loginController = loginLoader.getController();
        loginController.loggedInProperty().addListener((obs, wasLoggedIn, isNowLoggedIn) -> {
            if (isNowLoggedIn) {
                Scene mainScene = loadMainScene();
                primaryStage.setScene(mainScene);
                startServer(4000, "Server");
                primaryStage.centerOnScreen();
                primaryStage.setResizable(false);
                primaryStage.setTitle("Desktop Server");
                primaryStage.getIcons().add(new Image("/gui/images/app_icon.png"));
                primaryStage.show();
                MainController.currentUserLoggedIn = loginController.getLoggedInUsername();
                loginStage.hide();
                log.log(Level.INFO, "Current user logged in is: " + MainController.currentUserLoggedIn);
            }
        });
        loginStage.setTitle("Survey Server Login");
        loginStage.setResizable(false);
        loginStage.centerOnScreen();
        loginStage.getIcons().add(new Image("/gui/images/app_icon.png"));
        loginStage.show();
    }

    /**
     * Loads the main scene FXML.
     *
     * @return scene loader
     */
    private Scene loadMainScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/main/main.fxml"));
            return new Scene(loader.load());
        } catch (Exception exc) {
            throw new RuntimeException(exc);
        }
    }


    /**
     * Starts the network discovery server and the socket server.
     *
     * @param port
     * @param connectionName
     */
    private void startServer(int port, String connectionName) {
        this.serverConnections = new ServerConnections(port);
        this.serverThread = new Thread(this.serverConnections, connectionName);
        serverThread.start();
        Thread discoveryThread = new Thread(DiscoveryThread.getInstance());
        discoveryThread.start();
        if (discoveryThread.isAlive()) {
            log.log(Level.FINE, "Discovery Thread is running");
        }
    }

    /**
     * Closing the application causes the threads to be destroyed and server closes the sockets
     * and stops completely.
     *
     * @throws Exception
     */
    @Override
    public void stop() throws Exception {
        log.log(Level.INFO, "Server is closing and all threads are being terminated!");
        if (serverConnections != null) {
            serverConnections.stopListening();
            serverThread.interrupt();
            ServerConnections.closeAllConnections();
        }
        super.stop();
        System.exit(0);
    }
}