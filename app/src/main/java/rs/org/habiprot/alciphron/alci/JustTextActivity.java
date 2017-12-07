package rs.org.habiprot.alciphron.alci;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Calendar;

/**
 * Created by Damjan on 25-Nov-17.
 */

public class JustTextActivity extends AppCompatActivity {

    LocationManager mLocationManager;
    TextView tv3;
    Location mlocation = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_uploadtemplate);

        tv3 = (TextView) findViewById(R.id.textView3);



    }





    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
