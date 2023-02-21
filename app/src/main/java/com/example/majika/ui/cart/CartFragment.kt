package com.example.majika.ui.cart

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.majika.R
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.majika.MainActivity
import com.example.majika.MajikaApplication
import com.example.majika.ui.payment.PaymentActivity
import com.example.majika.databinding.FragmentCartBinding
import com.example.majika.utils.observeOnce

class CartFragment : Fragment() {
    private var _binding: FragmentCartBinding? = null

    private val binding get() = _binding!!
    val cartViewModel: CartViewModel by viewModels { CartViewModelFactory((this.requireActivity().application as MajikaApplication).repository) }
    private lateinit var adapter: CartAdapter
    private lateinit var cartRecyclerView: RecyclerView
    override fun onStart() {
        super.onStart()
        (activity as MainActivity).setTitle(getString(R.string.title_cart))
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) : View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.payButton.setOnClickListener{
            cartViewModel.totalPrice.observeOnce(this.viewLifecycleOwner) { price ->
                if (price == 0) {
                    Toast.makeText(context, "Cart is empty!", Toast.LENGTH_SHORT).show()
                } else {
                    val intent = Intent(this.activity, PaymentActivity::class.java)
                    activity?.startActivity(intent)
                }
            }
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = CartAdapter(cartViewModel, activity as Context)
        cartRecyclerView = binding.cartRecyclerView

        cartRecyclerView.adapter = adapter
        cartRecyclerView.layoutManager = LinearLayoutManager(this.requireContext())

        cartViewModel.cartItems.observe(this.viewLifecycleOwner) { items ->
            items.let { adapter.submitList(it) }

            if (items.isEmpty()) {
                cartRecyclerView.visibility = View.GONE
                binding.paymentTab.visibility = View.GONE
                binding.cartError.visibility = View.VISIBLE
            } else {
                cartRecyclerView.visibility = View.VISIBLE
                binding.paymentTab.visibility = View.VISIBLE
                binding.cartError.visibility = View.GONE
            }
        }
        cartViewModel.totalPrice.observe(this.viewLifecycleOwner) { price ->
            binding.totalPrice.text = getString(R.string.total_cart, price)
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as? AppCompatActivity?)?.supportActionBar?.title = "Cart"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}