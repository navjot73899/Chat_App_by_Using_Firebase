package com.example.chatapplication.UIFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.chatapplication.Adapter.chatAdapter
import com.example.chatapplication.R
import com.example.chatapplication.databinding.FragmentChatBinding
import com.example.chatapplication.modelClass.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatFragment : Fragment() {
    lateinit var  binding:FragmentChatBinding
    private var database:FirebaseDatabase?=null
    lateinit var userList:ArrayList<UserModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentChatBinding.inflate(layoutInflater)
        database= FirebaseDatabase.getInstance()
        userList= ArrayList()

        //Getting database from firebase
        database!!.reference.child("users")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                  userList.clear() // to remove any previous data
                  //use for loop to insert the data

                    for (snapshot1 in snapshot.children){
                        val user = snapshot1.getValue(UserModel::class.java)

                        //we need add all user accept the current user
                        if(user!!.vid!= FirebaseAuth.getInstance().uid){
                            userList.add(user)
                        }
                    }
                     binding.userListRecycleView.adapter=chatAdapter(requireContext(),userList)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })




        // Inflate the layout for this fragment
        return binding.root
    }



}