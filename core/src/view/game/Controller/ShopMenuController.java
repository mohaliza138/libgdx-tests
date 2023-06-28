package view.game.Controller;

import view.game.Model.Game;

import java.util.Map;
import java.util.regex.Matcher;

public class ShopMenuController {

    public String showPriceList() {//item's name: name |buy: price |sell: price |you have 5 of this item
        StringBuilder output = new StringBuilder();
        for (Map.Entry<String, Integer> entry : Game.getShopItems().entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            Integer amount;
            if (GameMenuController.getCurrentEmpire().getResources().isResourceType(key)) {
                amount = GameMenuController.getCurrentEmpire().getResources().getResourceAmount(key);
            } else if (GameMenuController.getCurrentEmpire().getArmoury().isArmouryType(key)) {
                amount = GameMenuController.getCurrentEmpire().getArmoury().getArmouryAmount(key);
            } else {
                amount = (int) GameMenuController.getCurrentEmpire().getFoodStock().getFoodAmount(key);
            }
            output.append("item's name: ").append(key).append(" |buy: ").append(value).append(" |sell: ").append((int) (value * 0.8)).append(" |you have ").append(amount).append(" of this item").append('\n');
        }
        int money = GameMenuController.getCurrentEmpire().getResources().getGold();
        output.append("your money: ").append(money);
        return output.toString();
    }

    public static String buy(String name) {
        int amount = 10;
        if (!GameMenuController.getCurrentEmpire().getResources().isResourceType(name) && !GameMenuController.getCurrentEmpire().getArmoury().isArmouryType(name) && !GameMenuController.getCurrentEmpire().getFoodStock().isFoodType(name)) {
            return "invalid item";
        }
        if (GameMenuController.getCurrentEmpire().getResources().getGold() < Game.getShopItems().get(name) * amount) {
            return "Not enough gold!";
        }
        if (GameMenuController.getCurrentEmpire().getResources().isResourceType(name)) {
            if (GameMenuController.getCurrentEmpire().getResources().getFreeCapacityStockpile() < amount) {
                return "not enough space in the stockpile";
            }
        } else if (GameMenuController.getCurrentEmpire().getArmoury().isArmouryType(name)) {
            if (GameMenuController.getCurrentEmpire().getArmoury().getFreeCapacityArmoury() < amount) {
                return "not enough space in the armoury";
            }
        } else if (GameMenuController.getCurrentEmpire().getFoodStock().isFoodType(name)) {
            if (GameMenuController.getCurrentEmpire().getFoodStock().getFreeCapacityFoodStock() < amount) {
                return "not enough space in the foodStock";
            }
        }
        GameMenuController.getCurrentEmpire().getResources().addResource("gold", -1 * Game.getShopItems().get(name) * amount);
        GameMenuController.getCurrentEmpire().getResources().addResource(name, amount);
        GameMenuController.getCurrentEmpire().getArmoury().addArmoury(name, amount);
        GameMenuController.getCurrentEmpire().getFoodStock().addFood(name, amount);
        return "success";
    }

    public static String sell(String name) {
        int amount = 10;
        if (!GameMenuController.getCurrentEmpire().getResources().isResourceType(name) && !GameMenuController.getCurrentEmpire().getArmoury().isArmouryType(name) && !GameMenuController.getCurrentEmpire().getFoodStock().isFoodType(name)) {
            return "invalid item";
        }
        if (GameMenuController.getCurrentEmpire().getResources().isResourceType(name) && GameMenuController.getCurrentEmpire().getResources().getResourceAmount(name) < amount) {
            return "not enough resource in the stockpile";
        } else if (GameMenuController.getCurrentEmpire().getArmoury().isArmouryType(name) && GameMenuController.getCurrentEmpire().getArmoury().getArmouryAmount(name) < amount) {
            return "not enough resource in the armoury";
        } else if (GameMenuController.getCurrentEmpire().getFoodStock().isFoodType(name) && GameMenuController.getCurrentEmpire().getFoodStock().getFoodAmount(name) < amount) {
            return "not enough food in the foodStock";
        }
        if (GameMenuController.getCurrentEmpire().getResources().isResourceType(name)) {
            GameMenuController.getCurrentEmpire().getResources().addResource(name, -1 * amount);
        } else if (GameMenuController.getCurrentEmpire().getArmoury().isArmouryType(name)) {
            GameMenuController.getCurrentEmpire().getArmoury().addArmoury(name, -1 * amount);
        } else if (GameMenuController.getCurrentEmpire().getFoodStock().isFoodType(name)) {
            GameMenuController.getCurrentEmpire().getFoodStock().addFood(name, -1 * amount);
        }
        GameMenuController.getCurrentEmpire().getResources().addResource("gold", (int) (amount * Game.getShopItems().get(name) * 0.8));
        return "success";
    }
}