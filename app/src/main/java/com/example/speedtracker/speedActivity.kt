package com.example.speedtracker

import android.Manifest
import android.Manifest.permission_group.LOCATION
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.telephony.SmsManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import kotlin.properties.Delegates

class speedActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks,
EasyPermissions.RationaleCallbacks
{
    private val TAG = "speedActivity"
    private  val LOCATION_PERM = 124
    private var speedUpStartTime = 0L
    private var speedUpEndTime = 0L
    private var speedDownStartTime = 0L
    private var speedDownEndTime = 0L


    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    private var isDone :Boolean by Delegates.observable(false){property, oldValue, newValue ->
        if (newValue == true)
        {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        }
    }


    lateinit var currentspeed:TextView
    lateinit var tenTothirty:TextView
    lateinit var thirtyToTen:TextView
    lateinit var map:Button
    private fun calcSpeed(speed: Int) {
        if (speed >= 10 ){
            speedUpStartTime=System.currentTimeMillis()
            speedDownEndTime=System.currentTimeMillis()

            if (speedDownStartTime != 0L){
                val speedDownTime = speedUpEndTime - speedUpStartTime
                thirtyToTen.text = (speedDownTime/1000).toString()
                speedDownStartTime = 0L
            }
        }
        else if(speed >= 30)
        {
            if (speedUpStartTime != 0L){
                speedUpEndTime = System.currentTimeMillis()
                val speedUpTime = speedUpEndTime - speedUpStartTime
                tenTothirty.text=(speedUpTime/1000).toString()
                speedUpStartTime = 0L
            }
            speedDownStartTime=System.currentTimeMillis()
        }
        else if (speed > 60){
            val uri = Uri.parse("smsto:0706302881")

            val intent = Intent(Intent.ACTION_SENDTO, uri)

            intent.putExtra("SPEED TRACKER", "YOU ARE OVER SPEEDING")

            startActivity(intent)
        }



    }
    override fun onResume(){
        super.onResume()
        startLocationUpdates()
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }
    override fun onPause(){
        super.onPause()
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_speed)
        tenTothirty = findViewById(R.id.tenToThirtyId)
        thirtyToTen = findViewById(R.id.thirtyToTenId)
        currentspeed = findViewById(R.id.currentSpeedId)
        map = findViewById(R.id.btn_map)

        map.setOnClickListener {
            val intent = Intent(this,MapsActivity::class.java)
            startActivity(intent)
        }


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        askLocationPermission()
        createLocationRequest()

        locationCallback = object : LocationCallback(){

            override fun onLocationResult(locationResult: LocationResult) {
                locationResult ?: return
                if(!isDone){
                    val speedToInt = locationResult.lastLocation.speed.toInt()
                    calcSpeed(speedToInt)
                    currentspeed.text = speedToInt.toString()

                }
            }
        }

    }

    fun createLocationRequest() {
        locationRequest = LocationRequest.create().apply {
            interval = 1000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
//    if (EasyPermissions.somePermissionDenied(this,perms)){
//        AppSettingsDialog.Builder(this).build().show()
//    }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE){
            val yes = "Allow"
            val no = "Deny"
            Toast.makeText(this,"onActivityResult",Toast.LENGTH_LONG).show()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        //it doesnt mean anything
    }

    override fun onRationaleAccepted(requestCode: Int) {

    }

    override fun onRationaleDenied(requestCode: Int) {

    }

    private fun hasLocationPermissions(): Boolean {
        return EasyPermissions.hasPermissions(this,android.Manifest.permission.ACCESS_FINE_LOCATION)

    }
    fun askLocationPermission(){
        if (hasLocationPermissions()){
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            fusedLocationProviderClient.lastLocation
                .addOnSuccessListener {
                    location: Location? ->

                }
        }
        else{
            EasyPermissions.requestPermissions(
                this,
                "Need permisssion to find your location and get the speed calculations",
                LOCATION_PERM,
                android.Manifest.permission.ACCESS_FINE_LOCATION

            )
        }
    }


}