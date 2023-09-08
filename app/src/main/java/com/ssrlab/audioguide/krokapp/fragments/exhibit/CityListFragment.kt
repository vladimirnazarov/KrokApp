package com.ssrlab.audioguide.krokapp.fragments.exhibit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssrlab.audioguide.krokapp.R
import com.ssrlab.audioguide.krokapp.databinding.FragmentCityListBinding
import com.ssrlab.audioguide.krokapp.db.objects.CityObject
import com.ssrlab.audioguide.krokapp.fragments.BaseFragment
import com.ssrlab.audioguide.krokapp.rv.CityListAdapter
import kotlinx.coroutines.launch

class CityListFragment: BaseFragment() {

    private lateinit var binding: FragmentCityListBinding
    private lateinit var adapter: CityListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentCityListBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        mainActivity.setToolbar(resources.getString(R.string.app_name), isFavouritesButtonShow = true) {
            findNavController().navigate(R.id.favouritesListFragment)
        }

        mainActivity.apply {
            getScope().launch {
                val list = mainActivity.cityDao().getCities()
                val newList = arrayListOf<CityObject>()

                for (i in list) if (i.language == this@apply.getMainApplication().getLocaleString()) newList.add(i)

                runOnUiThread {
                    adapter = CityListAdapter(newList, mainActivity) { list, title ->
                        val pointList = ArrayList<Int>()

                        for (i in list) pointList.add(i)

                        val bundle = Bundle()
                        bundle.putIntegerArrayList("point_list", pointList)
                        bundle.putString("point_list_title", title)

                        findNavController().navigate(R.id.pointListFragment, bundle)
                    }
                    binding.rvMain.layoutManager = LinearLayoutManager(this@apply)
                    binding.rvMain.adapter = adapter
                }
            }
        }
    }
}