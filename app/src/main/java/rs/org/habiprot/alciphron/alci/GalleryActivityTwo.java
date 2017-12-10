package rs.org.habiprot.alciphron.alci;
// com.bbn.openmap.proj.coords; MGRS koordinate se ovde konvertuju

import android.Manifest;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
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

import org.w3c.dom.Text;

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


public class GalleryActivityTwo extends AppCompatActivity implements LocationListener{

    private static int RESULT_LOAD_IMAGE = 1;
    private LocationListener mlocationListener;
    private LocationManager locationManager;
    private FloatingActionButton fab;
    private TextView tv;
    private TextView tv2;
    private Location mlocation;
    private TextView tv6;
    //private TextView tv7;

    private int Zone = 0;
    private char Letter ;
    private double Easting = 0;
    private double Northing = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadgallerytwo);
        // getWindow().setBackgroundDrawableResource(R.drawable.smallbackgroundtexture) ;



        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, RESULT_LOAD_IMAGE);


        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mlocation = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        tv2 = (TextView) findViewById(R.id.textView2);

        if (mlocation != null) {
            tv2.setText(demo(mlocation.getLatitude(), mlocation.getLongitude()));
        }
        else {
            tv2.setText("Current Location: No Data");
        }

       // tv.setText(demo(mlocation.getLatitude(),mlocation.getLongitude()));




        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);



        mlocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                // Backup location
                if (location != null) {
                    tv2.setText(demo(mlocation.getLatitude(), mlocation.getLongitude()));
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


        tv6 = (TextView) findViewById(R.id.textView6);

        MyLocation myLocation = new MyLocation();



        rs.org.habiprot.alciphron.alci.MyLocation.LocationResult locationResult = new rs.org.habiprot.alciphron.alci.MyLocation.LocationResult(){
            @Override
            public void gotLocation(Location location){
                //Got the location!
                if (location != null) {
                    tv6.setText("MyLocation: " + location.getLatitude() + " " + location.getLongitude());
                }
                else {
                    tv6.setText("Nepoznata Lokacija...");
                }
            }
        };


        if (locationResult!=null) {
            myLocation.getLocation(this, locationResult);
        }




        // Acquire a reference to the system Location Manager
        // Define a listener that responds to location updates
        // Register the listener with the Location Manager to receive location updates - TEST

        /*
        tv7 = (TextView) findViewById(R.id.textView7);
         LocationManager MylocationManager2 = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
         MylocationManager2.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, MylocationListener2);

         LocationListener MylocationListener2 = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                tv7.setText("Network: " + location.getLatitude() + "\n" + location.getLongitude());
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };
        */

/*



        // getLocationButton is the name of your button.  Not the best name, I know.
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // instantiate the location manager, note you will need to request permissions in your manifest
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                // get the last know location from your location manager.
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                // now get the lat/lon from the location and do something with it.

                Log.i("duzina "+location.getLatitude(), "sirina "  + location.getLongitude());
            }
        });
*/
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



            tv = (TextView) findViewById(R.id.textView);
            tv.setText(dateInt);
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


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 10:
                configure_button();
                break;
            default:
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

    // DUMMY PODATCI ZA


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
    public void onLocationChanged(Location location) {
        if (location != null) {
            tv6.setText("BIG LOC LISTENER " + location.getLatitude());
        }
        else {
            tv6.setText("Big Loc Listener Warning!");
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

    }
}

