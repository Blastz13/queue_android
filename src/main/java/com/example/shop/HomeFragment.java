package com.example.shop;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.example.shop.Category.AllProductsActivity;
import com.example.shop.Category.CategoryAdapter;
import com.example.shop.Category.CategoryModel;
import com.example.shop.Category.GridProductLayoutAdapter;
import com.example.shop.Category.HorizontalProductScrollAdapter;
import com.example.shop.Category.HorizontalProductScrollModel;
import com.example.shop.Slider.SliderAdapter;
import com.example.shop.Slider.SliderModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class HomeFragment extends Fragment {

    private FirebaseFirestore firebaseFirestore;

    private RecyclerView categoryRecyclerView;
    private CategoryAdapter categoryAdapter;
    private ViewPager bannerSlider;
    private List<SliderModel> sliderModelList;
    private int currentPage = 2;
    private Timer timer;
    SliderAdapter sliderAdapter;

    private TextView horizontalLayoutTitle;
    private Button horizontalViewAllBtn;
    private RecyclerView horizontalRecycler;
    private List<CategoryModel> categoryModelList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        categoryRecyclerView = view.findViewById(R.id.category_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        categoryRecyclerView.setLayoutManager(layoutManager);
        categoryModelList = new ArrayList<CategoryModel>();
        categoryAdapter = new CategoryAdapter(categoryModelList);
        categoryRecyclerView.setAdapter(categoryAdapter);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("CATEGORIES").orderBy("index").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for(QueryDocumentSnapshot documentSnapshot: task.getResult()){
                        Log.d("dbg", documentSnapshot.get("categoryName").toString());
                        categoryModelList.add(new CategoryModel(documentSnapshot.get("icon").toString(), documentSnapshot.get("categoryName").toString()));
                    }
                    categoryAdapter.notifyDataSetChanged();
                }
                else{
                    ;
                } }
        });


        bannerSlider = view.findViewById(R.id.banner_view_page);
        currentPage = 1;
        if(timer != null){
            timer.cancel();
        }

        bannerSlider.setClipToPadding(false);
        bannerSlider.setPageMargin(20);

        ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE){
                    pageLoop();
                }
            }
        };
        sliderModelList = new ArrayList<>();

        sliderAdapter = new SliderAdapter(sliderModelList);

        firebaseFirestore.collection("SLIDER").orderBy("index").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for(QueryDocumentSnapshot documentSnapshot: task.getResult()){
                                Log.d("dbg", documentSnapshot.get("count_banners").toString());
                                for(int i=1; i <= (long)documentSnapshot.get("count_banners"); i++) {
                                    sliderModelList.add(new SliderModel(documentSnapshot.get("banner_"+i).toString()));
                                }
                            }
                            sliderAdapter.notifyDataSetChanged();
                        }
                        else{
                            Log.d("dbg", "error");
                        }
                    }
                });

        bannerSlider.setAdapter(sliderAdapter);
        bannerSlider.addOnPageChangeListener(onPageChangeListener);

        startBannerSlideShow();

        bannerSlider.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                pageLoop();
                stopBannerSlider();
                if (event.getAction() == MotionEvent.ACTION_UP)
                    startBannerSlideShow();
                return false;
            }
        });

        horizontalLayoutTitle = view.findViewById(R.id.horizontal_scroll_title);
        horizontalViewAllBtn = view.findViewById(R.id.horizontal_scroll_btn);
        horizontalRecycler = view.findViewById(R.id.horizontal_scroll_recyclerview);

        List<HorizontalProductScrollModel> horizontalProductScrollModelList = new ArrayList<>();
//        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.product_item, "NoteBook", "700 $"));

        HorizontalProductScrollAdapter horizontalProductScrollAdapter = new HorizontalProductScrollAdapter(horizontalProductScrollModelList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        horizontalRecycler.setLayoutManager(linearLayoutManager);
        horizontalRecycler.setAdapter(horizontalProductScrollAdapter);
        horizontalProductScrollAdapter.notifyDataSetChanged();

        List<HorizontalProductScrollModel> gridProductScrollModelList = new ArrayList<>();
        TextView gridLayoutTitle = view.findViewById(R.id.grid_product_layout_title);
        Button gridLayoutAllBtn = view.findViewById(R.id.grid_product_layout_view_all_btn);
        GridView gridView = view.findViewById(R.id.grid_product_layout_gridview);
        GridProductLayoutAdapter gridProductLayoutAdapter = new GridProductLayoutAdapter(gridProductScrollModelList);
        gridView.setAdapter(gridProductLayoutAdapter);


        firebaseFirestore.collection("CATEGORIES").document("HOME").collection("POPULAR_PRODUCTS").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot documentSnapshot: task.getResult()){
                                    if ((long)documentSnapshot.get("type") == 1) {
                                        long count_products = (long) documentSnapshot.get("count_products");
                                        if(count_products >4){
                                            count_products = 4;
                                        }
                                        for (int i = 1; i <= count_products; i++) {
                                            horizontalProductScrollModelList.add(new HorizontalProductScrollModel(documentSnapshot.get("product_id_" + i).toString(),
                                                    documentSnapshot.get("product_image_" + i).toString(),
                                                    documentSnapshot.get("product_title_" + i).toString(),
                                                    documentSnapshot.get("product_price_" + i).toString()));
                                            horizontalProductScrollAdapter.notifyDataSetChanged();
                                            gridProductLayoutAdapter.notifyDataSetChanged();
                                        }
                                    }
                                    else if((long)documentSnapshot.get("type") == 2){
                                        long count_products = (long) documentSnapshot.get("count_products");
                                        if(count_products >4){
                                            count_products = 4;
                                        }
                                        for(int i=1; i <= count_products; i++){
                                            gridProductScrollModelList.add(new HorizontalProductScrollModel(documentSnapshot.get("product_id_"+i).toString(),
                                                    documentSnapshot.get("product_image_"+i).toString(),
                                                    documentSnapshot.get("product_title_"+i).toString(),
                                                    documentSnapshot.get("product_price_"+i).toString()));
                                            horizontalProductScrollAdapter.notifyDataSetChanged();
                                            gridProductLayoutAdapter.notifyDataSetChanged();
                                        }
                                    }
                                }
                            }
                        }
                    });


        horizontalViewAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewAllIntent = new Intent(getContext(), AllProductsActivity.class);
                viewAllIntent.putExtra("type_layout", 0);
                viewAllIntent.putExtra("category_title", "Популярные товары");
                getContext().startActivity(viewAllIntent);
            }
        });

        gridLayoutAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewAllIntent = new Intent(getContext(), AllProductsActivity.class);
                viewAllIntent.putExtra("type_layout", 1);
                viewAllIntent.putExtra("category_title", "Топ продаж");
                getContext().startActivity(viewAllIntent);
            }
        });

        return view;
    }

    private void pageLoop(){
//        if (currentPage == sliderModelList.size() - 2){
//            currentPage = 2;
//            bannerSlider.setCurrentItem(currentPage, false);
//        }
//        if (currentPage == 1){
//            currentPage = sliderModelList.size() - 3;
//            bannerSlider.setCurrentItem(currentPage, false);
//        }
    }

    private void startBannerSlideShow(){
        Handler handler = new Handler();
        Runnable update = new Runnable() {
            @Override
            public void run() {
                if (currentPage >= sliderModelList.size()) {
                    currentPage = 0;
                }
                bannerSlider.setCurrentItem(currentPage++, true);
            }
        };
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        }, 3000, 3000);
    }

    private void stopBannerSlider(){
        timer.cancel();
    }
}