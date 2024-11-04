import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


// video to load jar
//https://www.youtube.com/watch?v=QAJ09o3Xl_0

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

// Program for print data in JSON format.
public class ReadJson {
    private static final String apiKey = "plmQXK3tgourzPhezfyM9EeTmFbA3mCw";
    private static final String baseUrl = "http://dataservice.accuweather.com/";
    public static void main(String args[]) throws ParseException {
        // In java JSONObject is used to create JSON object
        // which is a subclass of java.util.HashMap.

        JSONObject file = new JSONObject();
        pull();

    }

    public static void pull() throws ParseException {
        String output = "abc";
        String totlaJson="";
        try {

                URL url = new URL("http://dataservice.accuweather.com/locations/v1/cities/search?apikey="+apiKey+"&q="+"Boston");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {

                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }


            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));


            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                // System.out.println(output);
                totlaJson+=output;
            }


            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONParser parser = new JSONParser();
        //System.out.println(str);
        org.json.simple.JSONArray cities = (org.json.simple.JSONArray) parser.parse(totlaJson);

        try {
            JSONObject city = (JSONObject) cities.get(0);
            System.out.println(city);

            String cityKey = (String)city.get("Key");
            String cityName = (String)city.get("EnglishName");
            System.out.println(cityKey+","+cityName);
        }

        catch (Exception e) {
            e.printStackTrace();
        }




    }

    public void getCityKey(String keyword) {
        
    }
}


