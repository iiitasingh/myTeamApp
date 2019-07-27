package com.ashish.myteam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.google.android.material.textfield.TextInputEditText;
import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.ExceptionHandler;
import com.kosalgeek.genasync12.PostResponseAsyncTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.ashish.myteam.SplashScreen.UserData;
import static com.ashish.myteam.SplashScreen.getUser;
import static com.ashish.myteam.SplashScreen.getprofileUserdata;
import static com.ashish.myteam.SplashScreen.pos;
import static com.ashish.myteam.SplashScreen.profileUser;

public class Resume extends AppCompatActivity {

    ImageView resumeWallpaper, resumeImgEdit;
    TextView resumeName, resumeDesignation, textVwabout, resumeHobbies, resumeAbout, resumeEmail, resumeEditBtn;
    CircleImageView circleResumeImageView;
    final int REQUEST_CODE_GALLERY = 888;
    Dialog dialog, updateDialog, aboutPopup, hobbiesPopup;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    String newImg;
    int position;
    User owner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_resume);

        position = getIntent().getIntExtra("position",0);

        resumeWallpaper = (ImageView) findViewById(R.id.resumeWallpaper);
        resumeImgEdit = (ImageView) findViewById(R.id.resumeImgEdit);
        resumeName = (TextView) findViewById(R.id.resumeName);
        resumeDesignation = (TextView) findViewById(R.id.resumeDesignation);
        textVwabout = (TextView) findViewById(R.id.textVwabout);
        resumeHobbies = (TextView) findViewById(R.id.resumeHobbies);
        resumeAbout = (TextView) findViewById(R.id.resumeAbout);
        resumeEmail = (TextView) findViewById(R.id.resumeEmail);
        resumeEditBtn = (TextView) findViewById(R.id.resumeEditBtn);
        circleResumeImageView = (CircleImageView) findViewById(R.id.circleResumeImageView);
        dialog = new Dialog(this);
        updateDialog = new Dialog(this);
        aboutPopup = new Dialog(this);
        hobbiesPopup = new Dialog(this);

        if(position == pos){
            owner = profileUser;
            resumeImgEdit.setVisibility(View.VISIBLE);
            resumeEditBtn.setVisibility(View.VISIBLE);
            setResumeData(owner);
        }else {
            owner = UserData.get(position);
            setResumeData(owner);
        }

        resumeImgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogUpdateImg(Resume.this, profileUser.email);
            }
        });

        resumeAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aboutPopup(Resume.this,owner);
            }
        });

    }

    public void showDefaultPopupMenu(View view) {
        showPopupMenu(view, false, R.style.MyPopupStyle);
    }

    public void updateUserData() {

        PostResponseAsyncTask taskRead = new PostResponseAsyncTask(this, getUser, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                UserData = new JsonConverter<User>().toArrayList(s, User.class);

                if (UserData.size() > 0) {
                    getprofileUserdata(UserData);
                    owner = profileUser;
                    setResumeData(owner);
                }
            }
        });
        taskRead.setExceptionHandler(new ExceptionHandler() {
            @Override
            public void handleException(Exception e) {
                if (e != null && e.getMessage() != null) {
                    Toast.makeText(getApplicationContext(),
                            "" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        taskRead.execute("https://voteforashish.000webhostapp.com/myTeam/getUserData.php");

    }

    public void setResumeData(User resumeMail) {

        byte[] usrerImg = java.util.Base64.getMimeDecoder().decode(resumeMail.image);
        Bitmap bitmap = BitmapFactory.decodeByteArray(usrerImg, 0, usrerImg.length);
        circleResumeImageView.setImageBitmap(bitmap);
        resumeName.setText(resumeMail.name);
        resumeDesignation.setText(resumeMail.designation);
        resumeHobbies.setText(resumeMail.hobbies);
        textVwabout.setText(resumeMail.about);
        resumeEmail.setText(resumeMail.email);
    }

    ImageView uploadImg;
    Button uploadBtn;

    private void showDialogUpdateImg(final Activity activity, final String mail) {

        dialog.setContentView(R.layout.popup_upload_img);
        uploadImg = (ImageView) dialog.findViewById(R.id.UpimageView);
        uploadBtn = (Button) dialog.findViewById(R.id.uploadbutton);

        int width1 = (int) (activity.getResources().getDisplayMetrics().widthPixels * .67);
        int height1 = (int) (activity.getResources().getDisplayMetrics().heightPixels * 0.57);
        dialog.getWindow().setLayout(width1, height1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        uploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(
                        Resume.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY
                );
            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    newImg = imageToString(uploadImg);
                    HashMap<String, String> updateImg = new HashMap<>();
                    updateImg.put("email", mail);
                    updateImg.put("img", newImg);
                    PostResponseAsyncTask taskRead = new PostResponseAsyncTask(activity, updateImg, new AsyncResponse() {
                        @Override
                        public void processFinish(String s) {
                            if (s.contains("Success")) {
                                dialog.dismiss();
                                updateUserData();
                                Toast.makeText(getApplicationContext(), "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Error!!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    taskRead.execute("https://voteforashish.000webhostapp.com/myTeam/updateImg.php");
                } catch (Exception error) {
                    Log.e("Update error", error.getMessage());
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CODE_GALLERY) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            } else {
                Toast.makeText(getApplicationContext(), "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            CropImage.activity(uri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .setRequestedSize(512, 512, CropImageView.RequestSizeOptions.RESIZE_INSIDE)
                    .start(Resume.this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    InputStream inputStream = getContentResolver().openInputStream(resultUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    uploadImg.setImageBitmap(bitmap);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    public String imageToString(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        String encodedImage = android.util.Base64.encodeToString(byteArray, Base64.DEFAULT);
        return encodedImage;
    }


    ImageView Usimgpop2;
    TextView MemNamepop2, MemEmailpop2, MemTeampop2, MemDobpop2, MemFoodpop2, MemMobpop2, MemBgppop2, MemNknampop2, MemDesig;
    byte[] userimgpop2;

    private void aboutPopup(Activity popActivity, final User userAbout) {

        aboutPopup.setContentView(R.layout.popup_about);

        int width1 = (int) (popActivity.getResources().getDisplayMetrics().widthPixels * .82);
        int height1 = (int) (popActivity.getResources().getDisplayMetrics().heightPixels * 0.75);
        aboutPopup.getWindow().setLayout(width1, height1);
        aboutPopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        aboutPopup.show();

        Usimgpop2 = (ImageView) aboutPopup.findViewById(R.id.Uimg);
        MemNknampop2 = (TextView) aboutPopup.findViewById(R.id.MemNknam);
        MemNamepop2 = (TextView) aboutPopup.findViewById(R.id.MemName);
        MemEmailpop2 = (TextView) aboutPopup.findViewById(R.id.MemEmail);
        MemTeampop2 = (TextView) aboutPopup.findViewById(R.id.MemTeam);
        MemDobpop2 = (TextView) aboutPopup.findViewById(R.id.MemDob);
        MemFoodpop2 = (TextView) aboutPopup.findViewById(R.id.MemFood);
        MemMobpop2 = (TextView) aboutPopup.findViewById(R.id.MemMob);
        MemBgppop2 = (TextView) aboutPopup.findViewById(R.id.MemBgp);
        MemDesig = (TextView) aboutPopup.findViewById(R.id.MemDesig);

        MemEmailpop2.setText("Email: " + userAbout.email);
        MemNamepop2.setText(userAbout.name);
        MemNknampop2.setText(userAbout.nickname);
        MemDesig.setText(userAbout.designation);
        MemTeampop2.setText("Team: " + userAbout.team);
        MemFoodpop2.setText("Food: " + userAbout.food);
        MemBgppop2.setText("BGrp: " + userAbout.bl_grp);
        MemMobpop2.setText("Mob: " + userAbout.mobile);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date data = null;
        try {
            data = sdf.parse(userAbout.dob);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        MemDobpop2.setText("DOB: " + DateFormat.format("dd MMMM", data));
        userimgpop2 = java.util.Base64.getMimeDecoder().decode(userAbout.image);
        Bitmap bitmap = BitmapFactory.decodeByteArray(userimgpop2, 0, userimgpop2.length);
        Usimgpop2.setImageBitmap(bitmap);
    }

    TextInputEditText editNknm, editDob, editMob, editBgrp, editFood, editPan, editAadhar, editDesig;
    Button updatebtn;

    private void aboutEditPopup(Activity activity, final User resumeUsr) {

        updateDialog.setContentView(R.layout.popup_edit_about);
        editNknm = (TextInputEditText) updateDialog.findViewById(R.id.name_edit_text1);
        editDob = (TextInputEditText) updateDialog.findViewById(R.id.name_edit_text2);
        editMob = (TextInputEditText) updateDialog.findViewById(R.id.name_edit_text3);
        editBgrp = (TextInputEditText) updateDialog.findViewById(R.id.name_edit_text4);
        editFood = (TextInputEditText) updateDialog.findViewById(R.id.name_edit_text5);
        editPan = (TextInputEditText) updateDialog.findViewById(R.id.name_edit_text6);
        editAadhar = (TextInputEditText) updateDialog.findViewById(R.id.name_edit_text7);
        editDesig = (TextInputEditText) updateDialog.findViewById(R.id.name_edit_text8);
        updatebtn = (Button) updateDialog.findViewById(R.id.ProfileUpdatebtn);

        int width1 = (int) (activity.getResources().getDisplayMetrics().widthPixels * .83);
        int height1 = (int) (activity.getResources().getDisplayMetrics().heightPixels * 0.85);
        updateDialog.getWindow().setLayout(width1, height1);
        updateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        updateDialog.show();

        editNknm.setText(resumeUsr.nickname);
        editDob.setText(resumeUsr.dob);
        editFood.setText(resumeUsr.food);
        editMob.setText(resumeUsr.mobile);
        editBgrp.setText(resumeUsr.bl_grp);
        editPan.setText(resumeUsr.pan_no);
        editAadhar.setText(resumeUsr.aadhaar);
        editDesig.setText(resumeUsr.designation);

        editDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dobPicker = new DatePickerDialog(
                        Resume.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                dobPicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dobPicker.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
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
                editDob.setText(date);
            }
        };

        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserDetails(resumeUsr.email);
            }
        });
    }

    public void updateUserDetails(final String mailid) {
        String nkname = editNknm.getText().toString().trim();
        String dob = editDob.getText().toString().trim();
        String mob = editMob.getText().toString().trim();
        String blgp = editBgrp.getText().toString().trim();
        String food = editFood.getText().toString().trim();
        String pan = editPan.getText().toString().trim();
        String aadhar = editAadhar.getText().toString().trim();
        String designation = editDesig.getText().toString().trim();

        if (nkname.length() >= 3 && dob.length() != 0 && mob.length() == 10 && blgp.length() >= 2 && food.length() >= 3 && pan.length() == 10 && aadhar.length() == 12 && designation.length() > 5) {
            HashMap<String, String> updateUser = new HashMap<>();
            updateUser.put("email", mailid);
            updateUser.put("nkname", nkname);
            updateUser.put("dob", dob);
            updateUser.put("mob", mob);
            updateUser.put("blgrp", blgp);
            updateUser.put("food", food);
            updateUser.put("pan", pan);
            updateUser.put("aadhaar", aadhar);
            updateUser.put("desig", designation);

            PostResponseAsyncTask taskupdate = new PostResponseAsyncTask(this, updateUser, new AsyncResponse() {
                @Override
                public void processFinish(String s) {
                    if (s.contains("Success")) {
                        updateDialog.dismiss();
                        updateUserData();
                        Toast.makeText(getApplicationContext(), "Updated successfully!!!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            taskupdate.setExceptionHandler(new ExceptionHandler() {
                @Override
                public void handleException(Exception e) {
                    if (e != null && e.getMessage() != null) {
                        Toast.makeText(getApplicationContext(),
                                "" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
            taskupdate.execute("https://voteforashish.000webhostapp.com/myTeam/updateUser.php");
        } else if (nkname.length() < 3) {
            Toast.makeText(Resume.this, "Nick name should have atleast 3 characters", Toast.LENGTH_SHORT).show();
        } else if (dob.length() == 0) {
            Toast.makeText(Resume.this, "DOB should not be null", Toast.LENGTH_SHORT).show();
        } else if (mob.length() < 10) {
            Toast.makeText(Resume.this, "Mobile no should contain 10 digits", Toast.LENGTH_SHORT).show();
        } else if (blgp.length() < 2) {
            Toast.makeText(Resume.this, "Blood Grp needs atleast 2 characters", Toast.LENGTH_SHORT).show();
        } else if (food.length() < 3) {
            Toast.makeText(Resume.this, "Min 3 characters (Veg/Nonveg)", Toast.LENGTH_SHORT).show();
        } else if (pan.length() < 10) {
            Toast.makeText(Resume.this, "Pan contains 10 characters", Toast.LENGTH_SHORT).show();
        } else if (designation.length() < 5) {
            Toast.makeText(Resume.this, "fill your designation", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(Resume.this, "Aadhaar contains 12 digits", Toast.LENGTH_SHORT).show();
        }
    }

    TextInputEditText yourself, hobby;
    Button updateAbt;

    private void aboutYourselfPopup(Activity activity, final User resumeUsr) {

        hobbiesPopup.setContentView(R.layout.popup_hobby);
        yourself = (TextInputEditText) hobbiesPopup.findViewById(R.id.yourself);
        hobby = (TextInputEditText) hobbiesPopup.findViewById(R.id.hobby);
        updateAbt = (Button) hobbiesPopup.findViewById(R.id.AbtUpdatebtn);

        int width1 = (int) (activity.getResources().getDisplayMetrics().widthPixels * .8);
        int height1 = (int) (activity.getResources().getDisplayMetrics().heightPixels * 0.6);
        hobbiesPopup.getWindow().setLayout(width1, height1);
        hobbiesPopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        hobbiesPopup.show();

        yourself.setText(resumeUsr.about);
        hobby.setText(resumeUsr.hobbies);

        updateAbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserAbout(resumeUsr.email);
            }
        });
    }

    public void updateUserAbout(String mailid) {

        String abt = yourself.getText().toString().trim();
        String hob = hobby.getText().toString().trim();

        if (abt.length() >= 10 && hob.length() >= 10) {

            HashMap<String, String> updateabout = new HashMap<>();
            updateabout.put("email", mailid);
            updateabout.put("about", abt);
            updateabout.put("hobby", SignUp.capitailizeWord(hob));

            PostResponseAsyncTask taskRead = new PostResponseAsyncTask(this, updateabout, new AsyncResponse() {
                @Override
                public void processFinish(String s) {
                    if (s.contains("Success")) {
                        hobbiesPopup.dismiss();
                        updateUserData();
                        Toast.makeText(getApplicationContext(), "Updated successfully!!!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            taskRead.execute("https://voteforashish.000webhostapp.com/myTeam/updateUserAbout.php");
        } else if (abt.length() < 10) {
            Toast.makeText(Resume.this, "minimum 10 characters in about", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(Resume.this, "minimum 10 characters in hoobies", Toast.LENGTH_SHORT).show();
        }
    }

    private void showPopupMenu(View anchor, boolean isWithIcons, int style) {
        //init the wrapper with style
        Context wrapper = new ContextThemeWrapper(this, style);

        //init the popup
        PopupMenu popup = new PopupMenu(wrapper, anchor);

        /*  The below code in try catch is responsible to display icons*/

        //inflate menu
        popup.getMenuInflater().inflate(R.menu.popup_edit_menues, popup.getMenu());

        //implement click events
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu1:
                        aboutEditPopup(Resume.this, owner);
                        break;
                    case R.id.menu2:
                        aboutYourselfPopup(Resume.this, owner);
                        break;
                    case R.id.menu3:
                        Toast.makeText(Resume.this, "Coming Soon", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menu4:
                        Toast.makeText(Resume.this, "Coming Soon", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menu5:
                        Toast.makeText(Resume.this, "Coming Soon", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
        popup.show();
    }
}
