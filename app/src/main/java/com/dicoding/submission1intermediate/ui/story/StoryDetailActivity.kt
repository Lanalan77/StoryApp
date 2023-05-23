package com.dicoding.submission1intermediate.ui.story

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dicoding.submission1intermediate.data.response.Story
import com.dicoding.submission1intermediate.databinding.ActivityStoryDetailBinding
import com.dicoding.submission1intermediate.util.UserPreference
import com.dicoding.submission1intermediate.ui.welcome.WelcomeActivity

class StoryDetailActivity : AppCompatActivity() {

    private lateinit var storyDetailBinding: ActivityStoryDetailBinding
    private lateinit var storyViewModel: StoryViewModel
    private lateinit var userPreference: UserPreference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        storyDetailBinding = ActivityStoryDetailBinding.inflate(layoutInflater)
        setContentView(storyDetailBinding.root)
        supportActionBar?.title = "Detail Story"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val id = intent.getStringExtra("key_story")
        userPreference = UserPreference(this)

        val data = userPreference.getSession()
        val token = "Bearer ${data.apiToken.toString()}"

        if (!data.isLogin){
            startActivity(Intent(this, WelcomeActivity::class.java))
        }

        storyViewModel = ViewModelProvider(this, ViewModelFactory(this, token)).get(StoryViewModel::class.java)

        if (id != null) {
            storyViewModel.getDetailStory(token, id)
        }

        storyViewModel.detailStory.observe(this) { detailStory ->
            detailedStory(detailStory)
        }

        storyViewModel.isLoading.observe(this) { loading ->
            showLoading(loading)
        }
    }

    private fun detailedStory(detailStory: Story?) {
        Glide.with(this)
            .load(detailStory?.photoUrl)
            .into(storyDetailBinding.ivItemPhoto)
        storyDetailBinding.tvItemName.text = detailStory?.name
        storyDetailBinding.tvDetailDescription.text = detailStory?.description
    }

    private fun showLoading(isLoading: Boolean) {
        storyDetailBinding.pbStoryDetail.visibility = if (isLoading) View.VISIBLE else View.GONE
    }


}