package cubicmadness.bin;

import java.awt.HeadlessException;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Martin
 */
public class HttpRequester {

    public static String submitScore(String nick, int score, GameHistory history) {
        String url = "http://cubicmadness.rainbowshaggy.com/addScore.php";
        String params = "";
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
            //Proxy
            Proxy proxy = null;
            if(JOptionPane.showConfirmDialog(null, "Jsi ve škole?", "Proxy", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
                proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("192.168.1.1", 800));
            
            //Create connection
            url = new URL(targetUrl);
            if(proxy != null)
                connection = (HttpURLConnection) url.openConnection(proxy);
            else
                connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(1000);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");

            connection.setRequestProperty("Content-Length", ""
                    + Integer.toString(params.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            
            try ( //Send request
                    DataOutputStream wr = new DataOutputStream(
                            connection.getOutputStream())) {
                wr.writeBytes(params);
                wr.flush();
            }

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

        } catch (HeadlessException | IOException e) {
            return "ERROR";

        } finally {

            if (connection != null) {
                connection.disconnect();
            }
        }
    }
    
    public static String getTopTen(){
        return getScore(0, 10);
    }
    
    public static String getScore(int from, int n){
        String url = "http://cubicmadness.rainbowshaggy.com/getScore.php?";
        String params = "";
        try {
            params = "from=" + URLEncoder.encode(Integer.toString(from), "UTF-8")
                    + "&n=" + URLEncoder.encode(Integer.toString(n), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(HttpRequester.class.getName()).log(Level.SEVERE, null, ex);
        }
        url += params;
        return requestGet(url);
    }
    
    private static String requestGet(String targetUrl){
        URL url;
        HttpURLConnection connection = null;
        try {
            //Proxy
            Proxy proxy = null;
            if(JOptionPane.showConfirmDialog(null, "Jsi ve škole?", "Proxy", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
                proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("192.168.1.1", 800));
            
            //Create connection
            url = new URL(targetUrl);
            if(proxy != null)
                connection = (HttpURLConnection) url.openConnection(proxy);
            else
                connection = (HttpURLConnection) url.openConnection();
            
            connection.setConnectTimeout(1000);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches(false);
            connection.setDoInput(true);
            

            //Get Response	
            InputStream is = connection.getInputStream();
            StringBuilder response;
            try (BufferedReader rd = new BufferedReader(new InputStreamReader(is))) {
                String line;
                response = new StringBuilder();
                while ((line = rd.readLine()) != null) {
                    response.append(line);
                    response.append(System.lineSeparator());
                }
            }
            return response.toString();

        } catch (HeadlessException | IOException e) {
            e.printStackTrace();
            return "ERROR";
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
