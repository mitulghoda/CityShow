package com.app.cityshow.pagination

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.cityshow.databinding.LayoutErrorViewBinding
import com.app.cityshow.network.ResponseHandler
import java.io.IOException

internal class GridPaginationHelper<T>(
    private val mContext: Context?,
    private var errorLayoutBinding: LayoutErrorViewBinding? = null,
    private var recyclerView: RecyclerView,
    private var layoutManager: GridLayoutManager,
    private var progress: View?,
    private val paginationCallback: PaginationCallback
) {
    private val arrayList = ArrayList<T?>()
    private var isLoadingData = false
    private var canIncreasePageSize = true
    val START_PAGE_INDEX = 1
    var PAGE_INDEX = START_PAGE_INDEX

    private var errTitle: TextView? = null
    private var errMessage: TextView? = null
    private var errbutton: Button? = null

    companion object {
        const val PAGE_SIZE = 20
        const val KEY_PAGE_NUMBER = "page"
        const val KEY_PAGE_SIZE = "limit"
    }

    init {
        setPaginationRecycler()
    }

    private fun setPaginationRecycler() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                callOnScrollListener(dy)
            }
        })

        //  paginationCallback.onNewPage(PAGE_INDEX);
    }

    fun updateView(
        errorLayoutBinding: LayoutErrorViewBinding? = null,
        recyclerView: RecyclerView,
        layoutManager: GridLayoutManager,
        progress: View?,
    ) {
        this.errorLayoutBinding = errorLayoutBinding
        this.recyclerView = recyclerView
        this.layoutManager = layoutManager
        this.progress = progress

        setPaginationRecycler()
    }

    fun resetValues() {
        arrayList.clear()
        isLoadingData = false
        canIncreasePageSize = true
        PAGE_INDEX = START_PAGE_INDEX
        notifyRecyclerView()
    }

    fun setProgressLayout(visibility: Int) {
        if (progress != null) {
            if (PAGE_INDEX == START_PAGE_INDEX) {
                try {
                    progress?.visibility = visibility
//                    Util.loadGifImage(mContext, progress.findViewById(R.id.imgGif))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                progress?.visibility = View.GONE
            }
        }
    }


    private fun callOnScrollListener(dy: Int) {
        val totalItem = layoutManager.itemCount
        val prevItem = layoutManager.findFirstVisibleItemPosition()
        val currentItem = layoutManager.childCount
        //Here the right side of || operator is used because when screen is too large and all 20 items are loaded then the pagination will never called. So its helps to load more data if there are available.
        if (dy > 0 && prevItem + currentItem >= totalItem && !isLoadingData || dy == 0 && arrayList!!.size > 0 && arrayList.size == currentItem && !isLoadingData) {
            if (canIncreasePageSize) {
                isLoadingData = true
                PAGE_INDEX += 1
                paginationCallback.onNewPage(PAGE_INDEX)

            } else {
                if (arrayList!!.size > 0 && arrayList[arrayList.size - 1] != null) {
                    arrayList.add(null)
                    recyclerView.post {
//                        recyclerView.adapter?.notifyDataSetChanged()
                        notifyRecyclerView()

                    }

                }
                if (!recyclerView.canScrollVertically(1)) {
                    isLoadingData = true
                    paginationCallback.onNewPage(PAGE_INDEX)
                }
            }
        }
    }

    fun setSuccessResponse(isSuccess: Boolean, arrayList1: List<T?>?, message: String) {
        setProgressLayout(View.GONE)
        handleErrorView(View.GONE, message, View.GONE, View.GONE)

        if (isSuccess) {
            isLoadingData = false
            canIncreasePageSize = true

            if (arrayList!!.size > 0 && arrayList[arrayList.size - 1] == null)
                arrayList.removeAt(arrayList.size - 1)

            if (arrayList1 != null)
                arrayList.addAll(arrayList1)

            if (arrayList.size < PAGE_SIZE)
                isLoadingData = true
            else
                arrayList.add(null)
            notifyRecyclerView()

        } else {
            if (arrayList.size > 0 && arrayList[arrayList.size - 1] == null) {
                arrayList.removeAt(arrayList.size - 1)
                notifyRecyclerView()

//                recyclerView.adapter?.notifyItemRemoved(arrayList.size)
            }
        }

        if (PAGE_INDEX == START_PAGE_INDEX && arrayList.isEmpty()) {
            handleErrorView(View.VISIBLE, message, View.GONE, View.GONE)
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun notifyRecyclerView() {
        (recyclerView.adapter as PagedAdapter<*, T>).updateList(ArrayList(arrayList))
    }

    fun setFailureResponse(message: String) {
        setProgressLayout(View.GONE)

        if (PAGE_INDEX == START_PAGE_INDEX) handleErrorView(
            View.VISIBLE,
            message,
            View.VISIBLE,
            View.VISIBLE
        )
        else handleErrorView(View.GONE, "", View.GONE, View.GONE)
        if (arrayList!!.size > 0 && arrayList[arrayList.size - 1] == null) {
            arrayList.removeAt(arrayList.size - 1)
//            recyclerView.adapter?.notifyItemRemoved(arrayList.size)
            notifyRecyclerView()

        }
    }

    fun setFailureResponse(t: Throwable) {
        setProgressLayout(View.GONE)
        handleErrorView(View.GONE, "", View.GONE, View.GONE)

        if (t is IOException) {
            //only for internet failure we are allow users to load more data if previous data available.
            //In other cases we don't need to load more data because suppose there is parse error then we don't need to allow user to load it again.
            if (arrayList!!.size > 0) {
                canIncreasePageSize = false
                isLoadingData = false
            }

            if (PAGE_INDEX == START_PAGE_INDEX) {
                handleErrorView(
                    View.VISIBLE,
                    ResponseHandler.handleErrorResponse(t),
                    View.VISIBLE,
                    View.VISIBLE
                )
            } else {
//                AlertMessage.showMessage(mContext, ResponseHandler.handleErrorResponse(t))
                Log.e("test", ResponseHandler.handleErrorResponse(t))
            }
        } else {
            if (PAGE_INDEX == START_PAGE_INDEX) {
                handleErrorView(
                    View.VISIBLE,
                    ResponseHandler.handleErrorResponse(t),
                    View.VISIBLE,
                    View.VISIBLE
                )
            }
        }

        if (arrayList!!.size > 0 && arrayList[arrayList.size - 1] == null) {
            arrayList.removeAt(arrayList.size - 1)
//            recyclerView.adapter?.notifyItemRemoved(arrayList.size)
            notifyRecyclerView()

        }

    }

    fun handleErrorView(
        visibility: Int,
        message: String,
        btnVisibility: Int,
        titleVisibility: Int
    ) {
        if (errorLayoutBinding != null) {
            errorLayoutBinding?.txtErrorMsg?.visibility = visibility
            if (message.isNotEmpty()) errorLayoutBinding?.txtErrorMsg?.text = message
        }
    }

    interface PaginationCallback {
        fun onNewPage(pageNumber: Int)
        fun onRetryPage(pageNumber: Int) {}
    }

}
