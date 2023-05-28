package com.example.familymap.UI;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.familymap.Client.Filter;
import com.example.familymap.UI.Activities.FilterActivity;
import com.example.familymap.UI.Activities.PersonActivity;
import com.example.familymap.UI.Activities.SearchActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.example.familymap.Client.Colors;
import com.example.familymap.Client.Model;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.example.familymap.R;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.PolylineOptions;

import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Model.Event;
import Model.Person;
import com.example.familymap.Client.Settings;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    private SupportMapFragment myFrag;
    private TextView myName;
    private TextView myEvent;
    private TextView myYear;
    private ImageView myIcon;
    private boolean isEvent;
    private Map<String, Event> currentDisplayedEvents;
    private List<Polyline> lineList;
    private Map<Marker, Event> mMarkerMap;
    private Marker selectedMarker;

    private Model model = Model.getModel();

    public MapFragment() {}
    public MapFragment (String eventId) {
        isEvent = eventId != null;
    }
    public static MapFragment newInstance() {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(!isEvent);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        super.onCreateView(layoutInflater, viewGroup, bundle);
        View v = layoutInflater.inflate(R.layout.activity_map, viewGroup, false);

        myFrag = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        myFrag.getMapAsync(this);

        myName = v.findViewById(R.id.textTop);
        myEvent = v.findViewById(R.id.textBottom);
        myYear = v.findViewById(R.id.year);
        myIcon = v.findViewById(R.id.map_icon);

        lineList = new ArrayList<>();
        return v;
    }
    
    @Override
    public void onResume() {
        super.onResume();
        if (mMap != null && mMarkerMap != null){
            clearMap();
            Event markedEvent = mMarkerMap.get(selectedMarker);
            putMarkers(mMap);

            if (selectedMarker == null) {
                if (!mMarkerMap.containsValue(markedEvent)) {
                    removeLines();
                }
            }
        }
        if (selectedMarker != null && mMarkerMap != null) {
            drawLines();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu myMenu, MenuInflater inflater){
        inflater.inflate(R.menu.menu, myMenu);
        super.onCreateOptionsMenu(myMenu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_settings: //settings option in menu
                filterClicked();
                return true;
            case R.id.menu_item_search:  //search option in menu
                searchClicked();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        putMarkers(googleMap);
    }

    private void filterClicked() {
        Intent intent = new Intent(getActivity(), FilterActivity.class);
        startActivity(intent);
    }

    private void searchClicked() {
        Intent intent = new Intent(getActivity(), SearchActivity.class);
        startActivity(intent);
    }


    private void clearMap() {
        for (Marker currMarker:mMarkerMap.keySet()) {
            currMarker.remove();
        }
    }

    private void putMarkers(GoogleMap googleMap) {
        selectedMarker = null;
        mMarkerMap = new HashMap<>();

        Map<String, Colors> allMapColors = model.getEventColor();
        currentDisplayedEvents = model.getDisplayedEvents();

        mMap = googleMap;
        
        //Map Marker Click Listener
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                markerClicked(marker);
                return true;
            }
        });

        for (Event currEvent : currentDisplayedEvents.values()) {
            LatLng currentPosition = new LatLng(currEvent.getLatitude(), currEvent.getLongitude());
            Colors mapColor = allMapColors.get(currEvent.getEventType().toLowerCase());

            Marker marker = mMap.addMarker(new MarkerOptions().position(currentPosition)
                    .icon(BitmapDescriptorFactory.defaultMarker(mapColor.getColor()))
                    .title(currEvent.getEventType()));
            mMarkerMap.put(marker, currEvent);

            if (model.getSelectedEvent() == currEvent){  // For Event Fragment selection
                selectedMarker = marker;
            }
        }

        if (selectedMarker != null && isEvent){  // Event Fragment camera focus
            mMap.moveCamera(CameraUpdateFactory.newLatLng(selectedMarker.getPosition()));
            markerClicked(selectedMarker);
        }
    }

    //Text OnClickListener
    View.OnClickListener onClickText = new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            textClicked();
        }
    };

    private void textClicked() {
        Intent intent = new Intent(getActivity(), PersonActivity.class);
        Person person = model.getPeopleMap().get(mMarkerMap.get(selectedMarker).getPersonID());
        model.setSelectedPerson(person);
        startActivity(intent);
    }

    private void markerClicked(Marker marker) {
        selectedMarker = marker;
        Event currEvent = mMarkerMap.get(marker);
        LatLng pos = new LatLng(currEvent.getLatitude(),currEvent.getLongitude());
        mMap.animateCamera(CameraUpdateFactory.newLatLng(pos));


        Person currPerson = model.getPeopleMap().get(currEvent.getPersonID());
        String newName = currPerson.getFirstName() + " " + currPerson.getLastName();
        String eventInfo = currEvent.getEventType() + ": " + currEvent.getCity() + ", " + currEvent.getCountry();
        String yearInfo = "(" + currEvent.getYear() + ")";

        myName.setText(newName);
        myName.setVisibility(View.VISIBLE);
        myName.setOnClickListener(onClickText);

        myEvent.setText(eventInfo);
        myEvent.setVisibility(View.VISIBLE);
        myEvent.setOnClickListener(onClickText);

        myYear.setText(yearInfo);
        myYear.setVisibility(View.VISIBLE);
        myYear.setOnClickListener(onClickText);

        if (currPerson.getGender().toLowerCase().equals("m")){
            Drawable genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_male).colorRes(R.color.maleColor).sizeDp(40);
            myIcon.setImageDrawable(genderIcon);
        }
        else {
            Drawable genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_female).colorRes(R.color.femaleColor).sizeDp(40);
            myIcon.setImageDrawable(genderIcon);
        }
        myIcon.setVisibility(View.VISIBLE);
        myIcon.setOnClickListener(onClickText);
        model.setSelectedEvent(currEvent);
        drawLines();
    }

    //all functions that are responsible for Drawing Map Lines
    private void drawLines() {
        Settings settings = Model.getModel().getSettings();
        removeLines();

        if (settings.isStoryLines()){
            drawStoryLines();
        }
        if (settings.isSpouseLines()){
            drawSpouseLines();
        }
        if (settings.isFamilyLines()){
            Event currEvent = mMarkerMap.get(selectedMarker);
            Person currPerson = model.getPeopleMap().get(currEvent.getPersonID());
            drawFamilyLines(currPerson,currEvent,10);
        }
    }
    
    private void removeLines() {
        for (com.google.android.gms.maps.model.Polyline currLine : lineList) {
            currLine.remove();
        }
        lineList = new ArrayList<>();
    }

    private void drawStoryLines() {
        Model model = Model.getModel();
        Event currEvent = mMarkerMap.get(selectedMarker);
        Person currPerson = model.getPeopleMap().get(currEvent.getPersonID());
        List<Event> eventsList = model.getPeopleEventMap().get(currPerson.getPersonID());
        eventsList = model.sortEventsByYear(eventsList);

        if (!model.getFilter().containsEventType(currEvent.getEventType())) {
            return;
        }
        firstStoryLine(eventsList);
    }

    //find the first valid events
    private void firstStoryLine(List<Event> eventsList) {
        int i = 0;
        while (i < eventsList.size() - 1) {
            if (model.getDisplayedEvents().containsValue(eventsList.get(i))) {
                Event event = eventsList.get(i);
                i++;
                secondStoryLine(event, eventsList, i);
            }
            else
                i++;
        }
    }

    //find second valid events and draw lines
    private void secondStoryLine(Event eventOne, List<Event> eventsList, int i) {
        while (i < eventsList.size()) {
            if (model.getDisplayedEvents().containsValue(eventsList.get(i))) {
                Event eventTwo = eventsList.get(i);

                Polyline newestLine = mMap.addPolyline(new PolylineOptions()
                        .add(new LatLng(eventOne.getLatitude(), eventOne.getLongitude()),
                                new LatLng(eventTwo.getLatitude(), eventTwo.getLongitude()))
                        .color(model.getSettings().getStoryColor()));
                lineList.add(newestLine);

                return;
            }
            i++;
        }
    }

    //Draw Spouse Lines
    private void drawSpouseLines() {
        Event currEvent = mMarkerMap.get(selectedMarker);
        Person currPerson = model.getPeopleMap().get(currEvent.getPersonID());
        List<Event> eventsList = model.getPeopleEventMap().get(currPerson.getSpouseID());
        if(eventsList==null){return;}
        eventsList = model.sortEventsByYear(eventsList);

        Filter filter = model.getFilter();

        if (filter.containsEventType(currEvent.getEventType())) {
            for (int i = 0; i < eventsList.size(); i++) {
                if (model.getDisplayedEvents().containsValue(eventsList.get(i))) {
                    Event spouseValidEvent = eventsList.get(i);

                    Polyline newestLine = mMap.addPolyline(new PolylineOptions()
                            .add(new LatLng(spouseValidEvent.getLatitude(), spouseValidEvent.getLongitude()),
                                    new LatLng(currEvent.getLatitude(), currEvent.getLongitude()))
                            .color(model.getSettings().getSpouseColor()));
                    lineList.add(newestLine);

                    return;
                }
            }
        }
    }

    //family Lines Recursion starter
    private void drawFamilyLines(Person currPerson, Event currEvent, int generation) {
        if (currPerson.getFatherID() != null) {
            fatherLines(currPerson, currEvent, generation);
        }
        if (currPerson.getMotherID() != null) {
            motherLines(currPerson, currEvent, generation);
        }
    }

    //father's side lines
    private void fatherLines(Person currPerson, Event focusedEvent, int generation) {
        List<Event> eventsList = model.getPeopleEventMap().get(currPerson.getFatherID());
        eventsList = model.sortEventsByYear(eventsList);

        for (int i = 0; i < eventsList.size(); i++) {
            if (currentDisplayedEvents.containsValue(eventsList.get(i))) {
                Event validEvent = eventsList.get(i);

                Polyline newestLine = mMap.addPolyline(new PolylineOptions().add(new LatLng(focusedEvent.getLatitude(), focusedEvent.getLongitude()),
                                new LatLng(validEvent.getLatitude(), validEvent.getLongitude())).color(model.getSettings().getFamilyColor())
                        .width(generation));
                lineList.add(newestLine);

                Person father = model.getPeopleMap().get(currPerson.getFatherID());
                drawFamilyLines(father, validEvent, generation / 2);
                return;
            }
        }
    }
    
    //mother's side lines
    private void motherLines(Person currPerson, Event focusedEvent, int generation) {
        List<Event> eventsList = model.getPeopleEventMap().get(currPerson.getMotherID());
        eventsList = model.sortEventsByYear(eventsList);

        for (int i = 0; i < eventsList.size(); i++) {
            if (currentDisplayedEvents.containsValue(eventsList.get(i))) {
                Event validEvent = eventsList.get(i);

                Polyline newestLine = mMap.addPolyline(new PolylineOptions().add(new LatLng(focusedEvent.getLatitude(), focusedEvent.getLongitude()),
                                new LatLng(validEvent.getLatitude(), validEvent.getLongitude())).color(model.getSettings().getFamilyColor())
                        .width(generation));
                lineList.add(newestLine);

                Person mother = model.getPeopleMap().get(currPerson.getMotherID());
                drawFamilyLines(mother, validEvent, generation / 2);
                return;
            }
        }
    }

}
