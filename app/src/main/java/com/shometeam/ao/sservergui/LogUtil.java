package com.shometeam.ao.sservergui;

import android.util.Log;

import java.util.LinkedList;

/**
 * Created by ao on 04.06.15.
 */
public class LogUtil {
    public static enum TAG {SERVER,APP,ARDUINO,NEURAL_NETWORK,WEATHER_SERVICE,DATA_BASE,ALL};
    private LinkedList<TagList> mTagLists;

    LogUtil(){
        mTagLists = new LinkedList<>();
        for(int i=0; i<TAG.values().length; i++){
            mTagLists.add(new TagList(TAG.values()[i]) );
        }
    }

    public void addMessage(TAG tag, String message){
        for(TagList tl: mTagLists){
            if(tag.equals(tl.getTag())){
                tl.addMessage(message);
                break;
            }
        }
    }

    public LinkedList<String> getMessagesByTag(TAG tag){
        LinkedList<String> messages = new LinkedList<>();
        for(TagList tl: mTagLists){
            if(tag.equals(tl.getTag())){
                if(tag.equals(TAG.ALL)){
                    //все в одну кучу, чтобы лишний раз не хранить
                    for(TagList tl2: mTagLists){
                        messages.addAll(tl2.getMessages());
                    }
                } else{
                    messages = tl.getMessages();
                }
                break;
            }
        }
        return messages;
    }


    public String[] getArrayOfTags(){
        String[] tags = new String[TAG.values().length];
        for (int i = 0; i < TAG.values().length; i++) {
            tags[i] = TAG.values()[i].toString();
        }
        return tags;
    }

    class TagList{
        private TAG mTag;
        private LinkedList<String> mMessages;

        private TagList(TAG tag){
            mTag = tag;
            mMessages = new LinkedList<>();
        }

        public synchronized void addMessage(String message){
            String converted = mTag.toString()+":\t\t\t\t\t"+message+"\n\n";
            mMessages.add(converted);
        }

        public LinkedList<String> getMessages(){
            return mMessages;
        }

        public TAG getTag(){
            return mTag;
        }
    }
}

