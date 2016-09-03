package doubledouble.savetheheartattack;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Message;
import android.os.MessageQueue;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;
import java.util.Locale;

public class Main extends AppCompatActivity {
    TextView gpsText;
    TextView ResultText;
    ImageButton convert;
    Location currentLocation;

    InputStream in;
    DataInputStream din = null;
    OutputStream out;
    DataOutputStream dout = null;

    private void sendSMS(String phoneNumber, String message) {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), 0);

        //---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver() {
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "119 신고했당!", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //gpsText = (TextView)findViewById(R.id.gpsInfoText);
        //ResultText = (TextView)findViewById(R.id.GeocodingResultText);
        convert = (ImageButton)findViewById(R.id.iconvert);

        Intent intent = new Intent("doubledouble.backgroundService");
        startService(intent);

        final LocationManager locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.NO_REQUIREMENT);
        criteria.setPowerRequirement(Criteria.NO_REQUIREMENT);
        final String provider = locationManager.getBestProvider(criteria, true);
        try {
            Location mLoc = locationManager.getLastKnownLocation(provider);
        }catch(SecurityException e){
            e.printStackTrace();
        }

        final LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //gpsText.setText("lati : " + location.getLatitude() + ", longi : " + location.getLongitude());
                currentLocation = location;
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Location mLoc = locationManager.getLastKnownLocation(provider);
                //gpsText.setText("lati : " + mLoc.getLatitude() + ", longi : " + mLoc.getLongitude());
            }

            @Override
            public void onProviderEnabled(String provider) {
                Location mLoc = locationManager.getLastKnownLocation(provider);
               // gpsText.setText("lati : " + mLoc.getLatitude() + ", longi : " + mLoc.getLongitude());
            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);   //caution

        convert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Location mLoc = locationManager.getLastKnownLocation(provider); //즉시 위도경도 받아옴
                currentLocation = mLoc;
                if(currentLocation != null){
                    String result = "";
                    String toSend = result + findAddress(37.375307, 126.6658447);
                    //Intent toBackground = new Intent(getApplicationContext(), BackgroundService.class);
                    //toBackground.putExtra("latitude", 37.375307);
                    //toBackground.putExtra("longitude", 126.6658447);
                    //(findAddress(currentLocation.getLatitude()-0.1, currentLocation.getLongitude()))
                    //ResultText.setText(toSend);
                    sendSMS("01027640415", toSend);
                }
                //else
                    //ResultText.setText("Failed.");
            }
        });


    }

    private String findAddress(double lat, double lng) {
        StringBuffer bf = new StringBuffer();
        Geocoder geocoder = new Geocoder(this, Locale.KOREA);
        String currentLocationAddress;
        List<Address> address;
        try {
            if (geocoder != null) {
                // 세번째 인수는 최대결과값인데 하나만 리턴받도록 설정했다
                address = geocoder.getFromLocation(lat, lng, 1);
                // 설정한 데이터로 주소가 리턴된 데이터가 있으면
                if (address != null && address.size() > 0) {
                    // 주소
                    currentLocationAddress = address.get(0).getAddressLine(0).toString();

                    // 전송할 주소 데이터 (위도/경도 포함 편집)
                    bf.append(currentLocationAddress).append("#");
                    bf.append(lat).append("#");
                    bf.append(lng);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bf.toString();
    }
}