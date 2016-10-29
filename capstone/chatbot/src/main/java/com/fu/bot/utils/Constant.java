package com.fu.bot.utils;

public class Constant {

    public static final String CONNECTION_TIMEOUT = "connection_timeout";

    public static final String PAGE_ACCESS_TOKEN = "page_access_token";

    public static final String SUBSCRIBED_APPS_URL = "subscribed_apps_url";

    public static final String THREAD_SETTINGS_URL = "thread_settings_url";

    public static final String SETTING_TYPE = "setting_type";

    public static final String POSTBACK = "postback";

    public static final String CART_POST_FIX = "_CART";

    public static final String SHORT_KEY_HISTORY_INPUT = "@ls:";

    public static final int TEXT_INPUT_HISTORY = 1;

    public static final String FIREBASE_DATABASE_URL = "https://ismbhack-3e28a.firebaseio.com/";

    public enum SHORT_KEY {
        TYPE("@"), SHOW_CART("@gh"), RESET("@xoa"), HISTORY("@ls"), PROMOTION("@km");

        private String value;

        SHORT_KEY(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public static final String PHONE_PREFIX = "#";

    public enum POST_BACK_TYPE {
        TYPE_MENU("menu"), TYPE_BUTTON("button");

        private String value;

        POST_BACK_TYPE(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public static final String GREETING = "greeting";

    public static final String GET_START = "get_start";

    public static final String HELLO_MESSAGE = "\u0058\u0069\u006e \u0063\u0068\u00e0\u006f";

    public static final String WELCOME_MESSAGE = "\u0063\u0068\u0061\u0074\u0062\u006f\u0074 \u0068\u1ed7 \u0074\u0072\u1ee3 \u0111\u0069 \u0073\u0069\u00ea\u0075 \u0074\u0068\u1ecb \u0074\u0068\u00f4\u006e\u0067 \u006d\u0069\u006e\u0068 \u0068\u00e2\u006e \u0068\u1ea1\u006e\u0068 \u0111\u01b0\u1ee3\u0063 \u0070\u0068\u1ee5\u0063 \u0076\u1ee5 \u003a\u0044";

    public static final String ASK_PHONE_MESSAGE = "\u004e\u1ebf\u0075 \u0062\u1ea1\u006e \u006d\u0075\u1ed1\u006e \u0074\u0068\u0065\u006f \u0064\u00f5\u0069 \u0111\u01b0\u1ee3\u0063 \u0067\u0069\u1ecf \u0068\u00e0\u006e\u0067 \u0063\u1ee7\u0061 \u0062\u1ea1\u006e \u006b\u0068\u0069 \u0111\u0061\u006e\u0067 \u0111\u0069 \u0073\u0069\u00ea\u0075 \u0074\u0068\u1ecb\u002c \u0062\u1ea1\u006e \u0076\u0075\u0069 \u006c\u00f2\u006e\u0067 \u006e\u0068\u1ead\u0070 \u0073\u1ed1 \u0111\u0069\u1ec7\u006e \u0074\u0068\u006f\u1ea1\u0069 \u0074\u0068\u0065\u006f \u0063\u00fa \u0070\u0068\u00e1\u0070\u003a \u0023\u0073\u006f\u005f\u0064\u0069\u0065\u006e\u005f\u0074\u0068\u006f\u0061\u0069 \n\u0056\u0064\u003a \u0023\u0030\u0039\u0030\u0038\u0035\u0039\u0030\u0038\u0031\u0031";

    public static final String SAVED_PHONE = "\u0053\u1ed1 \u0111\u0069\u1ec7\u006e \u0074\u0068\u006f\u1ea1\u0069 \u0063\u1ee7\u0061 \u0062\u1ea1\u006e \u0073\u1ebd \u0111\u01b0\u1ee3\u0063 \u0062\u1ea3\u006f \u006d\u1ead\u0074\u002e \n\u004e\u1ebf\u0075 \u006d\u0075\u1ed1\u006e \u0074\u0068\u0061\u0079 \u0111\u1ed5\u0069 \u0073\u1ed1 \u0111\u0069\u1ec7\u006e \u0074\u0068\u006f\u1ea1\u0069\u002c \u0062\u1ea1\u006e \u0063\u00f3 \u0074\u0068\u1ec3 \u006e\u0068\u1ead\u0070 \u006c\u1ea1\u0069 \u0076\u1edb\u0069 \u0063\u00fa \u0070\u0068\u00e1\u0070\u003a \u0023\u0073\u006f\u005f\u0064\u0069\u0065\u006e\u005f\u0074\u0068\u006f\u0061\u0069\u002e \n\u0058\u0069\u006e \u0063\u1ea3\u006d \u01a1\u006e \u003a\u0044";

    public static final String PHONE_ALREADY_REGISTERED = "\u0053\u1ed1 \u0111\u0069\u1ec7\u006e \u0074\u0068\u006f\u1ea1\u0069 \u006e\u00e0\u0079 \u0111\u00e3 \u0111\u01b0\u1ee3\u0063 \u0111\u0103\u006e\u0067 \u006b\u00fd \u0062\u1edf\u0069 \u006d\u1ed9\u0074 \u0074\u00e0\u0069 \u006b\u0068\u006f\u1ea3\u006e \u006b\u0068\u00e1\u0063\u002e \u0042\u1ea1\u006e \u0076\u0075\u0069 \u006c\u00f2\u006e\u0067 \u006b\u0069\u1ec3\u006d \u0074\u0072\u0061 \u006c\u1ea1\u0069\u002e";

    public static final String THREE_DOTS = "\u002e\u002e\u002e";

    public static final String PERCENT = "%";

    public enum BUTTON_TYPE {
        DETAIL("detail"), ADD("add"), PROMOTION("promotion"), REMOVE("remove"), SUGGEST("suggest"),
        REFRESH_HISTORY("refreshHistory"), TRACKING_HISTORY("trackingHistory"), OPTIONAL_SHOW("optionalShow"), OPTIONAL_HISTORY("optionalHistory");

        private String value;

        BUTTON_TYPE(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum MENU_TYPE {
        SHOW("show"), RESET("reset"), PROMOTION("promotion"), HISTORY("history"), FEEDBACK("feedback");

        private String value;

        MENU_TYPE(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum STATUS_CODE {
        ADD(201), REMOVE(202), RESET(203), CLONE(204);

        private int value;

        STATUS_CODE(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public static final String STATUS_NEW = "\u004e\u0065\u0077";

    public static final String STATUS_ANALIZED = "\u0041\u004e\u0041\u004c\u0049\u005a\u0045\u0044";

    public static final String STR_BLANK = " ";

    public static final String STR_COMMA = "\u002c ";

    public static final String TYPE_MONEY = "\u0056\u004e\u0110";

    public static final int BEGIN_HISTORY = 0;

    public static final int BEGIN_SHOW = 0;

    public static final int MAX_SHOW = 8;

    public static final int MAX_SHOW_RESULT = 8;

    public static final int CHECK_NEXT_HISTORY = 1;

    public static final int CHECK_NEXT_SEARCH_RESULT = 1;

    public static final int MAX_SHOW_CART = 8;

    public static final String TYPE_SHOW_CART = "showCart";

    public static final String TYPE_SHOW_SEARCH = "showSearch";

    public static final String TYPE_SHOW_PROMOTION = "showPromotion";

    public static final String NEARBY_PRODUCT = "\u0053\u1ea3\u006e \u0070\u0068\u1ea9\u006d \u1edf \u0067\u1ea7\u006e \u0062\u1ea1\u006e\u003a";

    public enum TYPE_DATE {
        DAY_MONTH_YEAR_ENOUGH("dd/MM/yyyy"),
        DAY_MONTH_YEAR_LIMIT("dd/MM/yy");
        private String value;

        TYPE_DATE(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum BASIC_BUTTON {
        DETAIL_PRODUCT("\u0058\u0065\u006d \u0063\u0068\u0069 \u0074\u0069\u1ebf\u0074"),
        ADD_PRODUCT("\u0054\u0068\u00ea\u006d \u0076\u00e0\u006f \u0067\u0069\u1ecf \u0068\u00e0\u006e\u0067"),
        REMOVE_PRODUCT("\u0058\u00f3\u0061 \u0073\u1ea3\u006e \u0070\u0068\u1ea9\u006d"),
        SEE_MORE_HISTORY("\u0058\u0065\u006d \u0074\u0068\u00ea\u006d"),
        SEE_BACK_HISTORY("\u0058\u0065\u006d \u006c\u1ea1\u0069 \u0074\u0072\u01b0\u1edb\u0063"),
        SEE_DETAIL_HISTORY("\u0058\u0065\u006d \u0063\u0068\u0069 \u0074\u0069\u1ebf\u0074"),
        ADD_TRACKING_HISTORY("\u0054\u0068\u00ea\u006d \u0076\u00e0\u006f \u0067\u0069\u1ecf \u0068\u00e0\u006e\u0067"),
        SHOW_CART("\u0058\u0065\u006d \u0067\u0069\u1ecf \u0068\u00e0\u006e\u0067"),
        RESET_CART("\u0054\u1ea1\u006f \u006d\u1edb\u0069 \u0067\u0069\u1ecf \u0068\u00e0\u006e\u0067"),
        PROMOTION("\u004b\u0068\u0075\u0079\u1ebf\u006e \u006d\u00e3\u0069 \u0068\u00f4\u006d \u006e\u0061\u0079"),
        HISTORY("\u004c\u1ecb\u0063\u0068 \u0073\u1eed \u006d\u0075\u0061 \u0068\u00e0\u006e\u0067"),
        FEEDBACK("\u0070\u0068\u1ea3\u006e \u0068\u1ed3\u0069 \u006b\u1ebf\u0074 \u0071\u0075\u1ea3"),
        NEXT_SHOW("\u0058\u0065\u006d \u0074\u0068\u00ea\u006d"),
        BACK_SHOW("\u0051\u0075\u0061\u0079 \u006c\u1ea1\u0069"),
        SUGGEST("\u0047\u1ee3\u0069 \u00fd");

        private String value;

        BASIC_BUTTON(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    private Constant() {
        // default constructor
    }
}
