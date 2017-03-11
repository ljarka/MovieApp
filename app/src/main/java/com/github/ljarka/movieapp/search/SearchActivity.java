package com.github.ljarka.movieapp.search;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RadioGroup;

import com.github.ljarka.movieapp.R;
import com.github.ljarka.movieapp.RetrofitProvider;
import com.github.ljarka.movieapp.listing.ListingActivity;
import com.github.ljarka.movieapp.listing.MovieListingItem;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.functions.Function;
import retrofit2.Retrofit;

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

    @BindView(R.id.poster_header)
    RecyclerView posterHeaderRecyclerView;

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

        PosterRecyclerViewAdapter adapter = new PosterRecyclerViewAdapter();
        posterHeaderRecyclerView.setAdapter(adapter);
        posterHeaderRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));

        RetrofitProvider retrofitProvider = (RetrofitProvider) getApplication();
        Retrofit retrofit = retrofitProvider.provideRetrofit();
        SearchService searchService = retrofit.create(SearchService.class);
        searchService.search("a*", "2016", null)
                .flatMap(searchResult -> Observable.fromIterable(searchResult.getItems()))
                .map(new Function<MovieListingItem, String>() {
                    @Override
                    public String apply(MovieListingItem movieListingItem) throws Exception {
                        return movieListingItem.getPoster();
                    }
                });
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
