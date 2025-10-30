package io.blackdeluxecat.painttd.utils.math;


import java.util.LinkedList;
//该类用于方便直接直接进行数学意义的运算
public class FloatVector {
    private LinkedList<Float> valueList = new LinkedList<>();

    public FloatVector(int size) {
        for (int i = 0; i < size; i++) {
            valueList.add(0.0f);
        }
    }
    public FloatVector(float... values) {
        for (float value : values) {
            valueList.add(value);
        }
    }



    //获取元素
    public float get(int index) {
        return valueList.get(index);
    }

    //修改元素
    public void set(int index, float value) {
        valueList.set(index, value);
    }
    //添加元素
    public void expand(float ...values){
        for (float value : values){
            valueList.add(value);
        }
    }
    //移除元素
    public float remove(int index) {
        return valueList.remove(index);
    }

    //向量相加
    public void add(FloatVector v) {
        //用0补齐
        while (this.valueList.size() < v.valueList.size()) {
            this.expand(0.0f);
        }
        for (int i = 0; i < v.valueList.size(); i++) {
            this.valueList.set(i, this.get(i) + v.get(i));
        }
    }

    //向量数乘
    public void multiply(float value) {
        for (int i = 0; i < this.valueList.size(); i++) {
            this.valueList.set(i, this.get(i) * value);
        }
    }

    //更高阶的数量积我不会了qwq

}
