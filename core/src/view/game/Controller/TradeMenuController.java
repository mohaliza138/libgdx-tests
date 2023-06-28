package view.game.Controller;



import view.game.Model.Empire;
import view.game.Model.Game;
import view.game.Model.Trade;

import java.util.regex.Matcher;

public class TradeMenuController {

    public static String trade(String message , String wantedResource , String givenResource , int wantedAmount , int givenAmount , String getterID) {

        boolean wantedResourceType = GameMenuController.getCurrentEmpire().getResources().isResourceType(wantedResource);
        boolean wantedArmouryType = GameMenuController.getCurrentEmpire().getArmoury().isArmouryType(wantedResource);
        boolean wantedFoodType = GameMenuController.getCurrentEmpire().getFoodStock().isFoodType(wantedResource);
        boolean givenResourceType = GameMenuController.getCurrentEmpire().getResources().isResourceType(givenResource);
        boolean givenArmouryType = GameMenuController.getCurrentEmpire().getArmoury().isArmouryType(givenResource);
        boolean givenFoodType = GameMenuController.getCurrentEmpire().getFoodStock().isFoodType(givenResource);
        Empire getterEmpire = null;
        for(Empire empire : Game.getEmpires())
        {
            if(String.valueOf(empire.getEmpireId()).equals(getterID))
            {
                getterEmpire = empire;
                break;
            }
        }

        if (!wantedResourceType && !wantedArmouryType && !wantedFoodType && !givenResourceType && !givenArmouryType && !givenFoodType) {
            return "invalid item";
        }
        if (givenResourceType && GameMenuController.getCurrentEmpire().getResources().getResourceAmount(givenResource) < givenAmount) {
            return "you don't have enough amount of this item";
        } else if (givenArmouryType && GameMenuController.getCurrentEmpire().getArmoury().getArmouryAmount(givenResource) < givenAmount) {
            return "you don't have enough amount of this item";
        } else if (givenFoodType && GameMenuController.getCurrentEmpire().getFoodStock().getFoodAmount(givenResource) < givenAmount) {
            return "you don't have enough amount of this item";
        }
        if (givenResourceType) {
            GameMenuController.getCurrentEmpire().getResources().addResource(givenResource, -1 * givenAmount);
        } else if (givenArmouryType) {
            GameMenuController.getCurrentEmpire().getArmoury().addArmoury(givenResource, -1 * givenAmount);
        } else if (givenFoodType) {
            GameMenuController.getCurrentEmpire().getFoodStock().addFood(givenResource, -1 * givenAmount);
        }
        GameMenuController.getGame().getAllTrades().add(new Trade(wantedResource, wantedAmount, givenResource, givenAmount, message, GameMenuController.getCurrentEmpire() , getterEmpire));
        return "success";
    }

    public static String listOfTradesThatUserHasSent() {// id-> 1 | wanted : food->5 | given : food->6 | sender'sID-> (int)EmpireID
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < GameMenuController.getGame().getAllTrades().size(); i++) {
            String givenResource = GameMenuController.getGame().getAllTrades().get(i).getGivenResource();
            String wantedResource = GameMenuController.getGame().getAllTrades().get(i).getWantedResource();
            int wantedResourceAmount = GameMenuController.getGame().getAllTrades().get(i).getWantedResourceAmount();
            int givenResourceAmount = GameMenuController.getGame().getAllTrades().get(i).getGivenResourceAmount();
            int senderId = GameMenuController.getGame().getAllTrades().get(i).getSenderEmpire().getEmpireId();
            int receiverId = GameMenuController.getGame().getAllTrades().get(i).getGetterEmpire().getEmpireId();
            String message = GameMenuController.getGame().getAllTrades().get(i).getMessage();
            boolean hasBeenAccepted = GameMenuController.getGame().getAllTrades().get(i).isAccepted();
            boolean hasBeenChecked = GameMenuController.getGame().getAllTrades().get(i).isCheckedByGetterEmpire();
            if (senderId != GameMenuController.getCurrentEmpire().getEmpireId()) {
                continue;
            }
            output.append("id-> ").append(i).append(" | you get : ").append(wantedResource).append("->").append(wantedResourceAmount).append(" | you give : ").append(givenResource).append("->").append(givenResourceAmount).append(" | receiver'sID->").append(receiverId).append(" | message->").append(message).append(" | is accepted: ").append(hasBeenAccepted).append(" | is checked: ").append(hasBeenChecked).append('\n');
        }
        return output.toString();
    }

    public static String listOfTradesThatHasBeenSentToUser() {// id-> 1 | wanted : food->5 | given : food->6 | sender'sID-> (int)EmpireID
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < GameMenuController.getGame().getAllTrades().size(); i++) {
            String givenResource = GameMenuController.getGame().getAllTrades().get(i).getGivenResource();
            String wantedResource = GameMenuController.getGame().getAllTrades().get(i).getWantedResource();
            int wantedResourceAmount = GameMenuController.getGame().getAllTrades().get(i).getWantedResourceAmount();
            int givenResourceAmount = GameMenuController.getGame().getAllTrades().get(i).getGivenResourceAmount();
            int senderId = GameMenuController.getGame().getAllTrades().get(i).getSenderEmpire().getEmpireId();
            int receiverId = GameMenuController.getGame().getAllTrades().get(i).getGetterEmpire().getEmpireId();
            String message = GameMenuController.getGame().getAllTrades().get(i).getMessage();
            boolean hasBeenAccepted = GameMenuController.getGame().getAllTrades().get(i).isAccepted();
            boolean hasBeenChecked = GameMenuController.getGame().getAllTrades().get(i).isCheckedByGetterEmpire();
            if (receiverId != GameMenuController.getCurrentEmpire().getEmpireId()) {
                continue;
            }
            output.append("id-> ").append(i).append(" | you get : ").append(givenResource).append("->").append(wantedResourceAmount).append(" | you give : ").append(wantedResource).append("->").append(givenResourceAmount).append(" | receiver'sID->").append(receiverId).append(" | message->").append(message).append(" | is accepted: ").append(hasBeenAccepted).append(" | is checked: ").append(hasBeenChecked).append('\n');
        }
        return output.toString();
    }



    public static String tradeAccept(int tradeId) {
        if (tradeId < 0 || tradeId >= GameMenuController.getGame().getAllTrades().size()) {
            return "invalid trade ID";
        }
        String givenResource = GameMenuController.getGame().getAllTrades().get(tradeId).getGivenResource();
        String wantedResource = GameMenuController.getGame().getAllTrades().get(tradeId).getWantedResource();
        String message = GameMenuController.getGame().getAllTrades().get(tradeId).getMessage();
        int wantedResourceAmount = GameMenuController.getGame().getAllTrades().get(tradeId).getWantedResourceAmount();
        int givenResourceAmount = GameMenuController.getGame().getAllTrades().get(tradeId).getGivenResourceAmount();
        int senderId = GameMenuController.getGame().getAllTrades().get(tradeId).getSenderEmpire().getEmpireId();
        int getterID = GameMenuController.getGame().getAllTrades().get(tradeId).getGetterEmpire().getEmpireId();
        boolean wantedResourceType = GameMenuController.getCurrentEmpire().getResources().isResourceType(wantedResource);
        boolean wantedArmouryType = GameMenuController.getCurrentEmpire().getArmoury().isArmouryType(wantedResource);
        boolean wantedFoodType = GameMenuController.getCurrentEmpire().getFoodStock().isFoodType(wantedResource);
        boolean givenResourceType = GameMenuController.getCurrentEmpire().getResources().isResourceType(givenResource);
        boolean givenArmouryType = GameMenuController.getCurrentEmpire().getArmoury().isArmouryType(givenResource);
        boolean givenFoodType = GameMenuController.getCurrentEmpire().getFoodStock().isFoodType(givenResource);
        if (wantedResourceType) {
            if (GameMenuController.getCurrentEmpire().getResources().getResourceAmount(wantedResource) < wantedResourceAmount) {
                return "you don't have enough resource to complete the trade";
            }
            GameMenuController.getCurrentEmpire().getResources().addResource(wantedResource, -1 * wantedResourceAmount);
            GameMenuController.getGame().getEmpires().get(senderId).getResources().addResource(wantedResource, wantedResourceAmount);
        } else if (wantedArmouryType) {
            if (GameMenuController.getCurrentEmpire().getArmoury().getArmouryAmount(wantedResource) < wantedResourceAmount) {
                return "you don't have enough armour to complete the trade";
            }
            GameMenuController.getCurrentEmpire().getArmoury().addArmoury(wantedResource, -1 * wantedResourceAmount);
            GameMenuController.getGame().getEmpires().get(senderId).getArmoury().addArmoury(wantedResource, wantedResourceAmount);
        } else if (wantedFoodType) {
            if (GameMenuController.getCurrentEmpire().getFoodStock().getFoodAmount(wantedResource) < wantedResourceAmount) {
                return "you don't have enough food to complete the trade";
            }
            GameMenuController.getCurrentEmpire().getFoodStock().addFood(wantedResource, -1 * wantedResourceAmount);
            GameMenuController.getGame().getEmpires().get(senderId).getFoodStock().addFood(wantedResource, wantedResourceAmount);
        }
        if (givenResourceType) {
            GameMenuController.getCurrentEmpire().getResources().addResource(givenResource, givenResourceAmount);
        } else if (givenArmouryType) {
            GameMenuController.getCurrentEmpire().getArmoury().addArmoury(givenResource, givenResourceAmount);
        } else if (givenFoodType) {
            GameMenuController.getCurrentEmpire().getFoodStock().addFood(givenResource, givenResourceAmount);
        }
//        GameMenuController.getGame().getAllTrades().remove(tradeId);
//        GameMenuController.getGame().getAllTrades().add(new Trade(wantedResource, wantedResourceAmount, givenResource, givenResourceAmount, message, GameMenuController.getGame().getEmpires().get(senderId)));
//        GameMenuController.getGame().getAllTrades().get(GameMenuController.getGame().getAllTrades().size() - 1).setGetterEmpire(GameMenuController.getCurrentEmpire());
        GameMenuController.getGame().getAllTrades().get(tradeId).setAccepted(true);
        GameMenuController.getGame().getAllTrades().get(tradeId).setCheckedByGetterEmpire(true);
        return "success";
    }

    public static String tradeReject(int tradeId) {
        if (tradeId < 0 || tradeId >= GameMenuController.getGame().getAllTrades().size()) {
            return "invalid trade ID";
        }

        GameMenuController.getGame().getAllTrades().get(tradeId).setAccepted(false);
        GameMenuController.getGame().getAllTrades().get(tradeId).setCheckedByGetterEmpire(true);
        return "successfully rejected";
    }

    public String tradeHistory() {// id-> 1 | wanted : food->5 | given : food->6 | sender'sID->1 | getter'sID->2
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < GameMenuController.getGame().getAllTrades().size(); i++) {
            String givenResource = GameMenuController.getGame().getAllTrades().get(i).getGivenResource();
            String wantedResource = GameMenuController.getGame().getAllTrades().get(i).getWantedResource();
            int wantedResourceAmount = GameMenuController.getGame().getAllTrades().get(i).getWantedResourceAmount();
            int givenResourceAmount = GameMenuController.getGame().getAllTrades().get(i).getGivenResourceAmount();
            int senderId = GameMenuController.getGame().getAllTrades().get(i).getSenderEmpire().getEmpireId();
            int getterId = GameMenuController.getGame().getAllTrades().get(i).getGetterEmpire().getEmpireId();
            output.append("id-> ").append(i).append(" | wanted : ").append(wantedResource).append("->").append(wantedResourceAmount).append(" | given : ").append(givenResource).append("->").append(givenResourceAmount).append(" | sender'sID->").append(senderId).append(" | getter'sID->").append(getterId).append('\n');
        }
        return output.toString();
    }
}