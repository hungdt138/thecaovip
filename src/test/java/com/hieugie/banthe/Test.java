package com.hieugie.banthe;

import com.google.common.io.BaseEncoding;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.net.*;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        System.out.println(BaseEncoding.base64().encode("Đào trung hiếu".getBytes()));
    }


//    public static void main(String[] args) throws Exception {
////        HttpPost httpPost = new HttpPost("https://dichvuthongtin.dkkd.gov.vn/inf/Public/Srv.aspx/GetSearch");
////
////        List<NameValuePair> params = new ArrayList<NameValuePair>();
////        params.add(new BasicNameValuePair("searchField", "0105987432"));
////        params.add(new BasicNameValuePair("h", "636834274795901142-952F6229C68DB13AA0B910F5CC79F54160A96BA603D6AB9B34B77BA470333A9C"));
////        httpPost.setEntity(new UrlEncodedFormEntity(params));
////
////        CloseableHttpClient client = HttpClients.custom().
////            setHostnameVerifier(new AllowAllHostnameVerifier()).
////            setSslcontext(new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy()
////            {
////                public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException
////                {
////                    return true;
////                }
////            }).build()).build();
////        CloseableHttpResponse response = client.execute(httpPost);
////
////        client.close();
//
//
//        CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
//        RestTemplate restTemplate = new RestTemplate(
//            new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));
//
//        // Add one interceptor like in your example, except using anonymous class.
//        restTemplate.setInterceptors(Collections.singletonList((request, body, execution) -> {
//
//            return execution.execute(request, body);
//        }));
//        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
//        CloseableHttpClient build = HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy()).build();
//        factory.setHttpClient(build);
//        restTemplate.setRequestFactory(factory);
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//        String json = "{ \"searchField\": \"0105987432\",\"h\": \"636834282187729609-DD7C0E362BC7BD23CBD6029163566C74EA368CF0F987078CDF70C54F69D7F409\"}";
//        HttpEntity<String> request = new HttpEntity<>(json, headers);
//
//        // Post trên trang của thuế để tra cứu mã số thuế
//        // lấy serial so sánh với serial trong chứng thư
//        // trong trường hợp không lấy được serial
//        // cố gắng parse từ chứng thư số ra
//        ResponseEntity<String> response = restTemplate.postForEntity("https://dichvuthongtin.dkkd.gov.vn/inf/Public/Srv.aspx/GetSearch", request , String.class );
//    }

//    public static void main(String[] args) throws Exception {
//        System.out.println(get("0105987432"));
//    }

    private static String get(String search) throws Exception {
        trustSSL();

        // First set the default cookie manager.
        CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));

        URL url = new URL("https://dichvuthongtin.dkkd.gov.vn/inf/Public/Srv.aspx/GetSearch");
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        //headers
        conn.setRequestProperty("Content-Type", "application/json");

        conn.setDoOutput(true);
        conn.setUseCaches(false);
        HttpURLConnection.setFollowRedirects(false);
        OutputStream os = conn.getOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
        osw.write("{\"searchField\": \"" + search + "\",\"h\": \"636834946793238530-401EAF4D31FB489213AF300D5AB53B9C2AD13D819782D9B319E75262122AB688\"}");
        osw.flush();
        osw.close();
        os.close();
        conn.connect();

        BufferedReader br = new BufferedReader(new InputStreamReader(
            conn.getInputStream(), "UTF-8"));

        StringBuilder sb = new StringBuilder(2048);
        for (String line; (line = br.readLine()) != null; ) {
            sb.append(line);
        }
        conn.disconnect();

        return sb.toString();

    }

    private static void trustSSL() {
        TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(
                    java.security.cert.X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(
                    java.security.cert.X509Certificate[] certs, String authType) {
                }
            }
        };

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
