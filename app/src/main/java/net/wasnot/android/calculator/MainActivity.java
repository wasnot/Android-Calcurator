
package net.wasnot.android.calculator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.textView)
    public TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick({
            R.id.button0, R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5,
            R.id.button6, R.id.button7, R.id.button8, R.id.button9, R.id.buttonPlusMinus,
            R.id.buttonPoint, R.id.buttonAdd, R.id.buttonSubtract, R.id.buttonMultiply,
            R.id.buttonDivide, R.id.buttonEqual
    })
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button0:
                textView.append("0");
                break;
            case R.id.button1:
                textView.append("1");
                break;
            case R.id.button2:
                textView.append("2");
                break;
            case R.id.button3:
                textView.append("3");
                break;
            case R.id.button4:
                textView.append("4");
                break;
            case R.id.button5:
                textView.append("5");
                break;
            case R.id.button6:
                textView.append("6");
                break;
            case R.id.button7:
                textView.append("7");
                break;
            case R.id.button8:
                textView.append("8");
                break;
            case R.id.button9:
                textView.append("9");
                break;
            case R.id.buttonPoint:
                break;
            case R.id.buttonPlusMinus:
                break;
            case R.id.buttonAdd:
                break;
            case R.id.buttonSubtract:
                break;
            case R.id.buttonMultiply:
                break;
            case R.id.buttonDivide:
                break;
            case R.id.buttonEqual:
                break;
        }
    }
}
