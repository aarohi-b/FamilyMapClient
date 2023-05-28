package com.example.familymap.UI.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.familymap.Client.Model;
import com.example.familymap.R;
import com.example.familymap.UI.ActivityHelpers.PersonAdapter;

import java.util.ArrayList;
import java.util.List;
import Model.Person;
import Model.Event;

public class PersonActivity extends AppCompatActivity {
    private Person currPerson;
    private TextView firstName;
    private TextView lastName;
    private TextView gender;
    private ExpandableListView myListView;
    private ExpandableListAdapter myListAdapter;
    private Model model = Model.getModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("FamilyMap: Person Details");
        currPerson = model.getSelectedPerson();

        firstName = findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name);
        gender = findViewById(R.id.gender);

        firstName.setText(currPerson.getFirstName());
        lastName.setText(currPerson.getLastName());
        gender.setText(currPerson.getGender().toUpperCase());

        myListView = findViewById(R.id.person_expandable_list);

        myListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Intent intent;
                if (groupPosition == 0){
                    intent = new Intent(PersonActivity.this, EventActivity.class);
                    intent.putExtra("Event", "Event");
                    model.setSelectedEvent((Event) myListAdapter.getChild(groupPosition, childPosition));
                }
                else {
                    intent = new Intent(PersonActivity.this, PersonActivity.class);
                    model.setSelectedPerson((Person) myListAdapter.getChild(groupPosition, childPosition));
                }
                startActivity(intent);
                return false;
            }
        });
        personClicked();    //Initialize PersonActivity Adapter when a person's marker is clicked
    }

    private void personClicked() {
        //making the expandable list
        List<Person> familyMembers = new ArrayList<>(model.findRelatives(currPerson.getPersonID()));
        List<Event> eventsArrayList = new ArrayList<>(model.getPeopleEventMap().get(currPerson.getPersonID()));
        eventsArrayList = model.sortEventsByYear(eventsArrayList);

        List<String> headers = new ArrayList<>();
        headers.add("Events");
        headers.add("Family relatives");

        eventsArrayList = filterEvents(eventsArrayList);
        familyMembers = filterPersons(familyMembers);

        myListAdapter = new PersonAdapter(this, headers, eventsArrayList, familyMembers, currPerson);
        myListView.setAdapter(myListAdapter);
    }

    //Overriding up Button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        return true;
    }

    private List<Event> filterEvents(List<Event> eventsList) {
        List<Event> temp = new ArrayList<>();
        for (Event currEvent: eventsList) {
            if (model.getDisplayedEvents().containsValue(currEvent)){
                temp.add(currEvent);
            }
        }
        return temp;
    }

    private List<Person> filterPersons(List<Person> personsList) {
        List<Person> temp = new ArrayList<>();
        for (Person person: personsList) {
            if (model.isPersonDisplayed(person)){
                temp.add(person);
            }
        }
        return temp;
    }
}
