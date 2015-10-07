package mygame;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class MyStartScreen implements ScreenController{
    private Nifty nifty;
    private Main main;
    
    public MyStartScreen(Main main, Nifty nifty){
        this.nifty = nifty;
        this.main = main;
    }
    
    public void startGame(){
        nifty.exit();
    }

    @Override public void bind(Nifty nifty, Screen screen) { }
    @Override public void onStartScreen() { }
    @Override public void onEndScreen() {
        main.activateGame(nifty.getCurrentScreen().findNiftyControl("playername", TextField.class).getRealText());
    }
}
