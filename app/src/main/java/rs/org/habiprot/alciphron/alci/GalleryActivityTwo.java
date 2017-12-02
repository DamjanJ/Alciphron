package rs.org.habiprot.alciphron.alci;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Damjan on 23-Nov-17.
 */



public class GalleryActivityTwo extends AppCompatActivity {

    private static int RESULT_LOAD_IMAGE = 1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadgallerytwo);


        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, RESULT_LOAD_IMAGE);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            ImageView imageView = (ImageView) findViewById(R.id.imageView5);
            imageView.setImageURI(selectedImage);


            File f = new File(picturePath);

            long lastmodified = f.lastModified();
            Date date = new Date();
            date.setTime(lastmodified);
            SimpleDateFormat postFormater = new SimpleDateFormat("dd.MM.yyyy.' 'HH:mm");

            String dateInt = postFormater.format(date);



            TextView textView = (TextView) findViewById(R.id.textView);
            textView.setText(dateInt);
            //textView.setText(picturePath.substring(picturePath.lastIndexOf("/" )+1,picturePath.lastIndexOf(".")));









        }

        else {

            Toast.makeText(this, "Niste Odabrali sliku!",
                    Toast.LENGTH_LONG).show();
            //Intent intent2 = new Intent(getApplicationContext(),MainActivity.class);
            this.finish();
           // startActivity(intent2);
        }


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}

