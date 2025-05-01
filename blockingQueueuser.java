import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.HashSet;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;

public class blockingQueueuser implements Runnable {

    private final BlockingQueue<String> queue;
    File file = new File(System.getProperty("user.dir")+"\\input.txt");
    FileReader r = new FileReader(file);
    BufferedReader reader = new BufferedReader(r);
    String line = null;

    @Override
    public void run() {

        try {
            process();
        } catch (InterruptedException | FileNotFoundException e) {
            Thread.currentThread().interrupt();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void process(  ) throws InterruptedException, IOException {

        while ((line = reader.readLine()) != null) {
            String url =line.replace("\n", "").replace("\r", "");
             System.out.println("put in queue "+url);
            queue.put("https://"+url);

        }

    }

    public blockingQueueuser(BlockingQueue<String> queue) throws FileNotFoundException {
        this.queue = queue;
    }
}