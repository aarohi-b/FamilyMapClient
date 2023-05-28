package com.example.familymap.Client;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import Model.Person;
import Model.Event;


public class Model {
    private static Model myObject;
    private Filter filter;
    private String serverHost;
    private String ipAddress;
    private String authToken;
    private boolean loggedIn;

    private Map<String, Event> eventMap;
    private Map<String,Person> peopleMap;
    private Map<String, List<Event>> peopleEventMap; //events related to people
    private List<String> eventTypes;

    private Person user;
    private Set<Person> paternalAncestors;
    private Set<Person> maternalAncestors;
    private Person selectedPerson;
    private Event selectedEvent;
    private Map<String, Person> children;
    private Map<String, List<Person>> childrenMap;
    private Map<String, Event> displayedEvents;
    private ArrayList<Person> searchResultsPeople;
    private ArrayList<Event> searchResultsEvents;
    private Map <String, Colors> eventColor;
    private Settings settings;

    public Set<Person> getPaternalAncestors() {
        return paternalAncestors;
    }
    public Set<Person> getMaternalAncestors() {
        return maternalAncestors;
    }
    public void setServerHost(String serverHost) {
        this.serverHost = serverHost;
    }
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
    public Map<String, Person> getChildren() {
        return children;
    }
    public Map<String, List<Person>> getChildrenMap() {
        return childrenMap;
    }
    public ArrayList<Person> getSearchResultsPeople() {
        return searchResultsPeople;
    }
    public void setSearchResultsPeople(ArrayList<Person> searchResultsPeople) {
        this.searchResultsPeople = searchResultsPeople;
    }
    public ArrayList<Event> getSearchResultsEvents() {
        return searchResultsEvents;
    }
    public void setSearchResultsEvents(ArrayList<Event> searchResultsEvents) {
        this.searchResultsEvents = searchResultsEvents;
    }
    public Map<String, Event> getEventMap() {
        return eventMap;
    }
    public Map<String, Person> getPeopleMap() {
        return peopleMap;
    }
    public List<String> getEventTypes() {
        return eventTypes;
    }
    public Settings getSettings() {
        return settings;
    }
    public Event getSelectedEvent() {
        return selectedEvent;
    }
    public Person getSelectedPerson() { return selectedPerson; }
    public void setSelectedPerson(Person selectedPerson) {
        this.selectedPerson = selectedPerson;
    }
    public void setSelectedEvent(Event selectedEvent) {
        this.selectedEvent = selectedEvent;
    }
    public void setUser(Person user) {
        this.user = user;
    }
    public void setEventTypes(List<String> eventTypes) {
        this.eventTypes = eventTypes;
    }
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
    public Map<String, Colors> getEventColor() {
        return eventColor;
    }
    public Filter getFilter() {
        return filter;
    }
    public Map<String, List<Event>> getPeopleEventMap() {
        return peopleEventMap;
    }
    public Person getUser() {
        return user;
    }
    public void setPeopleMap(Map<String, Person> people) {
        this.peopleMap=people;
        createMomDadSet();
    }
    public void setEventsMap(Map<String, Event> events) {
        this.eventMap = events;
    }

    //get instance of model
    public static Model getModel() {
        if (myObject == null) {
            myObject = new Model();
        }
        return myObject;
    }

    public void tearDown() {
        myObject=null;
    }
    //default constructor
    private Model() {
        peopleMap = new HashMap<>();
        peopleEventMap = new HashMap<>();
        eventMap = new HashMap<>();
        user = new Person();
        paternalAncestors = new HashSet<>();
        maternalAncestors = new HashSet<>();
        eventTypes=new ArrayList<>();
        childrenMap = new HashMap<>();
        authToken = new String();
        loggedIn = false;
    }

    //Sort Events By Year to display in event expandable list
    public List<Event> sortEventsByYear(List<Event> eventsArrayList) {
        List<Event> sortedEventsList = new ArrayList<>();
        List<Event> currArrayList = new ArrayList<>(eventsArrayList);

        while(currArrayList.size() > 0){
            Event currEvent = currArrayList.get(0);
            int index = 0;
            for (int i = 0; i < currArrayList.size(); i++){
                if (currArrayList.get(i).getYear() < currEvent.getYear()){
                    currEvent = currArrayList.get(i);
                    index = i;
                }
            }
            sortedEventsList.add(currEvent);
            currArrayList.remove(index);
        }
        return sortedEventsList;
    }

    //Find all Relatives of a Person
    public List<Person> findRelatives(String personID) {
        Person currPerson = getPeopleMap().get(personID);
        List<Person> personList = new ArrayList<>();

        if(getPeopleMap().get(currPerson.getSpouseID()) != null){
            personList.add(getPeopleMap().get(currPerson.getSpouseID()));
        }
        if(getPeopleMap().get(currPerson.getMotherID()) != null){
            personList.add(getPeopleMap().get(currPerson.getMotherID()));
        }
        if(getPeopleMap().get(currPerson.getFatherID()) != null){
            personList.add(getPeopleMap().get(currPerson.getFatherID()));
        }
        if(getChildren().get(currPerson.getPersonID()) != null){
            personList.add(getChildren().get(currPerson.getPersonID()));
        }
        return personList;
    }

    public Map<String, Event> getDisplayedEvents() { //to be displayed on the map, according to which filters are turned on/off
        displayedEvents = new HashMap<>();
        for (Event currEvent: eventMap.values()) {
            Person eventPerson = getPeopleMap().get(currEvent.getPersonID());
            if (!isPersonDisplayed(eventPerson)){
            }
            else if (!filter.containsEventType(currEvent.getEventType())){
            }
            else {
                displayedEvents.put(currEvent.getEventID(), currEvent);  //only add all events to displayedEvents if filters are not applied
            }
        }
        return displayedEvents;
    }

    public boolean isPersonDisplayed(Person currPerson) {
        if (!filter.isMales() && currPerson.getGender().toLowerCase().equals("m")){
            return false;
        }
        else if (!filter.isFemales() && currPerson.getGender().toLowerCase().equals("f")){
            return false;
        }
        else if (!filter.isFathersSide() && isPaternalAncestor(currPerson)) {
            return false;
        }
        else return filter.isMothersSide() || !isMaternalAncestor(currPerson);
    }

    public boolean isPaternalAncestor(Person person){
        for(Person p:paternalAncestors){
            if(p.getPersonID() == person.getPersonID()){
                return true;
            }
        }
        return false;
    }

    public boolean isMaternalAncestor(Person person){
        for(Person p:maternalAncestors){
            if(p.getPersonID() == person.getPersonID()){
                return true;
            }
        }
        return false;
    }

    public void setEvents(Event[] input){ //takes event response input from the server and sets those events in the model
        for (int i = 0; i < input.length; i++){
            eventMap.put(input[i].getEventID(), input[i]);
            if (peopleEventMap.containsKey(input[i].getPersonID())){
                List<Event> eventListFromMap = peopleEventMap.get(input[i].getPersonID());
                if (input[i].getYear() < eventListFromMap.get(0).getYear()){
                    eventListFromMap.add(0, input[i]);
                } else if (input[i].getYear() > eventListFromMap.get(eventListFromMap.size()-1).getYear()){
                    eventListFromMap.add(input[i]);
                } else {
                    for (int j = 0; j < eventListFromMap.size() - 1; ++j){
                        if ((input[i].getYear() > eventListFromMap.get(j).getYear()) && (!(input[i].getYear() > eventListFromMap.get(j+1).getYear()))){
                            eventListFromMap.add(j+1, input[i]);
                        }
                    }
                }
                peopleEventMap.put(input[i].getPersonID(), eventListFromMap);
            }
            else {
                List<Event> in = new ArrayList<>();
                in.add(input[i]);
                peopleEventMap.put(input[i].getPersonID(), in);
            }
        }
    }

    //initialize events and relatives (basically setup everything)
    public void initializeAllData() {
        initializeEventTypes();
        createMomDadSet();
        initializeAllPersonEvents();
        initializeAllChildren();
        if (settings == null){
            settings = new Settings();
        }
        if (filter == null) {
            filter = new Filter();
        }
    }
    
    //Everything below is generating data for the person and all these functions are called in initializeAlldata
    private void initializeEventTypes() {
        ArrayList<Event> eventsArray = new ArrayList<>();
        for (Event currEvent : eventMap.values()) {
            eventsArray.add(currEvent);
        }
        eventColor = new HashMap<>();
        eventTypes = new ArrayList<>();
        for (int i = 0; i < eventsArray.size(); i++){
            if (!eventColor.containsKey(eventsArray.get(i).getEventType().toLowerCase())){
                eventColor.put(eventsArray.get(i).getEventType().toLowerCase(),
                        new Colors(eventsArray.get(i).getEventType().toLowerCase()));

                eventTypes.add(eventsArray.get(i).getEventType().toLowerCase());
            }
        }
        setEventTypes(eventTypes);
    }

    public void createMomDadSet(){ //starts building family
        Person motherOfUser = peopleMap.get(user.getMotherID());
        Person fatherOfUser = peopleMap.get(user.getFatherID());
        addParents(motherOfUser,true);
        addParents(fatherOfUser,false);

        for (Map.Entry<String, Person> entry : peopleMap.entrySet()) {
            String possibleParentPersonID = new String(entry.getValue().getPersonID());
            List<Person> childrenOfPossible = new ArrayList<>();

            for(Map.Entry<String, Person> entryTwo : peopleMap.entrySet()){
                if (possibleParentPersonID.equals(entryTwo.getValue().getFatherID()) || possibleParentPersonID.equals(entryTwo.getValue().getMotherID())){
                    childrenOfPossible.add(entryTwo.getValue());
                }
            }
            childrenMap.put(possibleParentPersonID, childrenOfPossible);
        }
    }
    
    private void addParents(Person personReceived, Boolean fromMomSide){
        if (fromMomSide)
            maternalAncestors.add(personReceived);
        else
            paternalAncestors.add(personReceived);

        if (personReceived.getFatherID() == null || personReceived.getFatherID().equals("")){ //baseCase
            return;
        } else {
            Person mom = peopleMap.get(personReceived.getMotherID());
            Person dad = peopleMap.get(personReceived.getFatherID());
            if (fromMomSide){
                addParents(mom, true);
                addParents(dad, true);
            } else {
                addParents(mom,false); //true indicates this person belongs to maternal set
                addParents(dad,false);
            }
        }
    }

    //All Events per Person
    private void initializeAllPersonEvents() {
        peopleEventMap = new HashMap<>();
        for (Person person: peopleMap.values()) {
            ArrayList<Event> eventList = new ArrayList<Event>();

            for (Event event: eventMap.values()) {
                if (person.getPersonID().equals(event.getPersonID())){
                    eventList.add(event);
                }
            }
            peopleEventMap.put(person.getPersonID(),eventList);
        }
    }

    //All Children of each Person
    private void initializeAllChildren() {
        children = new HashMap<>();
        for (Person person: peopleMap.values()) {

            if (person.getFatherID() != null){
                children.put(person.getFatherID(), person);
            }
            if (person.getMotherID() != null){
                children.put(person.getMotherID(), person);
            }
        }
    }

    //generates search suggestions based on user input
    public void generateSuggestions(CharSequence s) {
        String searched = s.toString().toLowerCase();
        searchResultsEvents = new ArrayList<>();
        searchResultsPeople = new ArrayList<>();
        Map<String, Event> allEvents = getDisplayedEvents();
        Map<String, Person> allPeople = getPeopleMap();

        Iterator event_it = allEvents.entrySet().iterator();
        while(event_it.hasNext()) {
            Map.Entry pair = (Map.Entry)event_it.next();
            Event e = (Event)pair.getValue();
            String eDetails = e.getAssociatedUsername() + " " + e.getEventType() + " " + e.getCity() + " " + e.getCountry() + " " + e.getYear();
            if(eDetails.toLowerCase().contains(searched)) {
                searchResultsEvents.add(e);
            }
        }

        Iterator ppl_it = allPeople.entrySet().iterator();
        while(ppl_it.hasNext()) {
            Map.Entry pair2 = (Map.Entry)ppl_it.next();
            Person p = (Person) pair2.getValue();
            String pDetails = p.getFirstName() + " " + p.getLastName();
            if(pDetails.toLowerCase().contains(searched)) {
                searchResultsPeople.add(p);
            }
        }
        setSearchResultsPeople(searchResultsPeople);
        setSearchResultsEvents(searchResultsEvents);
    }

}
