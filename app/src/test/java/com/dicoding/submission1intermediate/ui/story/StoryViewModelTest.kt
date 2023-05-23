package com.dicoding.submission1intermediate.ui.story

import android.os.Build.VERSION_CODES.S
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.dicoding.submission1intermediate.DataDummy
import com.dicoding.submission1intermediate.MainDispatcherRule
import com.dicoding.submission1intermediate.data.UserStoryRepository
import com.dicoding.submission1intermediate.getOrAwaitValue
import com.dicoding.submission1intermediate.room.UserStoryEntity
import com.dicoding.submission1intermediate.util.StoryPagingAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class StoryViewModelTest{
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var userStoryRepository: UserStoryRepository


    @Test
    fun `when Get Story Should Not Null and Return Data`() = runTest {
        val dummyQuote = DataDummy.generateDummyQuoteResponse()
        val data: PagingData<UserStoryEntity> = StoryPagingSource.snapshot(dummyQuote)
        val expectedQuote = MutableLiveData<PagingData<UserStoryEntity>>()
        expectedQuote.value = data
        Mockito.`when`(userStoryRepository.getAllStory()).thenReturn(expectedQuote)

        val storyViewModel = StoryViewModel(userStoryRepository)
        val actualQuote: PagingData<UserStoryEntity> = storyViewModel.pagingStory.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryPagingAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualQuote)

        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyQuote.size, differ.snapshot().size)
        Assert.assertEquals(dummyQuote[0], differ.snapshot()[0])
    }

    @Test
    fun `when Get Story Empty Should Return No Data`() = runTest {
        val data: PagingData<UserStoryEntity> = PagingData.from(emptyList())
        val expectedQuote = MutableLiveData<PagingData<UserStoryEntity>>()
        expectedQuote.value = data
        Mockito.`when`(userStoryRepository.getAllStory()).thenReturn(expectedQuote)
        val storyViewModel = StoryViewModel(userStoryRepository)
        val actualQuote: PagingData<UserStoryEntity> = storyViewModel.pagingStory.getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryPagingAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualQuote)
        Assert.assertEquals(0, differ.snapshot().size)
    }

}

class StoryPagingSource : PagingSource<Int, LiveData<List<UserStoryEntity>>>() {
    companion object {
        fun snapshot(items: List<UserStoryEntity>): PagingData<UserStoryEntity> {
            return PagingData.from(items)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<UserStoryEntity>>>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<UserStoryEntity>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }

}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}