package com.planera.mis.planera2.controller;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.planera.mis.planera2.models.InputGift;
import com.planera.mis.planera2.models.InputOrders;

import io.fabric.sdk.android.Fabric;
import java.util.List;

public class DataController extends Application {
    public static DataController mInstance;
    public List<InputOrders> orderListSelected;
    public List<InputGift> inputGiftList;
    public List<InputOrders> orderPODList;
    public List<InputOrders> sampleListSelected;

    public List<InputOrders> getSampleListSelected() {
        return sampleListSelected;
    }

    public void setSampleListSelected(List<InputOrders> sampleListSelected) {
        this.sampleListSelected = sampleListSelected;
    }

    public List<InputGift> getInputGiftList() {
        return inputGiftList;
    }


    public List<InputOrders> getOrderPODList() {
        return orderPODList;
    }

    public void setOrderPODList(List<InputOrders> orderPODList) {
        this.orderPODList = orderPODList;
    }

    public void setInputGiftList(List<InputGift> inputGiftList) {
        this.inputGiftList = inputGiftList;
    }

    public List<InputOrders> getOrderListSelected() {
        return orderListSelected;
    }

    public void setOrderListSelected(List<InputOrders> orderListSelected) {
        this.orderListSelected = orderListSelected;
    }

    public static DataController getmInstance() {
        if (mInstance == null){
            mInstance = new DataController();
        }
        return mInstance;
    }

    public static void setmInstance(DataController mInstance) {
        DataController.mInstance = mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        mInstance = this;
    }
}
