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

        // 3. Update the 5G button to run the NR_ONLY commands
        btnForce5G.setOnClickListener {
            if (tvRootStatus.text == "Granted") {
                tvStatus.text = "Status: Forcing 5G (NR Only)..."

                // We use semicolons to run multiple shell commands back-to-back
                val force5GCommand = "settings put global preferred_network_mode 32; " +
                        "settings put global preferred_network_mode1 32; " +
                        "settings put global preferred_network_mode2 32"

                val success = executeRootCommand(force5GCommand)

                if (success) {
                    tvStatus.text = "Status: 5G Forced! (Toggle Airplane mode if signal doesn't refresh)"
                } else {
                    tvStatus.text = "Status: Failed to execute command."
                }
            } else {
                tvStatus.text = "Status: Cannot execute. No root access."
            }
        }

        // 4. Update the Auto button to run the Global Auto commands
        btnAuto.setOnClickListener {
            if (tvRootStatus.text == "Granted") {
                tvStatus.text = "Status: Restoring Auto Network..."

                // 33 is the standard integer for NR/LTE/GSM/WCDMA Auto
                val autoCommand = "settings put global preferred_network_mode 33; " +
                        "settings put global preferred_network_mode1 33; " +
                        "settings put global preferred_network_mode2 33"

                val success = executeRootCommand(autoCommand)

                if (success) {
                    tvStatus.text = "Status: Auto mode restored! (Toggle Airplane mode if needed)"
                } else {
                    tvStatus.text = "Status: Failed to execute command."
                }
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