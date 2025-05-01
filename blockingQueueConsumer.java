
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class blockingQueueConsumer implements Runnable {

    private final BlockingQueue<String> queue;
    List<String> data =new ArrayList<String>();


    @Override
    public void run() {

        try {
            while (true) {
                String take = queue.take();
                data.add(take);
                process(take);

            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void process(String take) throws Exception {

        System.out.println("[Consumer] Take : " + take);
        if (checklog(take)==false ){
            System.out.println("seit gecheckt");
        }
        if (checkstatus(take)==true){
            System.out.println("satuts down ");
        }
         else if (checklog(take)==true && checkstatus(take)==false ) {

            new mainchecker(take);

            Thread.sleep(1000);
        }



    }
    public blockingQueueConsumer(BlockingQueue<String> queue) {
        this.queue = queue;
    }
    public  boolean checklog (String url) throws FileNotFoundException {
        File file = new File(System.getProperty("user.dir") + "\\log.txt");
        final Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            final String lineFromFile = scanner.nextLine();
            if(lineFromFile.contains(url)) {
                System.out.println("website schon gecheckt " +url);
              return false;

            }
            else return true;
        }return true;}
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

        HttpsURLConnection  conn = null;
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
        else return false;
    }
    private static boolean isContain(String source, String subItem) {
        String pattern = "\\b" + subItem + "\\b";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(source);
        return m.find();
    }


}
