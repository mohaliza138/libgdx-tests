package view.game.Model;

import java.util.ArrayList;


public class Cell {
    //@JsonProperty
    private Building building;
    //@JsonProperty
    private ArrayList<Unit> units;
    //@JsonProperty
    private String type;
    //@JsonProperty
    private String environmentName;
    private boolean isPoisoned;
    private int degreeOfFire;

    public Cell(String type) {
        this.building = null;
        this.units = new ArrayList<>();
        this.type = type;
        this.environmentName = null;
        this.isPoisoned = false;
        this.degreeOfFire = 0;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public ArrayList<Unit> getUnits() {
        return units;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEnvironmentName() {
        return environmentName;
    }

    public boolean isPoisoned() {
        return isPoisoned;
    }

    public void setPoisoned(boolean poisoned) {
        isPoisoned = poisoned;
    }

    public int getDegreeOfFire()
    {
        return this.degreeOfFire;
    }

    public void setDegreeOfFire(int fireDegree)
    {
        this.degreeOfFire = fireDegree;
    }

    public void decreaseDegreeOfFire()
    {
        if(this.degreeOfFire > 0)
            this.degreeOfFire--;
        else this.degreeOfFire = 0;
    }

    public void setEnvironmentName(String environmentName) {
        this.environmentName = environmentName;
    }

    //@JsonProperty
    public void addUnit(Unit unit) {
        units.add(unit);
    }

}