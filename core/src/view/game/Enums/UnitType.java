package view.game.Enums;

public enum UnitType {
    ARCHER("Archer", 4, 20, 4, 12, 10, "Archer"),
    CROSSBOWMEN("Crossbowman", 12, 60, 2, 20, 9, "Archer"),
    SPEARMAN("Spearman", 3, 15, 3, 8, 0, "Sword"),
    PIKE_MAN("Pikeman", 30, 150, 2, 5, 0, "Sword"),
    MACE_MAN("Maceman", 30, 100, 3, 20, 0, "Sword"),
    SWORDSMAN("Swordsman", 60, 300, 1, 40, 0, "Sword"),
    KNIGHT("Knight", 60, 300, 5, 40, 0, "Sword"),
    TUNNELER("Tunneler", 10, 10, 4, 30, 0, "Engineer"),
    LADDER_MAN("Ladderman", 0, 10, 4, 4, 0, "Engineer"),
    ENGINEER("Engineer", 0, 10, 3, 30, 0, "Engineer"),
    ENGINEER_WITH_OIL("EngineerWithOil",200,10, 3,30,0,"Engineer"),
    BLACK_MONK("BlackMonk", 30, 150, 2, 10, 0, "Sword"),
    ARCHER_BOW("ArcherBow", 4, 20, 4, 75, 10, "Archer"),
    SLAVES("Slaves", 10, 10, 4, 5, 0, "Sword"),
    SLINGER("Slinger", 3, 10, 4, 12, 5, "Archer"),
    ASSASSIN("Assassin", 20, 20, 3, 60, 0, "Sword"),
    HORSE_ARCHER("HorseArcher", 20, 30, 5, 80, 13, "Archer"),
    ARABIAN_SWORDSMAN("ArabianSwordsman", 40, 100, 5, 80, 0, "Sword"),
    FIRE_THROWER("FireThrower", 70, 20, 5, 100, 3, "Archer"),
    PORTABLE_SHIELD("PortableShield", 0, 100, 4, 5, 0, "Machine"),
    BATTERING_RAM("BatteringRam", 500, 2000, 2, 150, 0, "Machine"),
    TREBUCHET("Trebuchet", 2000, 400, 0, 150, 40, "Machine"),
    CATAPULT("Catapult", 650, 150, 3, 150, 30, "Machine"),
    FIRE_BALLISTA("FireBallista", 100, 150, 3, 150, 30, "Archer"),
    SIEGE_TOWER("SiegeTower", 0, 3300, 3, 150, 0, "Machine");

    private final int cost;
    private final String name;
    private final int speed;
    private final int attackPower;
    private final int defencePower;
    private final int attackRange;
    private final String type;

    UnitType(String name, int attackPower, int defencePower, int speed, int cost, int attackRange, String type) {
        this.name = name;
        this.speed = speed;
        this.attackPower = attackPower;
        this.defencePower = defencePower;
        this.cost = cost;
        this.attackRange = attackRange;
        this.type = type;
    }

    public static UnitType getUnitByName(String name) {
        for (UnitType unit : UnitType.values()) {
            if (name.equals(unit.name))
                return unit;
        }
        return null;
    }

    public int getSpeed() {
        return speed;
    }

    public int getAttackRange() {
        return attackRange;
    }

    public int getCost() {
        return cost;
    }

    public String getName() {
        return name;
    }

    public int getAttackPower() {
        return attackPower;
    }

    public int getDefencePower() {
        return defencePower;
    }

    public String getType() {
        return type;
    }
}
