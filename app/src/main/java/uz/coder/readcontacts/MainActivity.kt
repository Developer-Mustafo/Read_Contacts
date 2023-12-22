package uz.coder.readcontacts

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import uz.coder.readcontacts.databinding.ActivityMainBinding

private const val TAG = "MainActivity"
private const val REQUEST_CODE = 1
class MainActivity : AppCompatActivity() {
    private val list = mutableListOf<Contact>()
    private lateinit var adapter: MyAdapter
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adapter = MyAdapter()
        adapter.submitList(list)
        binding.apply {
            btn.setOnClickListener {
                rec.adapter = adapter
                rec.layoutManager = LinearLayoutManager(this@MainActivity,LinearLayoutManager.VERTICAL,false)
                list.clear()
                if (ContextCompat.checkSelfPermission(this@MainActivity,android.Manifest.permission.READ_CONTACTS)
                    ==
                    PackageManager.PERMISSION_GRANTED
                ){
                    val uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                    val cursor = contentResolver.query(
                        uri,
                        null,
                        null,
                        null,
                        null
                    )
                    while(cursor?.moveToNext() == true){
                        val id = cursor.getInt(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone._ID))
                        val name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                        val phone = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
                        val contact = Contact(id, name, phone)
                        Log.d(TAG, "onCreate: $contact")
                        list.add(contact)
                    }
                }else{
                    permission()
                }
            }
        }
    }

    private fun permission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.READ_CONTACTS)){
            AlertDialog.Builder(this@MainActivity)
                .setIcon(R.drawable.ic_launcher_background)
                .setMessage("Ruxsat olish kerak !")
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                    ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_CONTACTS),
                    REQUEST_CODE)
                }
                .create().show()
        }else{
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_CONTACTS),
                REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode== REQUEST_CODE){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this@MainActivity, "Ruxsat berildi", Toast.LENGTH_SHORT).show()
            }else{
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package",packageName,null)
                intent.data = uri
                startActivity(intent)
            }
        }
    }
}