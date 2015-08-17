package ru.bk.klim9.p63001httpclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class MainActivity extends Activity implements View.OnClickListener,
        AdapterView.OnItemClickListener {

    private static final String QUERY_URL = "http://openlibrary.org/search.json?q=";
    TextView mainTextView;
    Button mainButton;
    EditText mainEditText;
    ListView mainListView;
    JSONAdapter mJSONAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Add a spinning progress bar (and make sure it's off)
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setProgressBarIndeterminateVisibility(false);

        setContentView(R.layout.activity_main);

        mainTextView = (TextView) findViewById(R.id.main_textview);
        mainButton = (Button) findViewById(R.id.main_button);
        mainButton.setOnClickListener(this);
        mainEditText = (EditText) findViewById(R.id.main_edittext);
        mainListView = (ListView) findViewById(R.id.main_listview);
        mainListView.setOnItemClickListener(this);

        // Create a JSONAdapter for the ListView
        mJSONAdapter = new JSONAdapter(this, getLayoutInflater());

        // Set the ListView to use the ArrayAdapter
        mainListView.setAdapter(mJSONAdapter);
    }

    @Override
    public void onClick(View view) {
        queryBooks(mainEditText.getText().toString());
    }

    @Override
    public void onItemClick(AdapterView parent, View view, int position, long id) {
        // Now that the user's chosen a book, grab the cover data
        JSONObject jsonObject = (JSONObject) mJSONAdapter.getItem(position);
        String coverID = jsonObject.optString("cover_i", "");

        // create an Intent to take you over to a new DetailActivity
        Intent detailIntent = new Intent(this, DetailActivity.class);

        // pack away the data about the cover
        // into your Intent before you head out
        detailIntent.putExtra("coverID", coverID);

        // TODO: add any other data you'd like as Extras

        // start the next Activity using your prepared Intent
        startActivity(detailIntent);

    }

    private void queryBooks(String searchString) {

        // Prepare your search string to be put in a URL
        // It might have reserved characters or something
        String urlString = "";
        try {
            urlString = URLEncoder.encode(searchString, "UTF-8");
        } catch (UnsupportedEncodingException e) {

            // if this fails for some reason, let the user know why
            e.printStackTrace();
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        // Create a client to perform networking
        AsyncHttpClient client = new AsyncHttpClient();
        setProgressBarIndeterminateVisibility(true);

        // Have the client get a JSONArray of data
        // and define how to respond
        client.get(QUERY_URL + urlString,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        setProgressBarIndeterminateVisibility(false);
                        // Display a "Toast" message
                        // to announce your success
                        Toast.makeText(getApplicationContext(), "Success!",
                                Toast.LENGTH_LONG).show();

                        // update the data in your custom method.
                        mJSONAdapter.updateData(jsonObject.optJSONArray("docs"));
                    }

                    @Override
                    public void onFailure(int statusCode, Throwable throwable, JSONObject error) {
                        setProgressBarIndeterminateVisibility(false);
                        // Display a "Toast" message
                        // to announce the failure
                        Toast.makeText(getApplicationContext(), "Error: " + statusCode + " " +
                                throwable.getMessage(), Toast.LENGTH_LONG).show();

                        // Log error message
                        // to help solve any problems
                        Log.e("omg android", statusCode + " " + throwable.getMessage());
                    }
                }
        );
    }
}
