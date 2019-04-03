package mouseclicker;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
        //Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));

        Platform.setImplicitExit(false);

        primaryStage.setTitle("Mouse Clicker");
        primaryStage.setScene(new Scene(loader.load(), 255, 80));
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("MouseClicker.png")));
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> {
            JOptionPane.showMessageDialog(null, "Fenster wird nur minimiert, nicht geschlossen!", "Info", JOptionPane.WARNING_MESSAGE);
            primaryStage.hide();
        });

        setupTray(primaryStage);
    }

    private void setupTray(Stage primaryStage) {

        //Check the SystemTray is supported
        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            return;
        }
        final PopupMenu popup = new PopupMenu();

        URL url_gruen = Main.class.getResource("MouseClicker_gruen.png");
        URL url_rot = Main.class.getResource("MouseClicker_rot.png");

        java.awt.Image image_gruen = Toolkit.getDefaultToolkit().getImage(url_gruen);
        java.awt.Image image_rot = Toolkit.getDefaultToolkit().getImage(url_rot);

        TrayIcon trayIcon = new TrayIcon(image_rot);

        final SystemTray tray = SystemTray.getSystemTray();

        // Create a pop-up menu components
        MenuItem showItem = new MenuItem("Zeige Fenster");
        MenuItem startstopItem = new MenuItem("Aktivieren");
        MenuItem exitItem = new MenuItem("Schließen");

        //Add components to pop-up menu
        popup.add(showItem);
        popup.addSeparator();
        popup.add(startstopItem);
        popup.addSeparator();
        popup.add(exitItem);

        trayIcon.setPopupMenu(popup);

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("TrayIcon konnte nicht hinzugefuegt werden.");
        }

        showItem.addActionListener((e -> Platform.runLater(() -> {
            primaryStage.show();
            primaryStage.toFront();
        })));

        startstopItem.addActionListener(event -> {
            if (trayIcon.getImage() == image_rot) {
                startstopItem.setLabel("Deaktivieren");
                trayIcon.setImage(image_gruen);
                try {
                    ProgramController.startProgram();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                trayIcon.displayMessage("Mouse Clicker", "Programm ist gestartet", TrayIcon.MessageType.INFO);
            } else {
                startstopItem.setLabel("Aktivieren");
                trayIcon.setImage(image_rot);
                ProgramController.stopProgram();
                trayIcon.displayMessage("Mouse Clicker", "Programm wurde beendet", TrayIcon.MessageType.INFO);
            }

        });

        exitItem.addActionListener((e -> System.exit(0)));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
