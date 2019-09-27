package fusionsoftware.loop.dawaionline.fragments;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import fusionsoftware.loop.dawaionline.R;
import fusionsoftware.loop.dawaionline.activity.DashboardActivity;
import fusionsoftware.loop.dawaionline.adapter.DoctorPrescriptionAdapter;
import fusionsoftware.loop.dawaionline.balltrianglecustomprogress.BallTriangleDialog;
import fusionsoftware.loop.dawaionline.framework.IAsyncWorkCompletedCallback;
import fusionsoftware.loop.dawaionline.framework.ServiceCaller;
import fusionsoftware.loop.dawaionline.model.ContentDataAsArray;
import fusionsoftware.loop.dawaionline.model.Result;
import fusionsoftware.loop.dawaionline.utilities.Contants;
import fusionsoftware.loop.dawaionline.utilities.FontManager;
import fusionsoftware.loop.dawaionline.utilities.Utility;

import static android.app.Activity.RESULT_OK;

public class AddDoctorPrescriptionFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public AddDoctorPrescriptionFragment() {
        // Required empty public constructor
    }

    public static AddDoctorPrescriptionFragment newInstance(String param1, String param2) {
        AddDoctorPrescriptionFragment fragment = new AddDoctorPrescriptionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private Context context;
    View view;
    Typeface materialDesignIcons, regular;
    Button btn_upload, btn_saved;
    ImageView image_view, img;
    Bitmap bitmap;
    boolean check = true;
    ProgressDialog progressDialog;
    String userChoosenTask, stringImage, sphone, sDocName;
    String ImagePath = "CategoryPictures";
    boolean isImageFitToScreen;
    LinearLayout layout;
    TextView tv_title, txt_addcam;
    EditText edit_docname;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    private int REQUEST_CAMERA = 1;
    private int SELECT_FILE = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity();
        view = inflater.inflate(R.layout.fragment_add_doctor_prescription, container, false);
        init();
        return view;
    }

    private void init() {
        DashboardActivity rootActivity = (DashboardActivity) getActivity();
        rootActivity.setScreencart(true);
        rootActivity.setScreenSave(false);
        rootActivity.setItemCart();
        regular = FontManager.getFontTypeface(context, "fonts/roboto.regular.ttf");
        materialDesignIcons = FontManager.getFontTypefaceMaterialDesignIcons(context, "fonts/materialdesignicons-webfont.otf");
        btn_upload = view.findViewById(R.id.btn_upload);
        btn_saved = view.findViewById(R.id.btn_saved);
        image_view = view.findViewById(R.id.image_view);
        img = view.findViewById(R.id.img);
        layout = view.findViewById(R.id.layout);
        edit_docname = view.findViewById(R.id.edit_docname);
        tv_title = view.findViewById(R.id.tv_title);
        txt_addcam = view.findViewById(R.id.txt_addcam);
        SharedPreferences sharedPreferences = context.getSharedPreferences("Login", Context.MODE_PRIVATE);
        sphone = sharedPreferences.getString("key", null);
        btn_saved.setOnClickListener(v -> {
            DoctorPrescriptionFragment doctorPrescriptionFragment = DoctorPrescriptionFragment.newInstance("", "");
            moveFragment(doctorPrescriptionFragment);
        });
        btn_upload.setOnClickListener(v -> {
//            ImageUploadToServerFunction();
            if (Validation()) {
                new UploadFileToServer().execute();
            }
        });
        txt_addcam.setOnClickListener(v -> {
//            showImageChooser();
            selectImage();
        });
    }


    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = checkPermission(context);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Gallery")) {
                    userChoosenTask = "Choose from Gallery";
                    if (result)
                        galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public static boolean checkPermission(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();

                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }


    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Gallery"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }

        image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isImageFitToScreen) {
                    isImageFitToScreen = false;
//                            image_view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    image_view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    image_view.setAdjustViewBounds(true);
                } else {
                    isImageFitToScreen = true;
                    image_view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
                    image_view.setScaleType(ImageView.ScaleType.FIT_XY);
                    btn_upload.setVisibility(View.GONE);
                    btn_saved.setVisibility(View.GONE);
                    img.setVisibility(View.GONE);
                    tv_title.setVisibility(View.GONE);
                    txt_addcam.setVisibility(View.GONE);
                    layout.setBackgroundColor(Color.parseColor("#ffffff"));

                }
            }

        });

    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(context.getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        image_view.setImageBitmap(bm);
        Uri contentURI = data.getData();
        stringImage = getPath(contentURI);
//        stringImage = getStringImage(bm);
    }


    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        image_view.setImageBitmap(thumbnail);
        Uri contentURI = data.getData();
        stringImage = getPath(contentURI);
//        stringImage = getStringImage(thumbnail);
    }

    private void moveFragment(Fragment fragment) {
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }



    public String getPath(Uri uri ) {
        String result = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver( ).query( uri, proj, null, null, null );
        if(cursor != null){
            if ( cursor.moveToFirst( ) ) {
                int column_index = cursor.getColumnIndexOrThrow( proj[0] );
                result = cursor.getString( column_index );
            }
            cursor.close( );
        }
        if(result == null) {
            result = "Not found";
        }
        return result;
    }


//    public String getPath(Uri uri) {
//        String[] projection = {MediaStore.Images.Media.DATA};
//        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
//        if (cursor != null) {            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
//            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
//            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//            cursor.moveToFirst();
//            return cursor.getString(column_index);
//        } else
//            return null;
//    }

    ProgressDialog donut_progress;
    public static final String UPLOAD_URL = "https://loopfusion.in/dawaionline/apis/uploadImage.php";
    File sourceFile;
    int totalSize = 0;

    private class UploadFileToServer extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            sourceFile = new File(stringImage);
            totalSize = (int) sourceFile.length();
            donut_progress = new ProgressDialog(context);
            donut_progress.setCancelable(false);
            donut_progress.setIndeterminate(false);
            donut_progress.setCanceledOnTouchOutside(false);
            donut_progress.show();
        }


        @Override
        protected void onProgressUpdate(String... progress) {
            donut_progress.setProgress(Integer.parseInt(progress[0])); //Updating progress
        }

        @Override
        protected String doInBackground(String... args) {
            int serverResponseCode = 0;

            HttpURLConnection connection;
            DataOutputStream dataOutputStream;
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";


            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1 * 1024 * 1024;
            File selectedFile = new File(stringImage);


            String[] parts = stringImage.split("/");
            final String fileName = parts[parts.length - 1];
            if (!selectedFile.isFile()) {

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                    tvFileName.setText("Source File Doesn't Exist: " + selectedFilePath);
                    }
                });
            } else {
                try {
                    FileInputStream fileInputStream = new FileInputStream(selectedFile);
                    URL url = new URL(UPLOAD_URL);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);//Allow Inputs
                    connection.setDoOutput(true);//Allow Outputs
                    connection.setUseCaches(false);//Don't use a cached Copy
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Connection", "Keep-Alive");
                    connection.setRequestProperty("ENCTYPE", "multipart/form-data");
                    connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                    connection.setRequestProperty("uploaded_file", stringImage);

                    //creating new dataoutputstream
                    dataOutputStream = new DataOutputStream(connection.getOutputStream());

                    //writing bytes to data outputstream
                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                            + stringImage + "\"" + lineEnd);

                    dataOutputStream.writeBytes(lineEnd);

                    //returns no. of bytes present in fileInputStream
                    bytesAvailable = fileInputStream.available();
                    //selecting the buffer size as minimum of available bytes or 1 MB
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    //setting the buffer as byte array of size of bufferSize
                    buffer = new byte[bufferSize];
                    int progress = 0;
                    //reads bytes from FileInputStream(from 0th index of buffer to buffersize)
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                    //loop repeats till bytesRead = -1, i.e., no bytes are left to read
                    while (bytesRead > 0) {
                        //write the bytes read from inputstream
                        dataOutputStream.write(buffer, 0, bufferSize);
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                        progress += 1; // Here progress is total uploaded bytes
                        publishProgress("" + (int) ((progress * 100) / 100)); // sending progress percent to publishProgress
                    }

                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                    serverResponseCode = connection.getResponseCode();
                    String serverResponseMessage = connection.getResponseMessage();
//                Toasty.error(getActivity(), serverResponseMessage).show();
                    Log.i("tg", "Server Response is: " + serverResponseMessage + ": " + serverResponseCode);

                    //response code of 200 indicates the server status OK
                    if (serverResponseCode == 200) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                donut_progress.dismiss();
                                String url = "https://loopfusion.in/dawaionline/apis/images/" + fileName;
                                uploadImage(url);
//                            tvFileName.setText("File Upload completed.\n\n You can see the uploaded file here: \n\n" + "http://coderefer.com/extras/uploads/"+ fileName);
                            }
                        });
                    }

                    //closing the input and output streams
                    fileInputStream.close();
                    dataOutputStream.flush();
                    dataOutputStream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                        Toast.makeText(MainActivity.this,"File Not Found",Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (MalformedURLException e) {
                    e.printStackTrace();
//                Toast.makeText(MainActivity.this, "URL error!", Toast.LENGTH_SHORT).show();

                } catch (IOException e) {
                    e.printStackTrace();
//                Toast.makeText(MainActivity.this, "Cannot Read/Write File!", Toast.LENGTH_SHORT).show();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            donut_progress.dismiss();
            super.onPostExecute(result);
        }
    }

    private void uploadImage(String url) {
            if (Utility.isOnline(context)) {
                BallTriangleDialog triangleDialog = new BallTriangleDialog(context);
                triangleDialog.show();
                ServiceCaller serviceCaller = new ServiceCaller(context);
                serviceCaller.callUploadImageData(sphone, sDocName, url, new IAsyncWorkCompletedCallback() {
                    @Override
                    public void onDone(String workName, boolean isComplete) {
                        triangleDialog.dismiss();
//                    btn_upload_video.setEnabled(false);
                        if (stringImage != null && !stringImage.equalsIgnoreCase("")) {
                            if (isComplete) {
                                if (workName.trim().equalsIgnoreCase("yes")) {
                                    Toast.makeText(context, "Prescription uploaded Sucessfully", Toast.LENGTH_SHORT).show();
                                    edit_docname.setText("");

                                } else {
                                    Toast.makeText(context, "Prescription does not uploaded Sucessfully", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(context, "Please select image", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                Toast.makeText(context, Contants.OFFLINE_MESSAGE, Toast.LENGTH_SHORT).show();
            }
    }


    private boolean Validation() {
        sDocName = edit_docname.getText().toString();
        if (sDocName.length() == 0) {
            edit_docname.setError("Please Enter Doctor Name");
            edit_docname.requestFocus();
            return false;
        } else if (stringImage == null) {
            Toast.makeText(context, "Please Select Image", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}


//    @Override
//    public void onActivityResult(int RC, int RQC, Intent I) {
//
//        super.onActivityResult(RC, RQC, I);
//
//        if (RC == 1 && RQC == RESULT_OK && I != null && I.getData() != null) {
//
//            Uri uri = I.getData();
//
//            try {
//
//                bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
//
//                image_view.setImageBitmap(bitmap);
//                btn_upload.setText("Upload Prescription");
//                image_view.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        if(isImageFitToScreen) {
//                            isImageFitToScreen=false;
////                            image_view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
//                            image_view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
//                            image_view.setAdjustViewBounds(true);
//                        }else{
//                            isImageFitToScreen=true;
//                            image_view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
//                            image_view.setScaleType(ImageView.ScaleType.FIT_XY);
//                            btn_upload.setVisibility(View.GONE);
//                            btn_saved.setVisibility(View.GONE);
//                            img.setVisibility(View.GONE);
//                            tv_title.setVisibility(View.GONE);
//                            txt_addcam.setVisibility(View.GONE);
//                            layout.setBackgroundColor(Color.parseColor("#ffffff"));
//
//                        }
//                    }
//
//                });
//
//            } catch (IOException e) {
//
//                e.printStackTrace();
//            }
//        }
//    }

//    public void ImageUploadToServerFunction() {
//        if (bitmap != null) {
//            ByteArrayOutputStream byteArrayOutputStreamObject;
//
//            byteArrayOutputStreamObject = new ByteArrayOutputStream();
//
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStreamObject);
//
//            byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();
//
//            final String ConvertImage = Base64.encodeToString(byteArrayVar, Base64.DEFAULT);
//
//            class AsyncTaskUploadClass extends AsyncTask<Void, Void, String> {
//
//                @Override
//                protected void onPreExecute() {
//
//                    super.onPreExecute();
//
//                    progressDialog = ProgressDialog.show(context, "Uploading", "Please Wait", false, false);
//                }
//
//                @Override
//                protected void onPostExecute(String string1) {
//
//                    super.onPostExecute(string1);
//
//                    // Dismiss the progress dialog after done uploading.
//                    progressDialog.dismiss();
//                    // Printing uploading success message coming from server on android app.
//                    Toast.makeText(context, string1, Toast.LENGTH_LONG).show();
//
//                    // Setting image as transparent after done uploading.
////                    image_view.setImageResource(android.R.color.transparent);
//                    image_view.setImageResource(R.drawable.ic_menu_camera);
//                    btn_upload.setText("Upload Prescription");
//
//                }
//
//                @Override
//                protected String doInBackground(Void... params) {
//                    ImageProcessClass imageProcessClass = new ImageProcessClass();
//
//                    HashMap<String, String> HashMapParams = new HashMap<String, String>();
//                    HashMapParams.put("loginId", String.valueOf(LoginId));
//                    HashMapParams.put(ImagePath, ConvertImage);
//
//                    String FinalData = imageProcessClass.ImageHttpRequest(url, HashMapParams);
//
//                    return FinalData;
//                }
//            }
//            AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();
//
//            AsyncTaskUploadClassOBJ.execute();
//        } else {
//            Utility.alertForErrorMessage("Please Select Image", context);
//        }
//    }
//
//    public class ImageProcessClass {
//
//        public String ImageHttpRequest(String requestURL, HashMap<String, String> PData) {
//
//            StringBuilder stringBuilder = new StringBuilder();
//
//            try {
//
//                URL url;
//                HttpURLConnection httpURLConnectionObject;
//                OutputStream OutPutStream;
//                BufferedWriter bufferedWriterObject;
//                BufferedReader bufferedReaderObject;
//                int RC;
//
//                url = new URL(requestURL);
//
//                httpURLConnectionObject = (HttpURLConnection) url.openConnection();
//
//                httpURLConnectionObject.setReadTimeout(19000);
//
//                httpURLConnectionObject.setConnectTimeout(19000);
//
//                httpURLConnectionObject.setRequestMethod("POST");
//
//                httpURLConnectionObject.setDoInput(true);
//
//                httpURLConnectionObject.setDoOutput(true);
//
//                OutPutStream = httpURLConnectionObject.getOutputStream();
//
//                bufferedWriterObject = new BufferedWriter(
//
//                        new OutputStreamWriter(OutPutStream, "UTF-8"));
//
//                bufferedWriterObject.write(bufferedWriterDataFN(PData));
//
//                bufferedWriterObject.flush();
//
//                bufferedWriterObject.close();
//
//                OutPutStream.close();
//
//                RC = httpURLConnectionObject.getResponseCode();
//
//                if (RC == HttpsURLConnection.HTTP_OK) {
//
//                    bufferedReaderObject = new BufferedReader(new InputStreamReader(httpURLConnectionObject.getInputStream()));
//
//                    stringBuilder = new StringBuilder();
//
//                    String RC2;
//
//                    while ((RC2 = bufferedReaderObject.readLine()) != null) {
//
//                        stringBuilder.append(RC2);
//                    }
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return stringBuilder.toString();
//        }
//
//        private String bufferedWriterDataFN(HashMap<String, String> HashMapParams) throws UnsupportedEncodingException {
//
//            StringBuilder stringBuilderObject;
//
//            stringBuilderObject = new StringBuilder();
//
//            for (Map.Entry<String, String> KEY : HashMapParams.entrySet()) {
//
//                if (check)
//
//                    check = false;
//                else
//                    stringBuilderObject.append("&");
//
//                stringBuilderObject.append(URLEncoder.encode(KEY.getKey(), "UTF-8"));
//
//                stringBuilderObject.append("=");
//
//                stringBuilderObject.append(URLEncoder.encode(KEY.getValue(), "UTF-8"));
//            }
//
//            return stringBuilderObject.toString();
//        }
//
//    }
//
//    private void showImageChooser() {
//        Intent intent = new Intent();
//        //sets the select file to all types of files
//        intent.setType("image/*");
//        //allows to select data and return it
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        //starts new activity to select file and return data
//        startActivityForResult(Intent.createChooser(intent, "Choose File to Upload.."), 1);
//    }



