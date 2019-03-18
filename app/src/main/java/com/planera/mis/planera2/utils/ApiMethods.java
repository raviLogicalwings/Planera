package com.planera.mis.planera2.utils;

import com.planera.mis.planera2.Retrofit.ApiClient;
import com.planera.mis.planera2.Retrofit.ApiInterface;

public class ApiMethods {
    private ApiInterface apiInterface;

    public ApiMethods(){
        apiInterface = ApiClient.getInstance();
    }


    public void getStateList(){

    }

}
