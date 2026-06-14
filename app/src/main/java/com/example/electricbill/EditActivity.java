package com.example.electricbill;

import android.database.Cursor;
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

public class EditActivity extends AppCompatActivity {

    DataHelper dbHelper;
    Spinner spinnerMonth;
    EditText editUnit;
    SeekBar seekBarRebate;
    TextView tvRebateValue, tvTotal, tvFinal;
    Button btnUpdate, btnBack;
    String recordId;

    double totalCharges = 0, finalCostValue = 0;
    int selectedRebate = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        dbHelper = new DataHelper(this);
        recordId = getIntent().getStringExtra("id");

        spinnerMonth = findViewById(R.id.spinnerMonth);
        editUnit = findViewById(R.id.editUnit);
        seekBarRebate = findViewById(R.id.seekBarRebate);
        tvRebateValue = findViewById(R.id.tvRebateValue);
        tvTotal = findViewById(R.id.tvTotal);
        tvFinal = findViewById(R.id.tvFinal);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnBack = findViewById(R.id.btnBack);

        seekBarRebate.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvRebateValue.setText("Rebate: " + progress + "%");
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        loadData();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { updateBill(); }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { finish(); }
        });
    }

    private void loadData() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM electricitybill WHERE id = '" + recordId + "'", null);
        if (c.moveToFirst()) {
            String month = c.getString(1);
            String[] months = getResources().getStringArray(R.array.months);
            for (int i = 0; i < months.length; i++) {
                if (months[i].equals(month)) { spinnerMonth.setSelection(i); break; }
            }
            editUnit.setText(c.getString(2));
            int rebate = Integer.parseInt(c.getString(4));
            seekBarRebate.setProgress(rebate);
            tvRebateValue.setText("Rebate: " + rebate + "%");
            tvTotal.setText("Total Charges: RM " + c.getString(3));
            tvFinal.setText("Final Cost: RM " + c.getString(5));
        }
        c.close();
    }

    private boolean computeAndShow() {
        String unitStr = editUnit.getText().toString().trim();
        if (unitStr.isEmpty()) { editUnit.setError("Please enter the number of units"); return false; }
        int units;
        try { units = Integer.parseInt(unitStr); }
        catch (NumberFormatException e) { editUnit.setError("Enter a valid number"); return false; }
        if (units < 1 || units > 1000) { editUnit.setError("Units must be between 1 and 1000"); return false; }

        int rebate = seekBarRebate.getProgress();
        double total = 0;
        int u = units;
        if (u > 600) { total += (u - 600) * 0.546; u = 600; }
        if (u > 300) { total += (u - 300) * 0.516; u = 300; }
        if (u > 200) { total += (u - 200) * 0.334; u = 200; }
        total += u * 0.218;
        double finalCost = total - (total * rebate / 100.0);

        totalCharges = total; finalCostValue = finalCost; selectedRebate = rebate;
        tvTotal.setText(String.format(Locale.US, "Total Charges: RM %.2f", total));
        tvFinal.setText(String.format(Locale.US, "Final Cost: RM %.2f", finalCost));
        return true;
    }

    private void updateBill() {
        if (!computeAndShow()) return;
        String month = spinnerMonth.getSelectedItem().toString();
        String unit = editUnit.getText().toString().trim();

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("UPDATE electricitybill SET " +
                "month='" + month + "', " +
                "unit='" + unit + "', " +
                "totalcharges='" + String.format(Locale.US, "%.2f", totalCharges) + "', " +
                "rebate='" + selectedRebate + "', " +
                "finalcost='" + String.format(Locale.US, "%.2f", finalCostValue) + "' " +
                "WHERE id='" + recordId + "'");

        Toast.makeText(this, "Record updated", Toast.LENGTH_SHORT).show();
        finish();
    }
}