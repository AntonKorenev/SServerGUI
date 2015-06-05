/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AI;

/**
 * Created by ao on 15.05.15.
 */
public class Layer {
    String mParOneName;
    String mParTwoName;
    int mParOneStart, mParOneEnd, mParOneRange;
    int mParTwoStart, mParTwoEnd, mParTwoRange;
    Neuron[][] mNeuralGrid;

    public Layer(String name1, String name2, int s1, int s2, int e1, int e2, int r1, int r2){
        mParOneName = name1;
        mParOneStart = s1;
        mParOneEnd = e1;
        mParOneRange = r1;

        mParTwoName = name2;
        mParTwoStart = s2;
        mParTwoEnd = e2;
        mParTwoRange = r2;

        mNeuralGrid = new Neuron[r1][r2];
        
        formNeuralGrid();
    }

    private void formNeuralGrid(){
        float step1 = (mParOneEnd - mParOneStart)/(float)mParOneRange;
        float step2 = (mParTwoEnd - mParTwoStart)/(float)mParTwoRange;

        for(int i=0; i<mParOneRange; i++){
            for(int j=0; j<mParTwoRange; j++){
                mNeuralGrid[i][j] = new Neuron(mParOneStart+i*step1, mParOneEnd+j*step2);
            }
            mNeuralGrid[i][mParTwoRange/2].setWeight(1);
        }
    }

    public int prognosisForWeek(float[] forecast){
        int sum=0;
        //для каждого значения температуры из прогноза
        for(float value1: forecast){
            float prevLineValueOne = mNeuralGrid[0][0].mValueOne;
            int index1 = 0;

            //ищем на шкале y соответствующее значение или максимально близкое к нему
            for(int i=1; i< mNeuralGrid.length/mNeuralGrid[0].length; i++){
                if(value1 >= prevLineValueOne && value1 <= mNeuralGrid[i][0].mValueOne){
                    if(value1 < prevLineValueOne + ((mNeuralGrid[i][0].mValueOne - prevLineValueOne)/2) ){
                        index1 = i-1;
                    } else{
                        index1 = i;
                    }
                    break;
                }
                prevLineValueOne = mNeuralGrid[i-1][0].mValueOne;
            }

            //сумируем вес всей линии
            int lineWeight = 0;
            for(Neuron neuron: mNeuralGrid[index1]){
                lineWeight += neuron.mWeight;
            }

            //считаем мат ожидание
            for(Neuron neuron: mNeuralGrid[index1]){
                sum+=neuron.mValueTwo*neuron.mWeight/lineWeight;
            }

        }

        return sum/4;
    }

    public void addValueToGrid(float valueOne, float valueTwo){;
        //searching for index in grid for first value(row number)
        int index1 = 0;
        float prevLineValueOne = mNeuralGrid[0][0].mValueOne;
        for(int i=1; i< mNeuralGrid.length/mNeuralGrid[0].length; i++){
            if(valueOne >= prevLineValueOne && valueOne <= mNeuralGrid[i][0].mValueOne){
                if(valueOne < prevLineValueOne + ((mNeuralGrid[i][0].mValueOne - prevLineValueOne)/2) ){
                    index1 = i-1;
                } else{
                    index1 = i;
                }
                break;
            }
            prevLineValueOne = mNeuralGrid[i-1][0].mValueOne;
        }

        //searching for index in grid for second value(column number)
        int index2 = 0;
        float prevLineValueTwo = mNeuralGrid[0][0].mValueTwo;
        for(int j=1; j< mNeuralGrid[0].length; j++){
            if(valueTwo >= prevLineValueTwo && valueTwo <= mNeuralGrid[index1][j].mValueTwo){
                if(valueTwo < prevLineValueTwo + ((mNeuralGrid[index1][j].mValueTwo - prevLineValueTwo)/2) ){
                    index2 = j-1;
                } else{
                    index2 = j;
                }
                break;
            }
            prevLineValueTwo = mNeuralGrid[index1][j-1].mValueTwo;
        }

        //setting new neuron weight
        mNeuralGrid[index1][index2].addWeight(1);
    }
}
