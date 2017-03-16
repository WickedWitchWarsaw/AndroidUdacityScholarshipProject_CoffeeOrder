package com.wickedwitchwarsaw.udacityscholarshipcoffeeorder;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import java.text.NumberFormat;

import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class CoffeeOrder extends AppCompatActivity {

    private int numberOfCoffees = 0;
    private double priceOfCoffee = 2.5;
    private double priceOfWhippedCream = 0.5;
    private double priceOfChocolate = 0.75;
    private int numberOfSugar = 0;
    double totalToPay = 0;

    @BindView(R.id.price_textview)
    protected TextView priceTextView;
    @BindView(R.id.quantity_number)
    protected TextView quantityCoffee;
    @BindView(R.id.cb_extra_topping)
    protected CheckBox cbWhippedCream;
    @BindView(R.id.cb_chocolate)
    protected CheckBox cbChocolate;
    @BindView(R.id.quantity_number_sugar)
    protected TextView quantitySugar;
    @BindView(R.id.customerName)
    protected EditText customerName;
    @BindView(R.id.btn_order)
    protected Button orderBtn;


    public static void startCoffeeOrder(Context context) {
        Intent intent = new Intent(context, CoffeeOrder.class);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coffee_order);
        ButterKnife.bind(this);
    }


    @OnClick(R.id.btn_plus)
    protected void addCoffee() {
        if (numberOfCoffees >= 15) {
            Toast.makeText(this, "Cannot select more coffees, pls make second order", Toast.LENGTH_SHORT).show();
        } else {
            numberOfCoffees = numberOfCoffees + 1;
            display(numberOfCoffees, quantityCoffee);
        }
    }

    @OnClick(R.id.btn_minus)
    protected void subtractCoffee() {
        if (numberOfCoffees == 0) {
            showAlertDialog();
        } else {
            numberOfCoffees = numberOfCoffees - 1;
            display(numberOfCoffees, quantityCoffee);
        }
    }

    @OnClick(R.id.btn_sugar_plus)
    protected void addSugar() {
        if (numberOfSugar >= 5) {
            Toast.makeText(this, "Cannot order any more sugar", Toast.LENGTH_SHORT).show();
        } else {
            numberOfSugar = numberOfSugar + 1;
            display(numberOfSugar, quantitySugar);
        }
    }

    @OnClick(R.id.btn_minus_sugar)
    protected void subtractSugar() {
        if (numberOfSugar == 0) {
            showAlertDialog();
        } else {
            numberOfSugar = numberOfSugar - 1;
            display(numberOfSugar, quantitySugar);
        }
    }

    @OnClick(R.id.btn_order)
    protected void submitOrder(View view) {
        checkToppings();
        displayPrice(totalToPay);
        if (numberOfCoffees != 0) {
            displayMessage("Thank you, confirm order??");
            orderBtn.setVisibility(orderBtn.GONE);
        } else {
            displayMessage("Are you really not ordering any coffees??");
        }
    }

    @OnClick(R.id.btn_yes)
    protected void confirmOrder() {
        startOrderConfirmationActivity();
    }

    @OnClick(R.id.btn_no)
    protected void backToOrder() {
        orderBtn.setVisibility(orderBtn.VISIBLE);

    }

    public void startOrderConfirmationActivity() {
        String chocolate = "chocolate";
        String whippedCream = "whippedCream";

        Intent startOrderConfirmation = new Intent(this, OrderConfirmation.class);

        String enteredName = customerName.getText().toString().trim();
        String coffeesOrderedExtra = (String) quantityCoffee.getText();
        String coffeePriceExtra = (String) priceTextView.getText();
        String sugarExtra = (String) quantitySugar.getText();

        putExtraToppingInIntent(startOrderConfirmation, cbWhippedCream, whippedCream);
        putExtraToppingInIntent(startOrderConfirmation, cbChocolate, chocolate);

        startOrderConfirmation.putExtra("customer", enteredName);
        startOrderConfirmation.putExtra("numberOfCoffees", coffeesOrderedExtra);
        startOrderConfirmation.putExtra("numberOfSugar", sugarExtra);
        startOrderConfirmation.putExtra("priceOfCoffees", coffeePriceExtra);
        startActivity(startOrderConfirmation);

        String completeOrder =
                //TODO: add topping info to email body
                "Order summary: /n" + enteredName +
                        "/n Number of coffees: " + coffeesOrderedExtra +
                        "/n Amount of sugar: " + sugarExtra +
                        "/n Total to pay: " + coffeePriceExtra;
    }


    private void putExtraToppingInIntent(Intent startOrderConfirmation, CheckBox checkBox, String topping) {
        if (checkBox.isChecked()) {
            startOrderConfirmation.putExtra(topping, true);
        } else {
            startOrderConfirmation.putExtra(topping, false);
        }
    }

    private void checkToppings() {
        if (cbChocolate.isChecked() && cbWhippedCream.isChecked()) {
            totalToPay = (priceOfCoffee + priceOfChocolate + priceOfWhippedCream) * numberOfCoffees;
        } else if (cbWhippedCream.isChecked()) {
            totalToPay = (priceOfCoffee + priceOfWhippedCream) * numberOfCoffees;
        } else if (cbChocolate.isChecked()) {
            totalToPay = (priceOfCoffee + priceOfChocolate) * numberOfCoffees;
        } else {
            totalToPay = priceOfCoffee * numberOfCoffees;
        }
    }


    private void display(int number, TextView textView) {
        textView.setText("" + number);
    }

    private void displayPrice(double price) {
        priceTextView.setText(NumberFormat.getCurrencyInstance().format(price));
    }


    protected void showAlertDialog() {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb
                .setTitle("ALERT")
                .setIcon(R.drawable.alert)
                .setMessage(getString(R.string.alert_text))
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .show();
    }

    private void displayMessage(String message) {
        TextView messageTextView = (TextView) findViewById(R.id.price_message);
        messageTextView.setText(message);
    }

}

