package barqsoft.footballscores.widgets;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.MainActivity;
import barqsoft.footballscores.R;
import barqsoft.footballscores.Utilies;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class GameScheduleWidgetIntentService extends RemoteViewsService {

    private static final String[] SCORES_COLUMNS = {
        DatabaseContract.SCORES_TABLE + "." + DatabaseContract.scores_table._ID,
            DatabaseContract.scores_table.LEAGUE_COL,
            DatabaseContract.scores_table.DATE_COL,
            DatabaseContract.scores_table.TIME_COL,
            DatabaseContract.scores_table.HOME_COL,
            DatabaseContract.scores_table.AWAY_COL,
            DatabaseContract.scores_table.HOME_GOALS_COL,
            DatabaseContract.scores_table.AWAY_GOALS_COL,
            DatabaseContract.scores_table.MATCH_ID,
            DatabaseContract.scores_table.MATCH_DAY
    };

    private static final int COL_HOME = 3;
    private static final int COL_AWAY = 4;
    private static final int COL_HOME_GOALS = 6;
    private static final int COL_AWAY_GOALS = 7;
    private static final int COL_DATE = 1;
    private static final int COL_LEAGUE = 5;
    private static final int COL_MATCHDAY = 9;
    private static final int COL_ID = 8;
    private static final int COL_MATCHTIME = 2;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsService.RemoteViewsFactory(){

            private Cursor data = null;

            @Override
            public void onCreate() {}

            @Override
            public void onDataSetChanged() {

                if(data != null){
                    data.close();
                }

                final long identityToken = Binder.clearCallingIdentity();
                Uri scoresUri = DatabaseContract.scores_table.buildScoreWithDate();

                data = getContentResolver().query(scoresUri,
                        SCORES_COLUMNS,
                        null,
                        null,
                        DatabaseContract.scores_table.DATE_COL + " ASC");


                Binder.restoreCallingIdentity(identityToken);


            }

            @Override
            public void onDestroy() {
                if (data != null) {
                data.close();
                data = null;
            }}

            @Override
            public int getCount() {
                return data == null ? 0 : data.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {

                if(position == AdapterView.INVALID_POSITION || null == data || !data.moveToPosition(position)){
                    return null;
                }


                RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.scores_list_item);

                remoteViews.setTextViewText(R.id.home_name, data.getString(COL_HOME));
                remoteViews.setTextViewText(R.id.away_name, data.getString(COL_AWAY));
                remoteViews.setTextViewText(R.id.data_textview, data.getString(COL_MATCHTIME));
                remoteViews.setTextViewText(R.id.score_textview, Utilies.getScores(data.getInt(COL_HOME_GOALS), data.getInt(COL_AWAY_GOALS)));
                remoteViews.setImageViewResource(R.id.home_crest, Utilies.getTeamCrestByTeamName(data.getString(COL_HOME)));
                remoteViews.setImageViewResource(R.id.away_crest, Utilies.getTeamCrestByTeamName(data.getString(COL_AWAY)));

                Intent clickIntent = new Intent(getApplicationContext(), MainActivity.class);

                remoteViews.setOnClickFillInIntent(R.id.score_list_item_container, clickIntent);

                return remoteViews;
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.scores_list_item);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return false;
            }
        };
    }

}
