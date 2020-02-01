package com.example.restclient.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.restclient.R

import com.example.restclient.model.User
import kotlinx.android.synthetic.main.row_ichiran.view.*

class UserAdapter(var context: Context, var listUser: ArrayList<User>): BaseAdapter() {
    class ViewHolder(row: View){
        var sNo:TextView
        var userid:TextView
        var username:TextView
        var authorityname:TextView

        init {
            sNo = row.findViewById(R.id.stt) as TextView
            userid = row.findViewById(R.id.userid) as TextView
            username = row.findViewById(R.id.username) as TextView
            authorityname = row.findViewById(R.id.authorityname) as TextView
        }
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view:View?
        var viewholder:ViewHolder

        if (convertView == null){
            var layoutInflater:LayoutInflater = LayoutInflater.from(context)
            view = layoutInflater.inflate(R.layout.row_ichiran, null)
            viewholder = ViewHolder(view)
            view.tag = viewholder
        }else{
            view = convertView
            viewholder = convertView.tag as ViewHolder
        }
        var user:User = getItem(position) as User
        viewholder.sNo.text =( position + 1).toString()
        viewholder.userid.text = user.userId
        viewholder.username.text = user.familyName + user.firstName
        if(user.admin == 1)
            viewholder.authorityname.text = "* " + user.authorityName
        else
            viewholder.authorityname.text = user.authorityName

        var animation:Animation = AnimationUtils.loadAnimation(context, R.anim.scale_list)
        view?.startAnimation(animation)

        return view as View
    }

    override fun getItem(position: Int): Any {
        return listUser.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return listUser.size
    }
}