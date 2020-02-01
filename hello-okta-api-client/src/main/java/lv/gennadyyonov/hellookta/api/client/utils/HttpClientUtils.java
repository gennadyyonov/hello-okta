package lv.gennadyyonov.hellookta.api.client.utils;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import static java.util.Base64.getEncoder;

@UtilityClass
public class HttpClientUtils {

    public static String createAuthorization(String username, String password) {
        String credentials = username + ":" + password;
        return getEncoder().encodeToString(credentials.getBytes());
    }

    @SneakyThrows
    public static URLConnection doPost(String uri, Map<String, String> headers, String content) {
        URL url = new URL(uri);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        headers.forEach(connection::setRequestProperty);
        PrintStream outputStream = new PrintStream(connection.getOutputStream());
        outputStream.print(content);
        outputStream.close();
        return connection;
    }

    @SneakyThrows
    public static URLConnection doGet(String uri, Map<String, String> headers) {
        URL url = new URL(uri);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        headers.forEach(connection::setRequestProperty);
        return connection;
    }

    @SneakyThrows
    public static String readResponse(URLConnection connection) {
        try (InputStream inputStream = connection.getInputStream();
             BufferedReader br = new BufferedReader(new InputStreamReader((inputStream)))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        }
    }
}
