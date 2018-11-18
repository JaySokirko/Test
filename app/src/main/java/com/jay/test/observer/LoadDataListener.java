package com.jay.test.observer;


/**
 *  Listener whether data has been loaded
 */
public class LoadDataListener {


    private boolean isDataLoaded = false;
    private ChangeListener listener;


    public boolean isDataLoaded() {
        return isDataLoaded;
    }


    public void setDataLoaded(boolean dataLoaded) {
        isDataLoaded = dataLoaded;
        if (listener != null) listener.onChange();
    }


    public ChangeListener getListener() {
        return listener;
    }


    public void setListener(ChangeListener listener) {
        this.listener = listener;
    }


    public interface ChangeListener {
        void onChange();
    }
}
