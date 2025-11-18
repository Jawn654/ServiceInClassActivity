package edu.temple.myapplication

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import java.util.logging.Handler

class MainActivity : AppCompatActivity() {

    lateinit var timerBinder: TimerService.TimerBinder
    lateinit var countView: TextView
    var isConnected = false

    private val handler = android.os.Handler(Looper.getMainLooper()) { msg ->
        countView.text = msg.what.toString()
        true
    }
    val serviceConnection = object: ServiceConnection{
        override fun onServiceConnected(p0: ComponentName?, service: IBinder?) {
            timerBinder = service as TimerService.TimerBinder
            isConnected = true
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            isConnected = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        countView = findViewById(R.id.countView)

        bindService(
            Intent(this, TimerService::class.java),
            serviceConnection,
            BIND_AUTO_CREATE
        )

        findViewById<Button>(R.id.startButton).setOnClickListener {
            if(!timerBinder.isRunning) {
                timerBinder.start(100)
                
            }
            else timerBinder.pause()
        }
        
        findViewById<Button>(R.id.stopButton).setOnClickListener {
            if(isConnected) timerBinder.stop()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.startButton ->{ if(!timerBinder.isRunning) {
                timerBinder.start(100)

            }
            else timerBinder.pause() }

            R.id.stopButton -> { timerBinder.stop() }


        }

        return super.onOptionsItemSelected(item)
    }
}