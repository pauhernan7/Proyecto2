package com.example.projecte2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

public class HeaderFragment extends Fragment {

    private String title;
    private OnMenuClickListener menuClickListener;
    private TextView tvTitle;
    private TextView tvUsername;

    public interface OnMenuClickListener {
        void onMenuClick();
    }

    public HeaderFragment() {
        // Constructor vacío requerido
    }

    public static HeaderFragment newInstance(String title) {
        HeaderFragment fragment = new HeaderFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString("title");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_header, container, false);

        tvTitle = view.findViewById(R.id.tvTitle);
        if (title != null) {
            tvTitle.setText(title);
        }

        // Cargar el username desde SharedPreferences y actualizar el TextView
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_data", Context.MODE_PRIVATE);


        ImageButton btnMenu = view.findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(v -> {
            if (menuClickListener != null) {
                menuClickListener.onMenuClick();
            }
        });

        return view;
    }

    public void setOnMenuClickListener(OnMenuClickListener listener) {
        this.menuClickListener = listener;
    }


    // Añade este método para cambiar el título dinámicamente
    public void setTitle(String newTitle) {
        this.title = newTitle;
        if (tvTitle != null) {
            tvTitle.setText(newTitle);
        }
    }
}