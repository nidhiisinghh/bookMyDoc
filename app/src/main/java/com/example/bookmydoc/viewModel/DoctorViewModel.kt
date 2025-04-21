package com.example.bookmydoc.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookmydoc.model.Doctor
import kotlinx.coroutines.delay

class DoctorViewModel : ViewModel() {
    private val _doctors = MutableLiveData<List<Doctor>>()
    val doctors: LiveData<List<Doctor>> = _doctors

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var allDoctors: List<Doctor> = listOf()

    init {
        loadDoctors()
    }

    private fun loadDoctors() {
        _isLoading.value = true
        // Simulate API call
        viewmodelscope.launch {
            // Replace with actual API call
            delay(1000)
            allDoctors = listOf(
                Doctor("1", "John Doe", "Cardiologist", 4.5f, 10),
                Doctor("2", "Jane Smith", "Dermatologist", 4.8f, 12),
                Doctor("3", "Mike Johnson", "Pediatrician", 4.2f, 8),
                Doctor("4", "Sarah Williams", "Orthopedic", 4.7f, 15),
                Doctor("5", "David Brown", "Neurologist", 4.6f, 20)
            )
            _doctors.value = allDoctors
            _isLoading.value = false
        }
    }

    fun filterDoctors(query: String) {
        if (query.isEmpty()) {
            _doctors.value = allDoctors
        } else {
            _doctors.value = allDoctors.filter {
                it.name.contains(query, ignoreCase = true) ||
                        it.specialization.contains(query, ignoreCase = true)
            }
        }
    }
}