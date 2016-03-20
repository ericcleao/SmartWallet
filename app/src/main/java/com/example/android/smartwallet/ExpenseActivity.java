package com.example.android.smartwallet;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class ExpenseActivity extends AppCompatActivity {
    EditText expenseDescription;
    EditText expenseValue;
    SeekBar seekBar;
    TextView seekBarValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        expenseDescription = (EditText) findViewById(R.id.expense_description_label);
        expenseValue = (EditText) findViewById(R.id.expense_value_label);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBarValue = (TextView) findViewById(R.id.seekBarValue);

        final DecimalFormat decimalFormat = new DecimalFormat("0.00");
        decimalFormat.setRoundingMode(RoundingMode.UP);

        Intent receive = getIntent();
        TextView accountName = (TextView) findViewById(R.id.account_expense_name);
        Bundle account = receive.getBundleExtra("account");
        accountName.setText(account.getString("name"));

        TextView accountBalance = (TextView) findViewById(R.id.account_expense_balance);
        accountBalance.setText(NumberFormat.getCurrencyInstance().format(account.getDouble("balance")));

        LinearLayout creditParceable = (LinearLayout) findViewById(R.id.credit_parceable);
        if (account.getString("type").equals("Credit")) {
            creditParceable.setVisibility(View.VISIBLE);
            seekBar.setProgress(0);
            seekBar.setMax(11);
            try {
                seekBarValue.setText(1 + " x " + decimalFormat.format(Double.valueOf(expenseValue.getText().toString()) / 1));
            } catch (Exception e) {
                e.printStackTrace();
                seekBarValue.setText(1 + " x " + decimalFormat.format(0 / 1));
            }

        } else {
            creditParceable.setVisibility(View.GONE);
        }

        expenseValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int progress = seekBar.getProgress() + 1;
                progress /= 1;
                progress *= 1;
                if (s.length() > 0) {
                    seekBarValue.setText(progress + " x " + decimalFormat.format(Double.valueOf(expenseValue.getText().toString()) / progress));
                } else {
                    seekBarValue.setText(progress + " x " + decimalFormat.format(0));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress++;
                progress /= 1;
                progress *= 1;
                try {
                    seekBarValue.setText(progress + " x " + decimalFormat.format(Double.valueOf(expenseValue.getText().toString()) / progress));
                } catch (Exception e) {
                    e.printStackTrace();
                    seekBarValue.setText(progress + " x " + decimalFormat.format(0 / progress));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void sendExpense(View view) {
        if (!expenseValue.getText().toString().equals("0") && !expenseValue.getText().toString().equals("") && !expenseDescription.getText().toString().equals("")) {
            Intent intent = new Intent();
            Bundle expense = new Bundle();

            expense.putCharSequence("description", expenseDescription.getText().toString());
            expense.putInt("times", seekBar.getProgress() + 1);
            expense.putDouble("value", Double.parseDouble(expenseValue.getText().toString()));
            intent.putExtra("expense", expense);

            setResult(1, intent);
            finish();
        } else {
            Snackbar.make(getCurrentFocus(), "Expense need a description and a value diferent of \'0\'", Snackbar.LENGTH_LONG)
                    .show();
        }
    }

    public void cancelExpense(View view) {
        setResult(0);
        finish();
    }
}
