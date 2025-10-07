package com.ender.artfolio.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ender.artfolio.databinding.RecyclerRowBinding
import com.ender.artfolio.data.model.ArtsModel
import com.squareup.picasso.Picasso

class ArtsRecyclerAdapter(
    private val artsList: ArrayList<ArtsModel>,
    private val onItemClick: (ArtsModel) -> Unit
) : RecyclerView.Adapter<ArtsRecyclerAdapter.ArtsHolder>() {

    class ArtsHolder(val binding: RecyclerRowBinding) : RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtsHolder {
        val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArtsHolder(binding)
    }

    override fun onBindViewHolder(
            holder: ArtsHolder,
            position: Int
    ){
        Picasso.get().load(artsList.get(position).downloadUrl).into(holder.binding.rowImageView)
        holder.binding.rowHeaderText.text = artsList.get(position).artworkName
        holder.binding.rowArtistText.text = "Artist: " + artsList.get(position).artistName
        holder.binding.rowYearText.text = artsList.get(position).year
        holder.itemView.setOnClickListener{
            onItemClick(artsList.get(position))
        }
    }

    override fun getItemCount(): Int{
        return artsList.size
    }
}