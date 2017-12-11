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
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
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

    private LocationManager mLocationManager;
    private TextView tv5;
    private Location mlocation;
    private FloatingActionButton fab;
    private LocationListener mlocationListener;
    private LocationManager locationManager;

    private int Zone = 0;
    private char Letter ;
    private double Easting = 0;
    private double Northing = 0;


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
        fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);


        Intent cameraIntent = new Intent();
        cameraIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        tv5 = (TextView) findViewById(R.id.textView5);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.INTERNET}
                        ,10);
            }
            return;
        }


        //Uri outputUri=FileProvider.getUriForFile(getActivity().getApplicationContext(), AUTHORITY, imageFile);
        //Uri pictureUri = Uri.fromFile(imageFile);
        //  cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);


        startActivityForResult(cameraIntent, CAMERA_REQUEST);



        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mlocation = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        if (mlocation != null) {
            tv5.setText("Trenutna lokacija je:\n" +demo(mlocation.getLatitude(), mlocation.getLongitude()));
        }
        else {
            tv5.setText("Trenutna Lokacija:\nTraži se...");
        }

        // tv.setText(demo(mlocation.getLatitude(),mlocation.getLongitude()));




        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);



        mlocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                // Backup location
                if (location != null) {
                    tv5.setText("Trenutna lokacija je:\n" +demo(mlocation.getLatitude(), mlocation.getLongitude()));
                    locationManager.removeUpdates(this);
                }


            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

                Intent mi = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(mi);
            }
        };

        locationManager.requestLocationUpdates("gps", 5000, 0, mlocationListener);
        configure_button();




        configure_button();




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
            textView.setText("Datum i Vreme:\n"+dateInt);






        }

        else {



            Toast.makeText(this, "Niste izvršili slikanje!", Toast.LENGTH_LONG).show();


            this.finish();

        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case 10:
                configure_button();
                break;
            default:
                this.finish();
                break;
        }
    }


    void configure_button(){
        // first check for permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.INTERNET}
                        ,10);
            }
            return;
        }
        // this code won't execute IF permissions are not allowed, because in the line above there is return statement.
        // Code to manually refresh location


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //noinspection MissingPermission
                locationManager.requestLocationUpdates("gps", 5000, 0, mlocationListener);
                Log.i("LISTENER", "RADIIIIIIII");


            }
        });
    }


    // to get Easting and Northing

    private String demo(double Lat, double Lon)
    {
        Zone= (int) Math.floor(Lon/6+31);

        System.out.println("Zone"+Zone);
        if (Lat<-72)
            Letter='C';
        else if (Lat<-64)
            Letter='D';
        else if (Lat<-56)
            Letter='E';
        else if (Lat<-48)
            Letter='F';
        else if (Lat<-40)
            Letter='G';
        else if (Lat<-32)
            Letter='H';
        else if (Lat<-24)
            Letter='J';
        else if (Lat<-16)
            Letter='K';
        else if (Lat<-8)
            Letter='L';
        else if (Lat<0)
            Letter='M';
        else if (Lat<8)
            Letter='N';
        else if (Lat<16)
            Letter='P';
        else if (Lat<24)
            Letter='Q';
        else if (Lat<32)
            Letter='R';
        else if (Lat<40)
            Letter='S';
        else if (Lat<48)
            Letter='T';
        else if (Lat<56)
            Letter='U';
        else if (Lat<64)
            Letter='V';
        else if (Lat<72)
            Letter='W';
        else
            Letter='X';
        Easting=0.5*Math.log((1+Math.cos(Lat*Math.PI/180)*Math.sin(Lon*Math.PI/180-(6*Zone-183)*Math.PI/180))/(1-Math.cos(Lat*Math.PI/180)*Math.sin(Lon*Math.PI/180-(6*Zone-183)*Math.PI/180)))*0.9996*6399593.62/Math.pow((1+Math.pow(0.0820944379, 2)*Math.pow(Math.cos(Lat*Math.PI/180), 2)), 0.5)*(1+ Math.pow(0.0820944379,2)/2*Math.pow((0.5*Math.log((1+Math.cos(Lat*Math.PI/180)*Math.sin(Lon*Math.PI/180-(6*Zone-183)*Math.PI/180))/(1-Math.cos(Lat*Math.PI/180)*Math.sin(Lon*Math.PI/180-(6*Zone-183)*Math.PI/180)))),2)*Math.pow(Math.cos(Lat*Math.PI/180),2)/3)+500000;
        Easting=Math.round(Easting*100)*0.01;


        Northing = (Math.atan(Math.tan(Lat*Math.PI/180)/Math.cos((Lon*Math.PI/180-(6*Zone -183)*Math.PI/180)))-Lat*Math.PI/180)*0.9996*6399593.625/Math.sqrt(1+0.006739496742*Math.pow(Math.cos(Lat*Math.PI/180),2))*(1+0.006739496742/2*Math.pow(0.5*Math.log((1+Math.cos(Lat*Math.PI/180)*Math.sin((Lon*Math.PI/180-(6*Zone -183)*Math.PI/180)))/(1-Math.cos(Lat*Math.PI/180)*Math.sin((Lon*Math.PI/180-(6*Zone -183)*Math.PI/180)))),2)*Math.pow(Math.cos(Lat*Math.PI/180),2))+0.9996*6399593.625*(Lat*Math.PI/180-0.005054622556*(Lat*Math.PI/180+Math.sin(2*Lat*Math.PI/180)/2)+4.258201531e-05*(3*(Lat*Math.PI/180+Math.sin(2*Lat*Math.PI/180)/2)+Math.sin(2*Lat*Math.PI/180)*Math.pow(Math.cos(Lat*Math.PI/180),2))/4-1.674057895e-07*(5*(3*(Lat*Math.PI/180+Math.sin(2*Lat*Math.PI/180)/2)+Math.sin(2*Lat*Math.PI/180)*Math.pow(Math.cos(Lat*Math.PI/180),2))/4+Math.sin(2*Lat*Math.PI/180)*Math.pow(Math.cos(Lat*Math.PI/180),2)*Math.pow(Math.cos(Lat*Math.PI/180),2))/3);
        if (Letter<'M')
            Northing = Northing + 10000000;


        Northing=Math.round(Northing*100)*0.01;

        String s = "Easting: "+Easting + "\nNorthing: "+ Northing;

        return s;




    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }


}