package view.game.Model;


import com.badlogic.gdx.graphics.g2d.Sprite;
import view.game.Controller.MoveController;
import view.game.Enums.UnitType;

import java.util.ArrayList;

public class Unit {
    private int i;
    private int j;
    private UnitType unitType;
    private Empire owner;
    private int hp;
    private String mode;
    private int attackPower;
    private ArrayList<MoveController.Pair<Integer, Integer>> path;
    private int currentCell;
    private boolean isPatrol;
    private Sprite sprite;
    public Unit(UnitType unitType, Empire owner, int i, int j) {
        path = new ArrayList<>();
        this.owner = owner;
        this.hp = unitType.getDefencePower();
        this.unitType = unitType;
        this.mode = "standing";
        this.attackPower = (int) (unitType.getAttackPower() * (1 + (owner.getFearRate() * 0.1)));
        isPatrol = false;
        currentCell = -1;
        this.i = i;
        this.j = j;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public int getJ() {
        return j;
    }

    public void setJ(int j) {
        this.j = j;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public ArrayList<MoveController.Pair<Integer, Integer>> getPath() {
        return path;
    }

    public void setPath(ArrayList<MoveController.Pair<Integer, Integer>> path) {
        this.path = path;
    }

    public int getCurrentCell() {
        return currentCell;
    }

    public void setCurrentCell(int currentCell) {
        this.currentCell = currentCell;
    }

    public boolean isPatrol() {
        return isPatrol;
    }

    public void setPatrol(boolean patrol) {
        isPatrol = patrol;
    }

    public int getHp() {
        return hp;
    }

    public void getDamage(int hp) {
        this.hp -= hp;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public UnitType getUnitType() {
        return unitType;
    }

    public Empire getOwner() {
        return owner;
    }

    public int getAttackPower() {
        return attackPower;
    }

    public void setAttackPower(int attackPower) {
        this.attackPower = attackPower;
    }
}