package com.example.animals.view

import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.example.animals.BasicApp
import com.example.animals.R
import com.example.animals.databinding.FragmentListBinding
import com.example.animals.model.Animal
import com.example.animals.viewModel.ListViewModel


class ListFragment : Fragment() {
    private lateinit var binding: FragmentListBinding
    private lateinit var viewModel: ListViewModel
    private val listAdapter = AnimalListAdapter(arrayListOf())


    private val animalListDataObserver = Observer<List<Animal>> { list: List<Animal> ->
        list?.let {
            binding.animalsList.visibility = View.VISIBLE
            listAdapter.updateAnimalList(it)
        }
    }
    private val loadingLiveDataObserver = Observer<Boolean> { isLoading: Boolean ->
        binding.loadingView.visibility = if (isLoading) View.VISIBLE else View.GONE
        if (isLoading) {
            binding.tvListError.visibility = View.GONE
            binding.animalsList.visibility = View.GONE
        }
    }
    private val errorLiveDataObserver = Observer<Boolean> { isError: Boolean ->
        binding.tvListError.visibility = if (isError) View.VISIBLE else View.GONE
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /* binding.buttonDetails.setOnClickListener {
             Navigation.findNavController(it).navigate(R.id.actionGoToDetail)
         }*/
        viewModel = ViewModelProvider.AndroidViewModelFactory(BasicApp.getInstance())
            .create(ListViewModel::class.java)
        viewModel.animals.observe(viewLifecycleOwner, animalListDataObserver)
        viewModel.loading.observe(viewLifecycleOwner, loadingLiveDataObserver)
        viewModel.loadError.observe(viewLifecycleOwner, errorLiveDataObserver)
        viewModel.refresh()

        binding.animalsList.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = listAdapter
        }


    }
}