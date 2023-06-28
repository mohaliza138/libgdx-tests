package view.game.Model;

import java.util.ArrayList;
import java.util.HashMap;

public class Game {
    private static HashMap<String, Integer> shopItems = new HashMap<>();
    private ArrayList<Trade> allTrades;

    private static ArrayList<Empire> empires;
    private int turnsCounter;

    public Game() {
        allTrades = new ArrayList<>();
        empires = new ArrayList<>();
        shopItems = new HashMap<>();
        shopItems.put("meat", 8);
        shopItems.put("bread", 8);
        shopItems.put("apple", 8);
        shopItems.put("cheese", 8);
        shopItems.put("wheat", 23);
        shopItems.put("flour", 32);
        shopItems.put("hop", 15);
        shopItems.put("ale", 20);
        shopItems.put("wood", 4);
        shopItems.put("stone", 14);
        shopItems.put("iron", 45);
        shopItems.put("pitch", 20);
        shopItems.put("spear", 20);
        shopItems.put("bow", 31);
        shopItems.put("mace", 58);
        shopItems.put("crossbow", 58);
        shopItems.put("pike", 36);
        shopItems.put("sword", 58);
        shopItems.put("leatherArmor", 25);
        shopItems.put("metalArmor", 58);
    }


    public static HashMap<String, Integer> getShopItems() {
        return shopItems;
    }

    public static int getPriceOfItem(String item) {
        if (shopItems.containsKey(item)) {
            return shopItems.get(item);
        }
        return 0;
    }

    public static ArrayList<Empire> getEmpires() {
        return empires;
    }

    public Empire getEmpireById(int id) {
        for (Empire empire : empires) {
            if (empire.getEmpireId() == id)
                return empire;
        }
        return null;
    }

    public int getTurnsCounter() {
        return turnsCounter;
    }

    public void setTurnsCounter(int turnsCounter) {
        this.turnsCounter = turnsCounter;
    }

    public ArrayList<Trade> getAllTrades() {
        return allTrades;
    }


}