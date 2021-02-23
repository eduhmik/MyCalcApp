package com.edwin.kimaita.mycalcapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Switch;
import android.widget.Toast;

import com.edwin.kimaita.mycalcapp.databinding.HomeBinding;

public class Home extends AppCompatActivity implements View.OnClickListener {
    private HomeBinding binding;
    protected IRemote mService;
    ServiceConnection mServiceConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = HomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.add.setOnClickListener(this);
        binding.subtract.setOnClickListener(this);
        binding.division.setOnClickListener(this);
        binding.multiply.setOnClickListener(this);

        binding.result.setEnabled(false);
        binding.result.setCursorVisible(false);
        binding.result.setKeyListener(null);

        binding.tlResult.setEnabled(false);

        initConnection();
    }

    void initConnection(){
        mServiceConnection = new ServiceConnection() {

            @Override
            public void onServiceDisconnected(ComponentName name) {
                // TODO Auto-generated method stub
                mService = null;
                Toast.makeText(getApplicationContext(), "Disconnected", Toast.LENGTH_SHORT).show();
                Log.d("IRemote", "Binding - Service disconnected");
            }

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                // TODO Auto-generated method stub
                mService = IRemote.Stub.asInterface((IBinder) service);
                Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_SHORT).show();
                Log.d("IRemote", "Binding is done - Service connected");
            }
        };
        if(mService == null) {
            Intent it = new Intent();
            it.setAction("com.remote.service.CALCULATOR");
            it.setPackage(this.getPackageName());
            //binding to remote service
            bindService(it, mServiceConnection, Service.BIND_AUTO_CREATE);
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
    }



    @Override
    public void onClick(View v) {
        int value1 = Integer.parseInt(binding.value1.getText().toString());
        int value2 = Integer.parseInt(binding.value2.getText().toString());
        hideKeyboard();
        switch (v.getId()) {
            case R.id.add:
                try{
                    binding.result.setText("Result -> Add ->"+mService.add(value1, value2));
                    Log.d("IRemote", "Binding - Add operation");
                } catch (RemoteException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
            case R.id.subtract:
                try{
                    binding.result.setText("Result -> Subtract ->"+mService.subtract(value1, value2));
                    Log.d("IRemote", "Binding - Subtract operation");
                } catch (RemoteException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
            case R.id.division:
                try{
                    binding.result.setText("Result -> Divide ->"+mService.divide(value1, value2));
                    Log.d("IRemote", "Binding - Divide operation");
                } catch (RemoteException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
            case R.id.multiply:
                try{
                    binding.result.setText("Result -> Multiply ->"+mService.multiply(value1, value2));
                    Log.d("IRemote", "Binding - Multiply operation");
                } catch (RemoteException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    private void hideKeyboard(){
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (this.getCurrentFocus() != null && inputManager != null) {
            inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
            inputManager.hideSoftInputFromInputMethod(this.getCurrentFocus().getWindowToken(), 0);
        }
    }
}