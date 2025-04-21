package src;


import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
    private AirportManager airportManager = new AirportManager();
    private AirplaneManager airplaneManager = new AirplaneManager();

    public static void main(String[] args) {
        System.setProperty("javafx.runtime.module.path", "csci360_flight_planning/lib/javafx-sdk/lib");
        System.setProperty("javafx.runtime.add.modules", "javafx.controls,javafx.fxml");
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Label warningLabel = new Label(
                "WARNING: THIS SOFTWARE IS NOT TO BE USED FOR FLIGHT PLANNING OR NAVIGATIONAL PURPOSE");
        warningLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: red;");

        Button btnAirport = new Button("Airport Functions");
        Button btnAirplane = new Button("Airplane Functions");
        Button btnPlanFlight = new Button("Plan a Flight");
        Button btnExit = new Button("Exit");

        btnAirport.setMaxWidth(Double.MAX_VALUE);
        btnAirplane.setMaxWidth(Double.MAX_VALUE);
        btnPlanFlight.setMaxWidth(Double.MAX_VALUE);
        btnExit.setMaxWidth(Double.MAX_VALUE);

        btnAirport.setOnAction(e -> showAlert("Airport Functions: (stub)"));
        btnAirplane.setOnAction(e -> showAlert("Airplane Functions: (stub)"));
        btnPlanFlight.setOnAction(e -> showAlert("Plan Flight: (stub)"));
        btnExit.setOnAction(e -> primaryStage.close());

        VBox root = new VBox(10,
                warningLabel,
                btnAirport,
                btnAirplane,
                btnPlanFlight,
                btnExit);
        root.setPadding(new Insets(20));

        primaryStage.setTitle("Flight Planning System");
        primaryStage.setScene(new Scene(root, 400, 300));
        primaryStage.show();
    }

    /** Utility to pop up a simple information alert. */
    private void showAlert(String msg) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
