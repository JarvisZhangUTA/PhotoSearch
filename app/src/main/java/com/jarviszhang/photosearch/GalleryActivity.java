package com.jarviszhang.photosearch;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * Created by Jarvis on 2017/9/7 0007.
 */

public class GalleryActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
    }

    @Override
    protected void onNewIntent(Intent intent){
        //再次进入时会调用这个而不是onCreate,复用,缩短加载时间
        //处理activity和fragment之间的交互
        if(intent.getAction().equals(Intent.ACTION_SEARCH)){
            String query = intent.getStringExtra(SearchManager.QUERY);
            PreferenceManager.getDefaultSharedPreferences(this)
                    .edit()
                    .putString(UrlManager.PREF_SEARCH_QUERY,query)
                    .apply();
            FragmentManager fm = getFragmentManager();
            Fragment fragment = fm.findFragmentById(R.id.fragment_gallery);
            if(fragment != null)
                ((GalleryFragment)fragment).refresh();
        }
    }
}
