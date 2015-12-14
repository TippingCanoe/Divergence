package com.tippingcanoe.divergence.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tippingcanoe.divergence.DivergenceAdapter;
import com.tippingcanoe.divergence.Importance;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Button add;
    Button remove;
    EditText number;
    EditText name;

    DivergenceAdapter divergenceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        add = (Button) findViewById(R.id.add);
        remove = (Button) findViewById(R.id.remove);
        number = (EditText) findViewById(R.id.number);
        name = (EditText) findViewById(R.id.name);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);

        divergenceAdapter = new DivergenceAdapter(recyclerView);
        recyclerView.setAdapter(divergenceAdapter);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number = 0;

                try {
                    number = Integer.parseInt(String.valueOf(MainActivity.this.number.getText()));
                } catch (NumberFormatException e) {
                }

                String name = String.valueOf(MainActivity.this.name.getText());

                DemoAdapter demoAdapter = new DemoAdapter(name);
                divergenceAdapter.addSection(demoAdapter, name, number, Importance.MEDIUM);
            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = String.valueOf(MainActivity.this.name.getText());
                divergenceAdapter.removeSection(name);
            }
        });

        for (int i = 0; i < 3; i++) {
            divergenceAdapter.addSection(new DemoAdapter(String.valueOf(i)), String.valueOf(i), 0, Importance.MEDIUM);
        }
    }
}
