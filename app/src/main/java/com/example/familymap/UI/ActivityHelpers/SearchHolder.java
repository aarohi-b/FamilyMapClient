package com.example.familymap.UI.ActivityHelpers;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.familymap.Client.Model;
import com.example.familymap.R;
import Model.Event;
import Model.Person;

public class SearchHolder extends RecyclerView.ViewHolder{
    private View convertView;
    private LinearLayout myLinearLayout;
    private TextView firstLine;
    private TextView description;
    private ImageView icon;

    //Constructor
    public SearchHolder(View itemView) {
        super(itemView);
        firstLine = itemView.findViewById(R.id.event_info);
        description = itemView.findViewById(R.id.event_person);
        icon = itemView.findViewById(R.id.list_item_icon);
        myLinearLayout = itemView.findViewById(R.id.linear_layout_click_area);
        myLinearLayout.setClickable(true);
        convertView = itemView;
    }

    public LinearLayout getLinearLayout() {
        return myLinearLayout;
    }

    //bind events in the search activity
    public void bindEvent(Object currObject) {
        final Event event = (Event) currObject;
        String eventInfo = event.getEventType() + ", " + event.getCity() + ", "
                + event.getCountry() + " " + event.getYear();
        firstLine.setText(eventInfo);

        Model model = Model.getModel();
        Person currPerson = model.getPeopleMap().get(event.getPersonID());
        String personInfo = currPerson.getFirstName() + " " + currPerson.getLastName();
        description.setText(personInfo);
        icon.setImageDrawable(convertView.getResources().getDrawable(R.drawable.map_pointer_icon));
    }

    //bind people in search activity
    public void bindPerson(Object currObject) {
        Person currPerson = (Person) currObject;
        String personInfo = currPerson.getFirstName() + " " + currPerson.getLastName();
        firstLine.setText(personInfo);
        description.setVisibility(View.INVISIBLE);
        if (currPerson.getGender().toLowerCase().equals("m")) {
            icon.setImageDrawable(convertView.getResources().getDrawable(R.drawable.male_icon));
        } else {
            icon.setImageDrawable(convertView.getResources().getDrawable(R.drawable.female_icon));
        }
    }
}
