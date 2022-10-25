module com.example {
    requires javafx.controls;
    requires transitive javafx.graphics;
    requires javafx.fxml;
    requires transitive org.json;
    opens fi.tuni.compse110.project to javafx.fxml;
    exports fi.tuni.compse110.project;
}
