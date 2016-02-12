package com.example.chengen.converter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Stack;

public class Converter64 extends AppCompatActivity implements View.OnClickListener {
    private EditText inputS,inputE,inputM,inputD;
    private TextView output;
    private String outputDString;
    private String outputBString;
    public void setOutputDString(String outputDString){this.outputDString=outputDString;};
    public void setOutputBString(String outputBString){this.outputBString=outputBString;};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_converter64);
        inputS=(EditText)findViewById(R.id.etSign);
        inputE=(EditText)findViewById(R.id.etExpo);
        inputM=(EditText)findViewById(R.id.etMantissa);
        inputD=(EditText)findViewById(R.id.etInDecimal);
        output=(TextView)findViewById(R.id.tvOutput2);
        Button DTB=(Button)findViewById(R.id.btnDTB2);
        Button BTD=(Button)findViewById(R.id.btnBTD2);
        ImageButton ibRef=(ImageButton)findViewById(R.id.imageButton);
        outputDString = "";
        outputBString = "";
        DTB.setOnClickListener(this);
        BTD.setOnClickListener(this);
        ibRef.setOnClickListener(this);
    }
    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btnDTB2:
                output.setText("");
                if(inputD.getText().toString().equals(""))
                    Toast.makeText(getApplicationContext(), "Do not have any decimal numbers to convert", Toast.LENGTH_LONG).show();
                else
                    output.setText(decimalToIEEE(Double.parseDouble(inputD.getText().toString())));

                break;
            case R.id.btnBTD2:
                output.setText("");
                if(inputS.getText().toString().length()<1||inputS.getText().toString().length()>1)
                    Toast.makeText(getApplication(),"Sign bit must be one bit in IEEE754 Double precision 64-bit",Toast.LENGTH_LONG).show();
                else if(inputE.getText().toString().length()<11||inputE.getText().toString().length()>11)
                    Toast.makeText(getApplication(),"Exponent  must be eight bits in IEEE754 Double precision 64-bit",Toast.LENGTH_LONG).show();
                else if(inputM.getText().toString().length()<52||inputM.getText().toString().length()>52)
                    Toast.makeText(getApplication(),"Mantissa must be twenty three bits in IEEE754 Double precision 64-bit",Toast.LENGTH_LONG).show();
                else {
                    String string = inputS.getText().toString() + inputE.getText().toString() + inputM.getText().toString();
                    output.setText(iEEEToDecimal(string));
                }
                break;
            case R.id.imageButton:
                inputD.setText("");
                inputE.setText("");
                inputM.setText("");
                inputS.setText("");
                output.setText("");
                break;
        }
    }
    private String decimalToIEEE(double decimal){
        String initBString = "0 ";
        int shifts = 0;
        int max = 1023;
        if(decimal<0){
            initBString = "1 ";
            decimal = Math.abs(decimal);
        }
        if(decimal>=1){
            while(decimal>=2){
                shifts++;
                decimal = decimal/2;
            }
        }else
        if(decimal<1&&decimal>0){
            while(decimal<1){
                shifts--;
                decimal = decimal*2;
            }
        }
        int decimalZ = max+shifts;
        Stack<Integer> stack = new Stack<>();
        if(decimal==0)
            initBString += "00000000000";
        else{
            while(decimalZ>0){
                int biZ = decimalZ%2;
                stack.push(biZ);
                decimalZ = decimalZ/2;
            }
            int size = stack.size();
            while(size<11){
                initBString+="0";
                size++;
            }
            while(!stack.isEmpty()){
                initBString+=stack.pop();
            }
            initBString+=" ";
        }
        double decimalD = decimal%1;
        if(decimalD==0)
            initBString +=" 0000000000000000000000000000000000000000000000000000";
        else{
            for(int i=0;i<52;i++){
                int biD= (int)(decimalD*2);
                decimalD = (decimalD*2)%1;
                initBString+=biD;
            }
        }
        outputBString = hexi(initBString)+" = "+initBString;
        return outputBString;
    }
    private String hexi(String iEEE){
        int[] num = new int[64];
        int o=0;
        for(int i=0;i<iEEE.length();i++){
            if(iEEE.charAt(i)!=' '){
                num[o]=Integer.parseInt(iEEE.substring(i,i+1));
                o++;
            }
        }
        String hex = "0x";
        int i=0;
        while(i<64){
            int k=3;
            int initNum=0;
            for(int j=i;j<i+4;j++){
                initNum += (int) (num[j]*Math.pow(2,k));
                k--;
            }
            switch (initNum){
                case 10:
                    hex+="A";
                    break;
                case 11:
                    hex+="B";
                    break;
                case 12:
                    hex+="C";
                    break;
                case 13:
                    hex+="D";
                    break;
                case 14:
                    hex+="E";
                    break;
                case 15:
                    hex+="F";
                    break;
                default:
                    hex+=initNum;
                    break;
            }
            i=i+4;
        }
        return hex;
    }
    private  String iEEEToDecimal(String iEEE){
        String expo = iEEE.substring(1,12);
        String mantissa=iEEE.substring(12);
        int sign=1;
        double m =0;
        int e = 0;
        if(Integer.parseInt(iEEE.substring(0,1))==1)
            sign = -1;
        int j=10;
        for(int i=0;i<11;i++){
            e += Integer.parseInt(expo.substring(i, i + 1))*(int)Math.pow(2,j);
            j--;
        }
        e = e-1023;
        int k=-1;
        System.out.println(mantissa);
        for(int i=0; i<mantissa.length();i++){
            m+= Integer.parseInt(mantissa.substring(i,i+1))*Math.pow(2, k);
            k--;
        }
        if(e==-1023)
            m=-1;
        outputDString = sign*(1+m)*Math.pow(2, e)+"";
        return outputDString;
    }
    public String getOutputBString(){return outputBString;}
    public String getOutputDString(){return outputDString;}
}

