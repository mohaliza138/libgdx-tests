package view.game.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class Map {

    //@JsonProperty
    private Cell[][] map;
    //@JsonProperty
    private ArrayList<int[]> empireCoordinates;
    //@JsonIgnore
    private int size;

    public Map(int size) {
        this.size = size;
        this.empireCoordinates = new ArrayList<>();
        map = new Cell[size][];
        for (int i = 0; i < size; i++) {
            map[i] = new Cell[size];
        }
        empireCoordinates.add(new int[]{20, 20});
        empireCoordinates.add(new int[]{80, 20});
        empireCoordinates.add(new int[]{80, 80});
        empireCoordinates.add(new int[]{20, 80});
    }

    public void setEmpireCoordinates(ArrayList<int[]> empireCoordinates) {
        this.empireCoordinates = empireCoordinates;
    }

    public void setSize(int size) {
        this.size = size;
    }
    public ArrayList<int[]> getEmpireCoordinates() {
        return empireCoordinates;
    }

    //@JsonProperty
    public int getSize() {
        return size;
    }

    public Cell[][] getMap() {
        return map;
    }

}