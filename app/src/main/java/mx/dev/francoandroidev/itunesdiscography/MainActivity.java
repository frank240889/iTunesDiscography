package mx.dev.francoandroidev.itunesdiscography;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import mx.dev.francoandroidev.itunesdiscography.List.Adapter;
import mx.dev.francoandroidev.itunesdiscography.Utilities.JsonParser;
import mx.dev.francoandroidev.itunesdiscography.models.Category;
import mx.dev.francoandroidev.itunesdiscography.models.Model;

public class MainActivity extends AppCompatActivity{
    private static String TAG = MainActivity.class.getName();

    public static final int REQUEST_CATEGORY = 10;
    public static final int REQUEST_ARTIST = 11;
    public static final int REQUEST_DISCOGRAPHY = 12;


    private Toolbar toolbar;
    private ArrayList<Model> categoryList = new ArrayList<>();
    private ListView categoryListView;
    private Adapter categoryAdapter;

    private ArrayList<Model> artistList = new ArrayList<>();
    private ListView artistListView;

    private ArrayList<Model> discographyList = new ArrayList<>();
    private ListView discographyListView;

    private ProgressBar progressBar;

    private AsyncLoadData asyncLoadData;
    //this counter will be used item count of lazy loading
    private int counter = 0;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setup toolbar for showing logout button
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progressBar = (ProgressBar) findViewById(R.id.progressLoadingBar);

        //Request categories
        asyncLoadData = new AsyncLoadData(REQUEST_CATEGORY);
        asyncLoadData.execute();


        //create genre list
        categoryListView = (ListView) findViewById(R.id.mainList);
        categoryAdapter = new Adapter(getApplicationContext(), Model.TYPE_ADAPTER_CATEGORY, categoryList);
        categoryListView.setAdapter(categoryAdapter);

        categoryListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        && (categoryListView.getLastVisiblePosition() - categoryListView.getHeaderViewsCount() -
                        categoryListView.getFooterViewsCount()) >= (categoryListView.getCount() - 1)) {

                        //this listener was implemented to handle the lazy swipe up
                        Log.d(TAG,"end list");


                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        //Handle click on every item in listview
        categoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                long _id = (long)view.findViewById(R.id.idGenre).getTag();
                Toast.makeText(MainActivity.this, "Not implemented yet =(", Toast.LENGTH_SHORT).show();
                Log.d("id",_id+"");
             }
        });


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);

        //Ask for sign out
        MenuItem signOutItem = menu.findItem(R.id.signOut);
        signOutItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(getString(R.string.sign_out)).setMessage(getString(R.string.sign_out_dialog_description))
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //save logged out status and request login in next time
                                sharedPreferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
                                editor = sharedPreferences.edit();
                                editor.putBoolean("loggedIn",false);
                                editor.apply();
                                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                                startActivity(intent);
                            }
                        });
                AlertDialog dialog =  builder.create();
                dialog.setCancelable(true);
                dialog.show();
                return false;
            }
        });

        return true;
    }

private class AsyncLoadData extends AsyncTask<Void,Void,Void> {
    private int requestType;

    AsyncLoadData(int requesType) {
        //request type for reuse code when request categories, artist and discographies
        this.requestType = requesType;
    }


    @Override
    protected void onPreExecute() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected Void doInBackground(Void... params) {
        if(requestType == REQUEST_CATEGORY) {
            JSONObject result = JsonParser.getJSON(MainActivity.REQUEST_CATEGORY);

            try {
                //This first json requires to get its iterator
                Iterator iterator = result.keys();
                while (iterator.hasNext()) {
                    String key = iterator.next().toString();
                    String name = ((JSONObject) result.get(key)).getString("name");
                    long id = Long.parseLong(((JSONObject) result.get(key)).getString("id"));

                    final Category categoryObject = new Category(name, "", id);
                    categoryList.add(categoryObject);
                    //adapter can't be modified inside another thread
                    //different than main thread
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            categoryAdapter.add(categoryObject);
                            categoryAdapter.notifyDataSetChanged();
                        }
                    });

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //For request artist
        else if (requestType == REQUEST_ARTIST){

        }

        //For request discograṕhies
        else {

        }
        return null;
    }

    @Override
    protected void onPostExecute(Void voids){
        progressBar.setVisibility(View.GONE);
        asyncLoadData = null;
        Log.d(TAG,"size " + categoryAdapter.getCount() + " - " + categoryList.size());
    }

    @Override
    protected void onCancelled(Void voids){
        progressBar.setVisibility(View.GONE);
        asyncLoadData = null;
    }

}

}
