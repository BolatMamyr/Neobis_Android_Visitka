package com.example.visitka

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Toast
import com.example.visitka.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private var doctor: Doctor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getDoctor()
        doctor?.lastName?.let { binding.txtName.text = "Dr. $it" }
        setClickListeners()
    }

    private fun setClickListeners() {
        binding.apply {
            cardToCall.setOnClickListener {
                if (hasCallPermission()) {
                    call()
                } else {
                    requestCallPermission()
                }
            }

            cardWhatsApp.setOnClickListener {
                // by default it is not existing phone number
                sendWhatsAppMessage()
            }

            cardEmail.setOnClickListener {
                sendEmail()
            }

            cardWebsite.setOnClickListener {
                openWebsite()
            }

            cardTelegram.setOnClickListener {
                // by default it is my telegram account
                sendTelegramMessage()
            }

            cardSaveContact.setOnClickListener {
                // by default it is not existing phone number
                saveContact()
            }

            txtOpenMap.setOnClickListener {
                Intent(Intent.ACTION_VIEW).also {
                    it.data = Uri.parse("http://maps.google.com/maps?daddr=Asia+Mall+Bishkek")
                    if (it.resolveActivity(packageManager) != null) {
                        startActivity(it)
                    }
                }
            }
        }
    }

    private fun openWebsite() {
        Intent(Intent.ACTION_VIEW).also {
            it.data = Uri.parse("https://" + doctor?.website)
            if (it.resolveActivity(packageManager) != null) {
                startActivity(it)
            }
        }
    }

    private fun saveContact() {
        Intent(Intent.ACTION_INSERT).also {
            it.type = ContactsContract.Contacts.CONTENT_TYPE
            it.putExtra(ContactsContract.Intents.Insert.NAME, "Dr. ${doctor?.lastName}")
            it.putExtra(
                ContactsContract.Intents.Insert.PHONE,
                "+" + doctor?.phoneNumber?.toString()
            )
            it.flags = Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_MULTIPLE_TASK
            try {
                startActivity(it)
            } catch (e: Exception) {
                Toast.makeText(
                    this@ProfileActivity,
                    getString(R.string.failed_to_save_contact),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun sendEmail() {
        Intent(Intent.ACTION_SEND).also {
            it.type = "message/rfc822"
            it.putExtra(Intent.EXTRA_EMAIL, arrayOf(doctor?.email))
            it.putExtra(Intent.EXTRA_SUBJECT, "Some subject")
            it.putExtra(Intent.EXTRA_TEXT, "Hello!")

            if (it.resolveActivity(packageManager) != null) {
                startActivity(Intent.createChooser(it, getString(R.string.choose_email_client)))
            } else {
                Toast.makeText(this, "nope", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendWhatsAppMessage() {
        Intent(Intent.ACTION_VIEW).also {
            it.data = Uri.parse("https://wa.me/${doctor?.phoneNumber}?text=Hello!")
            if (it.resolveActivity(packageManager) != null) {
                startActivity(it)
            }
        }
    }

    private fun sendTelegramMessage() {
        Intent(Intent.ACTION_VIEW).also {
            it.data = Uri.parse("https://telegram.me/${doctor?.telegramId}?")
            it.`package` = "org.telegram.messenger"
            if (it.resolveActivity(packageManager) != null) {
                startActivity(it)
            } else {
                navigateToPlayStore("org.telegram.messenger")
            }
        }
    }

    // if app is not installed open Play Store page of that app
    private fun navigateToPlayStore(appPackageName: String) {
        try {
            // try to open Play Store app
            val uri = Uri.parse("market://details?id=$appPackageName")
            startActivity(Intent(Intent.ACTION_VIEW, uri))
        } catch (e: ActivityNotFoundException) {
            // Play Store is not installed. Open browser page.
            val uri = Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
            startActivity(Intent(Intent.ACTION_VIEW, uri))
        }
    }

    // Check if permission status was changed and repeat operation not to make user click on button
    // one more time
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PermissionRequestCode.CALL_PHONE.code -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    call()
                }
            }
        }
    }

    private fun call() {
        doctor?.let { doctor ->
            Intent(Intent.ACTION_CALL).also { intent ->
                intent.data = Uri.parse("tel:${doctor.phoneNumber}")

                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                }
            }
        }
    }

    private fun requestCallPermission() {
        requestPermissions(
            arrayOf(Manifest.permission.CALL_PHONE),
            PermissionRequestCode.CALL_PHONE.code
        )
    }


    private fun getDoctor() {
        doctor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(MainActivity.EXTRA_DOCTOR, Doctor::class.java)
        } else {
            intent.getParcelableExtra(MainActivity.EXTRA_DOCTOR)
        }
    }

    private fun hasCallPermission() = checkSelfPermission(
        android.Manifest.permission.CALL_PHONE
    ) == PackageManager.PERMISSION_GRANTED
}