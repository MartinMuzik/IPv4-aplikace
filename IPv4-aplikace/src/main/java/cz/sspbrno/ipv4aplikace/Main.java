/**
 * Application for analysis of specified IPv4 address.
 * It determines: whether it is a public or non-public IP address,
 * then determines the class address of the network, subnet address,
 * network number, subnet number and PC number.
 *
 *
 * @author Martin Muzik
 * @version 1.0
 * @since 2022-04-24
 */

package cz.sspbrno.ipv4aplikace;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main-scene.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Analýza IPv4 adresy");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}