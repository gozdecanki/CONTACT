package com.example.gozde.denemedeneme;

import android.Manifest;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView mylistview;     //to access the listview
    ArrayList<PERSON> person_info; //to add person's info
    MyAdapter myadapter;         // to control listview
    private static final int CONTACTS_PERMISSIONS_REQUEST = 100;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        person_info = new ArrayList<PERSON>();
        mylistview = (ListView) findViewById(R.id.mylist);

        receive_person_info();   //to get all person info from the contact
        showmylist(person_info);

        //to call when clicking a person in the listview
        mylistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent callintent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + person_info.get(position).getP_num1()));
                startActivity(callintent);
            }
        });

    }


    //permission code
    private void requestPermissions() {
        String locationPermission = Manifest.permission.READ_CONTACTS;
        String calendarPermission = Manifest.permission.WRITE_CONTACTS;
        int hasLocPermission = 0;
        int hasCalPermission = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            hasLocPermission = checkSelfPermission(locationPermission);
            hasCalPermission = checkSelfPermission(calendarPermission);
        }

        List<String> permissions = new ArrayList<String>();
        if (hasLocPermission != PackageManager.PERMISSION_GRANTED) {
            permissions.add(locationPermission);
        }
        if (hasCalPermission != PackageManager.PERMISSION_GRANTED) {
            permissions.add(calendarPermission);
        }
        if (!permissions.isEmpty()) {
            String[] params = permissions.toArray(new String[permissions.size()]);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(params, CONTACTS_PERMISSIONS_REQUEST);
            }
        } else {

        }
    }

    //to get all person info from the contact
    private void receive_person_info() {
        requestPermissions();

        Uri content_uri = ContactsContract.Contacts.CONTENT_URI;

        String _id = ContactsContract.Contacts._ID;
        String _name = ContactsContract.Contacts.DISPLAY_NAME;

        String tel_state = ContactsContract.Contacts.HAS_PHONE_NUMBER;
        Uri phone_uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String phone_id = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String number = ContactsContract.CommonDataKinds.Phone.NUMBER;


        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(content_uri, null, null, null, null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(_id));
                String name = cursor.getString(cursor.getColumnIndex(_name));
                String phone_num = null;

                int Tel_state = Integer.parseInt(cursor.getString(cursor.getColumnIndex(tel_state)));
                if (Tel_state > 0) {

                    Cursor phone_cursor = contentResolver.query(phone_uri, null, phone_id + " = ?", new String[]{id}, null);

                    while (phone_cursor.moveToNext()) {
                        phone_num = phone_cursor.getString(phone_cursor.getColumnIndex(number));
                        person_info.add(new PERSON(name, phone_num, R.drawable.no_photo));
                    }
                    phone_cursor.close();
                }

            }
        }
        myadapter = new MyAdapter(this, R.layout.row_layout, person_info);
        if (mylistview != null) {
            mylistview.setAdapter(myadapter);

        }
    }

    public void showmylist(ArrayList<PERSON> person_info) {

        PERSON p = null;

        for (int i = 0; i < person_info.size(); i++) {
            p = person_info.get(i);
        }

    }
    //  filtering methods for  phone numbers
    public void radbtn_filter(View view) {
        PERSON p = null;
        String p_num = null;
        ArrayList<PERSON> filtered = new ArrayList<PERSON>();
        //initializing radio buttons
        RadioButton hepsi = (RadioButton) findViewById(R.id.rdb_all);
        RadioButton avea = (RadioButton) findViewById(R.id.rdb_avea);
        RadioButton turkcell = (RadioButton) findViewById(R.id.rdb_turkcell);
        RadioButton vodafone = (RadioButton) findViewById(R.id.rdb_vodafone);
        boolean select = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            //to filter AVEA numbers.THE NUMBERS start with "050" and "057"
            case R.id.rdb_avea:

                hepsi.setChecked(false);
                vodafone.setChecked(false);
                turkcell.setChecked(false);
                for (int i = 0; i < person_info.size(); i++) {
                    p = person_info.get(i);
                    p_num = p.getP_num1();
                    String teststr = p_num.substring(1, 3);
                    if (teststr.contains("50") == true || teststr.contains("57") == true) {
                        filtered.add(new PERSON(p.getP_name(), p_num, R.drawable.no_photo));
                    }
                }
                myadapter = new MyAdapter(this, R.layout.row_layout, filtered);
                if (mylistview != null) {
                    mylistview.setAdapter(myadapter);
                }
                break;
            //to show all tel numbers
            case R.id.rdb_all:
                avea.setChecked(false);
                vodafone.setChecked(false);
                turkcell.setChecked(false);
                if (select)

                    myadapter = new MyAdapter(this, R.layout.row_layout, person_info);
                if (mylistview != null) {
                    mylistview.setAdapter(myadapter);

                }

                break;
            // to filter only VODAFONE numbers. THE NUMBERS start with "053" and "052"
            case R.id.rdb_vodafone:
                hepsi.setChecked(false);
                avea.setChecked(false);
                turkcell.setChecked(false);
                for (int i = 0; i < person_info.size(); i++) {
                    p = person_info.get(i);
                    p_num = p.getP_num1();
                    String teststr = p_num.substring(1, 3);
                    if (teststr.contains("53") == true || teststr.contains("52") == true) {
                        filtered.add(new PERSON(p.getP_name(), p_num, R.drawable.no_photo));
                    }
                }
                myadapter = new MyAdapter(this, R.layout.row_layout, filtered);
                if (mylistview != null) {
                    mylistview.setAdapter(myadapter);
                }
                break;
            //to filter only TURKCELL NUMBERS. THE NUMBERS start with "054" and "055"
            case R.id.rdb_turkcell:
                hepsi.setChecked(false);
                vodafone.setChecked(false);
                avea.setChecked(false);
                for (int i = 0; i < person_info.size(); i++) {
                    p = person_info.get(i);
                    p_num = p.getP_num1();
                    String teststr = p_num.substring(1, 3);
                    if (teststr.contains("54") == true || teststr.contains("55") == true) {
                        filtered.add(new PERSON(p.getP_name(), p_num, R.drawable.no_photo));
                    }
                }
                myadapter = new MyAdapter(this, R.layout.row_layout, filtered);
                if (mylistview != null) {
                    mylistview.setAdapter(myadapter);
                }
                break;
        }

    }

            //to write all persons info to recovery file
    public void save_info(ArrayList<PERSON> person_info) {
        PERSON saveinfo = null;
        String blank = "/";
        String end = "\n";

        try {

            // creating a file for recovery
            String filename = "savedinfo.txt";
            //creating a writer to write all contacts to recovery file (savedinfo)
            FileOutputStream writer = openFileOutput(filename, MODE_PRIVATE);


            for (int i = 0; i < person_info.size(); i++) {
                saveinfo = person_info.get(i);

                writer.write(saveinfo.getP_name().getBytes());
                writer.write(blank.getBytes());
                writer.write(saveinfo.getP_num1().getBytes());
                writer.write(blank.getBytes());
                writer.write(String.valueOf(saveinfo.getP_imageres()).getBytes());
                writer.write(blank.getBytes());
                writer.write(end.getBytes());


            }
            writer.flush();
            writer.close();


            Toast.makeText(this, "Info are saved in the file.", Toast.LENGTH_LONG).show();

        } catch (Throwable t) {

            Toast.makeText(this, "Exception: " + t.toString(), Toast.LENGTH_LONG).show();

        }


    }
           int id;//if
    public void do_backup(View view) {
        save_info(person_info);
        id=1;
    }


    public void do_recovery(View viev) throws FileNotFoundException, IOException {

        if (id==1) {

            person_info.clear();
            PERSON person = null;
            String[] veriler = new String[2000]; // String dizimizi tanımlıyoruz

            FileInputStream fileInputStream = openFileInput("savedinfo.txt");// file reader ile dosyadan verileri okuyoruz
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);// BufferedReader ile satır okumasını sağlıyoruz*/
            String satir = new String(); //string nesnemiz
            int i = 0;
            while ((satir = bufferedReader.readLine()) != null) { // Dosya sonuna gelene kadar oku
                veriler[i] = satir; // okunan değerlerin diziye satır satır aktarılması aktarılması

                ArrayList<String> aList = new ArrayList<String>(Arrays.asList(veriler[i].split("/")));
                person = new PERSON(aList.get(0), aList.get(1), Integer.parseInt(aList.get(2)));
                person_info.add(person);

                i++;
            }

            showmylist(person_info);
            myadapter = new MyAdapter(this, R.layout.row_layout, person_info);
            if (mylistview != null) {
                mylistview.setAdapter(myadapter);

            }

            newrehber(person_info);
        }
        else
        {
            Toast.makeText(this, "you should backup!", Toast.LENGTH_LONG).show();
        }
    }

     // new phone contacts
    public void newrehber(ArrayList<PERSON> person_info){
           PERSON p=null;
           for (int j=0; j<person_info.size();j++) {
           p = person_info.get(j);

           ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
           int rawContactInsertIndex = ops.size();

           ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI);
           builder.withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null);
           builder.withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null);
           ops.add(builder.build());

           // Name
           builder = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI);
           builder.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex);
           builder.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
           builder.withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, p.getP_name());
           ops.add(builder.build());

           // Number
           builder = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI);
           builder.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex);
           builder.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
           builder.withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, p.getP_num1());
           builder.withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_WORK);
           ops.add(builder.build());
           ContentResolver contentResolver = getContentResolver();
           // image
           try {
               Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(String.valueOf(p.getP_imageres())));
               ByteArrayOutputStream image = new ByteArrayOutputStream();
               bitmap.compress(Bitmap.CompressFormat.JPEG, 100, image);
               builder = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI);
               builder.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex);
               builder.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE);
               builder.withValue(ContactsContract.CommonDataKinds.Photo.PHOTO, image.toByteArray());
               ops.add(builder.build());
           } catch (Exception e) {
               e.printStackTrace();
           }


           try {
               contentResolver.applyBatch(ContactsContract.AUTHORITY, ops);
               Toast.makeText(this, "recovery is sucessful!", Toast.LENGTH_LONG).show();
           } catch (Exception e) {
               e.printStackTrace();
           }

       }
}

  }













































































































