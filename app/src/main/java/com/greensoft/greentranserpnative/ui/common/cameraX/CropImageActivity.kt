package com.greensoft.greentranserpnative.ui.common.cameraX

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.greensoft.greentranserpnative.ENV
import com.greensoft.greentranserpnative.databinding.CropImageActivityBinding
import com.yalantis.ucrop.UCrop
import java.io.File
import java.util.UUID

class CropImageActivity : AppCompatActivity() {
    private lateinit var activityBinding: CropImageActivityBinding
    private var fileUri: Uri? = null
    private var result: String? = null

    private lateinit var cropLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = CropImageActivityBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)

        readIntent()
        val dest_uri = StringBuilder(UUID.randomUUID().toString()).append(".jpg").toString()
        val options = UCrop.Options()
        val uCropIntent = UCrop.of(fileUri!!, Uri.fromFile(File(cacheDir, dest_uri)))
            .withOptions(options)
            .withAspectRatio(0f, 0f)
            .useSourceImageAspectRatio()
            .withMaxResultSize(2000, 2000)

        // Initialize the cropLauncher
        cropLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                if (data != null) {
                    val resultUri = UCrop.getOutput(data)
                    // Save the resultUri in SharedPreferences
//                    val sharedPref = getSharedPreferences("image_prefs", Context.MODE_PRIVATE)
                    val sharedPref = getSharedPreferences(ENV.IMAGE_PREF_SHARE, Context.MODE_PRIVATE)
                    sharedPref.edit().putString("image_uri", resultUri.toString()).apply()
                    val returnIntent = Intent()
                    returnIntent.putExtra("RESULT", resultUri.toString())
                    setResult(RESULT_OK, returnIntent)
                }
            }
            finish()
        }

        // Start the UCrop activity
        val intent = uCropIntent.getIntent(this)
        cropLauncher.launch(intent)


    }

    private fun readIntent() {
        val intent = intent
        if (intent.extras != null) {
            result = intent.getStringExtra("DATA")
            fileUri = Uri.parse(result)
        }
    }
}
