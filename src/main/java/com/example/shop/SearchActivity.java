package com.example.shop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.shop.Wish.WishlistAdapter;
import com.example.shop.Wish.WishlistModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private SearchView searchView;
    private TextView notFound;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchView = findViewById(R.id.search_view);
        notFound = findViewById(R.id.not_found);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setVisibility(View.VISIBLE);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        final List<WishlistModel> list = new ArrayList<>();
        final List<String> listId = new ArrayList<>();

        Adapter adapter = new Adapter(list, false);
        adapter.setFromSearch(true);
        recyclerView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                list.clear();
                listId.clear();

                final List<String> tags = new ArrayList<String>(Arrays.asList(query.toLowerCase().split(" ")));
                tags.add(query);
                for(final String tag : tags){
                    tag.trim();
                    FirebaseFirestore.getInstance().collection("PRODUCTS")
                            .whereArrayContains("tags", tag)
                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for(DocumentSnapshot documentSnapshot : task.getResult().getDocuments()){
                                    WishlistModel wishlistModel = new WishlistModel(
                                            documentSnapshot.get("product_id").toString(),
                                            documentSnapshot.get("product_image_1").toString(),
                                            1,
                                            Long.parseLong(documentSnapshot.get("total_rating").toString()),
                                            documentSnapshot.get("product_title").toString(),
                                            documentSnapshot.get("avg_rating").toString(),
                                            documentSnapshot.get("product_price").toString(),
                                            documentSnapshot.get("product_discount_price").toString());
                                    wishlistModel.setTags((ArrayList<String>) documentSnapshot.get("tags"));
                                    if(!listId.contains(wishlistModel.getProductId())){
                                        list.add(wishlistModel);
                                        listId.add(wishlistModel.getProductId());
                                    }
                                }

                                if(tag.equals(tags.get(tags.size() - 1))){
                                    if(list.size() == 0){
                                        notFound.setVisibility(View.VISIBLE);
                                        recyclerView.setVisibility(View.GONE);
                                    }else {
                                        notFound.setVisibility(View.GONE);
                                        recyclerView.setVisibility(View.VISIBLE);
                                        adapter.getFilter().filter(query);
                                    }
                                }
                            }
                            else {
                                ;
                            }
                        }
                    });
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

    class Adapter extends WishlistAdapter implements Filterable{

        private List<WishlistModel> preFilteredList;

        public Adapter(List<WishlistModel> wishlistModelList, boolean isWishList) {
            super(wishlistModelList, isWishList);
            preFilteredList = wishlistModelList;
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    List<WishlistModel> filteredList = new ArrayList<>();

                    final String[] tags = constraint.toString().toLowerCase().split(" ");

                    for(WishlistModel wishlistModel : preFilteredList){
                        ArrayList<String> presentTags = new ArrayList<>();
                        for(String tag : tags){
                            if(wishlistModel.getTags().contains(tag)){
                                presentTags.add(tag);
                            }
                        }
                        wishlistModel.setTags(presentTags);
                    }
                    for(int i=tags.length; i> 0; i--){
                        for(WishlistModel model: preFilteredList){
                            if(model.getTags().size() == i){
                                filteredList.add(model);
                            }
                        }
                    }

                    filterResults.values = filteredList;
                    filterResults.count = filteredList.size();
                    
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if(results.count > 0){
                        setWishlistModelList((List<WishlistModel>) results.values);
                    }
                    notifyDataSetChanged();
                }
            };
        }
    }
}