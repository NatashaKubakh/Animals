package com.example.animals.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.animals.R
import com.example.animals.databinding.ItemAnimalBinding
import com.example.animals.model.Animal
import com.example.animals.util.getProgressDrawable
import com.example.animals.util.loadImage

class AnimalListAdapter(private val animalList: ArrayList<Animal>) :
    RecyclerView.Adapter<AnimalListAdapter.AnimalViewHolder>() {
    private lateinit var binding: ItemAnimalBinding

    fun updateAnimalList(newAnimalList: List<Animal>) {
        animalList.clear()
        animalList.addAll(newAnimalList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_animal, parent, false
        )
        return AnimalViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AnimalViewHolder, position: Int) {
        binding.animalName.text = animalList[position].name
        binding.animalImage.loadImage(animalList[position].imageUrl,
            getProgressDrawable(holder.itemView.context))
    }

    override fun getItemCount() = animalList.size


    inner class AnimalViewHolder(binding: ItemAnimalBinding) : RecyclerView.ViewHolder(binding.root)
}