package com.example.familymap.UI.ActivityHelpers;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.example.familymap.Client.Model;
import com.example.familymap.R;
import com.example.familymap.UI.Activities.EventActivity;
import com.example.familymap.UI.Activities.PersonActivity;

import Model.Event;
import Model.Person;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchHolder>{
    private List<Event> events;
    private List<Person> persons;
    private Context context;
    private LayoutInflater inflater;

    //Constructor
    public SearchAdapter(List<Event> events, List<Person> persons, Context context) {
        this.context = context;
        this.events = events;
        this.persons = persons;
        inflater = LayoutInflater.from(context);
    }

    //Creates View Holder
    @Override
    public SearchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.event_item, parent, false);
        return new SearchHolder(view);
    }

    //Binds the View Holder to a SearchHolder
    @Override
    public void onBindViewHolder(SearchHolder holder, int position) {
        if(position < persons.size()) {

            holder.getLinearLayout().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    //if person is clicked start person activity
                    personsClicked(persons.get(position));
                }
            });
            holder.bindPerson(persons.get(position));
        } else {
            holder.getLinearLayout().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    //if event is clicked start event activity
                    eventClicked(events.get(position - persons.size()));
                }
            });
            holder.bindEvent(events.get(position - persons.size()));
        }
    }

    @Override
    public int getItemCount() {return persons.size() + events.size();}

    //Go to Event Activity
    private void eventClicked(Event event) {
        Intent intent = new Intent(context, EventActivity.class);
        intent.putExtra("Event", "Event");
        Model.getModel().setSelectedEvent(event);
        context.startActivity(intent);
    }

    //Go to Person Activity
    private void personsClicked(Person person) {
        Intent intent = new Intent(context, PersonActivity.class);
        Model.getModel().setSelectedPerson(person);
        context.startActivity(intent);
    }
}
