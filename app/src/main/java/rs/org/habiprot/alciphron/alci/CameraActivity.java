package rs.org.habiprot.alciphron.alci;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.security.AccessController.getContext;

/**
 * Created by Damjan on 26-Nov-17.
 */


public class CameraActivity extends AppCompatActivity {



    ImageView imageView;
    int CAMERA_REQUEST = 1;
    private static final String AUTHORITY =
            BuildConfig.APPLICATION_ID + ".provider";

    String mCurrentPhotoPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        imageView = (ImageView) findViewById(R.id.cameraImage);





        Intent cameraIntent = new Intent();
        cameraIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);



                //Uri outputUri=FileProvider.getUriForFile(getActivity().getApplicationContext(), AUTHORITY, imageFile);
                //Uri pictureUri = Uri.fromFile(imageFile);
              //  cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);


                startActivityForResult(cameraIntent, CAMERA_REQUEST);






    }


    private String getPictureName() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timestamp = sdf.format(new Date());
        return "Alci" + timestamp + ".jpg";

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {

            File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            String pictureName = getPictureName();
            File imageFile = new File(pictureDirectory, pictureName);

            // String authorities = getApplicationContext().getPackageName() + ".fileprovider";
            Uri imageUri = FileProvider.getUriForFile(CameraActivity.this, "rs.org.fileprovider", imageFile);

            mCurrentPhotoPath = imageFile.getAbsolutePath();


            Log.i("OBAVESTENJE", mCurrentPhotoPath);
            Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");

            this.sendBroadcast(mediaScanIntent);

            ImageView cameraImage = (ImageView) findViewById(R.id.cameraImage);
            cameraImage.setImageURI(data.getData());



            Date date = new Date();
            SimpleDateFormat postFormater = new SimpleDateFormat("dd.MM.yyyy.' 'HH:mm");

            String dateInt = postFormater.format(date);



            TextView textView = (TextView) findViewById(R.id.textView);
            textView.setText(dateInt);






        }

        else {

            this.finish();

        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }


}