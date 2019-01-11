package com.samyotech.fabartist.interfacess;

/**
 * Created by shubham on 19/7/17.
 */

public interface Consts {
    public static String APP_NAME = "FabArtist";

    //    OLD Server
//    public String BASE_URL = "http://phpstack-132936-677440.cloudwaysapps.com/Webservice/";


    //    New MKnob
    public String BASE_URL = "http://planetory.agency/artist/Webservice/";


    public String PAYMENT_FAIL = "http://phpstack-132936-677440.cloudwaysapps.com/Stripe/Payment/fail";
    public String PAYMENT_SUCCESS = "http://phpstack-132936-677440.cloudwaysapps.com/Stripe/Payment/success";
    public String MAKE_PAYMENT = "http://phpstack-132936-677440.cloudwaysapps.com/Stripe/Payment/make_payment/";

    public String PAYMENT_FAIL_Paypal = "http://phpstack-132936-677440.cloudwaysapps.com/Webservice/payufailure";
    public String PAYMENT_SUCCESS_paypal = "http://phpstack-132936-677440.cloudwaysapps.com/Webservice/payusuccess";
    public String MAKE_PAYMENT_paypal = "http://phpstack-132936-677440.cloudwaysapps.com/Webservice/paypalWallent?";

    /*Api Details*/
    public String LOGIN_API = "signIn";
    public String REGISTER_API = "SignUp";
    public String GET_REFERRAL_CODE_API = "getMyReferralCode";
    public String GET_CHAT_HISTORY_API = "getChatHistoryForArtist";
    public String GET_CHAT_API = "getChat";
    public String SEND_CHAT_API = "sendmsg";
    public String GET_NOTIFICATION_API = "getNotifications";
    public String GET_ARTIST_BY_ID_API = "getArtistByid";
    public String UPDATE_PROFILE_API = "editPersonalInfo";
    public String UPDATE_PROFILE_ARTIST_API = "artistPrsonalInfo";
    public String GET_ALL_CATEGORY_API = "getAllCaegory";
    public String GET_ALL_SKILLS_BY_CAT_API = "getSkillsByCategory";
    public String ADD_QUALIFICATION_API = "addQualification";
    public String ADD_PRODUCT_API = "addProduct";
    public String ADD_GALLERY_API = "addGallery";
    public String GET_INVOICE_API = "getMyInvoice";
    public String CURRENT_BOOKING_API = "getMyCurrentBooking";
    public String BOOKING_OPERATION_API = "booking_operation";
    public String DECLINE_BOOKING_API = "decline_booking";
    public String ONLINE_OFFLINE_API = "onlineOffline";
    public String UPDATE_LOCATION_API = "updateLocation";
    public String ARTIST_LOGOUT_API = "artistLogout";
    public String GET_MY_TICKET_API = "getMyTicket";
    public String GENERATE_TICKET_API = "generateTicket";
    public String GET_TICKET_COMMENTS_API = "getTicketComments";
    public String ADD_TICKET_COMMENTS_API = "addTicketComments";
    public String FORGET_PASSWORD_API = "forgotPassword";
    public String GET_APPOINTMENT_API = "getAppointment";
    public String EDIT_APPOINTMENT_API = "edit_appointment";
    public String APPOINTMENT_OPERATION_API = "appointment_operation";
    public String GET_ALL_JOB_API = "get_all_job";
    public String APPLIED_JOB_API = "applied_job";
    public String JOB_STATUS_ARTIST_API = "job_status_artist";
    public String GET_APPLIED_JOB_ARTIST_API = "get_applied_job_artist";
    public String CHANGE_COMMISSION_ARTIST_API = "changeCommissionArtist";
    public String START_JOB_API = "startJob";
    public String DELETE_PROFILE_IMAGE_API = "deleteProfileImage";
    public String MY_EARNING1_API = "myEarning1";
    public String WALLET_REQUEST_API = "walletRequest";
    public String ADD_MONEY_API = "addMoney";
    public String GET_WALLET_HISTORY_API = "getWalletHistory";
    public String GET_WALLET_API = "getWallet";


    /*app data*/
    String CAMERA_ACCEPTED = "camera_accepted";
    String STORAGE_ACCEPTED = "storage_accepted";
    String MODIFY_AUDIO_ACCEPTED = "modify_audio_accepted";
    String CALL_PRIVILAGE = "call_privilage";
    String FINE_LOC = "fine_loc";
    String CORAS_LOC = "coras_loc";
    String CALL_PHONE = "call_phone";
    String PAYMENT_URL = "payment_url";
    String SURL = "surl";
    String FURL = "furl";
    /*app data*/

    /*Project Parameter*/
    String ARTIST_ID = "artist_id";
    String CHAT_LIST_DTO = "chat_list_dto";
    String USER_DTO = "user_dto";
    String IS_REGISTERED = "is_registered";
    String IMAGE_URI_CAMERA = "image_uri_camera";
    String DATE_FORMATE_SERVER = "EEE, MMM dd, yyyy hh:mm a"; //Wed, JUL 06, 2018 04:30 pm
    String DATE_FORMATE_TIMEZONE = "z";
    String BROADCAST = "broadcast";


    /*Parameter Get Artist and Search*/
    String USER_ID = "user_id";
    String LATITUDE = "latitude";
    String LONGITUDE = "longitude";

    /*Get All History*/
    String ROLE = "role";

    /*Send Message*/
    String MESSAGE = "message";
    String SEND_BY = "send_by";
    String SENDER_NAME = "sender_name";


    /*Login Parameter*/
    String NAME = "name";
    String EMAIL_ID = "email_id";
    String PASSWORD = "password";
    String DEVICE_TYPE = "device_type";
    String DEVICE_TOKEN = "device_token";
    String DEVICE_ID = "device_id";
    String REFERRAL_CODE = "referral_code";


    /*Update Profile*/
    String NEW_PASSWORD = "new_password";
    String GENDER = "gender";
    String MOBILE = "mobile";
    String OFFICE_ADDRESS = "office_address";
    String ADDRESS = "address";
    String IMAGE = "image";

    /*Update Profile Artist*/
    String CATEGORY_ID = "category_id";
    String BIO = "bio";
    String LOCATION = "location";
    String PRICE = "price";
    String ABOUT_US = "about_us";
    String SKILLS = "skills";
    String VIDEO_URL = "video_url";
    String CITY = "city";
    String COUNTRY = "country";

    /*Get Skills*/
    String CAT_ID = "cat_id";


    /*Update Qualification*/
    String TITLE = "title";
    String DESCRIPTION = "description";

    /*Update Qualification*/
    String PRODUCT_NAME = "product_name";
    String PRODUCT_IMAGE = "product_image";

    /*Booking Opreations*/
    String BOOKING_ID = "booking_id";
    String REQUEST = "request";

    /*Decline*/
    String DECLINE_BY = "decline_by";
    String DECLINE_REASON = "decline_reason";

    /*Online Offline*/
    String IS_ONLINE = "is_online";

    /*Add Ticket*/
    String REASON = "reason";


    /*Get Ticket*/
    String TICKET_ID = "ticket_id";
    String COMMENT = "comment";

    /*Edit Appointment*/
    String APPOINTMENT_ID = "appointment_id";


    /*Book Artist*/

    String DATE_STRING = "date_string";
    String TIMEZONE = "timezone";


    /*Apply Job*/
    String JOB_ID = "job_id";

    /*Job Status*/
    String AJ_ID = "aj_id";
    String STATUS = "status";


    // Google Console APIs developer key
    // Replace this key with your's
    public static final String DEVELOPER_KEY = "AIzaSyArfMm1YCgq6FCtxA7w_W-pOxJ_0D6GGy8";

    /*Chat*/
    String CHAT_TYPE = "chat_type";

    /*Payment Type*/
    String ARTIST_COMMISSION_TYPE = "artist_commission_type";

    /*Add Money*/
    String TXN_ID = "txn_id";
    String ORDER_ID = "order_id";
    String AMOUNT = "amount";
    String CURRENCY = "currency";
}
