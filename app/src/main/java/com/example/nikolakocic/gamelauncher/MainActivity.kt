package com.example.nikolakocic.gamelauncher

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer


class MainActivity : AppCompatActivity() {
    companion object {
        private val permissionRequestCodes = ActivityStartCodes.values().map { it.code }.toList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.button).setOnClickListener {
            run()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (permissionRequestCodes.contains(requestCode)) {
            val model: MyViewModel by viewModels()
            model.finishedPermissionRequest(requestCode, applicationContext)
        }
    }

    private fun run() {
        val model: MyViewModel by viewModels()
        model.getPermissionToRequest().observe(this, Observer {
            it?.requestPermission(this)
        })
        model.run(applicationContext)
    }
}
