package com.example.chatapplication.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.chatapplication.Adapter.ViewPagerAdapter
import com.example.chatapplication.R
import com.example.chatapplication.UIFragments.CallFragment
import com.example.chatapplication.UIFragments.ChatFragment
import com.example.chatapplication.UIFragments.StatusFragment
import com.example.chatapplication.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private var binding:ActivityMainBinding ?=null
    lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)


        setContentView(binding!!.root)
        val fragmentArrayList = ArrayList<Fragment>()

        fragmentArrayList.add(ChatFragment())
        fragmentArrayList.add(StatusFragment())
        fragmentArrayList.add(CallFragment())

        auth= FirebaseAuth.getInstance()
        if (auth.currentUser == null){
            startActivity(Intent(this,NumberActivity::class.java))
            // we do back exits
            finish()
        }



        val adapter= ViewPagerAdapter(this,supportFragmentManager,fragmentArrayList)
        binding!!.viewPager.adapter= adapter
        binding!!.tabs.setupWithViewPager(binding!!.viewPager)


    }
}