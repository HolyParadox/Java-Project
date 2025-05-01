
 import com.opencsv.CSVWriter;
 import org.apache.commons.io.FileUtils;
 import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.openqa.selenium.*;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
 import org.openqa.selenium.remote.Augmenter;
 import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
 import java.util.ArrayList;

 public class saveresult {



    public saveresult() throws IOException {  }

    public void longscreenshot(String name) throws IOException, URISyntaxException {
        subchecker sl = new subchecker();
        String url = sl.getHostName(name);
        File img = new File(System.getProperty("user.dir") + "\\data\\" + url + "\\" + url + "_full.PNG");
        if (img.exists()) {
            System.out.println("im back");
            return;
        } else
            System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") +"\\chromedriver2.exe");
        ChromeOptions options = new ChromeOptions();
        options.addExtensions(new File(System.getProperty("user.dir") +"\\cookeis.crx"));
        WebDriver driver = new ChromeDriver(options);
        driver= new Augmenter().augment(driver);
        driver.get(name);
        Screenshot fpScreenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(1000)).takeScreenshot(driver);

        ImageIO.write(fpScreenshot.getImage(), "PNG", img);

        driver.close();
    }

    public void screenshot(String name , String ts) throws IOException, AWTException {


        File theDir = new File(System.getProperty("user.dir") + "\\data\\" + name);
        if (!theDir.exists()) {

            try {
                theDir.mkdir();
            } catch (SecurityException se) {
                //handle it
            }
        }
        subchecker sl = new subchecker();
        String url = sl.getHostName(name);
        Robot robot = new Robot();

        File img = new File(System.getProperty("user.dir") + "\\data\\" + url + "\\" + url +"_"+ts+"_withcookies.PNG");
        if (img.exists()) {
            System.out.println("im back");
            return;
        } else
            System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") +"\\chromedriver2.exe");

        WebDriver driver = new ChromeDriver();

        driver.get(name);

        BufferedImage screenShot = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
        ImageIO.write(screenShot, "JPG", new File(System.getProperty("user.dir") + "\\data\\" + url + "\\" + url +"_"+ts+ "_fullbrowser.PNG"));
        driver.close();
    }

    public void screenshothtml(String name, String url, int i) throws IOException, URISyntaxException, AWTException, InterruptedException {


        File img = new File(System.getProperty("user.dir") + "\\data\\" + url + "\\" + url + "_" + i + ".PNG");
        if (img.exists()) {
            System.out.println("im back");
            return;
        } else
            System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") +"\\chromedriver2.exe");

        WebDriver driver = new ChromeDriver();
        driver.get(name);
        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(scrFile, img);
        driver.close();
    }

    public void save(String url, ArrayList<String> data) throws IOException, URISyntaxException {

        subchecker sl = new subchecker();
        String name = sl.getHostName(url);
        // System.out.println("your dir : \n" +       System.getProperty("user.dir"));
        File theDir = new File(System.getProperty("user.dir") + "\\data\\" + name);
        if (!theDir.exists()) {

            try {
                theDir.mkdir();
            } catch (SecurityException se) {
                //handle it
            }
        }
        File file = new File(System.getProperty("user.dir") + "\\data\\" + name + "\\" + name + ".txt");

        file.createNewFile(); // if file already exists will do nothing ArrayList<String> list
        FileWriter fw = new FileWriter(file, true);

        for (int i = 0; i < data.size(); i++) {
            // String  text = data.get(i).replace("\n", "").replace("\r", "");

            fw.write(data.get(i) + "\r\n");
        }

        fw.close();

        //}*/

    }

    public void savepdf(String url, ArrayList<String> data) throws IOException, URISyntaxException {
        subchecker sl = new subchecker();
        String name = sl.getHostName(url);
        File pdf = new File(System.getProperty("user.dir") + "\\data\\" + name + "\\" + name + "1_.pdf");
        pdf.createNewFile();
        PDDocument doc = new PDDocument();
        PDPage page = new PDPage();
        doc.addPage(page);
        PDPageContentStream contentStream = new PDPageContentStream(doc, page);
        PDFont pdfFont = PDType1Font.HELVETICA;
        float fontSize = 12;
        float leading = 1.5f * fontSize;
        PDRectangle mediabox = page.getMediaBox();
        float margin = 40;
        float width = mediabox.getWidth() - 2 * margin;
        float startX = mediabox.getLowerLeftX() + margin;
        float startY = mediabox.getUpperRightY() - margin;
        int lastSpace = -1;

        ArrayList<String> lines = new ArrayList<String>();
        for (int i = 0; i < data.size(); i++) {
            String text = data.get(i);
            while (text.length() > 0) {

                int spaceIndex = text.indexOf(' ', lastSpace + 1);
                if (spaceIndex < 0)
                    spaceIndex = text.length();
                String subString = text.substring(0, spaceIndex);
                float size = fontSize * pdfFont.getStringWidth(subString) / 1000;
                System.out.printf("'%s' - %f of %f\n", subString, size, width);
                if (size > width) {
                    if (lastSpace < 0)
                        lastSpace = spaceIndex;
                    subString = text.substring(0, lastSpace);
                    lines.add(subString);
                    text = text.substring(lastSpace).trim();
                    System.out.printf("'%s' is line\n", subString);
                    lastSpace = -1;
                } else if (spaceIndex == text.length()) {
                    lines.add(text);

                    System.out.printf("'%s' is line\n", text);
                    text = "";
                } else {
                    lastSpace = spaceIndex;
                }
            }
        }

        contentStream.beginText();
        contentStream.setFont(pdfFont, fontSize);
        contentStream.newLineAtOffset(startX, startY);

        for (String line : lines) {
            contentStream.showText(line);
            contentStream.newLineAtOffset(0, -leading);
        }
        contentStream.endText();
        contentStream.close();

        doc.save(pdf);

        {
            if (doc != null) {
                doc.close();
            }
        }


    }

    public void savehtml(String url, String name, int i , String time) throws IOException {

        File theDir = new File(System.getProperty("user.dir") + "\\data\\" + name);
        if (!theDir.exists()) {

            try {
                theDir.mkdir();
            } catch (SecurityException se) {
                //handle it
            }
        }

        File file = new File(System.getProperty("user.dir") + "\\data\\" + name + "\\" + name + "_" +time+"_"+ i + ".html");
        file.createNewFile(); // if file already exists will do nothing ArrayList<String> list
        FileWriter fw = new FileWriter(file, true);

        URL aurl = new URL(url);

        URLConnection con = aurl.openConnection();
        con.setRequestProperty("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36");

        String te="";
        if(con.getHeaderField(0)!=null){
        te=con.getHeaderField(0);}

         if(te.compareTo("HTTP/1.1 200 OK")==0  ) {

            con.connect();
        InputStream is = con.getInputStream();

        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        String line = "";


        while ((line = br.readLine()) != null) {
            System.out.println(line);
            fw.write(line);
        }
        fw.close();
        br.close();}
         else System.out.println("no acces");
    }

    public void savecsv( String time , String url,String fehler , String nameimp , String adresseimp) throws IOException {

        File file= new File(System.getProperty("user.dir") +"\\log.csv");
        file.createNewFile();// if file already exists will do nothing
        CSVWriter writer = new CSVWriter(new FileWriter( file,true) );

        String [] record = {time,url,fehler,nameimp,adresseimp};
        writer.writeNext(record,false);
        writer.close();

    }

    public void savelog(String url) throws IOException {
            File file= new File(System.getProperty("user.dir") +"\\log.txt");
            file.createNewFile();// if file already exists will do nothing
            FileWriter fw = new FileWriter(file, true);
            fw.write(url);
            fw.close();
        }





}
