package cubicmadness.bin;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Martin
 */
public class HttpRequester {

    public static String submitScore(String nick, int score, GameHistory history) {
        String url = "http://localhost/CubicMadness/addScore.php";
        String params = "";
        System.out.println(score);
        try {
            params = "nick=" + URLEncoder.encode(nick, "UTF-8")
                    + "&score=" + URLEncoder.encode(Integer.toString(score), "UTF-8")
                    + "&history=" + URLEncoder.encode(history.toJSONString(), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(HttpRequester.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return requestPost(url, params);
    }

    private static String requestPost(String targetUrl, String params) {
        URL url;
        HttpURLConnection connection = null;
        try {
            //Create connection
            url = new URL(targetUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");

            connection.setRequestProperty("Content-Length", ""
                    + Integer.toString(params.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            
            //Send request
            DataOutputStream wr = new DataOutputStream(
                    connection.getOutputStream());
            wr.writeBytes(params);
            wr.flush();
            wr.close();

            //Get Response	
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append(System.lineSeparator());
            }
            rd.close();
            return response.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";

        } finally {

            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
