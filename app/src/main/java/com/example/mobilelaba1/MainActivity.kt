package com.example.mobilelaba1

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), SensorEventListener{

    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometer: Sensor
    private lateinit var displayMetrics: DisplayMetrics


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels

        imageView4.x = (width / 2 - imageView4.layoutParams.width / 2).toFloat()
        imageView4.y = (height / 2 - imageView4.layoutParams.height / 2).toFloat()

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.let {
            accelerometer = it
        }
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME)
        // repeat that line for each sensor you want to monitor
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        when (event?.sensor?.type) {
            Sensor.TYPE_ACCELEROMETER  -> {
                val dx = event.values[0]
                val dy = event.values[1]

                val e = 4.0

                val height = displayMetrics.heightPixels
                val width = displayMetrics.widthPixels

                imageView4.x -= (dx * e).toFloat()
                imageView4.y += (dy * e).toFloat()

                if (imageView4.x < 0 || imageView4.x > width - imageView4.layoutParams.width)
                {
                    if (imageView4.x < 0)
                    {
                        imageView4.x = (0).toFloat()
                    }
                    else
                    {
                        imageView4.x = (width - imageView4.layoutParams.width).toFloat()
                    }
                }
                val heightNavBar: Int
                val resourceId: Int = resources.getIdentifier("navigation_bar_height", "dimen", "android")

                if (resourceId > 0) {
                     heightNavBar = resources.getDimensionPixelSize(resourceId)
                }
                else
                {
                    heightNavBar = 0
                }

                if (imageView4.y < 0 || imageView4.y > height - heightNavBar - imageView4.layoutParams.height)
                {
                    if (imageView4.y < 0)
                    {
                        imageView4.y = (0).toFloat()
                    }
                    else
                    {
                        imageView4.y = (height - heightNavBar - imageView4.layoutParams.height).toFloat()
                    }
                }

                imageView4.invalidate()
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu to use in the action bar
        val inflater = menuInflater
        inflater.inflate(R.menu.toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar menu items
        when (item.itemId) {
            R.id.action_change_img -> {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "image/*"
                if (intent.resolveActivity(packageManager) != null) {
                    startActivityForResult(intent, 1)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            imageView4.setImageURI(data?.data)
        }
    }
}

