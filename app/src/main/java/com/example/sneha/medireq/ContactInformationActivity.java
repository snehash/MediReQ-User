package com.example.sneha.medireq;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;


public class ContactInformationActivity extends Activity {
    private BackgroundService mBoundService;
    private boolean mIsBound;
    private Profile profile;
    private EditText mName;
    private EditText mAddress;
    private EditText mEmail;
    private EditText mPhone;
    private EditText mEmer;
    private EditText mEmerNum;
    private EditText mAge;
    private EditText mBirthday;
    private EditText mWeight;
    private EditText mHeight;
    private EditText physicanName;
    private RadioButton male;
    private RadioButton female;
    private RadioButton other;
    private EditText physicanNo;
    private EditText lastVisit;
    private EditText medications;
    private EditText vitamins;
    private Button mButton;
    private Context context;
    private String currentGender;
    private String filename;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_information);
        Intent intent = new Intent(this, BackgroundService.class);
        if (!BackgroundService.STARTED) {
            startService(intent);
        }
        doBindService();
    }

    private void init(){

        Intent intent = getIntent();

        filename = intent.getStringExtra(NavigationDrawer.PROFILE);
        System.out.println("Filename is " + filename);
        profile = mBoundService.profiles.get(filename);
        getActionBar().setTitle(profile.name);
        System.out.println(mBoundService.profiles);
        System.out.println(profile);

        mName = (EditText) findViewById(R.id.edit_name);
        mAddress = (EditText) findViewById(R.id.edit_address);
        mEmail = (EditText) findViewById(R.id.edit_email);
        mPhone = (EditText) findViewById(R.id.edit_phone);
        mEmer = (EditText) findViewById(R.id.edit_emer_name);
        mEmerNum = (EditText) findViewById(R.id.edit_emer_phone);
        mButton = (Button) findViewById(R.id.bttn_save_contact);
        mAge = (EditText) findViewById(R.id.tv_age);
        mBirthday = (EditText) findViewById(R.id.tv_birthdate);
        mWeight =  (EditText) findViewById(R.id.tv_weight);
        mHeight = (EditText) findViewById(R.id.tv_height);
        physicanName = (EditText) findViewById(R.id.tv_docname);
        male = (RadioButton) findViewById(R.id.male);
        female = (RadioButton) findViewById(R.id.female);
        other = (RadioButton) findViewById(R.id.other);
        physicanNo = (EditText) findViewById(R.id.tv_docnum);
        lastVisit = (EditText) findViewById(R.id.tv_lastvisit);
        medications = (EditText) findViewById(R.id.tv_medications);
        vitamins= (EditText) findViewById(R.id.tv_vitamins);

        context = this;

        mName.setText(profile.name);
        mAddress.setText(profile.address);
        mEmail.setText(profile.email);
        mPhone.setText(profile.phone);
        mEmer.setText(profile.emergencyContact);
        mEmerNum.setText(profile.emergencyNumber);
        if(profile.age >= 0) {
            mAge.setText(((Integer)profile.age).toString());
        }
        mBirthday.setText(profile.birthday);
        if(profile.weight >= 0) {
            mWeight.setText(((Integer)profile.weight).toString());
        }
        mHeight.setText(profile.height);
        physicanName.setText(profile.docname);
        physicanNo.setText(profile.docno);
        lastVisit.setText(profile.lastVisit);
        medications.setText(profile.currMedi);
        vitamins.setText(profile.vitamins);

        switch(profile.gender){
            case "female":
                female.setChecked(true);
                male.setChecked(false);
                other.setChecked(false);
                currentGender = "female";
                break;
            case "male":
                female.setChecked(false);
                male.setChecked(true);
                other.setChecked(false);
                currentGender = "male";
                break;
            case "other":
                female.setChecked(false);
                male.setChecked(false);
                other.setChecked(true);
                currentGender = "other";
                break;
            default:
                currentGender = "";
        }


        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                profile.name = mName.getText().toString();
                if(!mAge.getText().toString().equals(""))
                    profile.age = Integer.parseInt(mAge.getText().toString());
                profile.address = mAddress.getText().toString();
                profile.email = mEmail.getText().toString();
                profile.phone = mPhone.getText().toString();
                profile.emergencyNumber = mEmerNum.getText().toString();
                profile.emergencyContact = mEmer.getText().toString();
                profile.birthday = mBirthday.getText().toString();
                if(!mWeight.getText().toString().equals(""))
                    profile.weight = Integer.parseInt(mWeight.getText().toString());
                profile.height = mHeight.getText().toString();
                profile.docname = physicanName.getText().toString();
                profile.docno = physicanNo.getText().toString();
                profile.lastVisit = lastVisit.getText().toString();
                profile.currMedi = medications.getText().toString();
                profile.vitamins = vitamins.getText().toString();
                profile.gender = currentGender;

                mBoundService.saveProfile(filename, profile);
                Toast.makeText(context, "Changes saved successfully!", Toast.LENGTH_SHORT).show();




                /*try {
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("com.example.MediReQ." + profile.getName(), Context.MODE_PRIVATE));
                    BufferedWriter writer = new BufferedWriter(outputStreamWriter);
                    writer.write(profile.getAddress());
                    writer.newLine();
                    writer.write(profile.getEmail());
                    writer.newLine();
                    writer.write(profile.getPhone());
                    writer.newLine();
                    writer.write(profile.getEmergencyContact());
                    writer.newLine();
                    writer.write(profile.getEmergencyNumber());
                    writer.close();
                } catch (IOException e) {
                    Log.e("Exception", "File write failed: " + e.toString());
                }


                Toast.makeText(context, "Changes saved!", Toast.LENGTH_SHORT).show();
                Intent returnIntent = new Intent();
                returnIntent.putExtra(NavigationDrawer.PROFILE, profile);
                setResult(RESULT_OK, returnIntent);
                finish(); */
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contact_information, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        /*
        switch(id){

            case R.id.ci_past_cond:
                Intent intent_pastcond = new Intent(context, PastConditionsActivity.class);
                intent_pastcond.putExtra(NavigationDrawer.PROFILE, filename);
                startActivity(intent_pastcond);
                break;
            case R.id.ci_surgical_history:
                Intent intent_surg = new Intent(context, SurgicalHistory.class);
                intent_surg.putExtra(NavigationDrawer.PROFILE, filename);
                startActivity(intent_surg);
                break;
            case R.id.ci_medi_allergies:
                Intent intent_allergies = new Intent(context, MedicalAllergies.class);
                intent_allergies.putExtra(NavigationDrawer.PROFILE, filename);
                startActivity(intent_allergies);
                break;
            case R.id.ci_behav:
                Intent intent_behav = new Intent(context, BehaviorActivity.class);
                intent_behav.putExtra(NavigationDrawer.PROFILE, filename);
                startActivity(intent_behav);
                break;
            case R.id.ci_famhis:
                Intent intent_fam = new Intent(context, FamilyHistoryActivity.class);
                intent_fam.putExtra(NavigationDrawer.PROFILE, filename);
                startActivity(intent_fam);
                break;

        } */

        return super.onOptionsItemSelected(item);
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            mBoundService = ((BackgroundService.LocalBinder)service).getService();
            init();
        }

        public void onServiceDisconnected(ComponentName className){ mBoundService = null;}

    };

    private void doBindService(){
        bindService(new Intent(ContactInformationActivity.this, BackgroundService.class), mConnection, 0);
        mIsBound = true;
    }

    private void doUnbindService(){
        if (mIsBound){
            unbindService(mConnection);
            mIsBound = false;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        MyApplication app = ((MyApplication)this.getApplication());
        if (System.currentTimeMillis() - app.mLastPause > 5000) {
            final ScrollView sv = (ScrollView) findViewById(R.id.contactinfo_sv);
            sv.setVisibility(View.INVISIBLE);
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
            final String pwd = pref.getString("password", "");
            if (pwd.length() > 0) {
                LayoutInflater inflater=ContactInformationActivity.this.getLayoutInflater();
                final View layout=inflater.inflate(R.layout.password, null);
                final AlertDialog d = new AlertDialog.Builder(context)
                        .setView(layout)
                        .setTitle("Enter Password")
                        .setPositiveButton(android.R.string.ok, null)
                        .setCancelable(false)
                                //.setNegativeButton(android.R.string.cancel, null)
                        .create();
                WindowManager manager = (WindowManager) getSystemService(Activity.WINDOW_SERVICE);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(d.getWindow().getAttributes());
                lp.width = manager.getDefaultDisplay().getWidth();
                lp.height = manager.getDefaultDisplay().getHeight();
                d.getWindow().setAttributes(lp);

                d.setOnShowListener(new DialogInterface.OnShowListener() {

                    @Override
                    public void onShow(DialogInterface dialog) {

                        Button b = d.getButton(AlertDialog.BUTTON_POSITIVE);
                        b.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                EditText new_password=(EditText)layout.findViewById(R.id.et_checkpassword);
                                String password1 = new_password.getText().toString();
                                if (password1.equals(pwd)) {
                                    sv.setVisibility(View.VISIBLE);
                                    d.dismiss();
                                }
                                else {
                                    Toast.makeText(context, "Incorrect password", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    }
                });
                d.show();
            }
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        ((MyApplication)this.getApplication()).mLastPause = System.currentTimeMillis();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mIsBound) {
            doUnbindService();
        }
    }

    public void onGenderButtonClicked(View view){

        switch(view.getId()){
            case R.id.male:
                currentGender = "male";
                break;
            case R.id.female:
                currentGender = "female";
                break;
            case R.id.other:
                currentGender = "other";
                break;
        }
    }
}
