package com.merabill.data;
import static android.content.Context.MODE_PRIVATE;

import android.content.Context;

import com.merabill.model.Payment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class PaymentStorage {
    private static final String FILE_NAME = "LastPayment.txt";

    public static void savePayments(Context context, List<Payment> payments) {
        JSONArray jsonArray = new JSONArray();
        for (Payment payment : payments) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("type", payment.getType());
                jsonObject.put("amount", payment.getAmount());
                jsonObject.put("provider", payment.getProvider());
                jsonObject.put("transactionRef", payment.getTransactionRef());
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        try (FileOutputStream fos = context.openFileOutput(FILE_NAME, MODE_PRIVATE)) {
            fos.write(jsonArray.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadPayments(Context context, List<Payment> payments) {
        File file = new File(context.getFilesDir(), FILE_NAME);
        if (!file.exists()) return;

        try (FileInputStream fis = context.openFileInput(FILE_NAME)) {
            byte[] buffer = new byte[(int) file.length()];
            fis.read(buffer);
            String jsonStr = new String(buffer, StandardCharsets.UTF_8);

            JSONArray jsonArray = new JSONArray(jsonStr);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String type = jsonObject.getString("type");
                double amount = jsonObject.getDouble("amount");
                String provider = jsonObject.getString("provider");
                String transactionRef = jsonObject.getString("transactionRef");

                payments.add(new Payment(type, amount,provider,transactionRef));
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }
}
