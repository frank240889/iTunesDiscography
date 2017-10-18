package mx.dev.francoandroidev.itunesdiscography;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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
import mx.dev.francoandroidev.itunesdiscography.models.Artist;
import mx.dev.francoandroidev.itunesdiscography.models.Category;
import mx.dev.francoandroidev.itunesdiscography.models.Model;

public class MainActivity extends AppCompatActivity{
    private static String TAG = MainActivity.class.getName();

    private Toolbar toolbar;

    private ProgressBar progressBar;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private FragmentList fragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setup toolbar for showing logout button
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progressBar = (ProgressBar) findViewById(R.id.progressLoadingBar);
        fragmentList = new FragmentList();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.REQUEST_TYPE, Constants.REQUEST_CATEGORY);
        fragmentList.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.rootView, fragmentList).commit();
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
                                MainActivity.this.finish();
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

}
