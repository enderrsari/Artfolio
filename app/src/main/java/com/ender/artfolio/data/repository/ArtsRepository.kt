package com.ender.artfolio.data.repository

import com.ender.artfolio.data.model.ArtsModel
import com.ender.artfolio.util.FirebaseUtil
import java.util.UUID

class ArtsRepository {
    private val firestore = FirebaseUtil.firestore
    private val storage = FirebaseUtil.storage
    
    fun addArt(
        artworkName: String,
        artistName: String,
        year: String,
        imageUri: android.net.Uri?,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        if (imageUri == null) {
            onFailure(Exception("No image selected"))
            return
        }
        
        val uuid = UUID.randomUUID()
        val imageName = "$uuid.jpg"
        val reference = storage.reference
        val imageReference = reference.child("images/").child(imageName)
        
        imageReference.putFile(imageUri)
            .addOnSuccessListener {
                val uploadPictureReference = storage.reference.child("images").child(imageName)
                uploadPictureReference.downloadUrl.addOnSuccessListener { uri ->
                    val downloadUrl = uri.toString()
                    
                    val artMap = hashMapOf<String, Any>()
                    artMap["artworkName"] = artworkName
                    artMap["artistName"] = artistName
                    artMap["year"] = year
                    artMap["downloadUrl"] = downloadUrl
                    
                    firestore.collection("Arts")
                        .add(artMap)
                        .addOnSuccessListener { onSuccess() }
                        .addOnFailureListener { exception -> onFailure(exception) }
                }.addOnFailureListener { exception -> onFailure(exception) }
            }.addOnFailureListener { exception -> onFailure(exception) }
    }
    
    fun getArts(
        onSuccess: (List<ArtsModel>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        firestore.collection("Arts")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    onFailure(error)
                    return@addSnapshotListener
                }
                
                if (value != null) {
                    val artsList = mutableListOf<ArtsModel>()
                    for (document in value.documents) {
                        val artworkName = document.getString("artworkName") ?: ""
                        val artistName = document.getString("artistName") ?: ""
                        val year = document.getString("year") ?: ""
                        val downloadUrl = document.getString("downloadUrl") ?: ""
                        val docId = document.id
                        
                        if (artworkName.isNotEmpty() && artistName.isNotEmpty()) {
                            val art = ArtsModel(artworkName, artistName, year, downloadUrl, docId)
                            artsList.add(art)
                        }
                    }
                    onSuccess(artsList)
                }
            }
    }
    
    fun getArtById(
        artId: String,
        onSuccess: (ArtsModel?) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        if (artId.isEmpty()) {
            onSuccess(null)
            return
        }
        
        firestore.collection("Arts").document(artId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val artworkName = document.getString("artworkName") ?: ""
                    val artistName = document.getString("artistName") ?: ""
                    val year = document.getString("year") ?: ""
                    val downloadUrl = document.getString("downloadUrl") ?: ""
                    
                    val art = ArtsModel(artworkName, artistName, year, downloadUrl, artId)
                    onSuccess(art)
                } else {
                    onSuccess(null)
                }
            }
            .addOnFailureListener { exception -> onFailure(exception) }
    }
}