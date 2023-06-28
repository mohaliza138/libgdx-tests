package view.game.Enums;
public enum BuildingType {//fireRange will boost the range of the archers  || capacity is equal to the amount that in one time the worker bring to the storages
    SMALL_STONE_GATEHOUSE(10000, "SmallStoneGatehouse", 0, 0, 0, 0, 10, "Towers", 25, 0, 10),
    BIG_STONE_GATEHOUSE(20000, "BigStoneGatehouse", 0, 0, 0, 0, 20, "Towers", 50, 0, 15),
    DRAW_BRIDGE(5000, "DrawBridge", 0, 0, 10, 0, 0, "CastleBuildings", 0, 0, 0),
    LOOKOUT_TOWER(2500, "LookoutTower", 0, 0, 0, 0, 10, "Towers", 5, 0, 25),
    PERIMETER_TOWER(10000, "PerimeterTower", 0, 0, 0, 0, 10, "Towers", 15, 0, 10),
    DEFENCIVE_TURRET(12000, "DefenciveTurret", 0, 0, 0, 0, 15, "Towers", 20, 0, 15),
    SQUARE_TOWER(16000, "SquareTower", 0, 0, 0, 0, 35, "Towers", 35, 0, 20),
    ROUND_TOWER(20000, "RoundTower", 0, 0, 0, 0, 40, "Towers", 40, 0, 20),
    ARMOURY(450, "Armoury", 0, 0, 5, 0, 0, "CastleBuildings", 50, 0, 0),
    BARRACK(450, "Barrack", 0, 0, 0, 0, 15, "CastleBuildings", 0, 0, 0),
    MERCENARY_POST(450, "MercenaryPost", 0, 0, 10, 0, 0, "CastleBuildings", 0, 0, 0),
    ENGINEER_GUILD(450, "EngineerGuild", 0, 100, 10, 0, 0, "CastleBuildings", 0, 0, 0),
    KILLING_PIT(Integer.MAX_VALUE, "KillingPit", 0, 0, 6, 0, 0, "CastleBuildings", 0, 0, 0),
    INN(300, "Inn", 1, 100, 20, 0, 0, "FoodProcessingBuildings", 0, 8, 0),
    MILL(300, "Mill", 3, 0, 20, 0, 0, "FoodProcessingBuildings", 3, 16, 0),
    IRON_MINE(250, "IronMine", 2, 0, 20, 0, 0, "Industry", 1, 12, 0),
    MARKET(300, "Market", 1, 0, 5, 0, 0, "Industry", 0, 0, 0),
    OX_TETHER(100, "OxTether", 1, 0, 5, 0, 0, "Industry", 12, 8, 0),
    PITCH_RIG(250, "PitchRig", 1, 0, 20, 0, 0, "Industry", 1, 8, 0),
    QUARRY(250, "Quarry", 3, 0, 20, 0, 0, "Industry", 20, 8, 0),
    STOCKPILE(250, "Stockpile", 0, 0, 0, 0, 0, "Industry", 190, 0, 0),
    WOODCUTTER(100, "Woodcutter", 1, 0, 3, 0, 0, "Industry", 18, 8, 0),
    HOVEL(100, "Hovel", 0, 0, 6, 0, 0, "TownBuildings", 0, 0, 0),
    CHURCH(700, "Church", 0, 250, 0, 0, 0, "TownBuildings", 0, 0, 0),
    CATHEDRAL(1000, "Cathedral", 0, 1000, 0, 0, 0, "TownBuildings", 0, 0, 0),
    ARMOURER(250, "Armourer", 1, 100, 20, 0, 0, "Weapon", 1, 4, 0),
    BLACKSMITH(250, "BlackSmith", 1, 100, 20, 0, 0, "Weapon", 1, 4, 0),
    FLETCHER(250, "Fletcher", 1, 100, 20, 0, 0, "Weapon", 1, 4, 0),
    POLE_TURNER(250, "PoleTurner", 1, 100, 10, 0, 0, "Weapon", 1, 4, 0),
    OIL_SMELTER(250, "OilSmelter", 0, 100, 0, 10, 0, "CastleBuildings", 0, 0, 0),
    CAGED_WAR_DOGS(100, "CagedWarDogs", 0, 100, 10, 0, 0, "CastleBuildings", 0, 0, 0),
    SIEGE_TENT(50, "SiegeTent", 0, 0, 0, 0, 0, "CastleBuildings", 0, 0, 0),
    STABLE(250, "Stable", 0, 400, 20, 0, 0, "CastleBuildings", 4, 0, 0),
    APPLE_ORCHARD(120, "AppleOrchard", 1, 0, 5, 0, 0, "FarmBuildings", 4, 12, 0),
    DAIRY_ّFARM(120, "DairyFarm", 1, 0, 10, 0, 0, "FarmBuildings", 4, 12, 0),
    HOPS_FARM(120, "HopsFarm", 1, 0, 15, 0, 0, "FarmBuildings", 4, 12, 0),
    HUNTING_POST(120, "HuntingPost", 1, 0, 5, 0, 0, "FarmBuildings", 4, 12, 0),
    WHEAT_FARM(120, "WheatFarm", 1, 0, 15, 0, 0, "FarmBuildings", 2, 8, 0),
    BAKERY(300, "Bakery", 1, 0, 10, 0, 0, "FoodProcessingBuildings", 8, 8, 0),
    BREWERY(300, "Brewery", 1, 0, 10, 0, 0, "FoodProcessingBuildings", 1, 8, 0),
    SHORT_WALL(400, "ShortWall", 0, 0, 0, 0, 1, "CastleBuildings", 1, 0, 0),
    TALL_WALL(600, "TallWall", 0, 0, 0, 0, 2, "CastleBuildings", 1, 0, 0),
    STAIRS(400, "Stairs", 0, 0, 0, 0, 3, "CastleBuildings", 1, 0, 0),
    KEEP(Integer.MAX_VALUE, "Keep", 0, 0, 0, 0, 0, "CastleBuildings", 40, 0, 15),
    FOOD_STOCK(250, "FoodStock", 0, 0, 5, 0, 0, "FoodProcessingBuildings", 250, 0, 0),
    APOTHECARY(250, "Apothecary", 1, 150, 10, 0, 0, "CastleBuildings", 0, 0, 0),
    WATER_POT(250, "WaterPot", 1, 60, 0, 0, 0, "CastleBuildings", 0, 0, 0);


    private final int hp;
    private final String name;
    private final int Workers;
    private final int gold;
    private final int wood;
    private final int Iron;
    private final int stone;
    private final String type;
    private final int rate;
    private final int capacity;
    private final int fireRange;

    BuildingType(int hp, String name, int workers, int gold, int wood, int iron, int stone, String type, int capacity, int rate, int fireRange) {
        this.hp = hp;
        this.name = name;
        this.Workers = workers;
        this.gold = gold;
        this.wood = wood;
        this.Iron = iron;
        this.stone = stone;
        this.type = type;
        this.capacity = capacity;
        this.rate = rate;
        this.fireRange = fireRange;
    }

    public static BuildingType getBuildingByName(String name) {
        for (BuildingType building : BuildingType.values()) {
            if (name.equalsIgnoreCase(building.name))
                return building;
        }
        return null;
    }

    public int getRate() {
        return rate;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getHp() {
        return hp;
    }

    public String getName() {
        return name;
    }

    public int getWorkers() {
        return Workers;
    }

    public int getGold() {
        return gold;
    }

    public int getWood() {
        return wood;
    }

    public int getIron() {
        return Iron;
    }

    public int getStone() {
        return stone;
    }

    public String getType() {
        return type;
    }

    public int getFireRange() {
        return fireRange;
    }
}