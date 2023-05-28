package com.example.familymap.Client;
import android.graphics.Color;
import java.util.ArrayList;

public class Settings {
    private boolean storyLines;
    private boolean familyLines;
    private boolean spouseLines;
    private int storyColor;  //not part of lab requirement-change line colors
    private int familyColor;
    private int spouseColor;
    private ArrayList<Integer> settingsSpinnerSelections;

    public Settings() {
        storyLines = true;
        familyLines = true;
        spouseLines = true;
        storyColor = Color.BLUE;
        familyColor = Color.GREEN;
        spouseColor = Color.MAGENTA;
        settingsSpinnerSelections = new ArrayList<>();
        while (settingsSpinnerSelections.size() != 4){
            settingsSpinnerSelections.add(0);
        }
    }

    public boolean isStoryLines()
    {
        return storyLines;
    }

    public void setStoryLines(boolean storyLines)
    {
        this.storyLines = storyLines;
    }

    public boolean isFamilyLines()
    {
        return familyLines;
    }

    public void setFamilyLines(boolean familyLines)
    {
        this.familyLines = familyLines;
    }

    public boolean isSpouseLines()
    {
        return spouseLines;
    }

    public void setSpouseLines(boolean spouseLines)
    {
        this.spouseLines = spouseLines;
    }

    public int getStoryColor()
    {
        return storyColor;
    }

    public void setStoryColor(int storyColor)
    {
        this.storyColor = storyColor;
    }

    public int getFamilyColor()
    {
        return familyColor;
    }

    public void setFamilyColor(int familyColor)
    {
        this.familyColor = familyColor;
    }

    public int getSpouseColor()
    {
        return spouseColor;
    }

    public void setSpouseColor(int spouseColor)
    {
        this.spouseColor = spouseColor;
    }

    public int getSettingsSpinnerSelections(int index) {
        return settingsSpinnerSelections.get(index);
    }

    public void setSpinner(int selection, int position) {
        settingsSpinnerSelections.set(position, selection);
    }
}
