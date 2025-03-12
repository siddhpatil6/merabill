package com.merabill;

import com.merabill.model.Payment;

public interface OnPaymentAddedListener {
    void onPaymentAdded(Payment payment);
}