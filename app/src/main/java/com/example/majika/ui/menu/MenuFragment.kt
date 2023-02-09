package com.example.majika.ui.menu

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater

import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
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
    private var fragmentMenuData: List<Menu> = emptyList()

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
        val menuRv: RecyclerView = binding.menuRv
        menuViewModel.menuList.observe(viewLifecycleOwner) {
            menuRv.adapter = MenuAdapter(it.menuList!!, activity as Context, cartViewModel)
            fragmentMenuData = it.menuList
        }



        val menuSearch: SearchView = binding.menuSearch
        menuSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val filteredList = fragmentMenuData.filter {
                    it.name!!.contains(newText!!, true)
                }
                menuRv.adapter = MenuAdapter(filteredList, activity as Context, cartViewModel)
                return false
            }
        })
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}