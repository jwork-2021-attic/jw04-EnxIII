package screen;

import world.*;
import asciiPanel.AsciiPanel;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;


public class MPlayScreen implements Screen {

    private int buffx, buffy;
    private int transCoff;
    private int stepCount;
    private Maze maze;
    private Creature player;
    private int screenWidth;
    private int screenHeight;
    private List<String> messages;

    public MPlayScreen() {
        this.stepCount = 0;
        this.transCoff = 8;
        this.screenWidth = 60;
        this.screenHeight = 40;
        maze = createMaze();

        this.messages = new ArrayList<String>();
        //this.oldMessages = new ArrayList<String>();
        this.player = createPlayer(messages);
        maze.addPlayer(player);
    }

    private Creature createPlayer(List<String> messages) {
        return (new MCreatureFactory(this.maze)).newPlayer(messages);
    }

    private Maze createMaze(){
        String[] files = {
                            "world/bitmap_1.txt",
                            "world/bitmap_2.txt", 
                            "world/bitmap_3.txt", 
                            "world/bitmap_4.txt",
                            "world/bitmap_1.txt",
                            "world/bitmap_2.txt", 
                            "world/bitmap_3.txt", 
                            "world/bitmap_4.txt",
                            "world/bitmap_1.txt",
                            "world/bitmap_2.txt", 
                            "world/bitmap_3.txt", 
                            "world/bitmap_4.txt",
                            "world/bitmap_spawn.txt",
                            "world/bitmap_spawn.txt",
                            "world/bitmap_2.txt", 
                            "world/bitmap_3.txt", 
                            "world/bitmap_4.txt",
                            "world/bitmap_1.txt",
                            "world/bitmap_2.txt", 
                            "world/bitmap_3.txt", 
                            "world/bitmap_4.txt",
                            "world/bitmap_1.txt",
                            "world/bitmap_2.txt", 
                            "world/bitmap_3.txt", 
                            "world/bitmap_4.txt",
                        };
        int[] left={0,1,2,3,4,
                    0,1,2,3,4,
                    0,1,2,3,4,
                    0,1,2,3,4,
                    0,1,2,3,4
                    };
        int[] top ={0,0,0,0,0,
                    1,1,1,1,1,
                    2,2,2,2,2,
                    3,3,3,3,3,
                    4,4,4,4,4
                    };

        MazeGenerator mg = new MazeGenerator(85, 85);
        return mg.creatMaze(files, left, top, 17);
    }

    private void displayTiles(AsciiPanel terminal, int left, int top){
        for (int i = 0; i < this.screenWidth; ++i){
            for (int j = 0; j < this.screenHeight; ++j){
                int wy = j + top;
                int wx = i + left;
    
                if (player.canSee(wx, wy)) {
                    terminal.write(maze.glyph(wx, wy), i, j, maze.color(wy, wx));
                } else {
                    terminal.write((char)0, i, j, Color.BLACK);
                }
            }
        }
    }

    private void displayCreature(AsciiPanel terminal){}

    private void displayPlayer(AsciiPanel terminal, int left, int top){
        terminal.write(player.glyph(), player.x() - left, player.y() - top, player.color());
    }

    @Override
    public void displayOutput(AsciiPanel terminal){
        displayTiles(terminal, getScrollX(), getScrollY());
        //displayCreature(terminal);
        displayPlayer(terminal, getScrollX(), getScrollY());
    }

    @Override
    public Screen respondToUserInput(KeyEvent key){
        buffx = player.x();
        buffy = player.y();
        switch (key.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                player.moveBy(-1, 0);
                break;
            case KeyEvent.VK_RIGHT:
                player.moveBy(1, 0);
                break;
            case KeyEvent.VK_UP:
                player.moveBy(0, -1);
                break;
            case KeyEvent.VK_DOWN:
                player.moveBy(0, 1);
                break;
        }
        if (player.x() == 0 || player.x() == maze.width()-1 || player.y() == 0 || player.y() == maze.height()-1){
            return new WinScreen();
        }
        if (buffx != player.x() || buffy != player.y()){
            if (++stepCount == this.transCoff){
                stepCount = 0;
                maze.transForm();
            }
        }
        return this;
    }

    public int getScrollX() {
        return Math.max(0, Math.min(player.x() - screenWidth / 2, maze.width() - screenWidth));
    }

    public int getScrollY() {
        return Math.max(0, Math.min(player.y() - screenHeight / 2, maze.height() - screenHeight));
    }

}
