package mx.dev.francoandroidev.itunesdiscography;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = SplashActivity.class.getName();

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get the status of login
        sharedPreferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("loggedIn",false);

        //logged in pass directly to list
        if(isLoggedIn){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        //no logged in, request it
        else {
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
        }

        finish();
    }
}
