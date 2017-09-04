import com.test.LogTest;

/**
 * Created by luosv on 2017/9/4 0004.
 */
public class JMapDBTool {

    public static void main(String[] args) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    LogTest.getInstance().testLog();
                }
            }
        });

        thread.start();

    }

}
