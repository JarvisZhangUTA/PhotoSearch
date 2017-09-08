package com.jarviszhang.photosearch;

import android.app.DownloadManager;
import android.app.Fragment;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.reginald.swiperefresh.CustomSwipeRefreshLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jarvis on 9/7/17.
 */

public class GalleryFragment extends Fragment{
    final static String TAG = GalleryActivity.class.getSimpleName();
    final static int NUM_COL = 3;
    final static int IMG_PER_PAGE = 100;

    private RequestQueue queue;

    private GridLayoutManager layoutManager;

    private SearchView searchView;
    private RecyclerView recyclerView;
    private CustomSwipeRefreshLayout refreshLayout;

    private GalleryAdapter adapter;

    private boolean loading = false;
    private boolean hasMore = true;

    @Override
    public void onCreate(Bundle instance){
        super.onCreate(instance);
        //是否有Menu
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery,container,false);
        queue = Volley.newRequestQueue(getActivity().getApplicationContext());

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);//固定的ViewSize
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                int totalItem = adapter.getItemCount();
                //最后一个可见item
                int lastPos = layoutManager.findLastVisibleItemPosition();
                if(hasMore && !loading && lastPos != totalItem - 1 ){
                    loadMore();
                }
            }
        });

        //网格布局
        layoutManager = new GridLayoutManager(getActivity(),NUM_COL);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new GalleryAdapter(getContext(),new ArrayList<GalleryItem>());
        recyclerView.setAdapter(adapter);

        refreshLayout = (CustomSwipeRefreshLayout) view.findViewById(R.id.swipelayout);
        refreshLayout.setOnRefreshListener(
                new CustomSwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        refresh();
                    }
                }
        );
        return view;
    }

    public void refresh(){
        adapter.reset();
        loadMore();
    }

    public void loadMore(){
        loading = true;

        final int page = layoutManager.getItemCount() / IMG_PER_PAGE;
        String query = PreferenceManager.getDefaultSharedPreferences(getActivity())
                .getString(UrlManager.PREF_SEARCH_QUERY,null);

        String url = UrlManager.getItemUrl(query,page);
        JsonObjectRequest request = new JsonObjectRequest(url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response){
                        List<GalleryItem> list = new ArrayList<>();

                        try{
                            JSONObject photos = response.getJSONObject("photos");
                            if(photos.getInt("pages") == page)
                                hasMore = false;

                            JSONArray arr = photos.getJSONArray("photo");
                            for(int i = 0; i < arr.length(); i++){
                                JSONObject obj = arr.getJSONObject(i);
                                GalleryItem item = new GalleryItem();
                                item.setId(obj.getString("id"));
                                item.setFarm(obj.getString("farm"));
                                item.setSecret(obj.getString("secret"));
                                item.setServer(obj.getString("server"));

                                list.add(item);
                            }
                        }catch (Exception e){

                        }

                        adapter.addAll(list);
                        adapter.notifyDataSetChanged();

                        loading = false;
                        refreshLayout.refreshComplete();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(),"Loading Error",Toast.LENGTH_SHORT)
                                .show();
                    }
                });

        queue.add(request);
    }

    private void stopLoading(){
        try {
            loading = false;
            refreshLayout.refreshComplete();
            queue.cancelAll(TAG);
        }catch (Exception e){

        }
    }

    @Override
    public void onStop(){
        super.onStop();
        stopLoading();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);

        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        SearchManager manager = (SearchManager) getActivity()
                .getSystemService(Context.SEARCH_SERVICE);
        ComponentName name = getActivity().getComponentName();
        SearchableInfo searchableInfo = manager.getSearchableInfo(name);

        searchView.setSearchableInfo(searchableInfo);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        boolean handled = true;

        switch (item.getItemId()){
            case R.id.action_search:
                getActivity().onSearchRequested();
                break;
            case R.id.action_top:
                if(recyclerView != null)
                    recyclerView.smoothScrollToPosition(0);
                break;
            case R.id.action_clear:
                if(searchView != null){
                    searchView.setQuery("", false);
                    searchView.setIconified(false);
                }

                PreferenceManager.getDefaultSharedPreferences(getActivity())
                        .edit()
                        .putString(UrlManager.PREF_SEARCH_QUERY, null)
                        .apply();

                refresh();
                break;
            default:
                handled = false;
                break;
        }

        return handled;
    }
}
