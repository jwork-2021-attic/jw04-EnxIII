package world;

import java.util.List;

import asciiPanel.AsciiPanel;

public class MCreatureFactory {

    private Maze maze;

    public MCreatureFactory(Maze maze) {
        this.maze = maze;
    }

    public Creature newPlayer(List<String> messages) {
        Creature player = new Creature(this.maze, (char)2, AsciiPanel.brightWhite, 100, 20, 5, 9);
        // right in the center
        this.maze.addLocation(player, maze.width()/2, maze.height()/2);
        new PlayerAI(player, messages);
        return player;
    }

}
