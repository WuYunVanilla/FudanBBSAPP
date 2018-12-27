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
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import dandandateam.dandanda.Items.ProfileItem;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class Global {
    String user_id = "15302010056";
    String nickname = "liulu";
    String gender = "女";
    String grade = "15级本科";
    String signature = "不码不农";
    int num_answer = 0;
    int num_collection = 0;
    int num_follow = 0;
    int num_posting = 0;
    int num_view = 0;
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

    public static HttpEntity getHttpPostEntity(Entity en, String url_rel) {
        HttpPostThread td = new HttpPostThread(en, url_rel);
        //1.执行 Callable 方式，需要 FutureTask 实现类的支持，用于接收运算结果。
        FutureTask<HttpEntity> ft = new FutureTask<>(td);

        new Thread(ft).start();
        HttpEntity httpEntity = null;
        //2.接收线程运算后的结果
        try {
            httpEntity = ft.get();  //FutureTask 可用于 闭锁 类似于CountDownLatch的作用，在所有的线程没有执行完成之后这里是不会执行的
            System.out.println(httpEntity);
            System.out.println("------------------------------------");
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return httpEntity;
    }


    public static JSONObject getHttpGetEntity(String url_rel, String url_params) {
        HttpGetThread td = new HttpGetThread(url_rel, url_params);
        //1.执行 Callable 方式，需要 FutureTask 实现类的支持，用于接收运算结果。
        FutureTask<JSONObject> ft = new FutureTask<>(td);

        new Thread(ft).start();
        JSONObject jsonObject = null;
        //2.接收线程运算后的结果
        try {
            jsonObject = ft.get();  //FutureTask 可用于 闭锁 类似于CountDownLatch的作用，在所有的线程没有执行完成之后这里是不会执行的
            System.out.println("JSONObject is " + jsonObject);
            System.out.println("------------------------------------");
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    static class HttpPostThread implements Callable<HttpEntity> {
        Entity en;
        String url_rel;

        public HttpPostThread(Entity en, String url_rel) {
            this.en = en;
            this.url_rel = url_rel;
        }

        @Override
        public HttpEntity call() throws Exception {
            HttpEntity return_entity = null;
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

                        return_entity = httpResponse.getEntity();
                        if (null != return_entity) {
                            System.out.println("-------------------------------------------------------");
                            System.out.println("return_entity is  --" + EntityUtils.toString(return_entity));
                            System.out.println("-------------------------------------------------------");
                        }
                    } else {
                        System.out.print("Http Post return wrong" + url_rel + gson.toJson(en));
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
            return return_entity;
        }

    }

    static class HttpGetThread implements Callable<JSONObject> {
        String url_rel;
        String url_params;

        public HttpGetThread(String url_rel, String url_params) {
            this.url_rel = url_rel;
            this.url_params = url_params;
        }

        @Override
        public JSONObject call() throws Exception {
            HttpEntity return_entity = null;

            //http get
            CloseableHttpClient httpClient = Global.getHttpClient();
            try {
                HttpGet get = new HttpGet(Global.url_abs + url_rel + "?" + url_params);

                //执行请求
                CloseableHttpResponse httpResponse = httpClient.execute(get);
                try {
                    int statusCode = httpResponse.getStatusLine().getStatusCode();

                    if (statusCode == HttpStatus.SC_OK) {

                        return_entity = httpResponse.getEntity();
                        if (null != return_entity) {
                            System.out.println("-------------------------------------------------------");
                            //System.out.println(EntityUtils.toString(return_entity));
                            //!!!!!!!!!!!! entity所得到的流是不可重复读取
                            System.out.println("-------------------------------------------------------");
                        }
                    } else {
                        System.out.println("Http Get return wrong ---" + url_rel + "?" + url_params);
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
            //得到httpResponse的实体数据
            BufferedReader bufferedReader = null;
            StringBuilder entityStringBuilder = new StringBuilder();
            JSONObject resultJsonObject = null;

            if (return_entity != null) {
                resultJsonObject = new JSONObject();
                try {
                    bufferedReader = new BufferedReader
                            (new InputStreamReader(return_entity.getContent(), "UTF-8"), 8 * 1024);
                    String line = null;
                    while ((line = bufferedReader.readLine()) != null) {
                        entityStringBuilder.append(line);
                    }
                    //利用从HttpEntity中得到的String生成JsonObject
                    resultJsonObject = new JSONObject(entityStringBuilder.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    // 关闭文件流
                    try {
                        bufferedReader.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
            return resultJsonObject;
        }
    }
}

abstract class Entity {
    String time;
}