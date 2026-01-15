module lab01 {
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.media;
    opens projekt to javafx.fxml;
    exports projekt;
    exports projekt.history;
    opens projekt.history to javafx.fxml;
}