package com.example.android.smartwallet;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.NumberFormat;

public class IncomeActivity extends AppCompatActivity {
    EditText incomeDescription;
    EditText incomeValue;
    TextView seekBarValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);

        incomeDescription = (EditText) findViewById(R.id.income_description_label);
        incomeValue = (EditText) findViewById(R.id.income_value_label);

        Intent receive = getIntent();
        TextView accountName = (TextView) findViewById(R.id.account_income_name);
        Bundle account = receive.getBundleExtra("account");
        accountName.setText(account.getString("name"));

        TextView accountBalance = (TextView) findViewById(R.id.account_income_balance);
        accountBalance.setText(NumberFormat.getCurrencyInstance().format(account.getDouble("balance")));
    }

    public void sendIncome(View view) {
        if (!incomeValue.getText().toString().equals("0") && !incomeValue.getText().toString().equals("") && !incomeDescription.getText().toString().equals("")) {
            Intent intent = new Intent(this, WalletActivity.class);
            Bundle income = new Bundle();

            income.putCharSequence("description", incomeDescription.getText().toString());
            income.putDouble("value", Double.parseDouble(incomeValue.getText().toString()));
            intent.putExtra("income", income);

            setResult(2, intent);
            finish();
        } else {
            Snackbar.make(getCurrentFocus(), "Incomes need a description and a value diferent of \'0\'", Snackbar.LENGTH_LONG)
                    .show();
        }
    }

    public void cancelIncome(View view) {
        setResult(0);
        finish();
    }
}