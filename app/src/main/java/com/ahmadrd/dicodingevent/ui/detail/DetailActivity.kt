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
import com.ahmadrd.dicodingevent.data.local.entity.FavoriteEvents
import com.ahmadrd.dicodingevent.databinding.ActivityDetailBinding
import com.ahmadrd.dicodingevent.ui.favorite.FavoriteViewModel
import com.ahmadrd.dicodingevent.ui.utils.HelperTime
import com.ahmadrd.dicodingevent.ui.utils.ViewModelFactory
import com.bumptech.glide.Glide
import com.ahmadrd.dicodingevent.ui.utils.Result

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_EVENT_ID = "extra_event"
    }

    private lateinit var binding: ActivityDetailBinding
    private val viewModel: DetailViewModel by viewModels()
    private var favoriteEvent: FavoriteEvents? = null
    private var ivFavoriteState = false
    private lateinit var favoriteViewModel: FavoriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val dataEvent = intent.getIntExtra(EXTRA_EVENT_ID, 0)
        viewModel.getDetailEvent(dataEvent)
        observeViewModel()

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val factory = ViewModelFactory.getInstance(application)
        favoriteViewModel = viewModels<FavoriteViewModel> { factory }.value

        favoriteViewModel.getDetailFavoriteUser(dataEvent.toString())
        favoriteViewModel.result.observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                    }

                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(
                            this,
                            "Something went wrong " + result.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        binding.ivFavorite.setOnClickListener {
            val event = favoriteEvent
            if (event != null) {
                if (!ivFavoriteState) {
                    val fav = FavoriteEvents(
                        eventId = event.eventId,
                        title = event.title,
                        description = event.description,
                        mediaCover = event.mediaCover
                    )
                    favoriteViewModel.insertUser(fav)
                    showToast(getString(R.string.add))
                } else {
                    favoriteViewModel.deleteByEventId(event.eventId!!)
                    showToast(getString(R.string.delete))
                }

                // Refresh status favorit
                favoriteViewModel.isEventFavorited(event.eventId!!).observe(this) { favorites ->
                    val isFavorited = favorites.isNotEmpty()
                    ivFavoriteState = isFavorited
                    updateFavoriteIcon(isFavorited)
                }
            }
        }

    }

    private fun observeViewModel() {
        viewModel.detailEvent.observe(this) { detailEventResponse ->
            if (detailEventResponse != null) {
                if (detailEventResponse.event != null) {
                    with(binding) {
                        tvEventTitle.text = detailEventResponse.event.name
                        tvEventCategory.text = detailEventResponse.event.category
                        tvEventOwner.text = getString(R.string.event_owner, detailEventResponse.event.ownerName)
                        tvEventQuota.text = getString(R.string.event_quota, detailEventResponse.event.quota)
                        tvEventRegistered.text = getString(R.string.event_registered_text, detailEventResponse.event.registrants)
                        tvEventDescription.text = HtmlCompat.fromHtml(
                            detailEventResponse.event.description.toString(),
                            HtmlCompat.FROM_HTML_MODE_LEGACY
                        )
                        val beginHelperTime = HelperTime.formatBeginTime(detailEventResponse.event.beginTime!!)
                        tvEventBeginTime.text = beginHelperTime

                        Glide.with(this@DetailActivity)
                            .load(detailEventResponse.event.mediaCover)
                            .into(imgEventCover)

                        btnRegister.setOnClickListener {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(detailEventResponse.event.link))
                            startActivity(intent)
                        }
                    }
                    favoriteEvent = FavoriteEvents(
                        eventId = detailEventResponse.event.id,
                        title = detailEventResponse.event.name,
                        description = detailEventResponse.event.summary,
                        mediaCover = detailEventResponse.event.mediaCover
                    )

                    favoriteViewModel.isEventFavorited(detailEventResponse.event.id!!).observe(this) { favorites ->
                        val isFavorited = favorites.isNotEmpty()
                        ivFavoriteState = isFavorited
                        updateFavoriteIcon(isFavorited)
                    }
                }

            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.scrollView.visibility = if (isLoading) View.INVISIBLE else View.VISIBLE
        }

        viewModel.error.observe(this) { isError ->
            if (isError) {
                binding.scrollView.visibility = View.INVISIBLE
                Toast.makeText(this, R.string.failed, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun updateFavoriteIcon(isFav: Boolean) {
        binding.ivFavorite.setImageResource(
            if (isFav) R.drawable.baseline_favorite_24
            else R.drawable.baseline_favorite_border_24
        )
    }

}