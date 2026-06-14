# Electric Bill — Monthly Electricity Bill Estimator

An Android app that estimates a household's **monthly electricity bill** from the number of
units (kWh) used. It applies Malaysia's tiered (block) tariff rates, lets you apply an optional
rebate, and saves every estimate to an **offline database** so you can review, edit, or delete
past records at any time.

---

## Requirements

- An Android phone or emulator running **Android 7.0 (Nougat, API 24)** or newer.
- About 10 MB of free storage.

---

## Installation

**Option A — Install the APK on a phone**

1. Download the file **`app-release.apk`** from this repository (look in the repo files, or the
   *Releases* section if provided).
2. Copy it to your Android phone (or download it directly on the phone).
3. Tap the file to install. If Android blocks it, allow **"Install from unknown sources"** for your
   browser or file manager, then tap the APK again.
4. Open the **Electric Bill** app from your home screen / app drawer.

**Option B — Run from source in Android Studio**

1. Clone or download this repository.
2. Open the project folder in **Android Studio**.
3. Let Gradle finish syncing, then press the green **▶ Run** button to launch it on an emulator or
   a connected phone.

---

## How the Bill Is Calculated

Electricity is billed in **blocks**. Each block of usage has its own rate:

| Usage block (kWh) | Rate per kWh |
|-------------------|--------------|
| 1 – 200           | RM 0.218     |
| Next 201 – 300    | RM 0.334     |
| Next 301 – 600    | RM 0.516     |
| Next 601 – 1000   | RM 0.546     |

- **Total Charges** = the charge for the units in each block, added together.
- **Final Cost** = Total Charges − (Total Charges × rebate %).

**Worked examples**

- **250 kWh** → (200 × 0.218) + (50 × 0.334) = **RM 60.30**
- **467 kWh** → (200 × 0.218) + (100 × 0.334) + (167 × 0.516) = **RM 163.17**
- **467 kWh with a 2% rebate** → 163.17 − (163.17 × 0.02) = **RM 159.91**

---

## How to Use the App

This is the main guide. Each task below is one thing you can do in the app.

### 1. Calculate an estimate

1. Open the app. You start on the **Calculator** screen.
2. **Choose a month** from the *Month* dropdown (e.g. *January*).
3. **Enter the units used** in the *Units (kWh)* box. This must be a whole number between
   **1 and 1000**. Example: type `467`.
4. **Set the rebate** by dragging the rebate **slider** left or right. It goes from **0% to 5%**,
   and the current value is shown on screen (e.g. *Rebate: 2%*). Leave it at 0% if no rebate
   applies.
5. Tap the **CALCULATE** button.
6. The screen now shows two results:
   - **Total Charges** — the cost before any rebate.
   - **Final Cost** — the cost after the rebate is taken off.

> **If you see an error:** if the units box is empty or the number is outside 1–1000, the app
> shows a message and will not calculate. Just correct the number and tap **CALCULATE** again.

### 2. Save an estimate

1. After calculating (Step 1), tap the **SAVE** button.
2. A short message confirms the record has been saved.
3. The estimate is now stored in the app's offline database and will appear in your history.

> You must calculate first, then save — saving stores whatever is currently shown on screen.

### 3. View your saved history

1. On the Calculator screen, tap **VIEW HISTORY**.
2. The **History** screen opens, listing every saved estimate.
3. Each row shows the **month** and its **final cost** (e.g. *April — RM 159.91*).
4. To go back to the Calculator, use your device's **Back** gesture/button.

### 4. View a record's full details

1. From the **History** list, **tap any row**.
2. The **Detail** screen opens and shows all the saved information for that record:
   - Month
   - Units used (kWh)
   - Total Charges
   - Rebate %
   - Final Cost

### 5. Edit a record

1. Open a record's details (Step 4).
2. Tap the **EDIT** button.
3. The **Edit** screen opens with the record's current values filled in.
4. Change whatever you need (e.g. correct the units or the rebate).
5. Tap **UPDATE** to save your changes. The record is updated in the database, and the new values
   will show in History and Details.

### 6. Delete a record

1. Open a record's details (Step 4).
2. Tap the **DELETE** button.
3. A confirmation message appears — confirm to remove the record.
4. The record is permanently deleted from the database and disappears from your history.

> Deleting cannot be undone, so only delete records you no longer need.

### 7. Open the About page

1. On the Calculator screen, tap **ABOUT**.
2. The **About** screen shows the developer's details, the course information, usage notes, and a
   **clickable project link** — tap the link to open this GitHub page in your browser.

---

## App Screens at a Glance

| Screen        | What it does                                                            |
|---------------|-------------------------------------------------------------------------|
| Calculator    | Pick month, enter units, set rebate, calculate, and save an estimate    |
| History       | Lists all saved estimates (month + final cost)                          |
| Detail        | Shows the full details of one saved estimate; edit or delete it here    |
| Edit          | Change a saved estimate's values and update them                        |
| About         | Developer info, instructions, and a clickable project link              |

---

## Built With

- **Java** (Android)
- **Android SDK** — minimum API 24 (Android 7.0)
- **SQLite** — local/offline database for storing estimates
- **Android Studio**
