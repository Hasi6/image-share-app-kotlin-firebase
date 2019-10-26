package com.example.instagram_clone

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_upload__actiity.*
import java.util.*

class Upload_Actiity : AppCompatActivity() {
    var image : Uri? = null
    var mAuth: FirebaseAuth? = null
    var mAuthListner: FirebaseAuth.AuthStateListener? = null
    var firebaseDatabase: FirebaseDatabase? = null
    var myRef: DatabaseReference? = null
    var myStorageRef: StorageReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload__actiity)

        mAuth = FirebaseAuth.getInstance()
        mAuthListner = FirebaseAuth.AuthStateListener {  }
        myRef = firebaseDatabase?.reference
        myStorageRef = FirebaseStorage.getInstance().reference

        toggelLoading(false)

    }

    fun toggelLoading(type: Boolean){
        if(type){
            percentage.visibility = View.VISIBLE
            progressBar.visibility = View.VISIBLE
        }else{
            percentage.visibility = View.INVISIBLE
            progressBar.visibility = View.INVISIBLE
        }
    }

//    Select Image
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
            image = data.data
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
        var uploadPercentage: Double = 0.0
        val uuid = UUID.randomUUID()
        val imageName = "images/$uuid.jpg"
        var downloadUri: Uri? = null
        val storageref = myStorageRef?.child(imageName)

        if(image !== null && commentTxt.text.isNotEmpty()){

           var uploadTask =  storageref!!.putFile(image!!)

            val urlTask = uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                storageref.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    downloadUri = task.result
                    println("Hasi $downloadUri")


                } else {
                    print("Fuck")
                    Toast.makeText(this, "Failed Please Try Again Later", Toast.LENGTH_LONG)
                }
            }


            if(downloadUri !== null){
                Toast.makeText(this, downloadUri.toString(), Toast.LENGTH_LONG)
            }else{
                Toast.makeText(this, "hasasas", Toast.LENGTH_LONG)
            }

//            old and not working
//            storageref?.putFile(image!!)?.addOnSuccessListener { taskSnapshot ->
//
//                taskSnapshot.downLoadUrl()
//                toggelLoading(true)
//                uploadPercentage = ((taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount) * 100).toDouble()
//                percentage.setText(uploadPercentage.toString())
//
//                if(uploadPercentage === 100.0){
//                    toggelLoading(false)
//                }
//
//            }
        }else{
            Toast.makeText(this, "Please Fill App the Fields", Toast.LENGTH_LONG).show()
        }
    }

}
