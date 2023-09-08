package com.ssrlab.audioguide.krokapp.rv

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ssrlab.audioguide.krokapp.MainActivity
import com.ssrlab.audioguide.krokapp.R
import com.ssrlab.audioguide.krokapp.db.objects.FavouriteObject
import com.ssrlab.audioguide.krokapp.db.objects.PointObject
import kotlinx.coroutines.launch

class PointListAdapter(
    private val pointList: ArrayList<PointObject>,
    private val favouriteList: ArrayList<FavouriteObject>,
    private val mainActivity: MainActivity,
    private val action: (PointObject, String) -> Unit
) : RecyclerView.Adapter<PointListAdapter.PointListHolder>() {

    inner class PointListHolder(item: View) : RecyclerView.ViewHolder(item)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PointListHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rv_item_list, parent, false)
        return PointListHolder(itemView)
    }

    override fun onBindViewHolder(holder: PointListHolder, position: Int) {
        val itemView = holder.itemView
        val item = pointList[position]

        val favouriteButton = itemView.findViewById<ImageButton>(R.id.rv_item_favourite)
        favouriteButton.visibility = View.VISIBLE

        for (i in favouriteList) {
            if (i.pointId == pointList[position].id) {
                favouriteButton.setImageResource(R.drawable.ic_fav_active)
            }
        }

        favouriteButton.setOnClickListener {
            mainActivity.getScope().launch {
                val favObject = mainActivity.favouriteDao().getFavourite(pointList[position].id)
                if (favObject != null) {
                    mainActivity.favouriteDao().delete(favObject)
                    favouriteButton.setImageResource(R.drawable.ic_fav_inactive)
                } else {
                    mainActivity.favouriteDao().insert(FavouriteObject(pointId = pointList[position].id))
                    favouriteButton.setImageResource(R.drawable.ic_fav_active)
                }
            }
        }

        itemView.setOnClickListener { action(item, pointList[position].name) }
        itemView.findViewById<ImageView>(R.id.rv_item_logo).load(item.logo)
        itemView.findViewById<TextView>(R.id.rv_item_title).text = item.name
    }

    override fun getItemCount() = pointList.size
}