package com.ssrlab.audioguide.krokapp.fragments.exhibit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssrlab.audioguide.krokapp.R
import com.ssrlab.audioguide.krokapp.databinding.FragmentPointListBinding
import com.ssrlab.audioguide.krokapp.db.objects.FavouriteObject
import com.ssrlab.audioguide.krokapp.db.objects.PointObject
import com.ssrlab.audioguide.krokapp.fragments.BaseFragment
import com.ssrlab.audioguide.krokapp.rv.PointListAdapter
import com.ssrlab.audioguide.krokapp.vm.ExhibitViewModel
import kotlinx.coroutines.launch

class PointListFragment: BaseFragment() {

    private lateinit var binding: FragmentPointListBinding
    private lateinit var adapter: PointListAdapter

    private val exhibitVM: ExhibitViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentPointListBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        mainActivity.setToolbar(resources.getString(R.string.points), isBackButtonShown = true, isFavouritesButtonShow = true) {
            findNavController().navigate(R.id.favouritesListFragment)
        }

        mainActivity.apply {
            getScope().launch {
                val newPointList = arrayListOf<PointObject>()
                val pointIdsList = arguments?.getIntegerArrayList("point_list")

                val pointList = pointIdsList?.let { mainActivity.pointDao().getPoints(it) }

                if (pointList != null) {
                    for (i in pointList) {
                        if (i.language == this@apply.getMainApplication().getLocaleString()) newPointList.add(i)
                    }
                }

                val favouriteList = mainActivity.favouriteDao().getFavourites()

                runOnUiThread {
                    adapter = PointListAdapter(newPointList, favouriteList as ArrayList<FavouriteObject>, mainActivity) {
                        exhibitVM.setPointObject(it)
                        findNavController().navigate(R.id.exhibitFragment)
                    }
                    binding.rvSecondary.layoutManager = LinearLayoutManager(this@apply)
                    binding.rvSecondary.adapter = adapter
                }
            }
        }
    }
}