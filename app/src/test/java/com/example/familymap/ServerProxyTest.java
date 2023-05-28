package com.example.familymap;

import com.example.familymap.Tasks.ServerProxy;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import Request.LoginReq;
import Request.RegisterReq;
import Response.EventResponse;
import Response.LoginResponse;
import Response.PersonResponse;
import Response.RegisterResponse;

public class ServerProxyTest {
    RegisterResponse regResp=new RegisterResponse();
    LoginResponse loginResp=new LoginResponse();
    LoginResponse loginResFail=new LoginResponse();
    ServerProxy serverProxy=new ServerProxy();

    @Before
    public void setUp() {
        ServerProxy serverProxy = ServerProxy.initialize();
        RegisterReq regReq = new RegisterReq("testUser","pass","a","aarohi","bhatt", "f");
        regResp = serverProxy.register("localhost", "8080", regReq);
        LoginReq loginReq = new LoginReq("testUser", "pass");
        loginResp = serverProxy.login("localhost", "8080", loginReq);

        LoginReq loginReqFail = new LoginReq("no", "no");
        loginResFail = serverProxy.login("localhost", "8080", loginReqFail);
    }

    @Test
    public void loginPass() {
        Assert.assertNotNull(loginResp.getUserName());
    }

    @Test
    public void loginFail() { //it fails because user is not registered, so it's not found
        Assert.assertNotNull(loginResFail.getErrorMessage());
    }

    @Test
    public void registerPass() {
        ServerProxy serverProxy = ServerProxy.initialize();
        RegisterReq regReq = new RegisterReq("a","b","c","d","e", "f");
        RegisterResponse regResp = serverProxy.register("localhost", "8080", regReq);
        Assert.assertNotNull(regResp.getPersonId());
    }

    @Test
    public void registerFail() { //fails because the same user is already registered
        RegisterReq regReq = new RegisterReq("testUser","pass","a","aarohi","bhatt", "f");
        RegisterResponse registerResult = serverProxy.register("localhost", "8080", regReq);
        Assert.assertNotNull(registerResult.getErrorMessage());
    }

    @Test
    public void getAllPeoplePass() {
        String authTokenTest = loginResp.getAuthTok();
        PersonResponse allPersonResults = serverProxy.getAllPeople("localhost", "8080", authTokenTest);
        Assert.assertNotNull(allPersonResults.getData());
    }

    @Test
    public void getAllPeopleFail() {
        String authTokenTest = "Invalid Token";
        PersonResponse allPersonResults = serverProxy.getAllPeople("localhost", "8080", authTokenTest);
        Assert.assertNotNull(allPersonResults.getMessage());
    }

    @Test
    public void getAllEventsPass() {
        String authTokenTest = loginResp.getAuthTok();
        EventResponse allEventsResults = serverProxy.getAllEvents("localhost", "8080", authTokenTest);
        Assert.assertNotNull(allEventsResults.getData());
    }

    @Test
    public void getAllEventsFail() {
        String authTokenTest = "Invalid Token";
        EventResponse allEventsResults = serverProxy.getAllEvents("localhost", "8080", authTokenTest);
        Assert.assertNotNull(allEventsResults.getMessage());
    }

}