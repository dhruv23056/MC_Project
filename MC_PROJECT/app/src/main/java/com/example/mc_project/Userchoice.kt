package com.example.mc_project

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Activity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_2)

        // Find Button1 and set OnClickListener
        findViewById<Button>(R.id.button1).setOnClickListener {
            // Navigate to Activity3
            startActivity(Intent(this, Activity3::class.java))
        }

        // Find Button2 and set OnClickListener
        findViewById<Button>(R.id.button2).setOnClickListener {
            // Navigate to Activity4
            startActivity(Intent(this, Activity4::class.java))
        }
    }
}
