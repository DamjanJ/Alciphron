package rs.org.habiprot.alciphron.alci;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.R.attr.startYear;

/**
 * Created by Damjan on 25-Nov-17.
 */

public class JustTextActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    LocationManager mLocationManager;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    TextView tv2;
    TextView tv3;
    TextView tv7;
    AutoCompleteTextView actv;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;
    ArrayList<String> Vrste;
    EditText et2;
    TextView tv6;
    private LocationListener mlocationListener;
    private LocationManager locationManager;
    private FloatingActionButton fab;

    private Location mlocation;
    private int Zone = 0;
    private char Letter ;
    private double Easting = 0;
    private double Northing = 0;



    String[] Vrste2={
            "Tetrix subulata", "Aiolopus strepens" , "Arcyptera fusca", "Pholidoptera griseoaptera","Oedipoda germanica","Poecilimon schmidtii","Decticus verrucivorus","Tettigonia viridissima","Isophya modestior","Eupholidoptera schmidti",
            "Pezotettix giornae","Gryllus campestris","Oedipoda caerulescens","Pachytrachis gracilis","Odontopodisma schmidtii","Omocestus haemorrhoidalis","Calliptamus italicus","Phaneroptera nana",
            "Gryllotalpa gryllotalpa","Poecilimon thoracicus","Pholidoptera aptera","Polysarcus denticauda","Acrida ungarica","Aiolopus thalassinus","Chorthippus brunneus"
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_uploadtemplate);

        final Calendar myCalendar = Calendar.getInstance();

        tv2= (TextView) findViewById(R.id.textView2);
        tv3= (TextView) findViewById(R.id.textView3);
        tv7= (TextView) findViewById(R.id.textView7);
        tv6= (TextView) findViewById(R.id.textView9);
        actv = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        et2 = (EditText) findViewById(R.id.editText2);



        Date date = new Date();
        SimpleDateFormat postFormater = new SimpleDateFormat("dd.MM.yyyy.");



        String dateInt = postFormater.format(date);
        tv2.setText("Datum:\n"+dateInt);
        tv3.setText("Vreme:\n"+Calendar.getInstance().get(Calendar.HOUR)+":"+Calendar.getInstance().get(Calendar.MINUTE));

         datePickerDialog = new DatePickerDialog(
                this, datePickerListener, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        timePickerDialog = new TimePickerDialog(this, timeSetListener, Calendar.getInstance().get(Calendar.HOUR), Calendar.getInstance().get(Calendar.MINUTE), true);



        ((TextView) findViewById(R.id.textView2))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        datePickerDialog.show();
                        tv7.setVisibility(View.INVISIBLE);
                    }

                });

        ((TextView) findViewById(R.id.textView3)).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick (View v) {
                timePickerDialog.show();
                tv7.setVisibility(View.INVISIBLE);
            }

        });



        // end of pickers


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_dropdown_item_1line, Vrste2);
        actv.setAdapter(adapter);

        actv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                actv.setFocusable(true);
            }

        });

        et2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                et2.setFocusable(true);
            }

        });





        actv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                View view = getCurrentFocus();
                if (view != null) {
                    InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);


                }
            }

        });


        actv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                }
            }
        });

        et2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                }
            }
        });


        // Location from Gallery Activity


        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mlocation = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);


        if (mlocation != null) {
            tv6.setText("Trenutna lokacija je:\n" +demo(mlocation.getLatitude(), mlocation.getLongitude()));
        }
        else {
            tv6.setText("Trenutna Lokacija:\nTraži se...");
        }

        // tv.setText(demo(mlocation.getLatitude(),mlocation.getLongitude()));




        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);



        mlocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                // Backup location
                if (location != null) {
                    tv6.setText("Trenutna lokacija je:\n" +demo(mlocation.getLatitude(), mlocation.getLongitude()));
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


    }





    private DatePickerDialog.OnDateSetListener datePickerListener
            = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            // set selected date into textview
            tv2.setText(new StringBuilder().append("Datum:\n").append(day)
                    .append(".").append(month + 1).append(".").append(year)
                    .append("."));

            // set selected date into datepicker also
            //datePickerDialog.init(year, month, day, null);


        }
    };


    private TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {

        public void onTimeSet (TimePicker view, int selectedHour, int selectedMinute) {

            hour = selectedHour;
            minute = selectedMinute;
            tv3.setText("Vreme:\n"+hour+":"+minute);
        }


    };


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




    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

    }


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



}

