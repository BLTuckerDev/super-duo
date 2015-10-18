package barqsoft.footballscores;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends ActionBarActivity {

    public static int selected_match_id;

    public static int current_fragment = 2;

    public static String LOG_TAG = "MainActivity";

    private static String DATE_INTENT_EXTRA_KEY = "date";
    private static String MATCH_ID_INTENT_EXTRA_KEY = "matchId";

    private final String save_tag = "Save Test";
    private PagerFragment my_main;

    private static void calculcateCurrentFragment(int todaysDate, int intentRequestedDate){

        int fragmentPage = (intentRequestedDate + 2) - todaysDate;

        if(fragmentPage >= 0 && fragmentPage <= 4){
            MainActivity.current_fragment = fragmentPage;
        }

    }


    public static Intent getLaunchActivityToDateIntent(Context context, String dateString, Double matchId){

        Intent launchIntent = new Intent(context, MainActivity.class);

        launchIntent.putExtra(DATE_INTENT_EXTRA_KEY, dateString);
        launchIntent.putExtra(MATCH_ID_INTENT_EXTRA_KEY, matchId);

        return launchIntent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(LOG_TAG, "Reached MainActivity onCreate");
        if (savedInstanceState == null) {
            setupPagerFragment();
        }
    }


    private void setupPagerFragment(){

        if(getIntent().hasExtra(DATE_INTENT_EXTRA_KEY)){
            handleIntentDateExtra();
        } else {
            current_fragment = 2;
        }


        if(getIntent().hasExtra(MATCH_ID_INTENT_EXTRA_KEY)){
            handleIntentMatchIdExtra();
        } else {
            selected_match_id = 0;
        }



        my_main = new PagerFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, my_main)
                .commit();
    }

    private void handleIntentDateExtra(){

        String dateExtra = getIntent().getStringExtra(DATE_INTENT_EXTRA_KEY);
        String[] dateParts = dateExtra.split("-");

        if(dateParts.length == 3){
            try{
                int dayOfMonth = Calendar.getInstance().get(Calendar.DATE);
                int intentExtraDay = Integer.valueOf(dateParts[2]);
                calculcateCurrentFragment(dayOfMonth, intentExtraDay);
            } catch (NumberFormatException ex){
                current_fragment = 2;
                Log.e(LOG_TAG, "Invalid date string passed to MainActivity");
            }
        }
    }

    private void handleIntentMatchIdExtra(){
        Double matchIdExtra = getIntent().getDoubleExtra(MATCH_ID_INTENT_EXTRA_KEY, 0.0);
        selected_match_id = matchIdExtra.intValue();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            Intent start_about = new Intent(this, AboutActivity.class);
            startActivity(start_about);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.v(save_tag, "will save");
        Log.v(save_tag, "fragment: " + String.valueOf(my_main.mPagerHandler.getCurrentItem()));
        Log.v(save_tag, "selected id: " + selected_match_id);
        outState.putInt("Pager_Current", my_main.mPagerHandler.getCurrentItem());
        outState.putInt("Selected_match", selected_match_id);
        getSupportFragmentManager().putFragment(outState, "my_main", my_main);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.v(save_tag, "will retrive");
        Log.v(save_tag, "fragment: " + String.valueOf(savedInstanceState.getInt("Pager_Current")));
        Log.v(save_tag, "selected id: " + savedInstanceState.getInt("Selected_match"));
        current_fragment = savedInstanceState.getInt("Pager_Current");
        selected_match_id = savedInstanceState.getInt("Selected_match");
        my_main = (PagerFragment) getSupportFragmentManager().getFragment(savedInstanceState, "my_main");
        super.onRestoreInstanceState(savedInstanceState);
    }
}
