package com.anish.calabashbros;

public class MatrixSorter<T extends Comparable<T>>{
    private T[][] data;
    private String log = "";

    private void swap(int i1, int j1, int i2, int j2) {
        T temp;
        temp = data[i1][j1];
        data[i1][j1] = data[i2][j2];
        data[i2][j2] = temp;
        log += (data[i1][j1] + "<->" + data[i2][j2] + "\n");
    }

    public void load(T[][] data){
        this.data = data;
    }

    public void sort(){
        boolean changed = true;
        while (changed){
            changed = false;
            for (int i = 0; i < data.length; ++i){
                for (int j = 0; j < data[i].length - 1; ++j){
                    if (data[i][j].compareTo(data[i][j + 1]) > 0){
                        this.swap(i, j, i, j + 1);
                        changed = true;
                    }
                }
            // interlines
                if (i < data.length - 1 && data[i][data[i].length - 1].compareTo(data[i + 1][0]) > 0){
                    this.swap(i, data[i].length - 1, i + 1, 0);
                    changed = true;
                }
            }
        }
    }
    
    public String getPlan() {
        return this.log;
    }
}
