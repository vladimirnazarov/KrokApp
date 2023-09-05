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
import com.ssrlab.audioguide.krokapp.databinding.FragmentFavouritesListBinding
import com.ssrlab.audioguide.krokapp.db.objects.PointObject
import kotlinx.coroutines.launch

class FavouriteListAdapter(
    private val pointList: ArrayList<PointObject>,
    private val mainActivity: MainActivity,
    private val binding: FragmentFavouritesListBinding,
    private val action: (PointObject) -> Unit
) : RecyclerView.Adapter<FavouriteListAdapter.FavouriteListHolder>() {

    inner class FavouriteListHolder(item: View) : RecyclerView.ViewHolder(item)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteListHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rv_item_list, parent, false)
        return FavouriteListHolder(itemView)
    }

    override fun onBindViewHolder(holder: FavouriteListHolder, position: Int) {
        val itemView = holder.itemView
        val item = pointList[position]

        val favouriteButton = itemView.findViewById<ImageButton>(R.id.rv_item_favourite)
        favouriteButton.visibility = View.VISIBLE
        favouriteButton.setImageResource(R.drawable.ic_fav_active)

        favouriteButton.setOnClickListener {
            mainActivity.getScope().launch {
                val favObject = mainActivity.favouriteDao().getFavourite(item.id)
                if (favObject != null) {
                    pointList.removeAt(position)
                    mainActivity.favouriteDao().delete(favObject)
                    mainActivity.runOnUiThread {
                        notifyItemRemoved(position)
                        notifyItemRangeChanged(position, pointList.size)
                    }

                    if (mainActivity.favouriteDao().getFavourites().isEmpty()) {
                        mainActivity.runOnUiThread {
                            binding.rvFavsYouHavent.visibility = View.VISIBLE
                        }
                    }
                    else {
                        mainActivity.runOnUiThread {
                            binding.rvFavsYouHavent.visibility = View.GONE
                        }
                    }
                }
            }
        }

        itemView.setOnClickListener { action(item) }
        itemView.findViewById<ImageView>(R.id.rv_item_logo).load(item.logo)
        itemView.findViewById<TextView>(R.id.rv_item_title).text = item.name
    }

    override fun getItemCount() = pointList.size
}