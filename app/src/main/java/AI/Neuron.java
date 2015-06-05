/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AI;

/**
 * Created by ao on 15.05.15.
 */
public class Neuron {
    protected int mWeight;
    public float mValueOne;
    public float mValueTwo;

    public Neuron(float value1, float value2){
        mValueOne = value1;
        mValueTwo = value2;
        mWeight = 0;
    }

    public Neuron(float value1, float value2, int weight){
        mValueOne = value1;
        mValueTwo = value2;
        mWeight = weight;
    }

    public void setWeight(int newWeight){
        mWeight = newWeight;
    }

    public void addWeight(int addValue){
        mWeight+=addValue;
    }
}
