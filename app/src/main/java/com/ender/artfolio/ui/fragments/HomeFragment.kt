package com.ender.artfolio.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ender.artfolio.R
import com.ender.artfolio.ui.activities.MainActivity
import com.ender.artfolio.ui.adapters.ArtsRecyclerAdapter
import com.ender.artfolio.databinding.FragmentHomeBinding
import com.ender.artfolio.data.model.ArtsModel
import com.ender.artfolio.features.arts.ArtsViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class HomeFragment : Fragment(), MenuProvider {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var viewModel: ArtsViewModel
    private lateinit var adapter: ArtsRecyclerAdapter
    private lateinit var artsList: ArrayList<ArtsModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
        viewModel = ViewModelProvider(this)[ArtsViewModel::class.java]

        artsList = ArrayList<ArtsModel>()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerRow.layoutManager = LinearLayoutManager(requireContext())

        adapter = ArtsRecyclerAdapter(artsList) { selected ->
            val action = HomeFragmentDirections
                .actionHomeFragmentToAddArtFragment(isViewing = true, itemId = selected.id)
            findNavController().navigate(action)
        }
        binding.recyclerRow.adapter = adapter

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        
        observeViewModel()
        viewModel.loadArts()
    }

    private fun observeViewModel() {
        viewModel.artsList.observe(viewLifecycleOwner) { arts ->
            artsList.clear()
            artsList.addAll(arts)
            adapter.notifyDataSetChanged()
        }
        
        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_choices, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.addArt -> {
                val action = HomeFragmentDirections.actionHomeFragmentToAddArtFragment(isViewing = false, itemId = "")
                Navigation.findNavController(requireView()).navigate(action)
                true
            }
            R.id.signOut -> {
                auth.signOut()
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
                true
            }
            else -> false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}