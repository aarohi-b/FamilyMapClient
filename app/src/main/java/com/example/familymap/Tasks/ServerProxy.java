package com.example.familymap.Tasks;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import Request.LoginReq;
import Request.RegisterReq;
import Response.LoginResponse;
import Response.RegisterResponse;
import Response.EventResponse;
import Response.PersonResponse;

public class ServerProxy {
    private static ServerProxy serverProxy;

    //Singleton Constructor
    public static ServerProxy initialize() {
        if (serverProxy == null){
            serverProxy = new ServerProxy();
        }
        return serverProxy;
    }

    //login
    public LoginResponse login(String serverHost, String serverPort, LoginReq loginRequest) {
        Gson gson = new Gson();
        LoginResponse result = new LoginResponse();
        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/login");

            HttpURLConnection http = (HttpURLConnection)url.openConnection();

            http.setRequestMethod("POST");
            http.setDoOutput(true);

            http.addRequestProperty("Accept", "application/json");

            http.connect();

            String reqInfo = gson.toJson(loginRequest);
            OutputStream body = http.getOutputStream();
            writeString(reqInfo, body);

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream respBody = http.getInputStream();
                String respData = readString(respBody);
                result = gson.fromJson(respData, LoginResponse.class);
            }
            else {
                result.setErrorMessage(http.getResponseMessage());
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            result.setErrorMessage("Error with Login");
        }
        return result;
    }

    //register
    public RegisterResponse register(String serverHost, String serverPort, RegisterReq regReq) {
        Gson gson = new Gson();
        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/register");

            HttpURLConnection http = (HttpURLConnection)url.openConnection();

            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.addRequestProperty("Accept", "application/json");

            http.connect();

            String requestInfo = gson.toJson(regReq);
            OutputStream body = http.getOutputStream();
            writeString(requestInfo, body);
            System.out.println(http.getResponseCode());
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream respBody = http.getInputStream();
                String respData = readString(respBody);
                RegisterResponse regResult = gson.fromJson(respData, RegisterResponse.class);
                return regResult;
            }
            else {
                return new RegisterResponse(http.getResponseMessage());
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            return new RegisterResponse("Error with Registering User");
        }
    }

    //get all people
    public PersonResponse getAllPeople(String serverHost, String serverPort, String auth){
        Gson gson = new Gson();
        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/person");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("GET");
            http.setDoOutput(false);
            http.addRequestProperty("Authorization", auth);
            http.addRequestProperty("Accept", "application/json");
            http.connect();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK){
                InputStream respBody = http.getInputStream();
                String respData = readString(respBody);
                PersonResponse allPersonResults = gson.fromJson(respData, PersonResponse.class);
                return allPersonResults;

            } else {
                return new PersonResponse(http.getResponseMessage());
            }

        } catch (Exception e){
            e.printStackTrace();
            return new PersonResponse("Error with retrieving all people");
        }
    }

    //get all events
    public EventResponse getAllEvents(String serverHost, String serverPort, String authToken) {
        Gson gson = new Gson();
        EventResponse allEventResults = new EventResponse();
        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/event");
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("GET");
            http.setDoOutput(false);
            http.addRequestProperty("Authorization", authToken);
            http.addRequestProperty("Accept", "application/json");

            http.connect();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream respBody = http.getInputStream();
                String respData = readString(respBody);
                allEventResults = gson.fromJson(respData, EventResponse.class);
                return allEventResults;
            }
            else {
                allEventResults.setMessage(http.getResponseMessage());
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            allEventResults.setMessage("Error with retrieving all events");
        }
        return allEventResults;
    }

    private static String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

    private static void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }
}
