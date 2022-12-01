module fi.tuni.compse110.project {
    requires transitive javafx.controls;
    requires transitive javafx.graphics;
    requires javafx.fxml;
    requires transitive org.json;
    requires jfreechart.fx;
    requires org.jfree.jfreechart;
    requires java.desktop;
    opens fi.tuni.compse110.project to javafx.fxml;
    exports fi.tuni.compse110.project;
    exports fi.tuni.compse110.project.API;
    exports fi.tuni.compse110.project.UIView;
    opens fi.tuni.compse110.project.API to javafx.fxml;
    exports fi.tuni.compse110.project.Graph;
    opens fi.tuni.compse110.project.Graph to javafx.fxml;
    exports fi.tuni.compse110.project.UIView.Scenes;
}
