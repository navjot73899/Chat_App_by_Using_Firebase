package com.example.chatapplication.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatapplication.Activity.ChatActivity
import com.example.chatapplication.R
import com.example.chatapplication.databinding.ChatUserItemLayoutBinding
import com.example.chatapplication.modelClass.UserModel

class chatAdapter(var context:Context, var list: ArrayList<UserModel>) :RecyclerView.Adapter<chatAdapter.ViewHolder>(){
   inner class ViewHolder (view: View):RecyclerView.ViewHolder(view){
        var binding :ChatUserItemLayoutBinding = ChatUserItemLayoutBinding.bind(view)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.chat_user_item_layout,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var user= list[position]
        Glide.with(context).load(user.imageUrl).into(holder.binding.circlechatView)
        holder.binding.userChatName.text= user.name

          holder.itemView.setOnClickListener {
               val intent= Intent(context,ChatActivity::class.java)
              intent.putExtra("uid",user.vid)
              context.startActivity(intent)
          }
    }

    override fun getItemCount(): Int {
        return list.size
    }


}