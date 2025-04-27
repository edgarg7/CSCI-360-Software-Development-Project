package csci360_flight_planning.src.csci360_flight_planning;

public interface Alert {

    Object AlertType = null;

    void setContentText(String msg);

    void setHeaderText(Object object);

    void showAndWait();

}
