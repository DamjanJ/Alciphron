package rs.org.habiprot.alciphron.alci;

import android.content.Intent;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    Button button;
    MenuItem item;
    private Bitmap mImageBitmap;
    private ImageView mImageView;
    String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // toolbar

        Toolbar mToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);




        // Button Action

        button=(Button)findViewById(R.id.btnPhoto);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub



                Intent intent = new Intent(getApplicationContext(),CameraActivity.class);
                startActivity(intent);

            }
        });


        button=(Button)findViewById(R.id.btnGallery);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Intent intent2 = new Intent(getApplicationContext(),GalleryActivityTwo.class);
                startActivity(intent2);



            }
        });


        button=(Button)findViewById(R.id.btnTemplate);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Intent intent3 = new Intent(getApplicationContext(),JustTextActivity.class);
                startActivity(intent3);




            }
        });




    }




    @Override
    public boolean onCreateOptionsMenu (Menu menu) {

        MenuInflater mMenuInflater = getMenuInflater();
        mMenuInflater.inflate(R.menu.my_menu, menu);
        return true;


    }

    // Log Out Button
    @Override
    public boolean onOptionsItemSelected (MenuItem item) {

        if (item.getItemId()== R.id.mLogOut) {
            Intent i = new Intent(getApplicationContext(),LoginActivity.class);

            startActivity(i);
            this.finish();

        }


        return true;

    }








    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    }




