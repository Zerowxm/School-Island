package wxm.com.maca.ui.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wxm.com.maca.R;
import wxm.com.maca.ui.ReleaseActivity;

/**
 * Created by zero on 2015/7/2.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    Calendar calendar;
    DatePickCallBack datePickCallBack;
    String time = "";
    View view;
    int mode;
    int TIME = 0;
    int DATE = 1;
    String hour = "";
    String minutes = "";
    @Bind(R.id.picker)
    LinearLayout picker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        view = inflater.inflate(R.layout.date_picker, container);
        ButterKnife.bind(this, view);
        calendar = Calendar.getInstance();
        //setupDatePicker();
        DatePicker datePicker = new DatePicker(getActivity());
        setupDatePicker(datePicker);
        picker.addView(datePicker);
        mode = DATE;
        hour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
        minutes = String.valueOf(calendar.get(Calendar.MINUTE));
        return view;
    }


    static DatePickerFragment newInstance(int num) {
        DatePickerFragment f = new DatePickerFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);

        return f;
    }

    private void setupDatePicker(DatePicker datePicker) {
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {

                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(year, monthOfYear, dayOfMonth);

                    }
                });
    }

    private void setupTimePicker(TimePicker timePicker) {
        timePicker.setIs24HourView(true);
        timePicker.setHour(calendar.get(Calendar.HOUR_OF_DAY));
        timePicker.setMinute(calendar.get(Calendar.MINUTE));
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                hour = String.valueOf(hourOfDay);
                minutes = String.valueOf(minute);
            }
        });
    }

    @OnClick(R.id.cancel_btn)
    public void onCancel() {
        dismiss();
    }

    @OnClick(R.id.ok_btn)
    public void onOk() {
        if (mode == DATE) {

            picker.removeAllViews();
            TimePicker timePicker = new TimePicker(getActivity());
            setupTimePicker(timePicker);
            picker.addView(timePicker);
            mode = TIME;
        } else if (mode == TIME) {
            time += calendar.get(Calendar.YEAR) + "." + calendar.get(Calendar.MONTH) + "."
                    + calendar.get(Calendar.DAY_OF_MONTH) + " " + hour + ":" + minutes;
            datePickCallBack.addTime(time);
            dismiss();
        }
    }

    public interface DatePickCallBack {
        public void addTime(String time);
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ReleaseActivity) {
            datePickCallBack = (DatePickCallBack) activity;
        }
    }

    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {

    }
}
