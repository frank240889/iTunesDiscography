package mx.dev.francoandroidev.itunesdiscography.List;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mx.dev.francoandroidev.itunesdiscography.R;
import mx.dev.francoandroidev.itunesdiscography.models.Category;
import mx.dev.francoandroidev.itunesdiscography.models.Model;


/**
 * Created by franco on 13/09/17.
 */

public class Adapter extends ArrayAdapter<Model> {
    private Context context;
    private int modelType;
    private List<Model> list;
    public Adapter(@NonNull Context context, int modelType, ArrayList list) {
        super(context, 0);
        this.context = context;
        this.modelType = modelType;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertedView, ViewGroup parent){

        Category category =(Category) list.get(position);

        ViewHolder viewHolder;
        if (convertedView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertedView = layoutInflater.inflate(R.layout.category_item, null);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) convertedView.findViewById(R.id.cover);
            viewHolder.genre = (TextView) convertedView.findViewById(R.id.genre);
            viewHolder.idGenre = (TextView) convertedView.findViewById(R.id.idGenre);
            convertedView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertedView.getTag();
        }

        viewHolder.genre.setText( category.getGenre() );
        viewHolder.idGenre.setTag(  category.getId() );

        return convertedView;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    //This class allows us to cache thhe views from xml view,
    //improving performance of list, because "findViewById" only
    //it's called the number of elements in list visible oto user
     static class ViewHolder{
        public ImageView imageView;
        public TextView genre;
        public TextView idGenre;
    }
}
