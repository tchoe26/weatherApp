import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.OutputStream;


// video to load jar
//https://www.youtube.com/watch?v=QAJ09o3Xl_0

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

// Program for print data in JSON format.
public class ReadJson {
    static String instructions = "create a concise, sentence form summary of the weather based on this json data. this is going directly on the output of my app so include JUST the sentences and no sources, introductory text, etc. format it as sentences in a paragraph like a short description you might see on a website, not bulet points. aim to make it about 5 sentences. this is the json data:";
    private static final String accuWeatherApiKey = "plmQXK3tgourzPhezfyM9EeTmFbA3mCw";
    private static final String perplexityApiKey = "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";
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

            JSONArray cities = (JSONArray) parser.parse(openConnection("http://dataservice.accuweather.com/locations/v1/cities/search?apikey="+accuWeatherApiKey+"&q="+location));
            JSONObject city = (JSONObject) cities.get(0);
            System.out.println(city);
            String cityKey = (String)city.get("Key");
            String cityName = (String)city.get("EnglishName");
            System.out.println(cityKey+","+cityName);

            JSONObject dailyForecasts = (JSONObject) parser.parse(openConnection("https://dataservice.accuweather.com/forecasts/v1/daily/1day/"+cityKey+"?apikey="+accuWeatherApiKey+"&details=true"));
            String perplexityInput = dailyForecasts.toString();
            System.out.println(dailyForecasts);
            JSONObject headline = (JSONObject) dailyForecasts.get("Headline");
            System.out.println("headline:" +headline);
            String description = (String) headline.get("Text");
            System.out.println("description:" + description);

            //condition for image
            String condition = (String) headline.get("Category");

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

            //wind

            JSONObject wind = (JSONObject)day.get("Wind");
            System.out.println(day);
            JSONObject speed = (JSONObject)wind.get("Speed");
            Double windSpeedValue = (Double)speed.get("Value");
            System.out.println(windSpeedValue);


            JSONArray hourlyForecasts = (JSONArray) parser.parse(openConnection("https://dataservice.accuweather.com/forecasts/v1/hourly/1hour/"+cityKey+"?apikey="+accuWeatherApiKey));
            JSONObject hourlyForecastsValue = (JSONObject)hourlyForecasts.get(0);
            JSONObject currentTemp = (JSONObject)hourlyForecastsValue.get("Temperature");
            Double tempValue = (Double)currentTemp.get("Value");
            System.out.println(tempValue);

            System.out.println(perplexity(instructions + perplexityInput));

            String iconLink = "https://openweathermap.org/img/wn/"+ perplexity("based on the json data, find the condition that shows up as Category under the Headlines tab. then, go to https://openweathermap.org/weather-conditions and pick the icon code ie 01d for the icon that best fits this condition. your answer must contain NOTHING but the code, no side text. to clarify, your options are only the following:01d through 13d" + perplexityInput)+"@2x.png";
            System.out.println(iconLink);

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

    public static String perplexity(String userInput) throws ParseException {
        JSONParser parser = new JSONParser();
        String totalJson = null;
        String escapedSpecialCharacters = userInput.replace("\"", "\\\"").replace("\n", "\\n");
        try {
            String output;
            totalJson = "";
            URL location = new URL("https://api.perplexity.ai/chat/completions");
            HttpURLConnection conn = (HttpURLConnection) location.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer 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");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);


            String jsonBody = String.format("""
            {
                "model": "llama-3.1-sonar-small-128k-online",
                "messages": [
                    {
                        "role": "user",
                        "content": "%s"
                    }
                ]
            }""", escapedSpecialCharacters);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonBody.getBytes("utf-8");
                os.write(input, 0, input.length);
            }


            if (conn.getResponseCode() != 200) {
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                StringBuilder errorResponse = new StringBuilder();
                String line;
                while ((line = errorReader.readLine()) != null) {
                    errorResponse.append(line);
                }
                errorReader.close();
                System.out.println("Error details: " + errorResponse.toString());
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream()), "utf-8"));
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

        //isolate the response from the json output
        JSONObject response = (JSONObject) parser.parse(totalJson);
        JSONArray choices = (JSONArray)response.get("choices");
        JSONObject data = (JSONObject)choices.get(0);
        JSONObject message = (JSONObject)data.get("message");
        String content = (String)message.get("content");
        //System.out.println(content);

        return content;

    }




}


