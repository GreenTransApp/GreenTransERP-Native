package com.greensoft.greentranserpnative.ui.operation.bookingWithoutIndent.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.datepicker.MaterialDatePicker
import com.greensoft.greentranserpnative.common.PeriodSelection
import com.greensoft.greentranserpnative.databinding.ItemInvoiceListBinding
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.bookingWithoutIndent.DirectBookingActivity
import com.greensoft.greentranserpnative.ui.operation.bookingWithoutIndent.model.InvoiceDataModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class InVoiceListAdapter @Inject constructor(
    private val invoiceList: ArrayList<InvoiceDataModel>,
    private val onRowClick: OnRowClick<Any>?=null,
    private val activity: DirectBookingActivity
):RecyclerView.Adapter<RecyclerView.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val layoutBinding = ItemInvoiceListBinding.inflate(inflater,parent,false)
        return InvoiceViewHolder(layoutBinding,parent.context)
    }

    override fun getItemCount(): Int {
       return invoiceList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val dataHolder = holder as InvoiceViewHolder
        val dataModel:InvoiceDataModel = invoiceList[position]
        dataHolder.onBind(dataModel,onRowClick)
    }

    inner class InvoiceViewHolder(
        private val layoutBinding:ItemInvoiceListBinding,
        private val mContext:Context ):RecyclerView.ViewHolder(layoutBinding.root){

            fun onBind(invoiceData:InvoiceDataModel,onRowClick: OnRowClick<Any>?){
                layoutBinding.index = adapterPosition
                layoutBinding.inputEwbDate.setText(getViewCurrentDate())
                layoutBinding.inputEwbValidUptoDate.setText(getViewCurrentDate())
                layoutBinding.inputInvoiceDt.setText(getViewCurrentDate())
                layoutBinding.inVoiceData = invoiceData

                setOnclick(invoiceData)

            }
        fun getViewCurrentDate(): String {
            val formatter = SimpleDateFormat("dd-MM-yyyy")
            val date = Date()
            return formatter.format(date)
        }

        fun setOnclick(model: InvoiceDataModel){

            layoutBinding.inputEwayBillNo.setText(model.ewbbillno)
            layoutBinding.inputInvoiceNo.setText(model.invoiceno)
            layoutBinding.inputInvoiceValue.setText(model.invoicevalue)

            layoutBinding.inputEwbDate.setOnClickListener {
                selectDate("EwbDate")

            }
            layoutBinding.inputEwbValidUptoDate.setOnClickListener {
                selectDate("EwbValidUptoDate")
            }
            layoutBinding.inputInvoiceDt.setOnClickListener {
                selectDate("InVoiceDate")
            }
            layoutBinding.removeBtn.setOnClickListener {
                onRowClick?.onRowClick(model, "REMOVE_SELECT", adapterPosition)
            }

        }

        private fun selectDate(clickType:String) {
            val materialDateBuilder: MaterialDatePicker.Builder<*> =
                MaterialDatePicker.Builder.datePicker()
            materialDateBuilder.setTitleText("SELECT A DATE")
            var singleDatePicker: MaterialDatePicker<*> = materialDateBuilder.build()
            singleDatePicker.addOnPositiveButtonClickListener{ selection: Any ->
                val viewFormat = SimpleDateFormat("dd-MM-yyyy", Locale("US-ENG"))
                val sqlFormat = SimpleDateFormat("yyyy-MM-dd", Locale("US-ENG"))
                val selectedDate = selection
                val singleDate = Date(selectedDate as Long)
                val periodSelection = PeriodSelection()
                periodSelection.sqlsingleDate = sqlFormat.format(singleDate)
                periodSelection.viewsingleDate = viewFormat.format(singleDate)

                if (clickType=="EwbDate"){
                    invoiceList[adapterPosition].ewbdt = periodSelection.viewsingleDate
                    invoiceList[adapterPosition].sqlpoddt = periodSelection.sqlsingleDate
                    layoutBinding.inputEwbDate.setText(periodSelection.viewsingleDate)
                }
                else if (clickType == "EwbValidUptoDate"){
                    invoiceList[adapterPosition].ewbvaliduptodt = periodSelection.viewsingleDate
                    invoiceList[adapterPosition].sqlpoddt = periodSelection.sqlsingleDate
                    layoutBinding.inputEwbValidUptoDate.setText(periodSelection.viewsingleDate)
                }
                else if (clickType=="InVoiceDate"){
                    invoiceList[adapterPosition].invoicedt = periodSelection.viewsingleDate
                    invoiceList[adapterPosition].sqlpoddt = periodSelection.sqlsingleDate
                    layoutBinding.inputInvoiceDt.setText(periodSelection.viewsingleDate)
                  }

            }
            if (singleDatePicker.isVisible) {
                return;
            }
            singleDatePicker.show(activity.supportFragmentManager, "DATE_PICKER");
        }

    }

}