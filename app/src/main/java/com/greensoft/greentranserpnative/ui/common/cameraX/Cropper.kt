package com.greensoft.greentranserpnative.ui.common.cameraX

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toBitmap
import com.greensoft.greentranserpnative.ENV
import com.greensoft.greentranserpnative.databinding.CropperActivityBinding
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream


class Cropper : AppCompatActivity() {
    private lateinit var cameraPermission: Array<String>
    private lateinit var storagePermission: Array<String>
    private lateinit var activityBinding: CropperActivityBinding
    private lateinit var uri: Uri
    private lateinit var file : File
    private lateinit var camIntent: Intent
    private lateinit var cameraLauncher: ActivityResultLauncher<Intent>
    private lateinit var galleryLauncher: ActivityResultLauncher<String>

    var resultUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = CropperActivityBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)

        cameraPermission = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        storagePermission = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)

        onClick()
        cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // Handle the result from the camera
                val intent = Intent(this@Cropper, CropImageActivity::class.java)
                intent.putExtra("DATA", uri.toString()) // Pass the captured image's Uri to CropImageActivity
                startActivity(intent)
            }
        }

        galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { result ->
            if (result != null) {
                val intent = Intent(this@Cropper, CropImageActivity::class.java)
                intent.putExtra("DATA", result.toString()) // Pass the gallery image's Uri to CropImageActivity
                startActivity(intent)
            }
        }

    }
    private fun onClick(){
        activityBinding.clickBtn.setOnClickListener{
            showImagePicDialog()
        }
    }

    override fun onResume() {
        super.onResume()
//        val sharedPref = getSharedPreferences("image_prefs", Context.MODE_PRIVATE)
        val sharedPref = getSharedPreferences(ENV.IMAGE_PREF_SHARE, Context.MODE_PRIVATE)
        val savedUri = sharedPref.getString("image_uri", null)
        if (savedUri != null) {
            resultUri = Uri.parse(savedUri)
            activityBinding.setProfileImage.setImageURI(resultUri)
        }
    }


    private fun showImagePicDialog() {
        val options = arrayOf("Camera", "Gallery")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pick Image From")
        builder.setItems(options) { dialog, which ->
            if (which == 0) {
                if (!checkCameraPermission()) {
                    requestCameraPermission()
                } else {
//                    pickFromGallery()
                    pickFromCamera()
                }
            } else if (which == 1) {
                if (!checkStoragePermission()) {
                    requestStoragePermission()
                } else {
                    pickFromGallery()
                }
            }
        }
        builder.create().show()
    }
    //    Checking permission
    private fun checkStoragePermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }
    // Requesting gallery permission
    private fun requestStoragePermission() {
        requestPermissions(storagePermission, STORAGE_REQUEST)
    }
    // checking camera permissions
    private fun checkCameraPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
        val result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        return result && result1
    }
    private fun requestCameraPermission() {
        requestPermissions(cameraPermission, CAMERA_REQUEST)
    }
    private fun pickFromGallery() {
        galleryLauncher.launch("image/*")
    }
    private fun pickFromCamera() {

        val imageFileName = "file" + System.currentTimeMillis().toString() + ".jpg"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        file = File(storageDir, imageFileName)
        uri = FileProvider.getUriForFile(this, "${packageName}.fileprovider", file)
        camIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        camIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        camIntent.putExtra("return-Data", true)
//        camIntent.putExtra(ENV.IMAGE_PREF_SHARE,true)
        cameraLauncher.launch(camIntent)
    }
    companion object {
        private const val GalleryPick = 1
        private const val CAMERA_REQUEST = 100
        private const val STORAGE_REQUEST = 200
        private const val IMAGEPICK_GALLERY_REQUEST = 300
        private const val IMAGE_PICKCAMERA_REQUEST = 400

        private fun convertImageUriToBase64(contentResolver: ContentResolver, imageUri: Uri): String? {
            return try {
                val inputStream: InputStream? = contentResolver.openInputStream(imageUri)
                val buffer = ByteArrayOutputStream()
                val bufferSize = 1024
                val data = ByteArray(bufferSize)
                var bytesRead= 0
                while (inputStream?.read(data, 0, bufferSize).also {
                        if (it != null) {
                            bytesRead = it
                        }
                    } != -1) {
                    buffer.write(data, 0, bytesRead)
                }
                val byteArray: ByteArray = buffer.toByteArray()
                Base64.encodeToString(byteArray, Base64.DEFAULT)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    private fun bitmaptoImageView(){
        val base64Image = convertImageUriToBase64(contentResolver,uri)
        Toast.makeText(this, "bse", Toast.LENGTH_SHORT).show()
        // Decode the Base64 string into a byte array
        val decodedBytes: ByteArray = Base64.decode(base64Image, Base64.DEFAULT)
        val bitmap: Bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }
    private fun getbitmapfromImageView(){
        activityBinding.setProfileImage.drawable.toBitmap()

    }
}

