package ru.bk.klim9.p63001httpclient;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends Activity {

    private static final String IMAGE_URL_BASE = "http://covers.openlibrary.org/b/id/";
    String mImageURL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Enable the "Up" button for more navigation options
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // Access the imageview from XML
        ImageView imageView = (ImageView) findViewById(R.id.img_cover);

        // unpack the coverID from its trip inside your Intent
        String coverID = this.getIntent().getExtras().getString("coverID");

        // See if there is a valid coverID
        if (coverID.length() > 0) {
            // Use the ID to construct an image URL
            mImageURL = IMAGE_URL_BASE + coverID + "-L.jpg";

            // Use Picasso to load the image
            Picasso.with(this).load(mImageURL).placeholder(R.drawable.img_books_loading).
                    into(imageView);
        }
    }
}