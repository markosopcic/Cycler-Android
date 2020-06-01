package com.markosopcic.cycler.view.fragments

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.markosopcic.cycler.R
import com.markosopcic.cycler.view.adapters.MultiChoice
import com.markosopcic.cycler.viewmodel.NewEventViewModel
import kotlinx.android.synthetic.main.new_event_fragment.*
import org.koin.android.ext.android.get
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*


class NewEventFragment : Fragment(){

    var viewModel = get<NewEventViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.new_event_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        new_event_start_time_button.setOnClickListener {
            showDateTimePicker()
        }

        new_event_name.doOnTextChanged { text, start, count, after -> viewModel.eventName.value = text.toString()  }
        new_event_description.doOnTextChanged { text, start, count, after -> viewModel.eventDescription.value = text.toString()  }

        new_event_create_button.setOnClickListener {
            viewModel.createEvent(::resetView)
        }


        viewModel.selectedFriends.value = mutableListOf()

        new_event_add_friends_button.setOnClickListener {
            viewModel.loadFriends {
                val dialog = MultiChoice(context,it, it.filter { (viewModel.selectedFriends.value as MutableList<String>).contains(it.id) })
                dialog.setOnConfirmListener {
                    viewModel.selectedFriends.value = dialog.selection.map { it.id }
                    dialog.dismiss()
                }
                dialog.show()
            }
        }


        viewModel.startTime.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if(it != null){
                new_event_selected_start_time.text ="Start Time: "+ DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(Locale.US).withZone(
                    ZoneId.systemDefault()).format(it.toInstant())
            }

        })
    }

    fun resetView(){
        new_event_selected_start_time.text = ""
        new_event_description.setText("")
        new_event_name.setText("")

    }

    fun showDateTimePicker() {
        val currentDate: Calendar = Calendar.getInstance()
        val date =  Calendar.getInstance()
        val dialog = DatePickerDialog(
            requireContext(),
            OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                date!!.set(year, monthOfYear, dayOfMonth)
                TimePickerDialog(
                    context,
                    OnTimeSetListener { view, hourOfDay, minute ->
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        date.set(Calendar.MINUTE, minute)
                        date.set(Calendar.SECOND,0)
                        if(date.after(Calendar.getInstance())){
                            viewModel.startTime.value = date
                        }else{
                            Toast.makeText(NewEventFragment@activity,"You have to select a future time!",Toast.LENGTH_SHORT).show()
                        }

                    },
                    currentDate.get(Calendar.HOUR_OF_DAY),
                    currentDate.get(Calendar.MINUTE),
                    false
                ).show()
            },
            currentDate.get(Calendar.YEAR),
            currentDate.get(Calendar.MONTH),
            currentDate.get(Calendar.DATE)
        )
        dialog.setOnCancelListener {
            viewModel.startTime.value = null
        }
        dialog.show()
    }

}