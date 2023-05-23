package com.dicoding.submission1intermediate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.dicoding.submission1intermediate.data.response.ListStoryItem
import com.dicoding.submission1intermediate.databinding.ActivityMainBinding
import com.dicoding.submission1intermediate.map.MapsActivity
import com.dicoding.submission1intermediate.ui.story.*
import com.dicoding.submission1intermediate.util.UserPrefData
import com.dicoding.submission1intermediate.util.UserPreference
import com.dicoding.submission1intermediate.ui.welcome.WelcomeActivity
import com.dicoding.submission1intermediate.util.LoadingStateAdapter
import com.dicoding.submission1intermediate.util.StoryPagingAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding
    private lateinit var userPreference: UserPreference
    private lateinit var storyViewModel: StoryViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        supportActionBar?.title = "Story App"

        mBinding.fab.setOnClickListener {
            startActivity(Intent(this, AddStoryActivity::class.java))
        }

//        // set span count untuk menentukan jumlah kolom
//        val spanCount = 2
//        // set GridLayoutManager dengan span count
//        mBinding.rvStories.layoutManager = GridLayoutManager(this, spanCount)

        // set span count untuk menentukan jumlah kolom
        val spanCount = 2
        // set StaggeredGridLayoutManager dengan span count
        mBinding.rvStories.layoutManager = StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL)


        userPreference = UserPreference(this)

        val data = userPreference.getSession()
        val token = "Bearer ${data.apiToken.toString()}"

        storyViewModel = ViewModelProvider(this, ViewModelFactory(this, token)).get(StoryViewModel::class.java)

        if (!data.isLogin){
            startActivity(Intent(this, WelcomeActivity::class.java))
        } else {
            val adapter = StoryPagingAdapter()

            mBinding.rvStories.adapter = adapter.withLoadStateFooter(
                footer = LoadingStateAdapter{
                    adapter.retry()
                }
            )
//            mBinding.rvStories.adapter = adapter

            storyViewModel.pagingStory.observe(this){
                adapter.submitData(lifecycle, it)
            }

        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.log_out -> {
                userPreference.setSession(UserPrefData(false, ""))
                finish()
                startActivity(Intent(this, WelcomeActivity::class.java))
            }
            R.id.maps_menu -> {
                startActivity(Intent(this, MapsActivity::class.java))
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun getStories(dataStory: List<ListStoryItem>) {
        val storyList = ArrayList<StoryDataClass>()
        for(i in dataStory){
            storyList.add(StoryDataClass(i.id, i.name, i.photoUrl))
        }
        mBinding.rvStories.adapter = StoryAdapter(storyList)
    }


    private fun showLoading(isLoading: Boolean) {
        mBinding.pbMain.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}