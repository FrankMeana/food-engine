package com.frankmeana.foodengine;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.InputStream;
import java.util.Scanner;

public class SearchActivity extends AppCompatActivity
{
    /* ----- Declare fields ----- */
    private DatabaseHelper db; // TEST
    private EditText searchEditText;
    private ImageButton searchImageButton;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        /* ----- Initialize fields ----- */
        db = new DatabaseHelper(this);
        // if we can find a way to only read csv file when app is first installed and everytime
        // the database is upgraded it would be ideal, but for now we read it every time the app
        // opens
        new FileProcessor().processRecords();
        searchEditText = (EditText) findViewById(R.id.searchEditText);
        searchImageButton = (ImageButton) findViewById(R.id.searchImageButton);

        /* ----- Set on-click listeners ----- */
        searchImageButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // to change an image by name rather than by id
                //int resId = getResources().getIdentifier("temp_logo", "drawable", getPackageName());
                //searchImageButton.setImageResource(resId);


                // build a string out of the query
                // (used for the text displayed in the ResultSetActivity)
                StringBuilder sb = new StringBuilder();

                // BEGIN QUERY
                Cursor resultSet = db.select(searchEditText.getText().toString());


                int maxSize = resultSet.getCount();
                Log.e("MAX-SIZE", String.valueOf(maxSize));

                int counter = 0; // NEW
                // used for the images displayed in the ResultSetActivity
                String[] imageNames = new String[maxSize]; // NEW

                // if user enters nothing return and display a toast about entering something
                // (Note: added .trim() to treat multiple spaces as an empty string as well)
                if (searchEditText.getText().toString().trim().equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Please search for a specific item", Toast.LENGTH_SHORT).show();
                    return;
                }
                // if query returns 0 results
                if (maxSize == 0)
                {
                    // if max size is 0 and you try to store an element program crashes so make max size 1
                    imageNames = new String[1]; // TEMPORARY UNTIL BETTER LOGIC OCCURS
                    imageNames[counter] = "x";
                    // PERHAPS use a reference to the productImageView from the products.xml in order to
                    // make the size of the x image smaller because it looks too big
                    sb.append("Query Returned 0 results");
                }
                else
                {
                    while (resultSet.moveToNext())
                    {

                        // Assign the current imageNames element the value of the product number
                        imageNames[counter] = resultSet.getString(0).toLowerCase(); // NEW

                        // the last %n gives it a bit of extra space in the bottom of each item
                        sb.append(String.format("%s%n%nCountry: %s%nBrand: %s%nStore: %s%n",
                                resultSet.getString(1), resultSet.getString(2),
                                resultSet.getString(3).equals("null")? "N/A": resultSet.getString(3),
                                resultSet.getString(4).equals("null") ? "N/A": resultSet.getString(4)));

                        if (resultSet.getCount() != counter)
                            sb.append("!");

                        counter++;

                    }
                }

                // END QUERY

                // split into an array based on commas
                String[] results = sb.toString().split("!");


                // create intent to switch activities
                Intent intent = new Intent(getApplicationContext(), ResultSetActivity.class);
                // send the result object to the ResultSetActivity
                intent.putExtra("Query", results);

                // PUT THE imageNames array as an extra to send to next activity // NEW
                intent.putExtra("Images", imageNames);

                // go to the ResultSetActivity
                startActivity(intent);
            }
        });

    }

    /**
     * Inner class FileProcessor(we will only call an instance of this class from SearchActivity
     * (the enclosing class) when needed
     */
    private class FileProcessor
    {
        //DatabaseHelper db = new DatabaseHelper(SearchActivity.this);

        private void processRecords()
        {
            // This is the way to connect the file to an inputstream in android
            InputStream inputStream = getResources().openRawResource(R.raw.products);

            Scanner scanner = new Scanner(inputStream);


            // skipping first row by reading it before loop and displaying it as column names
            String[] tableRow = scanner.nextLine().split(",");

            //Log.e("COLUMN NAMES", Arrays.toString(tableRow));
            //Log.e("COLUMN NAMES SIZE", String.valueOf(tableRow.length));


            // --- Truncate table (delete all rows and reset auto increment --
            // this line of code will be useful because for now we are forced to read file
            // everytime we open app, this way we do not have duplicate data.
            db.resetTable();

            while (scanner.hasNextLine())
            {
                tableRow = scanner.nextLine().split(",");
                //Log.e("COLUMN VALUES", Arrays.toString(tableRow));
                //Log.e("COLUMN VALUES SIZE", String.valueOf(tableRow.length));

                /*
                 * Possible Change:
                 * On each iteration a new Product() can be created and call the setter to set
                 * its fields to the elements of the String array, example
                 * product = new Product();
                 * product.setNumber(tableRow[0]);
                 * product.setBusinessName(tableRow[1]);
                 * ...
                 * db.insertData(product);  // because the new insertData method would expect a
                 * Product object instead
                 *
                 */

                // insert data
                if (db.insertData(tableRow))
                {
                    //Log.e("Insert", "SUCCESSFUL INSERT AT " + tableRow[0]);
                }
                else
                {
                    //Log.e("Insert", "UNSUCCESSFUL INSERT");
                }

            }
        }
    }


}
