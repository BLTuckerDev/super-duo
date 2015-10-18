package barqsoft.footballscores.widgets;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.View;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Date;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.MainActivity;
import barqsoft.footballscores.R;
import barqsoft.footballscores.Utilities;

public class NextGameWidgetIntentService extends IntentService {

    private static final int COL_HOME = 3;
    private static final int COL_AWAY = 4;
    private static final int COL_MATCHTIME = 2;
    private static final int COL_DATE = 1;
    private static final int COL_ID = 8;


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

        Date todaysDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = formatter.format(todaysDate);

        AppWidgetManager widgetManager = AppWidgetManager.getInstance(this);

        int[] appWidgetIds = widgetManager.getAppWidgetIds(new ComponentName(this, NextGameWidgetProvider.class));

        Uri scoresByDate = DatabaseContract.scores_table.buildScoreWithDate();

        Cursor data = getContentResolver().query(scoresByDate, null, null, new String[]{ formattedDate}, null);

        if(null == data){
            showNoMatchesTodayMessage(widgetManager, appWidgetIds);
            return;
        }

        if(!data.moveToFirst()){
            showNoMatchesTodayMessage(widgetManager, appWidgetIds);
            data.close();
            return;
        }

        String homeTeamName = data.getString(COL_HOME);
        String awayTeamName = data.getString(COL_AWAY);
        String matchTime = data.getString(COL_MATCHTIME);
        String matchDate = data.getString(COL_DATE);
        Double matchId = data.getDouble(COL_ID);


        data.close();

        updateNextMatch(widgetManager, appWidgetIds, homeTeamName, awayTeamName, matchTime, matchDate, matchId);

    }

    private void updateNextMatch(AppWidgetManager widgetManager, int[] appWidgetIds,String homeTeamName, String awayTeamName, String matchTime, String matchDate, Double matchId){

        for(int widgetId : appWidgetIds){

            RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.next_game_widget);

            remoteViews.setViewVisibility(R.id.next_game_widget_no_matches_textview, View.GONE);

            remoteViews.setViewVisibility(R.id.next_game_widget_home_team_column, View.VISIBLE);
            remoteViews.setViewVisibility(R.id.next_game_widget_time_column, View.VISIBLE);
            remoteViews.setViewVisibility(R.id.next_game_widget_away_team_column, View.VISIBLE);

            remoteViews.setTextViewText(R.id.next_game_widget_home_name, homeTeamName);
            remoteViews.setImageViewResource(R.id.next_game_widget_home_crest, Utilities.getTeamCrestByTeamName(homeTeamName));

            remoteViews.setTextViewText(R.id.next_game_widget_away_name, awayTeamName);
            remoteViews.setImageViewResource(R.id.next_game_widget_away_crest, Utilities.getTeamCrestByTeamName(awayTeamName));

            remoteViews.setTextViewText(R.id.next_game_widget_time_textview, matchTime);

            Intent widgetIntent = MainActivity.getLaunchActivityToDateIntent(this, matchDate, matchId);


            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, widgetIntent, 0);
            remoteViews.setOnClickPendingIntent(R.id.next_game_widget_container_layout, pendingIntent);

            widgetManager.updateAppWidget(widgetId, remoteViews);
        }



    }

    private void showNoMatchesTodayMessage(AppWidgetManager widgetManager, int[] appWidgetIds){

        for(int widgetId : appWidgetIds){

            RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.next_game_widget);

            remoteViews.setViewVisibility(R.id.next_game_widget_no_matches_textview, View.VISIBLE);

            remoteViews.setViewVisibility(R.id.next_game_widget_home_team_column, View.INVISIBLE);
            remoteViews.setViewVisibility(R.id.next_game_widget_time_column, View.INVISIBLE);
            remoteViews.setViewVisibility(R.id.next_game_widget_away_team_column, View.INVISIBLE);

            Intent widgetIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, widgetIntent, 0);
            remoteViews.setOnClickPendingIntent(R.id.next_game_widget_container_layout, pendingIntent);

            widgetManager.updateAppWidget(widgetId, remoteViews);

        }

    }
}
