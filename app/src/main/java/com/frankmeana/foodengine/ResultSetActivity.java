package com.frankmeana.foodengine;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Arrays;

public class ResultSetActivity extends AppCompatActivity
{
    private ListView resultSetListView;
    private String[] images;
    private String[] results;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_set);

        /* ----- Initialize fields ----- */
        resultSetListView = (ListView) findViewById(R.id.resultSetListView);

        // retrieving extras from intent that called this activity
        Bundle bundle = getIntent().getExtras();
        // retrieving exact extra based on the key provided when calling putExtra
        results = bundle.getStringArray("Query");
        images = bundle.getStringArray("Images");


        Log.e("RESULTS", Arrays.toString(results));
        Log.e("IMAGES", Arrays.toString(images));

        if (results == null || images == null)
        {
            Log.e("ARRAY IS NULL", "OUCH");
            return;
        }

        // creating an CustomAdapter to be the middleman between this array and the ListView displayed
        CustomAdapter adapter = new CustomAdapter();
        // setting the listview to use the adapter specified
        resultSetListView.setAdapter(adapter);
    }

    private class CustomAdapter extends BaseAdapter
    {

        @Override
        public int getCount()
        {
            return results.length;
        }

        @Override
        public Object getItem(int position)
        {
            return null;
        }

        @Override
        public long getItemId(int position)
        {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            convertView = getLayoutInflater().inflate(R.layout.products, null);

            // if we don't use the convertView reference before invoking findViewById here
            // we get a null pointer exception
            ImageView productImageView = (ImageView) convertView.findViewById(R.id.productImageView);
            TextView productTextView = (TextView) convertView.findViewById(R.id.productTextView);

            //int resId = getResources().getIdentifier("temp_logo", "drawable", getPackageName());
            //searchImageButton.setImageResource(resId);

            int resourceId = getResources().getIdentifier(images[position], "drawable", getPackageName());

            // getIdentifier returns 0 if no resource was found
            if (resourceId == 0)
                productImageView.setImageResource(R.drawable.no_image);

            else
                productImageView.setImageResource(resourceId);

            productTextView.setText(results[position]);

            return convertView;
        }
    }


}
