

import java.io.File;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class main {

    public static void main(String[] args) throws Exception {
        BlockingQueue<String> queue = new LinkedBlockingQueue<>(10);
        creatordnedata();
        creatlogfile();
        creatinputfile();
        new Thread(new blockingQueueuser(queue)).start();
        new Thread(new blockingQueueConsumer(queue)).start();

    }

      static void creatordnedata(){
          File theDir = new File(System.getProperty("user.dir") + "\\data\\");
          if (!theDir.exists()) {
              try {
                  theDir.mkdir();
              } catch (SecurityException se) {
                  //handle it
              }}}
    static  void creatinputfile() throws IOException {
          File file = new File(System.getProperty("user.dir") + "\\input.txt");
          file.createNewFile();
      }
    static   void creatlogfile() throws IOException {
        File file = new File(System.getProperty("user.dir") + "\\log.txt");file.createNewFile();}



}

