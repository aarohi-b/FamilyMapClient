package com.example.familymap.Tasks;

import android.os.AsyncTask;
import com.example.familymap.Client.Model;
import Request.LoginReq;
import Response.LoginResponse;

public class LoginTask extends AsyncTask<LoginReq, LoginResponse, LoginResponse> implements GetDataTask.DataContext{

    //extends the AsyncTask and checks if the login or register request is valid and then uses a DataTask to extract data
    private String serverHost;
    private String ipAddress;
    private LoginContext context;

    public interface LoginContext {
        void onExecuteComplete(String message);
    }

    public LoginTask(String server, String ip, LoginContext c) {
        serverHost = server;
        ipAddress = ip;
        context = c;
    }

    @Override
    public LoginResponse doInBackground(LoginReq... myLoginRequest) {
        ServerProxy serverProxy = ServerProxy.initialize();
        LoginResponse loginResult = serverProxy.login(serverHost, ipAddress, myLoginRequest[0]);
        return loginResult;
    }

    @Override
    protected void onPostExecute(LoginResponse loginResult) {
        if (loginResult.getErrorMessage() == null){
            Model model = Model.getModel();
            model.setServerHost(serverHost);
            model.setIpAddress(ipAddress);
            model.setAuthToken(loginResult.getAuthTok());

            GetDataTask dataTask = new GetDataTask(serverHost, ipAddress, this);
            dataTask.execute(loginResult.getAuthTok());
        }
        else {
            context.onExecuteComplete(loginResult.getErrorMessage());
        }
    }

    @Override
    public void onExecuteCompleteData(String message) {
        context.onExecuteComplete(message);
    }

}
