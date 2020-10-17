package org.QinTai.Read.utlis;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

/**
 * @author wuangjing
 * @create 2020/10/16-14:40
 * @Description:
 */
@Component
public class HttpClientUtlis {

    private PoolingHttpClientConnectionManager cm;

    public HttpClientUtlis() {
        this.cm = new PoolingHttpClientConnectionManager();
        //设置最大连接数
        cm.setMaxTotal(100);
        //设置每个主机最大的连接数
        cm.setDefaultMaxPerRoute(10);
    }

    public String pareHtml(String url) {
        //得到httpClient对象
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();
        //声明HttpGet对象
        HttpGet httpGet = new HttpGet(url);
        //声明参数
        httpGet.setConfig(this.Config());

        //发收请求
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200) {
                //判断Entity非空
                if (response.getEntity() != null) {
                    //得到DOM 以utf-8编码
                    String html = EntityUtils.toString(response.getEntity(), "utf-8");
                    return html;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //得到图片
    public String pareImage(String url) {
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(this.Config());
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200) {
                if (response.getEntity() != null) {
                    // 获取文件类型
                    String extName = url.substring(url.lastIndexOf("."));
                    // 使用uuid生成图片名
                    String imageName = UUID.randomUUID().toString() + extName;
                    // 声明输出的文件
                    OutputStream outstream = new FileOutputStream(new File("D:/images/" + imageName));
                    // 使用响应体输出文件
                    response.getEntity().writeTo(outstream);

                    // 返回生成的图片名
                    return imageName;

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private RequestConfig Config() {
        RequestConfig config = RequestConfig.custom().setConnectTimeout(1000)// 设置创建连接的超时时间
                .setConnectionRequestTimeout(500) // 设置获取连接的超时时间
                .setSocketTimeout(10000) // 设置连接的超时时间
                .build();
        return config;
    }
}
