package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myapplication.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    double total = 0;
    String controller = "";
    ArrayList<String> elements = new ArrayList<>();

    // verifica se o controller está preenchido e adiciona seu valor e o operador ao elements
    public void checkControllerAndInsertValues(String operator, boolean operatorAllowedWithoutPreviousValue){

        if (operatorAllowedWithoutPreviousValue){
            if (!controller.equals("")){
                elements.add(controller);
            }
            elements.add(operator);
            binding.textView.setText(binding.textView.getText() + operator);
        } else{
            if (!controller.equals("")){
                elements.add(controller);
                elements.add(operator);
                binding.textView.setText(binding.textView.getText() + operator);
            }
        }

        controller = "";

    }

    // identifica a primeira ocorrência de um operador de multiplicação e os valores envolvidos,
    // depois realiza a operação, exclui os valores de base do elements e insere o resultado
    private void checkForTimes(){
        int index = elements.indexOf("*");
        double value1 = Double.parseDouble(elements.get(index - 1));
        double value2 = Double.parseDouble(elements.get(index + 1));
        elements.remove(index - 1);
        elements.remove(index - 1);
        elements.remove(index - 1);
        elements.add(index - 1, String.valueOf(value1 * value2));
    }

    // identifica a primeira ocorrência de um operador de divisão e os valores envolvidos,
    // depois realiza a operação, exclui os valores de base do elements e insere o resultado
    private void checkForDivision(){
        int index = elements.indexOf("/");
        double value1 = Double.parseDouble(elements.get(index - 1));
        double value2 = Double.parseDouble(elements.get(index + 1));
        elements.remove(index - 1);
        elements.remove(index - 1);
        elements.remove(index - 1);
        elements.add(index - 1, String.valueOf(value1 / value2));
    }

    // percorre do começo ao fim o elements, realizando na ordem correta operações de
    // multiplicação e divisão
    public void checkForTimesAndDivisionInOrder(){

        while (elements.contains("*") || elements.contains("/")){
            if (elements.contains("*")){
                if (elements.contains("/")){
                    if (elements.indexOf("*") < elements.indexOf("/")){
                        checkForTimes();
                    } else {
                        checkForDivision();
                    }
                } else {
                    checkForTimes();
                }
            } else if (elements.contains("/")){
                checkForDivision();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Button[] buttons = {binding.plus, binding.minus, binding.times, binding.divide, binding.btn0, binding.btn1, binding.btn2, binding.btn3, binding.btn4, binding.btn5, binding.btn6, binding.btn7, binding.btn8, binding.btn9, binding.addDot};

        // inicializa todos os botões
        for (int i = buttons.length - 1; i >= 0; i--){
            int i2 = i;
            buttons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    controller += buttons[i2].getText().toString();
                    binding.textView.setText(binding.textView.getText() + buttons[i2].getText().toString());
                }
            });
        }

        binding.ac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                elements.clear();
                total = 0;
                controller = "";
                binding.textView.setText("");
            }
        });

        binding.erase.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (binding.textView.getText().toString().length() != 0) {

                    String text = binding.textView.getText().toString();
                    if (controller.length() > 0){
                        binding.textView.setText(text.substring(0, text.length() - 1));
                        controller = controller.substring(0, controller.length() - 1);
                    } else {
                        System.out.println(elements.size());
                        binding.textView.setText(text.substring(0, text.length() - 1));
                        controller = elements.get(elements.size() - 2);
                        elements.remove(elements.size() - 1);
                        elements.remove(elements.size() - 1);
                    }
                }
            }
        });

        binding.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkControllerAndInsertValues("+", false);
            }
        });

        binding.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkControllerAndInsertValues("-", true);
            }
        });

        binding.times.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkControllerAndInsertValues("*", false);
            }
        });

        binding.divide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkControllerAndInsertValues("/", false);
            }
        });

        binding.equals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                elements.add(controller);
                controller = "";

                checkForTimesAndDivisionInOrder();

                int position = 0;

                if (elements.get(0).equals("-")){
                    total = Double.parseDouble(elements.get(1)) * (-1);
                    position = 2;
                } else {
                    total += Double.parseDouble(elements.get(0));
                    position = 1;
                }

                String[] elementsArray = elements.toArray(new String[0]);

                for (int i = position; i < elementsArray.length; i++){
                    if (elementsArray[i].equals("+")){
                        double number = Double.parseDouble(elementsArray[i + 1]);
                        total += number;
                    } else if (elementsArray[i].equals("-")){
                        double number = Double.parseDouble(elementsArray[i + 1]);
                        total -= number;
                    }
                }

                elements.clear();
                controller = String.valueOf(total);
                binding.textView.setText(String.valueOf(total));
                total = 0;
            }
        });
    }
}