package com.example.dss.project.Fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dss.R;
import com.example.dss.project.Activity.MapsActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ViewHistory extends Fragment {

    EditText searchFrom, searchTo;
    Button showSchedule, xemlichtrinh, chiduong;
    String timeFrom, timeTo, speed, stringchiduong;
    LinearLayout linearViewHistory, linearViewButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_history, container, false);

        showSchedule = view.findViewById(R.id.show_schedule);
        xemlichtrinh =  view.findViewById(R.id.schedule);
        chiduong = view.findViewById(R.id.get_me_there);

        searchFrom = view.findViewById(R.id.edit_from);
        searchTo =  view.findViewById(R.id.edit_to);
        linearViewHistory =  view.findViewById(R.id.linear_view_history);
        linearViewButton =  view.findViewById(R.id.linear_view_button);
        searchFrom.setInputType(InputType.TYPE_NULL);
        searchTo.setInputType(InputType.TYPE_NULL);

        Calendar calendar = Calendar.getInstance();
        String date = calendar.get(Calendar.YEAR) + "-"+ calendar.get(Calendar.MONTH)+ "-"+ calendar.get(Calendar.DAY_OF_MONTH) ;
        String time = calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE)+ ":" + calendar.get(Calendar.SECOND);
        searchFrom.setText(date + "    " + time);
        searchTo.setText(date + "    " + time);


        linearViewHistory.setVisibility(View.GONE);

        xemlichtrinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearViewButton.setVisibility(View.GONE);
                linearViewHistory.setVisibility(View.VISIBLE);
            }
        });
        chiduong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MapsActivity activity = (MapsActivity) getActivity();
                stringchiduong = "chiduong";

                String strUri = "https://www.google.com/maps/dir/" + activity.getChosingCarLat() + "," + activity.getChosingCarLog() + "/" + activity.getMyLat() + "," + activity.getMyLog();

                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));
                intent.putExtra("chiduong", stringchiduong);
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                startActivity(intent);
            }
        });

        searchFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateTimeDialog(searchFrom);
                System.out.println("Chọn ngày giờ khởi hành");
            }
        });

        searchTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateTimeDialog(searchTo);
                System.out.println("Chọn ngày giờ kết thúc");
            }
        });

        showSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MapsActivity activity = (MapsActivity) getActivity();
                String carSignFromActivity = activity.getChosingCarSign();
                speed = "showSpeedSeekBar";
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                timeFrom = searchFrom.getText().toString();
                timeTo = searchTo.getText().toString();
                intent.putExtra("car", carSignFromActivity);
                intent.putExtra("timeFrom", timeFrom);
                intent.putExtra("timeTo", timeTo);
                intent.putExtra("speedCar", speed);
                startActivity(intent);
                //getActivity().onBackPressed();
            }
        });

        return view;
    }

    public void showDateTimeDialog(final EditText edit_fromOrTo){
        final Calendar c = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH, month);
                c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        c.set(Calendar.MINUTE, minute);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yy HH:mm:ss");
                        edit_fromOrTo.setText(simpleDateFormat.format(c.getTime()));
                    }
                };
                new TimePickerDialog(getContext(), timeSetListener, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false).show();
            }
        };
        new DatePickerDialog(getContext(), dateSetListener, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();

    }


}
