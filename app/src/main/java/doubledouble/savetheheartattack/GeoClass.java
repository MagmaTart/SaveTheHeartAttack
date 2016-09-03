package doubledouble.savetheheartattack;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by leesm10413 on 2016-09-03.
 */
public class GeoClass extends AppCompatActivity {
    LocationManager locationManager;
    Criteria criteria;
    String provider;
    Location mLoc;
    public void GeoClass(Activity activity){
        locationManager = (LocationManager)activity.getSystemService(Context.LOCATION_SERVICE);
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.NO_REQUIREMENT);
        criteria.setPowerRequirement(Criteria.NO_REQUIREMENT);
        provider = locationManager.getBestProvider(criteria, true);
        try {
            mLoc = locationManager.getLastKnownLocation(provider);
        }catch(SecurityException e){
            e.printStackTrace();
        }
    }

}
