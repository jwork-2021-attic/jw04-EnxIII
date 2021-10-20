package world;

import java.io.BufferedReader;
import java.io.FileReader;

public class MazeGenerator {
    private int width;
    private int height;
    private Tile[][] tiles;
    private char[][] bitmap;

    public MazeGenerator(int height, int width){
        this.height = height;
        this.width = width;
        tiles = new Tile[width][height];
        bitmap = new char[height][width];
    }

    public int getHeight(){
        return this.height;
    }

    public int getWidth(){
        return this.width;
    }

    public Maze build(){
        return new Maze(tiles);
    }

    public void initBitMap(String file, int left, int top){ // (inner block)
        BufferedReader reader = null;
        try {
            String line = null;
            int xoffset = 0, yoffset = 0;
            reader = new BufferedReader(new FileReader(file));

            while ((line = reader.readLine()) != null) {
                for (var c : line.toCharArray()){
                    if ('0' <= c && c <= '1'){
                        bitmap[top + yoffset][left + xoffset] = c;
                        ++xoffset;
                    }
                }
                xoffset = 0;
                yoffset ++;
            }
            reader.close();
 
        }catch (Exception e){
        }finally{
            try{ reader.close();
            }catch (Exception e){}
        }
    }

    public Maze creatMaze(String[] files, int[] left, int[] top, int blocksize){
        if (files.length != left.length || left.length != top.length)
            return null;

        for (int i = 0; i < files.length; ++i){
            this.initBitMap(files[i], left[i] * blocksize, top[i] * blocksize);
        }

        for (int i = 0; i < height; ++i){
            for (int j = 0; j < width; ++j){
                switch (bitmap[i][j]){
                    // reverse
                    case '0':
                        tiles[j][i] = Tile.FLOOR;
                        break;
                    case '1':
                        tiles[j][i] = Tile.WALL;
                        break;
                }
            }
        }

        return new Maze(tiles);
    }

}
