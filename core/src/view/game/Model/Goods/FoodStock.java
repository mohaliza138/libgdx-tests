package view.game.Model.Goods;

public class FoodStock {
    private double meat;
    private double apple;
    private double cheese;
    private double bread;
    private double freeCapacityFoodStock;

    public FoodStock(double meat, double apple, double cheese, double bread, double freeCapacityFoodStock) {
        this.meat = meat;
        this.apple = apple;
        this.cheese = cheese;
        this.bread = bread;
        this.freeCapacityFoodStock = freeCapacityFoodStock;
    }

    public FoodStock() {
    }

    public double getMeat() {
        return meat;
    }

    public double getApple() {
        return apple;
    }

    public double getCheese() {
        return cheese;
    }

    public double getBread() {
        return bread;
    }

    public void addMeat(double meat) {
        this.meat += meat;
    }

    public void addApple(double apple) {
        this.apple += apple;
    }

    public void addCheese(double cheese) {
        this.cheese += cheese;
    }

    public void addBread(double bread) {
        this.bread += bread;
    }

    public void addFreeCapacityFoodStock(int amount) {
        this.freeCapacityFoodStock += amount;
    }

    public double getFreeCapacityFoodStock() {
        return freeCapacityFoodStock;
    }

    public void addFood(String name, double amount) {
        if (name.equals("meat")) {
            addMeat(amount);
            addFreeCapacityFoodStock((int) (-1 * amount));
        } else if (name.equals("apple")) {
            addApple(amount);
            addFreeCapacityFoodStock((int) (-1 * amount));
        } else if (name.equals("cheese")) {
            addCheese(amount);
            addFreeCapacityFoodStock((int) (-1 * amount));
        } else if (name.equals("bread")) {
            addBread(amount);
            addFreeCapacityFoodStock((int) (-1 * amount));
        }
    }

    public double getFoodAmount(String name) {
        if (name.equals("meat")) {
            return meat;
        } else if (name.equals("apple")) {
            return apple;
        } else if (name.equals("cheese")) {
            return cheese;
        } else if (name.equals("bread")) {
            return bread;
        } else {
            return -1;
        }
    }

    public boolean isFoodType(String name) {
        return name.equals("meat") || name.equals("apple") || name.equals("cheese") || name.equals("bread");
    }
}
