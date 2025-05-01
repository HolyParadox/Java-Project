import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class mainchecker {

    private static final String analtic = "gtag.js|Gtag.js|analytics.js|Analytics.js|ga.js|Ga.js|Analytics|analytics";
    private static final String analtix = "analytics|Analytics";
    private static final String pixel = "fbq|Pixel";
    private static final String datenshutz = "datenschutz|Datenschutz|privacy|Privacy|privacy-policy|policy";
    private static final String impressum = "impressum|Impressum|imprint|Imprint";
    private static final String aip = "anonymize_ip|Anonymize_ip|anonymizeIp|AnonymizeIp ";
    private static final String adresse="Address |address |Anschrift |anschrift ";
    private static final String nameimpress="name |Name ";
    ArrayList<String> save = new ArrayList<>();
    ArrayList<String> savepdf = new ArrayList<>();




    mainchecker(String url) throws Exception {

        Date date= new Date();
        long time = date.getTime();
        Timestamp ts = new Timestamp(time);
        String nameimpressum = "";
        String adresseimpressum="";
        boolean dts=false;
        String zeit=ts.toString().replace("-","_").replace(".","_").replace(":","_").replace(" ","_");
        // System.setProperty("http.agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.79 Safari/537.36");
        ArrayList<String> data;
        ArrayList<String> databuffer = new ArrayList<>();
        String buffer = "";
        crawling cl = new crawling();
        subchecker sb = new subchecker();
        String name=sb.getHostName(url);


        ///ruf check für url ob es http ist
        buffer=sb.httpchecker(url);
        if (buffer.compareTo("ok") == 0) {
            System.out.println("alls ok es ist kein http seite");}
        else if (buffer.compareTo("ok") != 0) {
            save.add("" + buffer);savepdf.add("" + buffer) ;}



        ///ruf check für ssl gültgikeit

        buffer = sb.sslchecker(url);
        if (buffer.compareTo("ok") == 0) {
            System.out.println("alls ok mit ssl");}
        else if (buffer.compareTo("ok") != 0) {
            save.add("" + buffer);savepdf.add("" + buffer) ;}




        // ruf check für url ob es https rederction gibt.

        buffer = sb.urichecker(url);
        if (buffer.compareTo("ok") == 0) {
            System.out.println("alls ok mit uri"); }
        else if (buffer.compareTo("ok") != 0) {
            save.add("" + buffer);savepdf.add("" + buffer); }




        ///daten werden gecrawlt und gecheckt
        data = cl.getPageLinks(url,name, -1, databuffer);
        verificationArray(sb.mixedchecker(data,name,savepdf),"http");

        // ruf check nach datenshutz
        buffer=  sb.impdatenshutz(data,datenshutz);
        if(buffer.compareTo("nop")==0){
            System.out.println("kein datnshutz"); save.add("warnung es ist kein datenshutz auf der seite.");savepdf.add("warnung es ist kein datenshutz auf der seite.");}
        else if (buffer.compareTo("nop")!=0) {
            System.out.println("es hat datenshutz");dts=true; }






//check nach impressum und ob impressum in datenchutz
        String impress = sb.impdatenshutz(data,impressum); if(impress.compareTo("nop")==0){if (dts==true){
            String be="";sb.sucheinhtml(buffer,impressum);
            if (be.compareTo("nop")!=0){System.out.println("impress ist in datesnshutz");impress=buffer;}
            else if(be.compareTo("nop")==0){
                System.out.println("impress ist nicht in datesnshutz");
                System.out.println("kein impress");
                save.add("warnung es ist kein impressum auf der seite.");savepdf.add("warnung es ist kein impressum auf der seite.");}
        }else if (dts==false){
            System.out.println("kein impress"); save.add("warnung es ist kein impressum auf der seite.");
            savepdf.add("warnung es ist kein impressum auf der seite.");}}
        else if(impress.compareTo("nop")!=0){
            System.out.println("es hat impress");
            nameimpressum=sb.sucheinhtml(impress,nameimpress);adresseimpressum=sb.sucheinhtml(impress,adresse); }
        if(nameimpressum.compareTo("nop")==0){nameimpressum="name in impresse nicht gefunden"; }
        else if (nameimpressum.compareTo("nop")!=0){System.out.println("es hat ein name im impressum"+nameimpressum);}
        if(adresseimpressum.compareTo("nop")==0){adresseimpressum="adresse in impresse nicht gefunden"; }
        else if (adresseimpressum.compareTo("nop")!=0){System.out.println("es hat ein adresse im impressum"+adresseimpressum);}



        //analytics check
        analyticverification(url,buffer,analtic,analtix,aip);






                //save
                if (save.size() == 0) {
                    System.out.println("alles gut mit der seite");
                    new saveresult().savelog(url);
                    //Save to cvs save to log
                } else if (save.size() > 0) {
                    StringBuilder stb = new StringBuilder();
                    for (String s : savepdf)
                    {
                        stb.append(s);
                        stb.append("\t");
                    }
                    String savecvs=stb.toString();
                    savecvs.replaceAll(",","");

                    save.add("zeit : "+ts.toString());
                    cookies(url);
                    new saveresult().save(url,save);
                    new saveresult().savepdf(url,savepdf);
                    new saveresult().screenshot(url,zeit);
                    new saveresult().longscreenshot(url);
                    if(impress.compareTo("nop")!=0){ new saveresult().savehtml(impress,name,001,"_Impressum"+zeit);}else if(impress.compareTo("nop")==0){System.out.println("no impress to save ");}
                    if(buffer.compareTo("nop")!=0){ new saveresult().savehtml(buffer,name,002,"_Datenschutz"+zeit);}else if(buffer.compareTo("nop")==0){System.out.println("no privacy to save ");}
                    new saveresult().savelog(url);
                    new saveresult().savecsv(ts.toString(),url,savecvs,"nameimpressum","adresseimpressum"); //get adress get name
                    System.out.println("save " + save);} //cvs file + log

    }






    public void verificationArray(ArrayList<String> url, String msg) {

        int cnt;
        cnt = url.size();
        if (cnt > 0) {
            for (int i = 0; i < cnt; i++) {
                save.add(url.get(i));

                System.out.println(" problem bei" + msg);
            }
        } else System.out.println("kein problem bei" + msg);
    }
    public void analyticverification(String url, String Ds, String anal, String anal2, String Aip) throws IOException {
        subchecker sb = new subchecker();
        String bf1;
        boolean analytic = false;
        boolean aip = false;

        if (sb.sucheinhtml(url, anal).compareTo("nop") != 0) {
            analytic = true;
            System.out.println("analtic .js ist voranden");

        } else if (sb.sucheinhtml(url, anal).compareTo("nop") == 0) { System.out.println("kein analtics .js"); }

        bf1 = sb.sucheinhtml(url, Aip);
        if(bf1.compareTo("nop")==0){System.out.println("kein aip");save.add("anlytic ist voranden aber kein aip zu finden auf der html");savepdf.add("anlytic ist voranden aber kein aip zu finden auf der html");}
        else if (bf1.compareTo("nop") != 0 && isContain(bf1, "true")) {
            aip = true;
            System.out.println("aip ist voranden and true");
        } else if (bf1.compareTo("nop") != 0 && isContain(bf1, "false")) {
            System.out.println("aip ist voranden and false");
            save.add("aip ist voranden aber auf false in : " + url);savepdf.add("aip ist voranden aber auf false in : " + url);
        }


        if (Ds.compareTo("nop")==0){System.out.println("kein datenshtuz ");}
        else if ( Ds.compareTo("nop") !=0) {
             if (aip == true && analytic == true && sb.sucheinhtml(Ds, anal2).compareTo("nop")!=0){
            System.out.println("analtic sind in ordnung und sind in daten shutz erwänt");}
         else  if (aip == true && analytic == true && sb.sucheinhtml(Ds, anal2).compareTo("nop") ==0) {
            System.out.println("aip und .js in ordung aber nicht in daten shutz erwähnt");
        save.add("aip und .js in ordung aber nicht in daten shutz erwähnt" + Ds); savepdf.add("aip und .js in ordung aber nicht in daten shutz erwähnt" + Ds);}}
    }
    public void pixelverification(String url, String Ds, String anal, String anal2, String Aip) throws IOException {
        subchecker sb = new subchecker();
        String bf1;
        boolean analytic = false;
        boolean aip = false;

        if (sb.sucheinhtml(url, anal).compareTo("nop") != 0) {
            analytic = true;
            System.out.println("facebook pixel ist voranden");

        } else if (sb.sucheinhtml(url, anal).compareTo("nop") == 0) { System.out.println("kein pixel");save.add("kein pixel in : " + url);savepdf.add("kein pixel  in : " + url); }

        bf1 = sb.sucheinhtml(url, Aip);
        if(bf1.compareTo("nop")==0){System.out.println("kein tracking");}
        else if (bf1.compareTo("nop") != 0 && isContain(bf1, "true")) {
            aip = true;
            System.out.println("tracking ist voranden and true");
        } else if (bf1.compareTo("nop") != 0 && isContain(bf1, "false")) {
            System.out.println("tracking ist voranden and false");
            save.add("tracking ist voranden aber auf false in : " + url);savepdf.add("tracking ist voranden aber auf false in : " + url);
        } else System.out.println("kein tracking");


        if (Ds.compareTo("nop")==0){System.out.println("kein datenshtuz ");}
        else if ( Ds.compareTo("nop") !=0) {
            if (aip == true && analytic == true && sb.sucheinhtml(Ds, anal2).compareTo("nop")!=0){
                System.out.println("facebook pixel sind in ordnung und sind in daten shutz erwänt");}
            else  if (aip == true && analytic == true && sb.sucheinhtml(Ds, anal2).compareTo("nop") ==0) {
                System.out.println("tracking und .facebook pixel in ordung aber nicht in daten shutz erwähnt");
                save.add("tracking und .facebook pixel in ordung aber nicht in daten shutz erwähnt" + Ds); savepdf.add("tracking und .facebook pixel in ordung aber nicht in daten shutz erwähnt" + Ds);}}
    }
    private static boolean isContain(String source, String subItem) {
        String pattern = "\\b" + subItem + "\\b";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(source);
        return m.find();
    }
    public void cookies(String url) throws URISyntaxException {

        System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") +"\\chromedriver2.exe");
         int i = 0;
        final ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");
        WebDriver driver = new ChromeDriver(chromeOptions);
        driver.get(url);


        for (Cookie ck : driver.manage().getCookies()) {
            i++;
            save.add("Cookies nummer : " + i + (" name : " + ck.getName() + " ### value : " + ck.getValue() + " ### Domain : " + ck.getDomain() + " ### Path : " + ck.getPath() + " ### Expiry : " + ck.getExpiry() + " ### Secure status : " + ck.isSecure()));
            savepdf.add("Cookies nummer : " + i + (" name : " + ck.getName() + " ### value : " + ck.getValue() + " ### Domain : " + ck.getDomain() + " ### Path : " + ck.getPath() + " ### Expiry : " + ck.getExpiry() + " ### Secure status : " + ck.isSecure()));

        }
        driver.close();

     }
}


