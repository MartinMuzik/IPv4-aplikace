module cz.sspbrno.ipv4aplikace {
    requires javafx.controls;
    requires javafx.fxml;


    opens cz.sspbrno.ipv4aplikace to javafx.fxml;
    exports cz.sspbrno.ipv4aplikace;
}