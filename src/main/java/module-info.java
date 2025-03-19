module org.example.pt2025_30422_turc_ariana_assignment_1 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;

    opens org.example.pt2025_30422_turc_ariana_assignment_1 to javafx.fxml;
    exports org.example.pt2025_30422_turc_ariana_assignment_1;
    opens graphicalUserInterface to javafx.graphics, javafx.fxml;
    exports graphicalUserInterface;

}