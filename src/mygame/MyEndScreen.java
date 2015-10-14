package mygame;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ElementBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.loaderv2.types.ElementType;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MyEndScreen implements ScreenController{
    private Nifty nifty;
    
    public MyEndScreen(Nifty nifty){
        this.nifty = nifty;
    }
    
    @Override public void bind(Nifty nifty, Screen screen) { }

    @Override public void onStartScreen() { 
        List<String[]> lBoard = new LeaderboardHandler().getTop10();
        String lBoardRanks = "Rank\n";
        String lBoardNames = "Name\n";
        String lBoardScores = "Score\n";
        for (int i = 0; i < lBoard.size(); i++){
            lBoardRanks += i+1 + ")\n";
            lBoardNames += lBoard.get(i)[0] + "\n";
            lBoardScores += lBoard.get(i)[1] + "\n";
        }
        
        Element panel2 = nifty.getCurrentScreen().findElementByName("panel_3");
        Screen currentScreen = nifty.getCurrentScreen();
        TextBuilder lbNr = new TextBuilder();{{
            lbNr.id("lbNr");
            lbNr.text(lBoardRanks);
            lbNr.font("Interface/Fonts/Default.fnt");
            lbNr.color("#000000");
            lbNr.width("20%");
            lbNr.height("100%");
            lbNr.textHAlign(ElementBuilder.Align.Right);
            lbNr.marginRight("5");
            lbNr.textVAlignTop();
        }};
        TextBuilder lbName = new TextBuilder();{{
            lbName.id("lbName");
            lbName.text(lBoardNames);
            lbName.font("Interface/Fonts/Default.fnt");
            lbName.color("#000000");
            lbName.width("50%");
            lbName.height("100%");
            lbName.textHAlign(ElementBuilder.Align.Left);
            lbName.marginRight("5");
            lbName.textVAlignTop();
        }};
        TextBuilder lbScore = new TextBuilder();{{
            lbScore.id("lbScore");
            lbScore.text(lBoardScores);
            lbScore.font("Interface/Fonts/Default.fnt");
            lbScore.color("#000000");
            lbScore.width("30%");
            lbScore.height("100%");
            lbScore.textHAlign(ElementBuilder.Align.Left);
            lbScore.textVAlignTop();
        }};
        lbNr.build(nifty, currentScreen, panel2);
        lbName.build(nifty, currentScreen, panel2);
        lbScore.build(nifty, currentScreen, panel2);
    }

    @Override public void onEndScreen() { }
}
