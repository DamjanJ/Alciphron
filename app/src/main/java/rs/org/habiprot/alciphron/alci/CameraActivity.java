package rs.org.habiprot.alciphron.alci;

import android.Manifest;
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
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

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

    LocationManager mLocationManager;
    TextView tv5;


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


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);


        if(location != null && location.getTime() > Calendar.getInstance().getTimeInMillis() - 2 * 60 * 1000) {
            // Do something with the recent location fix
            //  otherwise wait for the update below
            tv5 = (TextView) findViewById(R.id.textView5);
            tv5.setText("Lokacija je"+location.getLatitude() + location.getLongitude());
        }
        else {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) this);
        }



    }

    public void onLocationChanged(Location location) {
        if (location != null) {
            Log.v("Location Changed", location.getLatitude() + " and " + location.getLongitude());
            tv5.setText("Nova Lokacija je: "+location.getLatitude() + location.getLongitude());
            mLocationManager.removeUpdates((LocationListener) this);
        }
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



            Toast.makeText(this, "Niste izvršili slikanje!", Toast.LENGTH_LONG).show();


            this.finish();

        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }


}