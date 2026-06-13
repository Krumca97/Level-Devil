package projekt.frontEnd;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import projekt.backEnd.entities.LevelsProgress;
import projekt.backEnd.entities.Player;
import projekt.backEnd.entities.GameRecord;
import projekt.backEnd.entities.PlayerSettings;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

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
            System.out.println("newPlayer status: " + response.statusCode());
            System.out.println("newPlayer body: " + response.body());

            if (response.statusCode() == 200 || response.statusCode() == 201) {
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

    public static List<GameRecord> getGameRecordsByPlayerId(Long playerId){
        try{
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new java.net.URI(BaseUrl + "/gameRecords?playerId=" + playerId))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                Gson gson = new Gson();
                return gson.fromJson(response.body(), new TypeToken<List<GameRecord>>(){}.getType());
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

    public static List<LevelsProgress> getLevelsProgressByPlayerId(Long playerId){
        try{
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new java.net.URI(BaseUrl + "/levelProgress?playerId=" + playerId))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                Gson gson = new Gson();
                return gson.fromJson(response.body(), new TypeToken<List<LevelsProgress>>(){}.getType());
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

    public static PlayerSettings getPlayerSettingsByPlayerId(Long playerId){
        try{
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new java.net.URI(BaseUrl + "/playerSettings?playerId=" + playerId))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                Gson gson = new Gson();
                List<PlayerSettings> list = gson.fromJson(response.body(), new TypeToken<List<PlayerSettings>>(){}.getType());
                if(list.isEmpty()){
                    return null;
                }
                else{
                    return list.getFirst();
                }

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

    public static GameRecord saveGameRecords(GameRecord gameRecord){
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new java.net.URI(BaseUrl + "/gameRecords"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(gameRecord)))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200 || response.statusCode() == 201) {
                Gson gson = new Gson();
                return gson.fromJson(response.body(), GameRecord.class);
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

    public static LevelsProgress saveLevelsProgress(LevelsProgress levelsProgress){
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new java.net.URI(BaseUrl + "/levelProgress"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(levelsProgress)))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200 || response.statusCode() == 201) {
                Gson gson = new Gson();
                return gson.fromJson(response.body(), LevelsProgress.class);
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

    public static PlayerSettings savePlayerSettings(PlayerSettings playerSettings){
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new java.net.URI(BaseUrl + "/playerSettings"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(playerSettings)))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200 || response.statusCode() == 201) {
                Gson gson = new Gson();
                return gson.fromJson(response.body(), PlayerSettings.class);
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
