module fi.tuni.compse110.project {
    requires javafx.controls;
    requires transitive javafx.graphics;
    requires javafx.fxml;
    requires transitive org.json;
    requires jfreechart.fx;
    requires org.jfree.jfreechart;
    opens fi.tuni.compse110.project to javafx.fxml;
    exports fi.tuni.compse110.project;
    exports fi.tuni.compse110.project.API;
    opens fi.tuni.compse110.project.API to javafx.fxml;
    exports fi.tuni.compse110.project.Graph;
    opens fi.tuni.compse110.project.Graph to javafx.fxml;
}
