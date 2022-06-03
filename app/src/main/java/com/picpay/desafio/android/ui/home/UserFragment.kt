package com.picpay.desafio.android.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.picpay.desafio.android.databinding.FragmentUserBinding
import com.picpay.desafio.android.model.User
import kotlinx.coroutines.launch


class UserFragment : Fragment() {

    private lateinit var binding: FragmentUserBinding
    private val viewModel by viewModels<UserViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
        getList()
    }
    private fun getList(){
        viewModel.getUsers()
    }

    private fun setObservers() {
        lifecycleScope.launch {
            viewModel.state.observe(requireActivity()){ state ->
                when (state) {
                    is UserListState.Success -> setRecycler(state.users)
                    is UserListState.Error -> renderError(state.message)
                    is UserListState.UserLoading -> renderLoading(state.isLoading)
                }
            }
        }
    }

    private fun renderLoading(isLoading: Boolean) = with(binding){
        recyclerView.isVisible = isLoading.not()
        title.isVisible = isLoading.not()
        userListProgressBar.isVisible = isLoading
    }

    private fun renderError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun setRecycler(users: List<User>) = with(binding) {
        recyclerView.apply {
            adapter = UserListAdapter(users)
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
}