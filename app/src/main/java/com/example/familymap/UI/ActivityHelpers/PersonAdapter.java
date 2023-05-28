package com.example.familymap.UI.ActivityHelpers;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.familymap.Client.Model;
import com.example.familymap.R;
import Model.Person;
import Model.Event;
import java.util.List;

public class PersonAdapter extends BaseExpandableListAdapter{
    private Context context;
    private List<String> headers;
    private List<Event> events;
    private List<Person> persons;
    private Person currPerson;

    private TextView firstLine;
    private TextView secLine;
    private ImageView listIcon;
    private Model model = Model.getModel();

    public PersonAdapter(Context context, List<String> listDataHeader, List<Event> EventList, List<Person> PersonList, Person person) {
        this.context = context;
        this.headers = listDataHeader;
        this.events = EventList;
        this.persons = PersonList;
        this.currPerson = person;
    }

    //List Adapter necessary Override Functions - EVENTS ARE DISPLAYED FIRST, FAMILY SECOND
    @Override
    public int getGroupCount() { return headers.size(); }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (groupPosition == 0)
            return events.size();
        else
            return persons.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        if (groupPosition == 0)
            return events;
        else
            return persons;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if(groupPosition == 0)
            return events.get(childPosition);
        else
            return persons.get(childPosition);
    }

    //not used, but need to override
    @Override
    public long getGroupId(int groupPosition) {return groupPosition;}

    @Override
    public long getChildId(int groupPosition, int childPosition) {return childPosition; }

    @Override
    public boolean hasStableIds() {return false;}


    //header layout n inflate
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = headers.get(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.header_event, null);
        }
        TextView header = convertView.findViewById(R.id.event_header);
        header.setText(headerTitle);
        return convertView;
    }

    //child layout and inflate- for each individual event or person item in the expandable list
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.event_item, null);
        }

        firstLine = convertView.findViewById(R.id.event_info);
        secLine = convertView.findViewById(R.id.event_person);
        listIcon = convertView.findViewById(R.id.list_item_icon);

        if (groupPosition == 0) {
            Event currEvent = (Event) getChild(groupPosition, childPosition);
            listIcon.setImageDrawable(convertView.getResources().getDrawable(R.drawable.map_pointer_icon));
            generateChild(currEvent, null);
        }
        else{
            Person currPerson = (Person) getChild(groupPosition, childPosition);
            if (currPerson.getGender().toLowerCase().equals("m")){
                listIcon.setImageDrawable(convertView.getResources().getDrawable(R.drawable.male_icon));
            }
            else {
                listIcon.setImageDrawable(convertView.getResources().getDrawable(R.drawable.female_icon));
            }
            generateChild(null, currPerson);
        }
        return convertView;
    }

    //make child selectable-always true
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) { return true; }


    //Initiate text/details and icon of a Child Layout
    private void generateChild(Event Event, Person Person) {
        if (Person == null) {
            String eventInfo = Event.getEventType() + ", " + Event.getCity() + ", " + Event.getCountry() + " " + Event.getYear();
            firstLine.setText(eventInfo);
            Person currPerson = model.getPeopleMap().get(Event.getPersonID());
            String personInfo = currPerson.getFirstName() + " " + currPerson.getLastName();
            secLine.setText(personInfo);
        }
        else {
            String personInfo = Person.getFirstName() + " " + Person.getLastName();
            firstLine.setText(personInfo);
            secLine.setText(findRelationship(Person));
        }
    }

    //find the relationship of the person item to the current Person
    private String findRelationship(Person Person) {
        if (currPerson.getSpouseID().equals(Person.getPersonID())) {
            return "Spouse";
        }

        if (Person.getFatherID() != null && Person.getMotherID() != null) {
            if (Person.getFatherID().equals(currPerson.getPersonID()) || Person.getMotherID().equals(currPerson.getPersonID()))
                return "Child";
        }

        if (currPerson.getMotherID() != null && currPerson.getMotherID() != null) {
            if (currPerson.getFatherID().equals(Person.getPersonID())) {
                return "Father";
            }
            else if (currPerson.getMotherID().equals(Person.getPersonID())) {
                return "Mother";
            }
        }
        return "Error";
    }
}
