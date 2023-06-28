package view.game.Controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import view.game.Enums.BuildingType;
import view.game.Enums.UnitType;
import view.game.GameMenuGDX;
import view.game.Menu;
import view.game.Model.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;
import java.util.regex.Matcher;

public class GameMenuController {
    public static Map map = new Map(100);
    ;//zero base
    public static Game game;
    public static Empire currentEmpire;
    public static ArrayList<Unit> selectedUnits;
    public static HashMap<String, int[]> selectedCoordinates;//keys are "building" , "unit"
    public static Building selectedBuilding;
    private static int poisonNeed = 0;

    public GameMenuController() {
        selectedUnits = new ArrayList<>();
        selectedCoordinates = new HashMap<>();
    }

    public static Empire getCurrentEmpire() {
        return currentEmpire;
    }

    public static void setCurrentEmpire(Empire currentEmpire) {
        GameMenuController.currentEmpire = currentEmpire;
    }

    public static Game getGame() {
        return game;
    }

    public static void setGame(Game game) {
        GameMenuController.game = game;
    }

    public static Map getMap() {
        return map;
    }

    public static void setMap(Map map) {
        GameMenuController.map = map;
    }

    public static ArrayList<Cell> neighbors(int x, int y) {
        ArrayList<Cell> cells = new ArrayList<>();
        if (x > 0) {
            cells.add(map.getMap()[x - 1][y]);
        }
        if (x < map.getSize() - 1) {
            cells.add(map.getMap()[x + 1][y]);
        }
        if (y > 0) {
            cells.add(map.getMap()[x][y - 1]);
        }
        if (y < map.getSize() - 1) {
            cells.add(map.getMap()[x][y + 1]);
        }
        return cells;
    }

    public static String dropBuilding(int x, int y, String buildingName) {
        if (x > map.getSize() - 1 || x < 0 || y > map.getSize() - 1 || y < 0) {
            return "invalid coordinate";
        }
        if (map.getMap()[x][y].getBuilding() != null || map.getMap()[x][y].getUnits().size() > 0 || map.getMap()[x][y].getEnvironmentName() != null) {
            return "there are some other things in this place";
        }
        String cellType = map.getMap()[x][y].getType();
        if (BuildingType.getBuildingByName(buildingName).getType().equals("FarmBuildings")) {
            if (!cellType.equals("thickGrass") && !cellType.equals("oasisGrass")) {
                return "can't drop farm buildings in this place";
            }
        } else if (buildingName.equalsIgnoreCase("IronMine")) {
            if (!cellType.equals("ironTexture")) {
                return "IronMine must be built on IronTexture";
            }
        } else if (buildingName.equalsIgnoreCase("Quarry")) {
            if (!cellType.equals("boulder")) {
                return "Quarry must be built on Boulder";
            }
        } else if (buildingName.equalsIgnoreCase("PitchRig")) {
            if (!cellType.equals("oil")) {
                return "PitchRig must be built on Oil";
            }
        } else {//regular buildings
            if (!cellType.equals("earth") && !cellType.equals("earthAndStone") && !cellType.equals("scrub") && !cellType.equals("thickGrass") && !cellType.equals("oasisGrass") && !cellType.equals("beach")) {
                return "can't drop this building in this place";
            }
            if (buildingName.equalsIgnoreCase("DrawBridge")) {
                boolean bool = false;
                for (int i = 0; i < neighbors(x, y).size(); i++) {
                    if (neighbors(x, y).get(i).getBuilding() != null && (neighbors(x, y).get(i).getBuilding().getBuildingType().getName().equals("SmallStoneGatehouse") || neighbors(x, y).get(i).getBuilding().getBuildingType().getName().equals("BigStoneGatehouse"))) {
                        bool = true;
                    }
                }
                if (!bool) {
                    return "DrawBridge must be built near Gates";
                }
                if (!map.getMap()[x][y].getType().equals("moat")) {
                    return "DrawBridge must be built on moat";
                }
            }
            if (buildingName.equalsIgnoreCase("Armoury") && currentEmpire.haveThisBuilding(buildingName)) {
                boolean bool = false;
                for (int i = 0; i < neighbors(x, y).size(); i++) {
                    if (neighbors(x, y).get(i).getBuilding() != null && neighbors(x, y).get(i).getBuilding().getBuildingType().getName().equals("Armoury")) {
                        bool = true;
                    }
                }
                if (!bool) {
                    return "Armoury must be built near other Armouries";
                }
            }
            if (buildingName.equalsIgnoreCase("FoodStock") && currentEmpire.haveThisBuilding(buildingName)) {
                boolean bool = false;
                for (int i = 0; i < neighbors(x, y).size(); i++) {
                    if (neighbors(x, y).get(i).getBuilding() != null && neighbors(x, y).get(i).getBuilding().getBuildingType().getName().equals("FoodStock")) {
                        bool = true;
                    }
                }
                if (!bool) {
                    return "FoodStock must be built near other FoodStocks";
                }
            }
            if (buildingName.equalsIgnoreCase("Stockpile") && currentEmpire.haveThisBuilding(buildingName)) {
                boolean bool = false;
                for (int i = 0; i < neighbors(x, y).size(); i++) {
                    if (neighbors(x, y).get(i).getBuilding() != null && neighbors(x, y).get(i).getBuilding().getBuildingType().getName().equals("Stockpile")) {
                        bool = true;
                    }
                }
                if (!bool) {
                    return "Stockpile must be built near other Stockpiles";
                }
            }
        }
        if (BuildingType.getBuildingByName(buildingName).getGold() > currentEmpire.getResources().getGold()) {
            return "not enough gold for this building";
        }
        if (BuildingType.getBuildingByName(buildingName).getIron() > currentEmpire.getResources().getIron()) {
            return "not enough iron for this building";
        }
        if (BuildingType.getBuildingByName(buildingName).getWood() > currentEmpire.getResources().getWood()) {
            return "not enough wood for this building";
        }
        if (BuildingType.getBuildingByName(buildingName).getStone() > currentEmpire.getResources().getStone()) {
            return "not enough stone for this building";
        }
        if (currentEmpire.getUnemployedPeople() < BuildingType.getBuildingByName(buildingName).getWorkers()) {
            return "not enough worker for this building";
        }
        currentEmpire.addEmployedPeople(BuildingType.getBuildingByName(buildingName).getWorkers());
        currentEmpire.getResources().addGold(-1 * BuildingType.getBuildingByName(buildingName).getGold());
        currentEmpire.getResources().addResource("iron", -1 * BuildingType.getBuildingByName(buildingName).getIron());
        currentEmpire.getResources().addResource("wood", -1 * BuildingType.getBuildingByName(buildingName).getWood());
        currentEmpire.getResources().addResource("stone", -1 * BuildingType.getBuildingByName(buildingName).getStone());
        map.getMap()[x][y].setBuilding(new Building(BuildingType.getBuildingByName(buildingName), currentEmpire));
        currentEmpire.getBuildings().add(map.getMap()[x][y].getBuilding());
        return "success";
    }

    private static void checkDeadUnitsLocation(int x, int y) {
        ArrayList<Unit> forDelete = new ArrayList<>();
        for (int i = 0; i < map.getMap()[x][y].getUnits().size(); i++) {
            if (map.getMap()[x][y].getUnits().get(i).getHp() <= 0) {
                GameMenuGDX.units.remove(map.getMap()[x][y].getUnits().get(i).getSprite());
                forDelete.add(map.getMap()[x][y].getUnits().get(i));
            }
        }
        if (forDelete.size() == 0) {
            return;
        }
        for (Unit unit : forDelete) {
            map.getMap()[x][y].getUnits().remove(unit);
            if (unit.getOwner() != null) {
                unit.getOwner().getUnits().remove(unit);
            }
        }
    }

    public static String dropUnit(int x, int y, String type) {
        int count = 1;
        if (x > map.getSize() - 1 || x < 0 || y > map.getSize() - 1 || y < 0) {
            return "invalid coordinate";
        }
        if (map.getMap()[x][y].getBuilding() != null || map.getMap()[x][y].getEnvironmentName() != null) {
            return "there are some other things in this place";
        }
        if (count > currentEmpire.getUnemployedPeople()) {
            return "not enough unemployed people";
        }
        if (currentEmpire.getResources().getGold() < UnitType.getUnitByName(type).getCost() * count) {
            return "not enough gold";
        }
        if (type.equals("Archer")) {
            if (currentEmpire.getArmoury().getBow() < count) {
                return "weapons needed";
            }
            currentEmpire.getArmoury().addArmoury("bow", -1 * count);
        } else if (type.equals("Crossbowman")) {
            if (currentEmpire.getArmoury().getLeatherArmor() < count || currentEmpire.getArmoury().getCrossbow() < count) {
                return "weapons needed";
            }
            currentEmpire.getArmoury().addArmoury("crossbow", -1 * count);
            currentEmpire.getArmoury().addArmoury("leatherArmor", -1 * count);
        } else if (type.equals("Spearman")) {
            if (currentEmpire.getArmoury().getSpear() < count) {
                return "weapons needed";
            }
            currentEmpire.getArmoury().addArmoury("spear", -1 * count);
        } else if (type.equals("Pikeman")) {
            if (currentEmpire.getArmoury().getPike() < count || currentEmpire.getArmoury().getMetalArmor() < count) {
                return "weapons needed";
            }
            currentEmpire.getArmoury().addArmoury("pike", -1 * count);
            currentEmpire.getArmoury().addArmoury("metalArmor", -1 * count);
        } else if (type.equals("Maceman")) {
            if (currentEmpire.getArmoury().getMace() < count || currentEmpire.getArmoury().getLeatherArmor() < count) {
                return "weapons needed";
            }
            currentEmpire.getArmoury().addArmoury("mace", -1 * count);
            currentEmpire.getArmoury().addArmoury("leatherArmor", -1 * count);
        } else if (type.equals("Swordsman")) {
            if (currentEmpire.getArmoury().getSword() < count || currentEmpire.getArmoury().getMetalArmor() < count) {
                return "weapons needed";
            }
            currentEmpire.getArmoury().addArmoury("sword", -1 * count);
            currentEmpire.getArmoury().addArmoury("metalArmor", -1 * count);
        } else if (type.equals("Knight")) {
            if (currentEmpire.getArmoury().getSword() < count || currentEmpire.getArmoury().getMetalArmor() < count || currentEmpire.getArmoury().getHorse() < count) {
                return "weapons needed";
            }
            currentEmpire.getArmoury().addArmoury("sword", -1 * count);
            currentEmpire.getArmoury().addArmoury("metalArmor", -1 * count);
            currentEmpire.getArmoury().addHorse(-1 * count);
        }
        currentEmpire.addUnemployedPeople(-1);
        currentEmpire.getResources().addResource("gold", UnitType.getUnitByName(type).getCost() * count * -1);
        Unit unit = new Unit(UnitType.getUnitByName(type), currentEmpire, x, y);
        map.getMap()[x][y].getUnits().add(unit);
        return "success";
    }

    private static void attackNextTurnByMode(int x, int y, Unit unit) {
        if (!unit.getOwner().equals(currentEmpire)) {
            return;
        }
        int damage = unit.getAttackPower();
        if (unit.getUnitType().getType().equals("Archer")) {
            int attackRange = unit.getUnitType().getAttackRange();
            for (int i = x - attackRange; i < attackRange + x; i++) {
                for (int j = y - attackRange; j < y + attackRange; j++) {
                    if (j > map.getSize() - 1 || j < 0) {
                        continue;
                    }
                    if (i > map.getSize() - 1 || i < 0) {
                        break;
                    }
                    for (int k = 0; k < map.getMap()[i][j].getUnits().size(); k++) {
                        if (map.getMap()[i][j].getUnits().get(k).getOwner() == null || !map.getMap()[i][j].getUnits().get(k).getOwner().equals(currentEmpire)) {
                            if (map.getMap()[i][j].getUnits().get(k).getUnitType().equals(UnitType.ASSASSIN) && (x != i || y != j)) {
                                continue;
                            }
                            map.getMap()[i][j].getUnits().get(k).getDamage(damage);
                        }
                    }
                    checkDeadUnitsLocation(i, j);
                }
            }
        } else if (unit.getUnitType().getType().equals("Sword")) {
            if (unit.getMode().equals("standing")) {//just if they are in the same cell
                for (int i = 0; i < map.getMap()[x][y].getUnits().size(); i++) {
                    if (map.getMap()[x][y].getUnits().get(i).getOwner() == null || !map.getMap()[x][y].getUnits().get(i).getOwner().equals(currentEmpire)) {
                        map.getMap()[x][y].getUnits().get(i).getDamage(damage);
                    }
                }
            } else {
                int attackRange = 0;
                if (unit.getMode().equals("offensive")) {
                    attackRange = unit.getUnitType().getSpeed() + 2;
                } else if (unit.getMode().equals("defensive")) {
                    attackRange = unit.getUnitType().getSpeed();
                }
                for (int i = x - attackRange; i < attackRange + x; i++) {
                    for (int j = y - attackRange; j < y + attackRange; j++) {
                        if (j > map.getSize() - 1 || j < 0) {
                            continue;
                        }
                        if (i > map.getSize() - 1 || i < 0) {
                            break;
                        }
                        for (int k = 0; k < map.getMap()[i][j].getUnits().size(); k++) {
                            if (!map.getMap()[i][j].getUnits().get(k).getOwner().equals(currentEmpire)) {
//                                if (map.getMap()[i][j].getUnits().get(k).getUnitType().equals(UnitType.ASSASSIN) && (x != i || y != j)) {
//                                    continue;
//                                }
                                map.getMap()[i][j].getUnits().get(k).getDamage(damage);
                            }
                        }
                        checkDeadUnitsLocation(i, j);
                    }
                }
            }
        } else if (unit.getUnitType().equals(UnitType.ENGINEER_WITH_OIL)) {
            int neededEnemyForAttack = 0;
//                    = switch (unit.getMode()) {
//                case "offensive" -> 1;
//                case "standing" -> 2;
//                case "defensive" -> 3;
//                default -> 0;
//            };
            for (int i = 0; i < neighbors(x, y).size(); i++) {
                for (int j = 0; j < neighbors(x, y).get(i).getUnits().size(); j++) {
                    if (neighbors(x, y).get(i).getUnits().get(j).getOwner() != currentEmpire && !neighbors(x, y).get(i).getUnits().get(j).getUnitType().equals(UnitType.ASSASSIN)) {
                        neededEnemyForAttack--;
                    }
                }
            }
            if (neededEnemyForAttack <= 0) {
                for (int i = 0; i < neighbors(x, y).size(); i++) {
                    for (int j = 0; j < neighbors(x, y).get(i).getUnits().size(); j++) {
                        if (neighbors(x, y).get(i).getUnits().get(j).getOwner() != currentEmpire) {
                            neighbors(x, y).get(i).getUnits().get(j).getDamage(damage);
                        }
                    }
                }
                boolean bool = false;
                for (int i = 0; i < currentEmpire.getBuildings().size(); i++) {
                    if (currentEmpire.getBuildings().get(i).getBuildingType().equals(BuildingType.OIL_SMELTER)) {
                        bool = true;
                        break;
                    }
                }
                if (!(bool && currentEmpire.getResources().getPitch() > 0)) {
                    map.getMap()[x][y].getUnits().remove(unit);
                    currentEmpire.getUnits().remove(unit);
                    map.getMap()[x][y].getUnits().add(new Unit(UnitType.ENGINEER, currentEmpire, x, y));
                    currentEmpire.getUnits().add(map.getMap()[x][y].getUnits().get(map.getMap()[x][y].getUnits().size() - 1));
                } else {
                    currentEmpire.getResources().addResource("pitch", -1);
                }
            }
        }
        checkDeadUnitsLocation(x, y);
    }

    public static void removeSelect(int x, int y) {
        ArrayList<Unit> units = new ArrayList<>();
        for (int i = 0; i < map.getMap()[x][y].getUnits().size(); i++) {
            if (selectedUnits.contains(map.getMap()[x][y].getUnits().get(i))) {
                units.add(map.getMap()[x][y].getUnits().get(i));
            }
        }
        for (int i = 0; i < units.size(); i++) {
            selectedUnits.remove(units.get(i));
        }
    }

    public static String selectUnit(int x, int y) {
        selectedCoordinates.put("unit", new int[]{x, y});
        for (int i = 0; i < map.getMap()[x][y].getUnits().size(); i++) {
            if (map.getMap()[x][y].getUnits().get(i).getOwner() != null && map.getMap()[x][y].getUnits().get(i).getOwner().equals(currentEmpire)) {
                selectedUnits.add(map.getMap()[x][y].getUnits().get(i));
            }
        }
        return "success";
    }

    public static void setUnitMode(String mode) {
        for (Unit selectedUnit : selectedUnits) {
            selectedUnit.setMode(mode);
        }
    }

    private static void checkDestroyedBuildingLocation(int x, int y) {
        if (map.getMap()[x][y].getBuilding() == null) {
            return;
        }
        if (map.getMap()[x][y].getBuilding().getHp() <= 0) {
            if (map.getMap()[x][y].getBuilding().getOwner() != null) {
                map.getMap()[x][y].getBuilding().getOwner().getBuildings().remove(map.getMap()[x][y].getBuilding());
                map.getMap()[x][y].getBuilding().getOwner().addEmployedPeople(-1 * map.getMap()[x][y].getBuilding().getBuildingType().getWorkers());
                if (map.getMap()[x][y].getBuilding().getBuildingType().equals(BuildingType.CHURCH) || map.getMap()[x][y].getBuilding().getBuildingType().equals(BuildingType.CATHEDRAL)) {
                    map.getMap()[x][y].getBuilding().getOwner().addReligionPopularity(-2);
                } else if (map.getMap()[x][y].getBuilding().getBuildingType().equals(BuildingType.INN)) {
                    map.getMap()[x][y].getBuilding().getOwner().addAleCoverage(-1);
                } else if (map.getMap()[x][y].getBuilding().getBuildingType().equals(BuildingType.HOVEL)) {
                    map.getMap()[x][y].getBuilding().getOwner().addMaxPopulation(-8);
                } else if (map.getMap()[x][y].getBuilding().getBuildingType().equals(BuildingType.ARMOURY)) {
                    map.getMap()[x][y].getBuilding().getOwner().getArmoury().addFreeCapacityArmoury(-50);
                } else if (map.getMap()[x][y].getBuilding().getBuildingType().equals(BuildingType.FOOD_STOCK)) {
                    map.getMap()[x][y].getBuilding().getOwner().getFoodStock().addFreeCapacityFoodStock(-250);
                } else if (map.getMap()[x][y].getBuilding().getBuildingType().equals(BuildingType.STOCKPILE)) {
                    map.getMap()[x][y].getBuilding().getOwner().getResources().addFreeCapacityStockpile(-190);
                }
            }
            map.getMap()[x][y].setBuilding(null);
            TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("Tiles.atlas"));
            GameMenuGDX.sprites[x][y] = new Sprite(textureAtlas.findRegion("ground0"));
            GameMenuGDX.sprites[x][y].setPosition(GameMenuGDX.getXFromIAndJ(x, y), GameMenuGDX.getYFromIAndJ(x, y));
        }
    }

    public static void checkFoodProductiveBuildings() {// each building produces if there is enough free space in the foodStock
        for (int i = 0; i < currentEmpire.getBuildings().size(); i++) {
            String name = currentEmpire.getBuildings().get(i).getBuildingType().getName();
            if (!name.equals("AppleOrchard") && !name.equals("HuntingPost") && !name.equals("Bakery") && !name.equals("DairyFarm")) {
                continue;
            }
            int rate = currentEmpire.getBuildings().get(i).getRate();
            int productEachRate = currentEmpire.getBuildings().get(i).getBuildingType().getCapacity();
            if (currentEmpire.getFoodStock().getFreeCapacityFoodStock() < productEachRate * rate) {
                continue;//Not Enough space in the FoodStock
            }
            if (name.equals("AppleOrchard")) {
                currentEmpire.getFoodStock().addFood("apple", productEachRate * rate);
            } else if (name.equals("HuntingPost")) {
                currentEmpire.getFoodStock().addFood("meat", productEachRate * rate);
            } else if (name.equals("Bakery")) {
                if (currentEmpire.getResources().getFlour() >= rate) {
                    currentEmpire.getResources().addResource("flour", -1 * rate);
                    currentEmpire.getFoodStock().addFood("bread", productEachRate * rate);
                }
            } else {
                currentEmpire.getFoodStock().addFood("cheese", productEachRate * rate);
            }
        }
    }

    public static void checkArmourProductiveBuildings() {// each armour producer , produce 1 item in 1 turn
        for (int i = 0; i < currentEmpire.getBuildings().size(); i++) {
            if (!currentEmpire.getBuildings().get(i).getBuildingType().getType().equals("Weapon")) {
                continue;
            }
            int rate = currentEmpire.getBuildings().get(i).getRate();
            if (currentEmpire.getArmoury().getFreeCapacityArmoury() < rate) {
                continue;
            }
            if (currentEmpire.getBuildings().get(i).getBuildingType().getName().equals("Fletcher")) {
                if (currentEmpire.getResources().getWood() >= rate) {
                    currentEmpire.getResources().addResource("wood", -rate);
                    if (currentEmpire.getBuildings().get(i).getMode().equals("bow")) {
                        currentEmpire.getArmoury().addArmoury("bow", rate);
                    } else {
                        currentEmpire.getArmoury().addArmoury("crossbow", rate);
                    }
                }
            } else if (currentEmpire.getBuildings().get(i).getBuildingType().getName().equals("DairyFarm")) {
                currentEmpire.getArmoury().addArmoury("leatherArmor", rate);
            } else if (currentEmpire.getBuildings().get(i).getBuildingType().getName().equals("BlackSmith")) {
                if (currentEmpire.getResources().getIron() >= rate) {
                    currentEmpire.getResources().addResource("iron", -rate);
                    if (currentEmpire.getBuildings().get(i).getMode().equals("sword")) {
                        currentEmpire.getArmoury().addArmoury("sword", rate);
                    } else {
                        currentEmpire.getArmoury().addArmoury("mace", rate);
                    }
                }
            } else if (currentEmpire.getBuildings().get(i).getBuildingType().getName().equals("PoleTurner")) {
                if (currentEmpire.getResources().getWood() >= rate) {
                    currentEmpire.getResources().addResource("wood", -rate);
                    if (currentEmpire.getBuildings().get(i).getMode().equals("spear")) {
                        currentEmpire.getArmoury().addArmoury("spear", rate);
                    } else {
                        currentEmpire.getArmoury().addArmoury("pike", rate);
                    }
                }
            } else if (currentEmpire.getBuildings().get(i).getBuildingType().getName().equals("Armourer")) {
                if (currentEmpire.getResources().getIron() >= rate) {
                    currentEmpire.getResources().addResource("iron", -rate);
                    currentEmpire.getArmoury().addArmoury("metalArmor", rate);
                }
            }
        }
    }

    public static void checkResourceProductiveBuildings() {
        int oxTethers = 0;
        int quarry = 0;
        for (int i = 0; i < currentEmpire.getBuildings().size(); i++) {
            int rate = currentEmpire.getBuildings().get(i).getRate();
            int productEachRate = currentEmpire.getBuildings().get(i).getBuildingType().getCapacity();
            if (currentEmpire.getBuildings().get(i).getBuildingType().getName().equals("Mill")) {
                if (currentEmpire.getResources().getWheat() < rate) {
                    continue;
                }
                if (currentEmpire.getResources().getFreeCapacityStockpile() < productEachRate * (rate - 1)) {//(rate-1) because we use rate amount of resources from stockpile
                    continue;
                }
                currentEmpire.getResources().addResource("flour", productEachRate * rate);
                currentEmpire.getResources().addResource("wheat", -1 * rate);
            } else if (currentEmpire.getBuildings().get(i).getBuildingType().getName().equals("IronMine")) {
                if (currentEmpire.getResources().getFreeCapacityStockpile() < productEachRate * rate) {
                    continue;
                }
                currentEmpire.getResources().addResource("iron", productEachRate * rate);
            } else if (currentEmpire.getBuildings().get(i).getBuildingType().getName().equals("PitchRig")) {
                if (currentEmpire.getResources().getFreeCapacityStockpile() < productEachRate * rate) {
                    continue;
                }
                currentEmpire.getResources().addResource("pitch", productEachRate * rate);
            } else if (currentEmpire.getBuildings().get(i).getBuildingType().getName().equals("Woodcutter")) {
                if (currentEmpire.getResources().getFreeCapacityStockpile() < productEachRate * rate) {
                    continue;
                }
                currentEmpire.getResources().addResource("wood", productEachRate * rate);
            } else if (currentEmpire.getBuildings().get(i).getBuildingType().getName().equals("HopsFarm")) {
                if (currentEmpire.getResources().getFreeCapacityStockpile() < productEachRate * rate) {
                    continue;
                }
                currentEmpire.getResources().addResource("hop", productEachRate * rate);
            } else if (currentEmpire.getBuildings().get(i).getBuildingType().getName().equals("WheatFarm")) {
                if (currentEmpire.getResources().getFreeCapacityStockpile() < productEachRate * rate) {
                    continue;
                }
                currentEmpire.getResources().addResource("wheat", productEachRate * rate);
            } else if (currentEmpire.getBuildings().get(i).getBuildingType().getName().equals("Brewery")) {
                if (currentEmpire.getResources().getHop() < rate * productEachRate) {
                    continue;
                }
                if (currentEmpire.getResources().getFreeCapacityStockpile() < productEachRate * (rate - 1)) {
                    continue;
                }
                currentEmpire.getResources().addResource("ale", productEachRate * rate);
                currentEmpire.getResources().addResource("hop", -1 * rate);
            } else if (currentEmpire.getBuildings().get(i).getBuildingType().getName().equals("Quarry")) {
                quarry++;
            } else if (currentEmpire.getBuildings().get(i).getBuildingType().getName().equals("oxTether")) {
                oxTethers++;
            }
        }
        int productOfAllQuarries = quarry * BuildingType.QUARRY.getRate() * BuildingType.QUARRY.getCapacity();
        int transitAllOxTethers = oxTethers * BuildingType.OX_TETHER.getCapacity() * BuildingType.OX_TETHER.getRate();
        if (transitAllOxTethers > productOfAllQuarries) {
            transitAllOxTethers = productOfAllQuarries;
        }
        if (transitAllOxTethers > currentEmpire.getResources().getFreeCapacityStockpile()) {
            transitAllOxTethers = currentEmpire.getResources().getFreeCapacityStockpile();
        }
        currentEmpire.getResources().addResource("stone", transitAllOxTethers);
    }

    private static String checkEndOfTheGame() throws IOException {
        int[] scores = new int[game.getEmpires().size()];
        for (int i = 0; i < game.getEmpires().size(); i++) {
            for (int j = 0; j < game.getEmpires().get(i).getBuildings().size(); j++) {
                if (game.getEmpires().get(i).getBuildings().get(j).getBuildingType().equals(BuildingType.KEEP) || game.getEmpires().get(i).getBuildings().get(j).getBuildingType().equals(BuildingType.KILLING_PIT)) {
                    continue;
                }
                setNewScoresInJsonFile(game.getEmpires().get(i), game.getEmpires().get(i).getBuildings().get(j).getHp());
                scores[i] += game.getEmpires().get(i).getBuildings().get(j).getHp();
            }
            for (int k = 0; k < game.getEmpires().get(i).getUnits().size(); k++) {
                setNewScoresInJsonFile(game.getEmpires().get(i), game.getEmpires().get(i).getUnits().get(k).getHp());
                scores[i] += game.getEmpires().get(i).getUnits().get(k).getHp();
            }
        }
        for (int i = 0; i < scores.length; i++) {
            game.getEmpires().get(i).getUser().addScore(scores[i]);
        }
        StringBuilder output = new StringBuilder();
        output.append("scores :").append("\n");
        for (int i = 0; i < scores.length; i++) {
            output.append(game.getEmpires().get(i).getUser().getUsername()).append(" : ").append(scores[i]).append("\n");
        }
        return output.toString();
    }

    private static void setNewScoresInJsonFile(Empire empire, int numberOfIncrease) throws IOException {
        //TODO add Json
//        String username = empire.getUser().getUsername();
//
//        // read the existing contents of the users.json file into a JSONArray
//        String jsonString = new String(Files.readAllBytes(Paths.get("users.json")));
//        JSONArray usersArray = new JSONArray(jsonString);
//
//        // find the index of the JSONObject for the updated User
//        int userIndex = -1;
//        for (int i = 0; i < usersArray.length(); i++) {
//            JSONObject jsonObject = usersArray.getJSONObject(i);
//            if (jsonObject.getString("username").equals(username)) {
//                userIndex = i;
//                break;
//            }
//        }
//
//        // update the specified user field
//        if (userIndex != -1) {
//
//            empire.getUser().setScore(empire.getUser().getScore() + numberOfIncrease);
//
//            JSONObject updatedUser = new JSONObject(empire.getUser());
//            usersArray.put(userIndex, updatedUser);
//        }
//
//        // write the updated contents of the JSONArray back to the users.json file
//        Files.write(Paths.get("users.json"), usersArray.toString().getBytes());

    }

    public static String nextTurn() throws IOException {
        selectedUnits.clear();
        selectedBuilding = null;
        if (poisonNeed % 2 == 0) {
            poisonACell();
        }
        GameMenuController.getCurrentEmpire().addFrom100(EmpireMenuController.sumOfPop());
        poisonNeed++;
        EmpireMenuController.checkEffectOfFearRate();
        setWaterPotEffectForCurrentEmpire();
        burnBuildingsOfCurrentEmpire();
        checkFoodProductiveBuildings();
        checkArmourProductiveBuildings();
        checkResourceProductiveBuildings();
        EmpireMenuController.calculateFoodAndTax();
        EmpireMenuController.calculatePopularityFactors();
        EmpireMenuController.calculatePopulation();
        int id = currentEmpire.getEmpireId();
        if (id == game.getEmpires().size() - 1) {
            currentEmpire = game.getEmpires().get(0);
            game.setTurnsCounter(game.getTurnsCounter() - 1);
        } else {
            currentEmpire = game.getEmpires().get(id + 1);
        }
        if (game.getTurnsCounter() == 0) {
            return "end of the game" + '\n' + checkEndOfTheGame();
        }
        moveUnits();
        for (int i = 0; i < map.getSize(); i++) {
            for (int j = 0; j < map.getSize(); j++) {
                checkDeadUnitsLocation(i, j);
                checkDestroyedBuildingLocation(i, j);
                for (int k = 0; k < map.getMap()[i][j].getUnits().size(); k++) {
                    attackNextTurnByMode(i, j, map.getMap()[i][j].getUnits().get(k));
                }
            }
        }
        Menu.scribeReport();
        return currentEmpire.getUser().getUsername() + " is now playing";
    }

    public static void poisonACell() {
        Random random = new Random();

        int cellI = random.nextInt(0, 100);
        int cellJ = random.nextInt(0, 100);

        map.getMap()[cellI][cellJ].setPoisoned(true);
        GameMenuGDX.poisonCoordiantes.add(new int[]{cellI, cellJ});
        TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("disease.atlas"));
        Sprite sprite = new Sprite(textureAtlas.findRegion("disease10"));
        sprite.setPosition(((cellJ + cellI) * 15) - (GameMenuGDX.MAP_WIDTH / 2), (cellJ - cellI) * 8 - 8);
        GameMenuGDX.poison.add(sprite);
        Empire empire = thePoisonedCellBelongsToWhichEmpire(cellI, cellJ);

        empire.addDisaffectionFromPoisonedCells(1);

        boolean thisEmpireHasApothecary = false;
        for (Building building : currentEmpire.getBuildings()) {
            if (Objects.equals(building.getBuildingType().getName(), "Apothecary")) {
                thisEmpireHasApothecary = true;
                break;
            }
        }

        if (thisEmpireHasApothecary) {
            int numberOfCellsTheApothecaryHasCured = 0;
            empire.addDisaffectionFromPoisonedCells(-2);
            for (int i = 0; i < map.getSize(); i++) {
                for (int j = 0; j < map.getSize(); j++) {
                    if (map.getMap()[i][j].isPoisoned() && thePoisonedCellBelongsToWhichEmpire(i, j) == currentEmpire) {
                        map.getMap()[i][j].setPoisoned(false);
                        for (int k = 0; k < GameMenuGDX.poison.size(); k++) {
                            if (GameMenuGDX.poisonCoordiantes.get(k)[0] == i && GameMenuGDX.poisonCoordiantes.get(k)[1] == j) {
                                GameMenuGDX.poison.remove(k);
                                GameMenuGDX.poisonCoordiantes.remove(k);
                                break;
                            }
                        }
                        numberOfCellsTheApothecaryHasCured++;
                        if (numberOfCellsTheApothecaryHasCured == 2)
                            return;
                    }
                }
            }
        }
    }

    public static Empire thePoisonedCellBelongsToWhichEmpire(int i, int j) {
        int min = 1000000;
        Empire output = null;
        for (Empire empire : Game.getEmpires()) {
            int distance = (empire.getKeepCoordinates()[0] - i) * (empire.getKeepCoordinates()[0] - i) + (empire.getKeepCoordinates()[1] - j) * (empire.getKeepCoordinates()[0] - j);
            if (distance < min) {
                min = distance;
                output = empire;
            }
        }
        return output;
    }


    public static void moveUnits() {
        for (Unit unit : currentEmpire.getUnits()) {
            if (!unit.isPatrol()) {
                if (unit.getCurrentCell() != -1) {
                    ArrayList<MoveController.Pair<Integer, Integer>> path = unit.getPath();
                    ArrayList<MoveController.Pair<Integer, Integer>> revPath = new ArrayList<>();
                    for (int i = path.size() - 1; i >= 0; i--) {
                        revPath.add(path.get(i));
                    }
                    map.getMap()[revPath.get(unit.getCurrentCell()).getObject1()][revPath.get(unit.getCurrentCell()).getObject2()].getUnits().remove(unit);
                    int raise = unit.getCurrentCell() + unit.getUnitType().getSpeed();
                    if (raise < unit.getPath().size() - 1) unit.setCurrentCell(raise);
                    else {
                        raise = unit.getPath().size() - 1;
                        unit.setCurrentCell(unit.getPath().size() - 1);
                    }
                    map.getMap()[revPath.get(unit.getCurrentCell()).getObject1()][revPath.get(unit.getCurrentCell()).getObject2()].getUnits().add(unit);
                    if (raise == unit.getPath().size() - 1) {
                        unit.setCurrentCell(-1);
                        unit.getPath().clear();
                    }
                }
            } else {
                if (!unit.isPatrol()) return;
                ArrayList<MoveController.Pair<Integer, Integer>> path = unit.getPath();
                ArrayList<MoveController.Pair<Integer, Integer>> revPath = new ArrayList<>();
                for (int i = path.size() - 1; i >= 0; i--) {
                    revPath.add(path.get(i));
                }
                if (unit.getCurrentCell() != -1) {
                    map.getMap()[path.get(unit.getCurrentCell()).getObject1()][path.get(unit.getCurrentCell()).getObject2()].getUnits().remove(unit);
                    int raise = unit.getCurrentCell() + unit.getUnitType().getSpeed();
                    if (raise < unit.getPath().size() - 1) unit.setCurrentCell(raise);
                    else {
                        raise = unit.getPath().size() - 1;
                        unit.setCurrentCell(unit.getPath().size() - 1);
                    }
                    map.getMap()[path.get(unit.getCurrentCell()).getObject1()][path.get(unit.getCurrentCell()).getObject2()].getUnits().add(unit);
                    if (raise == unit.getPath().size() - 1) {
                        unit.setCurrentCell(0);
                        unit.setPath(revPath);
                    }
                }
            }
        }
    }

    public static boolean isPassable(int x , int y){
        if(map.getMap()[x][y].getType().equals("moat") || map.getMap()[x][y].getType().equals("sea") ||map.getMap()[x][y].getType().equals("largePond") ||map.getMap()[x][y].getType().equals("lowDepthWater") || map.getMap()[x][y].getType().equals("rockTexture")){
            return false;
        }
        if(map.getMap()[x][y].getBuilding()!= null && !map.getMap()[x][y].getBuilding().getBuildingType().equals(BuildingType.LOOKOUT_TOWER)&& !map.getMap()[x][y].getBuilding().getBuildingType().equals(BuildingType.PERIMETER_TOWER)&& !map.getMap()[x][y].getBuilding().getBuildingType().equals(BuildingType.ROUND_TOWER)&& !map.getMap()[x][y].getBuilding().getBuildingType().equals(BuildingType.SQUARE_TOWER)&& !map.getMap()[x][y].getBuilding().getBuildingType().equals(BuildingType.BIG_STONE_GATEHOUSE)&& !map.getMap()[x][y].getBuilding().getBuildingType().equals(BuildingType.SMALL_STONE_GATEHOUSE)){
           return false;
        }
        return true;
    }

    public static String attackEnemy(int x, int y) {//archers will stay and give damage them and other will go and damage one of the enemies randomly
        if (selectedUnits.size() == 0) {
            return "you should select a unit first";
        }
        int distance = distance(x, y, selectedCoordinates.get("unit")[0], selectedCoordinates.get("unit")[1]);
        for (Unit selectedUnit : selectedUnits) {
            int damage = selectedUnit.getUnitType().getAttackPower();
            if (selectedUnit.getUnitType().equals(UnitType.LADDER_MAN) && map.getMap()[x][y].getBuilding() != null) {
                if (distance <= selectedUnit.getUnitType().getSpeed()) {
                    String name = map.getMap()[x][y].getBuilding().getBuildingType().getName();
                    if (name.equals("ShortWall") || name.equals("TallWall")) {
                        if (map.getMap()[x][y].getBuilding().getOwner() != null && !map.getMap()[x][y].getBuilding().getOwner().equals(currentEmpire)) {
                            map.getMap()[x][y].getBuilding().setIsPassableForEnemies(true);
                        }
                    }
                }
            } else if (selectedUnit.getUnitType().getType().equals("Sword")) {
                MoveController moveController = new MoveController();
                MoveController.Pair<Integer, Integer> src = new MoveController.Pair<>(x, y);
                MoveController.Pair<Integer, Integer> dest = new MoveController.Pair<>(selectedCoordinates.get("unit")[0], selectedCoordinates.get("unit")[1]);
                ArrayList<Unit> units = new ArrayList<>();
                units.add(selectedUnit);
                if (moveController.aStarSearch(src, dest, units).equals("Success") && distance <= selectedUnit.getUnitType().getSpeed()) {
                    if (map.getMap()[x][y].getBuilding() != null && !map.getMap()[x][y].getBuilding().getOwner().equals(currentEmpire)) {
                        if (selectedUnit.getUnitType().getName().equals("Slaves")) {
                            GameMenuGDX.fireCoordinates.add(new int[]{x, y});
                            TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("fire.atlas"));
                            Sprite sprite = new Sprite(textureAtlas.findRegion("fire10"));
                            sprite.setPosition(((y + x) * 15) - (GameMenuGDX.MAP_WIDTH / 2), (y - x) * 8 - 8);
                            GameMenuGDX.fire.add(sprite);
                            map.getMap()[x][y].setDegreeOfFire(3);
                            damage *= 2;
                        }
                        map.getMap()[x][y].getBuilding().getDamage(damage);
                        continue;
                    }
                    boolean bool = true;
                    if (map.getMap()[x][y].getUnits().size() == 0)
                        continue;

                    boolean check = false;
                    for (int i = 0; i < map.getMap()[x][y].getUnits().size(); i++) {
                        if (map.getMap()[x][y].getUnits().get(i).getOwner() != currentEmpire)
                            check = true;
                    }
                    if (!check)
                        continue;

                    while (bool) {
                        int index = (int) (Math.random() * map.getMap()[x][y].getUnits().size());
                        if (map.getMap()[x][y].getUnits().get(index).getOwner() != null && !map.getMap()[x][y].getUnits().get(index).getOwner().equals(currentEmpire)) {
                            map.getMap()[x][y].getUnits().get(index).getDamage(damage);
                            bool = false;
                        }
                    }
                }
            } else if (selectedUnit.getUnitType().getType().equals("Archer") && distance <= selectedUnit.getUnitType().getAttackRange()) {
                if (map.getMap()[x][y].getBuilding() != null && !map.getMap()[x][y].getBuilding().getOwner().equals(currentEmpire)) {
                    if (selectedUnit.getUnitType().getName().equals("FireThrower")) {
                        map.getMap()[x][y].setDegreeOfFire(3);
                        GameMenuGDX.fireCoordinates.add(new int[]{x, y});
                        TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("fire.atlas"));
                        Sprite sprite = new Sprite(textureAtlas.findRegion("fire10"));
                        sprite.setPosition(((y + x) * 15) - (GameMenuGDX.MAP_WIDTH / 2), (y - x) * 8 - 8);
                        GameMenuGDX.fire.add(sprite);
                        damage *= 2;
                    }
                    map.getMap()[x][y].getBuilding().getDamage(damage);
                    continue;
                }
                boolean bool = true;
                if (map.getMap()[x][y].getUnits().size() == 0)
                    continue;

                boolean check = false;
                for (int i = 0; i < map.getMap()[x][y].getUnits().size(); i++) {
                    if (map.getMap()[x][y].getUnits().get(i).getOwner() != currentEmpire)
                        check = true;
                }
                if (!check)
                    continue;

                while (bool) {
                    int index = (int) (Math.random() * map.getMap()[x][y].getUnits().size());
                    if (map.getMap()[x][y].getUnits().get(index).getOwner() != null && !map.getMap()[x][y].getUnits().get(index).getOwner().equals(currentEmpire)) {
                        for (int i = 0; i < map.getMap()[x][y].getUnits().size(); i++) {
                            if (map.getMap()[x][y].getUnits().get(i).getUnitType().equals(UnitType.PORTABLE_SHIELD) && map.getMap()[x][y].getUnits().get(index).getOwner().equals(map.getMap()[x][y].getUnits().get(i).getOwner())) {
                                map.getMap()[x][y].getUnits().get(i).getDamage(damage);
                                bool = false;
                                break;
                            }
                        }
                        if (!bool) {
                            break;
                        }
                        map.getMap()[x][y].getUnits().get(index).getDamage(damage);
                        bool = false;
                    }
                }
            }
            selectedUnit.getPath().clear();
            checkDeadUnitsLocation(x, y);
        }
        checkDestroyedBuildingLocation(x, y);
        return "cell " + x + "," + y + " is now under attack!!";
    }

    public static int distance(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    public static void setSelectedBuilding(Building selectedBuilding) {
        GameMenuController.selectedBuilding = selectedBuilding;
    }

    private static boolean enemyInThisCell(Cell cell) {
        for (int i = 0; i < cell.getUnits().size(); i++) {
            if (cell.getUnits().get(i).getOwner() == null) {
                continue;
            }
            if (!cell.getUnits().get(i).getOwner().equals(currentEmpire)) {
                return true;
            }
        }
        return false;
    }

    static boolean anyEnemyNear(int x, int y) {//3*3 map that x,y is in the center
        if (enemyInThisCell(map.getMap()[x][y])) return true;
        if (x > 0 && enemyInThisCell(map.getMap()[x - 1][y])) return true;
        if (y > 0 && enemyInThisCell(map.getMap()[x][y - 1])) return true;
        if (x < map.getSize() - 1 && enemyInThisCell(map.getMap()[x + 1][y])) return true;
        if (y < map.getSize() - 1 && enemyInThisCell(map.getMap()[x][y + 1])) return true;
        if (x > 0 && y > 0 && enemyInThisCell(map.getMap()[x - 1][y - 1])) return true;
        if (x > 0 && y < map.getSize() - 1 && enemyInThisCell(map.getMap()[x - 1][y + 1])) return true;
        if (y > 0 && x < map.getSize() - 1 && enemyInThisCell(map.getMap()[x + 1][y - 1])) return true;
        if (y < map.getSize() - 1 && x < map.getSize() - 1 && enemyInThisCell(map.getMap()[x + 1][y + 1])) return true;

        return false;
    }

    public static HashMap<String, int[]> getSelectedCoordinates() {
        return selectedCoordinates;
    }

    public static String repair() {
        if (selectedBuilding == null) {
            return "you should select a building first";
        }
        String name = selectedBuilding.getBuildingType().getName();
        if (!(name.equals("SmallStoneGatehouse") || name.equals("BigStoneGatehouse") || name.equals("LookoutTower") || name.equals("PerimeterTower") || name.equals("DefenciveTurret") || name.equals("SquareTower") || name.equals("RoundTower"))) {
            return "this building won't repair";
        }
        if (anyEnemyNear(selectedCoordinates.get("building")[0], selectedCoordinates.get("building")[1])) {
            return "you can't repair your building while enemies are here";
        }
        if (currentEmpire.getResources().getStone() < (Math.ceil(selectedBuilding.getBuildingType().getHp() - selectedBuilding.getHp()) / 500)) {
            return "not enough stone to repair this building";
        }
        currentEmpire.getResources().addResource("stone", -1 * (int) (Math.ceil(selectedBuilding.getBuildingType().getHp() - selectedBuilding.getHp()) / 500));
        selectedBuilding.setHp(selectedBuilding.getBuildingType().getHp());
        return "success";
    }

    public static String removeBuilding(int x, int y) {
        if (map.getMap()[x][y].getBuilding() == null) {
            return "there is no building in this place";
        } else if (!map.getMap()[x][y].getBuilding().getOwner().equals(currentEmpire)) {
            return "this building isn't yours";
        } else {
            if (map.getMap()[x][y].getBuilding().getOwner() != null) {
                map.getMap()[x][y].getBuilding().getOwner().getBuildings().remove(map.getMap()[x][y].getBuilding());
                map.getMap()[x][y].getBuilding().getOwner().addEmployedPeople(-1 * map.getMap()[x][y].getBuilding().getBuildingType().getWorkers());
                if (map.getMap()[x][y].getBuilding().getBuildingType().equals(BuildingType.CHURCH) || map.getMap()[x][y].getBuilding().getBuildingType().equals(BuildingType.CATHEDRAL)) {
                    map.getMap()[x][y].getBuilding().getOwner().addReligionPopularity(-2);
                } else if (map.getMap()[x][y].getBuilding().getBuildingType().equals(BuildingType.INN)) {
                    map.getMap()[x][y].getBuilding().getOwner().addAleCoverage(-1);
                } else if (map.getMap()[x][y].getBuilding().getBuildingType().equals(BuildingType.HOVEL)) {
                    map.getMap()[x][y].getBuilding().getOwner().addMaxPopulation(-8);
                } else if (map.getMap()[x][y].getBuilding().getBuildingType().equals(BuildingType.ARMOURY)) {
                    map.getMap()[x][y].getBuilding().getOwner().getArmoury().addFreeCapacityArmoury(-50);
                } else if (map.getMap()[x][y].getBuilding().getBuildingType().equals(BuildingType.FOOD_STOCK)) {
                    map.getMap()[x][y].getBuilding().getOwner().getFoodStock().addFreeCapacityFoodStock(-250);
                } else if (map.getMap()[x][y].getBuilding().getBuildingType().equals(BuildingType.STOCKPILE)) {
                    map.getMap()[x][y].getBuilding().getOwner().getResources().addFreeCapacityStockpile(-190);
                }
            }
            map.getMap()[x][y].setBuilding(null);
            TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("Tiles.atlas"));
            GameMenuGDX.sprites[x][y] = new Sprite(textureAtlas.findRegion("ground0"));
            GameMenuGDX.sprites[x][y].setPosition(GameMenuGDX.getXFromIAndJ(x, y), GameMenuGDX.getYFromIAndJ(x, y));
            return "success";
        }
    }

    public static void burnBuildingsOfCurrentEmpire() {
        for (int i = 0; i < map.getSize(); i++) {
            for (int j = 0; j < map.getSize(); j++) {
                if (map.getMap()[i][j].getDegreeOfFire() > 0 && map.getMap()[i][j].getBuilding() != null && map.getMap()[i][j].getBuilding().getOwner() == currentEmpire) {
                    map.getMap()[i][j].getBuilding().setHp((int) (0.8 * map.getMap()[i][j].getBuilding().getHp()));
                    map.getMap()[i][j].decreaseDegreeOfFire();
                }
                if (map.getMap()[i][j].getDegreeOfFire() == 0) {
                    for (int k = 0; k < GameMenuGDX.fire.size(); k++) {
                        if (GameMenuGDX.fireCoordinates.get(k)[0] == i && GameMenuGDX.fireCoordinates.get(k)[1] == j) {
                            GameMenuGDX.fire.remove(k);
                            GameMenuGDX.fireCoordinates.remove(k);
                            break;
                        }
                    }
                }
            }
        }
    }

    public static void setWaterPotEffectForCurrentEmpire() {
        boolean currentEmpireHasWaterPot = false;
        for (Building building : currentEmpire.getBuildings()) {
            if (Objects.equals(building.getBuildingType().getName(), "WaterPot")) {
                currentEmpireHasWaterPot = true;
                break;
            }
        }

        if (currentEmpireHasWaterPot) {
            for (int i = 0; i < map.getSize(); i++) {
                for (int j = 0; j < map.getSize(); j++) {
                    if (map.getMap()[i][j].getDegreeOfFire() > 1 && map.getMap()[i][j].getBuilding().getOwner() == currentEmpire)
                        map.getMap()[i][j].setDegreeOfFire(1);
                }
            }
        }
    }

    public String startANewGame() throws IOException {
        String currentPath = new java.io.File(".").getCanonicalPath();
        System.out.println("Current dir:" + currentPath);
        String fileName = "../Phase 1 - Stronghold/game.txt";
        ArrayList<String> file = new ArrayList<>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = reader.readLine()) != null) {
                file.add(line);
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading the file: " + e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    System.out.println("An error occurred while closing the file: " + e.getMessage());
                }
            }
        }
        game = new Game();
        game.setTurnsCounter(Integer.parseInt(file.get(file.size() - 1)));
        TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("Buildings.atlas"));
        ArrayList<String> usernames = new ArrayList<>();
        for (int i = 0; i < file.size() - 1; i++) {
            usernames.add(file.get(i));
        }
        for (int i = 0; i < usernames.size(); i++) {
            User user = new User(usernames.get(i), "asfasf", "nick", "emnail");
            game.getEmpires().add(new Empire(user, i, map.getEmpireCoordinates().get(i)[0], map.getEmpireCoordinates().get(i)[1]));
            currentEmpire = game.getEmpires().get(i);
            int i1 = map.getEmpireCoordinates().get(i)[0];
            int j1 = map.getEmpireCoordinates().get(i)[1];
            Sprite sprite = new Sprite(textureAtlas.findRegion("Keep"));
            sprite.setPosition(((j1 + i1) * 15) - (GameMenuGDX.MAP_WIDTH / 2), (j1 - i1) * 8 - 8);
            GameMenuGDX.sprites[i1][j1] = sprite;
            i1++;
            sprite = new Sprite(textureAtlas.findRegion("Stockpile"));
            sprite.setPosition(((j1 + i1) * 15) - (GameMenuGDX.MAP_WIDTH / 2), (j1 - i1) * 8 - 8);
            GameMenuGDX.sprites[i1][j1] = sprite;
            i1 -= 2;
            sprite = new Sprite(textureAtlas.findRegion("FoodStock"));
            sprite.setPosition(((j1 + i1) * 15) - (GameMenuGDX.MAP_WIDTH / 2), (j1 - i1) * 8 - 8);
            GameMenuGDX.sprites[i1][j1] = sprite;
            game.getEmpires().get(i).getBuildings().add(new Building(BuildingType.KEEP, currentEmpire));
            map.getMap()[map.getEmpireCoordinates().get(i)[0]][map.getEmpireCoordinates().get(i)[1]].setBuilding(game.getEmpires().get(i).getBuildings().get(0));
            map.getMap()[map.getEmpireCoordinates().get(i)[0] + 1][map.getEmpireCoordinates().get(i)[1]].setBuilding(new Building(BuildingType.STOCKPILE, game.getEmpires().get(i)));//add stockpile for start of the game
            game.getEmpires().get(i).getBuildings().add(map.getMap()[map.getEmpireCoordinates().get(i)[0] + 1][map.getEmpireCoordinates().get(i)[1]].getBuilding());
            map.getMap()[map.getEmpireCoordinates().get(i)[0] - 1][map.getEmpireCoordinates().get(i)[1]].setBuilding(new Building(BuildingType.FOOD_STOCK, game.getEmpires().get(i)));//add foodStock for start of the game
            game.getEmpires().get(i).getBuildings().add(map.getMap()[map.getEmpireCoordinates().get(i)[0] - 1][map.getEmpireCoordinates().get(i)[1]].getBuilding());
            EmpireMenuController.calculatePopularityFactors();
        }
        currentEmpire = game.getEmpires().get(0);
        return "success";
    }

    public String selectBuilding(Matcher matcher) {
        if (!matcher.group("x").matches("\\d+") || !matcher.group("y").matches("\\d+")) {
            return "x & y should be positive numbers";
        }
        int x = Integer.parseInt(matcher.group("x"));
        int y = Integer.parseInt(matcher.group("y"));
        if (x > map.getSize() - 1 || x < 0 || y > map.getSize() - 1 || y < 0) {
            return "invalid coordinate";
        }
        if (map.getMap()[x][y].getBuilding() == null) {
            selectedBuilding = null;
            return "there is no building in this place";
        }
        if (map.getMap()[x][y].getBuilding().getOwner() == null || !map.getMap()[x][y].getBuilding().getOwner().equals(currentEmpire)) {
            return "this building isn't yours";
        }
        selectedBuilding = map.getMap()[x][y].getBuilding();
        selectedCoordinates.put("building", new int[]{x, y});
        String name = selectedBuilding.getBuildingType().getName();
        if (name.equals("SmallStoneGatehouse") || name.equals("BigStoneGatehouse") || name.equals("LookoutTower") || name.equals("PerimeterTower") || name.equals("DefenciveTurret") || name.equals("SquareTower") || name.equals("RoundTower")) {
            return "hp of this building is " + selectedBuilding.getHp();
        }
        return map.getMap()[x][y].getBuilding().getBuildingType().getName() + " selected";
    }

    public String pourOil(String dir) {
        int damage = UnitType.ENGINEER_WITH_OIL.getAttackPower();
        if (!dir.equals("up") && !dir.equals("down") && !dir.equals("left") && !dir.equals("right")) {
            return "invalid direction";
        }
        Unit unit = null;
        for (Unit selectedUnit : selectedUnits) {
            if (selectedUnit.getUnitType().equals(UnitType.ENGINEER_WITH_OIL)) {
                unit = selectedUnit;
                break;
            }
        }
        if (unit == null) {
            return "there is no engineer with oil in this place";
        }
        int x = selectedCoordinates.get("unit")[0];
        int y = selectedCoordinates.get("unit")[1];
//        switch (dir) {
//            case "up" -> {
//                if (y + 1 > map.getSize() - 1) {
//                    return "EndOfTheMap!";
//                }
//                for (int i = 0; i < map.getMap()[x][y + 1].getUnits().size(); i++) {
//                    if (map.getMap()[x][y + 1].getUnits().get(i).getOwner() == null || !map.getMap()[x][y + 1].getUnits().get(i).getOwner().equals(currentEmpire)) {
//                        map.getMap()[x][y + 1].getUnits().get(i).getDamage(damage);
//                    }
//                }
//            }
//            case "down" -> {
//                if (y == 0) {
//                    return "EndOfTheMap!";
//                }
//                for (int i = 0; i < map.getMap()[x][y - 1].getUnits().size(); i++) {
//                    if (map.getMap()[x][y - 1].getUnits().get(i).getOwner() == null || !map.getMap()[x][y - 1].getUnits().get(i).getOwner().equals(currentEmpire)) {
//                        map.getMap()[x][y - 1].getUnits().get(i).getDamage(damage);
//                    }
//                }
//            }
//            case "left" -> {
//                if (x == 0) {
//                    return "EndOfTheMap!";
//                }
//                for (int i = 0; i < map.getMap()[x - 1][y].getUnits().size(); i++) {
//                    if (map.getMap()[x - 1][y].getUnits().get(i).getOwner() == null || !map.getMap()[x - 1][y].getUnits().get(i).getOwner().equals(currentEmpire)) {
//                        map.getMap()[x - 1][y].getUnits().get(i).getDamage(damage);
//                    }
//                }
//            }
//            case "right" -> {
//                if (x + 1 > map.getSize() - 1) {
//                    return "EndOfTheMap!";
//                }
//                for (int i = 0; i < map.getMap()[x + 1][y].getUnits().size(); i++) {
//                    if (map.getMap()[x + 1][y].getUnits().get(i).getOwner() == null || !map.getMap()[x + 1][y].getUnits().get(i).getOwner().equals(currentEmpire)) {
//                        map.getMap()[x + 1][y].getUnits().get(i).getDamage(damage);
//                    }
//                }
//            }
//        }
        boolean bool = false;
        for (int i = 0; i < currentEmpire.getBuildings().size(); i++) {
            if (currentEmpire.getBuildings().get(i).getBuildingType().equals(BuildingType.OIL_SMELTER)) {
                bool = true;
                break;
            }
        }
        if (!(bool && currentEmpire.getResources().getPitch() > 0)) {
            map.getMap()[x][y].getUnits().remove(unit);
            currentEmpire.getUnits().remove(unit);
            map.getMap()[x][y].getUnits().add(new Unit(UnitType.ENGINEER, currentEmpire, x, y));
            currentEmpire.getUnits().add(map.getMap()[x][y].getUnits().get(map.getMap()[x][y].getUnits().size() - 1));
        } else {
            currentEmpire.getResources().addResource("pitch", -1);
        }
        return "success";
    }

    public String attackMachines(Matcher matcher) {//fire ballista attack in group of archers not here//TODO add
        if (!matcher.group("x").matches("\\d+") || !matcher.group("y").matches("\\d+")) {
            return "x & y should be positive numbers";
        }
        int x = Integer.parseInt(matcher.group("x"));
        int y = Integer.parseInt(matcher.group("y"));
        if (x > map.getSize() - 1 || x < 0 || y > map.getSize() - 1 || y < 0) {
            return "invalid coordinate";
        }
        if (selectedUnits.size() == 0) {
            return "you should select a unit first";
        }
        if (map.getMap()[x][y].getBuilding() == null) {
            return "there is no building in that coordinate";
        } else if (map.getMap()[x][y].getBuilding().getOwner() != null && map.getMap()[x][y].getBuilding().getOwner().equals(currentEmpire)) {
            return "that building is yours";
        }
        int distance = distance(x, y, selectedCoordinates.get("unit")[0], selectedCoordinates.get("unit")[1]);
        for (Unit selectedUnit : selectedUnits) {
//            switch (selectedUnit.getUnitType()) {
//                case CATAPULT, TREBUCHET -> {
//                    if (distance <= selectedUnit.getUnitType().getAttackRange())
//                        map.getMap()[x][y].getBuilding().getDamage(selectedUnit.getAttackPower());
//                }
//                case BATTERING_RAM -> {
//                    MoveController moveController = new MoveController();
//                    MoveController.Pair<Integer, Integer> src = new MoveController.Pair<>(x, y);
//                    MoveController.Pair<Integer, Integer> dest = new MoveController.Pair<>(selectedCoordinates.get("unit")[0], selectedCoordinates.get("unit")[1]);
//                    ArrayList<Unit> units = new ArrayList<>();
//                    units.add(selectedUnit);
//                    if (moveController.aStarSearch(src, dest, units).equals("Success")) {
//                        if (selectedUnit.getUnitType().getSpeed() >= distance)
//                            map.getMap()[x][y].getBuilding().getDamage(selectedUnit.getAttackPower());
//                    }
//                }
//            }
            selectedUnit.getPath().clear();
            checkDestroyedBuildingLocation(x, y);
        }
        return "success";
    }

    public String digTunnel(Matcher matcher) {//Tunnelers can dig tunnel in range 10 & damage that building 15000
        if (!matcher.group("x").matches("\\d+") || !matcher.group("y").matches("\\d+")) {
            return "x & y should be positive numbers";
        }
        int x = Integer.parseInt(matcher.group("x"));
        int y = Integer.parseInt(matcher.group("y"));
        if (x > map.getSize() - 1 || x < 0 || y > map.getSize() - 1 || y < 0) {
            return "invalid coordinate";
        }
        if (selectedUnits.size() == 0) {
            return "you should select a unit first";
        }
        if (distance(x, y, selectedCoordinates.get("unit")[0], selectedCoordinates.get("unit")[1]) > 10) {
            return "out of range";
        }
        int index = -1;
        for (int i = 0; i < selectedUnits.size(); i++) {
            if (selectedUnits.get(i).getUnitType().getName().equals("Tunneler")) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            return "there in no tunneler in the selected unit";
        }
        if (map.getMap()[x][y].getBuilding() == null) {
            return "there is no building in that coordinate";
        }
        if (map.getMap()[x][y].getBuilding().getOwner() != null && map.getMap()[x][y].getBuilding().getOwner().equals(currentEmpire)) {
            return "the building in that coordinate is yours";
        }
        map.getMap()[x][y].getBuilding().getDamage(15000);
        currentEmpire.getUnits().remove(selectedUnits.get(index));
        map.getMap()[selectedCoordinates.get("unit")[0]][selectedCoordinates.get("unit")[1]].getUnits().remove(selectedUnits.get(index));
        selectedUnits.clear();
        return "success";
    }

    public String changeBuildingMode(Matcher matcher) {
        String mode = matcher.group("mode");
        if (selectedBuilding == null) {
            return "no selected building";
        }
//        switch (selectedBuilding.getBuildingType().getName()) {
//            case "Fletcher" -> {
//                if (mode.equals("bow") || mode.equals("crossbow")) {
//                    selectedBuilding.setMode(mode);
//                } else {
//                    return "invalid mode for Fletcher building";
//                }
//            }
//            case "PoleTurner" -> {
//                if (mode.equals("spear") || mode.equals("pike")) {
//                    selectedBuilding.setMode(mode);
//                } else {
//                    return "invalid mode for PoleTurner building";
//                }
//            }
//            case "BlackSmith" -> {
//                if (mode.equals("sword") || mode.equals("mace")) {
//                    selectedBuilding.setMode(mode);
//                } else {
//                    return "invalid mode for BlackSmith building";
//                }
//            }
//            case "SmallStoneGatehouse", "BigStoneGatehouse" -> {
//                if (mode.equals("open") || mode.equals("close")) {
//                    selectedBuilding.setMode(mode);
//                } else {
//                    return "invalid mode for Gate building";
//                }
//            }
//            default -> {
//                return "you can't change the mode of this building";
//            }
//        }
        return "success";
    }

    public String buildEquipment(Matcher matcher) {
        String type = matcher.group("equipment");
        if (!type.equals("PortableShield") && !type.equals("BatteringRam") && !type.equals("Trebuchet") && !type.equals("Catapult") && !type.equals("FireBallista") && !type.equals("SiegeTower")) {
            return "invalid type for equipment";
        }
        int engineerCounter = 0;
        for (Unit selectedUnit : selectedUnits) {
            if (selectedUnit.getUnitType().getName().equals("Engineer")) {
                engineerCounter++;
            }
        }
        int cost = UnitType.getUnitByName(type).getCost();
        if (currentEmpire.getResources().getGold() < cost) {
            return "not enough gold";
        }
//        switch (type) {
//            case "PortableShield" -> {
//                if (engineerCounter < 1) {
//                    return "not enough engineer to build this equipment";
//                }
//                map.getMap()[selectedCoordinates.get("unit")[0]][selectedCoordinates.get("unit")[1]].getUnits().remove(getUnit(currentEmpire, "Engineer"));
//                currentEmpire.getUnits().remove(getUnit(currentEmpire, "Engineer"));
//            }
//            case "BatteringRam", "SiegeTower" -> {
//                if (engineerCounter < 4) {
//                    return "not enough engineer to build this equipment";
//                }
//                for (int i = 0; i < 4; i++) {
//                    map.getMap()[selectedCoordinates.get("unit")[0]][selectedCoordinates.get("unit")[1]].getUnits().remove(getUnit(currentEmpire, "Engineer"));
//                    currentEmpire.getUnits().remove(getUnit(currentEmpire, "Engineer"));
//                    selectedUnits.remove(getUnit(currentEmpire, "Engineer"));
//                }
//            }
//            case "Trebuchet" -> {
//                if (engineerCounter < 3) {
//                    return "not enough engineer to build this equipment";
//                }
//                for (int i = 0; i < 3; i++) {
//                    map.getMap()[selectedCoordinates.get("unit")[0]][selectedCoordinates.get("unit")[1]].getUnits().remove(getUnit(currentEmpire, "Engineer"));
//                    currentEmpire.getUnits().remove(getUnit(currentEmpire, "Engineer"));
//                    selectedUnits.remove(getUnit(currentEmpire, "Engineer"));
//                }
//            }
//            case "Catapult", "FireBallista" -> {
//                if (engineerCounter < 2) {
//                    return "not enough engineer to build this equipment";
//                }
//                for (int i = 0; i < 2; i++) {
//                    map.getMap()[selectedCoordinates.get("unit")[0]][selectedCoordinates.get("unit")[1]].getUnits().remove(getUnit(currentEmpire, "Engineer"));
//                    currentEmpire.getUnits().remove(getUnit(currentEmpire, "Engineer"));
//                    selectedUnits.remove(getUnit(currentEmpire, "Engineer"));
//                }
//            }
//        }
        currentEmpire.getResources().addGold(-1 * cost);
        currentEmpire.getUnits().add(new Unit(UnitType.getUnitByName(type), currentEmpire, selectedCoordinates.get("unit")[0], selectedCoordinates.get("unit")[1]));
        map.getMap()[selectedCoordinates.get("unit")[0]][selectedCoordinates.get("unit")[1]].getUnits().add(currentEmpire.getUnits().get(currentEmpire.getUnits().size() - 1));
        selectedUnits.clear();
        return "success";
    }

    public Unit getUnit(Empire empire, String name) {
        for (Unit selectedUnit : selectedUnits) {
            if (selectedUnit.getUnitType().getName().equals(name) && selectedUnit.getOwner() != null && selectedUnit.getOwner().equals(empire)) {
                return selectedUnit;
            }
        }
        return null;
    }

    public String disbandUnit() {
        if (selectedUnits.size() == 0) {
            return "you should select a unit";
        }
        for (Unit selectedUnit : selectedUnits) {
            map.getMap()[selectedCoordinates.get("unit")[0]][selectedCoordinates.get("unit")[1]].getUnits().remove(selectedUnit);
            currentEmpire.getUnits().remove(selectedUnit);
        }
        currentEmpire.addUnemployedPeople(selectedUnits.size());
        selectedUnits.clear();
        return "success";
    }

    public String setOilForEngineers(Matcher matcher) {
        int count = Integer.parseInt(matcher.group("count"));
        int counter = 0;
        for (Unit selectedUnit : selectedUnits) {
            if (selectedUnit.getUnitType().equals(UnitType.ENGINEER)) {
                counter++;
            }
        }
        if (count > counter) {
            return "not enough engineer";
        }
        boolean bool = false;
        for (int i = 0; i < currentEmpire.getBuildings().size(); i++) {
            if (currentEmpire.getBuildings().get(i).getBuildingType().equals(BuildingType.OIL_SMELTER)) {
                bool = true;
                break;
            }
        }
        if (!bool) {
            return "you don't have oil smelter in your buildings";
        }
        if (currentEmpire.getResources().getPitch() < count) {
            return "you don't have enough pitch";
        }
        currentEmpire.getResources().addResource("pitch", -count);
        for (int i = 0; i < count; i++) {
            map.getMap()[selectedCoordinates.get("unit")[0]][selectedCoordinates.get("unit")[1]].getUnits().remove(getUnit(currentEmpire, "Engineer"));
            currentEmpire.getUnits().remove(getUnit(currentEmpire, "Engineer"));
            selectedUnits.remove(getUnit(currentEmpire, "Engineer"));
        }
        for (int i = 0; i < count; i++) {
            map.getMap()[selectedCoordinates.get("unit")[0]][selectedCoordinates.get("unit")[1]].getUnits().add(new Unit(UnitType.ENGINEER_WITH_OIL, currentEmpire, selectedCoordinates.get("unit")[0], selectedCoordinates.get("unit")[1]));
            currentEmpire.getUnits().add(map.getMap()[selectedCoordinates.get("unit")[0]][selectedCoordinates.get("unit")[1]].getUnits().get(map.getMap()[selectedCoordinates.get("unit")[0]][selectedCoordinates.get("unit")[1]].getUnits().size() - 1));
        }
        return "success";
    }

    public String digMoat(Matcher matcher) {//spearman and slaves can dig moat
        if (!matcher.group("x").matches("\\d+") || !matcher.group("y").matches("\\d+")) {
            return "x & y should be positive numbers";
        }
        int x = Integer.parseInt(matcher.group("x"));
        int y = Integer.parseInt(matcher.group("y"));
        if (x > map.getSize() - 1 || x < 0 || y > map.getSize() - 1 || y < 0) {
            return "invalid coordinate";
        }
        if (selectedUnits.size() == 0) {
            return "you should select a unit first";
        }
        boolean bool = false;
        for (Unit selectedUnit : selectedUnits) {
            if (selectedUnit.getUnitType().equals(UnitType.SPEARMAN) || selectedUnit.getUnitType().equals(UnitType.SLAVES)) {
                bool = true;
            }
        }
        if (map.getMap()[x][y].getBuilding() != null || map.getMap()[x][y].getUnits().size() > 0 || map.getMap()[x][y].getEnvironmentName() != null) {
            return "there are some other things in this place";
        }
        if (!bool) {
            return "you should select a unit with spearman or slave";
        }
        map.getMap()[x][y].setType("moat");
        return "success";
    }

    public String Stop(Matcher matcher) {
        for (Unit unit : selectedUnits) {
            unit.getPath().clear();
            unit.setPatrol(false);
        }
        return "Success";
    }

    public String noMoat(Matcher matcher) {
        if (!matcher.group("x").matches("\\d+") || !matcher.group("y").matches("\\d+")) {
            return "x & y should be positive numbers";
        }
        int x = Integer.parseInt(matcher.group("x"));
        int y = Integer.parseInt(matcher.group("y"));
        if (x > map.getSize() - 1 || x < 0 || y > map.getSize() - 1 || y < 0) {
            return "invalid coordinate";
        }
        if (selectedUnits.size() == 0) {
            return "you should select a unit first";
        }
        boolean bool = false;
        for (Unit selectedUnit : selectedUnits) {
            if (selectedUnit.getUnitType().equals(UnitType.SPEARMAN) || selectedUnit.getUnitType().equals(UnitType.SLAVES)) {
                bool = true;
            }
        }
        if (map.getMap()[x][y].getBuilding() != null || map.getMap()[x][y].getUnits().size() > 0 || map.getMap()[x][y].getEnvironmentName() != null) {
            return "there are some other things in this place";
        }
        if (!bool) {
            return "you should select a unit with spearman or slave";
        }
        if (!map.getMap()[x][y].getType().equals("moat")) {
            return "there is no moa in that coordinate";
        }
        map.getMap()[x][y].setType("earth");
        return "success";
    }

    public String deployCagedWarDogs() {//dogs will damage(10) all units in the range of 3
        if (selectedBuilding == null || !selectedBuilding.getBuildingType().equals(BuildingType.CAGED_WAR_DOGS)) {
            return "you have to select a cagedWarDogs";
        }
        int attackRange = 3, damage = 10;
        int x = selectedCoordinates.get("building")[0];
        int y = selectedCoordinates.get("building")[1];
        for (int i = x - attackRange; i < attackRange + x; i++) {
            for (int j = y - attackRange; j < y + attackRange; j++) {
                if (j > map.getSize() - 1 || j < 0) {
                    continue;
                }
                if (i > map.getSize() - 1 || i < 0) {
                    break;
                }
                for (int k = 0; k < map.getMap()[i][j].getUnits().size(); k++) {
                    map.getMap()[i][j].getUnits().get(k).getDamage(damage);
                }
                checkDeadUnitsLocation(i, j);
            }
        }
        selectedBuilding = null;
        map.getMap()[x][y].getBuilding().getOwner().getBuildings().remove(map.getMap()[x][y].getBuilding());
        map.getMap()[x][y].setBuilding(null);
        return "success";
    }

    public String moveUnit(Matcher matcher) {
        if (selectedUnits.size() == 0) return "Select unit";
        int x = Integer.parseInt(matcher.group("x"));
        int y = Integer.parseInt(matcher.group("y"));
        MoveController.Pair<Integer, Integer> dest = new MoveController.Pair<>(x, y);
        MoveController.Pair<Integer, Integer> src = new MoveController.Pair<>(selectedCoordinates.get("unit")[0], selectedCoordinates.get("unit")[1]);
        MoveController moveController = new MoveController();
        return moveController.aStarSearch(src, dest, selectedUnits);
    }

    public String patrolUnit(Matcher matcher) {
        if (selectedUnits.size() == 0) return "Select unit";
        int x = Integer.parseInt(matcher.group("x"));
        int y = Integer.parseInt(matcher.group("y"));
        MoveController.Pair<Integer, Integer> dest = new MoveController.Pair<>(x, y);
        MoveController.Pair<Integer, Integer> src = new MoveController.Pair<>(selectedCoordinates.get("unit")[0], selectedCoordinates.get("unit")[1]);
        MoveController moveController = new MoveController();
        String stingPatrolUnit = moveController.aStarSearch(src, dest, selectedUnits);
        if (stingPatrolUnit.equals("Success")) {
            for (Unit unit : selectedUnits) {
                unit.setPatrol(true);
            }
        }
        return stingPatrolUnit;
    }

    public String stop() {
        if (selectedUnits.size() == 0) return "Select unit";
        for (Unit unit : selectedUnits) {
            unit.getPath().clear();
            unit.setCurrentCell(-1);
            unit.setPatrol(false);
        }
        return "Success";
    }
}