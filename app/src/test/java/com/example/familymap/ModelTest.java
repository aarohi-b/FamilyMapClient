package com.example.familymap;
import com.example.familymap.Client.Filter;
import com.example.familymap.Client.Model;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import Model.Event;
import Model.Person;
import Request.LoginReq;
import Request.RegisterReq;
import Response.EventResponse;
import Response.LoginResponse;
import Response.PersonResponse;
import Response.RegisterResponse;

public class ModelTest {
    Model model = Model.getModel();
    @Before
    public void setUp() {
        Event e1 = new Event("1", "a", "1", 22, 66,"l","p", "death", 1900);
        Event e2 = new Event("2", "a", "1", 33, 77,"m","q", "death", 1800);
        Event e3 = new Event("3", "a", "1", 44, 88,"n","r", "birth", 1850);
        Event e4 = new Event("4", "a", "1", 55, 99,"o","s", "death", 1600);

        Person p1 = new Person("1", "a", "name1", "xx", "f","4","3", "2");
        Person p2 = new Person("2", "a","name2","xx","f",null,null,"1");
        Person p3 = new Person("3", "a","name3","xx","m","2",null,"6");
        Person userMom = new Person("mom", "a","name4","xx","f",null,null,"dad");
        Person userDad = new Person("dad", "a","name4","xx","m",null,null,"mom");
        Person userOne = new Person("a", "h", "i", "j", "m","dad","mom", null);
        model.setUser(userOne);
        Person[] personArray = new Person[] {p1, p2, p3, userMom, userDad, userOne};
        Map<String, Person> personsMap = new HashMap<String, Person>();
        for(int i = 0; i < personArray.length; i++){
            String personID = personArray[i].getPersonID();
            personsMap.put(personID, personArray[i]);
        }
        model.setPeopleMap(personsMap);

        Event[] eventsArray = new Event[] {e1, e2, e3, e4};
        Map<String, Event> eventsMap = new HashMap<String, Event>();
        for(int i = 0; i < eventsArray.length; i++){
            String eventID = eventsArray[i].getEventID();
            eventsMap.put(eventID, eventsArray[i]);
        }
        model.setEventsMap(eventsMap);
        model.initializeAllData();  //sets up all the data- event related and family related
    }

    @After
    public void tearDown(){
        model.tearDown();
    }

    @Test
    public void getPeoplePass()  {   // map of all people is retrieved correctly
        Map<String, Person> testPeople = model.getPeopleMap();
        Assert.assertNotNull(testPeople);
        Assert.assertEquals(6, testPeople.size());

        Person testP1 = new Person("1", "a", "name1", "xx", "f","4","3", "2");
        Assert.assertEquals(testP1.getFatherID(), testPeople.get("1").getFatherID());

        Person testP4 = new Person("4", "d","name4","xx","m",null,null,"3");
        Assert.assertNotEquals(testP4.getFirstName(), testPeople.get("2").getFirstName());  //first names are different
    }

    @Test
    public void getEvents(){  //all events retrieval correctly
        Map<String, Event> testEvents = model.getEventMap();
        Assert.assertNotNull(testEvents);
        Assert.assertEquals(4, testEvents.size());

        Event eventOne = new Event("1", "a", "1", 22, 66,"l","p", "death", 1900);
        Assert.assertEquals(eventOne.getCountry(), testEvents.get("1").getCountry());
        Assert.assertNotEquals(eventOne.getEventID(), testEvents.get("2").getEventID());
    }

    @Test
    public void calculateRelationsPass(){
        Assert.assertNotNull(model.getUser());
        Assert.assertEquals(model.getUser().getMotherID(),"mom");
        Assert.assertEquals(model.getUser().getFatherID(),"dad");
        Assert.assertNull(model.getUser().getSpouseID()); //because I set it to be null
        Assert.assertNotNull(model.getChildrenMap().get("mom"));
        Assert.assertNotNull(model.getChildrenMap().get("dad"));
    }

    @Test
    public void getAncestorsPass(){              //paternal ancestor check
        Model model = Model.getModel();
        Set<Person> paternalAncestors = model.getPaternalAncestors();
        Set<Person> maternalAncestors = model.getMaternalAncestors();
        Assert.assertNotNull(paternalAncestors);
        Assert.assertEquals(1, paternalAncestors.size());
        Assert.assertNotNull(maternalAncestors);
        Assert.assertEquals(1, maternalAncestors.size());
    }

    @Test
    public void calculateRelationsFail(){
        Assert.assertFalse(model.getMaternalAncestors().isEmpty());
        Assert.assertFalse(model.getPaternalAncestors().isEmpty());
        Assert.assertNotEquals(3113, model.getPaternalAncestors().size()); //their is only 1 paternal ancestor- "dad"
        Assert.assertNotEquals(3113, model.getMaternalAncestors().size()); //their is only 1 maternal ancestor- "mom"
        Assert.assertNotEquals(model.getUser().getSpouseID(), "bad id"); //because I set it to be null
        Assert.assertNotEquals(model.getChildrenMap().get("mom").size(), 122);
        Assert.assertNotEquals(model.getChildrenMap().get("dad").size(), 122);
    }

    @Test
    public void sortEventsByYear(){ //contains both pass and fail conditions
        List<Event> eventsArrayList = model.getPeopleEventMap().get("1");
        Assert.assertNotNull(eventsArrayList);

        Event eventOne = new Event("1", "a", "1", 22, 66,"l","p", "death", 1900);
        Event eventTwo = new Event("2", "a", "1", 33, 77,"m","q", "death", 1800);
        Event eventThree = new Event("3", "a", "1", 44, 88,"n","r", "birth", 1850);
        Event eventFour = new Event("4", "a", "1", 55, 99,"o","s", "death", 1600);

        Assert.assertEquals(eventOne, eventsArrayList.get(0));
        Assert.assertEquals(eventFour, eventsArrayList.get(3));

        eventsArrayList = model.sortEventsByYear(eventsArrayList); //sorting events according to year
        Assert.assertEquals(eventFour, eventsArrayList.get(0));
        Assert.assertEquals(eventTwo, eventsArrayList.get(1));
        Assert.assertEquals(eventThree, eventsArrayList.get(2));
        Assert.assertEquals(eventOne, eventsArrayList.get(3));

        //fail case
        Assert.assertFalse(eventsArrayList.get(0).getYear()>eventsArrayList.get(1).getYear()); //eventsList is sorted now-earliest year to latest year

    }

    @Test
    public void filterEvents(){ //contains pass and fail/abnormal cases

        Event dadEvent = new Event("a", "a", "dad", 55, 99,"o","s", "death", 1600);
        model.getEventMap().put("dad",dadEvent); //single event that is connected to father's side
        Filter filter = model.getFilter();
        filter.setFathersSide(true);
        Map<String, Event> test = model.getDisplayedEvents();
        Assert.assertNotNull(test);
        Assert.assertEquals(5, test.size());

        filter.setFathersSide(false); //father's side is not displayed
        Assert.assertTrue(model.isPaternalAncestor(model.getPeopleMap().get("dad")));
        Assert.assertFalse(model.isPaternalAncestor(model.getPeopleMap().get("mom")));
        Assert.assertEquals(model.isPersonDisplayed(model.getPeopleMap().get("dad")), false); //not displayed because he is the dad-father's side
        Assert.assertEquals(model.isPersonDisplayed(model.getPeopleMap().get("mom")), true); //not displayed because he is the dad-father's side

        test = model.getDisplayedEvents();
        Assert.assertNotNull(test);
        Assert.assertNotEquals(5,test.size());
        Assert.assertEquals(4, test.size()); //new size is 4. One event was removed, which was related to father's side because filter is one

        filter.setFemales(false);
        test = model.getDisplayedEvents();
        Assert.assertNotEquals(4,test.size()); //all the events left now are female events- so with the filter on, none are displayed
        Assert.assertEquals(0,test.size());
    }

    @Test
    public void searchTest() { //contains both pass and fail cases
        model.generateSuggestions("name");
        Assert.assertNotNull(model.getSearchResultsPeople());
        model.generateSuggestions("name1");
        Assert.assertNotNull(model.getSearchResultsPeople());
        Assert.assertEquals(model.getSearchResultsPeople().size(),1);

        //fail case- bad/invalid search
        model.generateSuggestions("badSearch");
        Assert.assertEquals(model.getSearchResultsEvents().size(),0);
        Assert.assertEquals(model.getSearchResultsPeople().size(),0);
    }
}
