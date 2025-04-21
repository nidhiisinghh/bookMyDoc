package com.example.bookmydoc.view

import android.content.Intent
import android.hardware.biometrics.BiometricManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.bookmydoc.R
import com.example.bookmydoc.databinding.ActivityMainBinding
import com.example.bookmydoc.model.Doctor
import com.example.bookmydoc.viewModel.DoctorAdapter
import com.example.bookmydoc.viewModel.DoctorViewModel
import java.util.concurrent.Executor

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: DoctorViewModel
    private lateinit var doctorAdapter: DoctorAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(DoctorViewModel::class.java)

        setupToolbar()
        setupRecyclerView()
        setupSearch()
        observeViewModel()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = "Available Doctors"
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun setupRecyclerView() {
        doctorAdapter = DoctorAdapter { doctor ->
            navigateToDoctorDetails(doctor)
        }
        binding.doctorRecyclerView.adapter = doctorAdapter
    }

    private fun setupSearch() {
        binding.searchEditText.addTextChangedListener { text ->
            viewModel.filterDoctors(text.toString())
        }
    }

    private fun observeViewModel() {
        viewModel.doctors.observe(this) { doctors ->
            doctorAdapter.submitList(doctors)
            binding.emptyState.visibility = if (doctors.isEmpty()) View.VISIBLE else View.GONE
            binding.doctorRecyclerView.visibility = if (doctors.isEmpty()) View.GONE else View.VISIBLE
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun navigateToDoctorDetails(doctor: Doctor) {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("doctor_id", doctor.id)
            putExtra("doctor_name", doctor.name)
            putExtra("doctor_specialization", doctor.specialization)
            putExtra("doctor_rating", doctor.rating)
            putExtra("doctor_experience", doctor.experience)
        }
        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}