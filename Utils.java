package id.uniq.uniqpos.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import timber.log.Timber;

/**
 * Created by Git Solution on 29/09/2017.
 */

public class Utils {

    public static int getColor(Context context, int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getColor(id);
        } else {
            //noinspection deprecation
            return context.getResources().getColor(id);
        }
    }

    public static boolean isEmailValid(TextInputEditText txtEmail) {
        String EMAIL_PATTERN =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(txtEmail.getText().toString());
        boolean isValid = matcher.matches();
        if (!isValid) txtEmail.setError("Email not valid");
        return isValid;
    }

    public static boolean isValidField(View... views) {
        boolean status = true;
        for (View v : views) {
            if (v instanceof TextInputEditText) {
                if (((TextInputEditText) v).getText().toString().isEmpty()) {
                    ((TextInputEditText) v).setError("Field can't be blank");
                    status = false;
                } else {
                    ((TextInputEditText) v).setError(null);
                }
            } else if (v instanceof MaterialBetterSpinner) {
                if (((MaterialBetterSpinner) v).getEditableText().toString().isEmpty()) {
                    status = false;
                    ((MaterialBetterSpinner) v).setError("Field can't be blank");
                }
            }
        }
        return status;
    }

    public static void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static String generateNoNota() {
        List<String> words = new ArrayList<>(Arrays.asList("M", "N", "K", "O", "H", "S", "N", "I", "A", "L"));
        String[] time = String.valueOf(System.currentTimeMillis()).split("");
        List<Integer> changes = new ArrayList();
        int max = time.length;
        int min = 1;
        for (int i = 0; i < time.length / 2; i++) {
            int index = new Random().nextInt(max - min + 1) + min;
            if (!changes.contains(index)) changes.add(index);
        }

        StringBuilder value = new StringBuilder();
        for (int i = 0; i < time.length; i++) {
            if (!time[i].isEmpty()) {
                if (changes.contains(i)) value.append(words.get(Integer.parseInt(time[i])));
                else value.append(time[i]);
            }
        }
        return value.toString();
    }

    public static void registerToCurrency(TextInputEditText... editTexts) {
        for (final TextInputEditText editText : editTexts) {
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                    if (editText.getText().toString().trim().length() > 0) {
                        editText.removeTextChangedListener(this);
                        try {
                            String strNumber = s.toString().replace(",", "");
                            strNumber = strNumber.replace(".", "");
                            int number = Integer.parseInt(strNumber);
                            String currency = NumberFormat.getInstance().format(number);
                            editText.setText(currency);
                            editText.setSelection(currency.length());
                        } catch (Exception e) {
                        }
                        editText.addTextChangedListener(this);
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });
        }
    }

    static class TextWatcherCurrency implements TextWatcher {
        private EditText editText;
        private TextInputEditText textInputEditText;

        public TextWatcherCurrency(EditText editText) {
            this.editText = editText;
        }

        public TextWatcherCurrency(TextInputEditText textInputEditText) {
            this.textInputEditText = textInputEditText;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        @Override
        public void onTextChanged(CharSequence s, int i, int i1, int i2) {
            String value = editText != null ? editText.getText().toString() : textInputEditText.getText().toString();

        }
        @Override
        public void afterTextChanged(Editable editable) {}
    }

    public static void getDeviceID(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        telephonyManager.getDeviceId();
    }

    public static Long getMinTimeMillisToday() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    public static Long getMaxTimeMillisToday() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTimeInMillis();
    }

    public static Long currentTimeUpdate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MINUTE, -3);
        return cal.getTimeInMillis();
    }

    public static void registerToDatePicker(Activity activity, TextInputEditText... editText) {
        for (TextInputEditText edt : editText) {
            edt.setOnClickListener(new DatePickerListener(activity, edt));
        }
    }

    static class DatePickerListener implements View.OnClickListener {

        private Activity activity;
        private TextInputEditText editText;
        private Calendar calendar;

        public DatePickerListener(Activity activity, TextInputEditText editText) {
            this.activity = activity;
            this.editText = editText;
        }

        @Override
        public void onClick(View view) {
            Calendar now = Calendar.getInstance();
            if (calendar == null) calendar = now;
            DatePickerDialog dpd = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                    String tgl = String.format("%02d", dayOfMonth) + "-" + String.format("%02d", monthOfYear + 1) + "-" + year;
                    editText.setText(tgl);
                }
            },calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
            dpd.setMaxDate(now);
            dpd.setVersion(DatePickerDialog.Version.VERSION_1);
            dpd.show(activity.getFragmentManager(), "Pilih Tanggal");
        }
    }

    public static void appendLog(String text, String type) {
        File externalStorageDir = Environment.getExternalStorageDirectory();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy_MM_dd");
        String parent = externalStorageDir+"/UNIQ/"+sdfDay.format(new Date());
        File logFile = new File(parent,"uniq_log_"+type+"_"+cal.get(Calendar.DAY_OF_MONTH)+"_"+cal.get(Calendar.HOUR_OF_DAY)+".txt");
        if(!new File(parent).exists()){
            new File(parent).mkdirs();
        }
        if (!logFile.exists())
        {
            try {
                logFile.createNewFile();
            }catch (IOException e) {
                Timber.d("Create log file error. " + e);
            }
        }

        try{
            SimpleDateFormat sdf = new SimpleDateFormat( "dd-MM-yyyy HH:mm:ss");
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append("\n"+sdf.format(new Date()) + " : " +text);
            buf.newLine();
            buf.close();
        }catch (IOException e) {
            Timber.d("Write log file error. "+e);
        }
    }
}
