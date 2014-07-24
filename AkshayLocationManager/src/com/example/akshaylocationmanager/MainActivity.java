package com.example.akshaylocationmanager;

import java.io.IOException;
import java.util.List;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	TextView tvLat;
	TextView tvLong;
	TextView tvAddress;

	LocationManager locmgr;
	LocationListener locLis;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		tvLat = (TextView) findViewById(R.id.textView1);
		tvLong = (TextView) findViewById(R.id.textView2);
		tvAddress = (TextView) findViewById(R.id.tvAddress);

		locmgr = (LocationManager) getSystemService(LOCATION_SERVICE);
		locLis = new LocationListener() {

			@Override
			public void onLocationChanged(Location loc) {
				double lati = loc.getLatitude();
				double longi = loc.getLongitude();

				tvLat.setText("Lat : " + lati);
				tvLong.setText("Long : " + longi);

				Toast.makeText(getBaseContext(),
						"Lat : " + lati + ", Long : " + longi,
						Toast.LENGTH_LONG).show();

				// ///////////////////>>>>>>>>>Geocoder<<<<<<<<<<<<<//////////////////////////////////

				String add = "";

				Geocoder gc = new Geocoder(getBaseContext());
				try {
					List<Address> adressList = gc.getFromLocation(lati, longi,
							5);

					for (Address ad : adressList) {
						for (int i = 0; i < ad.getMaxAddressLineIndex(); i++) {
							add += "\n" + ad.getAddressLine(i);
						}
						add += "\n---------------\n";
					}

					tvAddress.setText("Address : " + add);
				} catch (IOException e) {
					Toast.makeText(
							getBaseContext(),
							"Internt Connection not available .IOException "
									+ e.getMessage(), Toast.LENGTH_LONG).show();

					e.printStackTrace();
				}

				// ///////////////////////////////////////////////////////////

			}

			@Override
			public void onStatusChanged(String provider, int status, Bundle b) {

				switch (status) {
				case LocationProvider.AVAILABLE:
					Toast.makeText(getBaseContext(),
							provider + " is available", Toast.LENGTH_LONG)
							.show();

					break;
				case LocationProvider.OUT_OF_SERVICE:
					Toast.makeText(getBaseContext(),
							provider + " is out of service", Toast.LENGTH_LONG)
							.show();

					break;
				case LocationProvider.TEMPORARILY_UNAVAILABLE:
					Toast.makeText(getBaseContext(),
							provider + " is temporarily unavailable",
							Toast.LENGTH_LONG).show();

					break;

				default:
					break;
				}

			}

			@Override
			public void onProviderEnabled(String provider) {
				Toast.makeText(getBaseContext(), provider + " is enabled",
						Toast.LENGTH_LONG).show();

			}

			@Override
			public void onProviderDisabled(String provider) {
				Toast.makeText(getBaseContext(), provider + " is disabled",
						Toast.LENGTH_LONG).show();

			}

		};

	}

	@Override
	protected void onResume() {

		// min time (seconds)
		// distance (meter)
		locmgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
				locLis);
		super.onResume();
	}

	@Override
	protected void onPause() {
		locmgr.removeUpdates(locLis);
		super.onPause();
	}

}
