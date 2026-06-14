package com.example.electricbill;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    DataHelper dbHelper;
    Spinner spinnerMonth;
    EditText editUnit;
    SeekBar seekBarRebate;
    TextView tvRebateValue, tvTotal, tvFinal;
    Button btnCalculate, btnSave, btnHistory, btnAbout;

    double totalCharges = 0;
    double finalCostValue = 0;
    int selectedRebate = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DataHelper(this);

        spinnerMonth = findViewById(R.id.spinnerMonth);
        editUnit = findViewById(R.id.editUnit);
        seekBarRebate = findViewById(R.id.seekBarRebate);
        tvRebateValue = findViewById(R.id.tvRebateValue);
        tvTotal = findViewById(R.id.tvTotal);
        tvFinal = findViewById(R.id.tvFinal);
        btnCalculate = findViewById(R.id.btnCalculate);
        btnSave = findViewById(R.id.btnSave);
        btnHistory = findViewById(R.id.btnHistory);
        btnAbout = findViewById(R.id.btnAbout);

        seekBarRebate.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvRebateValue.setText("Rebate: " + progress + "%");
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { computeAndShow(); }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { saveBill(); }
        });

        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, HistoryActivity.class));
            }
        });

        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
            }
        });
    }

    private boolean computeAndShow() {
        String unitStr = editUnit.getText().toString().trim();

        if (unitStr.isEmpty()) {
            editUnit.setError("Please enter the number of units");
            Toast.makeText(this, "Unit cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        int units;
        try {
            units = Integer.parseInt(unitStr);
        } catch (NumberFormatException e) {
            editUnit.setError("Please enter a valid number");
            return false;
        }
        if (units < 1 || units > 1000) {
            editUnit.setError("Units must be between 1 and 1000");
            Toast.makeText(this, "Units must be between 1 and 1000", Toast.LENGTH_SHORT).show();
            return false;
        }

        int rebate = seekBarRebate.getProgress();

        double total = 0;
        int u = units;
        if (u > 600) { total += (u - 600) * 0.546; u = 600; }
        if (u > 300) { total += (u - 300) * 0.516; u = 300; }
        if (u > 200) { total += (u - 200) * 0.334; u = 200; }
        total += u * 0.218;

        double finalCost = total - (total * rebate / 100.0);

        totalCharges = total;
        finalCostValue = finalCost;
        selectedRebate = rebate;

        tvTotal.setText(String.format(Locale.US, "Total Charges: RM %.2f", total));
        tvFinal.setText(String.format(Locale.US, "Final Cost: RM %.2f", finalCost));
        return true;
    }

    private void saveBill() {
        if (!computeAndShow()) return;

        String month = spinnerMonth.getSelectedItem().toString();
        String unit = editUnit.getText().toString().trim();

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("INSERT INTO electricitybill(month, unit, totalcharges, rebate, finalcost) VALUES('" +
                month + "','" +
                unit + "','" +
                String.format(Locale.US, "%.2f", totalCharges) + "','" +
                selectedRebate + "','" +
                String.format(Locale.US, "%.2f", finalCostValue) + "')");

        Toast.makeText(this, "Bill saved successfully", Toast.LENGTH_SHORT).show();
    }
}