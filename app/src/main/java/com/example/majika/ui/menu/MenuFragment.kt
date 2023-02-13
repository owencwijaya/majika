package com.example.majika.ui.menu

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater

import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.room.RoomDatabase
import com.example.majika.MajikaApplication
import com.example.majika.databinding.FragmentMenuBinding
import com.example.majika.db.RoomConfig
import com.example.majika.model.Menu
import com.example.majika.model.MenuList
import com.example.majika.ui.cart.CartViewModel
import com.example.majika.ui.cart.CartViewModelFactory

class MenuFragment : Fragment() {

    private var _binding: FragmentMenuBinding? = null
    val cartViewModel: CartViewModel by viewModels { CartViewModelFactory((this.requireActivity().application as MajikaApplication).repository) }
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var fragmentFoodData: List<Menu> = emptyList()
    private var fragmentDrinksData: List<Menu> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val menuViewModel =
            ViewModelProvider(this).get(MenuViewModel::class.java)

//        set view in the activity
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        recycle view for menu
        val foodRv: RecyclerView = binding.foodRv
        menuViewModel.foodList.observe(viewLifecycleOwner) {
            foodRv.adapter = MenuAdapter(it.menuList!!, activity as Context, cartViewModel)
            fragmentFoodData = it.menuList
        }

        val drinksRv: RecyclerView = binding.drinksRv
        menuViewModel.drinksList.observe(viewLifecycleOwner) {
            drinksRv.adapter = MenuAdapter(it.menuList!!, activity as Context, cartViewModel)
            fragmentDrinksData = it.menuList
        }

        val menuSearch: SearchView = binding.menuSearch
        menuSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val filteredFoodList = fragmentFoodData.filter {
                    it.name!!.contains(newText!!, true)
                }
                foodRv.adapter = MenuAdapter(filteredFoodList, activity as Context, cartViewModel)

                val filteredDrinksList = fragmentDrinksData.filter {
                    it.name!!.contains(newText!!, true)
                }
                drinksRv.adapter = MenuAdapter(filteredDrinksList, activity as Context, cartViewModel)

                return false
            }
        })
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.setTitle("Menu")
    }
}