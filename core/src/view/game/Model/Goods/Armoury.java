package view.game.Model.Goods;

public class Armoury {
    private int horse;
    private int bow;
    private int crossbow;
    private int spear;
    private int pike;
    private int mace;
    private int sword;
    private int leatherArmor;
    private int metalArmor;
    private int freeCapacityArmoury;

    public Armoury(int bow, int crossbow, int spear, int pike, int mace, int sword, int leatherArmor, int metalArmor, int freeCapacityArmoury, int horse) {
        this.bow = bow;
        this.crossbow = crossbow;
        this.spear = spear;
        this.pike = pike;
        this.mace = mace;
        this.sword = sword;
        this.leatherArmor = leatherArmor;
        this.metalArmor = metalArmor;
        this.freeCapacityArmoury = freeCapacityArmoury;
        this.horse = horse;
    }

    public Armoury() {
    }

    public boolean isArmouryType(String name) {
        return name.equals("bow") || name.equals("crossbow") || name.equals("spear") || name.equals("pike") || name.equals("mace") || name.equals("sword") ||
                name.equals("leatherArmor") || name.equals("metalArmor");
    }

    public int getHorse() {
        return horse;
    }

    public int getBow() {
        return bow;
    }

    public int getCrossbow() {
        return crossbow;
    }

    public int getSpear() {
        return spear;
    }

    public int getPike() {
        return pike;
    }

    public int getMace() {
        return mace;
    }

    public int getSword() {
        return sword;
    }

    public int getLeatherArmor() {
        return leatherArmor;
    }

    public int getMetalArmor() {
        return metalArmor;
    }

    public void addBow(int bow) {
        this.bow += bow;
    }

    public void addCrossbow(int crossbow) {
        this.crossbow += crossbow;
    }

    public void addSpear(int spear) {
        this.spear += spear;
    }

    public void addPike(int pike) {
        this.pike += pike;
    }

    public void addMace(int mace) {
        this.mace += mace;
    }

    public void addHorse(int horse) {
        this.horse += horse;
    }

    public void addSword(int sword) {
        this.sword += sword;
    }

    public void addLeatherArmor(int leatherArmor) {
        this.leatherArmor += leatherArmor;
    }

    public void addMetalArmor(int metalArmor) {
        this.metalArmor += metalArmor;
    }

    public void addFreeCapacityArmoury(int amount) {
        this.freeCapacityArmoury += amount;
    }

    public int getFreeCapacityArmoury() {
        return freeCapacityArmoury;
    }

    public void addArmoury(String name, int amount) {
        if (name.equals("bow")) {
            addBow(amount);
            addFreeCapacityArmoury(-1 * amount);
        } else if (name.equals("crossbow")) {
            addCrossbow(amount);
            addFreeCapacityArmoury(-1 * amount);
        } else if (name.equals("spear")) {
            addSpear(amount);
            addFreeCapacityArmoury(-1 * amount);
        } else if (name.equals("pike")) {
            addPike(amount);
            addFreeCapacityArmoury(-1 * amount);
        } else if (name.equals("mace")) {
            addMace(amount);
            addFreeCapacityArmoury(-1 * amount);
        } else if (name.equals("sword")) {
            addSword(amount);
            addFreeCapacityArmoury(-1 * amount);
        } else if (name.equals("leatherArmor")) {
            addLeatherArmor(amount);
            addFreeCapacityArmoury(-1 * amount);
        } else if (name.equals("metalArmor")) {
            addMetalArmor(amount);
            addFreeCapacityArmoury(-1 * amount);
        }
    }

    public int getArmouryAmount(String name) {
        if (name.equals("bow")) {
            return this.bow;
        } else if (name.equals("crossbow")) {
            return this.crossbow;
        } else if (name.equals("spear")) {
            return this.spear;
        } else if (name.equals("pike")) {
            return this.pike;
        } else if (name.equals("mace")) {
            return this.mace;
        } else if (name.equals("sword")) {
            return this.sword;
        } else if (name.equals("leatherArmor")) {
            return this.leatherArmor;
        } else if (name.equals("metalArmor")) {
            return this.metalArmor;
        } else {
            return -1;
        }
    }
}
