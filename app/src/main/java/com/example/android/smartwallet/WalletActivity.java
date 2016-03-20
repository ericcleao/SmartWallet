package com.example.android.smartwallet;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.HashMap;

public class WalletActivity extends AppCompatActivity {
    HashMap<String, Account> accounts = new HashMap<>();
    static final int GET_EXPENSE = 1, GET_INCOME = 2, ADD_ACCOUNT = 3;
    TextView balanceTextView;
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WalletActivity.this, AccountActivity.class);
                startActivityForResult(intent, ADD_ACCOUNT);
            }
        });

        radioGroup = (RadioGroup) findViewById(R.id.accounts_radio_group);
        balanceTextView = (TextView) findViewById(R.id.balance_text_view);
        balanceTextView.setText(NumberFormat.getCurrencyInstance().format(0.0));

        TextView anyAccount = (TextView) findViewById(R.id.any_account_text_view);
        if (accounts.isEmpty()) {
            anyAccount.setVisibility(View.VISIBLE);
            radioGroup.setVisibility(View.GONE);
        } else {
            anyAccount.setVisibility(View.GONE);
            radioGroup.setVisibility(View.VISIBLE);
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                balanceTextView.setText(NumberFormat
                        .getCurrencyInstance()
                        .format(accounts.get(((RadioButton) group.findViewById(checkedId))
                                .getText()
                                .toString())
                                .getBalance()));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        TextView anyAccount = (TextView) findViewById(R.id.any_account_text_view);
        if (accounts.isEmpty()) {
            anyAccount.setVisibility(View.VISIBLE);
            radioGroup.setVisibility(View.GONE);
        } else {
            anyAccount.setVisibility(View.GONE);
            radioGroup.setVisibility(View.VISIBLE);
        }

    }

    private void addAccountToRadioGroup(String name) {
        RadioButton radioButton = (RadioButton) getLayoutInflater().inflate(R.layout.account, null);
        radioButton.setText(name);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            radioButton.setId(View.generateViewId());
        }
        radioGroup.addView(radioButton);
    }

    public void addExpense(View view) {
        try {
            RadioButton radioButton = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
            double balance = accounts.get(radioButton.getText().toString()).getBalance();
            Bundle accountSelected = new Bundle();
            accountSelected.putCharSequence("name", radioButton.getText().toString());
            accountSelected.putDouble("balance", balance);
            if (accounts.get(radioButton.getText().toString()) instanceof Credit) {
                accountSelected.putCharSequence("type", "Credit");
            } else {
                accountSelected.putCharSequence("type", "Debit");
            }

            Intent intent = new Intent(WalletActivity.this, ExpenseActivity.class);
            intent.putExtra("account", accountSelected);
            startActivityForResult(intent, GET_EXPENSE);
        } catch (NullPointerException e) {
            e.printStackTrace();
            Snackbar.make(findViewById(R.id.wallet_layout), "Select an account please.", Snackbar.LENGTH_LONG).show();
        }
    }

    public void addIncome(View view) {
        try {
            RadioButton radioButton = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
            Account account = accounts.get(radioButton.getText().toString());
            Bundle accountSelected = new Bundle();
            accountSelected.putCharSequence("name", account.getName());
            accountSelected.putDouble("balance", account.getBalance());
            if (account instanceof Credit) {
                accountSelected.putCharSequence("type", "Credit");
                throw new cannotAddIncometoCreditAccountException();
            } else {
                accountSelected.putCharSequence("type", "Debit");
            }

            Intent intent = new Intent(WalletActivity.this, IncomeActivity.class);
            intent.putExtra("account", accountSelected);
            startActivityForResult(intent, GET_INCOME);
        } catch (NullPointerException e) {
            e.printStackTrace();
            Snackbar.make(findViewById(R.id.wallet_layout), "Select an account please.", Snackbar.LENGTH_LONG).show();
        } catch (cannotAddIncometoCreditAccountException e) {
            e.printStackTrace();
            Snackbar.make(findViewById(R.id.wallet_layout), "Credit's account doesn't have incomes", Snackbar.LENGTH_LONG).show();
        }
    }

    public void showHints(View view) {
        startActivity(new Intent(WalletActivity.this, HintActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("requestCode", "" + requestCode);
        Log.d("resultCode", "" + resultCode);
        RadioButton radioButton = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());

        if (GET_EXPENSE == resultCode) {
            balanceTextView.setText(NumberFormat
                    .getCurrencyInstance()
                    .format(accounts.get(((RadioButton) radioGroup.findViewById(radioGroup.getCheckedRadioButtonId()))
                            .getText()
                            .toString())
                            .getBalance()));
            Account account = accounts.get(radioButton.getText().toString());
            Bundle expense = data.getBundleExtra("expense");
            String description = expense.getString("description");
            double value = expense.getDouble("value");
            int times = expense.getInt("times");

            if (account instanceof Credit) {
                Credit creditAcc = (Credit) account;
                creditAcc.addExpense(new Expense(description, value, times));
            } else if (account instanceof Debit) {
                Debit debitAcc = (Debit) account;
                debitAcc.addExpense(new Expense(description, value, times));
            }
        } else if (GET_INCOME == resultCode) {
            balanceTextView.setText(NumberFormat
                    .getCurrencyInstance()
                    .format(accounts.get(((RadioButton) radioGroup.findViewById(radioGroup.getCheckedRadioButtonId()))
                            .getText()
                            .toString())
                            .getBalance()));
            Account account = accounts.get(radioButton.getText().toString());
            Bundle income = data.getBundleExtra("income");
            String description = income.getString("description");
            double value = income.getDouble("value");

            if (account instanceof Debit) {
                Debit debitAcc = (Debit) account;
                debitAcc.addIncome(new Income(description, value));
            }
        } else if (ADD_ACCOUNT == resultCode) {
            Bundle newAccount = data.getBundleExtra("account");
            String name = newAccount.getString("name");
            Log.d("name", name);
            Log.d("type", newAccount.getString("type"));
            if (newAccount.getString("type").equals("Credit")) {
                Double limit = newAccount.getDouble("limit");
                Log.d("limit", String.valueOf(limit));
                accounts.put(name, new Credit(name, limit));
            } else {
                accounts.put(name, new Debit(name));
            }
            addAccountToRadioGroup(name);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
