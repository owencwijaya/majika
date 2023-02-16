package com.example.majika.ui.branch

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.majika.databinding.FragmentBranchBinding
import com.example.majika.ui.cart.CartAdapter

class BranchFragment : Fragment() {

    private var _binding: FragmentBranchBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: BranchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "Branch"
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBranchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val branchRv: RecyclerView = binding.branchRv
        val branchViewModel = ViewModelProvider(this).get(BranchViewModel::class.java)
        adapter = BranchAdapter(this.requireContext())
        branchRv.layoutManager = LinearLayoutManager(this.requireContext())
        branchRv.adapter = adapter

        branchViewModel.branchList.observe(viewLifecycleOwner) {
            adapter.setBranchList(it.branchList!!)
            binding.branchCount.text = adapter.itemCount.toString() + " branches"
        }
        branchViewModel.getBranches()
    }
}