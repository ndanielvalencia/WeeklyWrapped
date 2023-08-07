package com.csci448.le1.weeklywrapped.util

import com.csci448.le1.weeklywrapped.MainActivity
import android.Manifest
import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.ui.res.stringResource
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.startActivity
import com.csci448.le1.weeklywrapped.R
import com.csci448.le1.weeklywrapped.data.CameraEnabled
import com.csci448.le1.weeklywrapped.presentation.viewmodel.WrappedViewModel
import java.text.SimpleDateFormat
import java.util.*


class PhotoAlarmReceiver : BroadcastReceiver() {
    companion object {
        private const val LOG_TAG = "448.LocationAlarmReceiver"
        private const val ALARM_ACTION = "448_ALARM_ACTION"
        private const val EXTRA_CAMERA_ENABLED = "camera_enabled"
        private fun createIntent(context: Context): Intent {
            val intent = Intent(context, PhotoAlarmReceiver::class.java).apply {
                action = ALARM_ACTION
            }
            return intent
        }
    }


    private fun scheduleAlarm(activity: Activity) {
        val alarmManager = activity.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = createIntent(activity)
        val pendingIntent = PendingIntent.getBroadcast(
            activity,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val alarmDelayInSeconds = 10
        val alarmTimeInUTC = System.currentTimeMillis() + alarmDelayInSeconds * 1_000L
        Log.d(
            LOG_TAG, "Setting alarm for ${
                SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.US).format(
                    Date(alarmTimeInUTC)
                )
            }"
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Log.d(LOG_TAG, "running on Version S or newer, checking if can schedule exact alarms")
            if (alarmManager.canScheduleExactAlarms()) {
                Log.d(LOG_TAG, "can schedule exact alarms")
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    alarmTimeInUTC,
                    pendingIntent
                )
            } else {
                Log.d(LOG_TAG, "can’t schedule exact alarms, launching intent to bring up settings")
                val settingsIntent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                startActivity(activity, settingsIntent, null)
            }
        } else {
            Log.d(LOG_TAG, "running on Version R or older, can set alarm directly")
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                alarmTimeInUTC,
                pendingIntent
            )
        }
    }

    fun checkPermissionAndScheduleAlarm(
        activity: Activity,
        permissionLauncher: ActivityResultLauncher<String>,
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Log.d(LOG_TAG, "running on Version Tiramisu or newer, need permission")
            if (activity.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
                == PackageManager.PERMISSION_GRANTED
            ) {
                Log.d(LOG_TAG, "have notification permission")
                scheduleAlarm(activity)
            } else {
                if (ActivityCompat
                        .shouldShowRequestPermissionRationale(
                            activity,
                            Manifest.permission.POST_NOTIFICATIONS
                        )
                ) {
                    Log.d(LOG_TAG, "previously denied notification permission")
                    Toast.makeText(
                        activity,
                        activity.getString(R.string.please_enable_notification_permission_text),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Log.d(LOG_TAG, "request notification permission")
                    permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        } else {
            Log.d(LOG_TAG, "running on Version S or older, post away")
            scheduleAlarm(activity)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        Log.d(LOG_TAG, "received alarm for action ${intent.action}")
        if (intent.action == ALARM_ACTION) {
            Log.d(LOG_TAG, "received our intent")

            if (ActivityCompat
                    .checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                == PackageManager.PERMISSION_GRANTED
            ) {
                Log.d(LOG_TAG, "have permission to post notifications")
                val notificationManager = NotificationManagerCompat.from(context)
                val channelID = "weekly_wrapped_01" // replace with your desired channel ID
                val channelName = "Weekly Wrapped Reminder" // replace with your desired channel name
                val channelDesc =
                    "Reminders for photo" // replace with your desired channel description
                val channel =
                    NotificationChannel(
                        channelID,
                        channelName,
                        NotificationManager.IMPORTANCE_DEFAULT
                    ).apply {
                        description = channelDesc
                    }

                val notificationTitle = context.getString(R.string.time_to_take_photo_title)
                val notificationText = context.getString(R.string.click_to_enable_camera_title)

                val deepLinkPendingIntent = MainActivity
                    .createPendingIntent(context, true)
                val notification = NotificationCompat.Builder(context, channelID)
                    .setSmallIcon(android.R.drawable.ic_dialog_map)
                    .setContentTitle(notificationTitle)
                    .setContentText(notificationText)
                    .setContentIntent(deepLinkPendingIntent)
                    .setAutoCancel(true)
                    .build()
                notificationManager.createNotificationChannel(channel)
                notificationManager.notify(0, notification)
            }
        }

    }
}