import com.MyTask;

import java.util.Timer;

/**
 * Created by luosv on 2017/9/4 0004.
 */
public class JMapDBTool {

    public static void main(String[] args) {

        Timer timer = new Timer();

        timer.schedule(new MyTask(), 1000, 10000);

    }

}
