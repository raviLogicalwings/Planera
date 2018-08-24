package com.planera.mis.planera2.activities.utils;

public class AppConstants {
    public static final int SPLASH_TIME_OUT = 5000;
    public static final String PREFRENCE_FILE_NAME = "PlaneraPrefFile";


    //Retrofit Constants
    public static final int RESULT_OK = 1;
    public static final long REQUEST_TIMEOUT = 200;
    public static final String BASE_URL = "http://35.231.248.33/mrapi/";
    public static final String LOGIN = "login/login";
    public static final String USER_REGISTRATION = "register/registerUserDetails";


    public static final String GIFT_LIST = "gift/getAllGifts";
    public static final String BRANDS_LIST = "product/getAllProducts";
    public static final String STATE_LIST = "state/getAllStates";
    public static final String TERRITORY_LIST = "territory/getAllTerritories";
    public static final String PATCH_LIST = "patch/getAllPatches";
    public static final String DOCTORS_LIST = "doctor/getAllDoctors";
    public static final String PRODUCT_LIST = "product/getAllProducts";


    public static final String ADD_STATE = "state/addState";
    public static final String ADD_TERRITORY = "territory/addTerritory";
    public static final String ADD_GIFT = "gift/addGift";
    public static final String ADD_PATCH = "patch/addPatch";
    public static final String ADD_PRODUCTS= "product/addProduct";
    public static final String ADD_DOCTORS = "doctor/addDoctor";
    public static final String ADD_CHEMIST = "chemist/addChemist";

    public static final String DELETE_STATE = "state/deleteState";
    public static final String DELETE_GIFT = "gift/deleteGift";
    public static final String DELETE_TERRITORY = "territory/deleteTerritory";
    public static final String DELETE_PATCH = "patch/deletePatch";
    public static final String DELETE_DOCTORS = "doctor/deleteDoctor";
    public static final String DELETE_PRODUCTS = "product/deleteProduct";

    public static final String UPDATE_DOCTOR = "doctor/updateDoctor";
    public static final String UPDATE_STATE = "state/updateState";
    public static final String UPDATE_TERRITORY = "territory/updateTerritory";
    public static final String UPDATE_PRODUCT = "product/updateProduct";
    public static final String UPDATE_GIFT = "gift/updateGift";
    public static final String UPDATE_PATCH = "patch/updatePatch";



    //Doctor's Meet Time Constants;
    public static final String KEY_MORNING_TIME = "Morning";
    public static final String KEY_EVENING_TIME = "Evening";
    public static final int MORNING = 1;
    public static final int EVENING = 2;
    public static final String UPDATE_DOCTOR_KEY = "updateDoctor";
    public static final String FIRST_NAME = "firstName";
    public static final String MIDDLE_NAME = "middleName";
    public static final String LAST_NAME = "lastName";
    public static final String EMAIL = "email";
    public static final String PHONE = "PHONE";
    public static final String DOB = "dob";
    public static final String QUALIFICATION = "qualification";
    public static  final String SPECIALIZATION = "specialization";
    public static final String ADDRESS1 = "address1";
    public static final String ADDRESS2 = "address2";
    public static final String ADDRESS3 = "address3";
    public static final String ADDRESS4 = "address4";
    public static final String DISTRICT = "district";
    public static final String CITY = "city";
    public static final String STATE = "state";
    public static final String PINCODE = "pincode";


    //Google Palces Api
    public static final String GOOGLE_PLACES_URL = "https://maps.googleapis.com/maps/api/";
    public static final String FIND_PALCE = "place/findplacefromtext/json";
    public static final String KEY_GOOGLE_PLACES = "AIzaSyCgS8T9gRYNHrewji4XYDbWwDie0if7uE4";
    public static final String INPUT_TYPE = "textquery";
    public static final String FIELDS = "formatted_address,name,geometry";
    public static final String STATUS_OK = "OK";
    public static final String STATUS_ZERO_RESULTS = "ZERO_RESULTS";
    public static final String STATUS_OVER_LIMIT = "OVER_QUERY_LIMIT";



    //Update Data
    public static final String KEY_TERRITORY_NAME = "territory_key";
    public static final String KEY_TERRITORY_ID = "territory_id";
    public static final String KEY_STATE_ID ="state_id" ;
    public static final String KEY_PATCH_ID = "patch_id";
    public static final String KEY_PATCH_NAME = "patch_name";
    public static final String KEY_GIFT_ID = "gift_id";
    public static final String KEY_GIFT_NAME = "gift_name";

    public static final String KEY_STATE_NAME ="state_key" ;
    public static final String KEY_PRODUCT_ID = "product_id";
    public static final String KEY_PRODUCT_NAME = "product_name";
    public static final String KEY_IS_BRAND = "is_brand";

    //Shared Preference Constants
    public static final String IS_LOGIN = "is_login";
    public static final String TOKEN = "token";
    public static final int ACTIVE_USER = 1;
    public static final int INACTIVE_USER = 2;

    // Pager fragment count
    public static final int FRAGMENT_COUNT = 3;


    public static final int STATE_FRAGMENT = 1;
    public static final int TERITORY_FRAGMENT = 2;
    public static final int PATCH_FRAGMENT = 3;
    public static final int GIFT_FRAGMENT = 4;
    public static final int PRODUCT_FRAGMENT = 5;
    public static final int DOCTOR_FRAGMENT = 6;
    public static final int CHEMIST_FRAGMENT = 7;
    public static final String KEY_TOUCHED_FRAGMENT = "key_touched_fragment";


    public static final String IS_CHANGES = "is_changes";
}
