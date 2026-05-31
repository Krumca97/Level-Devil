package projekt.frontEnd;

import com.google.gson.Gson;
import projekt.backEnd.entities.Player;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiClient {
    private static final String BaseUrl = "http://localhost:8080";
    private static final HttpClient client = HttpClient.newHttpClient();

    public static Player findPlayerByName(String name){
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new java.net.URI(BaseUrl + "/players/name/" + name))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                Gson gson = new Gson();
                return gson.fromJson(response.body(), Player.class);
            }
            else{
                return null;
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static Player newPlayer(Player player){
        try{
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new java.net.URI(BaseUrl + "/players"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(player)))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 201) {
                Gson gson = new Gson();
                return gson.fromJson(response.body(), Player.class);
            }
            else{
                return null;
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
