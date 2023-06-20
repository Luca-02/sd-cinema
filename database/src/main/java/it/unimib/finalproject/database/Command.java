package it.unimib.finalproject.database;
import java.io.Serializable;
import java.util.HashMap;

public class Command implements Serializable {

    public enum Request {GET, POST, PUT, DELETE}
    private Request request;
    private String apiRequest;
    private HashMap<String, Integer> parameters; // coppia nome-id

    public Command(Request request, String apiRequest, HashMap<String, Integer> parameters) {
        this.request = request;
        this.apiRequest = apiRequest;
        this.parameters = parameters;
    }

    public Command(Request request, String apiRequest) {
        this.request = request;
        this.apiRequest = apiRequest;
        parameters = null;
    }

}
