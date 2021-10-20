package com.anish.screen;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.Random;

import com.anish.calabashbros.BubbleSorter;
import com.anish.calabashbros.Calabash;
import com.anish.calabashbros.MatrixSorter;
import com.anish.calabashbros.World;

import asciiPanel.AsciiPanel;

public class WorldScreen implements Screen {

    private World world;
    private Calabash[][] bros;
    String[] sortSteps;

    private final static int row = 8;
    private final static int col = 8;

    public WorldScreen() {
        world = new World();
        bros = new Calabash[WorldScreen.row][WorldScreen.col];
        
        /**
        bros = new Calabash[7];
        bros[3] = new Calabash(new Color(204, 0, 0), 1, world);
        bros[5] = new Calabash(new Color(255, 165, 0), 2, world);
        bros[1] = new Calabash(new Color(252, 233, 79), 3, world);
        bros[0] = new Calabash(new Color(78, 154, 6), 4, world);
        bros[4] = new Calabash(new Color(50, 175, 255), 5, world);
        bros[6] = new Calabash(new Color(114, 159, 207), 6, world);
        bros[2] = new Calabash(new Color(173, 127, 168), 7, world);
        8 by 8 **/

        // initialize the sequence of cala bros
        int[] randomSeq = new int[WorldScreen.row * WorldScreen.col];
        Random generator = new Random(System.currentTimeMillis());
        for (int i = 0, r; i < randomSeq.length; ++i){
            r = generator.nextInt(i + 1);
            randomSeq[i] = randomSeq[r];
            randomSeq[r] = i;
        }

        for (int i = 0; i < WorldScreen.row; ++i){
            for (int j = 0; j < WorldScreen.col; ++j){
                int rank = randomSeq[i * WorldScreen.col + j];
                int div = (256 / WorldScreen.row / WorldScreen.col);
                bros[i][j] = new Calabash(new Color(100, 28 + rank * (div / 2), rank * (div / 2)), rank, world);
                world.put(bros[i][j], 12 + (j << 1), 3 + (i << 1));
            }
        }

        /**
        world.put(bros[0][0], 10, 10);
        world.put(bros[0][1], 12, 10);
        world.put(bros[0][2], 14, 10);
        world.put(bros[0][3], 16, 10);
        world.put(bros[0][4], 18, 10);
        world.put(bros[0][5], 20, 10);
        world.put(bros[0][6], 22, 10);
        BubbleSorter<Calabash> b = new BubbleSorter<>();
        b.load(bros);
        b.sort();
        **/

        MatrixSorter<Calabash> ms = new MatrixSorter<>();
        ms.load(bros);
        ms.sort();

        sortSteps = this.parsePlan(ms.getPlan());
    }

    private String[] parsePlan(String plan) {
        return plan.split("\n");
    }

    private void execute(Calabash[][] bros, String step) {
        String[] couple = step.split("<->");
        int rank1 = Integer.parseInt(couple[0]);
        int rank2 = Integer.parseInt(couple[1]);
        int y1 = rank1 / WorldScreen.col, x1 = rank1 % WorldScreen.col;
        int y2 = rank2 / WorldScreen.col, x2 = rank2 % WorldScreen.col;
        bros[y1][x1].swap(bros[y2][x2]);
        //getBroByRank(bros, Integer.parseInt(couple[0])).swap(getBroByRank(bros, Integer.parseInt(couple[1])));
    }

    /**
    private Calabash getBroByRank(Calabash[] bros, int rank) {
        for (Calabash bro : bros) {
            if (bro.getRank() == rank) {
                return bro;
            }
        }
        return null;
    }
    **/

    @Override
    public void displayOutput(AsciiPanel terminal) {

        for (int x = 0; x < World.WIDTH; x++) {
            for (int y = 0; y < World.HEIGHT; y++) {

                terminal.write(world.get(x, y).getGlyph(), x, y, world.get(x, y).getColor());

            }
        }
    }

    int i = 0;

    @Override
    public Screen respondToUserInput(KeyEvent key){
        
        if (i  < this.sortSteps.length) {
            this.execute(bros, sortSteps[i]);
            i++;
        }

        return this;
    }

}
