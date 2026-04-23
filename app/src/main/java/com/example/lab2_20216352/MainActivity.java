package com.example.lab2_20216352;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lab2_20216352.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.tvAlumnoInfo.setText(getString(R.string.alumno_info));

        binding.btnIngresar.setOnClickListener(v -> {
            String codigo = binding.etCodigoPucp.getText() == null
                    ? ""
                    : binding.etCodigoPucp.getText().toString().trim();

            if (codigo.isEmpty()) {
                binding.etCodigoPucp.setError(getString(R.string.error_codigo_requerido));
                return;
            }

            if (!hasInternetConnection()) {
                Toast.makeText(this, getString(R.string.error_sin_internet), Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(this, EquipoListActivity.class);
            intent.putExtra(EquipoListActivity.EXTRA_CODIGO_PUCP, codigo);
            startActivity(intent);
        });
    }

    private boolean hasInternetConnection() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        }

        Network activeNetwork = connectivityManager.getActiveNetwork();
        if (activeNetwork == null) {
            return false;
        }

        NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(activeNetwork);
        if (capabilities == null) {
            return false;
        }

        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET);
    }
}