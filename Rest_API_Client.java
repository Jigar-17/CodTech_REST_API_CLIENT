import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Scanner;

import org.json.JSONObject;

public class Rest_API_Client {

    private static final String API_KEY = "01a7e6be349d9b78a85febc69a5919cd"; // Replace this
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter city name: ");
        String CITY = scanner.nextLine();
        scanner.close();

        try {
            String urlString = BASE_URL + "?q=" + CITY + "&appid=" + API_KEY + "&units=metric";
            
            // Use URI and then convert to URL to avoid deprecation
            URI uri = new URI(urlString);
            URL url = uri.toURL();  // Convert URI to URL
            
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder responseContent = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    responseContent.append(inputLine);
                }
                in.close();
                parseAndDisplayWeather(responseContent.toString());
            } else {
                System.out.println("Error: Unable to fetch data. HTTP Code: " + responseCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void parseAndDisplayWeather(String jsonResponse) {
        JSONObject obj = new JSONObject(jsonResponse);

        String cityName = obj.getString("name");
        JSONObject main = obj.getJSONObject("main");
        double temp = main.getDouble("temp");
        int humidity = main.getInt("humidity");
        String condition = obj.getJSONArray("weather").getJSONObject(0).getString("description");

        System.out.println("Weather in " + cityName + ":");
        System.out.println("Temperature: " + temp + "°C");
        System.out.println("Humidity: " + humidity + "%");
        System.out.println("Condition: " + condition);
    }
}
