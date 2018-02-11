package ncs.com.kaulife;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by kkss2 on 2018-02-11.
 */

@SuppressLint("ValidFragment")
public class ScheduleMajorFragment extends Fragment {
    Context context;

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

    public ScheduleMajorFragment (Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View layout = inflater.inflate(R.layout.fragment_schedule_major, container, false);
        InitView(layout);
        AboutView();
        return layout;
    }

    public void onResume() {
        super.onResume();
        AboutView();
    }

    public void InitView (View view) {
        btn_liberal = view.findViewById(R.id.btn_liberal);
        btn_mechanical = view.findViewById(R.id.btn_mechanical);
        btn_software = view.findViewById(R.id.btn_software);
        btn_material = view.findViewById(R.id.btn_material);
        btn_transport = view.findViewById(R.id.btn_transport);
        btn_flight = view.findViewById(R.id.btn_flight);
        btn_english = view.findViewById(R.id.btn_english);
        btn_business = view.findViewById(R.id.btn_business);
        btn_electronic = view.findViewById(R.id.btn_electronic);
        btn_cyber = view.findViewById(R.id.btn_cyber);
    }

    public void AboutView () {
        final Intent intent = new Intent (context, ScheduleListActivity.class);
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
