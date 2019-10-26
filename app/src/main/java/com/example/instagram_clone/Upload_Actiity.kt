package com.example.instagram_clone

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_upload__actiity.*

class Upload_Actiity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload__actiity)
    }

//    Select Image
//    fun selectImage(view: View){
//    Toast.makeText(this, "Hasi", Toast.LENGTH_LONG).show()
//}
@RequiresApi(Build.VERSION_CODES.M)
fun selectImage(view: View){
        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),2)
        } else{
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent,1)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode === 2){
            if(grantResults.size > 0 && grantResults[0] === PackageManager.PERMISSION_GRANTED){
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent,1)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == 1 && resultCode == Activity.RESULT_OK && data != null){
            val image = data.data
            try {
                var selectedImage = MediaStore.Images.Media.getBitmap(this.contentResolver, image)
                selectedImages.setImageBitmap(selectedImage)
                tapImageTxt.setText("Tap to Change Image")
            }catch (e:Exception){
                println(e.localizedMessage)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }



//    Upload Post
    fun uploadPost(view: View){

    }

}
