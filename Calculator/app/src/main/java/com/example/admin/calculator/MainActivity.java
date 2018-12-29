package com.example.admin.calculator;

import android.net.sip.SipAudioCall;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.*;
import java.math.*;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    final int NAN = 10000000;
    //每个button
    Button btn_AC; Button btn_com; Button btn_sqrt; Button btn_div;
    Button btn_seven; Button btn_eight; Button btn_nine; Button btn_multiple;
    Button btn_four; Button btn_five; Button btn_six; Button btn_subtract;
    Button btn_one; Button btn_two; Button btn_three; Button btn_add;
    Button btn_zero; Button btn_decimal; Button btn_equal;
    EditText editText;
    boolean clearflag = false;
    String after = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //实例化
        btn_AC = (Button)findViewById(R.id.AC);     btn_AC.setOnClickListener(this);
        btn_com =(Button)findViewById(R.id.com);    btn_com.setOnClickListener(this);
        btn_sqrt = (Button)findViewById(R.id.sqrt); btn_sqrt.setOnClickListener(this);
        btn_div =(Button)findViewById(R.id.div);    btn_div.setOnClickListener(this);
        btn_seven = (Button)findViewById(R.id.seven);   btn_seven.setOnClickListener(this);
        btn_eight = (Button)findViewById(R.id.eight);   btn_eight.setOnClickListener(this);
        btn_nine = (Button)findViewById(R.id.nine);     btn_nine.setOnClickListener(this);
        btn_multiple = (Button)findViewById(R.id.multiple); btn_multiple.setOnClickListener(this);
        btn_four = (Button)findViewById(R.id.four);     btn_four.setOnClickListener(this);
        btn_five = (Button)findViewById(R.id.five);     btn_five.setOnClickListener(this);
        btn_six = (Button)findViewById(R.id.six);       btn_six.setOnClickListener(this);
        btn_subtract = (Button)findViewById(R.id.subtract); btn_subtract.setOnClickListener(this);
        btn_one = (Button)findViewById(R.id.one);       btn_one.setOnClickListener(this);
        btn_two =(Button)findViewById(R.id.two);        btn_two.setOnClickListener(this);
        btn_three = (Button)findViewById(R.id.three);   btn_three.setOnClickListener(this);
        btn_add = (Button)findViewById(R.id.add);       btn_add.setOnClickListener(this);
        btn_zero = (Button)findViewById(R.id.zero);     btn_zero.setOnClickListener(this);
        btn_decimal = (Button)findViewById(R.id.decimal);   btn_decimal.setOnClickListener(this);
        btn_equal = (Button)findViewById(R.id.equal);    btn_equal.setOnClickListener(this);
        editText = (EditText)findViewById(R.id.edittext);


    }
    public void showMyToast(final Toast toast, final int cnt) {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                toast.show();
            }
        }, 0, 3000);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                toast.cancel();
                timer.cancel();
            }
        }, cnt );
    }

    public void onClick(View v){
        String str = editText.getText().toString();
        switch (v.getId()){
            case R.id.AC:
                if(clearflag){
                    clearflag = false;
                    str = "";
                    editText.setText("");
                }
                editText.setText("");               //清空text和堆栈  str
                break;
            case R.id.com:
            case R.id.sqrt:
            case R.id.div:
            case R.id.seven:
            case R.id.eight:
            case R.id.nine:
            case R.id.multiple:
            case R.id.four:
            case R.id.five:
            case R.id.six:
            case R.id.subtract:
            case R.id.one:
            case R.id.two:
            case R.id.three:
            case R.id.add:
            case R.id.zero:
            case R.id.decimal:
                System.out.println(((Button)v).getText());
                editText.setText(str+((Button)v).getText().toString());
                break;
            case R.id.equal:
                if (!str.equals("")) {
                    editText.setText(str + ((Button) v).getText().toString());
                    System.out.println("str = " + str);
                    System.out.println("len = " + str.length());
                    System.out.println("第一个是 " + str.charAt(0));
                    if (str.charAt(0) == '~') {          //如果一开始好多 ~
                        int p = 0;
                        int num = 0;
                        for (int i = 0; i < str.length(); i++) {
                            if (str.charAt(i) == '~') {
                                num++;
                            } else {
                                p = i;
                                break;
                            }
                        }
                        int j = p;
                        String strr = "";
                        if (num % 2 != 0) {     //奇数数个 ~
                            strr += "~";
                        }
                        for (int i = p; i < str.length(); i++) {
                            strr += str.charAt(i);
                        }
                        str = strr;
                    }
                    boolean legal = input_judge(str);
                    System.out.println("legal = "+legal);
                    boolean exist = false;
                    for (int i = 0; i < str.length() - 1; i++) {
                        if (!isNumber(str.charAt(i)) && str.charAt(i) != '.') {
                            exist = true;               //判断是不是都是数字
                        }
                    }
                    if (!exist) {
                        editText.setText(str);
                        str = "";
                    } else {
                        if (!legal) {
                            System.out.println("legal=false，输入不合法");
                            Toast toast = Toast.makeText(this, "输入不合法，请重新输入.", Toast.LENGTH_LONG);
                            showMyToast(toast, 3000);
                            editText.setText("");
                            str = "";
                        } else {
                            String a = getAfter(str);
                            System.out.println("后缀表达式是" + a);
                            double correct = getResult(a);
                            if (correct == -NAN) {
                                Toast toast = Toast.makeText(this, "输入不合法，请重新输入.", Toast.LENGTH_LONG);
                                showMyToast(toast, 3000);
                                editText.setText("");
                                str = "";
                            } else {
                                String ans = Double.toString(getResult(a));
                                editText.setText(ans);
                                System.out.println("ans = "+ans);
                                str = "";
                            }
                        }
                        break;
                    }
                }
        }
    }
    public boolean isNumber(char c){            //判断是不是数字
        if(c-'0' >= 0 && c-'0'<=9){
            return true;
        }
        return false;
    }
    public int getPriority(char c){                 //取得优先级
        int ans = 0;
        switch(c){
            case '+':
            case '-':
                ans = 1 ;
                break;
            case '*':
            case '/':
                ans = 2;
                break;
            case '~':
            case '√':
                ans = 3;
                break;
            default:
                break;
        }
        return ans;
    }
    public String getAfter(String str){          //取得后缀表达式      数字和小数点直接加到back里， 字符出入栈操作。
        String back = "";                       //存后缀表达式的数组
        Stack<Character> ret = new Stack<Character>();  //存字符
        ret.clear();
        for (int i = 0; i < str.length(); i++)
        {
            char ch = str.charAt(i);
            if (isNumber(ch) || ch == '.')// 小数点或数字直接输出  数字之间，数字和小数点不需要空格
            {
                back += ch;
            }
            else
            // 比较操作符的进栈优先级
            {
                if (i != 0 && isNumber(str.charAt(i-1))){   //如果字符的前一个是数字 就加空格
                    back += ' ';
                }
                if (ret.isEmpty())          //栈空直接入栈
                {
                    ret.push(ch);
                    continue;
                }
                char temp = ret.peek();
                while (getPriority(ch) <= getPriority(temp))// 进栈优先级小于栈内优先级，则一直出栈
                {
                    back += temp;
                    back += " ";        //字符之间也要空格
                    ret.pop();
                    if (ret.isEmpty())
                        break;
                    temp = ret.peek();
                }
                ret.push(ch);
            }
        }
        // 将栈中剩余的元素弹出来
        while (!ret.isEmpty())
        {
            back += ' ';
            char temp = ret.peek();
            ret.pop();
            back += temp;
        }
        return back;
    }
    public boolean input_judge(String str){         //输入校验
        boolean exist = true;
        int len = str.length();
        if (str.charAt(0) == '*' || str.charAt(0) == '/' || str.charAt(0) == '+' || str.charAt(0) == '-'){         //第一个是 * /
            exist = false;
            System.out.println("1 "+exist);
        }
        if (!isNumber(str.charAt(len-1))){
            exist = false;
            System.out.println("2 "+exist);
        }
        for (int j = 0;j < len-1;j++){
            if (str.charAt(j) == '/' && str.charAt(j+1) == '0'){        //除0
                exist = false;
                System.out.println("3 "+exist);
            }
            if (str.charAt(j) == '√' && str.charAt(j+1) == '-'){        //负数开方
                exist = false;
                System.out.println("4 "+exist);
            }
            if (!isNumber(str.charAt(j)) && str.charAt(j) != '~' && str.charAt(j) == str.charAt(j+1)){  //两个+ - * /
                exist = false;
                System.out.println("5 "+exist);
            }
            if((str.charAt(j) == '+' || str.charAt(j) == '-') && (str.charAt(j+1) == '*' || str.charAt(j+1) == '/') ){
                exist = false;
                System.out.println("6 "+exist);
            }
            if((str.charAt(j) == '*') && (str.charAt(j+1) == '+' || str.charAt(j+1) == '/' || str.charAt(j+1) == '-') ){
                exist = false;
                System.out.println("7 "+exist);
            }
            if((str.charAt(j) == '/' )&& (str.charAt(j+1) == '+' || str.charAt(j+1) == '*' || str.charAt(j+1) == '-') ){
                exist = false;
                System.out.println("8 "+exist);
            }
            if((str.charAt(j) == '~' )&& (str.charAt(j+1) == '/' || str.charAt(j+1) == '*') ){
                exist = false;
                System.out.println("9 "+exist);
            }
            if((str.charAt(j) == '√') && (str.charAt(j+1) == '/' || str.charAt(j+1) == '*' || str.charAt(j+1) == '~') ){
                exist = false;
                System.out.println("10 "+exist);
            }
            if(str.charAt(j) == '+' && str.charAt(j+1) == '-'){
                exist = false;
            }
            if(str.charAt(j) == '-' && str.charAt(j+1) == '+'){
                exist = false;
            }
            if (str.charAt(0) != '~' && str.charAt(j) == '~' && str.charAt(j+1) == '~') {
                exist = false;
            }
        }
        return exist;
    }
    public double getResult(String back){       //计算结果
        Stack<Double> stack = new Stack<Double>();      //中间结果栈
        int i = 0;
        System.out.println(back.length());
        while(i < back.length()) {
            String sum = "";
            char cc = back.charAt(i);
            if(isNumber(cc) || cc=='.') {                   //连续的数字和小数点 算作一个数
                for (int j = i; j < back.length(); j++) {
                    char c = back.charAt(j);
                    if (isNumber(c) || c == '.') {
                        sum += c;
                        i++;
                    } else {
                        System.out.println("sum = " + sum);
                        stack.push(Double.parseDouble(sum));
                        System.out.println("转化成数字是"+Double.parseDouble(sum));
                        sum = "";
                        break;
                    }
                }
            }
            else if (cc == ' '){        //空格跳过
                i++;
                continue;
            }
            else {
                double a,b;
                switch (cc){
                    case '+':
                        a = stack.pop();
                        b = stack.pop();
                        stack.push(a+b);
                        break;
                    case '-':                   //因为栈是先进后出，所以 - 和 / 都得 b在先
                        a = stack.pop();
                        b = stack.pop();
                        double c = b-a;
                        stack.push(c);
                        break;
                    case '*':
                        a = stack.pop();
                        b = stack.pop();
                        stack.push(a*b);
                        break;
                    case '/':
                        a = stack.pop();
                        b = stack.pop();
                        stack.push(b/a);

                        break;
                    case '√':
                        a = stack.pop();
                        if(a < 0){
                            return -NAN;
                        }
                        stack.push(Math.sqrt(a));
                        break;
                    case '~':
                        a = stack.pop();
                        stack.push(-a);
                        break;
                    default:
                        break;
                }
                i++;
            }
        }
        return stack.pop();
    }

}
