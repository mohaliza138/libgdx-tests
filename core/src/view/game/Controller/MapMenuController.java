package view.game.Controller;


import view.game.Model.Map;
import view.game.Model.Unit;

public class MapMenuController {

    public static String showDetail(int x, int y) {
        Map map = GameMenuController.map;
        String stringShowDetail = "type : " + map.getMap()[x][y].getType();
        if (map.getMap()[x][y].getBuilding() != null) {
            stringShowDetail += "\nbuilding : " + map.getMap()[x][y].getBuilding().getBuildingType().getName() + "(hp:" + map.getMap()[x][y].getBuilding().getHp() + ")";
            if (map.getMap()[x][y].getBuilding().getOwner() != null)
                stringShowDetail += map.getMap()[x][y].getBuilding().getOwner().getUser().getUsername();
        }
        for (Unit unit : map.getMap()[x][y].getUnits()) {
            if (unit != null) {
                stringShowDetail += "\nUnit : " + unit.getUnitType().getName() + "(hp:" + unit.getHp() + ")" +"(mode:"+unit.getMode()+")";
                if (unit.getOwner() != null) stringShowDetail += " owner: " + unit.getOwner().getUser().getUsername();
            }
        }
        if (map.getMap()[x][y].getEnvironmentName() != null && !map.getMap()[x][y].getEnvironmentName().equals("rock"))
            stringShowDetail += "\nresource : wood";
        if (map.getMap()[x][y].getType().equals("boulder")) stringShowDetail += "\nresource : stone";
        if (map.getMap()[x][y].getType().equals("oil")) stringShowDetail += "\nresource : pitch";
        if (map.getMap()[x][y].getType().equals("ironTexture")) stringShowDetail += "\nresource : iron";
        return stringShowDetail;
    }
}