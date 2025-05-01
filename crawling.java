
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import javax.net.ssl.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class crawling {

    private HashSet<String> links;
    private static final int MAX_DEPTH = 2;

    public crawling() {
        links = new HashSet<String>(); }

    public ArrayList<String> getPageLinks(String URL,String name, int depth, ArrayList<String> data)  {

        if ( !links.contains(URL) &&(depth < MAX_DEPTH)&&URL.contains(name)&& URL.contains("://") &&URL.contains("download")==false) {
            data.add(URL + "\r\n");
            System.out.println("der url ist :"+URL);
            links.add(URL);
            Document document = null;
                Boolean inp=true;
                boolean check=checkstatus(URL);
                    if (check==false  ) {
                        try {
                            document = Jsoup.connect(URL).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36").ignoreContentType(true).ignoreHttpErrors(true)
                                    .timeout(0).followRedirects(true).get();
                        } catch (IOException e) {
                            e.printStackTrace();
                            System.out.println(URL);
                                inp=false;
                        }

                    if (inp==true) {
                        Elements linksOnPage = document.select("a[href]");
                         depth++;


                        for (Element page : linksOnPage) {
                            getPageLinks(page.attr("abs:href"), name, depth, data);
                        }
                     }
                     }else if (check==true){System.out.println("ein link ist down");} }

        return data;
    }

    private SSLSocketFactory socketFactory() {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }};

        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            return sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException("Failed to create a SSL socket factory", e);
        }
    }
    private static boolean isContain(String source, String subItem) {
        String pattern = "\\b" + subItem + "\\b";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(source);
        return m.find();
    }

    public  boolean checkstatus (String url)   {

        URL  obj = null;
        try {
            obj = new URL(null,url,new sun.net.www.protocol.https.Handler());
        } catch (MalformedURLException e) {
            System.out.println(e);
            if (isContain(e.toString(),"timed out")){
                return true;
            }
        }

        HttpsURLConnection conn = null;
        try {
            conn = (HttpsURLConnection) obj.openConnection();
        } catch (IOException e) {
            System.out.println("your error "+e);
        }
        conn.setRequestProperty("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36");
        try {
            conn.connect();
        } catch (IOException e) {
            System.out.println( " unultige ssl certificate meldung "+e );
            return true;
        }


        String te="";
        if(conn.getHeaderField(0)!=null){
            te=conn.getHeaderField(0);}
        else if(conn.getHeaderField(0)==null) {
            System.out.println("your header "+te);
            return true;
        }
        if(te.compareTo("HTTP/1.0 404 Not Found")==0) {
            return true;

        }
         return false;
    }


}
