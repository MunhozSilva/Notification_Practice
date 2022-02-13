package com.example.notificationpractice

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.notification_button)

        button.setOnClickListener {
            val builder = NotificationCompat.Builder(this, "channel_id")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(getString(R.string.notification_title))
                .setContentText(getString(R.string.notification_text))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            with(NotificationManagerCompat.from(this)) {
                notify(1, builder.build())
            }

            createNotificationChannel()
        }

        val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                toast("Permissão recebida")
            } else {
                toast("Explicar para o usuário o porquê da permissão")
            }
        }

        val permissionButton = findViewById<Button>(R.id.button_request_permission)
        permissionButton.setOnClickListener {
            requestPermission(permissionLauncher)
        }

    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("channel_id", name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun toast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    private fun requestPermission(requestPermission: ActivityResultLauncher<String>) = when {
        ContextCompat
            .checkSelfPermission(this, PERMISSION) == PackageManager.PERMISSION_GRANTED -> {
            // Usar a API
        }

        shouldShowRequestPermissionRationale(PERMISSION) -> {
            toast("Explicar para o usuário o porquê da permissão")
        }

        else -> {
            requestPermission.launch(PERMISSION)
        }
    }

    private companion object {
        private const val PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE
    }
}