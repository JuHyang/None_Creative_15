package ncs.com.kaulife;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ScheduleMajorActivity extends Activity {

    Button btn_liberal;
    Button btn_mechanical;
    Button btn_software;
    Button btn_material;
    Button btn_transport;
    Button btn_flight;
    Button btn_english;
    Button btn_business;
    Button btn_electronic;
    Button btn_cyber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_major);

        ActionBar actionBar = getActionBar();
        actionBar.hide();

        AboutView();
    }

    public void InitView () {
        btn_liberal = findViewById(R.id.btn_liberal);
        btn_mechanical = findViewById(R.id.btn_mechanical);
        btn_software = findViewById(R.id.btn_software);
        btn_material = findViewById(R.id.btn_material);
        btn_transport = findViewById(R.id.btn_transport);
        btn_flight = findViewById(R.id.btn_flight);
        btn_english = findViewById(R.id.btn_english);
        btn_business = findViewById(R.id.btn_business);
        btn_electronic = findViewById(R.id.btn_electronic);
        btn_cyber = findViewById(R.id.btn_cyber);
    }

    public void AboutView () {
        InitView();
        final Intent intent = new Intent (getApplicationContext(), ScheduleListActivity.class);
        btn_liberal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("label", "1");
                startActivity(intent);
            }
        });
        btn_mechanical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("label", "2");
                startActivity(intent);
            }
        });

        btn_software.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("label", "3");
                startActivity(intent);
            }
        });

        btn_material.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("label", "4");
                startActivity(intent);
            }
        });

        btn_transport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("label", "5");
                startActivity(intent);
            }
        });

        btn_flight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("label", "6");
                startActivity(intent);
            }
        });

        btn_english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("label", "7");
                startActivity(intent);
            }
        });

        btn_business.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("label", "8");
                startActivity(intent);
            }
        });

        btn_electronic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("label", "9");
                startActivity(intent);
            }
        });

        btn_cyber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("label", "00");
                startActivity(intent);
            }
        });
    }
}
