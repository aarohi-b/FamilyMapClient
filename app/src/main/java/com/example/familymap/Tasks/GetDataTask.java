package com.example.familymap.Tasks;

import Model.Person;
import Model.Event;
import Response.EventResponse;
import Response.PersonResponse;

import android.os.AsyncTask;
import com.example.familymap.Client.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GetDataTask extends AsyncTask<String, Boolean, Boolean> {
    //link between server proxy and model
    private String serverHost;
    private String ipAddress;
    private DataContext context;
    private Model model = Model.getModel();
    Boolean setupDone;

    public interface DataContext {
        void onExecuteCompleteData(String message);
    }

    public GetDataTask(String server, String ip, GetDataTask.DataContext c) {
        serverHost = server;
        ipAddress = ip;
        context = c;
    }

    @Override
    protected Boolean doInBackground(String... authToken) {
        ServerProxy serverProxy = ServerProxy.initialize();
        PersonResponse allPersonResults = serverProxy.getAllPeople(serverHost, ipAddress, authToken[0]);
        EventResponse allEventResults = serverProxy.getAllEvents(serverHost, ipAddress, authToken[0]);
        setupDone = sendDataToModel(allPersonResults, allEventResults);
        return setupDone;
    }

    private boolean sendDataToModel(PersonResponse allPersonResults, EventResponse allEventResults) {
        return (sendEventsToModel(allEventResults) && sendPeopleToModel(allPersonResults));
    }

    private boolean sendPeopleToModel(PersonResponse allPersonResults) {
        if (allPersonResults.getMessage() == null){
            Map<String, Person> personsMap = new HashMap<String, Person>();
            ArrayList<Person> personArray = allPersonResults.getData();
            model.setUser(personArray.get(0));

            for(int i = 0; i < personArray.size(); i++){
                String personID = personArray.get(i).getPersonID();
                personsMap.put(personID, personArray.get(i));
            }

            model.setPeopleMap(personsMap);
            return true;
        }
        return false;
    }

    private boolean sendEventsToModel(EventResponse allEventResults) {
        if (allEventResults.getMessage() == null){
            Map<String, Event> eventsMap = new HashMap<String, Event>();
            Event[] eventsArray = allEventResults.getData();

            model.setEvents(eventsArray);

            for(int i = 0; i < eventsArray.length; i++){
                String eventID = eventsArray[i].getEventID();
                eventsMap.put(eventID, eventsArray[i]);
            }
            model.setEventsMap(eventsMap);
            return true;
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean b) {
        if (b){
            Person user = model.getUser();
            String message = "Welcome, " + user.getFirstName() + " " + user.getLastName();
            context.onExecuteCompleteData(message);
            model.initializeAllData();
        }
        else {
            context.onExecuteCompleteData("Error occurred with user data");
        }
    }
}
