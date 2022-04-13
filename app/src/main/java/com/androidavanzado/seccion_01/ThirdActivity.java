package com.androidavanzado.seccion_01;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class ThirdActivity extends AppCompatActivity {

    private EditText editTextPhone;
    private EditText editTextWeb;
    private ImageButton imgBtnPhone;
    private ImageButton imgBtnWeb;
    private ImageButton imgBtnCamera;

    private final int PHONE_CALL_CODE = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third2);

        editTextPhone = (EditText) findViewById(R.id.editTextPhone);
        editTextWeb = (EditText) findViewById(R.id.editTextTextWeb);
        imgBtnPhone = (ImageButton) findViewById(R.id.imageButtonPhone);
        imgBtnWeb = (ImageButton) findViewById(R.id.imageButtonWeb);
        imgBtnCamera = (ImageButton) findViewById(R.id.imageButtonCamera);

        imgBtnPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = editTextPhone.getText().toString();
                if (phoneNumber != null && !phoneNumber.isEmpty()) {
                    // comprobar version actual de
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        // Comprobar si ha aceptado, no ha aceptado, o nunca se le ha preguntado
                        if(CheckPermission(Manifest.permission.CALL_PHONE)){
                            // Ha aceptado
                            Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+phoneNumber));
                            if (ActivityCompat.checkSelfPermission(ThirdActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) return;
                                startActivity(i);
                            }
                         else {
                            // O no ha aceptado, o es la primera vez que se le pregunta
                            if (!shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)) {
                                // No se le ha preguntado aun
                                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, PHONE_CALL_CODE);
                            } else {
                                // Ha denegado
                                Toast.makeText(ThirdActivity.this, "Please, enable the request permission", Toast.LENGTH_LONG).show();
                                Intent i = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                i.addCategory(Intent.CATEGORY_DEFAULT);
                                i.setData(Uri.parse("package:" + getPackageName()));
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                                startActivity(i);
                            }
                        }


                       // requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, PHONE_CALL_CODE);
                    } else {
                        OlderVersions(phoneNumber);
                    }
                }else {
                    Toast.makeText(ThirdActivity.this, "Insert a phone number", Toast.LENGTH_LONG);
                }
            }

            private void OlderVersions(String phoneNumber) {

                Intent intentCall = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
                if (CheckPermission(Manifest.permission.CALL_PHONE)) {
                    startActivity(intentCall);
                } else {
                    Toast.makeText(ThirdActivity.this, "You declined the access", Toast.LENGTH_LONG).show();
                }
            }
        });
        // Botón para la dirección web
        imgBtnWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = editTextWeb.getText().toString();
                String email = "soyelfranco";
                if (url != null && !url.isEmpty()) {
                    Intent intentWeb = new Intent();
                    intentWeb.setAction(Intent.ACTION_VIEW);
                    intentWeb.setData(Uri.parse("http://" + url));
                    // Contactos
                    Intent intentContacts = new Intent(Intent.ACTION_VIEW, Uri.parse("content://contacts/people"));
                    // Email rápido
                    Intent intentMailTo = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"+email));
                    // Email completo
                    Intent intentMail = new Intent(Intent.ACTION_VIEW, Uri.parse(email));
                    intentMail.setType("plain/text");
                    intentMail.putExtra(Intent.EXTRA_SUBJECT, "Mail's title");
                    intentMail.putExtra(Intent.EXTRA_TEXT, "Hi there, I love MyForm app, but...");
                    intentMail.putExtra(Intent.EXTRA_EMAIL, new String[]{"fernando@gmail.com", "antonio@gmail.com"});


                    // Teleéfono 2, sin permisos requeridos
                    Intent intentPhone = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:66611222"));
                    startActivity(intentPhone);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // Estamos en el caso del telefono
        switch (requestCode) {
            case PHONE_CALL_CODE:

                String permission = permissions[0];
                int result = grantResults[0];

                if (permission.equals(Manifest.permission.CALL_PHONE)) {
                    // Comprobar si ha sido aceptada o denegada la peticion de permiso
                    if (result == PackageManager.PERMISSION_GRANTED){
                        // Concedió su permiso
                        String phoneNumber = editTextPhone.getText().toString();
                        Intent intentCall = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) return;

                        startActivity(intentCall);
                    }else {
                        // No concedió su permiso
                        Toast.makeText(ThirdActivity.this, "You declined the access", Toast.LENGTH_LONG);
                    }
                }

                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }


    }

    private boolean CheckPermission(String permission) {
        int result = this.checkCallingOrSelfPermission(permission);
        return result == PackageManager.PERMISSION_GRANTED;
    }
}

