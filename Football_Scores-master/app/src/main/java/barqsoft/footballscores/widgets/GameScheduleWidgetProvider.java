package barqsoft.footballscores.widgets;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.TaskStackBuilder;
import android.widget.RemoteViews;

import barqsoft.footballscores.MainActivity;
import barqsoft.footballscores.R;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class GameScheduleWidgetProvider extends AppWidgetProvider {

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, getClass()));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list);

    }



    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds){

        for(int widgetId : appWidgetIds){

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.game_schedule_widget);

            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            remoteViews.setOnClickPendingIntent(R.id.game_schedule_widget_frame, pendingIntent);
            remoteViews.setEmptyView(R.id.widget_list, R.id.widget_empty);

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH){
                setRemoteAdapter(context, remoteViews);
            } else {
                setLegacyRemoteAdapter(context, remoteViews);
            }

            PendingIntent clickPendingIntentTemplate = TaskStackBuilder.create(context)
                    .addNextIntentWithParentStack(new Intent(context, MainActivity.class))
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            remoteViews.setPendingIntentTemplate(R.id.widget_list, clickPendingIntentTemplate);

            appWidgetManager.updateAppWidget(widgetId, remoteViews);

        }


    }

    @SuppressWarnings("deprecation")
    private void setLegacyRemoteAdapter(Context context, RemoteViews remoteViews) {
        remoteViews.setRemoteAdapter(0, R.id.widget_list, new Intent(context, GameScheduleWidgetIntentService.class));
    }


    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void setRemoteAdapter(Context context, @NonNull RemoteViews remoteViews) {
        remoteViews.setRemoteAdapter(R.id.widget_list, new Intent(context, GameScheduleWidgetIntentService.class));
    }


}
