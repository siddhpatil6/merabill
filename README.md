[merabill.webm](https://github.com/user-attachments/assets/f81873b5-15bd-4578-9129-dfbc2987378f)


## Overview

This document describes the implementation details of `BillBoardActivity` and `AddPaymentDialog` in an Android application. The functionality allows users to add, display, and remove payments using `ChipGroup` while persisting data via `PaymentStorage`.

## 1. BillBoardActivity

### **Class: ****`BillBoardActivity`**

**Package:** `com.merabill.activity`

### **Responsibilities**

- Displays a list of payments as `Chip` components.
- Allows users to add new payments via `AddPaymentDialog`.
- Removes payments when the close icon on a `Chip` is clicked.
- Calculates and displays the total amount of all payments.
- Persists payment data using `PaymentStorage`.

### **Dependencies**

- `ActivityBillBoardBinding`: View binding for UI elements.
- `PaymentStorage`: Handles loading and saving of payments.
- `AddPaymentDialog`: Modal dialog to add new payments.
- `Payment`: Model class representing a payment.

### **Key Methods**

#### **onCreate(Bundle savedInstanceState)**

- Initializes the activity and sets up event listeners.
- Loads existing payments from `PaymentStorage`.
- Calls `updateChipGroup()` to display payments.
- Sets click listeners for adding and saving payments.

#### **showAddPaymentDialog()**

- Opens `AddPaymentDialog`.
- Adds the new payment to the list and updates the UI upon user confirmation.

#### **updateChipGroup()**

- Updates `ChipGroup` with the latest payments.
- Clears existing views before adding new ones.
- Creates a `Chip` for each payment with a close icon.
- Calculates and updates the total payment amount.

#### **removeChip(Payment payment)**

- Removes a payment from the list and updates the UI.

---

## 2. AddPaymentDialog

### **Class: ****`AddPaymentDialog`**

**Package:** `com.merabill.dialog`

### **Responsibilities**

- Provides a UI for users to add a payment.
- Filters out already added payment methods.
- Validates user input before adding a new payment.
- Calls `OnPaymentAddedListener` to notify `BillBoardActivity`.

### **Dependencies**

- `DialogAddPaymentBinding`: View binding for dialog UI.
- `OnPaymentAddedListener`: Interface for passing added payments.
- `Payment`: Model class representing a payment.

### **Key Methods**

#### **onCreate(Bundle savedInstanceState)**

- Initializes the dialog UI and sets its size.
- Filters out already added payment types.
- Sets up `Spinner` for selecting payment methods.
- Shows/hides additional fields based on payment type selection.

#### **Event Listeners**

- `btnCancel`: Closes the dialog.
- `btnOK`: Validates and adds the payment.
  - Ensures the selected payment type is not already added.
  - Reads input values and constructs a `Payment` object.
  - Calls `listener.onPaymentAdded(payment)` to notify `BillBoardActivity`.
  - Closes the dialog after successful addition.

---

## 3. Data Persistence with PaymentStorage

### **Class: ****`PaymentStorage`**

- **`loadPayments(Context context, List<Payment> payments)`**: Loads saved payments into the provided list.
- **`savePayments(Context context, List<Payment> payments)`**: Saves the current payments to persistent storage.

---

## 4. UI Components

### **BillBoardActivity UI Elements**

- `txtTotalAmount`: Displays the total amount of all payments.
- `rvPaymentModeList`: A `ChipGroup` that dynamically lists payment methods.
- `addPaymentText`: Opens `AddPaymentDialog` when clicked.
- `btnSave`: Saves payments to storage.

### **AddPaymentDialog UI Elements**

- `spPaymentMethod`: Dropdown to select a payment type.
- `etAmount`: Input field for payment amount.
- `etProvider` & `etTransactionReference`: Shown for non-cash payments.
- `btnOK`: Confirms and adds the payment.
- `btnCancel`: Closes the dialog.

---

## 5. Error Handling

- Prevents duplicate payment types in `AddPaymentDialog`.
- Displays a message if all payment types are already added.
- Handles empty input validation before adding a payment.

---
