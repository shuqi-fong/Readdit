package com.example.readdit.ui.profile.notification

import android.app.*
import android.content.ContentValues.TAG
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.readdit.databinding.FragmentNotificationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class NotificationFragment : Fragment() {

    // View binding
    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!

    // Firebase Authentication
    private lateinit var firebaseAuth: FirebaseAuth

    // Firebase Firestore
    private lateinit var db : FirebaseFirestore
    private lateinit var documentReference : DocumentReference

    // User data
    private lateinit var userID : String

    // Set up for notification
    private lateinit var calendar: Calendar
    private lateinit var reminderManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Initialize binding
        _binding = FragmentNotificationBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initialize Firebase Authentication, Firebase Cloud Storage
        firebaseAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Get user id & reference
        userID = firebaseAuth.currentUser!!.uid
        documentReference = db.collection("user").document(userID)

        // Get the daily notification reminder switch from view
        val isDailyNotificationOnSwitch = binding.dailyNotiSwitch

        // Retrieve data from database
        documentReference.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                } else {
                    Log.d(TAG, "No such document")
                }

                val isDailyNotiChecked = document.getBoolean("isReminderOn")
                val reminderHour = document.getLong("reminderHour")!!.toInt()
                val reminderMin = document.getLong("reminderMin")!!.toInt()

                if(isDailyNotiChecked == true) {
                    // Check the switch and display time
                    isDailyNotificationOnSwitch.isChecked = true
                    displayTime(reminderHour, reminderMin)
                }

            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }

        isDailyNotificationOnSwitch.setOnCheckedChangeListener { _, isChecked ->
            // If the switch is checked = the user wish to set a daily reminder
            if(isChecked)
            {
                showTimePicker()
            }
            // If the switch is not checked = the user does not wish to set a daily reminder
            else
            {
                cancelReminder()
            }
        }

        // Create the notification
        createNotificationChannel()

        return root
    }

    private fun cancelReminder() {
        // Configure alarm manager
        reminderManager = activity?.getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(
            activity,
            ReminderReceiver::class.java
        )

        pendingIntent = PendingIntent.getBroadcast(activity, 0, intent, 0)

        reminderManager.cancel(pendingIntent)

        // Hide time text view
        binding.selectedTime.visibility = View.INVISIBLE

        // Update data in db
        documentReference.update("isReminderOn", false)
            .addOnSuccessListener {
                Log.d(tag, "Reminder check saved: false")
            }
            .addOnFailureListener {
                    e -> Log.w(tag, "Error occurred: ", e)
            }

        documentReference.update("reminderHour", null)
            .addOnSuccessListener {
                Log.d(tag, "Reminder hour saved: null")
            }
            .addOnFailureListener {
                    e -> Log.w(tag, "Error occurred: ", e)
            }

        documentReference.update("reminderMin", null)
            .addOnSuccessListener {
                Log.d(tag, "Reminder minute saved: null")
            }
            .addOnFailureListener {
                    e -> Log.w(tag, "Error occurred: ", e)
            }

        // Show toast message
        Toast.makeText(context, "Reminder cancelled.", Toast.LENGTH_SHORT).show()
    }

    private fun setReminder() {
        // Configure manager
        reminderManager = activity?.getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(
            activity,
            ReminderReceiver::class.java
        )

        pendingIntent = PendingIntent.getBroadcast(activity, 0, intent, 0)

        reminderManager.setRepeating(
            AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
            // Repeat the reminder daily
            AlarmManager.INTERVAL_DAY, pendingIntent
        )

        // Show toast message
        Toast.makeText(context, "Reminder successfully created.", Toast.LENGTH_SHORT).show()
    }

    private fun showTimePicker() {
        calendar = Calendar.getInstance()

        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            // Get the user input from the picker
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)

            // Update picked hour and minute in db
            documentReference.update("isReminderOn", true)
                .addOnSuccessListener {
                    Log.d(tag, "Reminder check saved: true")
                }
                .addOnFailureListener {
                        e -> Log.w(tag, "Error occurred: ", e)
                }

            documentReference.update("reminderHour", hour)
                .addOnSuccessListener {
                    Log.d(tag, "Reminder hour saved:$hour")
                }
                .addOnFailureListener {
                        e -> Log.w(tag, "Error occurred: ", e)
                }

            documentReference.update("reminderMin", minute)
                .addOnSuccessListener {
                    Log.d(tag, "Reminder minute saved:$minute")
                }
                .addOnFailureListener {
                        e -> Log.w(tag, "Error occurred: ", e)
                }

            displayTime(hour, minute)
            setReminder()
        }

        TimePickerDialog(activity, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

     private fun createNotificationChannel() {
         // Configure notification channel
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val name : CharSequence = "dailyReminderChannel"
            val description = "Channel for Alarm Manager"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("readdit", name, importance)

            channel.description = description

            val notificationManager = activity?.getSystemService(NotificationManager::class.java)

            notificationManager?.createNotificationChannel(channel)
        }
    }

    private fun displayTime(hour : Int, minute : Int) {
        // Update text view
        binding.selectedTime.visibility = View.VISIBLE

        if(hour > 12) {
            binding.selectedTime.text =
                "(" + String.format("%02d", hour - 12) + " : " + String.format("%02d", minute) + "PM" + ")"
        }
        else {
            binding.selectedTime.text =
                "(" + String.format("%02d", hour) + " : " + String.format("%02d", minute) + "AM" + ")"
        }
    }

}