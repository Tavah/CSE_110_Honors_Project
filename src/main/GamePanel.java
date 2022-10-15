package main;

import inputs.KeyboardInputs;
import inputs.MouseInputs;

import javax.swing.JPanel;

import java.awt.Dimension;
import java.awt.Graphics;

import static main.Game.GAME_HEIGHT;
import static main.Game.GAME_WIDTH;


public class GamePanel extends JPanel {

    private MouseInputs mouseInputs;
    private Game game;

    public GamePanel(Game game) {

    mouseInputs = new MouseInputs(this);
    this.game = game;

    setPanelSize();
    addKeyListener(new KeyboardInputs(this));
    addMouseListener(mouseInputs);
    addMouseMotionListener(mouseInputs);

    }

    private void setPanelSize() {
        Dimension size = new Dimension(GAME_WIDTH,  GAME_HEIGHT);
        setPreferredSize(size);
        System.out.println("Resolution: " + GAME_WIDTH + " : " + GAME_HEIGHT);


    }

    public void updateGame() {

    }

    //render game
    public void paintComponent(Graphics g) {

        //calls graphics superclass, prevents artifacts
        super.paintComponent(g);

        game.render(g);
    }

    public Game getGame(){
        return game;
    }

}