package com.example.familymap.Tasks;

import android.os.AsyncTask;
import Request.RegisterReq;
import Response.RegisterResponse;

public class RegisterTask extends AsyncTask<RegisterReq, RegisterResponse, RegisterResponse> implements GetDataTask.DataContext{
    private String serverHost;
    private String ipAddress;
    private RegisterContext context;

    public interface RegisterContext {
        void onExecuteComplete(String message);
    }

    public RegisterTask(String server, String ip, RegisterContext c) {
        serverHost = server;
        ipAddress = ip;
        context = c;
    }

    @Override
    protected RegisterResponse doInBackground(RegisterReq... registerRequests) {
        ServerProxy serverProxy = ServerProxy.initialize();
        RegisterResponse regResult = serverProxy.register(serverHost, ipAddress, registerRequests[0]);
        return regResult;
    }

    @Override
    protected void onPostExecute(RegisterResponse registerResult) {
        if (registerResult.getErrorMessage() == null){
            GetDataTask dataTask = new GetDataTask(serverHost, ipAddress, this);
            dataTask.execute(registerResult.getAuthTok());
        }
        else {
            context.onExecuteComplete(registerResult.getErrorMessage());
        }
    }

    @Override
    public void onExecuteCompleteData(String message) {
        context.onExecuteComplete(message);
    }
}
