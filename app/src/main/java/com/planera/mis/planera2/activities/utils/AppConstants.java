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
    public static final String CHANGE_PASSWORD = "login/changePassword";

    public static final String GIFT_LIST = "gift/getAllGifts";
    public static final String BRANDS_LIST = "product/getAllProducts";
    public static final String STATE_LIST = "state/getAllStates";
    public static final String TERRITORY_LIST = "territory/getAllTerritories";
    public static final String PATCH_LIST = "patch/getAllPatches";
    public static final String DOCTORS_LIST = "doctor/getAllDoctors";
    public static final String PRODUCT_LIST = "product/getAllProducts";
    public static final String CHEMIST_LIST = "chemist/getAllChemists";
    public static final String USER_LIST = "user/getAllUsers";
    public static final String PLAN_LIST ="plan/getAllPlans" ;
    public static final String USER_PLAN_LIST = "plan/getUserPlans";
    public static final String TERRITORY_WISE_PATCHES = "patch/getTerritoryWisePatches";
    public static final String PATCH_WISE_DOCTORS_LIST = "doctor/getPatchWiseDoctor";
    public static final String PATCH_WISE_CHEMIST_LIST = "chemist/getPatchWiseChemists";
    public static final String CHEMIST_REPORTS_LIST = "inputreport/chemistInputReport";
    public static final String DOCTOR_REPORTS_LIST = "inputreport/doctorInputReport";
    public static final String USER_REPORT_LIST = "inputreport/userInputReport";

    public static final String ADD_STATE = "state/addState";
    public static final String ADD_TERRITORY = "territory/addTerritory";
    public static final String ADD_GIFT = "gift/addGift";
    public static final String ADD_PATCH = "patch/addPatch";
    public static final String ADD_PRODUCTS= "product/addProduct";
    public static final String ADD_DOCTORS = "doctor/addDoctor";
    public static final String ADD_CHEMIST = "chemist/addChemist";
    public static final String ADD_PLAN = "plan/addPlan";
    public static final String ADD_INPUT_PRODUCT = "inputproduct/addInputProduct";
    public static final String ADD_INPUT = "input/addInput";
    public static final String ADD_INPUT_GIFT = "inputgift/addInputGift";

    public static final String DELETE_STATE = "state/deleteState";
    public static final String DELETE_GIFT = "gift/deleteGift";
    public static final String DELETE_TERRITORY = "territory/deleteTerritory";
    public static final String DELETE_PATCH = "patch/deletePatch";
    public static final String DELETE_DOCTORS = "doctor/deleteDoctor";
    public static final String DELETE_PRODUCTS = "product/deleteProduct";
    public static final String DELETE_USERS = "user/deleteUser";
    public static final String DELETE_CHEMIST = "chemist/deleteChemist";
    public static final String DELETE_PLAN = "plan/deletePlan";

    public static final String UPDATE_DOCTOR = "doctor/updateDoctor";
    public static final String UPDATE_STATE = "state/updateState";
    public static final String UPDATE_TERRITORY = "territory/updateTerritory";
    public static final String UPDATE_PRODUCT = "product/updateProduct";
    public static final String UPDATE_GIFT = "gift/updateGift";
    public static final String UPDATE_PATCH = "patch/updatePatch";
    public static final String UPDATE_CHEMIST = "chemist/updateChemist";
    public static final String UPDATE_USER = "user/updateUser";
    public static final String UPDATE_PLAN = "plan/updatePlan";

    //User's Constant
    public static final String EXPERIENCE_YEAR = "experience_year";
    public static final String UPDATE_USER_KEY = "update_user";
    public static final String LOGIN_ID = "login_id";
    public static final String PASSWORD = "password";
    public static final String PHONE1 = "phone1";
    public static final String PHONE2 = "phone2";
    public static final String EMAIL1 = "email1";
    public static final String EMAIL2 = "email2";
    public static final String DOJ = "doj";
    public static final String PAN  = "pan";

    //User's Plan constants
    public static final int ROLE_DOCTOR = 1;
    public static final int ROLE_CHEMIST = 2;
    public static final int ROLE_USER = 3;
    public static final String KEY_ROLE = "role";

    public static final String KEY_ROLE_DOCTOR = "role_doctor";
    public static final String KEY_ROLE_CHEMIST = "role_chemsit";


    // Plan's Constant
    public static final String UPDATE_PLAN_KEY = "update_plan";
    public static final String PATCH_ID = "patch_id";
    public static final String DOCTOR_ID = "doctor_id";
    public static final String CHEMIST_ID = "chemist_id";
    public static final String USER_ID = "user_id";
    public static final String CALLS = "patch_id";
    public static final String REMARK = "remark";
    public static final String MONTH = "month";
    public static final String YEAR = "year";
    public static final String STATUS = "status";
    //Chemist's Constant
    public static final String BILLING_EMAIL ="billing_email" ;
    public static final String BILLING_PHONE1 ="billing_phone1" ;
    public static final String BILLING_PHONE2 ="billing_phone2" ;
    public static final String RATING = "rating";
    public static final String MONTHLY_VOLUME_POTENTIAL = "volume_potential";
    public static final String SHOP_SIZE = "shop_size";
    public static final String COMPANY_NAME ="company_name" ;
    public static final String UPDATE_CHEMIST_KEY = "key_update_chemist";



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


    //Brands Contanst =
    public static final int BRAND = 1;
    public static final int NOT_BRAND = 2;

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
    public static final int FRAGMENT_COUNT_FOR_DOCTOR = 3;
    public static final int FRAGMENT_COUNT_FOR_CHEMIST = 1;

    //Fragment for user Role
    public static final int HOME_FRAGMENT = 10;
    public static final int PROFILE_FRAGMENT = 11;


    public static final int STATE_FRAGMENT = 1;
    public static final int TERRITORY_FRAGMENT = 2;
    public static final int PATCH_FRAGMENT = 3;
    public static final int GIFT_FRAGMENT = 4;
    public static final int PRODUCT_FRAGMENT = 5;
    public static final int DOCTOR_FRAGMENT = 6;
    public static final int CHEMIST_FRAGMENT = 7;
    public static final int USER_FRAGMENT = 8;
    public static final int PLAN_FRAGMENT = 9;
    public static final String KEY_TOUCHED_FRAGMENT = "key_touched_fragment";


    public static final String IS_CHANGES = "is_changes";


    public static final String USER = "0";
    public static final String IS_USER = "isUser" ;
    public static final String CUSTOMER_NAME ="customer_name" ;
    public static final String VISIT_DATE = "visit_date";
    public static final int USER_IN_LOCATION = 1;
    public static final int USER_NOT_IN_LOCATION = 0;
    public static final String KEY_IN_LOCATION = "in_location";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";


    public static final String KEY_PLAN_ID = "plan_id";
    public static final String KEY_USER_ID = "user_id";


    //interested Levels
    public static final String LOW = "3";
    public static final String REGULAR = "2";
    public static final String SUPER = "1";
    public static final String KEY_INPUT_ID = "input_id";
    public static final String KEY_REVERT_FRAGMENT = "revert_fragment";
}
