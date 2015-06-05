/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connectors;

import java.util.ArrayList;

/**
 *
 * @author ao
 */
public class Message {
    public static enum ACTIONS{GET,SET,TURN}
    public static enum PARAMETER{TEMPERATURE, HUMIDITY, LUMINOSITY, PRESSURE, ENERGY_USAGE}
    
    private ACTIONS mAction;
    private PARAMETER mParameter;
    private float mSetValue;
    private boolean mSwitchValue;
    private ArrayList<? extends Number> mRespondValues;

    private Message(ACTIONS action, PARAMETER parameter){
        mAction = action;
        mParameter = parameter;
    }

    private Message(ACTIONS action, PARAMETER parameter, float setValue){
        mAction = action;
        mParameter = parameter;
        mSetValue = setValue;
    }

    private Message(ACTIONS action, PARAMETER parameter, boolean switchValue){
        mAction = action;
        mParameter = parameter;
        mSwitchValue = switchValue;
    }

    private Message(ACTIONS action, PARAMETER parameter, ArrayList<? extends Number> respondValues){
        mAction = action;
        mParameter = parameter;
        mRespondValues = respondValues;
    }

    public static Message createInfoMessage(PARAMETER getParameter){
        return new Message(ACTIONS.GET, getParameter);
    }

    public static Message createSetMessage(PARAMETER setParameter, float setValue){
        return new Message(ACTIONS.SET, setParameter, setValue);
    }

    public static Message createSwitchMessage(PARAMETER switchParameter, boolean switchValue){
        return new Message(ACTIONS.TURN, switchParameter, switchValue);
    }

    public static Message createRespondMessage(PARAMETER respondParameter, ArrayList<? extends Number> respondValues){
        return new Message(ACTIONS.GET, respondParameter, respondValues);
    }

    public PARAMETER getParameter() {
        return mParameter;
    }

    public ACTIONS getAction(){
        return mAction;
    }

    public float getSetValue(){
        return mSetValue;
    }

    public boolean getSwitchValue(){
        return mSwitchValue;
    }

    public ArrayList<? extends Number> getRespondValues(){
        return mRespondValues;
    }
}
