package com.example.isss.SI.ui.slideshow;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.isss.R;
import com.example.isss.SI.IsiTugas;
import com.example.isss.databinding.FragmentSlideshowBinding;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;
    private FragmentSlideshowBinding binding;
    String documentClickId;
    RecyclerView recyclerView;
    FirestoreRecyclerAdapter<GetDataTugas, TugasHolder> adaptercardTugas;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = root.findViewById(R.id.recycler_ViewTugas);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Query query = FirebaseFirestore.getInstance()
                .collection("tugasTemplate")
//                .orderBy("templateDate",Query.Direction.DESCENDING)
                .whereEqualTo("statusTugas","Belum Selesai");

        FirestoreRecyclerOptions<GetDataTugas> options = new FirestoreRecyclerOptions.Builder<GetDataTugas>()
                .setQuery(query, GetDataTugas.class)
                .build();

        adaptercardTugas = new FirestoreRecyclerAdapter<GetDataTugas, TugasHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull TugasHolder holder, int position, @NonNull GetDataTugas getDataTugas) {
                holder.setTitle((getDataTugas.getTitleTugas()));
                holder.setDesk(getDataTugas.getDeskripsi());
                holder.setTeam("Untuk : "+ getDataTugas.getTeamTugas());
                holder.setStatus(getDataTugas.getStatusTugas());

                holder.setOnClickListener(new TugasHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        documentClickId = getSnapshots().getSnapshot(position).getId();
                        Log.d("getclickdoc", documentClickId);

                        Intent keIsiTugas = new Intent(getActivity(), IsiTugas.class);
                        keIsiTugas.putExtra("clickedId",documentClickId);
                        startActivity(keIsiTugas);

//                        if (getSnapshots().getSnapshot(position).get("statusTugas") == null){
//                            Intent keIsiTugas = new Intent(getActivity(), IsiTugas.class);
//                            keIsiTugas.putExtra("clickedId",documentClickId);
//                            startActivity(keIsiTugas);
//                        }else{
//                            Snackbar.make(root.findViewById(R.id.slideshow),"Tugas yang anda pilih sudah selesai",Snackbar.LENGTH_INDEFINITE)
//                                    .setAction("OK", new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//                                        }
//                                    }).show();
//                        }



                    }
                });

            }

            @NonNull
            @Override
            public TugasHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_laporatugas, parent, false);
                return new TugasHolder(view);
            }
        };
        adaptercardTugas.startListening();
        recyclerView.setAdapter(adaptercardTugas);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}