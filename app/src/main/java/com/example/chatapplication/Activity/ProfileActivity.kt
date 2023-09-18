package com.example.chatapplication.Activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.example.chatapplication.R
import com.example.chatapplication.databinding.ActivityProfileBinding
import com.example.chatapplication.modelClass.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.Date

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var storage:FirebaseStorage
    private lateinit var database:FirebaseDatabase
    private var selectImg: Uri? =null
    private lateinit var  dialog: AlertDialog.Builder
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth= FirebaseAuth.getInstance()
        storage= FirebaseStorage.getInstance()
        database= FirebaseDatabase.getInstance()

        dialog= AlertDialog.Builder(this)
            .setMessage("Updating Profile....")
            .setCancelable(false)

        val galleryLauncher= registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) {

                if (it != null) {
                    selectImg = it
                }
                    binding.userImage.setImageURI(selectImg)
        }

        binding.userImage.setOnClickListener {
            //intent.action=Intent.ACTION_GET_CONTENT
            //intent.type="image/*"
           // startActivityForResult(intent,1)
            galleryLauncher.launch("image/*")

        }

        binding.profileButton.setOnClickListener {
            if(binding.userName.text!!.isEmpty()){
                Toast.makeText(this, "Please Enter your Name..",Toast.LENGTH_SHORT).show()
            }else if (selectImg == null){

                Toast.makeText(this, "Please Enter your Image..",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Loading....!",Toast.LENGTH_SHORT).show()
                uploadData()
            }
        }

    }

    private fun uploadData() {
       val reference = storage.reference.child("Profile").child(Date().time.toString())
        selectImg?.let {
            reference.putFile(it).addOnCompleteListener{
                if(it.isSuccessful){
                    reference.downloadUrl.addOnSuccessListener {task ->
                        uploadInfo(task.toString())
                    }
                }

            }
        }


    }

    private fun uploadInfo(imageUrl: String) {
       val user= UserModel(auth.uid.toString(),binding.userName.text.toString(),
           auth.currentUser!!.phoneNumber.toString(),imageUrl)

        database.reference.child("users")
            .child(auth.uid.toString())
            .setValue(user)
            .addOnSuccessListener {
                Toast.makeText(this,"Data Inserted",Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,MainActivity::class.java))
                 finish()
            }

    }
}