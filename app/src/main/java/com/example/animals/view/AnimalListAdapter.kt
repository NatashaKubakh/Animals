package com.example.animals.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.animals.R
import com.example.animals.databinding.ItemAnimalBinding
import com.example.animals.model.Animal
import com.example.animals.util.getProgressDrawable
import com.example.animals.util.loadImage

class AnimalListAdapter(private val animalList: ArrayList<Animal>) :
    RecyclerView.Adapter<AnimalListAdapter.AnimalViewHolder>(), AnimalClickListener {
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
        holder.binding.animal = animalList[position]
        holder.binding.listener = this
        /* binding.animalImage.loadImage(
             animalList[position].imageUrl,
             getProgressDrawable(holder.itemView.context)*/

        /* binding.animalLayout.setOnClickListener {
             val action = ListFragmentDirections.actionGoToDetail(animalList[position])
             val navigation = Navigation.findNavController(binding.animalLayout)
             navigation?.let { it.navigate(action) }

         }*/
        holder.binding.executePendingBindings()
    }

    override fun onClick(v: View) {
        for (animal in animalList) {
            if (v.tag == animal.name) {
                val action = ListFragmentDirections.actionGoToDetail(animal)
                val navigation = Navigation.findNavController(v)
                navigation?.let { it.navigate(action) }
            }
        }
    }

    override fun getItemCount() = animalList.size


    inner class AnimalViewHolder(val binding: ItemAnimalBinding) : RecyclerView.ViewHolder(binding.root)
}