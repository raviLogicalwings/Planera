package com.planera.mis.planera2.activities.controller;

import android.app.Application;

import com.planera.mis.planera2.activities.models.InputGift;
import com.planera.mis.planera2.activities.models.InputOrders;

import java.util.List;

public class DataController extends Application {
    public static DataController mInstance;
    public List<InputOrders> orderListSelected;
    public List<InputGift> inputGiftList;
    public List<InputOrders> orderPODList;


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
        mInstance = this;
    }
}
