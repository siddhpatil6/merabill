package com.merabill.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.merabill.R;
import com.merabill.data.PaymentStorage;
import com.merabill.databinding.ActivityBillBoardBinding;
import com.merabill.dialog.AddPaymentDialog;
import com.merabill.model.Payment;

import java.util.ArrayList;
import java.util.List;

public class BillBoardActivity extends AppCompatActivity  {
    private ActivityBillBoardBinding binding;
    private List<Payment> payments;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        payments = new ArrayList<>();
        binding = ActivityBillBoardBinding.inflate(getLayoutInflater());
        binding.txtTotalAmount.setText(getString(R.string.total_amount, String.valueOf(0)));
        setContentView(binding.getRoot());
        PaymentStorage.loadPayments(getApplicationContext(),payments);
        updateChipGroup();
        binding.addPaymentText.setOnClickListener(view -> showAddPaymentDialog());
        binding.btnSave.setOnClickListener(view -> PaymentStorage.savePayments(getApplicationContext(),payments));
    }

    private void showAddPaymentDialog() {
        new AddPaymentDialog(this, payments, payment -> {
            payments.add(payment);
            updateChipGroup();
        }).show();
    }


    private void updateChipGroup() {
        double totalAmount = 0;

        // Ensure 'payments' list is not null
        if (payments == null) {
            payments = new ArrayList<>();
        }

        binding.rvPaymentModeList.removeAllViews(); // Clear old chips

        for (final Payment payment : payments) {
            Chip chip = (Chip) LayoutInflater.from(this)
                    .inflate(R.layout.item_chip, binding.rvPaymentModeList, false);

            chip.setText(String.format("%s: %s", payment.getType(), payment.getAmount()));
            chip.setCloseIconVisible(true);

            // Ensure correct layout parameters
            ChipGroup.LayoutParams params = new ChipGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            chip.setLayoutParams(params);

            // Remove chip on close icon click
            chip.setOnCloseIconClickListener(v -> removeChip(payment));

            binding.rvPaymentModeList.addView(chip);
            totalAmount += payment.getAmount(); // Sum total amount
        }

        binding.txtTotalAmount.setText(getString(R.string.total_amount, String.valueOf(totalAmount)));
    }


    private void removeChip(Payment payment) {
        payments.remove(payment);
        updateChipGroup();
    }


}