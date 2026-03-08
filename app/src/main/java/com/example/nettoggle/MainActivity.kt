package com.example.nettoggle

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.nettoggle.R
import java.io.DataOutputStream

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnForce5G = findViewById<Button>(R.id.btnForce5G)
        val btnAuto = findViewById<Button>(R.id.btnAuto)
        val tvStatus = findViewById<TextView>(R.id.tvStatus)

        val rootStatusLayout = findViewById<LinearLayout>(R.id.rootStatusLayout)
        val tvRootStatus = findViewById<TextView>(R.id.tvRootStatus)

        fun checkAndUpdateRootStatus() {
            tvStatus.text = "Status: Checking root permissions..."

            val hasRoot = executeRootCommand("ls /data")

            if (hasRoot) {
                tvRootStatus.text = "Granted"
                tvRootStatus.setTextColor(Color.parseColor("#4CAF50")) // Material Green
                tvStatus.text = "Status: Root access granted! Ready for commands."
            } else {
                tvRootStatus.text = "Denied"
                tvRootStatus.setTextColor(Color.parseColor("#F44336")) // Material Red
                tvStatus.text = "Status: Root access denied. Check Magisk/KernelSU."
            }
        }

        checkAndUpdateRootStatus()

        rootStatusLayout.setOnClickListener {
            checkAndUpdateRootStatus()
        }

        btnForce5G.setOnClickListener {
            if (tvRootStatus.text == "Granted") {
                tvStatus.text = "Status: Executing 5G force command..."
            } else {
                tvStatus.text = "Status: Cannot execute. No root access."
            }
        }

        btnAuto.setOnClickListener {
            if (tvRootStatus.text == "Granted") {
                tvStatus.text = "Status: Executing Auto network command..."
            } else {
                tvStatus.text = "Status: Cannot execute. No root access."
            }
        }
    }

    private fun executeRootCommand(command: String): Boolean {
        return try {
            val process = Runtime.getRuntime().exec("su")
            val outputStream = DataOutputStream(process.outputStream)

            outputStream.writeBytes(command + "\n")
            outputStream.flush()

            outputStream.writeBytes("exit\n")
            outputStream.flush()

            process.waitFor()
            process.exitValue() == 0

        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}