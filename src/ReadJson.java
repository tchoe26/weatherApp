import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


// video to load jar
//https://www.youtube.com/watch?v=QAJ09o3Xl_0

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

// Program for print data in JSON format.
public class ReadJson {
    private static final String apiKey = "plmQXK3tgourzPhezfyM9EeTmFbA3mCw";
    private static final String location = "boston";
    public static void main(String args[]) throws ParseException {
        // In java JSONObject is used to create JSON object
        // which is a subclass of java.util.HashMap.

        JSONObject file = new JSONObject();
        pull();

    }

    public static void pull() throws ParseException {
        try {
            JSONParser parser = new JSONParser();
            //System.out.println(str);
            JSONArray cities = (JSONArray) parser.parse(openConnection("http://dataservice.accuweather.com/locations/v1/cities/search?apikey="+apiKey+"&q="+location));
            JSONObject city = (JSONObject) cities.get(0);
            System.out.println(city);
            String cityKey = (String)city.get("Key");
            String cityName = (String)city.get("EnglishName");
            System.out.println(cityKey+","+cityName);

            JSONObject dailyForecasts = (JSONObject) parser.parse(openConnection("https://dataservice.accuweather.com/forecasts/v1/daily/1day/"+cityKey+"?apikey="+apiKey));
            System.out.println(dailyForecasts);
            JSONObject headline = (JSONObject) dailyForecasts.get("Headline");
            System.out.println("headline:" +headline);
            String description = (String) headline.get("Text");
            System.out.println("description:" + description);

            //temperature
            JSONArray forecasts = (JSONArray) dailyForecasts.get("DailyForecasts");
            JSONObject forecastInfo = (JSONObject) forecasts.get(0);
            JSONObject temperature = (JSONObject) forecastInfo.get("Temperature");

            //min and max temp
            JSONObject minimumTemp = (JSONObject) temperature.get("Minimum");
            Double minimumTempValue = (Double) minimumTemp.get("Value");
            JSONObject maximumTemp = (JSONObject) temperature.get("Maximum");
            Double maximumTempValue = (Double) minimumTemp.get("Value");

            System.out.println("High: "+maximumTempValue.intValue()+"F; Low: "+minimumTempValue.intValue()+"F");

            //conditions
            JSONObject day = (JSONObject) forecastInfo.get("Day");
            String dayCondition = (String) day.get("IconPhrase");
            System.out.println(dayCondition);

        } catch (Exception e) {
            e.printStackTrace();
        }




    }
    public static String openConnection(String link) {
        String totalJson = null;
        try {
            String output;
            totalJson = "";
            URL location = new URL(link);
            HttpURLConnection conn = (HttpURLConnection) location.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            if (conn.getResponseCode() != 200) {

                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            while ((output = br.readLine()) != null) {
                // System.out.println(output);
                totalJson += output;
            }
            conn.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return totalJson;
    }


}


