package com.merabill.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.merabill.OnPaymentAddedListener;
import com.merabill.databinding.DialogAddPaymentBinding;
import com.merabill.model.Payment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class AddPaymentDialog extends Dialog {

    private final OnPaymentAddedListener listener;
    private List<Payment> existingPayments = new  ArrayList<>();
    private List<String> availablePaymentMethods;
    private DialogAddPaymentBinding binding;
    public AddPaymentDialog(Context context, List<Payment> existingPayments, OnPaymentAddedListener listener) {
            super(context);
            this.listener = listener;
            this.existingPayments = existingPayments;
     }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate ViewBinding
        binding = DialogAddPaymentBinding.inflate(LayoutInflater.from(getContext()));
        setContentView(binding.getRoot());

        if (getWindow() != null) {
            getWindow().setLayout(
                    (int) (getContext().getResources().getDisplayMetrics().widthPixels * 0.90), // 85% of screen width
                    WindowManager.LayoutParams.WRAP_CONTENT
            );
        }

        List<String> allPaymentMethods = Arrays.asList("Cash", "Bank Transfer", "Credit Card");
        List<String> existingTypes = existingPayments.stream()
                .map(Payment::getType)
                .collect(Collectors.toList());

        availablePaymentMethods = allPaymentMethods.stream()
                .filter(method -> !existingTypes.contains(method))
                .collect(Collectors.toList());

        if (availablePaymentMethods.isEmpty()) {
            Toast.makeText(getContext(), "All payment types already added!", Toast.LENGTH_LONG).show();

            // Post dismiss() to ensure dialog is fully created before closing
            binding.getRoot().post(this::dismiss);
            return;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, availablePaymentMethods);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spPaymentMethod.setAdapter(adapter);

        binding.etProvider.setVisibility(View.GONE);
        binding.etTransactionReference.setVisibility(View.GONE);

        binding.spPaymentMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: // Cash
                        binding.etProvider.setVisibility(View.GONE);
                        binding.etTransactionReference.setVisibility(View.GONE);
                        break;
                    case 1: // Bank Transfer
                    case 2: // Credit Card
                        binding.etProvider.setVisibility(View.VISIBLE);
                        binding.etTransactionReference.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        binding.btnCancel.setOnClickListener(v -> dismiss());

        binding.btnOK.setOnClickListener(v -> {
            String type = binding.spPaymentMethod.getSelectedItem().toString();
            double amount = Double.parseDouble(binding.etAmount.getText().toString());
            String provider = binding.etProvider.getText().toString();
            String transactionRef = binding.etTransactionReference.getText().toString();

            if (existingPayments.stream().anyMatch(p -> p.getType().equals(type))) {
                Toast.makeText(getContext(), type + " payment already added!", Toast.LENGTH_SHORT).show();
                return;
            }

            listener.onPaymentAdded(new Payment(type, amount, provider, transactionRef));
            dismiss();
        });
    }

}
