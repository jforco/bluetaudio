package frc.bluetooha2dp;

import android.app.Activity;
import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class InicioActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTextMessage;
    private Button botonEncender;
    private Button botonApagar;

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothProfile mA2DPSinkProxy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        mTextMessage = (TextView) findViewById(R.id.textInicial);
        botonEncender = (Button) findViewById(R.id.botonEncender);
        botonApagar = (Button) findViewById(R.id.botonApagar);

        botonEncender.setOnClickListener(this);
        botonApagar.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.botonEncender:{
                encenderB();
                break;
            }
            case R.id.botonApagar:{
                apagarB();
                break;
            }
            default:{
                Toast t = Toast.makeText(getApplicationContext(), "No es valido", Toast.LENGTH_SHORT);
                t.show();
            }
        }
    }



    public void encenderB(){
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast t = Toast.makeText(getApplicationContext(), "Bluetooth no soportado", Toast.LENGTH_SHORT);
            t.show();
            return;
        }
        if (!mBluetoothAdapter.isEnabled()) {
            //Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            //startActivityForResult(enableBtIntent, 1);
            Intent discoverableIntent =new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            startActivityForResult(discoverableIntent, 1);
            return;
        }
        //iniciar perfil a2dp
        mBluetoothAdapter.setName("BluetAudio frc");
        mBluetoothAdapter.getProfileProxy(this, new BluetoothProfile.ServiceListener() {
            @Override
            public void onServiceConnected(int profile, BluetoothProfile proxy) {
                mA2DPSinkProxy = proxy;
            }
            @Override
            public void onServiceDisconnected(int profile) {


            }
        }, BluetoothProfile.A2DP);

        //configureButton();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Toast t = Toast.makeText(getApplicationContext(), "Bluetooth encendido", Toast.LENGTH_SHORT);
                t.show();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast t = Toast.makeText(getApplicationContext(), "Se necesita encender el Bluetooth para continuar", Toast.LENGTH_SHORT);
                t.show();
            }
        }
    }

    public void apagarB(){
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter.isEnabled()) {
            boolean disable = mBluetoothAdapter.disable();
        }
        Toast t = Toast.makeText(getApplicationContext(), "Bluetooh apagado", Toast.LENGTH_SHORT);
        t.show();
    }

}
