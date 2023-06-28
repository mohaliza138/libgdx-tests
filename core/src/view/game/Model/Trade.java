package view.game.Model;

public class Trade {
    private String wantedResource;
    private int wantedResourceAmount;
    private String givenResource;
    private int givenResourceAmount;
    private String message;
    private Empire senderEmpire;
    private Empire recieverEmpire;
    private Empire getterEmpire;
    private boolean accepted;
    private boolean checkedByGetterEmpire;

    public Trade(String wantedResource, int wantedResourceAmount, String givenResource, int givenResourceAmount, String message, Empire senderEmpire , Empire getterEmpire) {
        this.wantedResource = wantedResource;
        this.wantedResourceAmount = wantedResourceAmount;
        this.givenResource = givenResource;
        this.givenResourceAmount = givenResourceAmount;
        this.message = message;
        this.senderEmpire = senderEmpire;
        this.getterEmpire = getterEmpire;
        this.accepted = false;
        this.checkedByGetterEmpire = false;
    }

    public Trade() {
    }

    public String getWantedResource() {
        return wantedResource;
    }

    public int getWantedResourceAmount() {
        return wantedResourceAmount;
    }

    public String getGivenResource() {
        return givenResource;
    }

    public int getGivenResourceAmount() {
        return givenResourceAmount;
    }

    public String getMessage() {
        return message;
    }

    public Empire getSenderEmpire() {
        return senderEmpire;
    }

    public Empire getGetterEmpire() {
        return getterEmpire;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public Empire getRecieverEmpire() {
        return recieverEmpire;
    }

    public boolean isCheckedByGetterEmpire() {
        return checkedByGetterEmpire;
    }

    public void setRecieverEmpire(Empire recieverEmpire) {
        this.recieverEmpire = recieverEmpire;
    }

    public void setCheckedByGetterEmpire(boolean checkedByGetterEmpire) {
        this.checkedByGetterEmpire = checkedByGetterEmpire;
    }

    public void setGetterEmpire(Empire getterEmpire) {
        this.getterEmpire = getterEmpire;
    }
}