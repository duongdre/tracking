package com.example.dss.project.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Instrumentation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.dss.R;
import com.example.dss.project.Adapter.MessAdapter;
import com.example.dss.project.Models.Mess;
import com.example.dss.project.Models.Status;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {

    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;
    private BottomSheetBehavior bottomSheetBehavior;

    // loadingDialog = new LoadingDialog(MessageActivity.this);

    CircleImageView profile_image;
    TextView username, statusTime, statusName, test;
    ImageButton btn_send;
    ImageView btn_camera, btn_voice, capture_picture;
    //ImageView btn_update_status;

    EditText text_send;
    String url, check = "", myUrl;
    Uri fileUri, imageUri;

    MessAdapter messAdapter;
    CustomAdapter customAdapter;
    List<Mess> mmess;
    List<Status> listItemStatus = new ArrayList<Status>();
    ListView listViewStatus;
    //List<User> muser;

    RecyclerView recyclerView;

    FirebaseUser firebaseUser;
    DatabaseReference reference;
    StorageReference storageRef;

    StorageTask uploadTask;

    Intent intent;

    LoadingDialog loadingDialog = new LoadingDialog(MessageActivity.this);

    String listStatusNames[] = {"Nhận đơn", "Bắt đầu", "Bốc hàng", "Bốc hàng xong", "Giao hàng", "Tới điểm trả hàng", "Bốc hàng", "Bốc hàng xong", "Trở về", "Tới bãi tập kết", "Kết thúc chuyến đi"};
    String listStatusTimes[] = {"10:40:03 22/08/2020", "10:40:59 22/08/2020", "10:45:34 22/08/2020", "16:40:46 22/08/2020", "16:45:41 22/08/2020", "17:12:59 22/08/2020", "18:15:33 22/08/2020", "19:54:21 22/08/2020", "20:05:03 22/08/2020", "21:17:08 22/08/2020", "21:20:41 22/08/2020"};
    int listStatusProgesses[] = {1, 1, 1, 1, 1, 2, 3, 3, 3, 3, 3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        View bottomSheet = findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        bottomSheetBehavior.setHideable(false);
        bottomSheetBehavior.setPeekHeight(125);

        /*getSupportActionBar().setTitle("Trở về");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_back);*/
        //Action Bar and Set to center
        /*getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);*/
        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        final View cView = getLayoutInflater().inflate(R.layout.align_center_action_bar, null);
        actionBar.setCustomView(cView);

        /*firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null){
            Intent intent = new Intent(MessageActivity.this, MapsActivity.class);
            startActivity(intent);
            finish();
        }*/

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        profile_image = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);
        text_send = findViewById(R.id.text_send);
        btn_send = findViewById(R.id.btn_send);
        btn_camera = findViewById(R.id.btn_camera);
        btn_voice = findViewById(R.id.btn_voice);
        capture_picture = findViewById(R.id.capture_picture);
        //btn_update_status = findViewById(R.id.btn_status_update);

        capture_picture.setVisibility(View.GONE);

        test = findViewById(R.id.text_send);

        intent = getIntent();
        final String userid = "Device";//intent.getStringExtra("userid");

        //Auto ready message every x second
        /*Runnable runnable = new Runnable() {
            @Override
            public void run() {
                readMess("Server", "Device", "default");
                handler.postDelayed(this, 1000L);
            }
        };
        handler.post(runnable);*/


        //String Status for Testing
        for (int i = 0; i < listStatusNames.length; i++){
            Status status = new Status(listStatusNames[i], listStatusTimes[i], listStatusProgesses[i]);
            listItemStatus.add(status);
        }
        listViewStatus = findViewById(R.id.list_view_status);
        customAdapter = new CustomAdapter(listItemStatus);
        listViewStatus.setAdapter(customAdapter);

        listViewStatus.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Toast.makeText(MessageActivity.this, "Chi tiết trạng thái", Toast.LENGTH_SHORT).show();
                showUpdateStatus(position);
            }
        });


        //Button FUNCTION
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (capture_picture.getVisibility() != View.GONE){
                    if (uploadTask != null && uploadTask.isInProgress()){
                        Toast.makeText(MessageActivity.this, "Đang tải lên một tác vụ khác", Toast.LENGTH_SHORT).show();
                    }else{
                        final LoadingDialog loadingDialog = new LoadingDialog(MessageActivity.this);
                        Toast.makeText(MessageActivity.this, "Đang tải ảnh lên", Toast.LENGTH_SHORT).show();
                        loadingDialog.startLoadingDialog();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                fileUploader();
                                loadingDialog.dissmissDialog();
                            }
                        }, 10000);
                        capture_picture.setVisibility(View.GONE);
                        //sendMess("Device", "Server", "Image", fileUri.getLastPathSegment());
                    }
                }else{
                    String msg = text_send.getText().toString();
                    if (!msg.equals("")) {
                        sendMess("Device", "Server", "Text", msg);
                    }else{
                        Toast.makeText(MessageActivity.this, "Không thể gửi tin nhắn rỗng", Toast.LENGTH_SHORT).show();
                    }
                    text_send.setText("");
                    //readMess("Server", "Device", "default");
                }
            }
        });

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(userid).child("Server");
        storageRef = FirebaseStorage.getInstance().getReference("Images");




        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //User user = dataSnapshot.getValue(User.class);
                //username.setText("Server");
                //if (user.getURL().equals("default")){
                    //profile_image.setImageResource(R.mipmap.ww);
                //}else{
                    //Glide.with(MessageActivity.this).load(user.getURL()).into(profile_image);
                    //profile_image.setImageResource(R.drawable.dsslogo);
                //}

                readMess("Server", "Device", "default");
                url = "URL";
                System.out.println("url: " + url);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        //Chose Image
        /*btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent.createChooser(intent, "Selelect Image"), 1);
            }
        });*/

        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                        String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        //popup request
                        requestPermissions(permission, PERMISSION_CODE);
                    }else{
                        openCamera();
                    }
                }else{
                    openCamera();
                }
            }
        });

        btn_voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent.createChooser(intent, "Selelect Image"), 1);
            }
        });
    }

    private void openCamera(){
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera of DSS");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        //camera intent
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSION_CODE:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    openCamera();
                }else{
                    Toast.makeText(this, "Từ chối truy cập Máy ảnh", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private String getExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    /*public Bitmap resizeBitmap(Bitmap getBitmap, int maxSize) {
        int width = getBitmap.getWidth();
        int height = getBitmap.getHeight();
        double x;

        if (width >= height && width > maxSize) {
            x = width / height;
            width = maxSize;
            height = (int) (maxSize / x);
        } else if (height >= width && height > maxSize) {
            x = height / width;
            height = maxSize;
            width = (int) (maxSize / x);
        }
        return Bitmap.createScaledBitmap(getBitmap, width, height, false);
    }*/

    private void fileUploader(){
        final StorageReference ref = storageRef.child(System.currentTimeMillis() + "." + getExtension(fileUri));

        uploadTask = ref.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                sendMess("Device", "Server", "Image", String.valueOf(uri));
                            }
                        });
                        Toast.makeText(MessageActivity.this, "Tải lên thành công", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Chose Image
        /*if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null){
            fileUri = data.getData();
            capture_picture.setImageURI(fileUri.);
            capture_picture.setVisibility(View.VISIBLE);
        }*/
        //Capture
        if (resultCode == RESULT_OK){
            fileUri = imageUri;
            capture_picture.setImageURI(fileUri);
            capture_picture.setVisibility(View.VISIBLE);
            //System.out.println("URI: " + String.valueOf(imageUri));
        }
    }

    //Get URI of a Bitmap Object for set instead of URI of image
    /*public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }*/

    private void showUpdateStatus(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MessageActivity.this);
        StringBuilder sb = new StringBuilder();

        switch(listItemStatus.get(position).getProgress()) {
            case 1:
                builder.setTitle("Đã hoàn thành");
                sb.append("Trạng thái: " + listItemStatus.get(position).getName());
                sb.append("\n");
                sb.append("Thời gian hoàn thành: " + listItemStatus.get(position).getTime());
                sb.append("\n");
                sb.append("Tình trạng hàng hóa: ");

                builder.setIcon(R.drawable.iconfinished);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.cancel();
                    }
                });
                break;
            case 2:
                final Date currenttime;
                final String pattern = "HH:mm:ss dd/MM/yyy";
                final DateFormat df = new SimpleDateFormat(pattern);
                currenttime = Calendar.getInstance().getTime();
                //System.out.println("currenttime: " + df.format(currenttime));

                builder.setTitle("Thông tin chi tiết trạng thái");
                sb.append("Trạng thái: " + listItemStatus.get(position).getName());
                sb.append("\n");
                sb.append("Thời gian: " + df.format(currenttime));
                sb.append("\n");
                sb.append("Tình trạng hàng hóa: ");

                builder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if (position < listItemStatus.size() - 1){
                            listItemStatus.get(position).setProgress(1);
                            listItemStatus.get(position + 1).setProgress(2);
                        }else{
                            listItemStatus.get(position).setProgress(1);
                        }
                        listItemStatus.get(position).setTime(df.format(currenttime));
                        //refresh
                        listViewStatus.invalidateViews();
                        dialog.cancel();
                    }
                });

                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.cancel();
                    }
                });
                break;
            default:
                builder.setTitle("Chưa hoàn thành");
                sb.append("Trạng thái: " + listItemStatus.get(position).getName());
                sb.append("\n");
                sb.append("Thời gian dự kiến: " + listItemStatus.get(position).getTime());
                sb.append("\n");
                sb.append("Tình trạng hàng hóa: ");

                builder.setIcon(R.drawable.iconbanned);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.cancel();
                    }
                });
        }

        builder.setMessage(sb.toString());
        builder.setCancelable(false);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void sendMess(String fromWho, String toWho, String type, String value){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("fromWho", fromWho);
        hashMap.put("toWho", toWho);
        hashMap.put("type", type);
        hashMap.put("value", value);

        reference.child("Chats").push().setValue(hashMap);
    }

    private void readMess(final String myid, final String userid, final String url){
        mmess = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mmess.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Mess mess = snapshot.getValue(Mess.class);
                    if (mess.getToWho().equals(myid) && mess.getFromWho().equals(userid) || mess.getToWho().equals(userid) && mess.getFromWho().equals(myid)){
                        mmess.add(mess);
                    }

                    messAdapter = new MessAdapter(MessageActivity.this, mmess, url);
                    recyclerView.setAdapter(messAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public class CustomAdapter extends BaseAdapter implements Filterable {

        private List<Status> itemsModelList;
        private List<Status> itemsModelListFiltered;

        public CustomAdapter(List<Status> itemsModelList) {
            this.itemsModelList = itemsModelList;
            this.itemsModelListFiltered = itemsModelList;
        }

        @Override
        public int getCount() {
            return itemsModelListFiltered.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        //Get View for Adapter
        @Override
        public View getView(int i, View convertView, ViewGroup parent) {
            View view = getLayoutInflater().inflate(R.layout.item_status, null);
            statusTime = view.findViewById(R.id.status_time);
            statusName = view.findViewById(R.id.status_name);

            //set DataItem ListView
            //set for Items which are done
            //set for Items which are still in working or not yet
            switch(itemsModelListFiltered.get(i).getProgress()) {
                case 1:
                    statusTime.setText("Hoàn thành: " + itemsModelListFiltered.get(i).getTime());
                    statusName.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.background_status_first));
                    break;
                case 2:
                    statusName.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.background_status_second));
                    break;
                default:
                    statusTime.setText("Dự kiến: " + itemsModelListFiltered.get(i).getTime());
                    statusName.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.background_status_first));
            }
            statusName.setText(itemsModelListFiltered.get(i).getName());
            return view;
        }


        //Set on Text Change
        @Override
        public Filter getFilter() {
            final Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();

                    if (constraint == null || constraint.length() == 0) {
                        filterResults.count = itemsModelList.size();
                        filterResults.values = itemsModelList;
                    } else {
                        String searchString = constraint.toString().toUpperCase();

                        List<Status> resultData = new ArrayList<>();

                        //Car setChange
                        for (Status status : itemsModelList) {
                            if (status.getId().contains(searchString)) {
                                resultData.add(status);
                            }

                            filterResults.count = resultData.size();
                            filterResults.values = resultData;
                        }
                    }

                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    itemsModelListFiltered = (List<Status>) filterResults.values;
                    notifyDataSetChanged();
                }
            };
            return filter;
        }
    }
}
