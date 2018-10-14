package com.jge.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {
    private TextView mDescriptionTextView;
    private ImageView mImageUrlImageView;
    private String overview;
    private String imgPath;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        init();
        overview = getIntent().getExtras().getString("overview");
        imgPath = getIntent().getExtras().getString("imgUrl");
        title = getIntent().getExtras().getString("title");
        populateUI();
    }

    private void init(){
        mDescriptionTextView = findViewById(R.id.overview_tv);
        mImageUrlImageView = findViewById(R.id.poster_iv);
    }

    private void populateUI(){
        setTitle(title);
        mDescriptionTextView.setText(overview);
        Picasso.with(this).load(NetworkUtils.buildImageUrl(imgPath))
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(mImageUrlImageView);
    }
}
