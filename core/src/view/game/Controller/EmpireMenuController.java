package view.game.Controller;

public class EmpireMenuController {
    // for effect of the variety of foods I calculate that in the model.Empire in the getFoodPopularity method

    public static void calculatePopularityFactors() {
        switch (GameMenuController.getCurrentEmpire().getFoodRate()) {
            case -2:
                GameMenuController.getCurrentEmpire().setFoodPopularity(-8);
                break;
            case -1:
                GameMenuController.getCurrentEmpire().setFoodPopularity(-4);
                break;
            case 0:
                GameMenuController.getCurrentEmpire().setFoodPopularity(0);
                break;
            case 1:
                GameMenuController.getCurrentEmpire().setFoodPopularity(4);
                break;
            case 2:
                GameMenuController.getCurrentEmpire().setFoodPopularity(8);
                break;
        }
        switch (GameMenuController.getCurrentEmpire().getTaxRate()) {
            case -3:
                GameMenuController.getCurrentEmpire().setTaxPopularity(7);
                break;
            case -2:
                GameMenuController.getCurrentEmpire().setTaxPopularity(5);
                break;
            case -1:
                GameMenuController.getCurrentEmpire().setTaxPopularity(3);
                break;
            case 0:
                GameMenuController.getCurrentEmpire().setTaxPopularity(1);
                break;
            case 1:
                GameMenuController.getCurrentEmpire().setTaxPopularity(-2);
                break;
            case 2:
                GameMenuController.getCurrentEmpire().setTaxPopularity(-4);
                break;
            case 3:
                GameMenuController.getCurrentEmpire().setFearPopularity(-6);
                break;
            case 4:
                GameMenuController.getCurrentEmpire().setTaxPopularity(-8);
                break;
            case 5:
                GameMenuController.getCurrentEmpire().setTaxPopularity(-12);
                break;
            case 6:
                GameMenuController.getCurrentEmpire().setTaxPopularity(-16);
                break;
            case 7:
                GameMenuController.getCurrentEmpire().setTaxPopularity(-20);
                break;
            case 8:
                GameMenuController.getCurrentEmpire().setTaxPopularity(-24);
                break;
        }
        GameMenuController.getCurrentEmpire().setFearPopularity(GameMenuController.getCurrentEmpire().getFearRate());
    }


    public static int sumOfPop() {
        int popularity = GameMenuController.getCurrentEmpire().getFearPopularity();
        popularity += GameMenuController.getCurrentEmpire().getFoodPopularity();
        popularity += GameMenuController.getCurrentEmpire().getReligionPopularity();
        popularity += GameMenuController.getCurrentEmpire().getTaxPopularity();
        popularity += GameMenuController.getCurrentEmpire().getAleCoverage();
        popularity -= GameMenuController.getCurrentEmpire().getDisaffectionFromPoisonedCells();
        return popularity;
    }

    public static void calculatePopulation() {
        int employedPeople = GameMenuController.getCurrentEmpire().getEmployedPeople();
        int maxPopulation = GameMenuController.getCurrentEmpire().getMaxPopulation();
        int unemployedPeople = GameMenuController.getCurrentEmpire().getUnemployedPeople();
        int popularity = GameMenuController.getCurrentEmpire().getFearPopularity();
        popularity += GameMenuController.getCurrentEmpire().getFoodPopularity();
        popularity += GameMenuController.getCurrentEmpire().getReligionPopularity();
        popularity += GameMenuController.getCurrentEmpire().getTaxPopularity();
        popularity += GameMenuController.getCurrentEmpire().getAleCoverage();
        popularity += GameMenuController.getCurrentEmpire().getDisaffectionFromPoisonedCells();
        popularity *= 3;// 3*popularity effects our unemployedPeople
        if (unemployedPeople + employedPeople + popularity >= maxPopulation) {
            GameMenuController.getCurrentEmpire().setUnemployedPeople(maxPopulation - employedPeople);
        } else if (unemployedPeople + popularity <= 0) {
            GameMenuController.getCurrentEmpire().setUnemployedPeople(0);
        } else {
            GameMenuController.getCurrentEmpire().addUnemployedPeople(popularity);
        }
    }

    public static void calculateFoodAndTax() {// if your empire doesn't have enough food or gold your food and your tax rate will be change and you should use change rate commands to set them in another rate
        int allPeople = GameMenuController.getCurrentEmpire().getUnemployedPeople() + GameMenuController.getCurrentEmpire().getEmployedPeople();
        double allFood = GameMenuController.getCurrentEmpire().getFoodStock().getApple() + GameMenuController.getCurrentEmpire().getFoodStock().getBread() + GameMenuController.getCurrentEmpire().getFoodStock().getMeat() + GameMenuController.getCurrentEmpire().getFoodStock().getCheese();
        int gold = GameMenuController.getCurrentEmpire().getResources().getGold();
        if (GameMenuController.getCurrentEmpire().getFoodRate() == -1) {
            if (allFood < allPeople * 0.5) {
                GameMenuController.getCurrentEmpire().setFoodRate(-2);
            }
        } else if (GameMenuController.getCurrentEmpire().getFoodRate() == 0) {
            if (allFood < allPeople * 1.0) {
                if (allFood < allPeople * 0.5) {
                    GameMenuController.getCurrentEmpire().setFoodRate(-2);
                } else {
                    GameMenuController.getCurrentEmpire().setFoodRate(-1);
                }
            }
        } else if (GameMenuController.getCurrentEmpire().getFoodRate() == 1) {
            if (allFood < allPeople * 1.5) {
                if (allFood > allPeople * 1.0) {
                    GameMenuController.getCurrentEmpire().setFoodRate(0);
                } else if (allFood > allPeople * 0.5) {
                    GameMenuController.getCurrentEmpire().setFoodRate(-1);
                } else {
                    GameMenuController.getCurrentEmpire().setFoodRate(-2);
                }
            }
        } else if (GameMenuController.getCurrentEmpire().getFoodRate() == 2) {
            if (allFood < allPeople * 2.0) {
                if (allFood > allPeople * 1.5) {
                    GameMenuController.getCurrentEmpire().setFoodRate(1);
                } else if (allFood > allPeople * 1.0) {
                    GameMenuController.getCurrentEmpire().setFoodRate(0);
                } else if (allFood > allPeople * 0.5) {
                    GameMenuController.getCurrentEmpire().setFoodRate(-1);
                } else {
                    GameMenuController.getCurrentEmpire().setFoodRate(-2);
                }
            }
        }
        GameMenuController.getCurrentEmpire().calculateReductionInTheFoodStock();
        if (GameMenuController.getCurrentEmpire().getTaxRate() == -1) {
            if (gold < allPeople * 0.6) {
                GameMenuController.getCurrentEmpire().setTaxRate(0);
            }
        } else if (GameMenuController.getCurrentEmpire().getTaxRate() == -2) {
            if (gold < allPeople * 0.8) {
                if (gold < allPeople * 0.6) {
                    GameMenuController.getCurrentEmpire().setTaxRate(0);
                } else {
                    GameMenuController.getCurrentEmpire().setTaxRate(-1);
                }
            }
        } else if (GameMenuController.getCurrentEmpire().getTaxRate() == -3) {
            if (gold < allPeople) {
                if (gold > allPeople * 0.8) {
                    GameMenuController.getCurrentEmpire().setTaxRate(-2);
                } else if (gold > allPeople * 0.6) {
                    GameMenuController.getCurrentEmpire().setTaxRate(-1);
                } else {
                    GameMenuController.getCurrentEmpire().setTaxRate(0);
                }
            }
        }
        GameMenuController.getCurrentEmpire().calculateTaxRate();
    }

    public static void checkEffectOfFearRate() {// fear rate cause buildings and units do better
        int fearRate = GameMenuController.getCurrentEmpire().getFearRate();
        for (int i = 0; i < GameMenuController.getCurrentEmpire().getUnits().size(); i++) {
            int defaultAttackPower = GameMenuController.getCurrentEmpire().getUnits().get(i).getUnitType().getAttackPower();
            GameMenuController.getCurrentEmpire().getUnits().get(i).setAttackPower((int) (defaultAttackPower * (1 + (fearRate * 0.1))));
        }
        for (int i = 0; i < GameMenuController.getCurrentEmpire().getBuildings().size(); i++) {
            int defaultWorkingRate = GameMenuController.getCurrentEmpire().getBuildings().get(i).getBuildingType().getRate();
            GameMenuController.getCurrentEmpire().getBuildings().get(i).setRate(defaultWorkingRate - fearRate);
        }
    }
}
