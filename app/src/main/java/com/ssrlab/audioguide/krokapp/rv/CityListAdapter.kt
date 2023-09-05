package com.ssrlab.audioguide.krokapp.rv

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ssrlab.audioguide.krokapp.MainActivity
import com.ssrlab.audioguide.krokapp.R
import com.ssrlab.audioguide.krokapp.db.objects.CityObject

class CityListAdapter(
    private val list: ArrayList<CityObject>,
    private val mainActivity: MainActivity,
    private val action: (List<Int>) -> Unit
) : RecyclerView.Adapter<CityListAdapter.CityListHolder>() {

    inner class CityListHolder(item: View) : RecyclerView.ViewHolder(item)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityListHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rv_item_list, parent, false)
        return CityListHolder(itemView)
    }

    override fun onBindViewHolder(holder: CityListHolder, position: Int) {
        val itemView = holder.itemView
        val item = list[position]

        if (position < 6) itemView.findViewById<ConstraintLayout>(R.id.rv_item_holder).background = ContextCompat.getDrawable(mainActivity, R.drawable.background_rounded_orange_item)
        else itemView.findViewById<ConstraintLayout>(R.id.rv_item_holder).background = ContextCompat.getDrawable(mainActivity, R.drawable.background_rounded_item)

        itemView.setOnClickListener { action(item.points) }
        itemView.findViewById<ImageView>(R.id.rv_item_logo).load(item.logo)
        itemView.findViewById<TextView>(R.id.rv_item_title).text = item.name
    }

    override fun getItemCount() = list.size
}