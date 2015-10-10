package barqsoft.footballscores.widgets;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

public class NextGameWidgetIntentService extends IntentService {

    public static void getNextGame(Context context) {
        Intent intent = new Intent(context, NextGameWidgetIntentService.class);
        context.startService(intent);
    }

    public NextGameWidgetIntentService() {
        super("NextGameWidgetIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (null == intent) {
            return;
        }


        //get the games coming up for today and then look at what time it is and get the next
        //game to be played.


    }
}
