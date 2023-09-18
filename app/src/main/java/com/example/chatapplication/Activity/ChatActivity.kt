package com.example.chatapplication.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.chatapplication.Adapter.ChatMessageAdapter
import com.example.chatapplication.R
import com.example.chatapplication.databinding.ActivityChatBinding
import com.example.chatapplication.modelClass.MessageModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Date

class ChatActivity : AppCompatActivity() {
    lateinit var binding:ActivityChatBinding
    private lateinit var database: FirebaseDatabase

    private lateinit var senderUid:String
    private lateinit var receiverUid:String

    private lateinit var senderRoom:String
    private lateinit var receiverRoom:String

    private lateinit var list :ArrayList<MessageModel>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        senderUid =FirebaseAuth.getInstance().uid.toString()
        receiverUid = intent.getStringExtra("uid")!!
        senderRoom= senderUid+receiverUid
        receiverRoom= receiverUid+ senderUid

        database= FirebaseDatabase.getInstance()
        list=ArrayList()
        binding.sendButton.setOnClickListener {
            if(binding.messageText.text.isEmpty()){
                Toast.makeText(this, "Please Enter Your Message.... ",Toast.LENGTH_SHORT).show()
            }else{
                 val message = MessageModel(binding.messageText.text.toString(),senderUid,Date().time)
                //generate unique key that store message
                val randomKey = database.reference.push().key
                database.reference.child("chats")
                    .child(senderRoom).child("message").child(randomKey!!).setValue(message)
                    .addOnSuccessListener {
                        //when sender data is saved the we have to save receiver data
                         database.reference.child("chats").child(receiverRoom)
                             .child("message").child(randomKey!!).setValue(message)
                             .addOnSuccessListener {

                                 binding.messageText.text= null
                                 Toast.makeText(this,"Message sent",Toast.LENGTH_SHORT).show()
                             }
                    }
            }
        }

        database.reference.child("chats").child(senderRoom).child("message")
            .addValueEventListener(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    //remove the previous data
                   list.clear()

                    for (snapshot1 in snapshot.children){
                        val data = snapshot1.getValue(MessageModel::class.java)
                        list.add(data!!)

                    }
                    binding.recylceViewChat.adapter= ChatMessageAdapter(this@ChatActivity,list)

                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@ChatActivity,"Error :$error",Toast.LENGTH_SHORT).show()
                }
            })

    }
}