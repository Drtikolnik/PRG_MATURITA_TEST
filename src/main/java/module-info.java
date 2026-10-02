module com.example.prg_maturita {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires java.desktop;


    opens com.example.prg_maturita to javafx.fxml;
    exports com.example.prg_maturita;
}