package com.example.familymap.UI.Activities;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.familymap.Client.Model;
import com.example.familymap.R;
import com.example.familymap.UI.ActivityHelpers.SearchAdapter;

import java.util.ArrayList;
import java.util.Iterator;
import Model.Person;
import Model.Event;
import java.util.Map;


public class SearchActivity extends AppCompatActivity {
    private EditText mySearchEngine;
    private RecyclerView mSearchRecycler;
    private RecyclerView.Adapter mSearchAdapter;
    private Model model = Model.getModel();
    ArrayList<Person> persons = model.getSearchResultsPeople();
    ArrayList<Event> events = model.getSearchResultsEvents();

    //onCreate for SearchActivity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mySearchEngine = findViewById(R.id.search_text);
        mySearchEngine.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                generateSuggestions(s); //generates suggestions when something is typed in search box
                events=model.getSearchResultsEvents();
                persons=model.getSearchResultsPeople();
                displaySuggestions(); //lists those suggestions
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        mSearchRecycler = findViewById(R.id.list_search_recycler);
        mSearchRecycler.setLayoutManager(new LinearLayoutManager(this));

    }

    public void generateSuggestions(CharSequence s) { //refactored method that calls the Model's generate suggestion method
        model.generateSuggestions(s);
    }

    public void displaySuggestions() {
        System.out.println("events = " + events);
        if(events.size()==0 && persons.size()==0){
            mSearchAdapter = null;
            mSearchRecycler.setAdapter(mSearchAdapter);
        }
        if (events.size() != 0 || persons.size()!=0) {
            mSearchAdapter = new SearchAdapter(events,persons, this);
            mSearchRecycler.setAdapter(mSearchAdapter);
        }
    }

    //Overriding up Button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}