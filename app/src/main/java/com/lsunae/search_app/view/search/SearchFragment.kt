import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.lsunae.search_app.R
import com.lsunae.search_app.databinding.FragmentSearchBinding
import com.lsunae.search_app.util.OnSingleClickListener
import com.lsunae.search_app.view.base.BaseFragment
import com.lsunae.search_app.view.search.adapter.SearchResultAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(R.layout.fragment_search) {
    private lateinit var searchResultAdapter: SearchResultAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()
        setListener()
    }

    private fun setListener() {
        binding.apply {
            ivSearch.setOnClickListener(object : OnSingleClickListener() {
                override fun onSingleClick(v: View) {
                    val searchText = etSearch.text.toString()
                }
            })
        }
    }

    private fun setAdapter() {
        searchResultAdapter = SearchResultAdapter()
        binding.rvSearchResult.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = searchResultAdapter
        }
    }
}