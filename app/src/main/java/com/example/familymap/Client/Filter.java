package com.example.familymap.Client;
import java.util.ArrayList;
import java.util.List;
public class Filter {
    private List<String> displayedEvents;
    private boolean fathersSide;
    private boolean mothersSide;
    private boolean males;
    private boolean females;

    public Filter(){
        displayedEvents = new ArrayList<>(Model.getModel().getEventTypes());
        fathersSide = true;
        mothersSide = true;
        males = true;
        females = true;
    }
    public List<String> getDisplayedEvents() {
        return displayedEvents;
    }
    public boolean isFathersSide()
    {
        return fathersSide;
    }

    public void setFathersSide(boolean fathersSide)
    {
        this.fathersSide = fathersSide;
    }

    public boolean isMothersSide()
    {
        return mothersSide;
    }

    public void setMothersSide(boolean mothersSide)
    {
        this.mothersSide = mothersSide;
    }

    public boolean isMales()
    {
        return males;
    }

    public void setMales(boolean males)
    {
        this.males = males;
    }

    public boolean isFemales()
    {
        return females;
    }

    public void setFemales(boolean females)
    {
        this.females = females;
    }

    public boolean containsEventType(String eventType) {
        eventType = eventType.toLowerCase();
        for (String event: displayedEvents) {
            if (event.toLowerCase().equals(eventType)){
                return true;
            }
        }
        return false;
    }

}
