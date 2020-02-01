package com.example.restclient.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.restclient.R

import com.example.restclient.model.AggregateByAuthority

class ShuukeiAdapter(var context: Context, var listAggregate: ArrayList<AggregateByAuthority>) : BaseAdapter() {

    class ViewHolder(row: View){
        var rName: TextView
        var male: TextView
        var female: TextView
        var ageMax19: TextView
        var ageMin20:TextView
        var genderNone:TextView
        var notAge:TextView

        init {
            rName = row.findViewById(R.id.roleName) as TextView
            male = row.findViewById(R.id.male) as TextView
            female = row.findViewById(R.id.female) as TextView
            ageMax19 = row.findViewById(R.id.ageMax19) as TextView
            ageMin20 = row.findViewById(R.id.ageMin20) as TextView
            genderNone = row.findViewById(R.id.notFull) as TextView
            notAge = row.findViewById(R.id.notAge) as TextView
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view:View?
        var viewholder:ViewHolder

        if (convertView == null){
            var layoutInflater: LayoutInflater = LayoutInflater.from(context)
            view = layoutInflater.inflate(R.layout.row_shuukei, null)
            viewholder = ViewHolder(view)
            view.tag = viewholder
        }else{
            view = convertView
            viewholder = convertView.tag as ViewHolder
        }
        var aggregateByAuthority:AggregateByAuthority = getItem(position) as AggregateByAuthority
        viewholder.rName.text =aggregateByAuthority.authorityName
        viewholder.male.text = aggregateByAuthority.countMale.toString()
        viewholder.female.text = aggregateByAuthority.countFemale.toString()
        viewholder.genderNone.text = aggregateByAuthority.countGenderNone.toString()
        viewholder.ageMax19.text = aggregateByAuthority.countAgeBeetweenZeroToNineTeen.toString()
        viewholder.ageMin20.text = aggregateByAuthority.countAgeMoreThanTwenty.toString()
        viewholder.notAge.text = aggregateByAuthority.countAgeNone.toString()

        return view as View
    }

    override fun getItem(position: Int): Any {
       return listAggregate.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return listAggregate.size
    }

}