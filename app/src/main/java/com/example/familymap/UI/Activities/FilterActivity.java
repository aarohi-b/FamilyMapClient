package com.example.familymap.UI.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.familymap.Client.Model;
import com.example.familymap.R;

public class FilterActivity extends AppCompatActivity {
    private Switch lifeStoryLines;
    private Switch treeLines;
    private Switch spouseLines;
    private Switch femaleEvents;
    private Switch maleEvents;
    private Switch fatherSide;
    private Switch motherSide;
    private Spinner lifeSpinner; //The spinners are extra- to choose lines colors- not part of lab requirements
    private Spinner treeSpinner;
    private Spinner spouseSpinner;
    private TextView mLogout;

    private Model model = Model.getModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //check which lines are to be displayed
        lifeStoryLines = findViewById(R.id.life_story_switch);
        lifeStoryLines.setChecked(model.getSettings().isStoryLines());
        treeLines = findViewById(R.id.tree_switch);
        treeLines.setChecked(model.getSettings().isFamilyLines());
        spouseLines = findViewById(R.id.spouse_switch);
        spouseLines.setChecked(model.getSettings().isSpouseLines());
        maleEvents = findViewById(R.id.male_events_switch);
        maleEvents.setChecked(model.getFilter().isMales());
        femaleEvents = findViewById(R.id.female_events_switch);
        femaleEvents.setChecked(model.getFilter().isFemales());
        fatherSide = findViewById(R.id.father_side);
        fatherSide.setChecked(model.getFilter().isFathersSide());
        motherSide = findViewById(R.id.mother_side);
        motherSide.setChecked(model.getFilter().isMothersSide());

        lifeSpinner = findViewById(R.id.life_spinner);
        treeSpinner = findViewById(R.id.tree_spinner);
        spouseSpinner = findViewById(R.id.spouse_spinner);
        mLogout = findViewById(R.id.logout_text);
        mLogout.setLinksClickable(true);

        ArrayAdapter<CharSequence> storyColors = ArrayAdapter.createFromResource(this,
                R.array.life_story_colors, R.layout.support_simple_spinner_dropdown_item);
        lifeSpinner.setAdapter(storyColors);

        ArrayAdapter<CharSequence> spouseColors = ArrayAdapter.createFromResource(this,
                R.array.spouse_line_color, R.layout.support_simple_spinner_dropdown_item);
        spouseSpinner.setAdapter(spouseColors);

        ArrayAdapter<CharSequence> familyTreeColors = ArrayAdapter.createFromResource(this,
                R.array.family_tree_colors, R.layout.support_simple_spinner_dropdown_item);
        treeSpinner.setAdapter(familyTreeColors);

        lifeSpinner.setSelection(model.getSettings().getSettingsSpinnerSelections(0));
        treeSpinner.setSelection(model.getSettings().getSettingsSpinnerSelections(1));
        spouseSpinner.setSelection(model.getSettings().getSettingsSpinnerSelections(2));

        //Spinner Listeners for line colors
        lifeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        model.getSettings().setStoryColor(Color.BLACK);
                        model.getSettings().setSpinner(0, 0);
                        break;
                    case 1:
                        model.getSettings().setStoryColor(Color.BLUE);
                        model.getSettings().setSpinner(1, 0);
                        break;
                    case 2:
                        model.getSettings().setStoryColor(Color.MAGENTA);
                        model.getSettings().setSpinner(2, 0);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        treeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        model.getSettings().setFamilyColor(Color.YELLOW);
                        model.getSettings().setSpinner(0, 1);
                        break;
                    case 1:
                        model.getSettings().setFamilyColor(Color.CYAN);
                        model.getSettings().setSpinner(1, 1);
                        break;
                    case 2:
                        model.getSettings().setFamilyColor(Color.RED);
                        model.getSettings().setSpinner(2, 1);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spouseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        model.getSettings().setSpouseColor(Color.GREEN);
                        model.getSettings().setSpinner(0, 2);
                        break;
                    case 1:
                        model.getSettings().setSpouseColor(Color.DKGRAY);
                        model.getSettings().setSpinner(1, 2);
                        break;
                    case 2:
                        model.getSettings().setSpouseColor(Color.LTGRAY);
                        model.getSettings().setSpinner(2, 2);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //Line Switch Listeners
        lifeStoryLines.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                model.getSettings().setStoryLines(isChecked);
            }
        });

        treeLines.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                model.getSettings().setFamilyLines(isChecked);
            }
        });

        spouseLines.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                model.getSettings().setSpouseLines(isChecked);
            }
        });

        maleEvents.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                model.getFilter().setMales(isChecked);
            }
        });

        femaleEvents.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                model.getFilter().setFemales(isChecked);
            }
        });

        fatherSide.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                model.getFilter().setFathersSide(isChecked);
            }
        });

        motherSide.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                model.getFilter().setMothersSide(isChecked);
            }
        });

        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(intent, 0);
            }
        });
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

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}
