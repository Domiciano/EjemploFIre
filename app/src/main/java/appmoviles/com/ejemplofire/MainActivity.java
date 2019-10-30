package appmoviles.com.ejemplofire;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    EditText nombre;
    EditText telefono;
    Button registrar;
    TextView contactos;

    FirebaseDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseDatabase.getInstance();

        nombre = findViewById(R.id.nombre);
        telefono = findViewById(R.id.telefono);
        registrar = findViewById(R.id.registrar);
        contactos = findViewById(R.id.contactos);
        contactos.setMovementMethod(new ScrollingMovementMethod());

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String uid = db.getReference().child("contactos")
                        .push().getKey();

                Contacto contacto = new Contacto(
                        uid,
                        nombre.getText().toString(),
                        telefono.getText().toString()
                );

                db.getReference().child("contactos")
                        .child(uid)
                        .setValue(contacto);
            }
        });

        db.getReference()
                .child("contactos")
                .child("-LsTl6y8VO9g-wh7l5s6")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Contacto contacto = dataSnapshot.getValue(Contacto.class);
                        Toast.makeText(MainActivity.this
                                , "Hola "+ contacto.getNombre(),
                                        Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        db.getReference().child("contactos")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        long hijos = dataSnapshot.getChildrenCount();
                        String texto = "# Contactos: "+hijos +"\n\n";

                        for(DataSnapshot child : dataSnapshot.getChildren()){

                            Contacto contacto = child.getValue(Contacto.class);

                            texto+="\n";
                            texto+=contacto.getId()+"\n";
                            texto+=contacto.getNombre()+"\n";
                            texto+=contacto.getTelefono()+"\n";
                            texto+="\n";
                        }
                        contactos.setText(texto);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


    }
}
