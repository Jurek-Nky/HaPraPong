package local.hapra.pongxxkotlin.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import local.hapra.pongxxkotlin.R;
import local.hapra.pongxxkotlin.Settings;

/**
 * hinzugefügt im Feb 2020 von andbra
 */
public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button btnMinus = findViewById(R.id.btnMinus);
        btnMinus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                onBtnMinus();
            }
        });

        Button btnPlus = findViewById(R.id.btnPlus);
        btnPlus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                onBtnPlus();
            }
        });

        Button btnStart = findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                onBtnStart();
            }
        });

        TextView tvRounds  = findViewById(R.id.tvRounds);
        tvRounds.setText(String.valueOf(Settings.rounds));
    }

    /**
     * addiert eine Runde, bis max. 10
     */
    public void onBtnPlus() {
        if(Settings.rounds < 10) Settings.rounds++;
        TextView tvRounds = findViewById(R.id.tvRounds);
        tvRounds.setText(String.valueOf(Settings.rounds));
    }

    /**
     * subtrahiert eine Runde, bis max. 10
     */
    public void onBtnMinus() {
        if(Settings.rounds > 1) Settings.rounds--;
        TextView tvRounds = findViewById(R.id.tvRounds);
        tvRounds.setText(String.valueOf(Settings.rounds));
    }


    /**
     * Startet das Spiel
     * Einstellungen werden über ein statisches Objekt übergeben
     */
    public void onBtnStart() {
        Switch swEnableYAxis = findViewById(R.id.swEnableYAxis);
        Switch swEnableKI = findViewById(R.id.swEnableKI);
        Switch swEnableBallToPlayer = findViewById(R.id.swEnableBallToPlayer);
        Spinner spn = findViewById(R.id.spnPosition);

        boolean playerBottom = (spn.getSelectedItem().toString().equals(getResources().getString(R.string.spnBottum)));

        Intent intent = new Intent(this, PongActivity.class);
        Settings.settingsEnableY = swEnableYAxis.isChecked();
        Settings.settingsEnableKI = swEnableKI.isChecked();
        Settings.settingsPlayerPositionBottom = playerBottom;
        Settings.settingsThrowBallAlwaysToPlayer= swEnableBallToPlayer.isChecked();

        startActivity(intent);
    }
}