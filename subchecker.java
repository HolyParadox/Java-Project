import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.security.cert.*;
import java.util.*;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class subchecker {


    subchecker(){}


    private static boolean isContain(String source, String subItem){
        String pattern = "\\b"+subItem+"\\b";
        Pattern p=Pattern.compile(pattern);
        Matcher m=p.matcher(source);
        return m.find();
    }
    private static boolean isContainmixed(String source, String subItem){
        String pattern = subItem;
        Pattern p=Pattern.compile(pattern);
        Matcher m=p.matcher(source);
        return m.find();
    }

    public String sslchecker (String aURL) throws IOException {
        Date today = Calendar.getInstance().getTime();
        URL  destinationURL = new URL(null,aURL,new sun.net.www.protocol.https.Handler());

        HttpsURLConnection  conn = (HttpsURLConnection) destinationURL.openConnection();
        conn.setRequestProperty("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.79 Safari/537.36");
         try {
            conn.connect();
        } catch (IOException e) {
            System.out.println( " unultige ssl certificate meldung "+e );
            return "ungultige ssl certificate meldung : "+e;
        }
        Certificate[] certs ;
        certs = conn.getServerCertificates();
        for (Certificate cert : certs) {
            if(cert instanceof X509Certificate) {
                X509Certificate x = (X509Certificate ) cert;
                if(x.getNotAfter().compareTo(today)<0){
                    return "Warnung ssl certificate abgelaufen : "+x.getNotAfter();
                    //TO DO such validation von cert
                } } }return "ok";}

                //ask if need to keep ?
    public String urichecker (String url) throws MalformedURLException { // 301 fehler
        String nUrl ="http://"+getHostName(url);
        URL obj = new URL(null,nUrl,new sun.net.www.protocol.https.Handler());
        URLConnection conn = null;
        try {
            conn = obj.openConnection();
        } catch (IOException e) {
         System.out.println("your error"+e);
        }
        conn.setRequestProperty("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36");
        if (conn.getHeaderField(0)!=null){
        System.out.println(conn.getHeaderField(0));
        if(conn.getHeaderField(0).compareTo("HTTP/1.1 200 OK")==0) {
            System.out.println("warnung verbindung nicht sicher : es kann durch http accessed sein ohne redirection  auf https " + conn.getHeaderField(0));
            return "warnung verbindung nicht sicher :  es kann durch http accessed sein ohne redirection  auf https " + conn.getHeaderField(0);
        }
        else if (conn.getHeaderField(0)==null){System.out.println("empty link in uri checker");}
        }return "ok";}

    public String httpchecker (String url) throws Exception { // 301 fehler

        URI uri = null;
        try {
            uri = new URI(url);

        } catch (URISyntaxException e) {
            e.printStackTrace();

        }
        String hostname = uri.getScheme();
        if (hostname != null&& hostname.compareTo("http")==0) {
             return "warnung es ist ein http seite";
        }
        return "ok";}

        public String getHostName(String url)   {




            URI uri = null;
            try {
                uri = new URI(url);

            } catch (URISyntaxException e) {
                 e.printStackTrace();

            }
            String hostname = uri.getHost();
            if (hostname != null) {
                return hostname.startsWith("www.") ? hostname.substring(4) : hostname;
            }
            return hostname;
        }

        public ArrayList<String> mixedchecker (ArrayList<String> data,String name,ArrayList<String> PDFdata ) throws IOException {

        ArrayList<String> buffer=new ArrayList<>();

        for (int i =0 ; i<data.size();i++){
                 String line ="";
               /* System.setProperty("webdriver.chrome.driver", "C:\\Users\\ME\\IdeaProjects\\Maincrowly\\chromedriver2.exe");
                final ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--headless");
                WebDriver driver = new ChromeDriver(chromeOptions);

                driver.get(data.get(i));
                LogEntries logEntries = driver.manage().logs().get(LogType.BROWSER);
                for (LogEntry entry : logEntries) {
                    if( isContain(entry.getMessage(),"Mixed Content")==true){
                        line=new Date(entry.getTimestamp()) + " " + entry.getLevel() + " " + entry.getMessage();
                        System.out.println(line);
                    } }driver.close();*/


            try {

                line = sucheinhtmlmixed(data.get(i));
            } catch (MalformedURLException e) {
                System.out.println("your mixed error here "+e );
            }
            if ( line!= "nop"  ) {
                System.out.println("http found in "   + data.get(i)+":"+line);
                //   new saveresult().screenshothtml(data.get(i),name,i);
                new saveresult().savehtml(data.get(i),name,i,"2020");
                buffer.add("http wurde hier gefunden : "+data.get(i));
                buffer.add(line);


            }
              else if(line=="nop"){System.out.println("lerres zeile in http");}

            line="";
            }
            if(buffer.size()>0){PDFdata.add("http wurde  gefunden.check die .html datei fur mehr infos."); }
            return buffer; }

    public String impdatenshutz (ArrayList<String> data, String word){

        for (int i =0 ; i<data.size();i++){

            if (isContain(data.get(i), word) == true) {
                return data.get(i);
            }

        }
        return "nop"; }

    public String sucheinhtml (String aUrl, String word ) throws IOException {

        final URL url = new URL(aUrl);

        URLConnection con = url.openConnection();
        con.setRequestProperty("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.79 Safari/537.36");
        if(con.getHeaderField(0)!=null){

        if(con.getHeaderField(0).compareTo("HTTP/1.1 200 OK")==0  ) {

            con.connect();
            InputStream is = con.getInputStream();


    try (final BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
        // Read the whole page
        while (true) {
            final String line = br.readLine();
            if (line == null) {
                break;
            }
            if (isContain(line, word) == true) {
                return line;
                //return true ;
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}}

        return "nop"; }

    public String sucheinhtmlmixed (String aUrl ) throws IOException {

        final URL url = new URL(aUrl);

        URLConnection con = url.openConnection();
        con.setRequestProperty("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.79 Safari/537.36");
        if(con.getHeaderField(0)!=null){

            if(con.getHeaderField(0).compareTo("HTTP/1.1 200 OK")==0  ) {

                con.connect();
                InputStream is = con.getInputStream();



                try (final BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                    // Read the whole page
                    String pat = "nop";

                    while (true) {
                        final String line = br.readLine();
                        if (line == null) {
                            break;
                        }
                        if (isContainmixed(line, "<iframe src=\"http://") == true) {

                            pat =pat+ line;
                         }
                       else if (isContainmixed(line, "<img src=\"http://") == true) {
                            pat =pat+ "\n"+line;
                        }
                        else  if (isContainmixed(line, "<audio src=\"http://") == true) {
                            pat =pat+"\n"+ line;
                        }
                        else   if (isContainmixed(line, "<video src=\"http://") == true) {
                            pat =pat+"\n"+ line;
                        }
                        else  if (isContainmixed(line, "<script src=\"http://") == true) {
                            pat =pat+"\n"+ line;
                        }
                        else  if (isContainmixed(line, "<form action=\"http://") == true) {
                            pat =pat+ "\n"+line;
                        }
                        else  if (isContainmixed(line, "<embed src=\"http://") == true) {
                            pat =pat+"\n"+ line;
                        }
                        else  if (isContainmixed(line, "<source src=\"http://") == true) {
                            pat =pat+ "\n"+line;
                        }
                        else if (isContainmixed(line, "<param value=\"http://") == true) {
                            pat =pat+ "\n"+line;
                        }
                        else if (isContainmixed(line, "<link src=\"http://") == true) {
                            pat =pat+ "\n"+line;
                        }
                        else  if (isContainmixed(line, "<link href=\"http://") == true) {
                            pat =pat+ "\n"+line;
                        }
                        else  if (isContainmixed(line, "<script async=\"http://") == true) {
                            pat =pat+ "\n"+line;
                        }

                    }
                    return pat;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        return "nop"; }

}

