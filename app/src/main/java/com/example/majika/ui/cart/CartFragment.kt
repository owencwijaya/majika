package com.example.majika.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.majika.DataRepository
import com.example.majika.MajikaApplication
import com.example.majika.databinding.FragmentCartBinding
import com.example.majika.db.RoomConfig
import com.example.majika.db.dao.CartItemDao
import com.example.majika.db.entity.CartItemEntity

class CartFragment : Fragment() {
    private var _binding: FragmentCartBinding? = null

    private val binding get() = _binding!!
    val cartViewModel: CartViewModel by viewModels { CartViewModelFactory((this.requireActivity().application as MajikaApplication).repository) }
    private lateinit var adapter: CartAdapter
    private lateinit var cartRecyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) : View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = CartAdapter()
        cartRecyclerView = binding.cartRecyclerView
        cartRecyclerView.adapter = adapter
        cartRecyclerView.layoutManager = LinearLayoutManager(this.requireContext());
        cartViewModel.cartItems.observe(this.viewLifecycleOwner) { items ->
            items.let { adapter.submitList(it) }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}