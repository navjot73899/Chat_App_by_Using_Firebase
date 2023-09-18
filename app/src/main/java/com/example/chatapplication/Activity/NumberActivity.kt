 package com.example.chatapplication.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.chatapplication.R
import com.example.chatapplication.databinding.ActivityNumberBinding
import com.google.firebase.auth.FirebaseAuth

 class NumberActivity : AppCompatActivity() {
     private  lateinit var  binding: ActivityNumberBinding
     private lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityNumberBinding.inflate(layoutInflater)
        setContentView(binding.root)
         auth= FirebaseAuth.getInstance()

        //if we already have the user then we move user to MainActivity
        if (auth.currentUser!=null){
            startActivity(Intent(this,MainActivity::class.java))
             // we do back exits
            finish()
        }
        // new user
        binding.continueButton.setOnClickListener {
            if(binding.phoneNumber.text!!.isEmpty()){
                Toast.makeText(this,"PLease Enter the Number!!!",Toast.LENGTH_SHORT).show()
            }else{
                var intent= Intent(this, OTPActivity::class.java)
                 intent.putExtra("number",binding.phoneNumber.text!!.toString())
                 startActivity(intent)
            }
        }




    }
}