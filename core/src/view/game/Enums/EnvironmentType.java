package view.game.Enums;
public enum EnvironmentType {
    EARTH("earth", "\u001b[43m", true, true),
    EARTH_AND_SAND("earthAndStone", "\u001b[42m", true, true),
    BOULDER("boulder", "\u001b[41m", false, true),
    ROCK_TEXTURE("rockTexture", "\u001b[41m", false, false),
    IRON_TEXTURE("ironTexture", "\u001b[41m", false, true),
    SCRUB("scrub", "\u001b[42m", true, true),
    THICK_GRASS("thickGrass", "\u001b[42m", true, true),
    OASIS_GRASS("oasisGrass", "\u001b[42m", true, false),
    OIL("oil", "\u001b[45m", false, true),
    MARSH("marsh", "\u001b[43m", false, false),
    LOW_DEPTH_WATER("lowDepthWater", "\u001b[44m", false, false),
    RIVER("river", "\u001b[44m", false, false),
    SMALL_POND("smallPond", "\u001b[44m", false, false),
    LARGE_POND("largePond", "\u001b[44m", false, false),
    BEACH("beach", "\u001b[43m", false, true),
    SEA("sea", "\u001b[44m", false, false),
    MOAT("moat", "\u001b[44m", false, false);


    private final String name;
    private final String color;
    private final boolean dropTree;
    private final boolean passable;

    EnvironmentType(String name, String color, boolean dropTree, boolean passable) {
        this.name = name;
        this.color = color;
        this.dropTree = dropTree;
        this.passable = passable;
    }

    public static EnvironmentType getEnvironmentTypeByName(String name) {
        for (EnvironmentType type : EnvironmentType.values()) {
            if (name.equalsIgnoreCase(type.name))
                return type;
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public boolean isDropTree() {
        return dropTree;
    }

    public boolean isPassable() {
        return passable;
    }
}
