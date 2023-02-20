package com.example.majika.ui.branch

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
import com.example.majika.MainActivity
import com.example.majika.R
import com.example.majika.databinding.FragmentBranchBinding

class BranchFragment : Fragment() {

    private var _binding: FragmentBranchBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: BranchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            if (adapter.itemCount == 0) {
                binding.branchRv.visibility = View.GONE
                binding.branchError.visibility = View.VISIBLE
            } else {
                binding.branchRv.visibility = View.VISIBLE
                binding.branchError.visibility = View.GONE
            }
            binding.branchCount.text = getString(R.string.branches_count, it.branchList.size)
        }
        branchViewModel.getBranches()
    }

    override fun onStart(){
        super.onStart()
        (activity as MainActivity).setTitle(getString(R.string.title_branch))
    }
}