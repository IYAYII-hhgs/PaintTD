package io.blackdeluxecat.painttd.utils.math;

import java.util.ArrayList;
import java.util.Collections;

/*将矩阵理解为一组列向量
 *运算方法等有空再说（）

 */
public class Matrix {

    public int column;
    public int row;
    public ArrayList<Vector> rows = new ArrayList<>();
    public Matrix(int column){
        this.column = column;
    }

    public Matrix(int column, Vector ... vs){
        this.column = column;
        Collections.addAll(this.rows, vs);
        this.row = rows.size();
    }

    public void add(Matrix matrix){
        //偷懒，直接抛出
        if (matrix.column != this.column||matrix.row != this.row){
            throw new IllegalArgumentException("Matrix should be the same size");
        }
    }
}
