package mx.dev.francoandroidev.itunesdiscography;

import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import mx.dev.francoandroidev.itunesdiscography.List.Adapter;
import mx.dev.francoandroidev.itunesdiscography.Utilities.JsonParser;
import mx.dev.francoandroidev.itunesdiscography.models.Artist;
import mx.dev.francoandroidev.itunesdiscography.models.Category;
import mx.dev.francoandroidev.itunesdiscography.models.Model;

/**
 * Created by franco on 9/10/17.
 */

public class FragmentList extends Fragment {
    private static final String TAG = FragmentList.class.getName();

    private ArrayList<Model> artistList;
    private ListView artistListView;
    private Adapter artistAdapter;

    private ArrayList<Model> categoryList;
    private ListView categoryListView;
    private Adapter categoryAdapter;
    private int requestType= -1;
    private FragmentList fragmentList;

    private AsyncLoadData asyncLoadData;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle onSavedInstance){
        return layoutInflater.inflate(R.layout.fragment_list_model,viewGroup,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        requestType = bundle.getInt(Constants.REQUEST_TYPE);

        if (requestType == Constants.REQUEST_CATEGORY){
                //create genre list
            categoryListView = (ListView) view.findViewById(R.id.fragment_list);
            categoryList = new ArrayList<>();
            categoryAdapter = new Adapter(getContext(), Model.TYPE_ADAPTER_CATEGORY, categoryList);
            categoryListView.setAdapter(categoryAdapter);

            //Handle click on every item in listview
            categoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    long _id = (long) view.findViewById(R.id.idGenre).getTag();
                    FragmentList fragmentList = new FragmentList();
                    Bundle bundle = new Bundle();
                    bundle.putInt(Constants.REQUEST_TYPE, Constants.REQUEST_ARTIST);
                    fragmentList.setArguments(bundle);
                    getFragmentManager().beginTransaction().replace(R.id.rootView, fragmentList).addToBackStack(null).commit();
                    Log.d("id", _id + "");
                }
            });
        }
        else if (requestType == Constants.REQUEST_ARTIST){
            //create artist list
            artistListView = (ListView) view.findViewById(R.id.fragment_list);
            artistList = new ArrayList<>();
            artistAdapter = new Adapter(getContext(), Model.TYPE_ADAPTER_ARTIST, artistList);
            artistListView.setAdapter(artistAdapter);
            //Handle click on every item in listview
            artistListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    long _id = (long) view.findViewById(R.id.idGenre).getTag();
                    Log.d("id", _id + "");
                }
            });
        }

        Log.d("commit to dev branch","commit");
        asyncLoadData = new AsyncLoadData(requestType);
        asyncLoadData.execute();
    }

    private class AsyncLoadData extends AsyncTask<Void,Model,Void> {
        private int requestType;

        AsyncLoadData(int requestType) {
            //request type for reuse code when request categories, artist and discographies
            this.requestType = requestType;
        }


        @Override
        protected void onPreExecute() {
            getActivity().findViewById(R.id.progressLoadingBar).setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            if(requestType == Constants.REQUEST_CATEGORY){
                getGenres();
            }
            else if(requestType == Constants.REQUEST_ARTIST){
                getArtists();
            }
            return null;
        }

        private void getArtists(){
            JSONObject result = JsonParser.getJSON(Constants.REQUEST_CATEGORY);

            try {
                //This first json requires to get its iterator
                Iterator iterator = result.keys();
                while (iterator.hasNext()) {
                    String key = iterator.next().toString();
                    String name = ((JSONObject) result.get(key)).getString("name");
                    long id = Long.parseLong(((JSONObject) result.get(key)).getString("id"));

                    final Artist artistObject = new Artist();
                    artistObject.setArtistName(name);
                    artistObject.setId(id);
                    publishProgress(artistObject);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void getGenres(){
            JSONObject result = JsonParser.getJSON(Constants.REQUEST_CATEGORY);

            try {
                //This first json requires to get its iterator
                Iterator iterator = result.keys();
                while (iterator.hasNext()) {
                    String key = iterator.next().toString();
                    String name = ((JSONObject) result.get(key)).getString("name");
                    long id = Long.parseLong(((JSONObject) result.get(key)).getString("id"));

                    final Category categoryObject = new Category(name, "", id);
                    publishProgress(categoryObject);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onProgressUpdate(Model[] model){
            super.onProgressUpdate(model);
            if(requestType == Constants.REQUEST_CATEGORY) {
                categoryList.add(model[0]);
                categoryAdapter.add(model[0]);
                categoryAdapter.notifyDataSetChanged();
                Log.d(TAG, "size genres" + categoryAdapter.getCount() + " - " + categoryList.size());
            }
            else if(requestType == Constants.REQUEST_ARTIST) {
                artistList.add(model[0]);
                artistAdapter.add(model[0]);
                artistAdapter.notifyDataSetChanged();
                Log.d(TAG, "size artist " + artistAdapter.getCount() + " - " + artistList.size());
            }
            else {

            }
        }

        @Override
        protected void onPostExecute(Void voids){
            getActivity().findViewById(R.id.progressLoadingBar).setVisibility(View.GONE);
            asyncLoadData = null;

        }

        @Override
        protected void onCancelled(Void voids){
            getActivity().findViewById(R.id.progressLoadingBar).setVisibility(View.GONE);
            asyncLoadData = null;
        }

    }
}
