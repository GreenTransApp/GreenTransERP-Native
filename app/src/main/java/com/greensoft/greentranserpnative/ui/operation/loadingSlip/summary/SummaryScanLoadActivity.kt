package com.greensoft.greentranserpnative.ui.operation.loadingSlip.summary

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.greensoft.greentranserpnative.base.BaseActivity
import com.greensoft.greentranserpnative.databinding.ActivitySummaryScanLoadBinding
import com.greensoft.greentranserpnative.ui.home.HomeActivity
import com.greensoft.greentranserpnative.ui.operation.loadingSlip.summary.models.SaveLoadingCompleteModel
import com.greensoft.greentranserpnative.ui.operation.loadingSlip.summary.models.SummaryScanLoadModel
import com.greensoft.greentranserpnative.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SummaryScanLoadActivity @Inject constructor(): BaseActivity() {
    private var activityBinding: ActivitySummaryScanLoadBinding? = null
    private val viewModel: SummaryScanLoadViewModel by viewModels()
    private var rvAdapter: SummaryListAdapter? = null
    private var rvList = ArrayList<SummaryScanLoadModel>()
    private var recyclerView: RecyclerView? = null
    private var linearLayoutManager: LinearLayoutManager? = null
    private var refresh: SwipeRefreshLayout? = null
    private var searchView: SearchView? = null
    private var loadingNo: String? = null
    private var loadingCompleteSave: String? = "N"
    private var menuCode: String = "GTAPP_SCANANDLOAD"
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //        setContentView(R.layout.activity_scanandload_searchlist);
        activityBinding = ActivitySummaryScanLoadBinding.inflate(
            layoutInflater
        )
        setContentView(activityBinding!!.root)
        initUi()
        setObserver()
        setOnClicks()
    }

    private fun refreshData() {
        rvList.clear()
        setupRecyclerView()
        getSummaryScanLoadList()
    }

    fun initUi() {
        setSupportActionBar(activityBinding?.toolBar?.root)
        setUpToolbar("Summary")
        mContext = this@SummaryScanLoadActivity
        searchView = activityBinding!!.searchView
        recyclerView = activityBinding!!.recyclerView
        refresh = activityBinding!!.pullDownToRefresh
        refresh!!.setOnRefreshListener {
            refreshData()
            refresh!!.isRefreshing = false
        }
        if (intent.extras != null) {
            loadingNo = intent.getStringExtra("LOADING_NO")
            loadingCompleteSave = intent.getStringExtra("SAVE")
            if (loadingCompleteSave == "Y") {
                activityBinding!!.footer.visibility = View.VISIBLE
                supportActionBar!!.title = "Loading Complete Save"
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (Utils.loadingNo != "") {
            loadingNo = Utils.loadingNo
            getSummaryScanLoadList()
        }
    }

//    override fun onPause() {
//        super.onPause()
//        Utils.loadingNo = loadingNo!!
//    }

    //    @Override
    //    public boolean onCreateOptionsMenu(Menu menu) {
    //        getMenuInflater().inflate(R.menu.main_menu, menu);
    //        return true;
    //    }
    //    @Override
    //    public boolean onOptionsItemSelected(MenuItem item) {
    //        int id = item.getItemId();
    //        if (id == R.id.datePicker) {
    //            if(!materialDatePicker.isAdded()) {
    //                materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
    //            }
    //            return true;
    //        }
    //        return super.onOptionsItemSelected(item);
    //
    //    }
    private fun setObserver() {
        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                // filter recycler view when query submitted
                rvAdapter!!.filter.filter(query)
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                // filter recycler view when text is changed
                rvAdapter!!.filter.filter(query)
                return false
            }
        })
        recyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                refresh!!.isEnabled =
                    linearLayoutManager!!.findFirstCompletelyVisibleItemPosition() == 0
            }
        })

//        mPeriod.observe(this, it -> {
//            rvList.clear();
//            setupRecyclerView();
//            GetScanAndLoadSearchList();
//        });
        viewModel.viewDialogLiveData.observe(this) { it: Boolean ->
            if (it) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        }
        viewModel.saveLoadingCompleteLiveData.observe(this) { result: SaveLoadingCompleteModel ->
            if (result.commandstatus == 1) {
                successfullyLoadedAlert(result, loadingNo)
                successToast("Loading Successful.")
//                this.loadingNo = ""
//                Utils.loadingNo = ""
            }
        }
        viewModel.summaryListLiveData.observe(this) { result: ArrayList<SummaryScanLoadModel>? ->
            if (result != null) {
                rvList = result
                if (rvList.size > 0) {
                    setupRecyclerView()
                    activityBinding!!.branch.text = result[0].origin
                    activityBinding!!.loading.text = loadingNo
                }
            }
        }
        viewModel.isError.observe(this) { errMsg: String? -> errorToast(errMsg) }
    }

    private fun successfullyLoadedAlert(result: SaveLoadingCompleteModel, loadingNo: String?) {
        val builder = AlertDialog.Builder(mContext)
        builder.setTitle("Alert !")
        val generatedManifestNo = result.manifestno
//        if (generatedManifestNo != null && generatedManifestNo != "") {
        builder.setMessage("Your Loading # has been generated.\nLoading: $loadingNo")
//        }
        builder.setCancelable(false)
        builder.setPositiveButton(
            "Okay",
            DialogInterface.OnClickListener { dialog: DialogInterface?, which: Int ->
//                val i = Intent(this@SummaryScanLoadActivity, HomeActivity::class.java)
//                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                startActivity(i)
                finish()
            })
        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun setOnClicks() {
        activityBinding!!.btnSaveLoading.setOnClickListener { view: View? ->
            var listOk = true
            if (rvList.size > 0) {
                for (i in rvList.indices) {
                    if (rvList[i].balancepckgs < 0) {
                        errorToast("Balance Packages cannot be negative.")
                        listOk = false
                        break
                    }
                }
            }
            if (listOk) {
                showConfirmationAlert()
            }
        }
    }

    fun showConfirmationAlert() {
        val builder: AlertDialog.Builder
        builder = AlertDialog.Builder(mContext!!)
        builder.setMessage("Are you sure you want to Save?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog: DialogInterface?, id: Int -> saveLoadingComplete() }
            .setNegativeButton("No") { dialog: DialogInterface, id: Int -> dialog.cancel() }
        val alert = builder.create()
        alert.setTitle("Submit Alert!!")
        alert.show()
    }

    fun setupRecyclerView() {
        if (rvAdapter == null) {
            rvAdapter = SummaryListAdapter(mContext)
            linearLayoutManager = LinearLayoutManager(mContext)
        }
        recyclerView!!.layoutManager = linearLayoutManager
        recyclerView!!.adapter = rvAdapter
        recyclerView!!.itemAnimator = DefaultItemAnimator()
        recyclerView!!.isNestedScrollingEnabled = false
        rvAdapter!!.setData(rvList)
    }

    private fun getSummaryScanLoadList() {
        if (isNetworkConnected()) {
            viewModel!!.getSummaryForLoading(
                getCompanyId(),
                getLoginBranchCode(),
                loadingNo,
                "N",
                getUserCode(),
                menuCode,  // "WMSAPP_SCANLOAD"
                getSessionId()
            )
        } else {
            errorToast(internetError)
        }
    }

    private fun saveLoadingComplete() {
        if (isNetworkConnected()) {
            viewModel!!.saveLoadingComplete(
                getCompanyId(),
                getLoginBranchCode(),
                loadingNo,
                "Y",
                getUserCode(),
                menuCode,  // "WMSAPP_SCANLOAD"
                getSessionId()
            )
        } else {
            errorToast(internetError)
        }
    }
}