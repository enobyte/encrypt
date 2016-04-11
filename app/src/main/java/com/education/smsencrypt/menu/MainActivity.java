package com.education.smsencrypt.menu;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.education.smsencrypt.R;
import com.education.smsencrypt.menu.Environment.Encrypt;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button _encryptSMS, _keyEnDe, _decrypt_sms, _helpSMS, _cancel, _sendEn;
    private ImageButton _phonenumer;
    private EditText _ciphertext, _plaintext, phoneInput, keyEnkripsi, keyDecripsi;
    static final int PICK_CONTACT = 1;
    private static String phoneNumber, textMessage, keyEncrypt = "", keyDecrypt = ""; //ciphertext, plaintext;
    private static Encrypt encrypt;
    private static byte[] ciphertext, plaintext;
    private static final Charset UTF8_CHARSET = Charset.forName("UTF-8");
    static final byte[] HEX_CHAR_TABLE = {
            (byte)'0', (byte)'1', (byte)'2', (byte)'3',
            (byte)'4', (byte)'5', (byte)'6', (byte)'7',
            (byte)'8', (byte)'9', (byte)'a', (byte)'b',
            (byte)'c', (byte)'d', (byte)'e', (byte)'f'
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //aes = new AES();
        inetView();
    }

    private void inetView() {
        _encryptSMS = (Button) findViewById(R.id.enkripsms);
        _keyEnDe = (Button) findViewById(R.id.keyende);
        _decrypt_sms = (Button) findViewById(R.id.dekripsms);
        _helpSMS = (Button) findViewById(R.id.helpsms);
        _cancel = (Button) findViewById(R.id.cancel);
        _sendEn = (Button) findViewById(R.id.senden);
        _phonenumer = (ImageButton) findViewById(R.id.phonenumber);
        _ciphertext = (EditText) findViewById(R.id.ciphertext);
        _plaintext = (EditText) findViewById(R.id.plaintext);
        phoneInput = (EditText) findViewById(R.id.numberphone);

        _encryptSMS.setOnClickListener(this);
        _keyEnDe.setOnClickListener(this);
        _decrypt_sms.setOnClickListener(this);
        _helpSMS.setOnClickListener(this);
        _cancel.setOnClickListener(this);
        _sendEn.setOnClickListener(this);
        _phonenumer.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.enkripsms:
                encryptsms();
                break;
            case R.id.dekripsms:
                decryptsms();
                break;
            case R.id.keyende:
                keyManajemnt();
                break;
            case R.id.helpsms:
                //this is a Button Help SMS
                break;
            case R.id.cancel:
                finish();
                //this is a Button cancel
                break;
            case R.id.senden:
                sendEncrypt();
                break;
            case R.id.phonenumber:
                pickContact();
                break;
        }

    }

    private void keyManajemnt() {
        final AlertDialog.Builder alerDialog = new AlertDialog.Builder(MainActivity.this);
        alerDialog.setTitle("Pilih Implementasi Kunci");
        alerDialog.setPositiveButton("Key Encrypt", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final AlertDialog.Builder alertEncrypt = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                final View root = (View) inflater.inflate(R.layout.keyencrypt, null);
                keyEnkripsi = (EditText) root.findViewById(R.id.keyEncrypt);
                alertEncrypt.setView(root);
                alertEncrypt.setTitle("Key Encypt");
                alertEncrypt.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (keyEnkripsi.getText().toString().trim().length() < 1) {
                            Toast.makeText(MainActivity.this, "Create Key failure, please do not empty", Toast.LENGTH_SHORT).show();
                        } else {
                            keyEncrypt = keyEnkripsi.getText().toString();
                            Toast.makeText(MainActivity.this, "Key has been created", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                alertEncrypt.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertEncrypt.show();
            }
        });
        alerDialog.setNegativeButton("Key Decrypt", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //something happen
                final AlertDialog.Builder alertEncrypt = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                final View root = (View) inflater.inflate(R.layout.keydecrypt, null);
                keyDecripsi = (EditText) root.findViewById(R.id.keyDecrypt);
                alertEncrypt.setView(root);
                alertEncrypt.setTitle("Key Decrypt");
                alertEncrypt.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (keyDecripsi.getText().toString().trim().length() < 1) {
                            Toast.makeText(MainActivity.this, "Create Key failure, please do not empty", Toast.LENGTH_SHORT).show();
                        } else {
                            keyDecrypt = keyDecripsi.getText().toString();
                            Toast.makeText(MainActivity.this, "Key has been created", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                alertEncrypt.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertEncrypt.show();
            }
        });
        alerDialog.show();
    }

    private void sendEncrypt() {
        if (_ciphertext.getText().toString().trim().length() < 1) {
            Toast.makeText(MainActivity.this, "Ciphertext harus diisi", Toast.LENGTH_SHORT).show();
        } else if (phoneInput.getText().toString().trim().length() < 1) {
            Toast.makeText(MainActivity.this, "Nomer tujuan harus diisi", Toast.LENGTH_SHORT).show();
        } else {
            //Handle Sender
            phoneNumber = phoneInput.getText().toString();
            textMessage = _ciphertext.getText().toString();
            sendSMS(phoneNumber, textMessage);
        }
    }

    private void sendplaintext() {
        if (_plaintext.getText().toString().trim().length() < 1) {
            Toast.makeText(MainActivity.this, "Plaintext harus diisi", Toast.LENGTH_SHORT).show();
        } else if (phoneInput.getText().toString().trim().length() < 1) {
            Toast.makeText(MainActivity.this, "Nomer tujuan harus diisi", Toast.LENGTH_SHORT).show();
        } else {
            //Handle Sender
            phoneNumber = phoneInput.getText().toString();
            textMessage = _plaintext.getText().toString();
            sendSMS(phoneNumber, textMessage);
        }
    }


    private void decryptsms() {
        try {
            if (_ciphertext.getText().toString().trim().length() < 1) {
                Toast.makeText(MainActivity.this, "Cipher text kosong", Toast.LENGTH_SHORT).show();
            } else if (keyDecrypt.trim().length() < 1 || keyDecrypt == null || keyDecrypt.equalsIgnoreCase("")) {
                Toast.makeText(MainActivity.this, "Key kosong harus diisi", Toast.LENGTH_SHORT).show();
            } else {
                plaintext = encrypt.decrypt(ciphertext, keyDecrypt.getBytes());
                //aes.decrypt(_ciphertext.getText().toString().getBytes(), keyDecrypt.getBytes());
                _plaintext.setText(new String(plaintext));
                keyDecrypt = "";
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "Key tidak valid", Toast.LENGTH_SHORT).show();
            keyDecrypt = "";
        }

    }

    private void encryptsms() {
        try {
            if (_plaintext.getText().toString().trim().length() < 1) {
                Toast.makeText(MainActivity.this, "Plaintext harus diisi", Toast.LENGTH_SHORT).show();
            } else if (keyEncrypt.trim().length() < 1 || keyEnkripsi == null || keyEncrypt.equalsIgnoreCase("")) {
                Toast.makeText(MainActivity.this, "Key kosong harus diisi", Toast.LENGTH_SHORT).show();
            } else if ((keyEncrypt.length() != 16) && (keyEncrypt.length() != 24) && (keyEncrypt.length() != 32)) {
                Toast.makeText(MainActivity.this, "Key harus 128/192/256 bits (16/24/32 bytes karater)", Toast.LENGTH_SHORT).show();
            } else {
                String pl = _plaintext.getText().toString();
                ciphertext= encrypt.encrypt(_plaintext.getText().toString().getBytes(), keyEncrypt.getBytes());
                //aes.encrypt(pl.getBytes(), keyEncrypt.getBytes());
                System.out.print("Key" + keyEncrypt);
                System.out.print("_Plaintext" + _plaintext);
                _ciphertext.setText(getHexString(ciphertext));
                keyEncrypt = "";

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getHexString(byte[] raw)
            throws UnsupportedEncodingException
    {
        byte[] hex = new byte[2 * raw.length];
        int index = 0;

        for (byte b : raw) {
            int v = b & 0xFF;
            hex[index++] = HEX_CHAR_TABLE[v >>> 4];
            hex[index++] = HEX_CHAR_TABLE[v & 0xF];
        }
        return new String(hex, "ASCII");
    }

    // ============================ Method untuk Intent Ke List Contact =====================================//
    private void pickContact() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
        startActivityForResult(intent, PICK_CONTACT);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PICK_CONTACT:
                    Cursor cursor = null;
                    String phoneNumber = "";
                    List<String> allNumbers = new ArrayList<String>();
                    int phoneIdx = 0;
                    try {
                        Uri result = data.getData();
                        String id = result.getLastPathSegment();
                        cursor = getContentResolver().query(Phone.CONTENT_URI, null, Phone.CONTACT_ID + "=?", new String[]{id}, null);
                        phoneIdx = cursor.getColumnIndex(Phone.DATA);
                        if (cursor.moveToFirst()) {
                            while (cursor.isAfterLast() == false) {
                                phoneNumber = cursor.getString(phoneIdx);
                                allNumbers.add(phoneNumber);
                                cursor.moveToNext();
                            }
                        } else {
                            //no results actions
                        }
                    } catch (Exception e) {
                        //error actions
                    } finally {
                        if (cursor != null) {
                            cursor.close();
                        }

                        final CharSequence[] items = allNumbers.toArray(new String[allNumbers.size()]);
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Choose a number");
                        builder.setItems(items, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                String selectedNumber = items[item].toString();
                                selectedNumber = selectedNumber.replace("-", "");
                                phoneInput.setText(String.valueOf(selectedNumber));
                            }
                        });
                        AlertDialog alert = builder.create();
                        if (allNumbers.size() > 1) {
                            alert.show();
                        } else {
                            String selectedNumber = phoneNumber.toString();
                            selectedNumber = selectedNumber.replace("-", "");
                            phoneInput.setText(String.valueOf(selectedNumber));
                        }

                        if (phoneNumber.length() == 0) {
                            //no numbers found actions
                        }
                    }
                    break;
            }
        } else {
            //activity result error actions
        }
    }

    // ============================ End Method =====================================//

    private void sendSMS(String phoneNumber, String message) {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
                new Intent(SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
                new Intent(DELIVERED), 0);

        //---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS sent",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Generic failure",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No service",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio off",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));

        //---when the SMS has been delivered---
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS not delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
    }
}
