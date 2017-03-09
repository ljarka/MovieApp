package com.github.ljarka.movieapp;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RadioGroup;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class SearchActivity extends AppCompatActivity {

    private Map<Integer, String> apiKeysMap = new HashMap<Integer, String>() {{
        put(R.id.radio_movies, "movie");
        put(R.id.radio_episodes, "episode");
        put(R.id.radio_games, "game");
        put(R.id.radio_series, "series");
    }};

    @BindView(R.id.number_picker)
    NumberPicker numberPicker;

    @BindView(R.id.edit_text)
    TextInputEditText editText;

    @BindView(R.id.search_button)
    ImageView searchButton;

    @BindView(R.id.year_checkbox)
    CheckBox yearCheckbox;

    @BindView(R.id.type_checkbox)
    CheckBox typeCheckbox;

    @BindView(R.id.radio_group)
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        Calendar calender = Calendar.getInstance();
        int year = calender.get(Calendar.YEAR);
        numberPicker.setMinValue(1950);
        numberPicker.setMaxValue(year);
        numberPicker.setValue(year);
        numberPicker.setWrapSelectorWheel(true);
    }

    @OnCheckedChanged(R.id.type_checkbox)
    void onTypeCheckboxStateChanged(CompoundButton buttonView, boolean isChecked) {
        radioGroup.setVisibility(isChecked ? View.VISIBLE : View.GONE);
    }

    @OnCheckedChanged(R.id.year_checkbox)
    void onYearCheckboxStateChanged(CompoundButton buttonView, boolean isChecked) {
        numberPicker.setVisibility(isChecked ? View.VISIBLE : View.GONE);
    }

    @OnClick(R.id.search_button)
    void onSearchButtonClick() {
        int checkRadioId = radioGroup.getCheckedRadioButtonId();
        String typeKey = typeCheckbox.isChecked() ? apiKeysMap.get(checkRadioId) : null;
        int year = yearCheckbox.isChecked() ? numberPicker.getValue() : ListingActivity.NO_YEAR_SELECTED;

        startActivity(ListingActivity.createIntent(SearchActivity.this, editText.getText().toString(),
                year, typeKey));
    }
}
