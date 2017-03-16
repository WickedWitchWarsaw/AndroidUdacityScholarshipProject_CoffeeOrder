package com.wickedwitchwarsaw.udacityscholarshipcoffeeorder;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderConfirmation extends AppCompatActivity {

    @BindView(R.id.customerNameText)
    protected TextView customerName;
    @BindView(R.id.coffee_amount)
    protected TextView coffeeAmount;
    @BindView(R.id.coffee_price)
    protected TextView coffeePrice;
    @BindView(R.id.whipped_cream)
    protected TextView whippedCream;
    @BindView(R.id.chocolate)
    protected TextView chocolate;
    @BindView(R.id.sugar)
    protected TextView sugarAmount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);
        ButterKnife.bind(this);

        Intent startOrderConfirmation = getIntent();
        String nameOfCustomer = startOrderConfirmation.getStringExtra("customer");
        String coffeesOrdered = startOrderConfirmation.getStringExtra("numberOfCoffees");
        String totalPrice = startOrderConfirmation.getStringExtra("priceOfCoffees");
        String sugarOrdered = startOrderConfirmation.getStringExtra("numberOfSugar");
        Boolean extraWhippedCreamTrue = getIntent().getExtras().getBoolean("whippedCream");
        Boolean extraChocolate = getIntent().getExtras().getBoolean("chocolate");

        customerName.setText("Thanx " + nameOfCustomer + " ;)");
        coffeeAmount.setText("You ordered " + coffeesOrdered + " coffees");
        coffeePrice.setText("Total to pay: " + totalPrice);
        sugarAmount.setText("Amount of sugar: " + sugarOrdered);


        //TODO: refactor getExtra & ifs to one method with 2 variables

        setToppingText(extraWhippedCreamTrue, whippedCream, "Whipped Cream");
        setToppingText(extraChocolate, chocolate, "Chocolate");

    }

    private void setToppingText(Boolean extraTopping, TextView textView, String toppingType) {
        if (extraTopping == true) {
            textView.setText(toppingType + ": YES");
        } else {
            textView.setText(toppingType + ": NO");
        }
    }


    @OnClick(R.id.btn_send)
    protected void sendEmailIntent() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto: " + "zuz.p@interia.pl"));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Coffee Order");
        emailIntent.putExtra(Intent.EXTRA_TEXT, completeOrder());

        try {
            startActivity(Intent.createChooser(emailIntent, "Sending email via..."));
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(this, "No email clients installed.", Toast.LENGTH_LONG).show();
        }
    }


    private String completeOrder() {

        Intent startOrderConfirmation = getIntent();
        String nameOfCustomer = startOrderConfirmation.getStringExtra("customer");
        String coffeesOrdered = startOrderConfirmation.getStringExtra("numberOfCoffees");
        String totalPrice = startOrderConfirmation.getStringExtra("priceOfCoffees");
        String sugarOrdered = startOrderConfirmation.getStringExtra("numberOfSugar");
        Boolean extraWhippedCreamTrue = getIntent().getExtras().getBoolean("whippedCream");
        Boolean extraChocolate = getIntent().getExtras().getBoolean("chocolate");

        String completeOrder =
                //TODO: add topping info to email body
                "Order summary: " +
                        "\n" + nameOfCustomer +
                        " \n Number of coffees: " + coffeesOrdered +
                        "\n Amount of sugar: " + sugarOrdered +
                        "\n Total to pay: " + totalPrice;

        if (extraWhippedCreamTrue == true) {
            completeOrder = completeOrder + "\n Whipped Cream: YES";
        } else if (extraWhippedCreamTrue != true) {
            completeOrder = completeOrder + "\n Whipped Cream: NO";
        }

        if (extraChocolate == true) {
            completeOrder = completeOrder + "\n Chocolate: YES";
        } else if (extraChocolate != true) {
            chocolate.setText("\n Chocolate: NO");
        }

        return completeOrder;
    }

}


