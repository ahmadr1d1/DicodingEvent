package com.ahmadrd.dicodingevent.ui.settings

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.ahmadrd.dicodingevent.databinding.FragmentSettingsBinding
import com.ahmadrd.dicodingevent.ui.utils.NotificationWorker
import com.ahmadrd.dicodingevent.ui.utils.SettingPreferences
import com.ahmadrd.dicodingevent.ui.utils.ViewModelFactory
import com.ahmadrd.dicodingevent.ui.utils.dataStore
import java.util.concurrent.TimeUnit


class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pref = SettingPreferences.getInstance(requireContext().dataStore)
        val factory = ViewModelFactory.getInstance(pref) // pref = SettingPreferences instance
        val viewModel: SettingsViewModel by viewModels { factory }

        viewModel.getThemeSettings().observe(requireActivity()) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.switchDarkMode.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.switchDarkMode.isChecked = false
            }
        }

        binding.switchDarkMode.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            viewModel.saveThemeSetting(isChecked)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    100
                )
            }
        }

        val dailyReminder = binding.switchDailyReminder
        dailyReminder.setOnCheckedChangeListener(null) // Hindari pemicu ulang
        viewModel.getNotificationSetting().observe(viewLifecycleOwner) { isEnabled ->
            dailyReminder.isChecked = isEnabled
        }

        dailyReminder.setOnCheckedChangeListener { _, isChecked ->
            viewModel.saveNotificationSetting(isChecked)

            if (isChecked) {
                // Aktifkan worker
                val workRequest = PeriodicWorkRequestBuilder<NotificationWorker>(1, TimeUnit.DAYS)
                    .build()
                WorkManager.getInstance(requireContext()).enqueueUniquePeriodicWork(
                    "EventNotification",
                    ExistingPeriodicWorkPolicy.KEEP,
                    workRequest
                )
            } else {
                // Nonaktifkan worker
                WorkManager.getInstance(requireContext()).cancelUniqueWork("EventNotification")
            }
        }

        binding.btnTestNotification.setOnClickListener {
            val testWorkRequest = OneTimeWorkRequestBuilder<NotificationWorker>().build()
            WorkManager.getInstance(requireContext()).enqueue(testWorkRequest)

            Toast.makeText(requireContext(), "Worker dipanggil", Toast.LENGTH_SHORT).show()
        }
    }

}