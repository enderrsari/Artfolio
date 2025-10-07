package com.ender.artfolio

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
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ender.artfolio.activities.MainActivity
import com.ender.artfolio.adapter.ArtsRecyclerAdapter
import com.ender.artfolio.databinding.FragmentHomeBinding
import com.ender.artfolio.models.artsModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.Query


class HomeFragment : Fragment(), MenuProvider {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var adapter: ArtsRecyclerAdapter
    private lateinit var artsList: ArrayList<artsModel>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
        db = Firebase.firestore

        artsList = ArrayList<artsModel>()


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
        getData()

    }

    private fun getData() {

        db.collection("Arts")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Toast.makeText(requireContext(), error.localizedMessage, Toast.LENGTH_LONG)
                        .show()
                } else {
                    if (value != null) {
                        if (!value.isEmpty) {
                            val documents = value.documents

                            artsList.clear()

                            for (document in documents) {
                                // Safe casting with null checks
                                val artworkName = document.getString("artworkName") ?: ""
                                val artistName = document.getString("artistName") ?: ""
                                val year = document.getString("year") ?: ""
                                val downloadUrl = document.getString("downloadUrl") ?: ""
                                val docId = document.id

                                // Only add if essential fields are not empty
                                if (artworkName.isNotEmpty() && artistName.isNotEmpty()) {
                                    val arts = artsModel(artworkName, artistName, year, downloadUrl, docId)
                                    artsList.add(arts)
                                }
                            }
                            adapter.notifyDataSetChanged()
                        }
                    }

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