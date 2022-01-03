package com.xekep.meloncalc;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.EditText;
import android.view.View;
import android.os.Bundle;
import java.lang.Math;
import android.text.TextWatcher;
import android.text.Editable;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText editLen1 = ((EditText)findViewById(R.id.editLen1));
        EditText editLen2 = ((EditText)findViewById(R.id.editLen2));
        EditText editMass = ((EditText)findViewById(R.id.editMass));
        CheckBox checkBoxSpheroid = ((CheckBox)findViewById(R.id.checkBoxSpheroid));

        editLen2.setVisibility(View.INVISIBLE);
        editLen2.setHint("Экватор, см");
        editLen1.setHint("Окружность, см");
        editMass.setHint("Масса арбуза, кг");
        checkBoxSpheroid.setChecked(false);



        editLen1.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                Calc();
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
        });

        editLen2.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                Calc();
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
        });

        editMass.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                Calc();
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
        });

    }

    private static final double maxDensity  = 0.9; // Плотнсть спелого арбуза
    private static final double minDensity  = 0.7; // Плотность перезрелого арбуза
    /*
    Формы
    Сфера - Большая окружность
    Сфероид - меридиан (больше) и экватор
    */
    public void OnClickSpheroid(View view)
    {
        if(((CheckBox)view).isChecked())
        {
            ((EditText)findViewById(R.id.editLen1)).setHint("Меридиан, см");
            ((EditText)findViewById(R.id.editLen2)).setVisibility(View.VISIBLE);


        }
        else
        {
            ((EditText)findViewById(R.id.editLen1)).setHint("Окружность, см");
            ((EditText)findViewById(R.id.editLen2)).setVisibility(View.INVISIBLE);
        }

        ((EditText)findViewById(R.id.editLen1)).setText("");
        ((EditText)findViewById(R.id.editLen2)).setText("");
        ((EditText)findViewById(R.id.editMass)).setText("");
        ((TextView)findViewById(R.id.result)).setText("");
    }

    public void Calc()
    {
        TextView textView = (TextView) findViewById(R.id.result);
        String str_len1 = ((EditText) findViewById(R.id.editLen1)).getText().toString();
        String str_len2 = ((EditText) findViewById(R.id.editLen2)).getText().toString();
        String str_mass = ((EditText) findViewById(R.id.editMass)).getText().toString();
        try{
            boolean spheroid = ((CheckBox)findViewById(R.id.checkBoxSpheroid)).isChecked();
            double density;
            double mass = Double.parseDouble(str_mass) * 1000;

            if(!spheroid) {
                // Сфера
                double great_circle = Double.parseDouble(str_len1); // Большая окружность

                if (great_circle >= mass)
                    return;

                double r = great_circle / (2 * Math.PI); // Радиус
                double v = 4.0 / 3 * Math.PI * Math.pow(r, 3); // Объем
                density = mass / v;
            }
            else
            {
                // Вытянутый сфероид
                double equator = Double.parseDouble(str_len2);
                double meridian = Double.parseDouble(str_len1);
                if(equator > meridian) {
                    double t = equator;
                    equator = meridian;
                    meridian = t;
                }
                double b = equator / (2 * Math.PI); // Малая полуось

                double a = (3 * meridian - 4 * Math.PI * b + Math.sqrt(3 * Math.pow(meridian, 2) + 12 * Math.PI * meridian * b - 20 * Math.pow(Math.PI, 2 ) * Math.pow(b, 2))) / (6 * Math.PI);
                // Большая полуось

                double v = 4 * Math.PI / 3 * Math.pow(b, 2) * a; // Объем
                density = mass / v;
            }
            if(density > 1.2)
            {
                textView.setText("Введите корректные значения");
                textView.setTextColor(Color.parseColor("#c0392b"));
            }
            else if(density < minDensity)
            {
                textView.setText(String.format("Арбуз недозрел (%.2f)", density));
                textView.setTextColor(Color.parseColor("#c0392b"));
            }
            else if(density > maxDensity)
            {
                textView.setText(String.format("Арбуз перезрел (%.2f)", density));
                textView.setTextColor(Color.parseColor("#2c3e50"));
            }
            else
            {
                textView.setText(String.format("Сладкий арбуз! (%.2f)", density));
                textView.setTextColor(Color.parseColor("#27ae60"));
            }
        }
        catch(NumberFormatException ex)
        {
            textView.setText("");
        }
    }

    public void OnClickCalc(View view)
    {
        Calc();
    }
}