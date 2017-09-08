import com.model.JMapLog;
import com.service.JMapLogService;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by luosv on 2017/9/4 0004.
 */
public class JMapDBTool {

    public static void main(String[] args) {
        Timer timer = new Timer();
        timer.schedule(new MyTask(), 1000, 120000);
    }

}

class MyTask extends TimerTask {

    @Override
    public void run() {
        try {
//            if (JMapLogService.executeShell()) {
//                return;
//            }
            List<JMapLog> jMapLogList = JMapLogService.readLogFile();
            if (jMapLogList == null) {
                return;
            }
            String tabName = JMapLogService.createTable();
            if (tabName == null) {
                return;
            }
            JMapLogService.insertData(tabName, jMapLogList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
