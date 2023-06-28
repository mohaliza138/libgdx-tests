package view.game.Enums;

public enum EmpireColors {
    COLOR1(0, "red"),
    COLOR2(1, "orange"),
    COLOR3(2, "yellow"),
    COLOR4(3, "blue"),
    COLOR5(4, "gray"),
    COLOR6(5, "purple"),
    COLOR7(6, "brown"),
    COLOR8(7, "green");

    private final int numberOfColor;
    private final String name;

    EmpireColors(int numberOfColor, String name) {
        this.numberOfColor = numberOfColor;
        this.name = name;
    }

    public static EmpireColors getEmpireColorByNumber(int numberOfColor) {
        for (EmpireColors colors : EmpireColors.values()) {
            if (numberOfColor == colors.numberOfColor)
                return colors;
        }
        return null;
    }

    public int getNumberOfColor() {
        return numberOfColor;
    }

    public String getName() {
        return name;
    }
}
