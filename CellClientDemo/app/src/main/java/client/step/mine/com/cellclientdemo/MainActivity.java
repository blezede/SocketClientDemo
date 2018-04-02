package client.step.mine.com.cellclientdemo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private EditText editText_ip,editText_data;
    private OutputStream outputStream = null;
    private Socket socket = null;
    private String ip;
    private String data;
    private boolean socketStatus = false;
    TextView mStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText_ip = findViewById(R.id.ip);
        editText_data = findViewById(R.id.data);

        mStatus = findViewById(R.id.status);

    }


    public void connect(View view){

        if (socket != null) {
            try {
                socket.close();
                socketStatus = false;
                mStatus.setText("");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ip = editText_ip.getText().toString();
        if(ip == null){
            Toast.makeText(MainActivity.this,"please input Server IP",Toast.LENGTH_SHORT).show();
        }

        Thread thread = new Thread(){
            @Override
            public void run() {
                super.run();

                if (!socketStatus) {

                    try {
                        socket = new Socket(ip,8000);
                        if(socket == null){
                        }else {
                            socketStatus = true;
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    mStatus.setText("Connected Success");
                                    Toast.makeText(MainActivity.this,"Connected Success",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        outputStream = socket.getOutputStream();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }
        };
        thread.start();

    }


    public void send(View view){

        data = editText_data.getText().toString();
        if(TextUtils.isEmpty(data)){
            Toast.makeText(MainActivity.this,"please input Sending Data",Toast.LENGTH_SHORT).show();
            return;
        }

        Thread thread = new Thread(){
            @Override
            public void run() {
                super.run();
                if(socketStatus){
                    try {
                        outputStream.write(data.getBytes("Utf-8"));
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                editText_data.setText("");
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }
        };
        thread.start();

    }

    /*当客户端界面返回时，关闭相应的socket资源*/
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        /*关闭相应的资源*/
        try {
            outputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}