package fi.academy;

public
class ToDo {
    private int id;
    private String uppgift;
    private boolean fardig;

    public ToDo(){}

    public
    ToDo(String uppgift, boolean fardig) {
        this.uppgift = uppgift;
        this.fardig = fardig;
    }

    public
    int getId() {
        return id;
    }

    public
    void setId(int id) {
        this.id = id;
    }

    public
    String getUppgift() {
        return uppgift;
    }

    public
    void setUppgift(String uppgift) {
        this.uppgift = uppgift;
    }

    @Override
    public
    String toString() {
        return "Numero: " + id + "/n Tehtävä: " + uppgift;
    }

    public
    boolean isFardig() {
        return fardig;
    }

    public
    void setFardig(boolean fardig) {
        this.fardig = fardig;
    }
}
