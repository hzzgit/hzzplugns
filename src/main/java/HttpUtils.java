import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.*;

public class HttpUtils {
    public static final String
            URL_PREFIX_STRING="http://127.0.0.1:8924/FileAction/upload.action";

    /**
     * post:(上传).
     *
     * @author Joe Date:2017年9月11日下午5:37:46
     * @param serverUrl
     * @param fileParamName
     * @param file
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String post(String serverUrl, String fileParamName, File file,String modulename)
            throws ClientProtocolException, IOException {
        HttpPost httpPost = new HttpPost(serverUrl);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        // 上传的文件
        builder.addBinaryBody(fileParamName, file);
        builder.addTextBody("modulename",modulename);
        HttpEntity httpEntity = builder.setContentType(ContentType.MULTIPART_FORM_DATA).build();


        httpPost.setEntity(httpEntity);
        // 请求获取数据的超时时间 、 设置从connect
        // Manager获取Connection超时时间（因为目前版本是可以共享连接池的）、设置连接超时时间
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(10000).setConnectionRequestTimeout(3000)
                .setConnectTimeout(10000).build();
        httpPost.setConfig(requestConfig);
        HttpClient httpClient = HttpClients.createDefault();
        HttpResponse response = httpClient.execute(httpPost);
        return EntityUtils.toString(response.getEntity());
    }

    /**
     * post:查询module list
     * @author Joe Date:2017年9月11日下午5:37:46
     * @param serverUrl

     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String postseach(String serverUrl, String modulename)
            throws ClientProtocolException, IOException {
        HttpPost httpPost = new HttpPost(serverUrl+"?modulename="+modulename);

        // 请求获取数据的超时时间 、 设置从connect
        // Manager获取Connection超时时间（因为目前版本是可以共享连接池的）、设置连接超时时间
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(10000).setConnectionRequestTimeout(3000)
                .setConnectTimeout(10000).build();
        httpPost.setConfig(requestConfig);
        HttpClient httpClient = HttpClients.createDefault();
        HttpResponse response = httpClient.execute(httpPost);
        return EntityUtils.toString(response.getEntity());
    }


    /**
     * post:(上传).
     *
     * @author Joe Date:2017年9月11日下午5:37:46
     * @param serverUrl
     * @param fileParamName
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static void post2(String serverUrl, String fileParamName)
            throws ClientProtocolException, IOException {
        fileParamName=fileParamName.replaceAll("\\\\","%2F");
        HttpPost httpPost = new HttpPost(serverUrl+"?filename="+fileParamName);
        // 请求获取数据的超时时间 、 设置从connect
        // Manager获取Connection超时时间（因为目前版本是可以共享连接池的）、设置连接超时时间
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(10000).setConnectionRequestTimeout(3000)
                .setConnectTimeout(10000).build();
        httpPost.setConfig(requestConfig);
        HttpClient httpClient = HttpClients.createDefault();
        HttpResponse response = httpClient.execute(httpPost);
        InputStream content = response.getEntity().getContent();
        try {
            byte[] bytes = readInputStream(content);
            File newFile = new File("D:\\TEST.jar");
            FileOutputStream fops = new FileOutputStream(newFile);
            fops.write(bytes);
            fops.flush();
            fops.close();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
             content.close();
        }
    }

    private static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[10240];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }

    public static void main(String[] args) {
        String path = "D:\\hzzmysoft\\myspace\\hzzcloud\\target\\dependency\\xmlbeans-2.6.0.jar";
        File file = new File(path);
        try {
            String image = post(URL_PREFIX_STRING, "image", file,"libstest");
            System.out.println(image);
            post2("http://127.0.0.1:8924/FileAction/readImageFile1","D:\\hzzcloud\\libs\\bcpkix-jdk15on-1.60.jar");
            String hzzcloud = postseach("http://127.0.0.1:8924/FileAction/searchmodulelist.action", "hzzcloud");

            System.out.println("获取到列表"+hzzcloud);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
