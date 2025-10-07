package com.ender.artfolio.ui.fragments

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ender.artfolio.R
import com.ender.artfolio.databinding.FragmentAddArtBinding
import com.ender.artfolio.features.arts.ArtsViewModel
import com.google.android.material.snackbar.Snackbar

class AddArtFragment : Fragment() {
    private var _binding: FragmentAddArtBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ArtsViewModel
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    var selectedPicture: Uri? = null
    private val args: AddArtFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[ArtsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddArtBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (args.isViewing) {
            // View mode
            binding.addArtworkButton.visibility = View.INVISIBLE
            binding.imageSelector.visibility = View.INVISIBLE
            binding.placeHolder.visibility = View.VISIBLE
            binding.artworkText.isFocusable = false
            binding.artistText.isFocusable = false
            binding.yearText.isFocusable = false
            binding.artworkText.background = ContextCompat.getDrawable(requireContext(), R.drawable.add_art_custom_input)
            binding.artistText.background = ContextCompat.getDrawable(requireContext(), R.drawable.add_art_custom_input)
            binding.yearText.background = ContextCompat.getDrawable(requireContext(), R.drawable.add_art_custom_input)

            // Load data
            loadArtData(args.itemId)
        } else {
            // Add mode
            binding.addArtworkButton.visibility = View.VISIBLE
            binding.imageSelector.visibility = View.VISIBLE
            binding.placeHolder.visibility = View.GONE
            binding.artworkText.isEnabled = true
            binding.artistText.isEnabled = true
            binding.yearText.isEnabled = true
            
            binding.imageSelector.setOnClickListener{
                openGallery()
            }
    
            binding.placeHolder.setOnClickListener{
                openGallery()
            }
        }

        binding.addArtworkButton.setOnClickListener{
            val artworkName = binding.artworkText.text.toString()
            val artistName = binding.artistText.text.toString()
            val year = binding.yearText.text.toString()
            
            viewModel.addArt(
                artworkName = artworkName,
                artistName = artistName,
                year = year,
                imageUri = selectedPicture,
                onSuccess = {
                    val action = AddArtFragmentDirections.actionAddArtFragmentToHomeFragment()
                    findNavController().navigate(action)
                },
                onFailure = { exception ->
                    Toast.makeText(requireContext(), exception.localizedMessage, Toast.LENGTH_LONG).show()
                }
            )
        }

        registerLauncher()
    }

    private fun openGallery(){
        // Go to gallery
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
        
        if(ContextCompat.checkSelfPermission(requireContext(), permission) != PackageManager.PERMISSION_GRANTED){
            // If permission is not granted, decide whether to show snackbar
            if(ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), permission)){
                Snackbar.make(binding.root, "Permission needed for gallery!", Snackbar.LENGTH_INDEFINITE).setAction("Give Permission"){
                    permissionLauncher.launch(permission)
                }.show()
            }else{
                // Permission is granted, give access to gallery
                permissionLauncher.launch(permission)
            }
        }else{
            // If permission was granted in the initial check, you should give access directly
            val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            activityResultLauncher.launch(intentToGallery)
        }
    }

    private fun registerLauncher(){
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode == RESULT_OK){
                val intentFromResult = result.data
                if(intentFromResult != null){
                    selectedPicture = intentFromResult.data
                    selectedPicture?.let{
                        binding.placeHolder.setImageURI(it)
                        binding.placeHolder.visibility = View.VISIBLE
                        binding.imageSelector.visibility = View.INVISIBLE
                    }
                }
            }
        }

        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){ result ->
            if(result){
                val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            }else{
                // Permission denied
                Toast.makeText(requireContext(), "Permission needed!", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun loadArtData(itemId: String) {
        if (itemId.isNotEmpty()) {
            viewModel.getArtById(
                artId = itemId,
                onSuccess = { art ->
                    if (art != null) {
                        binding.artworkText.setText(art.artworkName)
                        binding.artistText.setText(art.artistName)
                        binding.yearText.setText(art.year)
                        
                        if (art.downloadUrl.isNotEmpty()) {
                            com.squareup.picasso.Picasso.get()
                                .load(art.downloadUrl)
                                .placeholder(R.drawable.ic_launcher_background)
                                .error(R.drawable.ic_launcher_background)
                                .into(binding.placeHolder)
                        }
                    }
                },
                onFailure = { exception ->
                    Toast.makeText(requireContext(), "Error: ${exception.localizedMessage}", Toast.LENGTH_LONG).show()
                }
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}