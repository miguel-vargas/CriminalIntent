package com.migstech.criminalintent

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import java.util.Calendar
import java.util.Date

private const val ARG_DATE = "date"
private const val ARG_REQUEST_CODE = "requestCode"
private const val RESULT_TIME_KEY = "resultTime"

class TimePickerFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // initialize calendar to now
        val calendar = Calendar.getInstance()

        // if a crime date was passed in, set calendar to that datetime.
        val date = arguments?.getSerializable(ARG_DATE, Date::class.java)
        if (date != null) {
            calendar.time = date
        }

        // grab the calendar's instance time
        val initialHour = calendar.get(Calendar.HOUR)
        val initialSecond = calendar.get(Calendar.SECOND)

        val dateListener = TimePickerDialog.OnTimeSetListener { _: TimePicker, hour: Int, minute: Int ->
            calendar.set(Calendar.HOUR, hour)
            calendar.set(Calendar.MINUTE, minute)
            calendar.set(Calendar.SECOND, 0)

            val resultDate: Date = calendar.time

            val result = Bundle().apply {
                putSerializable(RESULT_TIME_KEY, resultDate)
            }

            val resultRequestCode = requireArguments().getString(ARG_REQUEST_CODE, "")
            parentFragmentManager.setFragmentResult(resultRequestCode, result)
        }

        return TimePickerDialog(
            requireContext(),
            dateListener,
            initialHour,
            initialSecond,
            false
        )
    }

    companion object {
        fun newInstance(date: Date, requestCode: String): TimePickerFragment {
            val args = Bundle().apply {
                putSerializable(ARG_DATE, date)
                putString(ARG_REQUEST_CODE, requestCode)
            }

            return TimePickerFragment().apply {
                arguments = args
            }
        }

        fun getSelectedDate(result: Bundle) = result.getSerializable(RESULT_TIME_KEY, Date::class.java)
    }
}