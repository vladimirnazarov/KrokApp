package com.ssrlab.audioguide.krokapp.fragments.exhibit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssrlab.audioguide.krokapp.R
import com.ssrlab.audioguide.krokapp.databinding.FragmentFavouritesListBinding
import com.ssrlab.audioguide.krokapp.db.objects.PointObject
import com.ssrlab.audioguide.krokapp.fragments.BaseFragment
import com.ssrlab.audioguide.krokapp.rv.FavouriteListAdapter
import com.ssrlab.audioguide.krokapp.vm.ExhibitViewModel
import kotlinx.coroutines.launch

class FavouritesListFragment: BaseFragment() {

    private lateinit var binding: FragmentFavouritesListBinding
    private lateinit var adapter: FavouriteListAdapter

    private val exhibitVM: ExhibitViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentFavouritesListBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        mainActivity.setToolbar(resources.getString(R.string.favourites), isBackButtonShown = true, isFavouritesButtonShow = true, R.drawable.ic_fav_active)

        mainActivity.apply {
            getScope().launch {
                val pointList = mainActivity.pointDao().getPoints()
                val favouriteList = mainActivity.favouriteDao().getFavourites()
                val newPointList = arrayListOf<PointObject>()

                runOnUiThread {
                    if (favouriteList.isEmpty()) binding.rvFavsYouHavent.visibility = View.VISIBLE
                    else binding.rvFavsYouHavent.visibility = View.GONE
                }

                for (i in pointList) {
                    for (j in favouriteList) {
                        if (i.language == this@apply.getMainApplication().getLocaleString() && i.id == j.pointId) newPointList.add(i)
                    }
                }

                runOnUiThread {
                    adapter = FavouriteListAdapter(newPointList, mainActivity, binding) {
                        exhibitVM.apply {
                            setPointObject(it)
                            setButtonValue(true)
                        }
                        findNavController().navigate(R.id.exhibitFragment)
                    }
                    binding.rvFavs.layoutManager = LinearLayoutManager(this@apply)
                    binding.rvFavs.adapter = adapter
                }
            }
        }
    }
}