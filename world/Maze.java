package world;

import java.util.Random;

public class Maze extends World{

    private Creature player;
    private int[][] shiftSeq = {{0, 0, 0, 1},{0, 1, 0, 2},{0, 2, 0, 3},
                                {0, 3, 0, 4},{0, 4, 1, 4},{1, 4, 2, 4},
                                {2, 4, 3, 4},{3, 4, 4, 4},{4, 4, 4, 3},
                                {4, 3, 4, 2},{4, 2, 4, 1},{4, 1, 4, 0},
                                {4, 0, 3, 0},{3, 0, 2, 0},{2, 0, 1, 0},
                                {1, 1, 2, 1},{2, 1, 3, 1},{3, 1, 3, 2},
                                {3, 2, 3, 3},{3, 3, 2, 3},{2, 3, 1, 3},{1, 3, 1, 2}};
    private int[][] exitSeq  = {{0, 0},{0, 1},{0, 2},{0, 3},
                                {0, 4},{1, 0},{2, 0},{3, 0},
                                {4, 0},{4, 1},{4, 2},{4, 3},
                                {4, 4},{3, 4},{2, 4},{1, 4},
                                {1, 1},{1, 3},{3, 1},{3, 3}};
    private int[][] rotaSeq = {{1, 2},{3, 2},{2, 1},{3, 2}};


    public Maze(Tile[][] tiles) {
        super(tiles);
        rand = new Random(System.currentTimeMillis());


    }

    public void addPlayer(Creature player){
        this.player = player;
    }

    public boolean addLocation(Creature player, int x, int y){
        if (tiles[x][y] == Tile.WALL)
            return false;
        player.setX(x);
        player.setY(y);
        return true;
    }

    /** 
     * clockwise 90 degree
     */
    public void blockRotation(int Col, int Row, int blocksize){
        int r = (blocksize>>1)-1;
        int c = (blocksize-1)>>1;
        int left = Col * blocksize, top = Row * blocksize;
        Tile temp = null;
        for(int j = c; j >= 0; --j) {
            for(int i = r; i >= 0; --i) {
                temp = tiles[j + left][i + top];
                tiles[j + left][i + top] = tiles[i + left][blocksize-j-1 + top];
                tiles[i + left][blocksize-j-1 + top] = tiles[blocksize-j-1 + left][blocksize-i-1 + top];
                tiles[blocksize-j-1 + left][blocksize-i-1 + top] = tiles[blocksize-i-1 + left][j + top];
                tiles[blocksize-i-1 + left][j + top] = temp;
            }
        }
    }

    public void blockShift(int fromCol, int fromRow, int toCol, int toRow, int blocksize){
        int rx1 = fromCol * blocksize, ry1 = fromRow * blocksize;
        int rx2 = toCol* blocksize, ry2 = toRow * blocksize;
        Tile temp = null;
        for (int i = 0; i < blocksize; ++i){
            for (int j = 0; j < blocksize; ++j){
                temp = tiles[rx1 + i][ry1 + j];
                tiles[rx1 + i][ry1 + j] = tiles[rx2 + i][ry2 + j];
                tiles[rx2 + i][ry2 + j] = temp;
            }
        }

    }

    public void blockInvert(int Col, int Row, int blocksize){
        int startCol = blocksize * Col;
        int startRow = blocksize * Row;
        for (int i = 0; i < blocksize; ++i){
            for (int j = i + 1; j < blocksize; ++j){
                Tile temp = tiles[startCol + i][startRow + j];
                tiles[startCol + i][startRow + j] = tiles[startCol + j][startRow + i];
                tiles[startCol + j][startRow + i] = temp;
            }
        }
    }

    Random rand;

    public void switchExit(int Col, int Row, int blocksize){
        int ex = rand.nextInt(4);
        int rx = Col * blocksize, ry = Row * blocksize;
        tiles[rx][ry + (blocksize-1) / 2] = Tile.WALL;
        tiles[rx + blocksize-1][ry + (blocksize-1) / 2] = Tile.WALL;
        tiles[rx + (blocksize-1) / 2][ry] = Tile.WALL;
        tiles[rx + (blocksize-1) / 2][ry + blocksize-1] = Tile.WALL;
        switch(ex){
            case 0:
                tiles[rx][ry + (blocksize-1) / 2] = Tile.FLOOR;
                tiles[rx + blocksize-1][ry + (blocksize-1) / 2] = Tile.FLOOR;
                break;
            case 1:
                tiles[rx][ry + (blocksize-1) / 2] = Tile.FLOOR;
                tiles[rx + (blocksize-1) / 2][ry] = Tile.FLOOR;
                break;
            case 2:
                tiles[rx][ry + (blocksize-1) / 2] = Tile.FLOOR;
                tiles[rx + (blocksize-1) / 2][ry + blocksize-1] = Tile.FLOOR;
                break;
            case 3:
                tiles[rx + blocksize-1][ry + (blocksize-1) / 2] = Tile.FLOOR;
                tiles[rx + (blocksize-1) / 2][ry] = Tile.FLOOR;
                break;
            case 4:
                tiles[rx + blocksize-1][ry + (blocksize-1) / 2] = Tile.FLOOR;
                tiles[rx + (blocksize-1) / 2][ry + blocksize-1] = Tile.FLOOR;
                break;
            case 5:
                tiles[rx + (blocksize-1) / 2][ry] = Tile.FLOOR;
                tiles[rx + (blocksize-1) / 2][ry + blocksize-1] = Tile.FLOOR;
                break;
        }
    }

    public void transForm(){
        int blocksize = 17;
        int X = player.x() / blocksize;
        int Y = player.y() / blocksize;
        // Outer shift
        for (int i = 0; i < shiftSeq.length; ++i){
            if ((shiftSeq[i][0] != Y || shiftSeq[i][1] != X) && (shiftSeq[i][2] != Y || shiftSeq[i][3] != X))
                blockShift(shiftSeq[i][1], shiftSeq[i][0], shiftSeq[i][3], shiftSeq[i][2], blocksize);
        }

        // random rotation
        for (int i = 0; i < rotaSeq.length; ++i){
            if (rotaSeq[i][0] != Y || rotaSeq[i][1] != X)
                blockRotation(rotaSeq[i][1], rotaSeq[i][0], blocksize);
        }
        

        // random invert
        for (int i = 0, R, C; i < 4; ++i){
            R = rand.nextInt(5);
            C = rand.nextInt(5);
            if (C != X || R != Y){
                blockInvert(C, R, blocksize);
            }
        }

        // random exit
        for (int i = 0; i < exitSeq.length; ++i){
            if (exitSeq[i][0] != Y || exitSeq[i][1] != X)
                switchExit(exitSeq[i][1], exitSeq[i][0], blocksize);
        }
    }

}
