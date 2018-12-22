package dandandateam.dandanda;

import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import dandandateam.dandanda.Items.ProfileItem;

public class Global {
    String user_id="15302010056";
    String nickname = "liulu";
    String gender = "女";
    String grade = "15级本科";
    String signature = "不码不农";
    int num_answer = 0;
    int num_collection = 0;
    int num_follow=0;
    int num_posting=0;
    int num_view=0;

    public ProfileItem[] personalItem = new ProfileItem[0];
    public ProfileItem[] detailItem = new ProfileItem[0];

    //tags
    final static String tag_stu = "tag_student_group";
    final static String tag_cou = "tag_courses";
    final static String tag_sec = "tag_second_hand_market";
    final static String tag_rec = "tag_recruit_intern";
    final static String tag_oth = "tag_others";

    final static String url_abs = "http://116.62.236.184:5000/";
    //relative urls
    final static String url_post = "add_question/";
    final static String url_answer = "add_answer/";
    final static String url_answer_detail = "answer_detail/";

    final static String url_answers_list = "answers_list/";
    final static String url_add_follow = "add_follow/";
    final static String url_add_agree = "add_agree/";
    final static String url_add_collection = "add_collection/";

    private Global() {

    }

    private static class GlobalConstantsHolder {
        private final static Global instance = new Global();
    }

    public static Global getInstance() {
        return GlobalConstantsHolder.instance;
    }

    private static CloseableHttpClient getHttpClient() {
        return HttpClients.createDefault();
    }

    public static void closeHttpClient(CloseableHttpClient client) throws IOException {
        if (client != null) {
            client.close();
        }
    }

    public static HttpEntity httpPost(final Entity en, final String url_rel) {
        final HttpEntity[] return_entity = {null};
        //开启线程，发送请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                //add time
                Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = Calendar.getInstance().getTime();
                en.time = formatter.format(date);

                //http post
                CloseableHttpClient httpClient = Global.getHttpClient();
                try {
                    HttpPost post = new HttpPost(Global.url_abs + url_rel);

                    Gson gson = new Gson();
                    StringEntity entity = new StringEntity(gson.toJson(en), "utf-8");//解决中文乱码问题
                    entity.setContentEncoding("UTF-8");
                    entity.setContentType("application/json");
                    post.setEntity(entity);

                    System.out.println("POST 请求...." + post.getURI());
                    //执行请求
                    CloseableHttpResponse httpResponse = httpClient.execute(post);
                    try {
                        int statusCode = httpResponse.getStatusLine().getStatusCode();

                        if (statusCode == HttpStatus.SC_OK) {

                            return_entity[0] = httpResponse.getEntity();
                            if (null != return_entity[0]) {
                                System.out.println("-------------------------------------------------------");
                                System.out.println(EntityUtils.toString(return_entity[0]));
                                System.out.println("-------------------------------------------------------");
                            }
                        } else {
                           System.out.print("Http Post return wrong"+ url_rel + gson.toJson(en));
                        }
                    } finally {
                        httpResponse.close();
                    }

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        Global.closeHttpClient(httpClient);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        return return_entity[0];
    }

    public static HttpEntity httpGet(final String url_rel, final String url_params) {
        final HttpEntity[] return_entity = {null};
        //开启线程，发送请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                //http get
                CloseableHttpClient httpClient = Global.getHttpClient();
                try {
                    HttpGet get = new HttpGet(Global.url_abs + url_rel + "?" + url_params);

                    //执行请求
                    CloseableHttpResponse httpResponse = httpClient.execute(get);
                    try {
                        int statusCode = httpResponse.getStatusLine().getStatusCode();

                        if (statusCode == HttpStatus.SC_OK) {

                            return_entity[0] = httpResponse.getEntity();
                            if (null != return_entity) {
                                System.out.println("-------------------------------------------------------");
                                System.out.println(EntityUtils.toString(return_entity[0]));
                                System.out.println("-------------------------------------------------------");
                            }
                        } else {
                            System.out.print("Http Get return wrong"+ url_rel + "?" + url_params);
                        }
                    } finally {
                        httpResponse.close();
                    }

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        Global.closeHttpClient(httpClient);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        return return_entity[0];
    }
}

abstract class Entity {
    String time;
}
