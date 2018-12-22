package dandandateam.dandanda;


public class HTTPUtil {
    static int REQUEST_OK = 1;
    static int REQUEST_FAIL = 0;

    public static String main_page_url() {
        return "http://116.62.236.184:5000/homepage";
    }

    public static String recomm_page_url(String stdid) {
        return "http://116.62.236.184:5000/recommend/?stdid=" + stdid;
    }

    public static String search_page_urL(String keyword) {
        return "http://116.62.236.184:5000/search/?keyword=" + keyword;
    }


}


