package view.game.Model;

import view.game.Enums.BuildingType;
import view.game.Controller.GameMenuController;

public class Building {
    private BuildingType buildingType;
    private Empire owner;
    private int hp;
    private int rate;
    private int freeCapacity;
    private String mode;//this is for the buildings like armourers that can produce different things
    private boolean isPassableForEnemies;

    public Building(BuildingType buildingType, Empire owner) {
        this.owner = owner;
        this.hp = buildingType.getHp();
        this.freeCapacity = buildingType.getCapacity();
        this.buildingType = buildingType;
        this.rate = buildingType.getRate() - owner.getFearRate();
        this.isPassableForEnemies = false;
        if (buildingType.getName().equals("Church") || buildingType.getName().equals("Cathedral")) {
            owner.addReligionPopularity(2);
        }
        if (buildingType.getName().equals("Hovel")) {
            owner.addMaxPopulation(8);
        }
        if (buildingType.getName().equals("Fletcher")) {
            this.mode = "bow";
        }
        if (buildingType.getName().equals("BlackSmith")) {
            this.mode = "sword";
        }
        if (buildingType.getName().equals("PoleTurner")) {
            this.mode = "spear";
        }
        if (buildingType.getName().equals("Armoury")) {
            GameMenuController.getCurrentEmpire().getArmoury().addFreeCapacityArmoury(50);
        }
        if (buildingType.getName().equals("Stable")) {
            GameMenuController.getCurrentEmpire().getArmoury().addHorse(4);
        }
        if (buildingType.getName().equals("FoodStock")) {
            GameMenuController.getCurrentEmpire().getFoodStock().addFreeCapacityFoodStock(250);
        }
        if (buildingType.getName().equals("Stockpile")) {
            GameMenuController.getCurrentEmpire().getResources().addFreeCapacityStockpile(190);
        }
        if (buildingType.getName().equals("Inn")) {
            GameMenuController.getCurrentEmpire().addAleCoverage(1);
        }
    }

    public Building(BuildingType buildingType) {
        this.buildingType = buildingType;
    }

    public Building() {
    }

    public boolean isIsPassableForEnemies() {
        return isPassableForEnemies;
    }

    public void setIsPassableForEnemies(boolean isPassableForEnemies) {
        this.isPassableForEnemies = isPassableForEnemies;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public BuildingType getBuildingType() {
        return buildingType;
    }

    public Empire getOwner() {
        return owner;
    }

    public int getFreeCapacity() {
        return freeCapacity;
    }

    public void setFreeCapacity(int freeCapacity) {
        this.freeCapacity = freeCapacity;
    }

    public void addFreeCapacity(int amount) {
        this.freeCapacity += amount;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void getDamage(int hp) {
        this.hp -= hp;
    }
}