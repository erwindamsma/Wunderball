package mygame;

public class Player {
    private final Main main;
    private String name;
    private int score = 0;
    private int respawns = 0;
    
    public Player(String name, Main main){
        this.name = name;
        this.main = main;
    }
    
    public void setPlayerName(String name){
        this.name = name;
    }
    public String getPlayerName(){
        return this.name;
    }
    public void addPoint(){
        this.score++;
        main.updateHudText();
    }
    public int getScore(){
        return this.score;
    }
    public void addRespawn(){
        this.respawns++;
        if (this.score > 0)
            this.score--;
        main.updateHudText();
    }
    public int getRespawnCount(){
        return respawns;
    }
}
