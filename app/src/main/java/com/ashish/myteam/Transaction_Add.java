package com.ashish.myteam;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.ExceptionHandler;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.util.ArrayList;
import java.util.HashMap;

import static com.ashish.myteam.SplashScreen.UserMap;
import static com.ashish.myteam.SplashScreen.cardList;
import static com.ashish.myteam.SplashScreen.profileUser;

public class Transaction_Add extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    RadioGroup transacType;
    EditText amount, transDate, transDesc;
    Spinner payee, payeeEvents;
    RadioButton radioDebit, radioCredit;
    Button transacSubmit;
    private DatePickerDialog.OnDateSetListener mDateSet;
    ArrayList<Class_Event> MyEvents;
    ArrayList<User> EventMembers;
    String[] nothing = {""};
    String transacMode;
    Class_Event selectedEvent;
    User selectedMember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_transaction_add);

        transacType = (RadioGroup) findViewById(R.id.typeGrp);
        amount = (EditText) findViewById(R.id.transactionAmount);
        transDate = (EditText) findViewById(R.id.transactionDate);
        payee = (Spinner) findViewById(R.id.payeeMember);
        payeeEvents = (Spinner) findViewById(R.id.payeeEvents);
        radioDebit = (RadioButton) findViewById(R.id.RadioDebit);
        radioCredit = (RadioButton) findViewById(R.id.RadioCredit);
        transacSubmit = (Button) findViewById(R.id.TransacSubmit);
        transDesc = (EditText) findViewById(R.id.transactionDesc);
        MyEvents = new ArrayList<>();
        EventMembers = new ArrayList<>();

        for (int i = 0; i < cardList.size(); i++) {
            if (cardList.get(i).eOwner.equals(profileUser.email) && cardList.get(i).eContiBool.equals("true")) {
                MyEvents.add(cardList.get(i));
            }
        }

        if (MyEvents.size() == 0) {
            Toast.makeText(Transaction_Add.this, "No Events", Toast.LENGTH_LONG).show();
        } else {
            if (transacType.getCheckedRadioButtonId() == radioCredit.getId() && MyEvents.size() > 0) {
                ArrayAdapter<Class_Event> dataAdapter = new ArrayAdapter<Class_Event>(Transaction_Add.this, R.layout.list_spinner, MyEvents);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                transDesc.setVisibility(View.VISIBLE);
                payeeEvents.setEnabled(true);
                payeeEvents.setClickable(true);
                payeeEvents.setAdapter(dataAdapter);
                transacMode = "credit";
            }
        }
        payee.setOnItemSelectedListener(Transaction_Add.this);
        payeeEvents.setOnItemSelectedListener(Transaction_Add.this);

        transacType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if (checkedId == R.id.RadioDebit) {
                    ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(Transaction_Add.this,
                            R.layout.list_spinner, nothing);
                    dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    payee.setEnabled(false);
                    payee.setClickable(false);
                    payee.setAdapter(dataAdapter1);

                    ArrayAdapter<Class_Event> dataAdapter = new ArrayAdapter<Class_Event>(Transaction_Add.this,
                            R.layout.list_spinner, MyEvents);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    payeeEvents.setEnabled(true);
                    payeeEvents.setClickable(true);
                    payeeEvents.setAdapter(dataAdapter);
                    transDesc.setVisibility(View.VISIBLE);
                    transacMode = "debit";
                } else {
                    ArrayAdapter<User> dataAdapter2 = new ArrayAdapter<User>(Transaction_Add.this,
                            R.layout.list_spinner, EventMembers);
                    dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    payee.setEnabled(true);
                    payee.setClickable(true);
                    payee.setAdapter(dataAdapter2);

                    ArrayAdapter<Class_Event> dataAdapter = new ArrayAdapter<Class_Event>(Transaction_Add.this,
                            R.layout.list_spinner, MyEvents);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    payeeEvents.setEnabled(true);
                    payeeEvents.setClickable(true);
                    transDesc.setVisibility(View.INVISIBLE);
                    payeeEvents.setAdapter(dataAdapter);
                    transacMode = "credit";
                }
            }

        });

        transDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dobPicker = new DatePickerDialog(
                        Transaction_Add.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSet,
                        year, month, day);
                dobPicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dobPicker.show();
            }
        });

        mDateSet = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date;
                String dayS = String.valueOf(day);
                String monthS = String.valueOf(month);
                if (month < 10) {
                    monthS = "0" + monthS;
                }
                if (day < 10) {
                    dayS = "0" + dayS;
                }

                date = year + "-" + monthS + "-" + dayS;
                transDate.setText(date);
            }
        };

        transacSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                HashMap<String, String> transData = new HashMap<>();
                if (transacType.getCheckedRadioButtonId() == radioCredit.getId()) {
                    if (MyEvents.size() > 0) {
                        transData.clear();
                        String money = amount.getText().toString().trim();
                        String date = transDate.getText().toString().trim();
                        String desc = "";
                        if (money.length() > 0 && date.length() > 0) {
                            transData.put("eventId", String.valueOf(selectedEvent.eId));
                            transData.put("memberId", String.valueOf(selectedMember.id));
                            transData.put("ownerId", String.valueOf(profileUser.id));
                            transData.put("amount", money);
                            transData.put("date",date);
                            transData.put("type", transacMode);
                            transData.put("desc", desc);
                            transData.put("eventMems", selectedEvent.eMembers);
                            addNewTransaction(transData);
                        } else if (money.length() <= 0) {
                            Toast.makeText(Transaction_Add.this, "Add Some Money", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(Transaction_Add.this, "Select Transaction Date", Toast.LENGTH_LONG).show();
                        }
                    }
                    else {
                        Toast.makeText(Transaction_Add.this, "You haven't created any event!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (MyEvents.size() > 0) {
                        transData.clear();
                        String money = amount.getText().toString().trim();
                        String date = transDate.getText().toString().trim();
                        String desc = transDesc.getText().toString().trim();
                        if (money.length() > 0 && date.length() > 0 && desc.length() > 5) {
                            transData.put("eventId", String.valueOf(selectedEvent.eId));
                            transData.put("memberId", "");
                            transData.put("ownerId", String.valueOf(profileUser.id));
                            transData.put("amount", money);
                            transData.put("date",date);
                            transData.put("type", transacMode);
                            transData.put("desc", desc);
                            transData.put("eventMems", selectedEvent.eMembers);
                            addNewTransaction(transData);
                        } else if (money.length() <= 0) {
                            Toast.makeText(Transaction_Add.this, "Add Some Money", Toast.LENGTH_LONG).show();
                        } else if (date.length() <= 0) {
                            Toast.makeText(Transaction_Add.this, "Select Transaction Date", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(Transaction_Add.this, "Write Some Description", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(Transaction_Add.this, "You haven't created any event!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if (parent.getId() == R.id.payeeEvents) {
            if (MyEvents.size() > 0) {
                selectedEvent = MyEvents.get(position);
                String[] EventMemIds = selectedEvent.eMembers.split(",", 0);
                EventMembers.clear();
                for (int i = 0; i < EventMemIds.length; i++) {
                    EventMembers.add(UserMap.get(Integer.parseInt(EventMemIds[i])));
                }
                if (EventMembers.size() == 0) {
                    Toast.makeText(Transaction_Add.this, "No Member to show", Toast.LENGTH_LONG).show();
                } else {
                    if (transacType.getCheckedRadioButtonId() == radioDebit.getId()) {
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Transaction_Add.this,
                                R.layout.list_spinner, nothing);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        payee.setEnabled(false);
                        payee.setClickable(false);
                        transDesc.setVisibility(View.VISIBLE);
                        payee.setAdapter(dataAdapter);
                        transacMode = "debit";
                    } else {
                        ArrayAdapter<User> dataAdapter1 = new ArrayAdapter<User>(Transaction_Add.this,
                                R.layout.list_spinner, EventMembers);
                        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        payee.setEnabled(true);
                        payee.setClickable(true);
                        transDesc.setVisibility(View.INVISIBLE);
                        payee.setAdapter(dataAdapter1);
                        transacMode = "credit";
                    }
                }
            }
        } else if (parent.getId() == R.id.payeeMember) {
            if (EventMembers.size() > 0) {
                selectedMember = EventMembers.get(position);
                //Toast.makeText(AddTransaction.this, payeeName +"Selected", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void addNewTransaction(HashMap<String, String> transData){

        PostResponseAsyncTask loginTask = new PostResponseAsyncTask(Transaction_Add.this,
                transData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                if (s.contains("Error")) {
                    Toast.makeText(Transaction_Add.this, "transaction , Insert Error", Toast.LENGTH_LONG).show();
                } else if (s.contains("Error1")) {
                    Toast.makeText(Transaction_Add.this, "Error: event, credit_transaction", Toast.LENGTH_LONG).show();
                } else if (s.contains("Error2")) {
                    Toast.makeText(Transaction_Add.this, "Error: event, credit_member", Toast.LENGTH_LONG).show();
                }else if (s.contains("Error3")) {
                    Toast.makeText(Transaction_Add.this, "Error: user, transaction", Toast.LENGTH_LONG).show();
                }else if (s.contains("Error4")) {
                    Toast.makeText(Transaction_Add.this, "Error: event, debit_transaction", Toast.LENGTH_LONG).show();
                } else if (s.contains("Error5")) {
                    Toast.makeText(Transaction_Add.this, "Error: user, debit, transaction", Toast.LENGTH_LONG).show();
                }else if (s.contains("Error7")) {
                    Toast.makeText(Transaction_Add.this, "Error: sum, credit_transaction", Toast.LENGTH_LONG).show();
                } else if (s.contains("Error8")) {
                    Toast.makeText(Transaction_Add.this, "Error: sum, debit_transaction", Toast.LENGTH_LONG).show();
                }else if (s.contains("Error9")) {
                    Toast.makeText(Transaction_Add.this, "Error: event, update balance", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(Transaction_Add.this, "Transaction added successfully! Please swipe down to refresh.", Toast.LENGTH_LONG).show();
                    Intent events = new Intent(Transaction_Add.this, HomePage.class);
                    startActivity(events);
                    finish();
                }
            }
        });
        loginTask.setExceptionHandler(new ExceptionHandler() {
            @Override
            public void handleException(Exception e) {
                if (e != null && e.getMessage() != null) {
                    Toast.makeText(getApplicationContext(),
                            "" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        loginTask.execute("https://voteforashish.000webhostapp.com/myTeam/AddTransaction.php");
    }

}
