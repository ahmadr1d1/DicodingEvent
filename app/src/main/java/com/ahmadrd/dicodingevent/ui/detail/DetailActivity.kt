package com.ahmadrd.dicodingevent.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.ahmadrd.dicodingevent.R
import com.ahmadrd.dicodingevent.databinding.ActivityDetailBinding
import com.bumptech.glide.Glide

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_EVENT_ID = "extra_event"
    }

    private lateinit var binding: ActivityDetailBinding
    private val viewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        binding.collapsingToolbar.title = "Detail Event"

        val dataEvent = intent.getIntExtra(EXTRA_EVENT_ID, 0)
        viewModel.getDetailEvent(dataEvent)
        observeViewModel()

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

    }

    private fun observeViewModel() {
        viewModel.detailEvent.observe(this) { detailEventResponse ->
            if (detailEventResponse != null) {
                if (detailEventResponse.event != null) {
                    with(binding) {
                        toolbar.title = detailEventResponse.event.name
                        tvEventTitle.text = detailEventResponse.event.name
                        tvEventCategory.text = detailEventResponse.event.category
                        tvEventOwner.text = getString(R.string.event_owner, detailEventResponse.event.ownerName)
                        tvEventCity.text = getString(R.string.event_city, detailEventResponse.event.cityName)
                        tvEventQuota.text = getString(R.string.event_quota, detailEventResponse.event.quota)
                        tvEventRegistered.text = getString(R.string.event_registered, detailEventResponse.event.registrants)
                        tvEventSummary.text = detailEventResponse.event.summary
                        tvEventDescription.text = HtmlCompat.fromHtml(
                            detailEventResponse.event.description.toString(),
                            HtmlCompat.FROM_HTML_MODE_LEGACY
                        )
                        tvEventBeginTime.text = getString(R.string.event_begin_time, detailEventResponse.event.beginTime)
                        tvEventEndTime.text = getString(R.string.event_end_time, detailEventResponse.event.endTime)

                        Glide.with(this@DetailActivity)
                            .load(detailEventResponse.event.mediaCover)
                            .into(imgEventCover)

                        tvEventLink.setOnClickListener {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(detailEventResponse.event.link))
                            startActivity(intent)
                        }
                    }
                }
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(this) { isError ->
            if (isError) {
                Toast.makeText(this, R.string.failed, Toast.LENGTH_SHORT).show()
            }
        }
    }
}