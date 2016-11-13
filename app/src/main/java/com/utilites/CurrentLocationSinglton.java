package com.utilites;

import android.app.Activity;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;

/**
 * Created by jaggi on 9/9/16.
 */


/**
 * get current location
 * <p/>
 * Keep in mind also need runtime permission so you should have runtime permission check
 * <p/>
 * <p/>
 * also use user below in activity you are using for location setting
 * <p/>
 * <p/>
 * // * protected void onActivityResult(int requestCode, int resultCode, Intent data) {
 * //    super.onActivityResult(requestCode, resultCode, data);
 * //
 * //    //final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
 * //    switch (requestCode) {
 * //    case CurrentLocationSinglton.REQUEST_CHECK_SETTINGS:
 * //    switch (resultCode) {
 * //    case Activity.RESULT_OK:
 * //    CurrentLocationSinglton.getInstance().startLocationUpdates();
 * //    break;
 * //    case Activity.RESULT_CANCELED:
 * //
 * //    break;
 * //    default:
 * //    break;
 * //    }
 * //    break;
 * //    }
 * //
 * //    }
 */
public class CurrentLocationSinglton {

    CurrentLocationCallback mCurrentLocationCallback;
    static CurrentLocationSinglton mCurrentLocationSinglton;

    Activity mcontext;


    public interface CurrentLocationCallback {

        void onSuccess(Location location);

        void onFailure();

    }


    protected LocationRequest mLocationRequest;

    GoogleApiClient mGoogleApiClient;
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 20000;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    public static final int REQUEST_CHECK_SETTINGS = 200;


    private CurrentLocationSinglton() {


    }

    public static CurrentLocationSinglton getInstance() {

        if (mCurrentLocationSinglton == null) {
            mCurrentLocationSinglton = new CurrentLocationSinglton();
        }

        return mCurrentLocationSinglton;

    }


    public void getCurrentLocation(Activity context, CurrentLocationCallback mCurrentLocationCallback) {

        this.mcontext = context;
        this.mCurrentLocationCallback = mCurrentLocationCallback;
        /**
         * if we do not have permission to access location
         */
        if (!checkLocationPermission()) {
            return;
        }


        createLocationRequest();
        setUpGoogleClient();

    }


    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }

    public void startLocationUpdates() {
        if (mGoogleApiClient == null) {
            return;
        }

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient,
                        builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {

                final Status status = result.getStatus();
                final LocationSettingsStates states = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:

                        /**
                         * if we do not have permission to access location
                         */
                        if (!checkLocationPermission()) {
                            return;
                        }

                        LocationServices.FusedLocationApi.requestLocationUpdates(
                                mGoogleApiClient,
                                mLocationRequest,
                                mLocationListener
                        );
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                        try {
                            status.startResolutionForResult(mcontext, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }

                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:


                        try {
                            status.startResolutionForResult(mcontext, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }

//                        if (mGoogleApiClient != null) {
//                            mGoogleApiClient.disconnect();
//                        }
//                        mCurrentLocationCallback.onFailure();
                        // Location settings are not satisfied. However, we have no way
                        // to fix the settings so we won't show the dialog.
                        break;
                }

            }
        });
    }

    private LocationListener mLocationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {

            if (mGoogleApiClient.isConnected()) {

                stopLocationUpdates();
                if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.disconnect();
                    mGoogleApiClient = null;
                }
            }
            mCurrentLocationCallback.onSuccess(location);


        }


    };

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient,
                mLocationListener
        ).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {
            }
        });
    }


    private void setUpGoogleClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(mcontext, new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(Bundle bundle) {

                startLocationUpdates();


            }

            @Override
            public void onConnectionSuspended(int i) {

                if (mGoogleApiClient != null) {
                    mGoogleApiClient.disconnect();
                }

            }
        }, new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(ConnectionResult connectionResult) {
                mCurrentLocationCallback.onFailure();
                if (mGoogleApiClient != null) {
                    mGoogleApiClient.disconnect();
                }
            }
        }).addApi(LocationServices.API).build();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.reconnect();
        }
    }

    /**
     * '
     * Runtime permission for location services
     *
     * @return true if permission granted
     */
    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(mcontext,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(mcontext,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(mcontext,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    Constants.REQUEST_PERMISSION_FOR_LOCATION);
            return false;

        }
        return true;
    }
}
