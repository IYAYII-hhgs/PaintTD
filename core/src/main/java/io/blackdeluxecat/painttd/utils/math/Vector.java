package io.blackdeluxecat.painttd.utils.math;


import java.util.LinkedList;

//每个Vector对象作为矩阵的一列
public class Vector {
    LinkedList<Float> valueList = new LinkedList<>();

    public Vector(float... values) {
        for (float value : values) {
            valueList.add(value);
        }
    }

    public void add(Vector v) {
        //解决此短彼长
        while (this.valueList.size() < v.valueList.size()) {
            this.add(0.0f);
        }
        //无需解决此长彼短
        for (int i = 0; i < v.valueList.size(); i++) {
            valueList.set(i, valueList.get(i) + v.valueList.get(i));
        }
    }

    public void add(float value){
        valueList.add(value);
    }

    public float remove(int index) {
        return valueList.remove(index);
    }

}
